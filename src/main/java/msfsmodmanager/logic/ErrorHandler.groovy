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
    }
    
    public static void handleGlobalError(Exception ex) {
        switch (ex) {
            case ModsParseException.LineParseException:
                ModsParseException.LineParseException lpex = (ModsParseException.LineParseException)ex
                if (ex.cause in ModsParseException.MissingDataForLineParseException) {
                    ModsParseException.MissingDataForLineParseException cause = (ModsParseException.MissingDataForLineParseException)lpex.cause
                    error (
                        "Error.101",
                        cause.info,
                        cause.line,
                        StringUtil.toString(ex)
                    )
                }
                else {
                    error (
                        "Error.100",
                        lpex.info,
                        lpex.line,
                        StringUtil.toString(lpex)
                    )
                }

            break
            default:
                error (
                    "Error.001",
                    "Unexpected error occurred.",
                    null,
                    StringUtil.toString(ex)
                )
            
            break
        }
        
    }
    
    public static boolean error(String title, String message, String details, String stackTrace=null, ErrorType errorType=ErrorType.DEFAULT) {
        println title
        println "!!! " + message
        if (details) println details
        
        if (stackTrace) {
            println """If you believe this is a bug, please report it on the project's GitHub page (https://github.com/captn-nick/MSFS-Mod-Selector/issues)
with the following information. Make sure to read the README (https://github.com/captn-nick/MSFS-Mod-Selector) first."""
            println stackTrace
        }
        if (showErrorsAsPopups) {
            ErrorFrame.show(title, message, details, stackTrace, errorType)
        }
        else {
            System.exit(1)
        }
    }
}

