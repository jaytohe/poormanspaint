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
        int[] xPoints = {getStartX(), getEndX(), getStartX() + ((getEndX() - getStartX()) / 2)};
        int[] yPoints = {getStartY(), getEndY(), getEndY()};

        // Calculate the area of the triangle using the given points
        double area = 0.5 * (-yPoints[1] * xPoints[2] + yPoints[0] * (-xPoints[1] + xPoints[2]) + xPoints[0] * (yPoints[1] - yPoints[2]) + xPoints[1] * yPoints[2]);
        double sign = area < 0 ? -1 : 1;

        // Calculate the barycentric coordinates 's' and 't' for the given point
        double s = (yPoints[0] * xPoints[2] - xPoints[0] * yPoints[2] + (yPoints[2] - yPoints[0]) * x + (xPoints[0] - xPoints[2]) * y) * sign;
        double t = (xPoints[0] * yPoints[1] - yPoints[0] * xPoints[1] + (yPoints[0] - yPoints[1]) * x + (xPoints[1] - xPoints[0]) * y) * sign;

        // Check if the point lies within the triangle based on the barycentric coordinates
        return s > 0 && t > 0 && (s + t) < 2 * area * sign;
    }

}
