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
    LIVRERY("LI"),
    
    public static List<ModType> defaultSelection
    
    static {
        String i18n = I18N.getString("ModType.defaultSelection")
        defaultSelection = (i18n.split(", ") as List<String>).collect {
            parse(it)
        }
    }
    
    String abbr
    
    public static ModType parse(String abbr) {
        return values().find { it.abbr == abbr }
    }
}

