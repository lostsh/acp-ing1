package view;

import controller.Controller;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

import java.util.Observable;

public class ButtonLearningController extends ButtonController{

    public ButtonLearningController(Controller controller, Button button) {
        super(controller, button);
    }

    @Override
    public void update(Observable o, Object arg) {
        button.setDisable(!controller.isLearningDirectory() || controller.getLearningFiles() == null);
    }

    @Override
    public void handle(ActionEvent event) {
        controller.learn();
    }
}
