import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;

public class FlowExample extends JFrame {
    public FlowExample() {
        getContentPane().setLayout(new FlowLayout());
        for (int i = 0; i < 5; i++) {
            getContentPane().add(new JButton("Button " + i));
        }
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] argv) {
        new FlowExample();
    }
}
