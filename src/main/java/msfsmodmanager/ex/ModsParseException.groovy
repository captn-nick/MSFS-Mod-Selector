package msfsmodmanager.ex

import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor
import msfsmodmanager.model.FirstDefinition

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
        int lineNo
        
        public LineParseException(String line, int lineNo, String info="Cannot read line ${lineNo} in mods.txt:", Exception cause) {
            super(info, cause)
            this.line = line
            this.lineNo = lineNo
        }
        
        public String getMessage() {
            return info + "\n" + line
        }
    }
    
    public static class MissingDataForLineParseException extends LineParseException {
        public MissingDataForLineParseException(String information, String line, int lineNo) {
            super(line, lineNo, /Cannot read "$information" information for line ${lineNo} in mods.txt:/, null)
        }
    }
    
    public static class DuplicateModNameForLineParseException extends LineParseException {
        public DuplicateModNameForLineParseException(FirstDefinition first, String conflictingLine, int lineNo) {
            super(extractLineInformation(first, conflictingLine, lineNo), lineNo, /Duplicate mod definition found in mods.txt:/, null)
        }
    }
    
    public static class ConflictingDataForLineParseException extends LineParseException {
        public ConflictingDataForLineParseException(String information, FirstDefinition first, String conflictingLine, int lineNo) {
            super(extractLineInformation(first, conflictingLine, lineNo), lineNo, /Inconsistent "$information" information found in mods.txt:/, null)
        }
    }
    
    private static String extractLineInformation(FirstDefinition first, String conflictingLine, int lineNo) {
            return """- Original definition in line ${first.lineNo}:
${first.line}

- Conflicting definition in line ${lineNo}:
${conflictingLine}"""
    }
}

