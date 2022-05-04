package view;

import controller.Controller;
import javafx.collections.FXCollections;
import javafx.scene.control.ListView;

import java.io.File;
import java.util.Observable;

public class ListViewTestingController extends ListViewController{
    public ListViewTestingController(Controller controller, ListView<File> fileListView) {
        super(controller, fileListView);
    }

    @Override
    public void update(Observable o, Object arg) {
        if(controller.getTestingFiles() != null)
            fileListView.setItems(FXCollections.observableArrayList(controller.getTestingFiles()));
    }
}
