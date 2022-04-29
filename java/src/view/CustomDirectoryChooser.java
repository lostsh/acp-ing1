package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Observable;

public class CustomDirectoryChooser extends Observable {

    private TextField input = null;

    private File directory = null;

    private Button button = null;

    public CustomDirectoryChooser(Stage s, String placeholder) {
        input = new TextField();
        button = new Button("\uD83D\uDDC1 Browse");
        input.setPromptText(placeholder);
        input.setDisable(true);
        input.setPrefWidth(300);
        button.setOnAction(new BrowseHandler(s));
    }

    public Pane getPane(){
        HBox p = new HBox();
        p.getChildren().addAll(input, button);
        p.setSpacing(5);
        return p;
    }

    public boolean isValid(){
        return directory != null && directory.isDirectory();
    }

    public File getDirectory(){ return directory; }

    class BrowseHandler implements EventHandler<ActionEvent>{

        private Stage stage;

        public BrowseHandler(Stage s){
            this.stage = s;
        }

        @Override
        public void handle(ActionEvent event) {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Choose directory");
            chooser.setInitialDirectory(new File("../"));
            CustomDirectoryChooser.this.directory = chooser.showDialog(this.stage);
            CustomDirectoryChooser.this.input.
                    setText(CustomDirectoryChooser.this.directory.toString());
            CustomDirectoryChooser.this.setChanged();
            CustomDirectoryChooser.this.notifyObservers();
        }
    }
}
