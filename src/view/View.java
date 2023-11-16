package view;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.LinkedList;

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
import model.ShapeType;
import model.shapes.Shape;

public class View implements PropertyChangeListener {


    private Controller controller;
    private Model model;

    private JFrame mainFrame;
    private DrawingPanel drawingPanel;
    private JMenuBar menuBar;
    private JToolBar bottomToolbar;
    private JToolBar topToolbar;
    private JPanel bottomPanel;


    //Declare top toolbar buttons;
    // We want access to these throughout the class, to be able set if they are selected.
    private JButton lineButton;
    private JButton rectangleButton;
    private JButton triangleButton;
    private JButton ellipseButton;
    private JButton selectedShapeButton;

    //Declare bottom toolbar buttons
    private JButton undoButton;
    private JButton redoButton;
    

    private static final int FRAME_HEIGHT = 600;
    private static final int FRAME_WIDTH = 800;


    public View(Model model, Controller controller) {

        this.model = model;
        this.controller = controller;

        mainFrame = new JFrame();
        menuBar = new JMenuBar();
        drawingPanel = new DrawingPanel(controller);


        bottomPanel = new JPanel(new BorderLayout());
        bottomToolbar = new JToolBar();
        topToolbar = new JToolBar();

        buildMainFrame();
        
    }


    private void buildMainFrame() {

        //JLabel yellowLabel = new JLabel();
        //yellowLabel.setOpaque(true);
        //yellowLabel.setBackground(new Color(248, 213, 131));
        buildMenuBar();
        buildToolbars();
        mainFrame.getContentPane().add(drawingPanel, BorderLayout.CENTER);
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

    private void buildToolbars() {
        buildBottomToolbar(); // contains undo, redo.

        buildTopToolbar(); // contains choice of shape, color

    }

    private void buildTopToolbar() {
        lineButton = new JButton("Line");
        rectangleButton = new JButton("Rectangle");
        triangleButton = new JButton("Triangle");
        ellipseButton = new JButton("Ellpise");

        //Link to actionListener
        lineButton.addActionListener(e -> {
            controller.setShapeType(ShapeType.LINE);
        });
        rectangleButton.addActionListener(e -> {
            controller.setShapeType(ShapeType.RECTANGLE);
        });
        triangleButton.addActionListener(e -> {
            controller.setShapeType(ShapeType.TRIANGLE);
        });
        ellipseButton.addActionListener(e -> {
            controller.setShapeType(ShapeType.ELLIPSE);
            //drawingPanel.requestFocus(); //request focus so we can listen for SHIFT key.
        });


        // Select the lineButton by default
        selectedShapeButton = lineButton;
        selectedShapeButton.setSelected(true);


        // Add shape button to top toolbar
        topToolbar.add(lineButton);
        topToolbar.add(rectangleButton);
        topToolbar.add(triangleButton);
        topToolbar.add(ellipseButton);

        topToolbar.setFloatable(false);

        mainFrame.getContentPane().add(topToolbar, BorderLayout.PAGE_START);
    
    }

    private void buildBottomToolbar() {

        //Create buttons
        undoButton = new JButton("Undo");
        redoButton = new JButton("Redo");

        //Default state of undoButton and redoButton
        undoButton.setEnabled(false);
        redoButton.setEnabled(false);

        //Undo, redo click listeners
        undoButton.addActionListener(e -> controller.undoLastShape());
        redoButton.addActionListener(e -> controller.redoShape());

        undoButton.setToolTipText("Undo the last action");
        redoButton.setToolTipText("Redo the last undone action.");

        //Add buttons to toolbar
        bottomToolbar.add(undoButton);
        bottomToolbar.add(redoButton);

        bottomToolbar.setFloatable(false);

        //Add toolbar to bottom panel and add that to main frame on bottom right.
        bottomPanel.add(bottomToolbar, BorderLayout.LINE_END);
        mainFrame.getContentPane().add(bottomPanel, BorderLayout.PAGE_END);
    }

        /** Displays new total. Called by model whenever a value updates. */
    public void propertyChange(PropertyChangeEvent event) {

        Runnable uiAction = null;

        switch(event.getPropertyName()) {
            case "selectedShape":
                uiAction = () -> {
                    drawingPanel.setCurrentShapeType((ShapeType) event.getNewValue());
                    updateSelectedShapeButton((ShapeType) event.getNewValue());
                };
                break;
            
            case "drawnShapes":
                final LinkedList<Shape> shapes = (LinkedList<Shape>) event.getNewValue();
                drawingPanel.updateShapesPointer(shapes);
                break;
            
            case "undoBtnState":
                uiAction = () -> {
                    undoButton.setEnabled((Boolean) event.getNewValue());
                };
                
                break;
            case "redoBtnState":
                uiAction = () -> {
                    redoButton.setEnabled((Boolean) event.getNewValue());
                };
                break;
            default:
                return;
        }

        if (uiAction != null) {
            SwingUtilities.invokeLater(uiAction);
        }
    }

    private void updateSelectedShapeButton(ShapeType shapeType) {

        selectedShapeButton.setSelected(false);
        switch(shapeType) {
            case LINE:
                selectedShapeButton = lineButton;
            break;
            case RECTANGLE:
                selectedShapeButton = rectangleButton;
            break;
            case TRIANGLE:
                selectedShapeButton = triangleButton;
            break;
            case ELLIPSE:
                selectedShapeButton = ellipseButton;
            break;
        }
        selectedShapeButton.setSelected(true);
    }

    public static void main(String[] args) {
        Model model = new Model();

        new View(model, new Controller(model));
    }
}
