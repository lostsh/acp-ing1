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
        // Uncomment if u want the button to be disabled when selector is not updated
        button.setDisable(/*!(arg instanceof File) ||*/ !controller.isLearningDirectory());
    }

    @Override
    public void handle(ActionEvent event) {
        controller.extractLearn();
    }
}
