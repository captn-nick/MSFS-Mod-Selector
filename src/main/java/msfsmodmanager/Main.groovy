package msfsmodmanager

import groovy.transform.CompileStatic
import msfsmodmanager.ex.ModsParseException
import msfsmodmanager.logic.ErrorHandler
import msfsmodmanager.logic.ModChecker
import msfsmodmanager.model.*
import msfsmodmanager.state.*
import msfsmodmanager.ui.Dialogs
import msfsmodmanager.ui.MainFrame
import msfsmodmanager.util.CmdLine
import msfsmodmanager.util.WebReader

@CompileStatic
class Main {
    public static boolean FIRST_START = false
    
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
        
        FIRST_START = false
    }
    
    public static boolean init() {
        FileSystem.init()
        
        if (FIRST_START) {
            if (Dialogs.updateModsDb()) {
                FIRST_START = false
                if (ModsDb.instance.update() {
                        restart()
                }) {
                    if (!ModsDb.UPDATE_WITH_GIT) {
                        Dialogs.updateModsDbSuccessful()
                    }
                    else {
                        return // ModsDb.instance.update() will take control.
                    }
                }
                else {
                    return
                }
            }
        }
        
        Mods.instance.loadRegisteredMods()
        ModsDb.instance.loadRegisteredMods()
        
        Continent.loadContinents()
        Cities.loadCities()
        
        List<File> allMods = Mods.instance.findAllMods()
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
        ModsDb.DbInformationFound informationFromDb = ModsDb.instance.replaceWithDbInformation(modNames)
        
        if (!modNames.empty) {
            return ErrorHandler.error(
                "Error.030",
                "Found unregistered mods:",
                informationFromDb.lines.join("\n"),
                null,
                informationFromDb.foundAny ? ErrorHandler.ErrorType.UNREGISTERED_MODS_IN_DB : ErrorHandler.ErrorType.UNREGISTERED_MODS
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

