import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class JScrollPaneExample extends JFrame {
    public JScrollPaneExample() {
        GridButtonPanel gbp = new GridButtonPanel();
        JScrollPane sp = new JScrollPane(gbp);
        getContentPane().add(sp);
        setSize(75, 75);
        setVisible(true);
    }

    public static void main(String[] args) {
        new JScrollPaneExample();
    }
}
