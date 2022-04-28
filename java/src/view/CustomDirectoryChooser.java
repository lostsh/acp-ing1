package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class CustomDirectoryChooser extends HBox{

    private TextField input = null;

    private File directory = null;

    public CustomDirectoryChooser(Stage s, String placeholder) {
        input = new TextField();
        input.setPromptText(placeholder);
        input.setDisable(true);
        input.setPrefWidth(300);
        Button button = new Button("\uD83D\uDDC1 Browse");
        button.setOnAction(new BrowseHandler(s));
        this.getChildren().addAll(input, button);
        this.setSpacing(5);
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
            //while (CustomDirectoryChooser.this.directory == null)
        }
    }
}
