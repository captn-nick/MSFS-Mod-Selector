package msfsmodmanager.ui.edit;

// based on http://www.java2s.com/Tutorial/Java/0240__Swing/AddyourownListCellRenderer.htm
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class ColoredListCellRenderer implements ListCellRenderer {
    private static DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
    
    private EditModsFrameHandler handler;
    
    public ColoredListCellRenderer(EditModsFrameHandler handler) {
        this.handler = handler;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {
        JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index,
                isSelected, cellHasFocus);
        if (handler.checkErrorsByMod.containsKey(value) && handler.checkErrorsByMod.get(value).containsAnyErrors()) {
            renderer.setBackground(EditModsFrame.CHECK_ERROR_COLOR);
            if (isSelected) {
                renderer.setBackground(renderer.getBackground().darker());
           }
        }
        return renderer;
    }
}
