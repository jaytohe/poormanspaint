package model.shapes;

import java.awt.Graphics;


public class Ellipse extends Shape implements ShiftKeyModifiable {

    private boolean drawCircle;
    public Ellipse(int startX, int startY, int endX, int endY, boolean drawCircle) {
        super(startX, startY, endX, endY);
        this.drawCircle = drawCircle;
    }

    public void setSHIFTKeyState(boolean state) {
        drawCircle = state;
    }

    @Override
    public void draw(Graphics g) {

        //get width and height of the bounding rectangle of the ellipse.
        int width = Math.abs(getEndX() - getStartX());
        int height = Math.abs(getEndY() - getStartY());

        //find the coordinates of the top left corner of the bounding rectangle.
        int minX = Math.min(getStartX(), getEndX());
        int minY = Math.min(getStartY(), getEndY());

        
        if (drawCircle) {

            //circle is an ellipse drawn in a bounded square.
            // set diameter as the max of the width and height of the bounded rectangle of ellipse.
            int diameter = Math.max(width, height);
            // To calculate the top left corner of the new bounded square.
            //new top left corner is midpoind of rectangle minus half the diameter of the circle.
            int centerX = minX + width/2 - diameter/2;
            int centerY = minY + height/2 - diameter/2;
            g.drawOval(centerX, centerY, diameter, diameter);      
        }
        else {
            g.drawOval(minX, minY, width, height);
        }
    }

    @Override
    public boolean contains(int x, int y) {
        return false;
    }
    
}
