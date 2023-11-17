package model.shapes;

import java.awt.Graphics;

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
        int startX = getStartX();
        int startY = getStartY();
        int endX = getEndX();
        int endY = getEndY();

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
    @Override
    public void draw(Graphics g) {
        g.drawLine(getStartX(), getStartY(), getEndX(), getEndY());
    }
}
