package model;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Model {
    private int clicks;
    private int oldClicks;
    private PropertyChangeSupport notifier;


    public Model() {
        clicks = oldClicks = 0;
        this.notifier = new PropertyChangeSupport(this);
    }

    public void addListener(PropertyChangeListener pe) {
        notifier.addPropertyChangeListener(pe);
    }

    private void update() {
        notifier.firePropertyChange("clicks", oldClicks, clicks);
        oldClicks = clicks;
    }

    public void incrementClicks() {
        clicks++;
        update();
    }


}
