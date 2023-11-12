package controller;

import model.Model;
import model.ShapeType;


public class Controller {

    private Model model;

    private int startX, startY;

    private boolean newShape = true;

    public Controller(Model model) {
        this.model = model;
    }


    public void undoLastShape() {
        model.undoLastShape();
    }

    public void redoShape() {
        model.redoShape();
    }

    public void setShapeType(ShapeType shapeType) {
        model.setShapeType(shapeType);
    }

    public void handleMousePressed(int x, int y) {
        this.startX = x;
        this.startY = y;
        newShape = true;
    }

    public void handleMouseDragged(int x, int y) {
        if (newShape) {
            model.drawShape(startX, startY, x, y);
            newShape = false;
        }
        else {
            System.out.println("here");
            model.updateLastShape(x, y);
        }
    }

    public void handleMouseReleased() {
        newShape = true;
    }
}
