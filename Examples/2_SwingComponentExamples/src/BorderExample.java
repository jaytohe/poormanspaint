import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JFrame;

public class BorderExample extends JFrame {
    public BorderExample() {
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(new JButton("Button North"), BorderLayout.NORTH);
        cp.add(new JButton("Button South"), BorderLayout.SOUTH);
        cp.add(new JButton("Button East"), BorderLayout.EAST);
        cp.add(new JButton("Button West"), BorderLayout.WEST);
        cp.add(new JButton("Button Center"), BorderLayout.CENTER);
        setSize(500, 500);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        BorderExample ex = new BorderExample();
    }
}
