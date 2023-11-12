package model.shapes;

import java.awt.Graphics;

public class Triangle extends Shape {
    public Triangle(int startX, int startY, int endX, int endY) {
        super(startX, startY, endX, endY);


    }

    @Override
    public void draw(Graphics g) {

        int width = Math.abs(getEndX() - getStartX());
        int height = Math.abs(getEndY() - getStartY());
        int minX = Math.min(getStartX(), getEndX());
        int minY = Math.min(getStartY(), getEndY());
        
        int[] xPoints = {minX, minX + width, minX + width / 2};
        int[] yPoints = {minY + height, minY + height, minY};

        g.drawPolygon(xPoints, yPoints, 3);
    }

    @Override
    public boolean contains(int x, int y) {
        return false;
    }


    
}
