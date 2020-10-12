package msfsmodmanager.model

import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor

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
}

