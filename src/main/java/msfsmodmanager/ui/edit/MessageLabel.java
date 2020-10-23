package msfsmodmanager.ui.edit;

import javax.swing.JLabel;

import msfsmodmanager.model.Mod;
import msfsmodmanager.model.FirstDefinition;
import msfsmodmanager.ui.I18N;

public class MessageLabel extends JLabel {
    private static final String MISSING_TEXT = "Must not be empty.";
    private static final String COUNTRY_TO_CONTINENT_CONSISTENCY_TEXT = "Inconsistent country link: ${country} has been previously linked to ${continent}.";
    private static final String CITY_TO_COUNTRY_CONSISTENCY_TEXT = "Inconsistent city link: ${city} has been previously linked to ${country}.";
    private static final String CITY_TO_CONTINENT_CONSISTENCY_TEXT = "Inconsistent city link: ${city} has been previously linked to ${continent}.";
    
    EditModsFrameHandler handler;
    String property;
    
    private String illegalText;
    
    public MessageLabel(EditModsFrameHandler handler, String property) {
        this(handler, property, "Must be a valid text.");
    }
    
    public MessageLabel(EditModsFrameHandler handler, String property, String illegalText) {
        this.handler = handler;
        this.property = property;
        this.illegalText = illegalText;
        
        setFont(new java.awt.Font("Tahoma", 1, 11));
        setForeground(java.awt.Color.red);
        setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 18, 2, 8));
    }
    
    @Override
    public String getText() {
        if (handler == null) return ""; // Additional safeguard is needed during construction init
        
        Mod.CheckErrors checkErrors = handler.getCurrentCheckErrors();
        
        if (checkErrors == null) return "";
        
        return buildText(checkErrors.getErrorsByField().get(property));
    }
    
    private String buildText(Mod.CheckErrorInfo info) {
        Mod.CheckErrorType type = info.getT();
        FirstDefinition payload = info.getPayload();
        
        if (type == null) return "";
        
        switch(type) {
            case MISSING: return MISSING_TEXT;
            case ILLEGAL: return illegalText;
            case COUNTRY_TO_CONTINENT_CONSISTENCY: return replaceVariables(COUNTRY_TO_CONTINENT_CONSISTENCY_TEXT, payload);
            case CITY_TO_COUNTRY_CONSISTENCY: return replaceVariables(CITY_TO_COUNTRY_CONSISTENCY_TEXT, payload);
            case CITY_TO_CONTINENT_CONSISTENCY: return replaceVariables(CITY_TO_CONTINENT_CONSISTENCY_TEXT, payload);
            
            default: throw new IllegalArgumentException(type.toString());
        }
    }
    
    private static String replaceVariables(String input, FirstDefinition payload) {
        return input
                .replaceAll("\\$\\{city\\}", payload.getMod().getCity())
                .replaceAll("\\$\\{country\\}", !("".equals(payload.getMod().getCountry())) ? I18N.getString("Country." + payload.getMod().getCountry()) : "(none)")
                .replaceAll("\\$\\{continent\\}", payload.getMod().getContinent() != null ? payload.getMod().getContinent().toString() : "(none)");
    }
}
