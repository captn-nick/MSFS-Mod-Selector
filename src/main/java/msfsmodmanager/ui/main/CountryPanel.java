package msfsmodmanager.ui.main;

import java.util.List;
import javax.swing.JComponent;
import msfsmodmanager.ui.I18N;

public class CountryPanel<C extends JComponent & SelectableComp> extends CheckBoxTitledBorderPanel<String, C>{
    public CountryPanel(String country) {
        super(country, I18N.getString("Country." + country), (List<C>)SelectableComps.createForCities(country));
    }
}
