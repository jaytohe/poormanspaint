import javax.swing.JFrame;

public class TestExPanel2 extends JFrame {
    public TestExPanel2() {
        getContentPane().add(new ExPanel2());
        setSize(75, 75);
        setVisible(true);
    }

    public static void main(String argv[]) {
        new TestExPanel();
    }
}
