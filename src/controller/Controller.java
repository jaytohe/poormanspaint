package controller;

import java.awt.Point;

import model.Model;
import model.ShapeType;


public class Controller {

    private Model model;

    private int startX, startY;

    public Controller(Model model) {
        this.model = model;
    }


    public void setShapeType(ShapeType shapeType) {
        model.setShapeType(shapeType);
    }


    public void handleMousePressed(int x, int y) {

        this.startX = x;
        this.startY = y;
    }

    public void handleMouseReleased(int endX, int endY) {
        model.drawShape(startX, startY, endX, endY);
    }
}
