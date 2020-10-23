package msfsmodmanager.util

import groovy.transform.CompileStatic
import msfsmodmanager.ui.git.GitFrame

@CompileStatic
class Git {
    public static boolean pull(String filePath, Closure okButtonAction) {
        CmdLine.Result result = CmdLine.execute($/cmd.exe /c "cd "/$ + filePath + /" && git pull"/)
        
        if (result.serr) {
            info(true, result.serr, okButtonAction)
            return false
        }
        else {
            info(false, result.sout, okButtonAction)
            return true
        }
    }
    
    private static void info(boolean error, String out, Closure okButtonAction) {
        GitFrame.show(error, out, okButtonAction)
    }
}

