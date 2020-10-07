package msfsmodmanager.state

import groovy.transform.CompileStatic
import java.nio.file.Files
import static java.nio.file.StandardCopyOption.*;
import msfsmodmanager.Main
import msfsmodmanager.model.*
import msfsmodmanager.ui.I18N

@CompileStatic
class FileSystem {
    public static String MOD_DIR
    public static String TEMP_DIR
    
    public static void init() {
        MOD_DIR = I18N.getString("path.community")
        TEMP_DIR = I18N.getString("path.temp")
        
        if (MOD_DIR == "") {
            I18N.setString("path.community", getJarPath().absolutePath + /\Community/)
            MOD_DIR = I18N.getString("path.community")
        }
        
        if (TEMP_DIR == "") {
            I18N.setString("path.temp", getJarPath().absolutePath + /\Temp/)
            TEMP_DIR = I18N.getString("path.temp")
        }
        
        File tempDir = new File(TEMP_DIR)
        if (!tempDir.exists()) {
            tempDir.mkdir()
        }
        
        Mods.MOD_INFO_FILE = new File(getJarPath().absolutePath + /\mods.txt/)
    }
    
    public static File getJarPath() {
        File file = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        if (file.name.endsWith(".jar")) {
            file = file.parentFile
        }
        return file
    }
    
    public static boolean isActive(String modName) {
        return new File(MOD_DIR + $/\/$ + modName).exists()
    }
    
    public static void activate(Mod mod) {
        if (mod.active) {
            return
        }
        Files.move(mod.file.toPath(), new File(MOD_DIR + $/\/$ + mod.name).toPath(), REPLACE_EXISTING);
    }
    
    public static void deactivate(Mod mod) {
        if (!mod.active) {
            return
        }
        Files.move(mod.file.toPath(), new File(TEMP_DIR + $/\/$ + mod.name).toPath(), REPLACE_EXISTING);
    }
}

