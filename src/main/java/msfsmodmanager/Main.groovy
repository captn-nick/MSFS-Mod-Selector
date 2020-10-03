package msfsmodmanager

import groovy.transform.CompileStatic
import msfsmodmanager.logic.ModsChecker
import msfsmodmanager.model.*
import msfsmodmanager.state.*
import msfsmodmanager.ui.ErrorFrame
import msfsmodmanager.ui.MainFrame

@CompileStatic
class Main {
    private static boolean showErrorsAsPopups = true
    
    public static void main(String[] args) {
        if ("--dontShowErrorPopups" in args) {
            showErrorsAsPopups = false
        }
        
        if (init()) {
            MainFrame.main()
        }
    }
    
    public static boolean init() {
        FileSystem.init()
        Mods.loadRegisteredMods()
        Continent.loadContinents()
        Cities.loadCities()
        
        List<File> allMods = Mods.findAllMods()
        boolean ret = (
            checkUnregisteredAndCorruptedMods(allMods) &&
            checkDuplicatedMods(allMods) &&
            checkUnregisteredMods(allMods) &&
            checkUninstalledMods() &&
            checkCorruptedMods()
        )
        return ret
    }
    
    private static boolean checkUnregisteredAndCorruptedMods(List<File> allMods) {
        List<File> modFiles = ModsChecker.findUnregisteredAndCorruptedMods(allMods)
        if (!modFiles.empty) {
            return error(
                "!!! Found mods which weren't registered and have a corrupted directory structure:",
                modFiles*.name.join("\n")
            )
        }
        return true
    }
    
    private static boolean checkDuplicatedMods(List<File> allMods) {
        List<String> modNames = ModsChecker.findDuplicatedMods(allMods)
        if (!modNames.empty) {
            return error(
                "!!! Found mods which are duplicated (one in Community, one in Temp folder):",
                modNames.join("\n")
            )
        }
        return true
    }
    
    private static boolean checkUnregisteredMods(List<File> allMods) {
        List<String> modNames = ModsChecker.findUnregisteredMods(allMods)
        if (!modNames.empty) {
            return error(
                "!!! Found unregistered mods:",
                modNames.join("\n")
            )
        }
        return true
    }
    
    private static boolean checkUninstalledMods() {
        List<String> modNames = ModsChecker.findUninstalledMods()
        if (!modNames.empty) {
            return error (
                "!!! Found mods which were registered but aren't present in mod directory:",
                modNames.join("\n")
            )
        }
        return true
    }
    
    private static boolean checkCorruptedMods() {
        List<Mod> mods = ModsChecker.findCorruptedMods()
        if (!mods.empty) {
            return error (
                "!!! Found mods with corrupted directory structure:",
                mods.collect {
                    it.name + (it.active ? "" : " (deactivated)")
                }.join("\n")
            )
        }
        return true
    }
    
    private static boolean error(String message, String details) {
        println message
        println details
        if (showErrorsAsPopups) {
            ErrorFrame.show(message, details)
        }
        else {
            System.exit(0)
        }
    }
}

