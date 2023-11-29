package model;
import model.shapes.Shape;
import model.shapes.ShiftKeyModifiable;
import model.shapes.Triangle;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Point;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.json.*;

import model.shapes.ColorFillable;
import model.shapes.Ellipse;
import model.shapes.Line;
import model.shapes.Rectangle;

/**
 * The `Model` class represents the data and logic of the application.
 * It manages the shapes, shape types, and the state of the drawing panel.
 */
public class Model {
    private ShapeType shapeType;
    private ShapeType oldShapeType;


    private List<List<Shape>> drawingPanelStates; // used for undo-redo functionality.
    // A state is defined as a list of shapes drawn on the drawing panel.
    // Undo and redo allows the user to go back and forth between states in the list.
    private int currentDrawingPanelStatePosition; // keeps track of the current position in the list
    private List<Shape> currentDrawingPanelState; // keeps track of the current state in the list

    private PropertyChangeSupport notifier; // used for notifying the View for changes in the model.

    //Boolean state of stateful buttons in the View.
    private Shape selectedShape = null;
    private boolean selectModeEnabled = false;
    private boolean isFillColorSelected = false;

    private Color borderColor;
    private Color fillColor;
    private BasicStroke borderWidth;

    private TCPDrawingClient client; // used for communicating with a drawing server.
    //allows pushing and pulling shapes.

    //Supported colors for JSON encoding, decoding.
    HashMap<String, Color> supportedColors = new HashMap<String, Color>();
    HashMap<Color, String> supportedColorsRev = new HashMap<Color, String>();

    public Model() {
        
        shapeType = ShapeType.LINE;
        oldShapeType = ShapeType.LINE;
        borderColor = Color.BLACK;
        fillColor = Color.WHITE;
        borderWidth = new BasicStroke(1);

        drawingPanelStates = new ArrayList<>();
        drawingPanelStates.add(new ArrayList<>());
        currentDrawingPanelStatePosition = 0;
        currentDrawingPanelState = drawingPanelStates.get(currentDrawingPanelStatePosition);
        this.notifier = new PropertyChangeSupport(this);
        
        //Supported Json colors.
        supportedColors.put("red", Color.RED);
        supportedColorsRev.put(Color.RED, "red");
        supportedColors.put("green", Color.GREEN);
        supportedColorsRev.put(Color.GREEN, "green");
        supportedColors.put("blue", Color.BLUE);
        supportedColorsRev.put(Color.BLUE, "blue");
        supportedColors.put("yellow", Color.YELLOW);
        supportedColorsRev.put(Color.YELLOW, "yellow");
        supportedColors.put("black", Color.BLACK);
        supportedColorsRev.put(Color.BLACK, "black");
        supportedColors.put("white", Color.WHITE);
        supportedColorsRev.put(Color.WHITE, "white");
        supportedColors.put("cyan", Color.CYAN);
        supportedColorsRev.put(Color.CYAN, "cyan");
        supportedColors.put("magenta", Color.MAGENTA);
        supportedColorsRev.put(Color.MAGENTA, "magenta");
        supportedColors.put("orange", Color.ORANGE);
        supportedColorsRev.put(Color.ORANGE, "orange");
        supportedColors.put("pink", Color.PINK);
        supportedColorsRev.put(Color.PINK, "pink");
        supportedColors.put("gray", Color.GRAY);
        supportedColorsRev.put(Color.GRAY, "gray");
        supportedColors.put("darkGray", Color.DARK_GRAY);
        supportedColorsRev.put(Color.DARK_GRAY, "darkGray");
        supportedColors.put("lightGray", Color.LIGHT_GRAY);
        supportedColorsRev.put(Color.LIGHT_GRAY, "lightGray");
    }

    /**
     * Adds a property change listener to the notifier.
     *
     * @param  pe  the property change listener to be added
     * @return     void
     */
    public void addListener(PropertyChangeListener pe) {
        notifier.addPropertyChangeListener(pe);
    }

    /**
     * Sets the TCPDrawingClient for this object.
     *
     * @param  client  the TCPDrawingClient to set
     * @return         void
     */
    public void setTCPDrawingClient(TCPDrawingClient client) {
        this.client = client;
        notifier.firePropertyChange("connectedServer", null, client != null);
    }

    public TCPDrawingClient getTCPDrawingClient() {
        return client;
    }

    

    /**
     * Updates which Shape button is selected in the View.
     * Fires back to the view to let it know which button is selected.
     *
     * @param  None
     * @return None
     */
    private void updateSelectedShape() {
        notifier.firePropertyChange("selectedShapeType", oldShapeType, shapeType);
        oldShapeType = shapeType;
    }

    
    public void setShapeType(ShapeType shapeType) {
        this.shapeType = shapeType;
        updateSelectedShape();
    }


    public Color getBorderColor() {
        return borderColor;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }


    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }


    public BasicStroke getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(BasicStroke stroke) {
        this.borderWidth = stroke;
    }

    public List<Shape> getShapes() {
        return currentDrawingPanelState;
    }

    /**
     * Updates the current state of the drawing panel to the one given by currentDrawingPanelStatePosition.
     *
     * @param  None
     * @return None
     */
    private void updateCurrentDrawingPanelState() {
        currentDrawingPanelState = drawingPanelStates.get(currentDrawingPanelStatePosition);
    }

    /**
     * Updates the current state of the drawing panel with the provided list of shapes.
     * 
     * This is used in two scenarios:
     * A) When the user selects a shape that is drawn on the screen. In that case the selection happens,
     * in a cloned state of the current state. Therefore, if the selection is successful, the cloned state
     * passed in must become the new current state.
     * 
     * B) When shapes drawn by the other users are fetched from a drawing server. In that case,
     * the state which contains all the fetched shapes becomes the new state.
     *
     * @param  state  the new state of the drawing panel as a list of shapes
     */
    private void updateCurrentDrawingPanelState(List<Shape> state) {
        if (currentDrawingPanelStatePosition+1 < drawingPanelStates.size()) {
            drawingPanelStates.set(currentDrawingPanelStatePosition+1, state);
        }
        else {
            drawingPanelStates.add(state);
        }
        currentDrawingPanelStatePosition +=1;
        currentDrawingPanelState = drawingPanelStates.get(currentDrawingPanelStatePosition);
    }

    

    /**
     * Returns the name of the shape based on the instance of the shape object.
     *
     * @param  shape  the shape object
     * @return        the name of the shape (line, rectangle, ellipse, triangle) or an empty string if the shape is not recognized
     */
    private String getShapeNameByInstance(Shape shape) {
        if (shape instanceof Line) {
            return "line";
        }
        if (shape instanceof Rectangle) {
            return "rectangle";
        }
        if (shape instanceof Ellipse) {
            return "ellipse";
        }
        if (shape instanceof Triangle) {
            return "triangle";
        }
        return "";
    }

    /**
     * Creates a list of JSON objects representing the shapes in the drawing panel state.
     * 
     * All shapes except Triangle are returned because this program supports only
     * isosceles triangles.
     *
     * @return The list of JSON objects representing the shapes in the drawing panel state.
     */
    public List<JsonObject> createJsonFromDrawingPanelState() {

        List<JsonObject> jsonShapes = new ArrayList<>();

        JsonBuilderFactory factory = Json.createBuilderFactory(null);

        for (Shape shape: getShapes()) {

            if (shape instanceof Triangle) { //Skip triangle because it is not supported.
                continue;
            }

            // Get properties which are shared by ALL shapes.
            int x = shape.getStartPoint().x;
            int y = shape.getStartPoint().y;
            String type = getShapeNameByInstance(shape);
            String borderColor = supportedColorsRev.getOrDefault(shape.getBorderColor(), "black");
            int borderWidth = (int) shape.getBorderWidth().getLineWidth();


            JsonObjectBuilder b0 = factory.createObjectBuilder(); // Holds b1 and b2 and siginifies that an "addDrawing" action is being performed.
            JsonObjectBuilder b1 = factory.createObjectBuilder(); // For properties which are shared by ALL shapes.
            JsonObjectBuilder b2 = factory.createObjectBuilder(); // For properties which are specific to each shape.

            b1.add("type", type);
            b1.add("x", x);
            b1.add("y", y);

            // Get properties which are specific to each shape.
            // Here we distinguish between color-fillable shapes and the line.
            if (shape instanceof ColorFillable) {
                String fillColor = supportedColorsRev.getOrDefault( ((ColorFillable) shape).getFillColor(), "black");
                int width = shape.getWidth();
                int height = shape.getHeight();
                int rotation = (int) Math.toDegrees(shape.getRotationAngle()); //TODO: Make get return in degrees.
                b2.add("width", width);
                b2.add("height", height);
                b2.add("rotation", rotation);
                b2.add("borderColor", borderColor);
                b2.add("borderWidth", borderWidth);
                b2.add("fillColor", fillColor);
            }
            if (shape instanceof Line) {
                int x2 = shape.getEndPoint().x;
                int y2 = shape.getEndPoint().y;

                b2.add("x2", x2);
                b2.add("y2", y2);
                b2.add("lineColor", borderColor);
                b2.add("lineWidth", borderWidth);
            }

            b1.add("properties", b2);
            b0.add("action", "addDrawing");
            b0.add("data", b1);
            jsonShapes.add(b0.build());
        }

        return jsonShapes;
    }

    
    /**
     * Creates a drawing panel state from a JSON array of shapes.
     *
     * @param  jsonShapeArray  the JSON array of shapes
     * @throws IOException              if there is an error reading the JSON array
     * @throws NumberFormatException    if there is an error parsing a number from the JSON array
     * @throws ClassCastException       if there is an error casting a JSON value to a JSON object
     */
    public void createDrawingPanelStateFromJson(JsonArray jsonShapeArray) throws IOException, NumberFormatException, ClassCastException {
        List<Shape> fetchedState = new ArrayList<>();
        for (JsonValue jsonValue: jsonShapeArray) {

            if (!(jsonValue instanceof JsonObject)) {
                continue;
            }

            try {
                JsonObject jsonShape = (JsonObject) jsonValue;
                JsonString shapeType = jsonShape.getJsonString("type");
                JsonNumber x = jsonShape.getJsonNumber("x");
                JsonNumber y = jsonShape.getJsonNumber("y");
                if (shapeType == null)
                    throw new IOException("Invalid JSON drawing array. Could not find type.");
                if (x == null)
                    throw new IOException("Invalid JSON drawing array. Could not find x.");
                if (y == null)
                    throw new IOException("Invalid JSON drawing array. Could not find y.");
                    
                //Reaching here means shapeType, startX and startY exist.

                int startX = x.intValue();
                int startY = y.intValue();

                //Get all properties regardless of shape

                JsonObject jsonShapeProperties = jsonShape.getJsonObject("properties");

                if (jsonShapeProperties == null)
                    continue;

                JsonNumber widthS = jsonShapeProperties.getJsonNumber("width");
                JsonNumber heightS = jsonShapeProperties.getJsonNumber("height");
                JsonNumber rotationS = jsonShapeProperties.getJsonNumber("rotation");
                JsonString borderColorS = jsonShapeProperties.getJsonString("borderColor");
                JsonNumber borderWidthS= jsonShapeProperties.getJsonNumber("borderWidth");
                JsonString fillColorS = jsonShapeProperties.getJsonString("fillColor");
                JsonNumber x2S = jsonShapeProperties.getJsonNumber("x2");
                JsonNumber y2S = jsonShapeProperties.getJsonNumber("y2");
                JsonString lineColorS = jsonShapeProperties.getJsonString("lineColor");
                JsonNumber lineWidthS = jsonShapeProperties.getJsonNumber("lineWidth");

                int endX;
                int endY;
                Color borderColor = supportedColors.getOrDefault((borderColorS == null) ? null : borderColorS.getString().toLowerCase(), getBorderColor());
                BasicStroke borderWidth =(borderWidthS == null) ? new BasicStroke(1) : new BasicStroke(borderWidthS.intValue());
                Color fillColor = supportedColors.getOrDefault((fillColorS == null) ? null : fillColorS.getString().toLowerCase(), getFillColor());
                int rotation = (rotationS == null) ? 0 : rotationS.intValue();
                switch(shapeType.getString()) {
                    case "line": {
                        endX = (x2S == null) ? 10 : x2S.intValue();
                        // If you do not get a property value that you expect, you should use a default value. 
                        endY = (y2S == null) ? 10: y2S.intValue();
                        borderColor = supportedColors.getOrDefault((lineColorS == null) ? null : lineColorS.getString().toLowerCase(), getBorderColor());
                        borderWidth = (lineWidthS == null) ? new BasicStroke(1) : new BasicStroke(lineWidthS.intValue());

                        fetchedState.add(new Line(startX, startY, endX, endY, borderColor, borderWidth));
                    }
                    break;
                    case "rectangle": {
                        int width = (widthS == null) ? 20 : widthS.intValue();
                        int height = (heightS == null) ? 10 : heightS.intValue();
                        endX = startX + width;
                        endY = startY + height;
                        Rectangle rectangle = new Rectangle(startX, startY, endX, endY, borderColor, fillColor, borderWidth, width == height);
                        rectangle.setRotationAngle(rotation);
                        fetchedState.add(rectangle);
                    }
                    break;
                    case "ellipse": {
                        int width = (widthS == null) ? 20 : widthS.intValue();
                        int height = (heightS == null) ? 10 : heightS.intValue();
                        endX = startX + width;
                        endY = startY + height;
                        Ellipse ellipse = new Ellipse(startX, startY, endX, endY, borderColor, fillColor, borderWidth, width == height);
                        ellipse.setRotationAngle(rotation);
                        fetchedState.add(ellipse);
                    }
                    break;
                }
            } catch(Exception e) {continue;}   
        }

        updateCurrentDrawingPanelState(fetchedState);
        notifier.firePropertyChange("drawnShapes", null, getShapes());
        notifyUndoRedoStates();
    }


    /**
     * Clones the current state of the drawing panel.
     * 
     * This is used when a user wants to draw a new shape on the screen,
     * such that all previously drawn shapes are carried over.
     * 
     * This is also used when selecting a shape on the screen. In that case,
     * the selection needs to happen at a cloned state otherwise the selection
     * will not be drawn correctly.
     *
     * @return  A list of Shape objects representing the cloned state of the drawing panel.
     */
    private List<Shape> cloneDrawingPanelState() {
        List<Shape> currentState = currentDrawingPanelState;
        List<Shape> clonedState = new ArrayList<Shape>();

        for (int i=0; i<currentState.size(); i++) {
            Shape shape = currentState.get(i);
            Shape clonedShape = shape.clone();
            clonedShape.setBorderWidth(shape.getBorderWidth());
            clonedShape.setBorderColor(shape.getBorderColor());
            clonedShape.setRotationAngle((int) Math.toDegrees(shape.getRotationAngle())); //temporary solution
            clonedShape.setScaleFactor(shape.getScaleFactor());
            clonedState.add(clonedShape);
        }
        return clonedState;
    }

    /**
     * Creates a new drawing panel state.
     *
     * This function is responsible for creating a new state in the drawing panel.
     * If the user has clicked undo and draws something new, this function will
     * overwrite the old state. It first checks if there are any existing states
     * after the current position and clears them. Then, it adds a clone of the
     * current drawing panel state to the list of states. Finally, it updates
     * the current drawing panel state.
     *
     * @param  None
     * @return None
     */
    private void createNewDrawningPanelState() {
        if (drawingPanelStates.size() > currentDrawingPanelStatePosition) { //if the user has clicked undo and draws smth new, this will overwrite the old state.
            drawingPanelStates.subList(currentDrawingPanelStatePosition+1, drawingPanelStates.size()).clear();
        }
        //drawingPanelStates.add(currentDrawingPanelStatePosition+1, cloneDrawingPanelState());
        drawingPanelStates.add(cloneDrawingPanelState());
        currentDrawingPanelStatePosition +=1;
        //Clone the currentState to the new state
        updateCurrentDrawingPanelState();
    }

    /**
     * Removes all saved states of the drawing panel
     * and clears the current state.
     *
     * @param  None
     * @return None
     */
    public void clearAllShapes() {
        //shapes.clear();
        getShapes().clear();
        drawingPanelStates.clear();
        drawingPanelStates.add(new ArrayList<>());
        currentDrawingPanelStatePosition = 0;
        currentDrawingPanelState = drawingPanelStates.get(currentDrawingPanelStatePosition);
        //undoneShapes.clear();
        notifier.firePropertyChange("drawnShapes", null, getShapes());
        notifyUndoRedoStates();
    }


    /**
     * Notifies the UI whether the undo and redo buttons should be enabled or disabled
     * based on the current state of the drawing panel.
     *
     * @param  paramName  description of parameter
     * @return            description of return value
     */
    private void notifyUndoRedoStates() {
        notifier.firePropertyChange("undoBtnState", currentDrawingPanelStatePosition == 0, currentDrawingPanelStatePosition != 0);
        //notifier.firePropertyChange("redoBtnState", undoneShapes.isEmpty(), !undoneShapes.isEmpty());
        notifier.firePropertyChange("redoBtnState", (currentDrawingPanelStatePosition == drawingPanelStates.size() - 1), currentDrawingPanelStatePosition != drawingPanelStates.size() - 1);
    }

    /**
     * Moves the current state of the drawing panel from n to n-1
     * and updates the current state of the drawing panel.
     * This is used when the user undoes a shape or shapes.
     *
     * @param  None
     * @return None
     */
    public void undoLastShape() {
        if (!drawingPanelStates.isEmpty() && currentDrawingPanelStatePosition > 0) {
            currentDrawingPanelStatePosition -= 1;
            updateCurrentDrawingPanelState();
            //undoneShapes.push(shapes.pop());
            notifier.firePropertyChange("drawnShapes", null, currentDrawingPanelState);
        }
        notifyUndoRedoStates();
    }

    /**
     * Moves the current state of the drawing panel from n-1 to n.
     * and updates the current state of the drawing panel.
     * This is used when the user redoes a shape or shapes.
     *
     * @param  None
     * @return None
     */
    public void redoShape() {
        //if (!undoneShapes.isEmpty()) {
        if (currentDrawingPanelStatePosition < drawingPanelStates.size() - 1) {
            currentDrawingPanelStatePosition += 1;
            updateCurrentDrawingPanelState();
            //shapes.push(undoneShapes.pop());
            notifier.firePropertyChange("drawnShapes", null, getShapes());
        }
        notifyUndoRedoStates();
    }


    /**
     * Updates the last shape drawn in the current state with the given end point coordinates.
     * This is used when the user hasn't finished drawing a shape and they are still dragging with the mouse.
     * If the last shape is an instance of ShiftKeyModifiable, updates its SHIFT key state.
     * Notifies the view of the updated drawn shapes.
     *
     * @param  x             the x-coordinate of the end point
     * @param  y             the y-coordinate of the end point
     * @param  SHIFTKeyDown  the SHIFT key state
     */
    public void updateLastShape(int x, int y, boolean SHIFTKeyDown) {
        if (!getShapes().isEmpty()) {
            Shape lastShape = getShapes().get(getShapes().size() - 1);

            lastShape.setEndPoint(new Point(x, y));

            if (lastShape instanceof ShiftKeyModifiable) {
                ((ShiftKeyModifiable) lastShape).setSHIFTKeyState(SHIFTKeyDown);
            }

            notifier.firePropertyChange("drawnShapes", null, getShapes());
        }
    }


    /**
     * Finds a shape at the specified position by calling the contains() method of each shape.
     * This is used when select mode is enabled.
     * 
     * All selection happens in a cloned UI state of the current drawing panel state.
     * @param  x  the x-coordinate of the position
     * @param  y  the y-coordinate of the position
     */
    public void findShapeInPos(int x, int y) {
        List<Shape> clonedState = cloneDrawingPanelState();
        for (Shape shape: clonedState) {
            if (shape.contains(x, y)) {
                this.selectedShape = shape;
                updateCurrentDrawingPanelState(clonedState);
                break;
            }
        }
    }

    public boolean isSelectModeEnabled() {
        return selectModeEnabled;
    }

    public void toggleSelectMode() {
        this.selectModeEnabled = !this.selectModeEnabled;
        this.selectedShape = selectModeEnabled ? this.selectedShape : null;
        notifier.firePropertyChange("selectModeEnabled", null, selectModeEnabled);
    }


    public boolean isFillColorSelected() {
        return isFillColorSelected;
    }
    public void setFillColorSelected(boolean b) {
        this.isFillColorSelected = b;
    }

    /**
     * Moves the selected shape by the specified offset values.
     *
     * @param  xOffset  the amount to move the shape horizontally
     * @param  yOffset  the amount to move the shape vertically
     */
    public void moveSelectedShape(int xOffset, int yOffset) {
        if (selectedShape != null) {
            selectedShape.move(xOffset, yOffset);
            // Notify the view to redraw the shapes
            notifier.firePropertyChange("drawnShapes", null, currentDrawingPanelState);
        }
    }

    /**
     * Rotates the selected shape by the specified angle in degrees.
     *
     * @param  angle  the angle by which to rotate the shape
     */
    public void rotateSelectedShape(int angle) {
        if (selectedShape != null) {
            selectedShape.setRotationAngle(angle);
            System.out.println("Set rotation angle of shape to " + angle);
            // Notify the view to redraw the shapes
            notifier.firePropertyChange("drawnShapes", null, currentDrawingPanelState);
        }
    }

    /**
     * Scales the selected shape by the specified factor.
     *
     * @param  factor  the scaling factor
     */
    public void scaleSelectedShape(double factor) {
        if (selectedShape != null) {
            selectedShape.setScaleFactor(factor);
            // Notify the view to redraw the shapes
            notifier.firePropertyChange("drawnShapes", null, currentDrawingPanelState);
        }
    }

    /**
     * Sets the border color of the selected shape.
     *
     * @param  borderColor  the color to set as the border color
     */
    public void setBorderColorSelectedShape(Color borderColor) {
        if (selectedShape != null) {
            selectedShape.setBorderColor(borderColor);
            // Notify the view to redraw the shapes
            notifier.firePropertyChange("drawnShapes", null, currentDrawingPanelState);
        }
    }

    /**
     * Sets the fill color of a ColorFillable selected shape.
     *
     * @param  borderColor  the color to set as the fill color
     */
    public void setFillColorSelectedShape(Color borderColor) {
        if (selectedShape != null && selectedShape instanceof ColorFillable) {
            ((ColorFillable) selectedShape).setFillColor(borderColor);
            // Notify the view to redraw the shapes
            notifier.firePropertyChange("drawnShapes", null, currentDrawingPanelState);
        }
    }

    /**
     * Set the border width of the selected shape.
     *
     * @param  stroke  the border width to be applied to the selected shape
     */
    public void strokeSelectedShape(BasicStroke stroke) {
        if (selectedShape != null) {
            selectedShape.setBorderWidth(stroke);
            // Notify the view to redraw the shapes
            notifier.firePropertyChange("drawnShapes", null, currentDrawingPanelState);
        }
    }

    /**
     * Draws a shape on the drawing panel based on the given coordinates and shape type.
     *
     * @param  startX       the x-coordinate of the starting point
     * @param  startY       the y-coordinate of the starting point
     * @param  endX         the x-coordinate of the ending point
     * @param  endY         the y-coordinate of the ending point
     * @param  SHIFTKeyDown indicates whether the SHIFT key is pressed or not (used for drawing squares and circles)
     */
    public void drawShape(int startX, int startY, int endX, int endY, boolean SHIFTKeyDown) {

        createNewDrawningPanelState();
        switch(shapeType) {
            case LINE:
                currentDrawingPanelState.add(new Line(startX, startY, endX, endY, borderColor, borderWidth));
                break;
            case RECTANGLE:
                currentDrawingPanelState.add(new Rectangle(startX, startY, endX, endY, borderColor, fillColor, borderWidth, SHIFTKeyDown));
                break;
            case TRIANGLE:
                currentDrawingPanelState.add(new Triangle(startX, startY, endX, endY, borderColor, fillColor, borderWidth));
                break;
            case ELLIPSE:
                currentDrawingPanelState.add(new Ellipse(startX, startY, endX, endY, borderColor, fillColor, borderWidth, SHIFTKeyDown));
                break;
        }
        

        notifier.firePropertyChange("drawnShapes", null, currentDrawingPanelState);
        notifyUndoRedoStates();
    }


    /**
     * Exports the canvas as an image file by calling the ImageExporter helper class.
     *
     * @param  selectedFilePath  the file path where the image will be saved
     * @param  width             the desired width of the image
     * @param  height            the desired height of the image
     * @throws IOException       if there is an error writing the image file
     * @throws IllegalArgumentException if the width or height is invalid
     */
    public void exportCanvasAsImage(File selectedFilePath, int width, int height) 
    throws IOException, IllegalArgumentException {
        ImageExporter.exportShapesToImage(currentDrawingPanelState, selectedFilePath, width, height);
    }


}
