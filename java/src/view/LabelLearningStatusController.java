package view;

import controller.Controller;
import javafx.scene.control.Label;

import java.util.Observable;

public class LabelLearningStatusController extends LabelController{

    public LabelLearningStatusController(Controller controller, Label label) {
        super(controller, label);
    }

    @Override
    public void update(Observable o, Object arg) {
        if(!controller.isLearningDirectory()){
            label.setText("Status : Waiting for learning directory");
        }else{
            if(controller.getLearningFiles() == null){
                label.setText("Status : Ready to extract");
            }else{
                if(!controller.isLearned()){
                    label.setText("Status : Ready to learn");
                }else{
                    label.setText("Status : Learning done");
                }
            }
        }
    }
}
