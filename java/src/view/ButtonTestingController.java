package view;

import controller.Controller;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

import java.util.Observable;

public class ButtonTestingController extends ButtonController{

    public ButtonTestingController(Controller controller, Button button) {
        super(controller, button);
    }

    @Override
    public void update(Observable o, Object arg) {
        button.setDisable(!controller.isTestingDirectory() || controller.getTestingFiles() == null);
    }

    @Override
    public void handle(ActionEvent event) {
        controller.test();
        controller.compareDistances();
    }
}
