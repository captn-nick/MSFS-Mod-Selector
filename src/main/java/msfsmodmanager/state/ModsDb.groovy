package msfsmodmanager.state

import groovy.time.TimeCategory
import groovy.time.TimeDuration
import groovy.transform.CompileStatic
import msfsmodmanager.Main
import msfsmodmanager.model.Mod
import msfsmodmanager.util.WebReader

@CompileStatic
class ModsDb extends Mods {
    private static final String UPDATE_URL = "https://raw.githubusercontent.com/captn-nick/MSFS-Mod-Repository/master/mods-db.txt"
    
    public static final ModsDb instance = new ModsDb()
    
    public DbInformationFound replaceWithDbInformation(List<String> modNames) {
        DbInformationFound ret = new DbInformationFound()
        
        ret.lines = modNames.collect { String modName ->
            Mod mod = modsByName[modName]
            
            if (mod) {
                ret.foundAny = true
                return mod.toTxt()
            }
            else {
                return modName
            }
        }
        
        return ret
    }
    
    public static class DbInformationFound {
        boolean foundAny
        List<String> lines
    }
    
    public boolean isUpdatable() {
        if (Main.FIRST_START) {
            return true
        }
        
        Date fileModified = new Date(modInfoFile.lastModified())
        TimeDuration sinceModified = TimeCategory.minus(new Date(), fileModified)
        
        return sinceModified >= new TimeDuration(0, 10, 0, 0)
    }
    
    public void update() {
        String text = WebReader.readUrl(UPDATE_URL)
        
        modInfoFile.setText(text, "UTF-8")
    }
}

