package msfsmodmanager.ui

import groovy.transform.CompileStatic
import msfsmodmanager.Main
import msfsmodmanager.state.FileSystem

@CompileStatic
class I18N {
    private static final File BASE_DIR = FileSystem.getJarPath()
    private static final String BASE_NAME = "config"
    private static ResourceBundle bundle
    
    static {
        init()
    }
    
    private static init() {
        URL[] urls = [BASE_DIR.toURI().toURL()];
        ClassLoader loader = new URLClassLoader(urls);
        bundle = ResourceBundle.getBundle(BASE_NAME, Locale.getDefault(), loader);
    }

    public static String getString(String key) {
        try {
            return bundle.getString(key)
        }
        catch (MissingResourceException ex) {
            return "??? $key ???"
        }
    }
    
    public static void setString(String key, String value) {
        value = value.replaceAll("\\\\", "\\\\\\\\")
        File bundleFile = new File(BASE_DIR.absolutePath + "\\" + BASE_NAME + ".properties")
        String text = bundleFile.getText("UTF-8")
        text = text.replace("$key=\n", "$key=$value\n")
        bundleFile.setText(text, "UTF-8")
        init()
    }
}

