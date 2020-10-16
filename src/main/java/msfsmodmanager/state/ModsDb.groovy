package msfsmodmanager.state

import groovy.time.TimeCategory
import groovy.time.TimeDuration
import groovy.transform.CompileStatic
import msfsmodmanager.Main
import msfsmodmanager.logic.ModsDbHandler
import msfsmodmanager.model.Mod
import msfsmodmanager.util.WebReader

@CompileStatic
class ModsDb extends Mods {
    public static final String HTTP_UPDATE_URL = "https://raw.githubusercontent.com/captn-nick/MSFS-Mod-Repository/master/mods-db.txt"
    public static boolean UPDATE_WITH_GIT
    
    public static final ModsDb instance = new ModsDb()
    
    public String getFileName() {
        return "mod-repository\\mods-db.txt"
    }
    
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
    
    public boolean update(Closure okButtonAction) {
        boolean success = true
        if (UPDATE_WITH_GIT) {
            success = ModsDbHandler.updateWithGit(okButtonAction)
        }
        else {
            ModsDbHandler.updateWithHttp()
        }
        
        return success
    }
    
    public void addAllMods(Map<String, Mod> newMods) {
        // reset here to not keep anything in memory
        resetFirstDefinitions()
        
        mods.each { Mod mod ->
            checkRepositoryConsistency(mod, mod.line, mod.lineNo)
        }
        
        newMods.each { String name, Mod mod ->
            mod.checkBasicCorrectness(Mods.instance.fileName)
            mod.checkCanonicalCorrectness(Mods.instance.fileName)
            checkRepositoryConsistency(mod, mod.line, mod.lineNo)
        }
        
        modsByName.putAll(newMods)
    }
    
    protected List<Mod> sortBeforeSaving(List<Mod> mods) {
        return mods.sort { it.name.toLowerCase() }
    }
}

