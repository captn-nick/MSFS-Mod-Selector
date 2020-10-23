package msfsmodmanager.ui.edit

import groovy.transform.CompileStatic
import java.awt.Component
import java.awt.Font
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.JComboBox
import javax.swing.JTextField

@CompileStatic
class ExampleComboBox<T> extends JComboBox<T> implements HasText<T> {
    private String example
    
    public ExampleComboBox(String example) {
        super([example] as T[])
        this.example = example
        
        // add focus listener as in https://stackoverflow.com/a/17285132
        Component component = getEditor().getEditorComponent();  
        if (component instanceof JTextField) {
            JTextField textField = (JTextField) component;
            
            textField.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusLost(FocusEvent e) {
                        super.focusLost(e);
                        
                        if (editable) {
                            setText(getText().toString().replaceAll("\t", " "));
                        }
                        
                        if ("".equals(getText())) {
                            setText(null)
                        }
                    }

                    @Override
                    public void focusGained(FocusEvent e) {
                        super.focusGained(e);
                        if (getText() == null) { // null == example item, see below
                            editor.item = ""
                        }
                    }
                });
            
            HasText.addKeyListener(textField, this);
        }
    }
    
    public T getText() {
        T item
        if (editable) {
            item = editor.item
        }
        else {
            item = selectedItem
        }
        
        if (example.equals(item)) {
            return null
        }
        else {
            return item
        }
    }
    
    public void setText(T item) {
        if (item == null || "".equals(item)) {
            item = example
        }
        
        if (editable) {
            editor.item = item
        }
        else {
            selectedItem = item
        }
    }
    
    public void removeAllItems() {
        super.removeAllItems()
        addItem(example)
    }
}

