package msfsmodmanager.state

import groovy.transform.CompileStatic
import groovy.io.FileType
import msfsmodmanager.model.Mod

@CompileStatic
class Mods {
    public static File MOD_INFO_FILE
    
    static List<Mod> mods
    
    public static void loadRegisteredMods() {
        mods = MOD_INFO_FILE.getText("UTF-8").split("\n").findAll {
            it.trim()
        }.collect { String it -> 
            it = it.trim()
            String[] line = it.split("\t")
            new Mod(line[3], line[0], line[1], line[2], line.size() > 4 ? line[4] : null, FileSystem.isActive(line[3]))
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

