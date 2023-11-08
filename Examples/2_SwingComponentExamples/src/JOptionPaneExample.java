import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class JOptionPaneExample extends JFrame {

    public JOptionPaneExample() {
        super("JOptionPane Example");
        setSize(700, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        JOptionPane.showMessageDialog(
                this, "Greetings, Professor Falken.", "Salutation", JOptionPane.PLAIN_MESSAGE);
    }

    public static void main(String[] args) {
        new JOptionPaneExample();
    }
}
