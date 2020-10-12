package msfsmodmanager.ui

import groovy.transform.CompileStatic
import javax.swing.JOptionPane

@CompileStatic
class Dialogs {
    public static boolean updateModsDb() {
        int result = JOptionPane.showConfirmDialog(null, """Would you like to update your mods database via the internet now?

The mods database is used to look up information of mods you haven't registered yet to automate the registration process.""",
            "Welcome to MSFS Mod Selector", JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION
    }
}

