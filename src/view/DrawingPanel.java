package view;

import javax.swing.*;

import controller.Controller;
import model.ShapeType;
import model.shapes.Shape;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class DrawingPanel extends JPanel {

    private final Controller controller;
    //private ShapeType currentShapeType = ShapeType.LINE;
    private List<Shape> shapes; //pointer to the shapes array in Model.
    // this allows us to just keep one array in the model and update that.

    public DrawingPanel(Controller controller) {
        this.controller = controller;

        this.shapes = new ArrayList<>();
        setBorder(BorderFactory.createLineBorder(Color.black));
        setFocusable(true); // Allow the panel to get focus

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                System.out.println("mouse pressed");
                controller.handleMousePressed(e.getX(), e.getY());
                
            }
            public void mouseReleased(MouseEvent e) {
                System.out.println("mouse released");
                controller.handleMouseReleased();
            }
        });
        
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                // Request focus when the mouse is moved over the panel
                // such that we can detect a SHIFT key press or release.
                requestFocusInWindow();
            }
            public void mouseDragged(MouseEvent e) {
                System.out.println("mouse dragged");
                controller.handleMouseDragged(e.getX(), e.getY());
            }
        });

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.isShiftDown()) {
                    // Shift key is pressed
                    System.out.println("Shift key pressed");
                    // Add your shift key logic here
                    controller.setSHIFTKeyState(true);
                }
            }

            public void keyReleased(KeyEvent e) {
                if (!e.isShiftDown()) {
                    // Shift key is released
                    System.out.println("Shift key released");
                    // Add your shift key release logic here
                    controller.setSHIFTKeyState(false);
                }
            }
        });

        requestFocusInWindow(); // Request focus for the panel initially
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        for (Shape shape : shapes) {
            shape.draw(g2);
        }
    }

    public void updateShapesPointer(List<Shape> shapes) {
        this.shapes = shapes;
        revalidate();
        repaint();
    }

    //public void setCurrentShapeType(ShapeType shapeType) {
    //    this.currentShapeType = shapeType;
    //}

    
    /*
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 400);
            DrawingPanel drawingPanel = new DrawingPanel();
            frame.add(drawingPanel);

            JButton lineButton = new JButton("Line");
            lineButton.addActionListener(e -> drawingPanel.setCurrentShapeType(ShapeType.LINE));

            JButton rectangleButton = new JButton("Rectangle");
            rectangleButton.addActionListener(e -> drawingPanel.setCurrentShapeType(ShapeType.RECTANGLE));

            JButton ovalButton = new JButton("Oval");
            ovalButton.addActionListener(e -> drawingPanel.setCurrentShapeType(ShapeType.OVAL));

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(lineButton);
            buttonPanel.add(rectangleButton);
            buttonPanel.add(ovalButton);
            frame.add(buttonPanel, BorderLayout.PAGE_START);

            frame.setVisible(true);
        });
    }
    */



}
