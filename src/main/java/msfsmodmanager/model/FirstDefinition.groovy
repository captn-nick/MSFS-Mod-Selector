package msfsmodmanager.model

import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor

@CompileStatic
@TupleConstructor
class FirstDefinition {
    static Map<String, FirstDefinition> MOD_NAMES = [:]
    static Map<String, FirstDefinition> CONTINENT_BY_COUNTRY = [:]
    static Map<String, FirstDefinition> COUNTRY_BY_CITY = [:]
    static Map<String, FirstDefinition> CONTINENT_BY_CITY = [:]
    
    String line
    int lineNo
    Mod mod
}

