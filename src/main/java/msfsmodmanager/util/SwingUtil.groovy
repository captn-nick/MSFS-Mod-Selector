package msfsmodmanager.util

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import java.awt.Component
import java.awt.Container
import java.awt.Window
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.RootPaneContainer

@CompileStatic
class SwingUtil {
    public static void closeWindow(Window window) {
        window.setVisible(false)
        window.dispose()
    }
    
    @CompileDynamic // because setDefaultButton() is only defined on getRootPane() subclasses
    public static void initPanel(RootPaneContainer window, JComponent defaultComponent) {
        window.setLocationRelativeTo(null);
        
        if (defaultComponent in JButton) {
            window.getRootPane().setDefaultButton(defaultComponent);
        }
        
        defaultComponent.requestFocus();
    }
    
    public static JComponent getPreviousComponent(JComponent component) {
        Container parent = component.getParent()
        int index = parent.getComponents().findIndexOf { it == component }
        return (JComponent)(parent.getComponent(index-1))
    }
}

