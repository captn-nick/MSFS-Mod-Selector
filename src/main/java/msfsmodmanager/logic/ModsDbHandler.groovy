package msfsmodmanager.logic

import groovy.transform.CompileStatic
import msfsmodmanager.model.Mod
import msfsmodmanager.state.Mods
import msfsmodmanager.state.ModsDb
import msfsmodmanager.util.Git
import msfsmodmanager.util.WebReader

@CompileStatic
class ModsDbHandler {
    public static void addUserDefinedModsToModsDb() {
        Map<String, Mod> mods = Mods.instance.getUserDefinedMods()
        ModsDb.instance.addAllMods(mods)
        ModsDb.instance.saveToTxt()
    }
    
    public static void updateWithHttp() {
        String text = WebReader.readUrl(ModsDb.HTTP_UPDATE_URL)
        
        ModsDb.instance.modInfoFile.setText(text, "UTF-8")
    }
    
    public static boolean updateWithGit(Closure okButtonAction) {
        return Git.pull(ModsDb.instance.modInfoFile.parentFile.absolutePath, okButtonAction)
    }
}

