package view;

import javax.swing.*;

import controller.Controller;
import model.ShapeType;
import model.shapes.Shape;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.LinkedList;

public class DrawingPanel extends JPanel {

    private Controller controller;
    private ShapeType currentShapeType = ShapeType.LINE;
    private LinkedList<Shape> shapes; //pointer to the shapes array in Model.
    // this allows us to just keep one array in the model and update that.

    public DrawingPanel(Controller controller) {
        this.controller = controller;

        this.shapes = new LinkedList<>();
        setBorder(BorderFactory.createLineBorder(Color.black));
        
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
            public void mouseDragged(MouseEvent e) {
                System.out.println("mouse dragged");
                controller.handleMouseDragged(e.getX(), e.getY());
            }
        });
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Shape shape : shapes) {
            shape.draw(g);
        }
    }

    public void updateShapesPointer(LinkedList<Shape> shapes) {
        this.shapes = shapes;
        revalidate();
        repaint();
    }

    public void setCurrentShapeType(ShapeType shapeType) {
        this.currentShapeType = shapeType;
    }


    
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
