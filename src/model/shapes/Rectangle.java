package model.shapes;

import java.awt.Graphics;

public class Rectangle extends Shape {


    private boolean square;
    public Rectangle(int startX, int startY, int endX, int endY, boolean square) {
        super(startX, startY, endX, endY);
        this.square = square;
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

        int width = Math.abs(getEndX() - getStartX());
        int height = Math.abs(getEndY() - getStartY());
        int minX = Math.min(getStartX(), getEndX());
        int minY = Math.min(getStartY(), getEndY());

        if (x >= minX && x <= minX + width && (y == minY || y == minY + height)) {
            return true;
        } // check if point is on top or bottom edge of the rectangle.

        if (y >= minY && y <= minY + height && (x == minX) || x == minX + width) {
            return true;
        } // check if point is on the left or right edges of the rectangle.

        return false;

    }
    public void draw(Graphics g) {
        int width = Math.abs(getEndX() - getStartX());
        int height = Math.abs(getEndY() - getStartY());
        int minX = Math.min(getStartX(), getEndX());
        int minY = Math.min(getStartY(), getEndY());
        if (square) {
            width = Math.max(width, height);
            height = width;
        }
        g.drawRect(minX, minY, width, height);
    }


}
