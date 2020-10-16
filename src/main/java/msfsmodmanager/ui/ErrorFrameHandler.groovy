package msfsmodmanager.ui

import groovy.transform.CompileStatic
import msfsmodmanager.Main
import msfsmodmanager.state.Mods
import msfsmodmanager.state.ModsDb
import msfsmodmanager.util.SwingUtil

@CompileStatic
class ErrorFrameHandler {
    ErrorFrame frame
    
    public ErrorFrameHandler(ErrorFrame frame) {
        this.frame = frame
    }
    
    public void autoAddMods() {
        List<String> lines = frame.detailsTextArea.text.split("\n") as List<String>
        lines = lines.findAll { String line ->
            line.contains("\t")
        }
        Mods.instance.addAllMods(lines)
        Mods.instance.saveToTxt()
        
        SwingUtil.closeWindow(frame);
        Main.restart()
    }
    
    public void updateModsDb() {
        SwingUtil.closeWindow(frame);
        
        if (ModsDb.instance.update() {
            Main.restart();
        }) {
            if (!ModsDb.UPDATE_WITH_GIT) {
                Dialogs.updateModsDbSuccessful();
            }
            else {
                return // ModsDb.instance.update() will take control.
            }
        }
        Main.restart();
    }
}

