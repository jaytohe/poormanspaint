package model;
    /*
     * Enum class for the different types of drawable shapes this program supports.
     * 
     * This enum allows us to update the selected shape button in the view by conditioning on the shape type
     * 
     * It also allows us to know what type of shape the user wants to draw in the drawShape of the model.
     */
    public enum ShapeType {
        LINE,
        RECTANGLE,
        TRIANGLE,
        ELLIPSE
    }
