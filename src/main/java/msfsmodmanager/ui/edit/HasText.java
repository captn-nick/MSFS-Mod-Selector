package msfsmodmanager.ui.edit;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;

import msfsmodmanager.util.StringUtil;

public interface HasText<T> {
    public T getText();

    public void setText(T text);
    
    public static <T> void addKeyListener(JTextField textField, HasText<T> hasText) {
        textField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                StringUtil.Trim trim = StringUtil.canTrim(hasText.getText().toString());
                if (trim != null) {
                    hasText.setText((T) hasText.getText().toString().trim());
                    if (trim == StringUtil.Trim.START) {
                        textField.setCaretPosition(0);
                    }
                    else if (trim == StringUtil.Trim.END) {
                        textField.setCaretPosition(hasText.getText().toString().length());
                    }
                    else throw new IllegalArgumentException(trim.toString());
                }
            }
        });
    }
}
