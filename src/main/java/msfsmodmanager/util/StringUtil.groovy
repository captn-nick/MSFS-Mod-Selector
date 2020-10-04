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
}

