package main;

import controller.CalcController;

import model.CalcModel;

import view.guiview.CalcGUIView;

public class MVCMain {
    public static void main(String[] args) {
        // create Model
        CalcModel model = new CalcModel();

        // Create controller
        CalcController controller = new CalcController(model);

        // Create View (GUI)
        new CalcGUIView(model, controller);
    }
}
