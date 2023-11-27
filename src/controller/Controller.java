package controller;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.io.File;

import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import model.Model;
import model.ShapeType;


public class Controller {

    private Model model;

    private int startX, startY; //the starting coordinates of a mouse drag.

    private boolean newShape = true; 
    //This is false only when a shape is being drawn i.e. the user drags the mouse to draw it.

    private boolean SHIFTKeyDown = false; //tracks whether shift key is being held down or not

    public Controller(Model model) {
        this.model = model;
    }



    //===Actions corresponding to the actionsToolba buttons===//
    public void undoLastShape() {
        model.undoLastShape();
    }
    public void redoShape() {
        model.redoShape();
    }
    public void setShapeType(ShapeType shapeType) {
        model.setShapeType(shapeType);
    }
    // ==============================================================//


    //===Actions corresponding to the selectOptionsToolbar buttons===//
    public void toggleSelectMode() {
        model.toggleSelectMode();
    }

    public void rotateSelectedShape(int angle) {
        model.rotateSelectedShape(angle);
    }

    public void scaleSelectedShape(double scaleFactor) {
        model.scaleSelectedShape(scaleFactor);
    }
    // ==============================================================//

    
    public void setSHIFTKeyState(boolean state) {
        SHIFTKeyDown = state;
    }


    /**
     * Displays a color chooser dialog to allow the user to pick a color. The selected color
     * is then used to update the border color of the model. If the select mode is enabled,
     * the selected color is also applied to the currently selected shape.
     *
     * @param  parent  the parent component to display the color chooser dialog
     */
    public void showColorChooser(Component parent) {
        Color pickedColor = JColorChooser.showDialog(parent, "Choose Color", model.getBorderColor()); // fire up a color chooser dialog with the default color being the previously selected color.
        if (pickedColor != null) {
            if (model.isSelectModeEnabled()) {
                model.colorSelectedShape(pickedColor);
            }
            model.setBorderColor(pickedColor);
        }
    }


    /**
     * Displays a file chooser dialog to allow the user to export the canvas as an image.
     * 
     * The file chooser is set to directory only mode since the user is expected to select
     * a directory to save the image to.
     * 
     * If the user closes the dialog without selecting a file, the export will be cancelled.
     * Otherwise, ImageExporter is called to save the image. In case an error occurs,
     * a popup message error appears explaining the exception.
     *
     * @param  parent  the parent component to display the dialog
     */
    public void showExportImageDialog(Component parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogTitle("Export Canvas As");
        fileChooser.setApproveButtonText("Export");
        fileChooser.setApproveButtonToolTipText("Export the Image");
        int returnVal = fileChooser.showSaveDialog(parent);

        if (returnVal == JFileChooser.APPROVE_OPTION) { // if the user selected a directory and clicked Export.
            File selectedFilePath = fileChooser.getSelectedFile();

            try {
                model.exportCanvasAsImage(selectedFilePath, parent.getWidth(), parent.getHeight()); //we pass in the width and the height of the canvas so that only what the user sees gets exported.
                //If we reach here it means the export was successful
                JOptionPane.showMessageDialog(parent, "Image Export Successful!", "Export", JOptionPane.INFORMATION_MESSAGE);
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(parent, "Error: " + e.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    /**
     * Sets the line thickness of the shape to be drawn.
     * If the select mode is enabled,
     * the selected line thickness is also applied to the currently selected shape.
     *
     * @param  width  the line thicknss
     * @return        void
     */
    public void setBorderWidth(float width) {

        if (model.isSelectModeEnabled()) {
            model.strokeSelectedShape(new BasicStroke(width));
        }
        model.setBorderWidth(new BasicStroke(width));
    }



    /**
     * Tell the model to reset its drawing panel state.
     * This clears all the shapes drawn on the canvas as well as the undo/redo states.
     *
     * @param  None   No parameters required.
     * @return        No return value.
     */
    public void clearAllShapes() {
        model.clearAllShapes();
    }

    public void handleMousePressed(int x, int y) {
        this.startX = x;
        this.startY = y;
        newShape = true;

        if (model.isSelectModeEnabled()) {
            model.findShapeInPos(startX, startY); //find the shape that was clicked on
        }
    }

    public void handleMouseDragged(int x, int y) {
        if (!model.isSelectModeEnabled()) {
            if (newShape) { //create a new shape if the user has just clicked.
                model.drawShape(startX, startY, x, y, SHIFTKeyDown);
                newShape = false;
            }
            else { //otherwise it means the user is dragging the newly-drawn shape. So update that.
                model.updateLastShape(x, y, SHIFTKeyDown);
            }
        }
        else { //If we are on select mode, move the select shape
            model.moveSelectedShape(x-startX, y-startY); //calculate x and y offset and move shape by that amount.
            startX = x;
            startY = y; //allows us to keep track of the mouse position and calculate an offset for the next drag
        }
    }

    public void handleMouseReleased() {
        newShape = true; //the user has finished drawing/dragging so next time a new shape should be created.
    }
}
