public class FrogPrinter implements Frog.Listener {

    private Frog frog;

    public FrogPrinter(Frog frog) {
        // Keep a reference to the model
        this.frog = frog;

        // Register as a listener so that the model notifies us of changes
        frog.addListener(this);
    }

    /** Print the frog's details.  Called whenever the frog changes in the model. */
    public void update() {
        System.out.println("The frog has changed!");
        System.out.println("It is " + frog.getLength() + " cm long and " + frog.getColour() + ".");
        System.out.println();
    }

}
