package msfsmodmanager

import groovy.transform.CompileStatic
import msfsmodmanager.ex.ModsParseException
import msfsmodmanager.logic.ErrorHandler
import msfsmodmanager.logic.ModChecker
import msfsmodmanager.model.*
import msfsmodmanager.state.*
import msfsmodmanager.ui.MainFrame

@CompileStatic
class Main {
    public static void main(String[] args) {
        try {
            start(args)
        }
        catch(Exception ex) {
            ErrorHandler.handleGlobalError(ex)
        }
    }
    
    private static void start(String[] args) {
        if ("--dontShowErrorPopups" in args) {
            ErrorHandler.showErrorsAsPopups = false
        }
        
        restart()
    }
    
    public static void restart() {
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
        List<File> modFiles = ModChecker.findUnregisteredAndCorruptedMods(allMods)
        if (!modFiles.empty) {
            return ErrorHandler.error(
                "Error.010",
                "Found mods which weren't registered and have a corrupted directory structure:",
                modFiles*.name.join("\n")
            )
        }
        return true
    }
    
    private static boolean checkDuplicatedMods(List<File> allMods) {
        List<String> modNames = ModChecker.findDuplicatedMods(allMods)
        if (!modNames.empty) {
            return ErrorHandler.error(
                "Error.020",
                "Found mods which are duplicated (one in Community, one in Temp folder):",
                modNames.join("\n"),
                null,
                ErrorHandler.ErrorType.DUPLICATE_MODS
            )
        }
        return true
    }
    
    private static boolean checkUnregisteredMods(List<File> allMods) {
        List<String> modNames = ModChecker.findUnregisteredMods(allMods)
        if (!modNames.empty) {
            return ErrorHandler.error(
                "Error.030",
                "Found unregistered mods:",
                modNames.join("\n")
            )
        }
        return true
    }
    
    private static boolean checkUninstalledMods() {
        List<String> modNames = ModChecker.findUninstalledMods()
        if (!modNames.empty) {
            return ErrorHandler.error (
                "Error.040",
                "Found mods which were registered but aren't present in mod directory:",
                modNames.join("\n")
            )
        }
        return true
    }
    
    private static boolean checkCorruptedMods() {
        List<Mod> mods = ModChecker.findCorruptedMods()
        if (!mods.empty) {
            return ErrorHandler.error (
                "Error.050",
                "Found mods with corrupted directory structure:",
                mods.collect {
                    it.name + (it.active ? "" : " (deactivated)")
                }.join("\n")
            )
        }
        return true
    }
}

