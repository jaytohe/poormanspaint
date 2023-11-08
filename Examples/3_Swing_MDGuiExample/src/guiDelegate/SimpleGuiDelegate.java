package guiDelegate;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import model.SimpleModel;


/**
 * The SimpleGuiDelegate class whose purpose is to render relevant state information stored in the model and make changes to the model state based on user events. 
 * 
 * This class uses Swing to display the model state when the model changes. This is the view aspect of the delegate class. 
 * It also listens for user input events (in the listeners defined below), translates these to appropriate calls to methods
 * defined in the model class so as to make changes to the model. This is the controller aspect of the delegate class. 
 * The class implements PropertyChangeListener in order to permit it to be added as an observer of the model class. 
 * When the model calls notifier.firePropertyChange  
 * the propertyChange(...) method below is called in order to update the view of the model.
 * 
 * @author jonl
 *
 */
public class SimpleGuiDelegate implements PropertyChangeListener {

    private static final int FRAME_HEIGHT = 200;
    private static final int FRAME_WIDTH = 600;
    private static final int TEXT_HEIGHT = 10;
    private static final int TEXT_WIDTH = 10;

    private JFrame mainFrame;

    private JToolBar toolbar;
    private JTextField inputField;
    private JRadioButton button1;
    private JButton button2;
    private JScrollPane outputPane;
    private JTextArea outputField;
    private JMenuBar menu;

    private SimpleModel model;


    /**
     * Instantiate a new SimpleGuiDelegate object
     * @param model the Model to observe, render, and update according to user events
     */
    public SimpleGuiDelegate(SimpleModel model){
        this.model = model;
        this.mainFrame = new JFrame();  // set up the main frame for this GUI
        menu = new JMenuBar();
        toolbar = new JToolBar();
        inputField = new JTextField(TEXT_WIDTH);
        outputField = new JTextArea(TEXT_WIDTH, TEXT_HEIGHT);
        outputField.setEditable(false);
        outputPane = new JScrollPane(outputField);
        setupComponents();

        // add the delegate UI component as an observer of the model
        // so as to detect changes in the model and update the GUI view accordingly
        model.addObserver(this);

    }



    /**
     * Initialises the toolbar to contain the buttons, label, input field, etc. and adds the toolbar to the main frame.
     * Listeners are created for the buttons and text field which translate user events to model object method calls (controller aspect of the delegate) 
     */
    private void setupToolbar(){
        button1 = new JRadioButton("Button 1");
        button1.addActionListener(new ActionListener(){     // to translate event for this button into appropriate model method call
            public void actionPerformed(ActionEvent e){
                // should  call method in model class if you want it to affect model
                JOptionPane.showMessageDialog(mainFrame, "Ooops, Button 1 not linked to model!");
            }
        });
        button2 = new JButton("Button 2");
        button2.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                // should  call method in model class if you want it to affect model
                JOptionPane.showMessageDialog(mainFrame, "Ooops, Button 2 not linked to model!");
            }
        });

        JLabel label = new JLabel("Enter Text: ");

        inputField.addKeyListener(new KeyListener(){        // to translate key event for the text filed into appropriate model method call
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    model.addText(inputField.getText());    // tell model to add text entered by user
                    inputField.setText("");                 // clear the input box in the GUI view
                }
            }
            public void keyReleased(KeyEvent e) {
            }
            public void keyTyped(KeyEvent e) {
            }
        });

        JButton add_button = new JButton("Add Text");       // to translate event for this button into appropriate model method call
        add_button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                model.addText(inputField.getText());        // same as when user presses carriage return key, tell model to add text entered by user
                inputField.setText("");                     // and clear the input box in the GUI view
            }
        });

        // add buttons, label, and textfield to the toolbar
        toolbar.add(button1);
        toolbar.add(button2);
        toolbar.add(label);
        toolbar.add(inputField);
        toolbar.add(add_button);
        // add toolbar to north of main frame
        mainFrame.add(toolbar, BorderLayout.NORTH);
    }


    /**
     * Sets up File menu with Load and Save entries
     * The Load and Save actions would normally be translated to appropriate model method calls similar to the way the code does this 
     * above in @see #setupToolbar(). However, load and save functionality is not implemented here, instead the code below merely displays 
     * an error message. 
     */ 
    private void setupMenu(){
        JMenu file = new JMenu ("File");
        JMenuItem load = new JMenuItem ("Load");
        JMenuItem save = new JMenuItem ("Save");
        file.add (load);
        file.add (save);
        menu.add (file);
        load.addActionListener(new ActionListener(){ 
            public void actionPerformed(ActionEvent e) {
                // should call appropriate method in model class if you want it to do something useful
                JOptionPane.showMessageDialog(mainFrame, "Ooops, Load not linked to model!");
            }
        });
        save.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                // should call appropriate method in model class if you want it to do something useful
                JOptionPane.showMessageDialog(mainFrame, "Ooops, Save not linked to model!");
            }
        });		
        // add menubar to frame
        mainFrame.setJMenuBar(menu);
    }

    /**
     * Method to setup the menu and toolbar components  
     */
    private void setupComponents(){
        setupMenu();
        setupToolbar();
        mainFrame.add(outputPane, BorderLayout.CENTER);
        mainFrame.setSize (FRAME_WIDTH, FRAME_HEIGHT);
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }	

    /**
     * This method contains code to update the GUI view when the model changes.
     * The method is called when the model changes (i.e. when the model executes notifier.firePropertyChange)
     * 
     * The code in propertyChange is sent the property that has changed and can display it appropriately.
     * For this simple example, the only state information we need from the model is what is in the model's text buffer and the
     * only GUI view element we need to update is the text area used for output.
     * 
     * NOTE: In a more complex program, the model may hold information on a variety of objects.
     * 
     */
    public void propertyChange(final PropertyChangeEvent event) {

        if(event.getSource() == model && event.getPropertyName().equals("theText")) {
            // Tell the SwingUtilities thread to update the text in the GUI components.
            SwingUtilities.invokeLater(new Runnable(){
                public void run(){
                    outputField.setText((String) event.getNewValue());
                }
            });
        }
    }

}

