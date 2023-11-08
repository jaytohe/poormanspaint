package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * A simple model class whose purpose is to store text entered by a user.
 *
 * The model's state is altered by user input when the delegate calls the addText() method below.
 *
 * The model supports change listeners to be notified when it changes. This form of loose
 * coupling permits the Delegate (View) to be updated when the model has changed.
 *
 * @author jonl
 */
public class SimpleModel {

    /** The text to store */
    private StringBuffer text;

    /** The change support object to help us fire change events at observers */
    private PropertyChangeSupport notifier;

    /**
     * Constructs a new SimpleModel instance. Initialises the StringBuffer and property change
     * support.
     */
    public SimpleModel() {
        text = new StringBuffer();
        notifier = new PropertyChangeSupport(this);
    }

    /**
     * Utility method to permit an observer to add themselves as an observer to the model's change
     * support object.
     *
     * @param listener the listener to add
     */
    public void addObserver(PropertyChangeListener listener) {
        notifier.addPropertyChangeListener(listener);
    }

    /**
     * This method is called by the GUI control aspect of the delegate to update the model according
     * to user input The method adds the text entered by the user to its buffer and calls
     * notifier.firePropertyChange so that the GUI view is notified that the model has changed and
     * can update the display
     *
     * @param str the string to add to the buffer
     */
    public void addText(String str) {
        String old = text.toString();
        text.append("\n" + str);
        notifier.firePropertyChange("theText", old, text.toString());
    }

    /**
     * This method can be called by the GUI view aspect of the delegate to get hold of the text in
     * the model's buffer.
     *
     * @return text.toString() the text to return as String.
     */
    public String getText() {
        return text.toString();
    }
}
