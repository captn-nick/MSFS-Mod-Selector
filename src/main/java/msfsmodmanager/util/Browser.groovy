package msfsmodmanager.util

import groovy.transform.CompileStatic
import java.awt.Desktop

@CompileStatic
class Browser {
    public static void openWebpage(String url) {
        Desktop.getDesktop().browse(new URL(url).toURI());
    }
}

