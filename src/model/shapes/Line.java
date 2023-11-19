package model.shapes;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;

public class Line extends Shape {
    
    public Line(int startX, int startY, int endX, int endY) {
        super(startX, startY, endX, endY);
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


        // Ideally we would just check if exceptedY == y.
        // But because of rounding/precision error, we give an uncertainty of 1 unit
        // to the difference between expectedY and y
        if (Math.abs(y - expectedY) <= 1.5) {
            // Check if the x value is within the bounds of the line
            if (x >= Math.min(startX, endX) && x <= Math.max(startX, endX)) {
                return true;
            }
        }

        return false;
    }


    /*
    protected void scale() {
        Point2D midpoint = getCentroid();

        double xDiff = midpoint.getX() - getStartPoint().getX();
        double yDiff = midpoint.getY() - getStartPoint().getY();

        scaledStartPoint = new Point((int) (midpoint.getX() - xDiff * scaleFactor), (int) (midpoint.getY() - yDiff * scaleFactor));
        
        xDiff = midpoint.getX() - getEndPoint().getX();
        yDiff = midpoint.getY() - getEndPoint().getY();

        scaledEndPoint = new Point((int) (midpoint.getX() - xDiff * scaleFactor), (int) (midpoint.getY() - yDiff * scaleFactor));
    }*/


    /*
    protected Point2D getCentroid() {
        //Midpoint of the x axis
        double midpointX = (getMinScaledX() + getMaxScaledX()) /2;
        
        //Midpoint of the y axis
        double midpointY = (getMinScaledY() + getMaxScaledY()) /2;
        
        return new Point2D.Double(midpointX, midpointY);
    }*/

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
