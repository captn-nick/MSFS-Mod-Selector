package msfsmodmanager

import groovy.transform.CompileStatic
import msfsmodmanager.logic.ModsChecker
import msfsmodmanager.model.*
import msfsmodmanager.state.*
import msfsmodmanager.ui.MainFrame

@CompileStatic
class Main {
    public static void main(String[] args) {
        init()
        MainFrame.main()
    }
    
    public static void init() {
        FileSystem.init()
        Mods.loadRegisteredMods()
        Continent.loadContinents()
        Cities.loadCities()
        
        List<File> allMods = Mods.findAllMods()
        checkUnregisteredAndCorruptedMods(allMods)
        checkDuplicatedMods(allMods)
        checkUnregisteredMods(allMods)
        checkUninstalledMods()
        checkCorruptedMods()
    }
    
    private static void checkUnregisteredAndCorruptedMods(List<File> allMods) {
        List<File> modFiles = ModsChecker.findUnregisteredAndCorruptedMods(allMods)
        if (!modFiles.empty) {
            println "!!! Found mods which weren't registered and have a corrupted directory structure:"
            println modFiles*.name.join("\n")
            System.exit(1)
        }
    }
    
    private static void checkDuplicatedMods(List<File> allMods) {
        List<String> modNames = ModsChecker.findDuplicatedMods(allMods)
        if (!modNames.empty) {
            println "!!! Found mods which are duplicated (one in Community, one in Temp folder):"
            println modNames.join("\n")
            System.exit(1)
        }
    }
    
    private static void checkUnregisteredMods(List<File> allMods) {
        List<String> modNames = ModsChecker.findUnregisteredMods(allMods)
        if (!modNames.empty) {
            println "!!! Found unregistered mods:"
            println modNames.join("\n")
            System.exit(1)
        }
    }
    
    private static void checkUninstalledMods() {
        List<String> modNames = ModsChecker.findUninstalledMods()
        if (!modNames.empty) {
            println "!!! Found mods which were registered but aren't present in mod directory:"
            println modNames.join("\n")
            System.exit(1)
        }
    }
    
    private static void checkCorruptedMods() {
        List<Mod> mods = ModsChecker.findCorruptedMods()
        if (!mods.empty) {
            println "!!! Found mods with corrupted directory structure:"
            println mods.collect {
                it.name + (it.active ? "" : " (deactivated)")
            }.join("\n")
            System.exit(1)
        }
    }
}

