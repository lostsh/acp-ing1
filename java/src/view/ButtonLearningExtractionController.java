package view;

import controller.Controller;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

import java.util.Observable;

public class ButtonLearningExtractionController extends ButtonController{

    public ButtonLearningExtractionController(Controller controller, Button button) {
        super(controller, button);
    }

    @Override
    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Action when Controller is updated check clicability status");
    }

    @Override
    public void handle(ActionEvent event) {
        throw new UnsupportedOperationException("Action when button clicked controller extraction launched");
    }
}
