package view;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

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
    private DrawingPanel drawingPanel; //Custom JPanel that allows us to draw shapes on it.
    private JMenuBar menuBar;
    private JToolBar actionsToolbar; //Contains Undo, Redo and Select.
    private JToolBar selectOptionsToolbar; //Contains Rotation, Scale.
    private JToolBar shapeSelectionToolbar; // Contains shape selection buttons
    private JToolBar colorSelectionToolbar; //contains pick color button and clear.
    private JPanel topPanel; // contains the shape-selection and color selection toolbars.
    private JPanel bottomPanel; // contains the select-options toolbar (far left) and actionToolbar (far right).


    //Declare top toolbar buttons;
    // We want access to these throughout the class, to be able set if they are selected.
    private JButton lineButton;
    private JButton rectangleButton;
    private JButton triangleButton;
    private JButton ellipseButton;
    private JButton selectedShapeButton;

    //Declare color chooser toolbar buttons
    private JButton pickColorButton;
    private JButton clearButton;

    //Declare bottom toolbar buttons
    private JButton undoButton;
    private JButton redoButton;
    private JButton selectButton;

    //Declare components of selectOptionsToolbar
    private JSpinner rotationSpinner;
    private JSpinner scalingSpinner;
    private JSpinner lineWidthSpinner;
    

    //Set the preferred size for the main frame.
    private static final int FRAME_HEIGHT = 600;
    private static final int FRAME_WIDTH = 800;


    public View(Model model, Controller controller) {

        this.model = model;
        this.controller = controller;

        mainFrame = new JFrame();
        menuBar = new JMenuBar();
        drawingPanel = new DrawingPanel(controller);

        topPanel = new JPanel(new BorderLayout());
        bottomPanel = new JPanel(new BorderLayout());
        actionsToolbar = new JToolBar();
        selectOptionsToolbar = new JToolBar();
        shapeSelectionToolbar = new JToolBar();
        colorSelectionToolbar = new JToolBar();

        buildMainFrame();
        
    }


    /**
     * Builds/Populates all the components of the main frame.
     *
     * @param  None
     * @return None
     */
    private void buildMainFrame() {
        buildMenuBar();
        buildToolbars();
        mainFrame.getContentPane().add(drawingPanel, BorderLayout.CENTER); //append the drawing panel to the centre of frame's content pane,
        mainFrame.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setTitle("Poor Man's MSPaint v1.0");
        mainFrame.pack();
        mainFrame.setVisible(true);
        model.addListener(this); //make this view a propertychange listener of the model. Allows us to respond to changes in the model.
    }

    /**
     * Builds the menu bar for the main frame of the Java application.
     * 
     * @param  None
     * @return None
     */
    private void buildMenuBar() {
        // File menu that allows export as JPG or exiting.
        JMenu fileMenu = new JMenu("File");
        JMenuItem exportItem = new JMenuItem("Export");
        JMenuItem exitItem = new JMenuItem("Exit");
    
        // Set export button to open file dialog for export.
        exportItem.addActionListener(e -> {
            controller.showExportImageDialog(mainFrame);
        });
    
        // Set exit button to clear all shapes and exit the program.
        exitItem.addActionListener(e -> {
            controller.clearAllShapes();
            System.exit(0);
        });
    
        fileMenu.add(exportItem);
        fileMenu.add(exitItem);
    
        // Network menu with submenu items
        JMenu networkMenu = new JMenu("Network");
        JMenuItem connectItem = new JMenuItem("Connect");
        JMenuItem fetchShapesItem = new JMenuItem("Fetch Shapes");
        JMenuItem pushShapesItem = new JMenuItem("Push Shapes");
    
        // Disable fetchShapesItem and pushShapesItem by default
        fetchShapesItem.setEnabled(false);
        pushShapesItem.setEnabled(false);


        //Add listeners to the menu items
        connectItem.addActionListener(e -> {
            controller.showNetworkConnectDialog(mainFrame);
        });
    
        networkMenu.add(connectItem);
        networkMenu.add(fetchShapesItem);
        networkMenu.add(pushShapesItem);
    
        // Add both menus to menu bar
        menuBar.add(fileMenu);
        menuBar.add(networkMenu);
    
        // Add menu to main frame
        mainFrame.setJMenuBar(menuBar);
    }

    /**
     * Builds the toolbars for the main frame of the Java application.
     * Specifically calls the build methods for each toolbar.
     *
     * @param  None     No input parameters.
     * @return None     No return value.
     */
    private void buildToolbars() {
        buildActionsToolbar(); // contains undo, redo.
        buildSelectOptionsToolbar(); //contains rotate and scale options.
        mainFrame.getContentPane().add(bottomPanel, BorderLayout.PAGE_END);

        buildShapeSelectionToolbar(); // contains choice of shape to draw.
        buildColorSelectToolbar();
        mainFrame.getContentPane().add(topPanel, BorderLayout.PAGE_START);
    }

    /**
     * Builds the toolbar that contains buttons that allow the user to select the shape to draw.
     */
    private void buildShapeSelectionToolbar() {

        shapeSelectionToolbar.setLayout(new FlowLayout()); //set layout to flowlayout so that buttons have some space between them.
        shapeSelectionToolbar.setOpaque(false); //set background to transparent; makes it fitter with the design.

        //Instantiate shape selection buttons.
        lineButton = new JButton("Line");
        rectangleButton = new JButton("Rectangle");
        triangleButton = new JButton("Triangle");
        ellipseButton = new JButton("Ellipse");


        //Make each button update the shape type that is selected in the model.
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
        });


        // Select the lineButton by default
        selectedShapeButton = lineButton;
        selectedShapeButton.setSelected(true);
        selectedShapeButton.setBackground(Color.LIGHT_GRAY); //set background to gray because setSelected is tough to see.


        // Append each shapeButton to the shapeSelectionToolbar
        shapeSelectionToolbar.add(lineButton);
        shapeSelectionToolbar.add(rectangleButton);
        shapeSelectionToolbar.add(triangleButton);
        shapeSelectionToolbar.add(ellipseButton);
        shapeSelectionToolbar.setFloatable(false);
        shapeSelectionToolbar.setRollover(true);

        topPanel.add(shapeSelectionToolbar, BorderLayout.LINE_START); //append the toolbar to the far left of the top panel.
    
    }

    /**
     * Builds the color selection toolbar that contains the "pick color" button and "Clear".
     * 
     * The pick color button opens up a dialog that allows the user to select the color of the shape to draw.
     * 
     * The picked color will also change the color of a _selected_ shape.
     * 
     * The clear button resets the canvas and the undo/redo state.
     *
     * @param  None   This function does not take any parameters.
     * @return        This function does not return any value.
     */
    private void buildColorSelectToolbar() {

        colorSelectionToolbar.setLayout(new FlowLayout());
        colorSelectionToolbar.setOpaque(false);

        //Spinner for setting the line thickness.
        // We allow the user to set a line thickness of up to 5 pixels.
        // This is clearly a design choice and can be changed later.
        lineWidthSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));

        //Button that opens the color picker dialog.
        pickColorButton = new JButton("Pick Color");

        // Button that clears everything drawn on the canvas as well as the undo and redo state.
        clearButton = new JButton("Clear");

        //Set preferred size for spinner.
        lineWidthSpinner.setPreferredSize(new Dimension(50, lineWidthSpinner.getPreferredSize().height));


        //Add change listener to the spinner that updates the line thickness in the model.
        lineWidthSpinner.addChangeListener(e -> {
            controller.setBorderWidth(((Integer) lineWidthSpinner.getValue()).intValue());
        });

        //Add action listener to the pick color button which fires up the color selection dialog.
        // The mainFrame is passed in as a root parent to center the dialog in the frame.
        pickColorButton.addActionListener(e -> {
            controller.showColorChooser(mainFrame);
        });


        //Add action listener to the clear button which resets the canvas and the undo/redo state.
        clearButton.addActionListener(e -> {
            controller.clearAllShapes();
        });


        colorSelectionToolbar.add(new JLabel("Line Width:"));
        colorSelectionToolbar.add(lineWidthSpinner);
        colorSelectionToolbar.add(pickColorButton);
        colorSelectionToolbar.add(clearButton);

        //set toolbar options
        colorSelectionToolbar.setFloatable(false);

        //add toolbar to topPanel
        topPanel.add(colorSelectionToolbar, BorderLayout.LINE_END);
    }


    

    /**
     * Builds the select options toolbar that contains Rotation and Scale spinners.
     * 
     * The Rotation spinner takes in degrees as input and changes the rotation of the selected shape.
     * 
     * The Scale spinner takes in a decimal as input and changes the scale of the selected shape.
     * 
     * Both rotation and scale _should_ happen relative to the centroid of the shape. In the case of the line,
     * the centroid is considered the middle point of the line.
     *
     * @param  None
     * @return None
     */
    private void buildSelectOptionsToolbar() {

        selectOptionsToolbar.setLayout(new FlowLayout());
        selectOptionsToolbar.setOpaque(false);

        // Create a spinner that controls shape rotation (0 to 360 degrees).
        rotationSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 360, 1));
        
        //Create a spinner that controls the scaling factor of the shape 0x to 4x in 0.1 increments.
        scalingSpinner = new JSpinner(new SpinnerNumberModel(1.0, 0.0, 4.0, 0.1));

        //Create a spinner that controls the line width of the s
        
        // Set the preferred size of the spinners
        rotationSpinner.setPreferredSize(new Dimension(50, rotationSpinner.getPreferredSize().height));
        scalingSpinner.setPreferredSize(new Dimension(50, scalingSpinner.getPreferredSize().height));
        

        //Add change listener to the spinner that updates the rotation of the selected shape in the model.
        rotationSpinner.addChangeListener(e -> {
            controller.rotateSelectedShape(((Integer) rotationSpinner.getValue()).intValue());
        });

        //Add change listener to the spinner that updates the scale of the selected shape in the model.
        scalingSpinner.addChangeListener(e -> {
            controller.scaleSelectedShape(((Double) scalingSpinner.getValue()).doubleValue());
        });

        selectOptionsToolbar.add(new JLabel("Rotation: "));
        selectOptionsToolbar.add(rotationSpinner);

        selectOptionsToolbar.add(new JLabel("Scale: "));
        selectOptionsToolbar.add(scalingSpinner);

        selectOptionsToolbar.setFloatable(false);
        selectOptionsToolbar.setRollover(true);
        selectOptionsToolbar.setVisible(false);

        bottomPanel.add(selectOptionsToolbar, BorderLayout.LINE_START); // Add toolbar to the far left side of the bottom panel
    }

    private void buildActionsToolbar() {

        //Create buttons
        undoButton = new JButton("Undo");
        redoButton = new JButton("Redo");
        selectButton = new JButton("Select Mode: Off");

        //Default state of undoButton and redoButton is disabled.
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
        actionsToolbar.setRollover(true);

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
     * If the case involves a change in DrawingPanel, for instance the model says a new shape is to be drawn,
     * then that action is NOT performed with invokeLater as DrawingPanel has its own thread.
     * 
     * @param event the property change event
     */
    //@SuppressWarnings("unchecked")
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
                final List<Shape> shapes = (List<Shape>) event.getNewValue();
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

    /**
     * Updates the selected shape button (the one in the shapeSelectionToolbar) based on the given shape type.
     *
     * @param  shapeType  the type of shape to update the selected button for
     */
    private void updateSelectedShapeButton(ShapeType shapeType) {

        selectedShapeButton.setSelected(false);
        selectedShapeButton.setBackground(null); //reset the background of the previously selected button to default.
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
        selectedShapeButton.setBackground(Color.LIGHT_GRAY);
    }
}
