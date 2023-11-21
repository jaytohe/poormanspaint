package model.shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;

public class Rectangle extends Shape implements ShiftKeyModifiable {


    private boolean drawSquare;

    public Rectangle(int startX, int startY, int endX, int endY, Color borderColor, BasicStroke borderWidth, boolean drawSquare) {
        super(startX, startY, endX, endY, borderColor, borderWidth);
        this.drawSquare = drawSquare;
    }

    public void setSHIFTKeyState(boolean state) {
        drawSquare = state;
    }

    /**
     * Check if the passed in point is in the perimeter of the rectangle.
     *
     * @param  x	the x coordinate
     * @param  y	the y coordinate
     * @return  	true iff point is on top/bottom/left/right edges of the rectangle.
     */
    @Override
    public boolean contains(int x, int y) {

        /*
        int width = Math.abs(getEndX() - getStartX());
        int height = Math.abs(getEndY() - getStartY());
        int minX = Math.min(getStartX(), getEndX());
        int minY = Math.min(getStartY(), getEndY());
        */
        //if (x >= minX && x <= minX + width && (y == minY || y == minY + height)) {
        if (x >= getMinX() && x <= getMinX() + getWidth() && (y >= getMinY() && y <= getMinY() + getHeight())) {
            return true;
        } // check if point is on top or bottom edge of the rectangle.

        //if (y >= minY && y <= minY + height && (x == minX) || x == minX + width) {
        //    return true;
        //} // check if point is on the left or right edges of the rectangle.

        return false;

    }


    protected Point2D getCentroid() {
        //Midpoint of the x axis
        //double midpointX = (getMinScaledX() + getMaxScaledX()) /2;
        //double midpointX = (getWidth()) / 2 + getMinX();

        int width = getWidth();
        int height = getHeight();

        if (drawSquare) {
            height = width = Math.max(width, height);
        } 

        //double midPointX = (startPoint.x + getScaledEndPoint().x) / 2;
        //double midPointY = (startPoint.y + getScaledEndPoint().y) / 2;

        //Midpoint of the x axis
        double midPointX = getTopLeftPoint().x + width / 2;

        //Midpoint of the y axis
        double midPointY = getTopLeftPoint().y + height / 2;

        
        //Midpoint of the y axis
        return new Point2D.Double(midPointX, midPointY);
    }

    public void draw(Graphics2D g) {
        super.draw(g);
        
        /*     int width = Math.abs(getEndX() - getStartX());
        int height = Math.abs(getEndY() - getStartY());
        int minX = Math.min(getStartX(), getEndX());
        int minY = Math.min(getStartY(), getEndY());
        */
        //Point scaledTopLeftPoint = getScaledTopLeftPoint();
        Point topLeftPoint = getTopLeftPoint();
        int width = (int) (getWidth());
        int height = (int) (getHeight());
        if (drawSquare) {
            height = width = Math.max(width, height);
        }
        g.drawRect(topLeftPoint.x, topLeftPoint.y, width, height);
        if (beforeRotation != null) {
            g.setTransform(beforeRotation);
        }
    }


    /* @Override
    protected void scale() {
        Point2D midpoint = getCentroid();

        double xDiff = midpoint.getX() - getStartPoint().getX();
        double yDiff = midpoint.getY() - getStartPoint().getY();

        scaledStartPoint = new Point((int) (midpoint.getX() - xDiff * scaleFactor), (int) (midpoint.getY() - yDiff * scaleFactor));
        
        xDiff = midpoint.getX() - getEndPoint().getX();
        yDiff = midpoint.getY() - getEndPoint().getY();

        scaledEndPoint = new Point((int) (midpoint.getX() - xDiff * scaleFactor), (int) (midpoint.getY() - yDiff * scaleFactor));
    }*/
}
