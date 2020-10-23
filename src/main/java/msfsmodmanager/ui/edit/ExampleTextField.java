package msfsmodmanager.ui.edit;

import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;

import msfsmodmanager.util.StringUtil;

public class ExampleTextField extends JTextField implements HasText<String> {

    String exampleText;
    Font defaultFont;

    public ExampleTextField(String exampleText) {
        this.exampleText = exampleText;
        this.defaultFont = getFont();
        setToExampleText();

        this.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);

                setText(getText().replaceAll("\t", " "));

                if ("".equals(getText())) {
                    setToExampleText();
                }
            }

            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if (getText() == null) { // null == example text, see below
                    setToCustomText("");
                }
            }
        });

        HasText.addKeyListener(this, this);
    }

    @Override
    public String getText() {
        String ret = super.getText();
        if (exampleText.equals(ret)) {
            return null;
        } else {
            return ret;
        }
    }

    @Override
    public void setText(String text) {
        if (text == null || "".equals(text)) {
            setToExampleText();
        } else if (!exampleText.equals(text)) {
            setToCustomText(text);
        }
    }

    private void setToExampleText() {
        super.setText(exampleText);
        setForeground(new java.awt.Color(153, 153, 153));
        super.setFont(defaultFont.deriveFont(defaultFont.getStyle() | Font.ITALIC));
    }

    private void setToCustomText(String text) {
        super.setText(text);
        setForeground(new java.awt.Color(0, 0, 0));
        super.setFont(defaultFont);
    }

    @Override
    public void setFont(Font f) {
        super.setFont(f);
        this.defaultFont = f;
    }
}
