package msfsmodmanager.ui

import groovy.transform.CompileStatic
import javax.swing.JOptionPane
import msfsmodmanager.state.ModsDb

@CompileStatic
class Dialogs {
    public static boolean updateModsDb() {
        int result = JOptionPane.showConfirmDialog(null, """Would you like to update your mods database via the internet now?

The mods database is used to look up information of mods you haven't registered yet to automate the registration process.""",
            "Welcome to MSFS Mod Selector", JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION
    }
    
    public static void updateModsDbSuccessful() {
        JOptionPane.showMessageDialog(null, """Mods database successfully updated.""",
            "Update successful", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void afterContributedToModsDb() {
        JOptionPane.showInputDialog(null, """Your user-defined mods have been added to "${ModsDb.instance.fileName}".

Please use your preferred Git client to push it to the following central repository file:""", "Thank you.", JOptionPane.INFORMATION_MESSAGE, null, null,
"https://github.com/captn-nick/MSFS-Mod-Repository/blob/master/mods-db.txt")
    }
}

