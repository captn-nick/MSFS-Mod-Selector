package msfsmodmanager.ui;

import java.awt.Font;
import java.awt.Insets;
import javax.swing.JCheckBox;

public class SelectableCheckBox extends JCheckBox implements SelectableComp {
    private final String name;
    private final boolean isCity;
    
    public SelectableCheckBox(String name, boolean isCountry) {
        super(isCountry ? I18N.getString("Country." + name) : name);
        
        this.name = name;
        this.isCity = !isCountry;
        
        setMargin(new Insets(0, 5, 0, 0));
        if (isCountry) {
            setFont(new Font(getFont().getName(), Font.BOLD, getFont().getSize()));
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isCity() {
        return this.isCity;
    }
    
    
}
