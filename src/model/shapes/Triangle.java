package model.shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

public class Triangle extends Shape {
    public Triangle(int startX, int startY, int endX, int endY, Color borderColor, BasicStroke borderWidth) {
        super(startX, startY, endX, endY, borderColor, borderWidth);
    }

    @Override
    public void draw(Graphics2D g) {
        super.draw(g);
        
        int width = getWidth();
        int height = getHeight();
        int minX = getTopLeftPoint().x;
        int minY = getTopLeftPoint().y;
        
        int[] xPoints = {minX, minX + width, minX + width / 2};
        int[] yPoints = {minY + height, minY + height, minY};

        g.drawPolygon(xPoints, yPoints, 3);

        if (beforeRotation != null) {
            g.setTransform(beforeRotation);
        }
    }
    @Override
    public boolean contains(int x, int y) {


        //int[] xPoints = {getStartX(), getEndX(), getStartX() + ((getEndX() - getStartX()) / 2)};
        //int[] yPoints = {getStartY(), getEndY(), getEndY()};

        int[] xPoints = {getStartPoint().x, getEndPoint().x, getStartPoint().x + ((getEndPoint().x - getStartPoint().x) / 2)};
        int[] yPoints = {getStartPoint().y, getEndPoint().y, getEndPoint().y};

        // Calculate the area of the triangle using the given points
        double area = 0.5 * (-yPoints[1] * xPoints[2] + yPoints[0] * (-xPoints[1] + xPoints[2]) + xPoints[0] * (yPoints[1] - yPoints[2]) + xPoints[1] * yPoints[2]);
        double sign = area < 0 ? -1 : 1;

        // Calculate the barycentric coordinates 's' and 't' for the given point
        double s = (yPoints[0] * xPoints[2] - xPoints[0] * yPoints[2] + (yPoints[2] - yPoints[0]) * x + (xPoints[0] - xPoints[2]) * y) * sign;
        double t = (xPoints[0] * yPoints[1] - yPoints[0] * xPoints[1] + (yPoints[0] - yPoints[1]) * x + (xPoints[1] - xPoints[0]) * y) * sign;

        // Check if the point lies within the triangle based on the barycentric coordinates
        return s > 0 && t > 0 && (s + t) < 2 * area * sign;
    }

    @Override
    protected Point2D getCentroid() {
        int width = getWidth();
        int height = getHeight();
        int minX = getTopLeftPoint().x;
        int minY = getTopLeftPoint().y;
        
        // The centroid of a triangle is given by ((x1+x2+x3)/3, (y1+y2+y3)/3)
        int[] xPoints = {minX, minX + width, minX + width / 2};
        int[] yPoints = {minY + height, minY + height, minY};

        return new Point2D.Double((xPoints[0] + xPoints[1] + xPoints[2]) / 3, (yPoints[0] + yPoints[1] + yPoints[2]) / 3);
    }

    public Shape clone() {
        return new Triangle(startPoint.x, startPoint.y, endPoint.x, endPoint.y, borderColor, borderWidth);
    }

}
