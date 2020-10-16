package msfsmodmanager.logic

import groovy.transform.CompileStatic

import msfsmodmanager.ex.ModsParseException
import msfsmodmanager.util.StringUtil
import msfsmodmanager.ui.ErrorFrame

@CompileStatic
class ErrorHandler {
    public static boolean showErrorsAsPopups = true
    
    public static enum ErrorType {
        DEFAULT,
        DUPLICATE_MODS,
        UNREGISTERED_MODS,
        UNREGISTERED_MODS_IN_DB,
    }
    
    public static void handleGlobalError(Exception ex) {
        handleGlobalError(ex, true)
    }
    
    public static void handleGlobalError(Exception ex, boolean forceShutdown) {
        if (ex in ModsParseException.LineParseException) {
            ModsParseException.LineParseException lpex = (ModsParseException.LineParseException)ex
            
            error (
                "Error.${lpex.errorNo}",
                lpex.info,
                lpex.line,
                lpex.showStackTrace ? StringUtil.toString(lpex) : null,
                forceShutdown
            )
        }
        else {
            error (
                "Error.001",
                "Unexpected error occurred.",
                null,
                StringUtil.toString(ex),
                forceShutdown
            )
        }
    }
    
    public static boolean error(String title, String message, String details, String stackTrace=null,
        ErrorType errorType=ErrorType.DEFAULT) {
        return error(title, message, details, stackTrace, errorType, true)
    }
    
    public static boolean error(String title, String message, String details, String stackTrace=null,
        ErrorType errorType=ErrorType.DEFAULT, boolean forceShutdown) {
        println title
        println "!!! " + message
        if (details) println details
        
        if (stackTrace) {
            println """If you believe this is a bug, please report it on the project's GitHub page (https://github.com/captn-nick/MSFS-Mod-Selector/issues)
with the following information. Make sure to read the README (https://github.com/captn-nick/MSFS-Mod-Selector) first."""
            println stackTrace
        }
        if (showErrorsAsPopups) {
            ErrorFrame.show(title, message, details, stackTrace, errorType, forceShutdown)
        }
        else {
            if (forceShutdown) {
                System.exit(1)
            }
        }
    }
}

