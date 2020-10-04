package msfsmodmanager.model

import groovy.transform.CompileStatic
import groovy.transform.ToString
import msfsmodmanager.state.*

@CompileStatic
@ToString(includePackage=false, includeNames=true, excludes="description,author,url,active,file")
class Mod {
    final String name
    
    ModType type
    
    Continent continent
    String country
    String city
    String icao
    
    String description
    String author
    String url
    
    boolean active
    
    protected Mod(String name) {
        this.name = name
    }
    
    public File getFile() {
        return new File((active ? FileSystem.MOD_DIR : FileSystem.TEMP_DIR) + $/\/$ + name)
    }
    
    public static Builder name(String name) {
        return new Builder(name)
    }
    
    public static class Builder {
        Mod mod
        
        public Builder(String name) {
            this.mod = new Mod(name)
        }
        
        public Builder type(String type) {
            this.mod.type = ModType.parse(type)
            return this
        }
        
        public Builder continent(String continent) {
            this.mod.continent = continent != "" ? Continent.BY_NAME[continent] : null
            return this
        }
        
        public Builder country(String country) { this.mod.country = country; return this }
        
        public Builder cityOrIcao(String cityOrIcao) {
            if (this.mod.type == ModType.AIRPORT) {
                if (isIcao(cityOrIcao)) {
                    this.mod.icao = cityOrIcao
                }
            }
            else {
                this.mod.city = cityOrIcao
            }
            return this
        }
        
        public Builder description(String description) { this.mod.description = description; return this }
        public Builder author(String author) { this.mod.author = author; return this }
        public Builder url(String url) { this.mod.url = url; return this }        
        
        public Builder active(boolean active) { this.mod.active = active; return this }
        
        public Mod mod() { return mod }
    }
    
    private static boolean isIcao(String candidate) {
        return candidate ==~ /[A-Z0-9]{4}/
    }
}

