package controller;

import model.Model;


public class Controller {

    private Model model;
    public Controller(Model model) {
        this.model = model;
    }


    public void incrementClicks() {
        model.incrementClicks();
    }
}
