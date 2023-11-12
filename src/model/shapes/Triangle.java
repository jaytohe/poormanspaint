package model.shapes;

import java.awt.Graphics;

public class Triangle extends Shape {

    private int width;
    private int height;
    private int minX;
    private int minY;
    public Triangle(int startX, int startY, int endX, int endY) {
        super(startX, startY, endX, endY);

        width = Math.abs(getEndX() - getStartX());
        height = Math.abs(getEndY() - getStartY());
        minX = Math.min(getStartX(), getEndX());
        minY = Math.min(getStartY(), getEndY());

    }

    @Override
    public void draw(Graphics g) {
        //Draw the three lines to make us a nice triangle
        g.drawLine(minX, minY, (minX - minY / 2), (minY + height));
        g.drawLine(minX, minY, (minX + width / 2), (minY + height));
        g.drawLine((minX - width / 2), minY + height, (minX + width / 2), minY + height);
    }

    @Override
    public boolean contains(int x, int y) {
        return false;
    }


    
}
