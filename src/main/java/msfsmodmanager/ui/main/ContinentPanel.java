package msfsmodmanager.ui.main;

import java.util.List;
import javax.swing.JComponent;

import msfsmodmanager.model.Continent;
import msfsmodmanager.ui.I18N;

public class ContinentPanel<C extends JComponent & SelectableComp> extends CheckBoxTitledBorderPanel<Continent, C> {
    public ContinentPanel(Continent continent) {
        super(continent.abbr, I18N.getString("Continent." + continent.abbr), (List<C>)SelectableComps.create(continent));
    }
}
