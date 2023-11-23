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

    private Color borderColor;
    private BasicStroke borderWidth;
    
    public Model() {
        shapeType = ShapeType.LINE;
        oldShapeType = ShapeType.LINE;
        borderColor = Color.BLACK;
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

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
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


    private void createNewDrawningPanelState() {
        List<Shape> currentState = currentDrawingPanelState;
        if (drawingPanelStates.size() > currentDrawingPanelStatePosition) {
            drawingPanelStates.subList(currentDrawingPanelStatePosition+1, drawingPanelStates.size()).clear();
        }
        drawingPanelStates.add(currentDrawingPanelStatePosition+1, new ArrayList<Shape>()); //if the user has clicked undo and draws smth new, this will overwrite the old state.
        currentDrawingPanelStatePosition +=1;
        //Clone the currentState to the new state
        for (int i=0; i<currentState.size(); i++) {
            Shape shape = currentState.get(i);
            drawingPanelStates.get(currentDrawingPanelStatePosition).add(shape.clone());
            Shape clonedShape = drawingPanelStates.get(currentDrawingPanelStatePosition).get(i);
            clonedShape.setBorderWidth(shape.getBorderWidth());
            clonedShape.setBorderColor(shape.getBorderColor());
            clonedShape.setRotationAngle((int) Math.toDegrees(shape.getRotationAngle())); //temporary solution
            clonedShape.setScaleFactor(shape.getScaleFactor());
        }
        //return drawingPanelStates.get(currentDrawingPanelStatePosition);
        updateCurrentDrawingPanelState();
    }

    public void clearAllShapes() {
        //shapes.clear();
        getShapes().clear();
        //undoneShapes.clear();
        notifier.firePropertyChange("drawnShapes", null, getShapes());
        notifyUndoRedoStates();
    }


    private void notifyUndoRedoStates() {
        notifier.firePropertyChange("undoBtnState", drawingPanelStates.isEmpty(), !drawingPanelStates.isEmpty());
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
            //lastShape.setEndX(x);
            //lastShape.setEndY(y);

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
        createNewDrawningPanelState();
        for (Shape shape: currentDrawingPanelState) {
            if (shape.contains(x, y)) {
                this.selectedShape = shape;
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

    public void colorSelectedShape(Color borderColor) {
        if (selectedShape != null) {
            selectedShape.setBorderColor(borderColor);
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
                currentDrawingPanelState.add(new Rectangle(startX, startY, endX, endY, borderColor, borderWidth, SHIFTKeyDown));
                break;
            case TRIANGLE:
                currentDrawingPanelState.add(new Triangle(startX, startY, endX, endY, borderColor, borderWidth));
                break;
            case ELLIPSE:
                currentDrawingPanelState.add(new Ellipse(startX, startY, endX, endY, borderColor, borderWidth, SHIFTKeyDown));
                break;
        }
        
        // Clear the stacks holding undone shapes when a new shape is drawn (That's how MSPaint works)
        //if (!undoneShapes.isEmpty()) {
        //    undoneShapes.clear();
        //}
        //if (currentDrawingPanelStatePosition < drawingPanelStates.size() - 1) {
       //     drawingPanelStates.subList(0, currentDrawingPanelStatePosition+1).clear();
        //}

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
