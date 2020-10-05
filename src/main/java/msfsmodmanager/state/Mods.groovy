package msfsmodmanager.state

import groovy.transform.CompileStatic
import groovy.io.FileType
import msfsmodmanager.model.*
import msfsmodmanager.util.StringUtil

@CompileStatic
class Mods {
    public static File MOD_INFO_FILE
    
    static List<Mod> mods
    
    public static void loadRegisteredMods() {
        String text = MOD_INFO_FILE.getText("UTF-8")
        /*
         * For very unknown reasons, a file might have a bogus character at the very beginning.
         * Also for very unknown reasons, we cannot reliably match its presense / non-presense with regex.
         * Thus, match against all known ModTypes since a ModType's first character should be the first character in mods.txt.
         */
        if (!text.empty && !ModType.any { ModType it -> text.startsWith(it.name()[0]) }) {
            text = text.substring(1)
        }
        
        mods = text.split("\n").findAll {
            it.trim()
        }.collect { String it -> 
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
            
            if (!mod.type) {
                throw new RuntimeException("No mod type defined for $line.")
            }
            
            return mod
        }
    }
    
    public static List<File> findAllMods() {
        List<File> ret = []
        
        new File(FileSystem.MOD_DIR).eachFile(FileType.DIRECTORIES) {
            ret << it
        }
        new File(FileSystem.TEMP_DIR).eachFile(FileType.DIRECTORIES) {
            ret << it
        }
        return ret
    }
}

