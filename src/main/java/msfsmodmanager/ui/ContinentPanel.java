package msfsmodmanager.ui;

import java.util.List;
import javax.swing.JComponent;

import msfsmodmanager.model.Continent;

public class ContinentPanel<C extends JComponent & SelectableComp> extends CheckBoxTitledBorderPanel<Continent, C> {
    public ContinentPanel(Continent continent) {
        super(continent.abbr, I18N.getString("Continent." + continent.abbr), (List<C>)SelectableComps.create(continent));
    }
}
