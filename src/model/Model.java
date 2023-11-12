package model;
import model.shapes.Shape;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

import model.shapes.Line;
import model.shapes.Rectangle;

public class Model {
    private ShapeType shapeType;
    private ShapeType oldShapeType;

    private ArrayList<Shape> shapes;
    private PropertyChangeSupport notifier;


    public Model() {
        shapeType = ShapeType.LINE;
        oldShapeType = ShapeType.LINE;
        shapes = new ArrayList<>();
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

    public ArrayList<Shape> getShapes() {
        return shapes;
    }

    public void drawShape(int startX, int startY, int endX, int endY) {

        
        switch(shapeType) {
            case LINE:
                shapes.add(new Line(startX, startY, endX, endY));
                break;
            case RECTANGLE:
                shapes.add(new Rectangle(startX, startY, endX, endY));
                break;
            case TRIANGLE:
                break;
            case ELLIPSE:
                break;
        }
        
        notifier.firePropertyChange("drawnShapes", null, shapes);
    }


}
