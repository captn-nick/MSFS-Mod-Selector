package msfsmodmanager.model

import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor

import msfsmodmanager.state.Config

@CompileStatic
@TupleConstructor
enum ModType {
    AIRPORT("AP"),
    LANDMARK("LM"),
    CITY("CT"),
    LANDSCAPE("LS"),
    LANDSCAPE_FIX("LF"),
    LIVRERY("AL"),
    AIRCRAFT_MODEL("AM"),
    OTHER("OT"),
    
    public static List<ModType> defaultSelection
    
    static {
        String config = Config.getString("ModType.defaultSelection")
        defaultSelection = (config.split(", ") as List<String>).collect {
            parse(it)
        }
    }
    
    String abbr
    
    public static ModType parse(String abbr) {
        // for backwards compatibility with v. 0.3 only
        if (abbr == "LI") {
            return LIVRERY
        }
        return values().find { it.abbr == abbr }
    }
}

