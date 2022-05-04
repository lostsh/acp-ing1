package view;

import controller.Controller;
import javafx.scene.control.Label;

import java.util.Observer;

public abstract class LabelController implements Observer {
    protected Controller controller;
    protected Label label;

    public LabelController(Controller controller, Label label) {
        this.controller = controller;
        this.label = label;
    }
}
