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
    AircraftType aircraftType
    
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
    
    public String toTxt() {
        return """${type.toTxt()}\t${continent ? continent.toTxt() : ''}\t$country\t$name\t${cityOrIcaoOrAircraftTypeToTxt()}${commentsToTxt()}"""
    }
    
    private String cityOrIcaoOrAircraftTypeToTxt() {
        String ret = ""
        if (city) ret = city
        else if (icao) ret = icao
        else if (aircraftType) ret = aircraftType.toTxt()
        
        if (ret) {
            ret += "\t"
        }
        return ret
    }
    
    private String commentsToTxt() {
        String ret = ""
        if (description) ret += description + "\t"
        if (author) ret += author + "\t"
        if (url) ret += url
        
        if (ret) {
            ret = "##\t" + ret
        }
        return ret
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
        
        public Builder cityOrIcaoOrAircraft(String cityOrIcaoOrAircraft) {
            if (this.mod.type == ModType.AIRPORT) {
                if (isIcao(cityOrIcaoOrAircraft)) {
                    this.mod.icao = cityOrIcaoOrAircraft
                }
            }
            else if (this.mod.type in [ModType.LIVRERY, ModType.AIRCRAFT_MODEL]) {
                this.mod.aircraftType = AircraftType.parse(cityOrIcaoOrAircraft)
            }
            else {
                this.mod.city = cityOrIcaoOrAircraft
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
        return candidate ==~ /([A-Z0-9]{3,6})|(LF[0-9]{4})/ //2nd part: local French ICAO codes
    }
}

