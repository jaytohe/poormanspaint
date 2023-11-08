import java.util.ArrayList;
import java.util.List;

/**
 * Simple class describing a frog, which only knows its length and colour.  This
 * is the "model" part of an MVC program.
 *
 * This class uses the "listener" design pattern, also called the "observer"
 * pattern.  We define an interface Frog.Listener, and any object that wants to
 * listen for updates to this frog can implement it and add themselves with
 * `addListener`.  Any updates cause the `changed` method to be executed, which
 * notifies all listeners using their `update` method.
 *
 * Note that this class has NO UI FUNCTIONALITY WHATSOEVER.  It has:
 *   - No print statements
 *   - No Swing objects
 *   - No references to FrogPrinter or FrogGUI
 *
 * All it knows is that there may be some objects listening, and that they will
 * have an update method.  The rest is dealt with by the listeners themselves.
 * This separation is what you should aim for when making a model in MVC.
 */
public class Frog {

    /** Any object that wants to observe a Frog must implement this interface. */
    public interface Listener {
        void update();  // Called whenever the frog changes
    }

    /** Objects that are currently listening to this Frog */
    private List<Listener> listeners;

    private String colour;
    private int length;

    public Frog(String colour, int length) {
        this.colour = colour;
        this.length = length;

        listeners = new ArrayList<Listener>();  // No listeners to start with.
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        if (colour.equals("green") || colour.equals("red") || colour.equals("orange")) {
            this.colour = colour;
            changed();
        }
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        if (length > 0) {
            this.length = length;
            changed();
        }
    }

    /** Any object implementing Frog.Listener can "install" themselves here. */
    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    /** Called whenever something changes.  Notifies all the listeners. */
    private void changed() {
        for (Listener listener : listeners) {
            listener.update();
        }
    }

}
