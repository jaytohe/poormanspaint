import javax.swing.JFrame;

public class TestExPanel extends JFrame {
    public TestExPanel() {
        getContentPane().add(new ExPanel());
        setSize(75, 75);
        setVisible(true);
    }

    public static void main(String argv[]) {
        new TestExPanel();
    }
}
