import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class ButtonExample extends JFrame {
    public ButtonExample() {
        JButton button = new JButton("Press Me!");
        button.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("Button pressed");
                    }
                });
        getContentPane().add(button);
        setSize(75, 75);
        setVisible(true);
    }

    public static void main(String argv[]) {
        new ButtonExample();
    }
}
