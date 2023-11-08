package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class CalcModel {

    private PropertyChangeSupport notifier;  // tracks and notifies listeners

    private double total;  // current total in the calculator
    private double lastTotal;  // total before the last change

    public CalcModel() {
        this.total = 0.0;
        this.lastTotal = 0.0;
        this.notifier = new PropertyChangeSupport(this);
    }

    /** Register a listener so it will be notified of any changes. */
    public void addListener(PropertyChangeListener listener) {
        notifier.addPropertyChangeListener(listener);
    }

    /** Broadcast most recent change to all listeners */
    private void update() {
        notifier.firePropertyChange("total", lastTotal, total);
        lastTotal = total;  // reset for next change
    }

    public void reset() {
        total = 0.0;
        update();
    }

    public void add(double value) {
        total = total + value;
        update();
    }

    public void subtract(double value) {
        total = total - value;
        update();
    }

    public void multiply(double value) {
        total = total * value;
        update();
    }

    public void divide(double value) {
        total = total / value;
        update();
    }

    public double getTotal() {
        return total;
    }
}
