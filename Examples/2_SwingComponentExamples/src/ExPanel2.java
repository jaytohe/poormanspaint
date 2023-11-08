import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import javax.swing.JPanel;

public class ExPanel2 extends JPanel {
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Line2D line = new Line2D.Double(0, 0, 75, 75);
        g2d.draw(line);
        Ellipse2D curve = new Ellipse2D.Double(10, 10, 20, 20);
        g2d.draw(curve);
    }
}
