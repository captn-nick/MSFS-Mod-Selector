package msfsmodmanager.model

import groovy.transform.CompileStatic
import msfsmodmanager.state.Mods

@CompileStatic
class Cities {
    public static Map<String, Set<String>> BY_COUNTRY = [:].withDefault {new TreeSet<String>()}
    public static Map<String, Set<String>> BY_CONTINENT = [:].withDefault {new TreeSet<String>()}
    
    public static loadCities() {
        Mods.mods.each {
            if (it.city) {
                if (it.country && Continent.belongsToAnyContinent(it.country)) {
                    BY_COUNTRY[it.country] << it.city
                }
                else if (it.continent) {
                    ((Set<String>)BY_CONTINENT[it.continent]) << it.city
                }
            }
        }
    }
}

