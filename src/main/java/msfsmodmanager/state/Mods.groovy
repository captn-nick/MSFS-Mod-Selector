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
    
    protected String getFileName() {
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

            mod.checkBasicCorrectness(fileName)
            checkRepositoryConsistency(mod, it, i)

            return [(mod.name): mod]
        })
    }
    
    private Mod parseLine(String it, int i) {
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
    
    protected void checkRepositoryConsistency(Mod mod, String line, int lineNo) {      
        firstDefinitions.checkDuplicateModNames(line, lineNo, mod)
        firstDefinitions.checkCityCountryDefinitionConsistency(line, lineNo, mod)
        firstDefinitions.checkCountryDefinitionConsistency(line, lineNo, mod)
        firstDefinitions.checkCityContinentDefinitionConsistency(line, lineNo, mod)
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
        
        protected void checkDuplicateModNames(String line, int lineNo, Mod mod) {
            if (!modNames[mod.name]) {
                modNames[mod.name] = new FirstDefinition(line, lineNo, mod)
            }
            else {
                FirstDefinition firstDefinition = modNames[mod.name]
                throw new ModsParseException.DuplicateModNameForLineParseException(Mods.this.fileName, firstDefinition, line, lineNo)  
            }
        }
        
        protected void checkCountryDefinitionConsistency(String line, int lineNo, Mod mod) {
            if (!mod.country) {
                return
            }
        
            if (!continentByCountry[mod.country]) {
                continentByCountry[mod.country] = new FirstDefinition(line, lineNo, mod)
            }
            else {
                FirstDefinition firstDefinition = continentByCountry[mod.country]
                if (firstDefinition.mod.continent != mod.continent) {
                    throw new ModsParseException.ConflictingDataForLineParseException(Mods.this.fileName, "continent", firstDefinition, line, lineNo)
                }
            }
        }
        
        protected void checkCityCountryDefinitionConsistency(String line, int lineNo, Mod mod) {
            if (!mod.city) {
                return
            }

            if (!countryByCity[mod.city]) {
                countryByCity[mod.city] = new FirstDefinition(line, lineNo, mod)
            }
            else {
                FirstDefinition firstDefinition = countryByCity[mod.city]
                if (firstDefinition.mod.country != mod.country) {
                    throw new ModsParseException.ConflictingDataForLineParseException(Mods.this.fileName, "country", firstDefinition, line, lineNo)
                }
            }
        }

        protected void checkCityContinentDefinitionConsistency(String line, int lineNo, Mod mod) {
            if (!mod.city) {
                return
            }

            if (!continentByCity[mod.city]) {
                continentByCity[mod.city] = new FirstDefinition(line, lineNo, mod)
            }
            else {
                FirstDefinition firstDefinition = continentByCity[mod.city]
                if (firstDefinition.mod.continent != mod.continent) {
                    throw new ModsParseException.ConflictingDataForLineParseException(Mods.this.fileName, "continent", firstDefinition, line, lineNo)
                }
            }
        }
    }
}

