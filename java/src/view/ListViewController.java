package view;

import controller.Controller;
import javafx.collections.FXCollections;
import javafx.scene.control.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class ListViewController implements Observer {

    protected Controller controller;

    protected ListView<File> fileListView;

    public ListViewController(Controller controller, ListView<File> fileListView) {
        this.controller = controller;
        this.fileListView = fileListView;
    }

    @Override
    public void update(Observable o, Object arg) {
        if( arg instanceof ArrayList){
            ArrayList<File> f = (ArrayList<File>) arg;
            fileListView.setItems(FXCollections.observableArrayList(f));
        }
    }
}
