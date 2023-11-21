package model;
import model.shapes.Shape;
import model.shapes.ShiftKeyModifiable;
import model.shapes.Triangle;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Point;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Iterator;
import java.util.LinkedList;

import model.shapes.Ellipse;
import model.shapes.Line;
import model.shapes.Rectangle;

public class Model {
    private ShapeType shapeType;
    private ShapeType oldShapeType;

    private LinkedList<Shape> shapes;
    private LinkedList<Shape> undoneShapes;
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
        shapes = new LinkedList<>();
        undoneShapes = new LinkedList<>();
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

    public LinkedList<Shape> getShapes() {
        return shapes;
    }


    public void clearAllShapes() {
        shapes.clear();
        undoneShapes.clear();
        notifier.firePropertyChange("drawnShapes", null, shapes);
        notifyUndoRedoStates();
    }


    private void notifyUndoRedoStates() {
        notifier.firePropertyChange("undoBtnState", shapes.isEmpty(), !shapes.isEmpty());
        notifier.firePropertyChange("redoBtnState", undoneShapes.isEmpty(), !undoneShapes.isEmpty());
    }

    public void undoLastShape() {
        if (!shapes.isEmpty()) {
            undoneShapes.push(shapes.pop());
            notifier.firePropertyChange("drawnShapes", null, shapes);
        }
        notifyUndoRedoStates();
    }

    public void redoShape() {
        if (!undoneShapes.isEmpty()) {
            shapes.push(undoneShapes.pop());
            notifier.firePropertyChange("drawnShapes", null, shapes);
        }
        notifyUndoRedoStates();
    }


    public void updateLastShape(int x, int y, boolean SHIFTKeyDown) {
        if (!shapes.isEmpty()) {
            Shape lastShape = shapes.peek();

            lastShape.setEndPoint(new Point(x, y));
            //lastShape.setEndX(x);
            //lastShape.setEndY(y);

            if (lastShape instanceof ShiftKeyModifiable) {
                ((ShiftKeyModifiable) lastShape).setSHIFTKeyState(SHIFTKeyDown);
            }

            notifier.firePropertyChange("drawnShapes", null, shapes);
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
        for (Shape shape: shapes) {
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
            notifier.firePropertyChange("drawnShapes", null, shapes);
        }
    }

    public void rotateSelectedShape(int angle) {
        if (selectedShape != null) {
            selectedShape.setRotationAngle(angle);
            System.out.println("Set rotation angle of shape to " + angle);
            // Notify the view to redraw the shapes
            notifier.firePropertyChange("drawnShapes", null, shapes);
        }
    }

    public void scaleSelectedShape(double factor) {
        if (selectedShape != null) {
            selectedShape.setScaleFactor(factor);
            // Notify the view to redraw the shapes
            notifier.firePropertyChange("drawnShapes", null, shapes);
        }
    }

    public void colorSelectedShape(Color borderColor) {
        if (selectedShape != null) {
            selectedShape.setBorderColor(borderColor);
            // Notify the view to redraw the shapes
            notifier.firePropertyChange("drawnShapes", null, shapes);
        }
    }

    public void strokeSelectedShape(BasicStroke stroke) {
        if (selectedShape != null) {
            selectedShape.setBorderWidth(stroke);
            // Notify the view to redraw the shapes
            notifier.firePropertyChange("drawnShapes", null, shapes);
        }
    }

    public void drawShape(int startX, int startY, int endX, int endY, boolean SHIFTKeyDown) {
        
        switch(shapeType) {
            case LINE:
                shapes.push(new Line(startX, startY, endX, endY, borderColor, borderWidth));
                break;
            case RECTANGLE:
                shapes.push(new Rectangle(startX, startY, endX, endY, borderColor, borderWidth, SHIFTKeyDown));
                break;
            case TRIANGLE:
                shapes.push(new Triangle(startX, startY, endX, endY, borderColor, borderWidth));
                break;
            case ELLIPSE:
                shapes.push(new Ellipse(startX, startY, endX, endY, borderColor, borderWidth, SHIFTKeyDown));
                break;
        }
        
        // Clear the stacks holding undone shapes when a new shape is drawn (That's how MSPaint works)
        if (!undoneShapes.isEmpty()) {
            undoneShapes.clear();
        }

        notifier.firePropertyChange("drawnShapes", null, shapes);
        notifyUndoRedoStates();
    }


}
