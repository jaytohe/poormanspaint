package model.shapes;

import java.awt.Graphics;

public class Rectangle extends Shape {

    private int minX;
    private int minY;
    private int width;
    private int height;
    public Rectangle(int startX, int startY, int endX, int endY) {
        super(startX, startY, endX, endY);
        width = Math.abs(getEndX() - getStartX());
        height = Math.abs(getEndY() - getStartY());
        minX = Math.min(getStartX(), getEndX());
        minY = Math.min(getStartY(), getEndY());

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
        if (x >= minX && x <= minX + width && (y == minY || y == minY + height)) {
            return true;
        } // check if point is on top or bottom edge of the rectangle.

        if (y >= minY && y <= minY + height && (x == minX) || x == minX + width) {
            return true;
        } // check if point is on the left or right edges of the rectangle.

        return false;

    }
    public void draw(Graphics g) {
        g.drawRect(minX, minY, width, height);
    }


}
