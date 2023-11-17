package model.shapes;

import java.awt.Color;
import java.awt.Graphics;

public abstract class Shape {
    protected int startX;
    protected int startY;
    protected int endX;
    protected int endY;
    protected Color color;

    public Shape(int startX, int startY, int endX, int endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }

    public void setStartX(int x) {
        startX = x;
    }

    public void setStartY(int y) {
        startY = y;
    }

    public void setEndX(int x) {
        endX = x;
    }

    public void setEndY(int y) {
        endY = y;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void move(int dx, int dy) {
        //Move the start and end points
        // by offset defined by dx and dy
        startX += dx;
        startY += dy;
        endX += dx;
        endY += dy;
    }

    public abstract void draw(Graphics g);
    public abstract boolean contains(int x, int y);
}