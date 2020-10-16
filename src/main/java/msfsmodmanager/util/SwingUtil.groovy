package msfsmodmanager.util

import groovy.transform.CompileStatic
import java.awt.Window

@CompileStatic
class SwingUtil {
    public static void closeWindow(Window window) {
        window.setVisible(false)
        window.dispose()
    }
}

