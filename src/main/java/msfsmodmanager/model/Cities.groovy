package msfsmodmanager.model

import groovy.transform.CompileStatic
import msfsmodmanager.state.Mods
import msfsmodmanager.state.Config
import msfsmodmanager.ex.ModsParseException

@CompileStatic
class Cities {
    private static Map<String, Set<String>> BY_COUNTRY = [:].withDefault {new TreeSet<String>()}
    private static Map<Continent, Set<String>> BY_CONTINENT = [:].withDefault {new TreeSet<String>()}
    public static Set<String> DISABLED_CITIES = [] as Set
    
    public static Set<String> byCountry(String country) {
        return BY_COUNTRY[country] - DISABLED_CITIES
    }
    
    public static Set<String> byContinent(Continent continent) {
        return BY_CONTINENT[continent] - DISABLED_CITIES
    }
    
    public static loadCities() {
        Map<String, Integer> numberOfMods = [:].withDefault { 0 }
        
        Mods.instance.mods.each {
            if (it.city) {
                numberOfMods[it.city] = numberOfMods[it.city] + 1
                
                if (it.country && Continent.belongsToAnyContinent(it.country)) {
                    if (showCitiesFor(it.country)) {
                        BY_COUNTRY[it.country] << it.city
                    }
                    else {
                        DISABLED_CITIES << it.city
                    }
                }
                else if (it.continent) {
                    BY_CONTINENT[it.continent] << it.city
                }
            }
        }
        
        numberOfMods.each { k, v ->
            if (!showCitiesFor(v)) {
                DISABLED_CITIES << k
            }
        }
    }
    
    private static showCitiesFor(String country) {
        String ret = Config.getStringOrNull("Country.${country}.showCities")
        return ret == null || ret == "true"
    }
    
    private static boolean showCitiesFor(int occurrences) {
        return occurrences >= (Config.getString("Cities.minMods") as int)
    }
}

