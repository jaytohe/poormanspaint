package model.shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;

public class Rectangle extends Shape implements ShiftKeyModifiable, ColorFillable {


    private boolean drawSquare;
    private Color fillColor;
    public Rectangle(int startX, int startY, int endX, int endY, Color borderColor,Color fillColor, BasicStroke borderWidth, boolean drawSquare) {
        super(startX, startY, endX, endY, borderColor, borderWidth);
        this.drawSquare = drawSquare;

        this.fillColor = fillColor;
    }


    public void setSHIFTKeyState(boolean state) {
        drawSquare = state;
    }

    /**
     * Check if the passed in point is in the area of the rectangle.
     *
     * @param  x	the x coordinate
     * @param  y	the y coordinate
     * @return  	true iff point is on top/bottom/left/right edges of the rectangle or inside it.
     */
    @Override
    public boolean contains(int x, int y) {


        if (x >= getMinX() && x <= getMinX() + getWidth() && (y >= getMinY() && y <= getMinY() + getHeight())) {
            return true;
        } // check if point is in the area of the rectangle.


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

        return new Point2D.Double(midPointX, midPointY);
    }

    public void draw(Graphics2D g) {
        super.draw(g);
        
        Point topLeftPoint = getTopLeftPoint();
        int width = (int) (getWidth());
        int height = (int) (getHeight());
        if (drawSquare) {
            height = width = Math.max(width, height);
        }

        g.setColor(fillColor);
        g.fillRect(topLeftPoint.x, topLeftPoint.y, width, height);
        g.setColor(borderColor);
        g.drawRect(topLeftPoint.x, topLeftPoint.y, width, height);
        if (beforeRotation != null) {
            g.setTransform(beforeRotation);
        }
    }

    public Shape clone() {
        return new Rectangle(startPoint.x, startPoint.y, endPoint.x, endPoint.y, borderColor, fillColor, borderWidth, drawSquare);
    }

    @Override
    public Color getFillColor() {
        return fillColor;
    }

    @Override
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }
}
