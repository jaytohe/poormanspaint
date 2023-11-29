package model.shapes;

import java.awt.Color;

/**
 * Represents a shape that can be filled with a color
 * In our case this is any shape except a line.
 */
public interface ColorFillable {
    Color getFillColor();
    void setFillColor(Color fillColor);
}
