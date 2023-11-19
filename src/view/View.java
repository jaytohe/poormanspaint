package view;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
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
    private JToolBar actionsToolbar; //Contains Undo, Redo and Select.
    private JToolBar selectOptionsToolbar; //Contains Rotation, Scale.
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
    private JButton selectButton;

    //Declare components of selectOptionsToolbar
    private JSpinner rotationSpinner;
    private JSpinner scalingSpinner;
    

    private static final int FRAME_HEIGHT = 600;
    private static final int FRAME_WIDTH = 800;


    public View(Model model, Controller controller) {

        this.model = model;
        this.controller = controller;

        mainFrame = new JFrame();
        menuBar = new JMenuBar();
        drawingPanel = new DrawingPanel(controller);


        bottomPanel = new JPanel(new BorderLayout());
        actionsToolbar = new JToolBar();
        selectOptionsToolbar = new JToolBar();
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
        buildactionsToolbar(); // contains undo, redo.
        buildSelectOptionsToolbar();
        mainFrame.getContentPane().add(bottomPanel, BorderLayout.PAGE_END);

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

    private void buildSelectOptionsToolbar() {

        selectOptionsToolbar.setLayout(new FlowLayout());

        // Create a spinner that controls shape rotation (0 to 360 degrees).
        rotationSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 360, 1));
        
        //Create a spinner that controls the scaling factor of the shape 0x to 4x in 0.1 increments.
        scalingSpinner = new JSpinner(new SpinnerNumberModel(1.0, 0.0, 4.0, 0.1));
        
        // Set the preferred size of the spinners
        rotationSpinner.setPreferredSize(new Dimension(50, rotationSpinner.getPreferredSize().height));//#endregion
        scalingSpinner.setPreferredSize(new Dimension(50, scalingSpinner.getPreferredSize().height));
        
        rotationSpinner.addChangeListener(e -> {
            //System.out.println("Rotation changed to: " + rotationSpinner.getValue());
            controller.rotateSelectedShape(((Integer) rotationSpinner.getValue()).intValue());
        });

        scalingSpinner.addChangeListener(e -> {
            //System.out.println("Scaling changed to: " + scalingSpinner.getValue());
            controller.scaleSelectedShape(((Double) scalingSpinner.getValue()).doubleValue());
        });

        selectOptionsToolbar.add(new JLabel("Rotation: "));
        selectOptionsToolbar.add(rotationSpinner);

        selectOptionsToolbar.add(new JLabel("Scale: "));
        selectOptionsToolbar.add(scalingSpinner);

        selectOptionsToolbar.setFloatable(false);
        selectOptionsToolbar.setVisible(true);

        bottomPanel.add(selectOptionsToolbar, BorderLayout.LINE_START);
    }

    private void buildactionsToolbar() {

        //Create buttons
        undoButton = new JButton("Undo");
        redoButton = new JButton("Redo");
        selectButton = new JButton("Select Mode: Off");

        //Default state of undoButton and redoButton
        undoButton.setEnabled(false);
        redoButton.setEnabled(false);

        //Undo, redo click listeners
        undoButton.addActionListener(e -> controller.undoLastShape());
        redoButton.addActionListener(e -> controller.redoShape());
        selectButton.addActionListener(e -> controller.toggleSelectMode());

        undoButton.setToolTipText("Undo the last action");
        redoButton.setToolTipText("Redo the last undone action.");
        selectButton.setToolTipText("Select a shape drawn on the screen.");
        //Add buttons to toolbar
        actionsToolbar.add(selectButton);
        actionsToolbar.add(undoButton);
        actionsToolbar.add(redoButton);

        actionsToolbar.setFloatable(false);

        //Add toolbar to bottom panel and add that to main frame on bottom right.
        bottomPanel.add(actionsToolbar, BorderLayout.LINE_END);
    }


    /**
     * A listener method that is called when a property change event occurs.
     *
     * When the model's state changes, a notification is sent to this method
     * with the name of the property that has changes and its new value (and old value in some cases).
     * 
     * We encapsulate the action to be performed on the UI thread in a Runnable
     * object that is ran safely using SwingUtils.invokeLater().
     * 
     * @param event the property change event
     */
    public void propertyChange(PropertyChangeEvent event) {

        Runnable uiAction = null; // The action to be performed on the UI thread

        switch(event.getPropertyName()) {
            case "selectedShapeType": // When the user selects a different shape to be drawn
                uiAction = () -> {
                    //drawingPanel.setCurrentShapeType((ShapeType) event.getNewValue());
                    // Make the new shape button that was chosen the selected one.
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

            case "selectModeEnabled":
                uiAction = () -> {
                    boolean selectModeEnabled = (Boolean) event.getNewValue();
                    selectOptionsToolbar.setVisible(selectModeEnabled);
                    selectButton.setText(selectModeEnabled ? "Select Mode: On" : "Select Mode: Off");
                    selectButton.setSelected(selectModeEnabled);
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
