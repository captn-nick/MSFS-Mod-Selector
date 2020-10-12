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
    
    public void loadRegisteredMods() {
        // reset here to not keep anything in memory
        modsByName.clear()
        firstDefinitions = new FirstDefinitions()
        
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
    
    public Map<String, Mod> addAllMods(List<String> lines) {
        modsByName.putAll(lines.findAll {
            it.trim()
        }.withIndex().collectEntries { String it, int i ->
            i = i+1
            
            Mod mod = null
            try {
                mod = parseLine(it, i)
            }
            catch (Exception ex) {
                throw new ModsParseException.LineParseException(it, i, ex)
            }

            firstDefinitions.checkDuplicateModNames(it, i, mod)
            firstDefinitions.checkCityCountryDefinitionConsistency(it, i, mod)
            firstDefinitions.checkCountryDefinitionConsistency(it, i, mod)
            firstDefinitions.checkCityContinentDefinitionConsistency(it, i, mod)

            return [(mod.name): mod]
        })
    }
    
    private static Mod parseLine(String it, int i) {
        it = it.trim()
        String[] line = it.split("##", -1)

        List<String> basic = StringUtil.trimEnd(line[0]).split("\t", -1) as List

        String type = basic[0]
        Mod.Builder builder = Mod.name(basic[3])
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

        if (!mod.name) {
            throw new ModsParseException.MissingDataForLineParseException("mod name", it, i)
        }
        if (!mod.type) {
            throw new ModsParseException.MissingDataForLineParseException("mod type", it, i)
        }
        if (basic[1] && !mod.continent) {
            throw new ModsParseException.MissingDataForLineParseException("continent", it, i)
        }
        

        return mod
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
    
    public void saveToTxt() {
        String text = mods*.toTxt().join("\n")
        
        modInfoFile.setText(text, "UTF-8")
    }
    
    protected static class FirstDefinitions {
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
                throw new ModsParseException.DuplicateModNameForLineParseException(firstDefinition, line, lineNo)  
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
                    throw new ModsParseException.ConflictingDataForLineParseException("continent", firstDefinition, line, lineNo)
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
                    throw new ModsParseException.ConflictingDataForLineParseException("country", firstDefinition, line, lineNo)
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
                    throw new ModsParseException.ConflictingDataForLineParseException("continent", firstDefinition, line, lineNo)
                }
            }
        }
    }
}

