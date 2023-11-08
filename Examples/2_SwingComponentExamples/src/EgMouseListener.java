import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;

public class EgMouseListener extends JFrame {
    public EgMouseListener() {
        addMouseListener(
                new MouseListener() {
                    public void mouseClicked(MouseEvent e) {
                        System.out.println("Mouse clicked " + e.getX() + " " + e.getY());
                    }

                    public void mouseReleased(MouseEvent e) {
                        System.out.println("Mouse released " + e.getX() + " " + e.getY());
                    }

                    public void mouseEntered(MouseEvent e) {}

                    public void mouseExited(MouseEvent e) {}

                    public void mousePressed(MouseEvent e) {}
                });
        addMouseMotionListener(
                new MouseMotionListener() {
                    public void mouseDragged(MouseEvent e) {
                        System.out.println("Mouse dragged " + e.getX() + " " + e.getY());
                    }

                    public void mouseMoved(MouseEvent e) {}
                });
        setVisible(true);
        setSize(500, 350);
    }

    public static void main(String[] args) {
        new EgMouseListener();
    }
}
