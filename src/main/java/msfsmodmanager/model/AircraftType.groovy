package msfsmodmanager.model

import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor
import msfsmodmanager.ui.I18N

@CompileStatic
@TupleConstructor
enum AircraftType {
    AIRLINER("AL"),
    JET("JT"),
    TURBOPROP("TP"),
    PROPELLER("PR"),
    
    String abbr
    
    public String toTxt() {
        return abbr
    }
    
    public static AircraftType parse(String abbr) {
        return values().find { it.abbr == abbr }
    }
    
    public String toString() {
        return I18N.getString("AircraftType.${abbr}")
    }
}

