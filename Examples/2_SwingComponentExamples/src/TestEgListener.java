import javax.swing.JButton;
import javax.swing.JFrame;

public class TestEgListener extends JFrame {
    public TestEgListener() {
        JButton button = new JButton("Press Me!");
        button.addActionListener(new EgListener());
        getContentPane().add(button);
        setSize(75, 75);
        setVisible(true);
    }

    public static void main(String[] args) {
        new TestEgListener();
    }
}
