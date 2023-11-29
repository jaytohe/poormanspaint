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

public class Model {
    private ShapeType shapeType;
    private ShapeType oldShapeType;


    private List<List<Shape>> drawingPanelStates;
   // private LinkedList<Shape> shapes;
   // private LinkedList<Shape> undoneShapes;

    private int currentDrawingPanelStatePosition;
    private List<Shape> currentDrawingPanelState;
    private PropertyChangeSupport notifier;

    private Shape selectedShape = null;
    private boolean selectModeEnabled = false;
    private boolean isFillColorSelected = false;

    private Color borderColor;
    private Color fillColor;
    private BasicStroke borderWidth;

    private TCPDrawingClient client;


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
       // shapes = new LinkedList<>();
       // undoneShapes = new LinkedList<>();
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

    public void addListener(PropertyChangeListener pe) {
        notifier.addPropertyChangeListener(pe);
    }

    public void setTCPDrawingClient(TCPDrawingClient client) {
        this.client = client;
        notifier.firePropertyChange("connectedServer", null, client != null);
    }

    public TCPDrawingClient getTCPDrawingClient() {
        return client;
    }

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

    private void updateCurrentDrawingPanelState() {
        currentDrawingPanelState = drawingPanelStates.get(currentDrawingPanelStatePosition);
    }

    private void updateCurrentDrawingPanelState(List<Shape> state) {
        drawingPanelStates.add(state);
        currentDrawingPanelStatePosition +=1;
        currentDrawingPanelState = drawingPanelStates.get(currentDrawingPanelStatePosition);
    }

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

    public List<JsonObject> createJsonFromDrawingPanelState() {

        List<JsonObject> jsonShapes = new ArrayList<>();

        JsonBuilderFactory factory = Json.createBuilderFactory(null);

        for (Shape shape: getShapes()) {

            if (shape instanceof Triangle) {
                continue;
            }

            int x = shape.getStartPoint().x;
            int y = shape.getStartPoint().y;
            String type = getShapeNameByInstance(shape);
            String borderColor = supportedColorsRev.getOrDefault(shape.getBorderColor(), "black");
            int borderWidth = (int) shape.getBorderWidth().getLineWidth();
            JsonObjectBuilder b0 = factory.createObjectBuilder();
            JsonObjectBuilder b1 = factory.createObjectBuilder();
            JsonObjectBuilder b2 = factory.createObjectBuilder();

            b1.add("type", type);
            b1.add("x", x);
            b1.add("y", y);
            if (shape instanceof ColorFillable) {
                String fillColor = supportedColorsRev.getOrDefault( ((ColorFillable) shape).getFillColor(), "black");
                int width = shape.getWidth();
                int height = shape.getHeight();
                int rotation = (int) Math.toDegrees(shape.getRotationAngle());
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


    private void notifyUndoRedoStates() {
        notifier.firePropertyChange("undoBtnState", currentDrawingPanelStatePosition == 0, currentDrawingPanelStatePosition != 0);
        //notifier.firePropertyChange("redoBtnState", undoneShapes.isEmpty(), !undoneShapes.isEmpty());
        notifier.firePropertyChange("redoBtnState", (currentDrawingPanelStatePosition == drawingPanelStates.size() - 1), currentDrawingPanelStatePosition != drawingPanelStates.size() - 1);
    }

    public void undoLastShape() {
        if (!drawingPanelStates.isEmpty() && currentDrawingPanelStatePosition > 0) {
            currentDrawingPanelStatePosition -= 1;
            updateCurrentDrawingPanelState();
            //undoneShapes.push(shapes.pop());
            notifier.firePropertyChange("drawnShapes", null, currentDrawingPanelState);
        }
        notifyUndoRedoStates();
    }

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
     * Finds a shape at the specified position.
     *
     * In case where there are two overlapping shapes, the one that was drawn last is selected.
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

    public void moveSelectedShape(int xOffset, int yOffset) {
        if (selectedShape != null) {
            selectedShape.move(xOffset, yOffset);
            // Notify the view to redraw the shapes
            notifier.firePropertyChange("drawnShapes", null, currentDrawingPanelState);
        }
    }

    public void rotateSelectedShape(int angle) {
        if (selectedShape != null) {
            selectedShape.setRotationAngle(angle);
            System.out.println("Set rotation angle of shape to " + angle);
            // Notify the view to redraw the shapes
            notifier.firePropertyChange("drawnShapes", null, currentDrawingPanelState);
        }
    }

    public void scaleSelectedShape(double factor) {
        if (selectedShape != null) {
            selectedShape.setScaleFactor(factor);
            // Notify the view to redraw the shapes
            notifier.firePropertyChange("drawnShapes", null, currentDrawingPanelState);
        }
    }

    public void setBorderColorSelectedShape(Color borderColor) {
        if (selectedShape != null) {
            selectedShape.setBorderColor(borderColor);
            // Notify the view to redraw the shapes
            notifier.firePropertyChange("drawnShapes", null, currentDrawingPanelState);
        }
    }

        public void setFillColorSelectedShape(Color borderColor) {
        if (selectedShape != null && selectedShape instanceof ColorFillable) {
            ((ColorFillable) selectedShape).setFillColor(borderColor);
            // Notify the view to redraw the shapes
            notifier.firePropertyChange("drawnShapes", null, currentDrawingPanelState);
        }
    }

    public void strokeSelectedShape(BasicStroke stroke) {
        if (selectedShape != null) {
            selectedShape.setBorderWidth(stroke);
            // Notify the view to redraw the shapes
            notifier.firePropertyChange("drawnShapes", null, currentDrawingPanelState);
        }
    }

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


    public void exportCanvasAsImage(File selectedFilePath, int width, int height) 
    throws IOException, IllegalArgumentException {

        System.out.println("width: "+width);
        System.out.println("height: "+height);
        ImageExporter.exportShapesToImage(currentDrawingPanelState, selectedFilePath, width, height);
    }


}
