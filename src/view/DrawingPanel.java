package view;

import controller.Controller;
import model.shapes.Shape;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;


/*
 * 
 * Custom JPanel that allows us to draw shapes on it.
 * 
 * This is achieved by three parts:
 * A) We listen for mouse events on the panel (press, drag, release, mouseove) or a SHIFT-key press/release and pass these events to the controller
 * B) We store a pointer/reference to the shapes array in the model which gets updated whenever a "drawnShapes" propertyChange gets received by the View.
 * C) We override the JPanel's paintComponent() method to draw the shapes present in the shapes array pointer passed in.
 */
public class DrawingPanel extends JPanel {

    private final Controller controller;
    private List<Shape> shapes; //pointer to the shapes array in Model.
    // this allows us to just keep one array in the model and update that.

    public DrawingPanel(Controller controller) {
        this.controller = controller;

        this.shapes = new ArrayList<>();
        setBorder(BorderFactory.createLineBorder(Color.black)); //add a black border around the panel to make the drawin area distinguishable.
        setFocusable(true); // Allow the panel to get focus

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                //System.out.println("mouse pressed");
                controller.handleMousePressed(e.getX(), e.getY());
                
            }
            public void mouseReleased(MouseEvent e) {
                //System.out.println("mouse released");
                controller.handleMouseReleased();
            }
        });
        
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                // Request focus whenever the mouse is moved over the panel
                // such that we can detect a SHIFT key press or release.
                requestFocusInWindow();
            }
            public void mouseDragged(MouseEvent e) {
                //System.out.println("mouse dragged");
                controller.handleMouseDragged(e.getX(), e.getY());
            }
        });

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.isShiftDown()) {
                    // Shift key is pressed
                    //System.out.println("Shift key pressed");
                    controller.setSHIFTKeyState(true);
                }
            }

            public void keyReleased(KeyEvent e) {
                if (!e.isShiftDown()) {
                    // Shift key is released
                    //System.out.println("Shift key released");
                    controller.setSHIFTKeyState(false);
                }
            }
        });

        requestFocusInWindow(); // Request focus for the panel initially
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g; //cast to Graphics2D so that each shape can have more control over what can be drawn.
        for (Shape shape : shapes) {
            shape.draw(g2);
        }
    }

    public void updateShapesPointer(List<Shape> shapes) {
        this.shapes = shapes;
        revalidate(); // this is needed cus for some reason repaint() by itself doesn't work
        repaint(); // notify the UI that the panel has been updated.
    }

}
