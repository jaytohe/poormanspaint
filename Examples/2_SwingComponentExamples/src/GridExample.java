import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;

public class GridExample extends JFrame {
    public GridExample() {
        getContentPane().setLayout(new GridLayout(2, 3));
        for (int i = 0; i < 5; i++) {
            getContentPane().add(new JButton("Button " + i));
        }
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        GridExample ex = new GridExample();
    }
}
