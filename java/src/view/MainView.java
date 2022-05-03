package view;

import controller.Controller;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

public class MainView extends BorderPane{

    Stage primaryStage;

    ImageView averageImage;

    ListView<File> learningFiles;
    CustomDirectoryChooser learningDirectoryChooser;
    Button runLearningExtraction;
    Button runLearning;
    Label learningStatusIndicator;

    ListView<File> testingFiles;
    CustomDirectoryChooser testingDirectoryChooser;
    Button runRecognitionExtraction;
    Button runRecognition;
    Label recognitionRateIndicator;

    Controller controller;

    public MainView(Stage primaryStage){
        this.primaryStage = primaryStage;
        controller = new Controller();

        //this.setCenter(createCenter());
        this.setLeft(createLeft());
        controller.extractLearn(new File("../BDD/cropped&gray/learn"));
        controller.extractTest(new File("../BDD/cropped&gray/test/newphotosofpeopleinLearn"));
    }

    private Pane createLeft(){
        VBox p = new VBox(5);
        learningFiles = new ListView<>();
        learningFiles.setFocusTraversable(false);
        learningFiles.setMaxSize(300, 200);
        learningFiles.setPrefSize(300, 200);
        ListViewController learningController = new ListViewLearningController(controller, learningFiles);
        controller.addObserver(learningController);

        testingFiles = new ListView<>();
        testingFiles.setMaxSize(300, 200);
        testingFiles.setPrefSize(300, 200);
        ListViewController testingController = new ListViewTestingController(controller, testingFiles);
        controller.addObserver(testingController);

        p.getChildren().addAll(learningFiles, testingFiles);
        p.setPadding(new Insets(0, 5, 5, 5));
        return p;
    }
}
