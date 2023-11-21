package test;

import org.junit.Test;

import model.Model;
import model.ShapeType;
import model.shapes.*;

import static org.junit.Assert.*;

import java.awt.BasicStroke;
import java.awt.Color;

public class ModelTest {
  
  @Test
  public void testAddShape() {
    Model model = new Model();
    

    model.setShapeType(ShapeType.LINE);
    
    // Draw a line.
    model.drawShape(10, 10, 100, 100, false);
    
    // Retrieve the shapes and assert that the added shape is present
    assertEquals(1, model.getShapes().size());
    assertTrue(model.getShapes().get(0) instanceof Line);
  }
  
  @Test
  public void testTwoShapes() {
    Model model = new Model();
    
    // Add a rectangle

    model.setShapeType(ShapeType.RECTANGLE);
    model.drawShape(10, 10, 100, 100, true);

    // Add a traingle
    model.setShapeType(ShapeType.TRIANGLE);
    model.drawShape(123, 22, 33, 21, true);
    
    
    // Retrieve the shapes and check that the list is of size 2.
    assertEquals(2, model.getShapes().size());

    // Make sure that the triangle is first in the list
    assertTrue(model.getShapes().get(0) instanceof Triangle);
    assertTrue(model.getShapes().get(1) instanceof Rectangle);
  }

  @Test
  public void testRemoveShapes() {
    Model model = new Model();
    
    // Add a rectangle
    model.setShapeType(ShapeType.RECTANGLE);
    model.drawShape(10, 10, 100, 100, true);

    // Add a traingle
    model.setShapeType(ShapeType.TRIANGLE);
    model.drawShape(123, 22, 33, 21, true);

    
    // Retrieve the shapes and check that the list is of size 2.
    assertEquals(2, model.getShapes().size());

    model.clearAllShapes();

    assertEquals(0, model.getShapes().size());
  }
  

  
  @Test
  public void testDrawColor() {
    Model model = new Model();

    // Update the shape's properties
    model.setBorderColor(Color.CYAN);

    // Add a shape
    model.setShapeType(ShapeType.ELLIPSE);
    model.drawShape(0, 0, 50, 60, false);
    
    Shape shape = model.getShapes().getLast();

    // Retrieve the shapes and assert that the shape's properties have been updated
    assertEquals(Color.CYAN, shape.getBorderColor());
  }

@Test
  public void testDrawLineWidth() {
    Model model = new Model();


    BasicStroke borderWidth = new BasicStroke(6.0f);
    // Update the shape's properties
    model.setBorderWidth(borderWidth);

    // Add a shape
    model.setShapeType(ShapeType.ELLIPSE);
    model.drawShape(0, 0, 50, 60, false);
    
    Shape shape = model.getShapes().getLast();

    // Retrieve the shapes and assert that the shape's properties have been updated
    assertEquals(borderWidth, shape.getBorderWidth());
    assertEquals(borderWidth.getLineWidth(), shape.getBorderWidth().getLineWidth(), 0.01);
  }
  


  /*
  @Test
  public void testSelectShape() {
    Model model = new Model();
    
    // Add shapes
    Shape shape1 = new Rectangle(10, 10, 100, 100);
    Shape shape2 = new Ellipse(50, 50, 80, 80);
    model.addShape(shape1);
    model.addShape(shape2);
    
    // Select a shape
    model.selectShape(shape1);
    
    // Retrieve the selected shape and assert that it matches the expected shape
    assertEquals(shape1, model.getSelectedShape());
  }
  
  @Test
  public void testMoveShape() {
    Model model = new Model();
    
    // Add a shape
    Shape shape = new Rectangle(10, 10, 100, 100);
    model.addShape(shape);
    
    // Move the shape to a new position
    model.moveShape(shape, 50, 50);
    
    // Retrieve the shapes and assert that the shape's position has been updated
    assertEquals(50, shape.getX());
    assertEquals(50, shape.getY());
  }
  
  @Test
  public void testExportCanvasAsImage() {
    Model model = new Model();
    
    // Add shapes
    model.addRectangle(10, 10, 100, 100);
    model.addEllipse(50, 50, 80, 80);
    
    // Export the shapes as an image
    model.exportCanvasAsImage("output.png");
    
    // Verify that the image file has been created
    File imageFile = new File("output.png");
    assertTrue(imageFile.exists());
  }

  */
}