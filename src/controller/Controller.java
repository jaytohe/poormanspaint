package controller;

import java.awt.BasicStroke;
import java.awt.Color;

import javax.swing.JColorChooser;

import model.Model;
import model.ShapeType;


public class Controller {

    private Model model;

    private int startX, startY;

    private boolean newShape = true;

    private boolean SHIFTKeyDown = false;

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

    public void toggleSelectMode() {
        model.toggleSelectMode();
    }

    public void rotateSelectedShape(int angle) {
        model.rotateSelectedShape(angle);
    }

    public void scaleSelectedShape(double scaleFactor) {
        model.scaleSelectedShape(scaleFactor);
    }

    public void setSHIFTKeyState(boolean state) {
        SHIFTKeyDown = state;
    }


    public void showColorChooser() {
        Color pickedColor = JColorChooser.showDialog(null, "Choose Color", model.getBorderColor());

        if (pickedColor != null) {
            if (model.isSelectModeEnabled()) {
                model.colorSelectedShape(pickedColor);
            }
            model.setBorderColor(pickedColor);
        }
    }

    public void setBorderWidth(float width) {

        if (model.isSelectModeEnabled()) {
            model.strokeSelectedShape(new BasicStroke(width));
        }
        model.setBorderWidth(new BasicStroke(width));
    }

    public void clearAllShapes() {
        model.clearAllShapes();
    }

    public void handleMousePressed(int x, int y) {
        this.startX = x;
        this.startY = y;
        newShape = true;

        if (model.isSelectModeEnabled()) {
            model.findShapeInPos(startX, startY);
        }
    }

    public void handleMouseDragged(int x, int y) {
        if (!model.isSelectModeEnabled()) {
            if (newShape) {
                model.drawShape(startX, startY, x, y, SHIFTKeyDown);
                newShape = false;
            }
            else {
                model.updateLastShape(x, y, SHIFTKeyDown);
            }
        }
        else {
            model.moveSelectedShape(x-startX, y-startY);
            startX = x;
            startY = y;
        }
    }

    public void handleMouseReleased() {
        newShape = true;
    }
}
