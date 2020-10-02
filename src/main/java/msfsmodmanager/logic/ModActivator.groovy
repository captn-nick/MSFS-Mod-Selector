package msfsmodmanager.logic

import groovy.transform.CompileStatic
import msfsmodmanager.model.*
import msfsmodmanager.state.*

@CompileStatic
class ModActivator {
    public static void activateMods(Selection selection) {
        List<Mod> activeMods = Mods.mods.findAll { mod ->
            selection.activates(mod)
        }
        
        List<Mod> inactiveMods = Mods.mods - activeMods
        
        activeMods.each { mod ->
            FileSystem.activate(mod)
            mod.active = true
        }
        
        inactiveMods.each { mod ->
            FileSystem.deactivate(mod)
            mod.active = false
        }
        
        println "Mods activated:"
        println activeMods.join("\n")
        println activeMods.size() + " of " + Mods.mods.size() + " in total."
        println()
    }
    
    public static void deactivateAllMods() {
        Mods.mods.each { mod ->
            FileSystem.deactivate(mod)
            mod.active = false
        }
        
        println "All mods deactivated.\n"
    }
}

