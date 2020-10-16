package msfsmodmanager.util

import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor

@CompileStatic
class CmdLine {
    public static Result execute(String command) {
        StringBuilder sout = new StringBuilder()
        StringBuilder serr = new StringBuilder()
        Process proc = command.execute()
        proc.consumeProcessOutput(sout, serr)
        proc.waitFor()
        return new Result(sout.toString(), serr.toString())
    }
    
    @TupleConstructor
    public static class Result {
        String sout
        String serr
    }
}

