package msfsmodmanager.ui

import groovy.transform.CompileStatic
import msfsmodmanager.Main
import msfsmodmanager.state.FileSystem

@CompileStatic
class I18N {
    private static final File BASE_DIR = FileSystem.getJarPath()
    private static final String BASE_NAME = "labels"
    private static ResourceBundle bundle
    
    private static ResourceBundle defaultBundle = ResourceBundle.getBundle("default_labels", Locale.getDefault());
    
    static {
        try {
            init()
        }
        catch (MissingResourceException ex) {
            // no user-defined resource bundle set
        }
    }
    
    private static init() {
        URL[] urls = [BASE_DIR.toURI().toURL()];
        ClassLoader loader = new URLClassLoader(urls);
        bundle = ResourceBundle.getBundle(BASE_NAME, Locale.getDefault(), loader);
    }
    
    public static String getString(String key) {
        String ret = null
        
        if (bundle != null) {
            ret = getStringOrNull(key, bundle)
        }
        
        if (ret == null) {
            ret = getDefaultStringOrNull(key)
        }
        
        return ret != null ? ret : "??? $key ???"
    }
    
    public static String getDefaultStringOrNull(String key) {
        return getStringOrNull(key, defaultBundle)
    }
    
    private static String getStringOrNull(String key, ResourceBundle rb) {
        try {
            // see https://stackoverflow.com/a/6995374 (Java 8 issue)
            return new String(rb.getString(key).getBytes("ISO-8859-1"), "UTF-8")
        }
        catch (MissingResourceException ex) {
            return null
        }
    }
}

