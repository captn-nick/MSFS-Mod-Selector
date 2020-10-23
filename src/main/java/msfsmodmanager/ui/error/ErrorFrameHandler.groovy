package msfsmodmanager.ui.error

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
    
    @Deprecated
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
    
    @Deprecated
    public void updateModsDb() {
        throw new UnsupportedOperationException("Use EditModsFrameHandler instead.")
    }
}

