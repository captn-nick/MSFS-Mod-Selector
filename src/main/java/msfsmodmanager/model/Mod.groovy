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
    public static final String FLIGHTSIM_TO_URL_WITH_WWW_START = "https://www.flightsim.to/file/"
    
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
    
    public Mod(String name) {
        this.name = name
        this.line = null
        this.lineNo = -1
    }
    
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
        if (description) ret += description
        ret += "\t"
        if (author) ret += author
        ret += "\t"
        if (url) ret += url
        
        if (ret.trim()) {
            ret = "##\t" + ret
        }
        return ret
    }
    
    public void checkBasicCorrectness(CheckErrors checkErrors) {
        if (!name) {
            checkErrors.errorsByField.name.t = CheckErrorType.MISSING
        }
        if (!type) {
            checkErrors.errorsByField.type.t = CheckErrorType.MISSING
        }
        if (continentRaw && !continent) {
            checkErrors.errorsByField.continent.t = CheckErrorType.ILLEGAL
        }
    }
    
    public void checkCanonicalCorrectness(CheckErrors checkErrors, String fileName) {
        if (type == ModType.OTHER) {
            checkErrors.errorsByField.type.t = CheckErrorType.ILLEGAL
        }
        else if (type in [ModType.AIRPORT, ModType.LANDMARK, ModType.CITY, ModType.LANDSCAPE, ModType.LANDSCAPE_FIX]) {
            if (!continentRaw) {
                checkErrors.errorsByField.continent.t = CheckErrorType.MISSING
            }
            else if (!continent) {
                checkErrors.errorsByField.continent.t = CheckErrorType.ILLEGAL
            }
            else if (continent == Continent.OF) {
                checkErrors.errorsByField.continent.t = CheckErrorType.ILLEGAL
            }
            
            if (!country) {
                checkErrors.errorsByField.country.t = CheckErrorType.MISSING
            }
            else if (!isCanonicalCountry()) {
                checkErrors.errorsByField.country.t = CheckErrorType.ILLEGAL
            }
            
            if (!isCorrectContinent()) {
                checkErrors.errorsByField.continent.t = CheckErrorType.CANONICITY
            }
            
            if (type == ModType.AIRPORT) {
                if (!isCanonicalIcao()) {
                    checkErrors.errorsByField.icao.t = CheckErrorType.ILLEGAL
                }
            }
        }
        else if (type in [ModType.LIVRERY, ModType.AIRCRAFT_MODEL]) {
            if (continentRaw) {
                if (!continent) {
                    checkErrors.errorsByField.continent.t = CheckErrorType.MISSING
                }
                else if (continent == Continent.OF) {
                    checkErrors.errorsByField.continent.t = CheckErrorType.ILLEGAL
                }
            }
            if (country) {
                if (!isCanonicalCountry()) {
                    checkErrors.errorsByField.country.t = CheckErrorType.ILLEGAL
                }
                if (!isCorrectContinent()) {
                    checkErrors.errorsByField.continent.t = CheckErrorType.CANONICITY
                }
            }
            
            if (!aircraftTypeRaw) {
                checkErrors.errorsByField.aircraftType.t = CheckErrorType.MISSING
            }
            else if (!aircraftType) {
                checkErrors.errorsByField.aircraftType.t = CheckErrorType.ILLEGAL
            }
        }
        
        if (!description) {
            checkErrors.errorsByField.description.t = CheckErrorType.MISSING
        }
        if (!author) {
            checkErrors.errorsByField.author.t = CheckErrorType.MISSING
        }
        
        if (!url) {
            checkErrors.errorsByField.url.t = CheckErrorType.MISSING
        }
        else if (!(url.startsWith(FLIGHTSIM_TO_URL_START) || url.startsWith(FLIGHTSIM_TO_URL_WITH_WWW_START))) {
            checkErrors.errorsByField.url.t = CheckErrorType.ILLEGAL
        }
    }
    
    private boolean isCanonicalCountry() {
        if (!country) return true
        
        return I18N.getDefaultStringOrNull("Country." + country) != null
    }
    
    private boolean isCorrectContinent() {
        if (!country) return true
        
        if (!continent) return true
        
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
    
    // Hides Groovy's ugly inner class construction syntax (https://stackoverflow.com/a/27980914)
    public CheckErrors checkErrors(String fileName) {
        return new CheckErrors(fileName)
    }
    
    @TupleConstructor(includes="fileName")
    @ToString(includeNames=true)
    public class CheckErrors {
        String fileName
        
        Map<String, CheckErrorInfo> errorsByField = [:].withDefault { new CheckErrorInfo() }
        
        public boolean containsAnyErrors() {
            return errorsByField.any { k, v -> v.t }
        }
        
        public void throwIfFailure() {
            if (containsAnyErrors()) {
                throw new ModsParseException.ModCheckException(fileName, this)
            }
        }

        public void throwSubException() {
            // checkBasicCorrectness
            if (errorsByField.name.t == CheckErrorType.MISSING) {
                throw new ModsParseException.MissingDataForLineParseException(fileName, "mod name", line, lineNo)
            }
            if (errorsByField.type.t == CheckErrorType.MISSING) {
                throw new ModsParseException.MissingDataForLineParseException(fileName, "mod type", line, lineNo)
            }
            if (errorsByField.continent.t == CheckErrorType.MISSING) {
                throw new ModsParseException.IllegalDataForLineParseException(fileName, "continent", continentRaw, line, lineNo)
            }
            
            // checkCanonicalCorrectness
            if (errorsByField.type.t == CheckErrorType.ILLEGAL) {
                throw new ModsParseException.IllegalDataForLineParseException(fileName, "mod type", type.abbr, line, lineNo)
            }
            if (errorsByField.continent.t == CheckErrorType.ILLEGAL) {
                throw new ModsParseException.IllegalDataForLineParseException(fileName, "continent", continentRaw, line, lineNo)
            }
            if (errorsByField.country.t == CheckErrorType.MISSING) {
                throw new ModsParseException.MissingDataForLineParseException(fileName, "country", line, lineNo)
            }
            if (errorsByField.country.t == CheckErrorType.ILLEGAL) {
                throw new ModsParseException.IllegalDataForLineParseException(fileName, "country", country, line, lineNo)
            }
            if (errorsByField.continent.t == CheckErrorType.CANONICITY) {
                throw new ModsParseException.IllegalContinentDataForLineParseException(fileName, continent.abbr, Continent.getContinentFor(country), country, line, lineNo)
            }
            if (errorsByField.icao.t == CheckErrorType.ILLEGAL) {
                throw new ModsParseException.MissingDataForLineParseException(fileName, "icao", line, lineNo)
            }
            if (errorsByField.aircraftType.t == CheckErrorType.MISSING) {
                throw new ModsParseException.MissingDataForLineParseException(fileName, "aircraft type", line, lineNo)
            }
            if (errorsByField.aircraftType.t == CheckErrorType.ILLEGAL) {
                throw new ModsParseException.IllegalDataForLineParseException(fileName, "aircraft type", aircraftTypeRaw, line, lineNo)
            }
            if (errorsByField.description.t == CheckErrorType.MISSING) {
                throw new ModsParseException.MissingDataForLineParseException(fileName, "description", line, lineNo)
            }
            if (errorsByField.author.t == CheckErrorType.MISSING) {
                throw new ModsParseException.MissingDataForLineParseException(fileName, "author", line, lineNo)
            }
            if (errorsByField.url.t == CheckErrorType.MISSING) {
                throw new ModsParseException.MissingDataForLineParseException(fileName, "url", line, lineNo)
            }
            if (errorsByField.url.t == CheckErrorType.ILLEGAL) {
                throw new ModsParseException.IllegalDataForLineParseException(fileName, "url", url, line, lineNo)
            }
            
            // Mods.checkRepositoryConsistency
            if (errorsByField.name.t == CheckErrorType.DUPLICATE) {
                throw new ModsParseException.DuplicateModNameForLineParseException(Mods.instance.fileName, errorsByField.name.payload, line, lineNo)
            }
            if (errorsByField.continent.t == CheckErrorType.COUNTRY_TO_CONTINENT_CONSISTENCY) {
                throw new ModsParseException.ConflictingDataForLineParseException(Mods.instance.fileName, "continent", errorsByField.continent.payload, line, lineNo)
            }
            if (errorsByField.country.t == CheckErrorType.CITY_TO_COUNTRY_CONSISTENCY) {
                throw new ModsParseException.ConflictingDataForLineParseException(Mods.instance.fileName, "country", errorsByField.country.payload, line, lineNo)
            }
            if (errorsByField.continent.t == CheckErrorType.CITY_TO_CONTINENT_CONSISTENCY) {
                throw new ModsParseException.ConflictingDataForLineParseException(Mods.instance.fileName, "continent", errorsByField.continent.payload, line, lineNo)
            }
        }
        
        public Mod getMod() {
            return Mod.this
        }
    }
    
    @ToString(includeNames=true)
    public static class CheckErrorInfo {
        CheckErrorType t
        FirstDefinition payload
    }
    
    // can't make this an inner class of the inner class CheckErrors because the latter would have to be static
    public static enum CheckErrorType {
        MISSING,
        ILLEGAL,
        DUPLICATE,
        CANONICITY,
        COUNTRY_TO_CONTINENT_CONSISTENCY,
        CITY_TO_COUNTRY_CONSISTENCY,
        CITY_TO_CONTINENT_CONSISTENCY,
    }
}

