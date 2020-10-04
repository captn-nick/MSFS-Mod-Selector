package msfsmodmanager.model

import groovy.transform.CompileStatic
import msfsmodmanager.ui.I18N

@CompileStatic
class Continent {
    public static Continent EU = new Continent("EU")
    public static Continent US = new Continent("US")
    public static Continent NA = new Continent("NA")
    public static Continent SA = new Continent("SA")
    public static Continent AF = new Continent("AF")
    public static Continent AS = new Continent("AS")
    public static Continent OZ = new Continent("OZ")
    public static Continent AA = new Continent("AA")
    public static Continent OF = new Continent("OF")
    
    public static Map<String, Continent> BY_NAME = [
        EU: EU,
        US: US,
        NA: NA,
        SA: SA,
        AF: AF,
        AS: AS,
        OZ: OZ,
        AA: AA,
        OF: OF,
    ]
    
    public final String name
    private List<String> countries = []
    
    Continent(String name) {
        this.name = name
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
            v.setCountries(I18N.getString("Continent.${k}.Countries"))
        }
    }
    
    public static boolean belongsToAnyContinent(String country) {
        return BY_NAME.any { k, v ->
            country in v.countries
        }
    }
    
    public String toString() {
        return name
    }
}

