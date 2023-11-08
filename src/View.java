import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class View implements PropertyChangeListener{

    private Controller controller;
    private Model model;

    private JFrame frame;
    private JPanel panel;
    private JLabel label;

    public View(Model model, Controller controller) {

        this.model = model;
        this.controller = controller;

        frame = new JFrame();
        panel = new JPanel();

        JButton button = new JButton("Press Me!");

        this.label = new JLabel("Number of clicks: 0");

        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        panel.setLayout(new GridLayout(0, 1));

        panel.add(button);
        panel.add(label);

        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Toy GUI");
        frame.pack();
        frame.setVisible(true);


        //Add action listener
        button.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    controller.incrementClicks();
                }
            }
        );

        model.addListener(this);
    }


        /** Displays new total. Called by model whenever a value updates. */
    public void propertyChange(PropertyChangeEvent event) {

        // event has the new value written into it
        int newClickCount = (int) event.getNewValue();

        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        label.setText("Number of clicks: " + newClickCount);
                        //frame.repaint(); label setText automatically calls repaint.
                    }
        });
    }

    public static void main(String[] args) {
        Model model = new Model();

        new View(model, new Controller(model));
    }
}
