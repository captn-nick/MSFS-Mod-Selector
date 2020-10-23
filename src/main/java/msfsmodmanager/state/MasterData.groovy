package msfsmodmanager.state

import groovy.transform.CompileStatic
import msfsmodmanager.model.Continent
import msfsmodmanager.ui.I18N

@CompileStatic
class MasterData {
    public static Set<String> allAuthors
    public static Map<String, String> allCountriesByAbbr
    public static Map<String, String> allCountriesByName
    public static Map<String, Set<String>> allCitiesByCountry = [:].withDefault { new TreeSet<String>(String.CASE_INSENSITIVE_ORDER) }
    public static Map<Continent, Set<String>> allCitiesByContinent = [:].withDefault { new TreeSet<String>(String.CASE_INSENSITIVE_ORDER) }
    public static Set<String> allCitiesWithoutContinent
    
    public static void init() {
        Set<String> temp = (Mods.instance.mods.collect { it.author } +
            ModsDb.instance.mods.collect { it.author } ).findAll { it } as Set
        allAuthors = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER)
        allAuthors.addAll(temp)
        
        allCountriesByAbbr = I18N.getAllKeysMatching(/Country\.[A-Za-z]{2,3}/).collectEntries { String key ->
            String abbr = key - "Country."
            String name = I18N.getString(key)
            if (Continent.getContinentFor(abbr) == "UC") {
                if (key != "Country.CAN") {
                    name += " (US)"
                }
            }
            return [(abbr): name]
        }
        
        allCountriesByName = I18N.getAllKeysMatching(/Country\.[A-Za-z]{2,3}/).collectEntries { String key ->
            String abbr = key - "Country."
            String name = I18N.getString(key)
            if (Continent.getContinentFor(abbr) == "UC") {
                if (key != "Country.CAN") {
                    name += " (US)"
                }
            }
            return [(name): abbr]
        }
        
        Map<String, String> countriesByCity = 
            (Mods.instance.mods.collectEntries { [(it.city): it.country] } +
            ModsDb.instance.mods.collectEntries { [(it.city): it.country] } )
        countriesByCity.each { city, country ->
            if (city) allCitiesByCountry[country] << city
        }
        
        Map<String, Continent> continentsByCity = 
            (Mods.instance.mods.collectEntries { [(it.city): it.country ? null : it.continent] } +
            ModsDb.instance.mods.collectEntries { [(it.city): it.country ? null : it.continent] } )
        continentsByCity.each { city, continent ->
            if (city) allCitiesByContinent[continent] << city
        }
        
        temp = ((Mods.instance.mods.collect { it.city } +
            ModsDb.instance.mods.collect { it.city } ).findAll { it } as Set) -
            countriesByCity.findAll { it.value }.keySet() - continentsByCity.findAll { it.value }.keySet()
        
        allCitiesWithoutContinent = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER)
        allCitiesWithoutContinent.addAll(temp)
    }
}

