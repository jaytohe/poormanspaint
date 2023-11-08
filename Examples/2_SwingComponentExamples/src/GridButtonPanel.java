import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class GridButtonPanel extends JPanel {
    public GridButtonPanel() {
        setLayout(new GridLayout(10, 3));
        for (int i = 0; i < 30; i++) {
            add(new JButton("Button " + i));
        }
        setVisible(true);
    }
}
