package msfsmodmanager.model

import groovy.transform.CompileStatic
import groovy.transform.ToString
import groovy.transform.TupleConstructor
import msfsmodmanager.state.*
import msfsmodmanager.ui.I18N
import msfsmodmanager.ex.ModsParseException

@CompileStatic
@ToString(includePackage=false, includeNames=true, excludes="description,author,url,active,file")
class Mod {
    private static final String FLIGHTSIM_TO_URL_START = "https://flightsim.to/file/"
    private static final String FLIGHTSIM_TO_URL_WITH_WWW_START = "https://www.flightsim.to/file/"
    
    final String line
    final int lineNo
    
    final String name
    
    ModType type
    
    private String continentRaw
    Continent continent
    String country
    String city
    String icao
    private String aircraftTypeRaw
    AircraftType aircraftType
    
    String description
    String author
    String url
    
    boolean active
    
    protected Mod(String name, String line, int lineNo) {
        this.name = name
        this.line = line
        this.lineNo = lineNo
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
    
    public void checkBasicCorrectness(String fileName) {
        if (!name) {
            throw new ModsParseException.MissingDataForLineParseException(fileName, "mod name", line, lineNo)
        }
        if (!type) {
            throw new ModsParseException.MissingDataForLineParseException(fileName, "mod type", line, lineNo)
        }
        if (continentRaw && !continent) {
            throw new ModsParseException.IllegalDataForLineParseException(fileName, "continent", continentRaw, line, lineNo)
        }
    }
    
    public void checkCanonicalCorrectness(String fileName) {
        if (type == ModType.OTHER) {
            throw new ModsParseException.IllegalDataForLineParseException(fileName, "mod type", type.abbr, line, lineNo)
        }
        else if (type in [ModType.AIRPORT, ModType.LANDMARK, ModType.CITY, ModType.LANDSCAPE, ModType.LANDSCAPE_FIX]) {
            if (!continentRaw) {
                throw new ModsParseException.MissingDataForLineParseException(fileName, "continent", line, lineNo)
            }
            else if (!continent) {
                throw new ModsParseException.IllegalDataForLineParseException(fileName, "continent", continentRaw, line, lineNo)
            }
            else if (continent == Continent.OF) {
                throw new ModsParseException.IllegalDataForLineParseException(fileName, "continent", continent.abbr, line, lineNo)
            }
            
            if (!country) {
                throw new ModsParseException.MissingDataForLineParseException(fileName, "country", line, lineNo)
            }
            else if (!isCanonicalCountry()) {
                throw new ModsParseException.IllegalDataForLineParseException(fileName, "country", country, line, lineNo)
            }
            
            if (!isCorrectContinent()) {
                throw new ModsParseException.IllegalContinentDataForLineParseException(fileName, continent.abbr, Continent.getContinentFor(country), country, line, lineNo)
            }
            
            if (type == ModType.AIRPORT) {
                if (!isCanonicalIcao()) {
                    throw new ModsParseException.MissingDataForLineParseException(fileName, "icao", line, lineNo)
                }
            }
        }
        else if (type in [ModType.LIVRERY, ModType.AIRCRAFT_MODEL]) {
            if (continentRaw) {
                if (!continent) {
                    throw new ModsParseException.IllegalDataForLineParseException(fileName, "continent", continentRaw, line, lineNo)
                }
                else if (continent == Continent.OF) {
                    throw new ModsParseException.IllegalDataForLineParseException(fileName, "continent", continent.abbr, line, lineNo)
                }
            }
            if (country) {
                if (!isCanonicalCountry()) {
                    throw new ModsParseException.IllegalDataForLineParseException(fileName, "country", country, line, lineNo)
                }
                if (!isCorrectContinent()) {
                    throw new ModsParseException.IllegalContinentDataForLineParseException(fileName, continent.abbr, Continent.getContinentFor(country), country, line, lineNo)
                }
            }
            
            if (!aircraftTypeRaw) {
                throw new ModsParseException.MissingDataForLineParseException(fileName, "aircraft type", line, lineNo)
            }
            else if (!aircraftType) {
                throw new ModsParseException.IllegalDataForLineParseException(fileName, "aircraft type", aircraftTypeRaw, line, lineNo)
            }
        }
        
        if (!description) {
            throw new ModsParseException.MissingDataForLineParseException(fileName, "description", line, lineNo)
        }
        if (!author) {
            throw new ModsParseException.MissingDataForLineParseException(fileName, "author", line, lineNo)
        }
        
        if (!url) {
            throw new ModsParseException.MissingDataForLineParseException(fileName, "url", line, lineNo)
        }
        else if (!(url.startsWith(FLIGHTSIM_TO_URL_START) || url.startsWith(FLIGHTSIM_TO_URL_WITH_WWW_START))) {
            throw new ModsParseException.IllegalDataForLineParseException(fileName, "url", url, line, lineNo)
        }
    }
    
    private boolean isCanonicalCountry() {
        if (!country) return true
        
        return I18N.getDefaultStringOrNull("Country." + country) != null
    }
    
    private boolean isCorrectContinent() {
        if (!country) return true
        
        return Continent.getContinentFor(country) == continent.abbr
    }
    
    private boolean isCanonicalIcao() {
        return icao != null
    }
    
    public static Builder create(String name, String line, int lineNo) {
        return new Builder(name, line, lineNo)
    }
    
    public static class Builder {
        Mod mod
        
        public Builder(String name, String line, int lineNo) {
            this.mod = new Mod(name, line, lineNo)
        }
        
        public Builder type(String type) {
            this.mod.type = ModType.parse(type)
            return this
        }
        
        public Builder continent(String continent) {
            this.mod.continentRaw = continent
            this.mod.continent = this.mod.continentRaw ? Continent.BY_NAME[continent] : null
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
                this.mod.aircraftTypeRaw = cityOrIcaoOrAircraft
                this.mod.aircraftType = AircraftType.parse(cityOrIcaoOrAircraft)
            }
            else {
                this.mod.city = cityOrIcaoOrAircraft
            }
            return this
        }
        
        public Builder description(String description) { this.mod.description = description; return this }
        public Builder author(String author) { this.mod.author = author; return this }
        public Builder url(String url) {
            if (url != null) {
                url.replace("https://flightsim.to/", "https://www.flightsim.to/")
            }
            this.mod.url = url;
            return this
        }
        
        public Builder active(boolean active) { this.mod.active = active; return this }
        
        public Mod mod() { return mod }
    }
    
    private static boolean isIcao(String candidate) {
        // 1st OR: '?', an explicit "no ICAO given" marker
        // 2nd OR: 3-6 character ICAO
        // 3rd OR: local French ICAO codes
        return candidate ==~ /-|([A-Z0-9]{3,6})|(LF[0-9]{4})/
    }
}

