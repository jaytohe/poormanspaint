import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class FrogGUI implements Frog.Listener {

    private Frog frog;

    private JFrame frame;
    private JLabel label;

    public FrogGUI(Frog frog) {
        // Display a frame with a label on it
        frame = new JFrame();
        label = new JLabel();
        frame.getContentPane().add(label);
        frame.setSize(200, 100);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        label.setOpaque(true);

        // Keep a reference to the model
        this.frog = frog;

        // Register as a listener so that the model notifies us of changes
        frog.addListener(this);
    }

    /** Display the frog's details.  Called whenever the frog changes in the model. */
    public void update() {
        // Print out the length
        label.setText("I am " + frog.getLength() + " cm long");

        // Change the background to the correct colour
        switch(frog.getColour()) {
            case "green": label.setBackground(Color.GREEN); break;
            case "red": label.setBackground(Color.RED); break;
            case "orange": label.setBackground(Color.ORANGE); break;
        }
        frame.repaint();
    }

}
