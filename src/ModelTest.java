import org.junit.Test;

import model.Model;
import model.ShapeType;
import model.shapes.*;

import static org.junit.Assert.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.List;

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
    assertTrue(model.getShapes().get(0) instanceof Rectangle);
    assertTrue(model.getShapes().get(1) instanceof Triangle);
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
    
    List<Shape> shapes = model.getShapes();
    Shape shape = shapes.get(shapes.size() - 1);

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
    
    List<Shape> shapes = model.getShapes();
    Shape shape = shapes.get(shapes.size() - 1);

    // Retrieve the shapes and assert that the shape's properties have been updated
    assertEquals(borderWidth, shape.getBorderWidth());
    assertEquals(borderWidth.getLineWidth(), shape.getBorderWidth().getLineWidth(), 0.01);
  }

}