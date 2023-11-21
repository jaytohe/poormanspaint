package model;

import model.shapes.Shape;
import java.util.LinkedList;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageExporter {
    public static void exportShapesToImage(LinkedList<Shape> shapes, File selectedFilePath, int width, int height) 
    throws IOException, IllegalArgumentException {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        //Fill the entire image with solid white color, 
        //BEFORE any shapes are drawn on it.
        // This is to ensure that the image is non-transparent
        // and the shapes drawn on it can actually be seen.
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);


        // Draw shapes onto the Graphics2D Export Canvas
        for (Shape shape : shapes) {
            shape.draw(g2d);
        }

        // Save the image as a JPEG file
        ImageIO.write(image, "JPEG", selectedFilePath);
        System.out.println("Image exported successfully.");

        // Flush the Graphics2D Export Canvas
        g2d.dispose();

    }
}