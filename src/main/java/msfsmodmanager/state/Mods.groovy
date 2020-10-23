package msfsmodmanager.state

import groovy.transform.CompileStatic
import groovy.io.FileType
import msfsmodmanager.ex.ModsParseException
import msfsmodmanager.model.*
import msfsmodmanager.util.StringUtil

@CompileStatic
class Mods {
    public static final Mods instance = new Mods()
    
    File modInfoFile
    protected Map<String, Mod> modsByName = [:]
    FirstDefinitions firstDefinitions
    
    public String getFileName() {
        return "mods.txt"
    }
    
    public void loadRegisteredMods() {
        // reset here to not keep anything in memory
        modsByName.clear()
        resetFirstDefinitions()
        
        String text = modInfoFile.getText("UTF-8")
        /*
         * For very unknown reasons, a file might have a bogus character at the very beginning.
         * Also for very unknown reasons, we cannot reliably match its presense / non-presense with regex.
         * Thus, match against all known ModTypes since a ModType's first character should be the first character in mods.txt.
         */
        while (!text.empty && !ModType.any { ModType it -> text.startsWith(it.name()[0]) }) {
            text = text.substring(1)
        }
        
        addAllMods(text.split("\n") as List<String>)
    }
    
    public void addAllMods(List<String> lines) {
        modsByName.putAll(lines.findAll {
            it.trim()
        }.withIndex().collectEntries { String it, int i ->
            i = i+1
            
            Mod mod = null
            try {
                mod = parseLine(it, i)
            }
            catch (Exception ex) {
                throw new ModsParseException.LineParseException(fileName, it, i, ex)
            }

            Mod.CheckErrors checkErrors = mod.checkErrors(fileName)
            mod.checkBasicCorrectness(checkErrors)
            checkRepositoryConsistency(checkErrors, mod, it, i)
            

            return [(mod.name): mod]
        })
    }
    
    public static Mod parseSafeLine(String line) {
        Mod mod
        if (!line.contains("\t")) {
            mod = new Mod(line)
        }
        else {
            mod = parseLine(line, -1)
        }
    }
    
    private static Mod parseLine(String it, int i) {
        it = it.trim()
        String[] line = it.split("##", -1)

        List<String> basic = StringUtil.trimEnd(line[0]).split("\t", -1) as List

        String type = basic[0]
        Mod.Builder builder = Mod.create(basic[3], it, i)
        .type(basic[0])
        .continent(basic[1])
        .country(basic[2])
        .cityOrIcaoOrAircraft(basic.size() > 4 ? basic[4] : null)
        .active(FileSystem.isActive(basic[3]))

        if (line.size() > 1) {
            List<String> more = StringUtil.trimStart(line[1]).split("\t", -1) as List
            builder = builder
            .description(more[0])
            .author(more[1])
            .url(more[2])
        }

        Mod mod = builder.mod()

        return mod
    }
    
    public void addAllMods(Map<String, Mod> newMods, boolean collectErrors=false) {
        // reset here to not keep anything in memory
        resetFirstDefinitions()
        
        mods.each { Mod mod ->
            Mod.CheckErrors checkErrors = mod.checkErrors(fileName)
            checkRepositoryConsistency(checkErrors, mod, mod.line, mod.lineNo)
        }
        
        Map<String, Mod.CheckErrors> checkErrorsByMod = [:]
        newMods.each { String name, Mod mod ->
            try {
                Mod.CheckErrors checkErrors = mod.checkErrors(fileName)
                checkCorrectness(checkErrors, mod)
                checkRepositoryConsistency(checkErrors, mod, mod.line, mod.lineNo)
                checkErrors.throwIfFailure()
            }
            catch(ModsParseException.ModCheckException ex) {
                if (collectErrors) {
                    checkErrorsByMod[ex.checkErrors.mod.name] = ex.checkErrors
                }
                else {
                    ex.checkErrors.throwSubException()
                }
            }
        }
        
        if (!checkErrorsByMod.empty) {
            throw new ModsParseException.MultipleModsCheckException(checkErrorsByMod)
        }
        
        modsByName.putAll(newMods)
    }
    
    protected void checkCorrectness(Mod.CheckErrors checkErrors, Mod mod) {
        mod.checkBasicCorrectness(checkErrors)
    }
    
    protected void checkRepositoryConsistency(Mod.CheckErrors checkErrors, Mod mod, String line, int lineNo) {      
        firstDefinitions.checkDuplicateModNames(checkErrors, line, lineNo, mod)
        firstDefinitions.checkCityCountryDefinitionConsistency(checkErrors, line, lineNo, mod)
        firstDefinitions.checkCountryDefinitionConsistency(checkErrors, line, lineNo, mod)
        firstDefinitions.checkCityContinentDefinitionConsistency(checkErrors, line, lineNo, mod)
    }
    
    public List<String> getModNames() {
        return modsByName.keySet() as List
    }
    
    public List<Mod> getMods() {
        return modsByName.values() as List
    }
    
    public List<File> findAllMods() {
        List<File> ret = []
        
        new File(FileSystem.MOD_DIR).eachFile(FileType.DIRECTORIES) {
            ret << it
        }
        new File(FileSystem.TEMP_DIR).eachFile(FileType.DIRECTORIES) {
            ret << it
        }
        return ret
    }
    
    public Map<String, Mod> getUserDefinedMods() {
        List<String> keys = (getModNames() - ModsDb.instance.getModNames())
        
        return modsByName.subMap(keys)
    }
    
    public void saveToTxt() {
        String text = sortBeforeSaving(mods)*.toTxt().join("\n")
        
        modInfoFile.setText(text, "UTF-8")
    }
    
    protected List<Mod> sortBeforeSaving(List<Mod> mods) {
        return mods
    }
    
    /*
     * For very unknown reasons, this gives a compile error if called like that in a child class.
     * It works, however, when encapsulated in a method.
     */
    protected void resetFirstDefinitions() {
        firstDefinitions = new FirstDefinitions()
    }
    
    protected class FirstDefinitions {
        Map<String, FirstDefinition> modNames = [:]
        Map<String, FirstDefinition> continentByCountry = [:]
        Map<String, FirstDefinition> countryByCity = [:]
        Map<String, FirstDefinition> continentByCity = [:]
        
        protected void checkDuplicateModNames(Mod.CheckErrors checkErrors, String line, int lineNo, Mod mod) {
            if (checkErrors.errorsByField.name.t) return
            
            if (!modNames[mod.name]) {
                modNames[mod.name] = new FirstDefinition(line, lineNo, mod)
            }
            else {
                FirstDefinition firstDefinition = modNames[mod.name]
                checkErrors.errorsByField.name.t = Mod.CheckErrorType.DUPLICATE
            }
        }
        
        protected void checkCountryDefinitionConsistency(Mod.CheckErrors checkErrors, String line, int lineNo, Mod mod) {
            if (checkErrors.errorsByField.country.t) return
            
            if (!mod.country) {
                return
            }
        
            if (!continentByCountry[mod.country]) {
                continentByCountry[mod.country] = new FirstDefinition(line, lineNo, mod)
            }
            else {
                FirstDefinition firstDefinition = continentByCountry[mod.country]
                if (firstDefinition.mod.continent != mod.continent) {
                    checkErrors.errorsByField.continent.t = Mod.CheckErrorType.COUNTRY_TO_CONTINENT_CONSISTENCY
                    checkErrors.errorsByField.continent.payload = firstDefinition
                }
            }
        }
        
        protected void checkCityCountryDefinitionConsistency(Mod.CheckErrors checkErrors, String line, int lineNo, Mod mod) {
            if (checkErrors.errorsByField.city.t) return
            
            if (!mod.city) {
                return
            }

            if (!countryByCity[mod.city]) {
                countryByCity[mod.city] = new FirstDefinition(line, lineNo, mod)
            }
            else {
                FirstDefinition firstDefinition = countryByCity[mod.city]
                if (firstDefinition.mod.country != mod.country) {
                    checkErrors.errorsByField.country.t = Mod.CheckErrorType.CITY_TO_COUNTRY_CONSISTENCY
                    checkErrors.errorsByField.country.payload = firstDefinition
                }
            }
        }

        protected void checkCityContinentDefinitionConsistency(Mod.CheckErrors checkErrors, String line, int lineNo, Mod mod) {
            if (checkErrors.errorsByField.city.t) return
            
            if (!mod.city) {
                return
            }

            if (!continentByCity[mod.city]) {
                continentByCity[mod.city] = new FirstDefinition(line, lineNo, mod)
            }
            else {
                FirstDefinition firstDefinition = continentByCity[mod.city]
                if (firstDefinition.mod.continent != mod.continent) {
                    checkErrors.errorsByField.continent.t = Mod.CheckErrorType.CITY_TO_CONTINENT_CONSISTENCY
                    checkErrors.errorsByField.continent.payload = firstDefinition
                }
            }
        }
    }
}

