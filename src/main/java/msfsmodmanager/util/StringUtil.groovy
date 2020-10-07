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
    
    public static String toString(Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String sStackTrace = sw.toString();
        return sStackTrace
    }
}

