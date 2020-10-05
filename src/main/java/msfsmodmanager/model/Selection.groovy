package msfsmodmanager.model

import groovy.transform.CompileStatic
import groovy.transform.ToString

@CompileStatic
@ToString
class Selection {
    List<ModType> types = []
    
    List<Continent> continents
    List<String> countries = []
    List<String> cities = []
    
    List<AircraftType> aircraftTypes = []
    
    public Selection(List<String> continentNames) {
        continents = continentNames.collect { Continent.BY_NAME[it] }
    }
    
    public boolean activates(Mod mod) {
        if (!(mod.type in types)) {
            return false
        }
        
        if (mod.type in [ModType.LIVRERY, ModType.AIRCRAFT_MODEL]) {
            if (!mod.aircraftType || mod.aircraftType in aircraftTypes) {
                return true
            }
            else {
                return false
            }
        }
        
        if (mod.city in cities) {
            return true
        }
        else if (mod.city == null || mod.city in Cities.DISABLED_CITIES) {
            if (mod.country in countries) {
                return true
            }
            else if (mod.continent == null) {
                return true
            }
            else if (!(mod.country in mod.continent.countries)) {
                if (mod.continent in continents) {
                    return true
                }
            }
        }
        
        return false
    }
}

