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
import java.util.List;

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
    }

    public void addListener(PropertyChangeListener pe) {
        notifier.addPropertyChangeListener(pe);
    }

    public void setTCPDrawingClient(TCPDrawingClient client) {
        this.client = client;
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
