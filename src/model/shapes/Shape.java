package model.shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;


public abstract class Shape {
    //protected int startX;
    //protected int startY;
    protected Point startPoint;
    protected Point scaledStartPoint;
    //protected int scaledStartX;
    //protected int scaledStartY;
    //protected int endX;
    //protected int endY;
    protected Point endPoint;
    protected Point scaledEndPoint;
    //protected int scaledEndX;
    //rotected int scaledEndY;
    protected Color color;
    private double rotationAngle = 0.0;
    double scaleFactor = 1.0;
    protected AffineTransform beforeRotation;

    public Shape(int startX, int startY, int endX, int endY) {
        this.startPoint = new Point(startX, startY);
        //this.startX = this.scaledStartX =startX;
        //this.startY = this.scaledStartY = startY;
        //this.scaledStartPoint = new Point(startX, startY);
        this.endPoint = new Point(endX, endY);
        //this.scaledEndPoint = new Point(endX, endY);
        //this.endX = this.scaledEndX = endX;
        //this.endY = this.scaledEndY = endY;
    }


    /*
    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }
    */

    public Point getStartPoint() {
        return startPoint;
    }


    /*
    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }
    */

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

    /*
    public void setStartX(int x) {
        startX = x;
    }

    public void setStartY(int y) {
        startY = y;
    }*/


    public void setStartPoint(Point point) {
        startPoint = point;
    }

    /*   public void setEndX(int x) {
        endX = x;
    }

    public void setEndY(int y) {
        endY = y;
    } */


    public void setEndPoint(Point point) {
        endPoint = point;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
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
        //scaleEndPoint();
    }
    
    public Point getScaledEndPoint() {
        double newEndPointX = ((endPoint.getX() - startPoint.getX())*scaleFactor) + startPoint.getX();
        double newEndPointY = ((endPoint.getY() - startPoint.getY())*scaleFactor) + startPoint.getY();
        return new Point((int) newEndPointX, (int) newEndPointY);
    }


    protected Point getRotatedPoint(Point p) {
        // Calculate the midpoint of the line
        double midpointX = (startPoint.getX() + getScaledEndPoint().getX()) / 2;
        double midpointY = (startPoint.getY() + getScaledEndPoint().getY()) / 2;
    
        // Calculate the distance from the midpoint to the end point
        double distance = Math.sqrt(Math.pow(p.getX() - midpointX, 2) + Math.pow(p.getY() - midpointY, 2));
    
        // Calculate the angle between the line and the x-axis
        double angle = Math.atan2(p.getY() - midpointY, p.getX() - midpointX);
    
        // Apply the rotation transformation to the end point
        double rotatedStartX = midpointX + distance * Math.cos(angle + rotationAngle);
        double rotatedStartY = midpointY + distance * Math.sin(angle + rotationAngle);
    
        return new Point((int) rotatedStartX, (int) rotatedStartY);

    }

    protected Point getRotatedStartPoint() {
        // Calculate the midpoint of the line
        double midpointX = (startPoint.getX() + getScaledEndPoint().getX()) / 2;
        double midpointY = (startPoint.getY() + getScaledEndPoint().getY()) / 2;
    
        // Calculate the distance from the midpoint to the end point
        double distance = Math.sqrt(Math.pow(startPoint.getX() - midpointX, 2) + Math.pow(startPoint.getY() - midpointY, 2));
    
        // Calculate the angle between the line and the x-axis
        double angle = Math.atan2(startPoint.getY() - midpointY, startPoint.getX() - midpointX);
    
        // Apply the rotation transformation to the end point
        double rotatedStartX = midpointX + distance * Math.cos(angle + rotationAngle);
        double rotatedStartY = midpointY + distance * Math.sin(angle + rotationAngle);
    
        return new Point((int) rotatedStartX, (int) rotatedStartY);
    }

    protected Point getRotatedEndPoint() {
        // Calculate the midpoint of the line
        double midpointX = (startPoint.getX() + getScaledEndPoint().getX()) / 2;
        double midpointY = (startPoint.getY() + getScaledEndPoint().getY()) / 2;
    
        // Calculate the distance from the midpoint to the end point

        double distance = endPoint.distanceSq(midpointX, midpointY);
        //double distance = Math.sqrt(Math.pow(endPoint.getX() - midpointX, 2) + Math.pow(endPoint.getY() - midpointY, 2));
    
        // Calculate the angle between the line and the x-axis
        double angle = Math.atan2(endPoint.getY() - midpointY, endPoint.getX() - midpointX);
    
        // Apply the rotation transformation to the end point
        double rotatedEndX = midpointX + distance * Math.cos(angle + rotationAngle);
        double rotatedEndY = midpointY + distance * Math.sin(angle + rotationAngle);
    
        return new Point((int) rotatedEndX, (int) rotatedEndY);
    }
    

    public Point updateEndPoint(double scaleFactor) {
        double deltaX = endPoint.getX() - startPoint.getX();
        double deltaY = endPoint.getY() - startPoint.getY();
        
        double scaledDeltaX = deltaX * scaleFactor;
        double scaledDeltaY = deltaY * scaleFactor;
        
        double newEndX = startPoint.getX() + scaledDeltaX;
        double newEndY = startPoint.getY() + scaledDeltaY;
        
        // Adjust the newEndX and newEndY based on rotation angle
        double rotatedDeltaX = newEndX - startPoint.getX();
        double rotatedDeltaY = newEndY - startPoint.getY();
        
        double cosTheta = Math.cos(rotationAngle);
        double sinTheta = Math.sin(rotationAngle);
        
        double adjustedDeltaX = cosTheta * rotatedDeltaX - sinTheta * rotatedDeltaY;
        double adjustedDeltaY = sinTheta * rotatedDeltaX + cosTheta * rotatedDeltaY;
        
        newEndX = startPoint.getX() + adjustedDeltaX;
        newEndY = startPoint.getY() + adjustedDeltaY;
        
        return new Point((int) newEndX, (int) newEndY);
    }

    public double getRotationAngle() {
        return rotationAngle;
    }

    public void move(int dx, int dy) {
        //Move the start and end points
        // by offset defined by dx and dy

        startPoint.translate(dx, dy);
        //startX += dx;
        //startY += dy;
        //endX += dx;
        //endY += dy;
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
        System.out.println("called g.rotate with rotationAngle: "+ rotationAngle);
    }


    public void draw(Graphics2D g) {
        beforeRotation = g.getTransform();
        //scale();
        rotate(g);
    }

    protected abstract Point2D getCentroid();
    //public abstract void draw(Graphics2D g);
    //protected abstract void scale();
    public abstract boolean contains(int x, int y);
}