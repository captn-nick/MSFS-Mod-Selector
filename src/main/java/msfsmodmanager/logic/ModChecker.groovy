package msfsmodmanager.logic

import groovy.transform.CompileStatic
import msfsmodmanager.model.Mod
import msfsmodmanager.state.Mods

@CompileStatic
class ModChecker {
    public static List<File> findUnregisteredAndCorruptedMods(List<File> allMods) {
        return allMods.findAll {
            !(new File(it.path + /\manifest.json/).exists())
        }
    }
    
    public static List<String> findDuplicatedMods(List<File> allMods) {
        return allMods.findAll { mod ->
            allMods.findAll{ it.name == mod.name }.size() == 2
        }*.name.unique()
    }
    
    public static List<String> findUnregisteredMods(List<File> allMods) {
        return allMods.findAll {
            !(it.name in Mods.instance.mods*.name)
        }*.name
    }
    
    public static List<String> findUninstalledMods() {
        return Mods.instance.mods.findAll {
            !(new File(it.file.path).exists())
        }*.name
    }
    
    public static List<Mod> findCorruptedMods() {
        return Mods.instance.mods.findAll {
            !(new File(it.file.path + /\manifest.json/).exists())
        }
    }
}

