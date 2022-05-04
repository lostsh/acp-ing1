package view;

import controller.Controller;
import javafx.scene.control.Label;

import java.util.Observable;

public class LabelTestingStatusController extends LabelController{

    public LabelTestingStatusController(Controller controller, Label label) {
        super(controller, label);
    }

    @Override
    public void update(Observable o, Object arg) {
        label.setText("Status : Standby");
        if(!controller.isTestingDirectory()){
            label.setText("Status : Waiting for testing directory");
        }else{
            if(controller.getTestingFiles() == null){
                label.setText("Status : Ready to extract");
            }else{
                if(!(arg instanceof Boolean)){
                    if(!controller.isLearned()){
                        label.setText("Status : Need to learn");
                    }else{
                        label.setText("Status : Ready to Run");
                    }
                }else{
                    label.setText("Status : Recognition done");
                }
            }
        }
    }
}
