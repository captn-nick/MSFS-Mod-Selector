package msfsmodmanager.ui;

import java.util.List;
import javax.swing.JComponent;

public class CountryPanel<C extends JComponent & SelectableComp> extends CheckBoxTitledBorderPanel<String, C>{
    public CountryPanel(String country) {
        super(country, I18N.getString("Country." + country), (List<C>)SelectableComps.createForCities(country));
    }
}
