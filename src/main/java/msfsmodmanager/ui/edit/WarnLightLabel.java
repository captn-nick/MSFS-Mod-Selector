package msfsmodmanager.ui.edit;

import javax.swing.JLabel;

import msfsmodmanager.model.Mod;

public class WarnLightLabel extends JLabel {
    EditModsFrameHandler handler;
    String property;
    
    public WarnLightLabel(EditModsFrameHandler handler, String property) {
        this.handler = handler;
        this.property = property;
        
        setBackground(new java.awt.Color(255, 51, 51));
        setText("  ");
        setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        setOpaque(true);
    }

    @Override
    public boolean isVisible() {
        Mod.CheckErrors checkErrors = handler.getCurrentCheckErrors();
        
        if (checkErrors == null) return false;
        
        return checkErrors.getErrorsByField().get(property).getT() != null;
    }
}
