import javax.swing.JFrame;
import javax.swing.JLabel;

public class HelloWorld extends JFrame {

    public static void main(String args[]) {
        new HelloWorld();
    }

    HelloWorld() {
        JLabel jlbHelloWorld = new JLabel("Hello World");
        getContentPane().add(jlbHelloWorld);
        this.setSize(100, 100);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
