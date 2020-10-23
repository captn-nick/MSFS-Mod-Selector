package msfsmodmanager.ui.edit

import groovy.transform.CompileStatic
import javax.swing.JComponent
import javax.swing.JPanel

@CompileStatic
public class MessagePanel extends JPanel {
    public final String[] properties;
    
    /*
     * We need to keep these components in memory permanently because when we do a parentComponent.remove(this)
     * later (which we will!), the link through #getParent() doesn't work anymore thus an "add back" would
     * run into a NullPointer when working with #getParent().
     */
    public JComponent parentComponent;
    public JComponent previousComponent;
    
    public MessagePanel(String... properties) {
        this.properties = properties;
    }
    
    public void addBack() {
        int previousIndex = parentComponent.getComponents().findIndexOf { it == previousComponent }
        parentComponent.add(this, previousIndex+1)
    }
}
