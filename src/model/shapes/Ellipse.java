package model.shapes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;


public class Ellipse extends Shape implements ShiftKeyModifiable {

    private boolean drawCircle;

    public Ellipse(int startX, int startY, int endX, int endY, boolean drawCircle) {
        super(startX, startY, endX, endY);
        this.drawCircle = drawCircle;
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

        //get width and height of the bounding rectangle of the ellipse.
        //int width = Math.abs(getEndX() - getStartX());
        //int height = Math.abs(getEndY() - getStartY());

        //find the coordinates of the top left corner of the bounding rectangle.
        //int minX = Math.min(getStartX(), getEndX());
        //int minY = Math.min(getStartY(), getEndY());

        Point topLeftPoint = getTopLeftPoint();
        int width = getWidth();
        int height = getHeight();
        if (drawCircle) {

            //circle is an ellipse drawn in a bounded square.
            // set diameter as the max of the width and height of the bounded rectangle of ellipse.
            int diameter = Math.max(width, height);
            // To calculate the top left corner of the new bounded square.
            //new top left corner is midpoind of rectangle minus half the diameter
            //int centerX = getMinX() + getWidth()/2 - diameter/2;
            //int centerY = getMinY() + getHeight()/2 - diameter/2;

            int centerX = topLeftPoint.x + width/2 - diameter/2;
            int centerY = topLeftPoint.y + height/2 - diameter/2;
            g.drawOval(centerX, centerY, diameter, diameter);      
        }
        else {
            //g.drawOval(getMinX(), getMinY(), getWidth(), getHeight());
            g.drawOval(topLeftPoint.x, topLeftPoint.y, width, height);
        }


        if (beforeRotation != null) {
            g.setTransform(beforeRotation);
        }
    }

    @Override
    protected Point2D getCentroid() {
        // Calculate the center point of the ellipse
        //int centerX = getMinX() + getWidth() / 2;
        //int centerY = getMinY() + getHeight() / 2;
        //return new Point2D.Double(centerX, centerY);

        

        double midpointX = (startPoint.x + getScaledEndPoint().x) / 2;
        //Midpoint of the y axis
        //double midpointY = (getMinScaledY() + getMaxScaledY()) /2;
        //double midpointY = (getHeight()) /2 + getMinY();
        double midpointY = (startPoint.y + getScaledEndPoint().y) / 2;
        return new Point2D.Double(midpointX, midpointY);
    }
    
}
