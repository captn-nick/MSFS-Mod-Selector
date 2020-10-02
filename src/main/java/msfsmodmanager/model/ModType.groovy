package msfsmodmanager.model

import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor

@CompileStatic
@TupleConstructor
enum ModType {
    AIRPORT("AP"),
    LANDMARK("LM"),
    CITY("CT"),
    LANDSCAPE("LS"),
    LANDSCAPE_FIX("LF"),
    LIVRERY("LI"),
    
    String abbr
    
    public static ModType parse(String abbr) {
        return values().find { it.abbr == abbr }
    }
}

