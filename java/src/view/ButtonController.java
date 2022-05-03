package view;

import controller.Controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

import java.util.Observer;

public abstract class ButtonController implements Observer, EventHandler<ActionEvent> {

    protected Controller controller;

    protected Button button;

    public ButtonController(Controller controller, Button button){
        this.controller = controller;
        this.button = button;
    }

    /*
    @Override
    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Unsupported operation exception");
    }*/
}
