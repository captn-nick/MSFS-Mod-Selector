package msfsmodmanager.state

import groovy.transform.CompileStatic
import java.nio.file.Files
import static java.nio.file.StandardCopyOption.*;
import msfsmodmanager.Main
import msfsmodmanager.model.*
import msfsmodmanager.state.Config

@CompileStatic
class FileSystem {
    public static String MOD_DIR
    public static String TEMP_DIR
    
    public static void init() {
        MOD_DIR = Config.getString("path.community")
        TEMP_DIR = Config.getString("path.temp")
        
        if (MOD_DIR == "") {
            Main.FIRST_START = true
            Config.setString("path.community", getJarPath().absolutePath + /\Community/)
            MOD_DIR = Config.getString("path.community")
        }
        
        if (TEMP_DIR == "") {
            Config.setString("path.temp", getJarPath().absolutePath + /\Temp/)
            TEMP_DIR = Config.getString("path.temp")
        }
        
        File tempDir = new File(TEMP_DIR)
        if (!tempDir.exists()) {
            tempDir.mkdir()
        }
        
        Mods.instance.modInfoFile = new File(getJarPath().absolutePath + /\mods.txt/)
        ModsDb.instance.modInfoFile = new File(getJarPath().absolutePath + /\mod-repository\mods-db.txt/)
        
        ModsDb.UPDATE_WITH_GIT = Config.getString("ModsDb.updateWithGit") == "true"
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

