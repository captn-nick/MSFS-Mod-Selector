package msfsmodmanager.ui.edit

import javax.swing.JComponent
import groovy.transform.CompileStatic
import msfsmodmanager.Main
import msfsmodmanager.ex.ModsParseException
import msfsmodmanager.model.Mod
import msfsmodmanager.model.ModType
import msfsmodmanager.model.Continent
import msfsmodmanager.model.AircraftType
import msfsmodmanager.state.*
import msfsmodmanager.ui.*
import msfsmodmanager.util.SwingUtil

@CompileStatic
class EditModsFrameHandler {
    EditModsFrame frame
    
    Mod mod
    
    public final String[] modNames
    private final Map<String, Mod> mods
    public final Set<String> allAuthors
    public final Set<ModType> allModTypes
    public final Set<Continent> allContinents
    public final Set<String> allCountries
    public final Map<String, Set<String>> allCitiesByCountry
    public final Map<Continent, Set<String>> allCitiesByContinent
    public final Set<String> allCitiesWithoutContinent
    public final Set<AircraftType> allAircraftTypes
    
    public boolean changesModSelection
    
    public Map<String, Mod.CheckErrors> checkErrorsByMod = [:]
    
    private Set<MessagePanel> messagePanels
    
    public EditModsFrameHandler(EditModsFrame frame, List<Mod> mods) {
        this.frame = frame
        
        this.mods = mods.collectEntries {
            [(it.name): it]
        }
        modNames = mods*.name as String[]
        allAuthors = MasterData.allAuthors
        allModTypes = ModType.values() as TreeSet // TreeSet keeps insert order for enum values
        allContinents = Continent.BY_NAME.values() as Set
        
        allCountries = MasterData.allCountriesByName.entrySet().sort { it ->
            // sort by translated label name, but all U.S. states (except Canada) at the end of the list
            // it = the entry. Note that the map is flipped: key = the label, value = "Country.$code"
            if (Continent.getContinentFor(it.value) == "UC" && it.value != "CAN") {
                return it.key
            }
            else {
                return "\t" + it.key
            }
        }*.key as LinkedHashSet // LinkedHashSet to preserve insertion / sort order
        
        allCitiesByCountry = MasterData.allCitiesByCountry
        allCitiesByContinent = MasterData.allCitiesByContinent
        allCitiesWithoutContinent = MasterData.allCitiesWithoutContinent
        
        allAircraftTypes = AircraftType.values() as TreeSet // TreeSet keeps insert order for enum values
    }
    
    public void initFrame() {
        messagePanels = [
            frame.jPanel1Msgs,
            frame.jPanel2Msgs,
            frame.jPanel3Msgs,
            frame.jPanel4Msgs,
            frame.jPanel5Msgs,
        ] as Set
    
        messagePanels.each {
            it.parentComponent = (JComponent)(it.parent)
            it.previousComponent = SwingUtil.getPreviousComponent(it)
        }
        
        allAuthors.each { frame.authorComboBox.addItem(it) }
        allModTypes.each { frame.typeComboBox.addItem(it) }
        allContinents.each { frame.continentComboBox.addItem(it) }
        allCountries.each { frame.countryComboBox.addItem(it) }
        allAircraftTypes.each { frame.aircraftTypeComboBox.addItem(it) }
        
        setMod(modNames[0])
        
        updateMessagePanels()
    }
    
    public void setMod(String modName) {
        mod = mods[modName]
        load()
    }
    
    private void load() {
        changesModSelection = true
        
        frame.urlTextField.text = mod.url
        frame.descriptionTextField.text = mod.description
        frame.authorComboBox.text = mod.author
        
        frame.typeComboBox.text = mod.type
        frame.continentComboBox.text = mod.continent
        frame.countryComboBox.text = MasterData.allCountriesByAbbr[mod.country]
        
        frame.cityComboBox.text = mod.city
        frame.icaoTextField.text = mod.icao
        frame.aircraftTypeComboBox.text = mod.aircraftType
        
        changeType()
        
        changesModSelection = false
    }
    
    public void save() {
        mod.url = frame.urlTextField.text
        mod.description = frame.descriptionTextField.text
        mod.author = frame.authorComboBox.text
        
        mod.type = frame.typeComboBox.text
        mod.continent = frame.continentComboBox.text
        mod.country = MasterData.allCountriesByName[frame.countryComboBox.text]
        
        mod.city = frame.cityComboBox.text
        mod.icao = frame.icaoTextField.text
        mod.aircraftType = frame.aircraftTypeComboBox.text
    }
    
    public void changeType() {
        switch (frame.typeComboBox.text) {
            case [ModType.AIRPORT]:
                frame.jPanel5.remove(frame.cityPanel)
                frame.jPanel5.add(frame.icaoPanel)
                frame.jPanel5.remove(frame.aircraftTypePanel)
                
                frame.jPanel5Msgs.remove(frame.cityMsgLabel)
                frame.jPanel5Msgs.add(frame.icaoMsgLabel)
                frame.jPanel5Msgs.remove(frame.aircraftTypeMsgLabel)
            break
            case [ModType.LANDMARK, ModType.CITY, ModType.LANDSCAPE, ModType.LANDSCAPE_FIX]:
                frame.jPanel5.add(frame.cityPanel)
                frame.jPanel5.remove(frame.icaoPanel)
                frame.jPanel5.remove(frame.aircraftTypePanel)
                
                frame.jPanel5Msgs.add(frame.cityMsgLabel)
                frame.jPanel5Msgs.remove(frame.icaoMsgLabel)
                frame.jPanel5Msgs.remove(frame.aircraftTypeMsgLabel)
            break
            case [ModType.LIVRERY, ModType.AIRCRAFT_MODEL]:
                frame.jPanel5.remove(frame.cityPanel)
                frame.jPanel5.remove(frame.icaoPanel)
                frame.jPanel5.add(frame.aircraftTypePanel)
                
                frame.jPanel5Msgs.remove(frame.cityMsgLabel)
                frame.jPanel5Msgs.remove(frame.icaoMsgLabel)
                frame.jPanel5Msgs.add(frame.aircraftTypeMsgLabel)
            break
            default:
                frame.jPanel5.remove(frame.cityPanel)
                frame.jPanel5.remove(frame.icaoPanel)
                frame.jPanel5.remove(frame.aircraftTypePanel)
                
                frame.jPanel5Msgs.remove(frame.cityMsgLabel)
                frame.jPanel5Msgs.remove(frame.icaoMsgLabel)
                frame.jPanel5Msgs.remove(frame.aircraftTypeMsgLabel)
        }
        frame.revalidate()
        frame.repaint()
    }
    
    public void changeCountry() {
        if (frame.countryComboBox.text == null) {
            changeContinent()
            return
        }
        
        String abbr = MasterData.allCountriesByName[frame.countryComboBox.text]
        
        // Don't change continent as a result of switching to another mod for editing
        if (!changesModSelection) {
            frame.continentComboBox.text = Continent.BY_NAME[Continent.getContinentFor(abbr)]
        }
        
        frame.cityComboBox.removeAllItems()
        allCitiesByCountry[abbr].each { frame.cityComboBox.addItem(it) }
    }
    
    public void changeContinent() {
        if (frame.countryComboBox.text == null) {
            frame.cityComboBox.removeAllItems()
            if (frame.continentComboBox.text == null) {
                allCitiesWithoutContinent.each { frame.cityComboBox.addItem(it) }
                return
            }
            
            allCitiesByContinent[frame.continentComboBox.text].each { frame.cityComboBox.addItem(it) }
        }
    }
    
    public void addAll() {
        mods.values().each { Mod mod ->
            switch (mod.type) {
                case [ModType.AIRPORT]:
                    mod.city = null
                    mod.aircraftType = null
                break
                case [ModType.LANDMARK, ModType.CITY, ModType.LANDSCAPE, ModType.LANDSCAPE_FIX]:
                    mod.icao = null
                    mod.aircraftType = null
                break
                case [ModType.LIVRERY, ModType.AIRCRAFT_MODEL]:
                    mod.city = null
                    mod.icao = null
                break
                default:
                    mod.city = null
                    mod.icao = null
                    mod.aircraftType = null
            }
        }
        
        try {
            Mods.instance.addAllMods(mods, true)
        }
        catch (ModsParseException.MultipleModsCheckException ex) {
            checkErrorsByMod = ex.checkErrorsByMod
            updateMessagePanels()
            changeType()
            frame.revalidate()
            frame.repaint()
            return
        }
        
        Mods.instance.saveToTxt()
        
        SwingUtil.closeWindow(frame);
        Main.restart();
    }
    
    /*
     * We would like to have linked messagePanel's visibilities dynamically to the respective checkErrors
     * (as we do dynamic linking in MessageLabel and WarnLightLabel), but this doesn't work because
     * (a) we want to actually remove / add components completely (to also erase the space occupied by them)
     * which cannot be done in this "dymnamic" fashion and (b) even if we used the #visible flag instead of
     * actually removing / adding them, even a revalidate() / repaint() wouldn't actually properly show them
     * since these methods don't properly update the UI in case of shifting components around.
     */
    public void updateMessagePanels() {
        messagePanels.each {
            if (it.parent != null) {
                it.parent.remove(it)
            }
            if (isVisible(it)) {
                it.addBack()
            }
        }
    }
    
    private boolean isVisible(MessagePanel panel) {
        Mod.CheckErrors checkErrors = getCurrentCheckErrors()
        
        if (checkErrors == null) return false
        
        return panel.properties.any { checkErrors.getErrorsByField().containsKey(it) }
    }
    
    public Mod.CheckErrors getCurrentCheckErrors() {
        if (mod == null) return null
        
        return checkErrorsByMod[mod.name]
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

