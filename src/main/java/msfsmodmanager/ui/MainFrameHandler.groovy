package msfsmodmanager.ui

import groovy.transform.CompileStatic
import msfsmodmanager.Main

import msfsmodmanager.logic.ModActivator
import msfsmodmanager.model.*

@CompileStatic
class MainFrameHandler {
    MainFrame frame;
    
    public MainFrameHandler(MainFrame frame) {
        this.frame = frame
    }
    
    public void apply() {
        Main.init()
        
        Selection selection = SelectableComps.getSelection([frame.eu, frame.us, frame.na, frame.sa, frame.as, frame.af, frame.oz, frame.aa, frame.of])
        
        if (frame.airportCheckBox.selected) selection.types << ModType.AIRPORT
        if (frame.landmarkCheckBox.selected) selection.types << ModType.LANDMARK
        if (frame.cityCheckBox.selected) selection.types << ModType.CITY
        if (frame.landscapeCheckBox.selected) selection.types << ModType.LANDSCAPE
        if (frame.landscapeFixCheckBox.selected) selection.types << ModType.LANDSCAPE_FIX
        if (frame.livreryCheckBox.selected) selection.types << ModType.LIVRERY
        
        ModActivator.activateMods(selection)
    }
    
    public void deactivateAll() {
        Main.init()
        
        ModActivator.deactivateAllMods();
    }
}

