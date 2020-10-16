package msfsmodmanager.model

import groovy.transform.CompileStatic
import msfsmodmanager.ex.ModsParseException
import msfsmodmanager.state.Config
import msfsmodmanager.ui.I18N

@CompileStatic
class Continent {
    public static Continent EU = new Continent("EU")
    public static Continent UC = new Continent("UC")
    public static Continent MA = new Continent("MA")
    public static Continent SA = new Continent("SA")
    public static Continent AF = new Continent("AF")
    public static Continent AS = new Continent("AS")
    public static Continent OC = new Continent("OC")
    public static Continent AA = new Continent("AA")
    public static Continent OF = new Continent("OF")
    
    public static Map<String, Continent> BY_NAME = [
        EU: EU,
        UC: UC,
        MA: MA,
        SA: SA,
        AF: AF,
        AS: AS,
        OC: OC,
        AA: AA,
        OF: OF,
    ]
    
    public final String abbr
    private List<String> countries = []
    
    Continent(String abbr) {
        this.abbr = abbr
    }
    
    public List<String> getCountries() {
        return countries
    }
    
    public void setCountries(String countries) {
        if (countries) {
            this.countries = countries.split(", ") as List
        }
    }
    
    public static void loadContinents() {
        BY_NAME.each { k, v ->
            v.setCountries(Config.getString("Continent.${k}.Countries"))
        }
    }
    
    public static boolean belongsToAnyContinent(String country) {
        return BY_NAME.any { k, v ->
            country in v.countries
        }
    }
    
    public static String getContinentFor(String country) {
        return I18N.getDefaultStringOrNull("Country.${country}.Continent")
    }
    
    public String toTxt() {
        return abbr
    }
    
    public String toString() {
        return abbr
    }
}

