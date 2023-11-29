package model.shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;


public abstract class Shape {

    protected Point startPoint;
    protected Point scaledStartPoint;
    protected Point endPoint;
    protected Point scaledEndPoint;

    protected Color borderColor;
    protected BasicStroke borderWidth;
    protected double rotationAngle = 0.0;
    double scaleFactor = 1.0;
    protected AffineTransform beforeRotation;

    public Shape(int startX, int startY, int endX, int endY, Color borderColor, BasicStroke borderWidth) {
        this.startPoint = new Point(startX, startY);
        this.borderColor = borderColor;
        this.borderWidth = borderWidth;
        this.endPoint = new Point(endX, endY);
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }


    public int getMinX() {
        return (int) Math.min(startPoint.getX(), endPoint.getX());
    }


    public int getMaxX() {
        return (int) Math.max(startPoint.getX(), endPoint.getX());
    }


    public int getMinY() {
        return (int) Math.min(startPoint.getY(), endPoint.getY());
    }


    public int getMaxY() {
        return (int) Math.max(startPoint.getY(), endPoint.getY());
    }

    public Point getTopLeftPoint() {
        return new Point(getMinX(), getMinY());
    }

    public int getWidth() {
        return (int) (Math.abs(startPoint.getX() - endPoint.getX())*scaleFactor);
    }

    public int getHeight() {
        return (int) (Math.abs(startPoint.getY() - endPoint.getY())*scaleFactor);
    }

    public void setStartPoint(Point point) {
        startPoint = point;
    }

    public void setEndPoint(Point point) {
        endPoint = point;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color color) {
        this.borderColor = color;
    }

    public BasicStroke getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(BasicStroke stroke) {
        this.borderWidth = stroke;
    }

    public void setRotationAngle(int angle) {
        if (0 <= angle && angle <= 360) {
            rotationAngle = Math.toRadians(angle);
        }
    }

    public void setScaleFactor(double factor) {
        if (0 <= factor && factor <= 4) {
            scaleFactor = factor;
        }
    }

    public double getScaleFactor() {
        return scaleFactor;
    }
    
    public Point getScaledEndPoint() {
        double newEndPointX = ((endPoint.getX() - startPoint.getX())*scaleFactor) + startPoint.getX();
        double newEndPointY = ((endPoint.getY() - startPoint.getY())*scaleFactor) + startPoint.getY();
        return new Point((int) newEndPointX, (int) newEndPointY);
    }

    public double getRotationAngle() {
        return rotationAngle;
    }

    public void move(int dx, int dy) {
        //Move the start and end points
        // by offset defined by dx and dy
        startPoint.translate(dx, dy);
        endPoint.translate(dx, dy);
    }

    /**
     * Rotate a shape about its centre point
     *
     * @param  g  the Graphics2D shape to rotate
     */
    private void rotate(Graphics2D g) {
        beforeRotation = g.getTransform();
        Point2D centroid = getCentroid();
        g.rotate(rotationAngle, centroid.getX(), centroid.getY());
        //System.out.println("called g.rotate with rotationAngle: "+ rotationAngle);
    }


    public void draw(Graphics2D g) {
        beforeRotation = g.getTransform();
        g.setColor(borderColor);
        g.setStroke(borderWidth);
        rotate(g);
    }
    protected abstract Point2D getCentroid();
    public abstract boolean contains(int x, int y);
    public abstract Shape clone();
}