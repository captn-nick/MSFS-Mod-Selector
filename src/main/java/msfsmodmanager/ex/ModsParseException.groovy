package msfsmodmanager.ex

import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor

@CompileStatic
abstract class ModsParseException extends RuntimeException {
    String info
    
    public ModsParseException(String info, Exception cause) {
        super(cause)
        this.info = info
    }
        
    public String getMessage() {
        return info
    }

    public static class LineParseException extends ModsParseException {
        String line
        
        public LineParseException(String line, String info="Cannot read the following line in mods.txt:", Exception cause) {
            super(info, cause)
            this.line = line
        }
        
        public String getMessage() {
            return info + "\n" + line
        }
    }
    
    public abstract static class MissingDataForLineParseException extends LineParseException {
        public MissingDataForLineParseException(String information, String line) {
            super(line, /Cannot read "$information" information for the following line in mods.txt:/, null)
        }
    }
    
    public static class NoModNameForLineParseException extends MissingDataForLineParseException {
        public NoModNameForLineParseException(String line) {
            super("mod name", line)
        }
    }
    
    public static class NoModTypeForLineParseException extends MissingDataForLineParseException {
        public NoModTypeForLineParseException(String line) {
            super("mod type", line)
        }
    }
}

