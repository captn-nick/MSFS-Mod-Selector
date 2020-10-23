package msfsmodmanager.ui.main

import groovy.transform.CompileStatic
import msfsmodmanager.Main

import msfsmodmanager.ui.*
import msfsmodmanager.logic.*
import msfsmodmanager.model.*

@CompileStatic
class MainFrameHandler {
    MainFrame frame;
    
    public MainFrameHandler(MainFrame frame) {
        this.frame = frame
    }
    
    public void apply() {
        Main.init()
        
        Selection selection = SelectableComps.getSelection([frame.eu, frame.uc, frame.ma, frame.sa, frame.as, frame.af, frame.oc, frame.aa, frame.of])
        selection.aircraftTypes = frame.aircraftPanel.selection
        
        if (frame.airportCheckBox.selected) selection.types << ModType.AIRPORT
        if (frame.landmarkCheckBox.selected) selection.types << ModType.LANDMARK
        if (frame.cityCheckBox.selected) selection.types << ModType.CITY
        if (frame.landscapeCheckBox.selected) selection.types << ModType.LANDSCAPE
        if (frame.landscapeFixCheckBox.selected) selection.types << ModType.LANDSCAPE_FIX
        if (frame.livreryCheckBox.selected) selection.types << ModType.LIVRERY
        if (frame.aircraftModelCheckBox.selected) selection.types << ModType.AIRCRAFT_MODEL
        if (frame.otherCheckBox.selected) selection.types << ModType.OTHER
        
        ModActivator.activateMods(selection)
    }
    
    public void deactivateAll() {
        Main.init()
        
        ModActivator.deactivateAllMods();
    }
    
    public void addUserDefinedModsToModsDb() {
        Main.init()
        
        ModsDbHandler.addUserDefinedModsToModsDb()
        Dialogs.afterContributedToModsDb()
    }
}

