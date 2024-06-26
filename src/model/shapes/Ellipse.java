package model.shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;


public class Ellipse extends Shape implements ShiftKeyModifiable, ColorFillable {

    private boolean drawCircle;
    private Color fillColor;
    public Ellipse(int startX, int startY, int endX, int endY, Color borderColor, Color fillColor, BasicStroke borderWidth, boolean drawCircle) {
        super(startX, startY, endX, endY, borderColor, borderWidth);
        this.drawCircle = drawCircle;
        this.fillColor = fillColor;
    }

    public void setSHIFTKeyState(boolean state) {
        drawCircle = state;

    }
    
    @Override
    public boolean contains(int x, int y) {
        // Calculate the center point of the ellipse

        int width = getWidth();
        int height = getHeight();
        if (drawCircle) {
            height = width;
        }
        int centerX = getMinX() + width / 2;
        int centerY = getMinY() + height / 2;
    
        // Calculate the radius of the ellipse
        int radiusX = width / 2;
        int radiusY = height / 2;
    
        // Check if the point is inside the ellipse using the formula for an ellipse:
        // ((x - centerX) ^ 2) / radiusX ^ 2 + ((y - centerY) ^ 2) / radiusY ^ 2 <= 1
        double result = Math.pow(x - centerX, 2) / Math.pow(radiusX, 2) + Math.pow(y - centerY, 2) / Math.pow(radiusY, 2);
        return result <= 1;
    }

    @Override
    public void draw(Graphics2D g) {

        super.draw(g);


        Point topLeftPoint = getTopLeftPoint();
        int width = getWidth();
        int height = getHeight();
        if (drawCircle) {

            //circle is an ellipse drawn in a bounded square.
            // set diameter as the max of the width and height of the bounded rectangle of ellipse.
            int diameter = Math.max(width, height);
            // To calculate the top left corner of the new bounded square.
            //new top left corner is midpoind of rectangle minus half the diameter
            int centerX = topLeftPoint.x + width/2 - diameter/2;
            int centerY = topLeftPoint.y + height/2 - diameter/2;

            g.setColor(fillColor);
            g.fillOval(centerX, centerY, diameter, diameter);
            g.setColor(borderColor);
            g.drawOval(centerX, centerY, diameter, diameter);     
        }
        else {
            g.setColor(fillColor);
            g.fillOval(topLeftPoint.x, topLeftPoint.y, width, height);
            g.setColor(borderColor);
            g.setStroke(borderWidth);
            g.drawOval(topLeftPoint.x, topLeftPoint.y, width, height);
        }


        if (beforeRotation != null) {
            g.setTransform(beforeRotation);
        }
    }

    @Override
    protected Point2D getCentroid() {
        // Calculate the center point of the ellipse
        double midpointX = (startPoint.x + getScaledEndPoint().x) / 2;
        double midpointY = (startPoint.y + getScaledEndPoint().y) / 2;
        return new Point2D.Double(midpointX, midpointY);
    }

    public Shape clone() {
        return new Ellipse(startPoint.x, startPoint.y, endPoint.x, endPoint.y, borderColor, fillColor, borderWidth, drawCircle);
    }

    @Override
    public Color getFillColor() {
        return this.fillColor;
    }

    @Override
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }
    
}
