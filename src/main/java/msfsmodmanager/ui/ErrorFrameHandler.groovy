package msfsmodmanager.ui

import groovy.transform.CompileStatic
import msfsmodmanager.Main
import msfsmodmanager.state.Mods

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
        
        frame.setVisible(false)
        Main.restart()
    }
}

