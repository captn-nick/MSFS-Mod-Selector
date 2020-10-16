package msfsmodmanager.ex

import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor
import msfsmodmanager.model.FirstDefinition

@CompileStatic
abstract class ModsParseException extends RuntimeException {
    String errorNo
    boolean showStackTrace
    
    String info
    
    public ModsParseException(String errorNo, boolean showStackTrace, String info, Exception cause) {
        super(cause)
        this.errorNo = errorNo
        this.showStackTrace = showStackTrace
        
        this.info = info
    }
        
    public String getMessage() {
        return info
    }

    public static class LineParseException extends ModsParseException {
        String line
        int lineNo
        
        public LineParseException(String errorNo="100", boolean showStackTrace=true, 
            String fileName, String line, int lineNo, String info="Cannot read line ${lineNo} in ${fileName}", Exception cause) {
            super(errorNo, showStackTrace, info, cause)
            this.line = line
            this.lineNo = lineNo
        }
        
        public String getMessage() {
            return info + "\n" + line
        }
    }
    
    public static class MissingDataForLineParseException extends LineParseException {
        public MissingDataForLineParseException(String fileName, String information, String line, int lineNo) {
            super("101", true, fileName, line, lineNo, /Cannot read "$information" information for line ${lineNo} in ${fileName}:/, null)
        }
    }
    
    public static class DuplicateModNameForLineParseException extends LineParseException {
        public DuplicateModNameForLineParseException(String fileName, FirstDefinition first, String conflictingLine, int lineNo) {
            super("110", false, fileName, extractLineInformation(first, conflictingLine, lineNo), lineNo, /Duplicate mod definition found in ${fileName}:/, null)
        }
    }
    
    public static class ConflictingDataForLineParseException extends LineParseException {
        public ConflictingDataForLineParseException(String fileName, String information, FirstDefinition first, String conflictingLine, int lineNo) {
            super("111", false, fileName, extractLineInformation(first, conflictingLine, lineNo), lineNo, /Inconsistent "$information" information found in ${fileName}:/, null)
        }
    }
    
    private static String extractLineInformation(FirstDefinition first, String conflictingLine, int lineNo) {
            return """- Original definition in line ${first.lineNo}:
${first.line}

- Conflicting definition in line ${lineNo}:
${conflictingLine}"""
    }
    
    public static class IllegalDataForLineParseException extends LineParseException {
        public IllegalDataForLineParseException(String fileName, String information, String value, String line, int lineNo) {
            super("120", false, fileName, line, lineNo, /"$value" is not accepted as "$information" information for line ${lineNo} in ${fileName}:/, null)
        }
    }
    
    public static class IllegalContinentDataForLineParseException extends LineParseException {
        public IllegalContinentDataForLineParseException(String fileName, String continent, String correctContinent, String country, String line, int lineNo) {
            super("120", false, fileName, line, lineNo, /"$continent" is not the correct continent for country "$country", should be "$correctContinent" for line ${lineNo} in ${fileName}:/, null)
        }
    }
}

