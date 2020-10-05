package msfsmodmanager.model

import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor

import msfsmodmanager.ui.I18N

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
        String i18n = I18N.getString("ModType.defaultSelection")
        defaultSelection = (i18n.split(", ") as List<String>).collect {
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

