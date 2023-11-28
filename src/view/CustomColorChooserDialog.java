package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class CustomColorChooserDialog extends JColorChooser {
    private JCheckBox fillCheckBox;
    private JPanel checkboxPanel;
    private JDialog dialog;
    public CustomColorChooserDialog(Color initialColor, boolean fillColorSelected) {

        super(initialColor);


        // Create the checkbox
        fillCheckBox = new JCheckBox("Fill Color");
        fillCheckBox.setSelected(fillColorSelected);

        // Create a panel to hold the checkbox
        checkboxPanel = new JPanel();
        checkboxPanel.add(fillCheckBox);

        updateUI();
    }

    public JDialog createDialog(Component parent, String title, boolean modal, ActionListener okListener, ActionListener cancelListener) {
        dialog = super.createDialog(parent, title, false, this, okListener, cancelListener);
        dialog.add(checkboxPanel, BorderLayout.PAGE_START);

        return dialog;
    }

    public boolean isFillColorSelected() {
        return fillCheckBox.isSelected();
    }
}
