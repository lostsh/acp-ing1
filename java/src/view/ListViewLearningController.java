package view;

import controller.Controller;
import javafx.collections.FXCollections;
import javafx.scene.control.ListView;

import java.io.File;
import java.util.Observable;

public class ListViewLearningController extends ListViewController{
    public ListViewLearningController(Controller controller, ListView<File> fileListView) {
        super(controller, fileListView);
    }

    @Override
    public void update(Observable o, Object arg) {
        if( null != controller.getLearningFiles() )
            fileListView.setItems(FXCollections.observableArrayList(controller.getLearningFiles()));
    }
}
