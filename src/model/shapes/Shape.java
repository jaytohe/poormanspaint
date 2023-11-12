package model.shapes;

import java.awt.Color;
import java.awt.Graphics;

public abstract class Shape {
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    private Color color;

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

    public abstract void draw(Graphics g);

    public abstract boolean contains(int x, int y);
}