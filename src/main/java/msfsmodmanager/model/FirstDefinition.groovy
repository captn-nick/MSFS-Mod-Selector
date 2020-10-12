package msfsmodmanager.model

import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor

@CompileStatic
@TupleConstructor
class FirstDefinition {
    String line
    int lineNo
    Mod mod
}

