package view;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import controller.Controller;
import model.Model;

public class View implements PropertyChangeListener{


    private Controller controller;
    private Model model;

    private JFrame mainFrame;
    private JPanel contentPane;
    private JMenuBar menuBar;
    private JToolBar toolbar;
    private JPanel bottomPanel;

    private static final int FRAME_HEIGHT = 200;
    private static final int FRAME_WIDTH = 600;


    public View(Model model, Controller controller) {

        this.model = model;
        this.controller = controller;

        mainFrame = new JFrame();
        contentPane = new JPanel(new BorderLayout());
        bottomPanel = new JPanel(new BorderLayout());
        menuBar = new JMenuBar();
        toolbar = new JToolBar();


        buildMainFrame();

        //Add action listener
        /*
        button.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    controller.incrementClicks();
                }
            }
        );
        */
        
    }


    private void buildMainFrame() {
        buildMenuBar();
        buildToolbar();

        mainFrame.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setTitle("Poor Man's MSPaint v1.0");
        mainFrame.pack();
        mainFrame.setVisible(true);
        model.addListener(this);
    }

    /**
     * Builds the menu bar for the main frame of the Java application.
     *
     */
    private void buildMenuBar() {

        //File menu that allows export as JPG or exiting.
        JMenu fileMenu = new JMenu ("File");
        JMenuItem exportItem = new JMenuItem ("Export");
        fileMenu.add(exportItem);
        JMenuItem exitItem = new JMenuItem ("Exit");
        fileMenu.add(exitItem);

        //Help Menu that allows user to open About dialog.
        JMenu helpMenu = new JMenu ("Help");
        JMenuItem aboutItem = new JMenuItem ("About");
        helpMenu.add(aboutItem);

        //Add both menus to menu bar
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        //Add menu to main frame
        mainFrame.setJMenuBar(menuBar);
    }


    private void buildToolbar() {

        //Create buttons
        JButton undoButton = new JButton("Undo");
        JButton redoButton = new JButton("Redo");

        undoButton.setToolTipText("Undo the last action");
        redoButton.setToolTipText("Redo the last action.");

        //Add buttons to toolbar
        toolbar.add(undoButton);
        toolbar.add(redoButton);

        toolbar.setFloatable(false);

        //Add toolbar to bottom panel and add that to main frame on bottom right.
        bottomPanel.add(toolbar, BorderLayout.LINE_END);
        mainFrame.add(bottomPanel, BorderLayout.PAGE_END);
    }

        /** Displays new total. Called by model whenever a value updates. */
    public void propertyChange(PropertyChangeEvent event) {

        // event has the new value written into it
        int newClickCount = (int) event.getNewValue();

        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        //label.setText("Number of clicks: " + newClickCount);
                        //frame.repaint(); label setText automatically calls repaint.
                    }
        });
    }

    public static void main(String[] args) {
        Model model = new Model();

        new View(model, new Controller(model));
    }
}
