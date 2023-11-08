import java.awt.Graphics;

import javax.swing.JPanel;

public class ExPanel extends JPanel {
    public void paint(Graphics g) {
        g.drawLine(0, 0, 75, 75);
        g.drawOval(10, 10, 20, 20);
    }
}
