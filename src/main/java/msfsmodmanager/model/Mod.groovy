package msfsmodmanager.model

import groovy.transform.CompileStatic
import groovy.transform.ToString
import msfsmodmanager.state.*

@CompileStatic
@ToString(includePackage=false, excludes="active,file")
class Mod {
    final String name
    
    final ModType type
    
    final Continent continent
    final String country
    final String city
    
    boolean active
    
    public Mod(String name, String type, String continent, String country, String city, boolean active) {
        this.name = name
        this.type = ModType.parse(type)
        this.continent = continent != "" ? Continent.BY_NAME[continent] : null
        this.country = country
        this.city = city
        this.active = active
    }
    
    public File getFile() {
        return new File((active ? FileSystem.MOD_DIR : FileSystem.TEMP_DIR) + $/\/$ + name)
    }
}

