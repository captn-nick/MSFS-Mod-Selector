package msfsmodmanager.util

import groovy.transform.CompileStatic

@CompileStatic
class StringUtil {
    public static String trimStart(String input) {
        return input.replaceAll("^\\s+", "")
    }
    
    public static String trimEnd(String input) {
        return input.replaceAll('\\s+$', "")
    }
    
    public static String toString(Throwable ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String sStackTrace = sw.toString();
        return sStackTrace
    }
    
    public static Trim canTrim(String input) {
        if (input == null) return null
        
        String trimmed = input.trim()
        
        if (trimmed == input) return null
        
        if (input.startsWith(trimmed)) {
            return Trim.END
        }
        else if (input.endsWith(trimmed)) {
            return Trim.START
        }
        else {
            // if trimmable at both ends, move to end
            return Trim.END
        }
    }
    
    public static enum Trim {
        START,
        END,
    }
}

