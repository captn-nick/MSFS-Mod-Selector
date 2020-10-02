package msfsmodmanager.model

import groovy.transform.CompileStatic
import groovy.transform.ToString

@CompileStatic
@ToString
class Selection {
    List<Continent> continents
    List<String> countries = []
    List<String> cities = []
    
    List<ModType> types = []
    
    public Selection(List<String> continentNames) {
        continents = continentNames.collect { Continent.BY_NAME[it] }
    }
    
    public boolean activates(Mod mod) {
        if (!(mod.type in types)) {
            return false
        }
        
        if (mod.city in cities) {
            return true
        }
        else if (mod.city == null) {
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

