package model.shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;

public class Line extends Shape {
    
    public Line(int startX, int startY, int endX, int endY, Color borderColor, BasicStroke borderWidth) {
        super(startX, startY, endX, endY, borderColor, borderWidth);
    }

    /**
     * Check if the passed in point is on the line and within bounds
     *
     * @param  x  the x coordinate of the point to check
     * @param  y  the y coordinate of the point to check
     * @return    true if the point is on the line and within bounds, false otherwise
     */
    @Override
    public boolean contains(int x, int y) {
        //check if point is on the line and within bounds


        Point startPoint = getRotatedStartPoint();
        Point endPoint = getRotatedEndPoint();
        
        double startX = startPoint.getX();
        double startY = startPoint.getY();
        double endX = endPoint.getX();
        double endY = endPoint.getY();
    

        // Calculate the slope of the line
        double slope = (double) (endY - startY) / (endX - startX);

        // Calculate the y-intercept of the line
        double yIntercept = startY - slope * startX; // y = mx +b; solve for unknown b: b = y - mx

        // Calculate the expected y value for the passed in point's x value
        double expectedY = slope * x + yIntercept;

        float lineWidth = borderWidth.getLineWidth();

        // Ideally we would just check if exceptedY == y.
        // But because of rounding/precision error and the fact that the user may  have picked a different line width
        // we give an uncertainty of lineWidth (default: 1) + 0.5 units in either direction
        // to the difference between expectedY and y.
        if (Math.abs(y - expectedY) <= lineWidth + 0.5) {
            // Check if the x value is within the bounds of the line
            if (x >= Math.min(startX, endX) && x <= Math.max(startX, endX)) {
                return true;
            }
        }

        return false;
    }



    protected Point getRotatedStartPoint() {
        // Calculate the midpoint of the line
        double midpointX = (startPoint.getX() + getScaledEndPoint().getX()) / 2;
        double midpointY = (startPoint.getY() + getScaledEndPoint().getY()) / 2;
    
        // Calculate the distance from the midpoint to the end point
        double distance = Math.sqrt(Math.pow(startPoint.getX() - midpointX, 2) + Math.pow(startPoint.getY() - midpointY, 2));
    
        // Calculate the angle between the line and the x-axis
        double angle = Math.atan2(startPoint.getY() - midpointY, startPoint.getX() - midpointX);
    
        // Apply the rotation transformation to the end point
        double rotatedStartX = midpointX + distance * Math.cos(angle + rotationAngle);
        double rotatedStartY = midpointY + distance * Math.sin(angle + rotationAngle);
    
        return new Point((int) rotatedStartX, (int) rotatedStartY);
    }

    protected Point getRotatedEndPoint() {
        // Calculate the midpoint of the line
        double midpointX = (startPoint.getX() + getScaledEndPoint().getX()) / 2;
        double midpointY = (startPoint.getY() + getScaledEndPoint().getY()) / 2;
    
        // Calculate the distance from the midpoint to the end point

        double distance = endPoint.distanceSq(midpointX, midpointY);
        //double distance = Math.sqrt(Math.pow(endPoint.getX() - midpointX, 2) + Math.pow(endPoint.getY() - midpointY, 2));
    
        // Calculate the angle between the line and the x-axis
        double angle = Math.atan2(endPoint.getY() - midpointY, endPoint.getX() - midpointX);
    
        // Apply the rotation transformation to the end point
        double rotatedEndX = midpointX + distance * Math.cos(angle + rotationAngle);
        double rotatedEndY = midpointY + distance * Math.sin(angle + rotationAngle);
    
        return new Point((int) rotatedEndX, (int) rotatedEndY);
    }
    protected Point2D getCentroid() {
        //Midpoint of the x axis
        double midpointX = (startPoint.x + getScaledEndPoint().x) / 2;
        //Midpoint of the y axis
        double midpointY = (startPoint.y + getScaledEndPoint().y) / 2;
        return new Point2D.Double(midpointX, midpointY);    
    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g);
        g.drawLine(startPoint.x, startPoint.y, getScaledEndPoint().x, getScaledEndPoint().y);
        if (beforeRotation != null) {
            g.setTransform(beforeRotation);
        }
    }
}
