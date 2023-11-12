package model;
import model.shapes.Shape;
import model.shapes.Triangle;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
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


    public Model() {
        shapeType = ShapeType.LINE;
        oldShapeType = ShapeType.LINE;
        shapes = new LinkedList<>();
        undoneShapes = new LinkedList<>();
        this.notifier = new PropertyChangeSupport(this);
    }

    public void addListener(PropertyChangeListener pe) {
        notifier.addPropertyChangeListener(pe);
    }

    private void updateSelectedShape() {
        notifier.firePropertyChange("selectedShape", oldShapeType, shapeType);
        oldShapeType = shapeType;
    }

    public void setShapeType(ShapeType shapeType) {
        this.shapeType = shapeType;
        updateSelectedShape();
    }

    public LinkedList<Shape> getShapes() {
        return shapes;
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


    public void updateLastShape(int x, int y) {
        if (!shapes.isEmpty()) {
            shapes.peek().setEndX(x);
            shapes.peek().setEndY(y);
            notifier.firePropertyChange("drawnShapes", null, shapes);
        }
    }

    public void drawShape(int startX, int startY, int endX, int endY) {
        
        switch(shapeType) {
            case LINE:
                shapes.push(new Line(startX, startY, endX, endY));
                break;
            case RECTANGLE:
                shapes.push(new Rectangle(startX, startY, endX, endY, false));
                break;
            case TRIANGLE:
                shapes.push(new Triangle(startX, startY, endX, endY));
                break;
            case ELLIPSE:
                shapes.push(new Ellipse(startX, startY, endX, endY));
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
