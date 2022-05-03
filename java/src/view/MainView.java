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

        this.setCenter(createCenter());
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

    private Pane createCenter(){
        VBox p = new VBox(5);
        p.setPadding(new Insets(0, 5, 5, 0));
        p.setAlignment(Pos.BASELINE_CENTER);

        // Top part : learning
        HBox top = new HBox(5);
        top.setPrefWidth(700);
        top.setPrefHeight(200);
        top.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-border-radius: 2px;");
        top.setPadding(new Insets(5));
        top.setAlignment(Pos.CENTER);
        // Bottom part : testing
        HBox btm = new HBox(5);
        btm.setPrefSize(top.getPrefWidth(), top.getPrefHeight());
        btm.setStyle(top.getStyle());
        btm.setPadding(top.getPadding());

        // ImageView container layout
        HBox avgImContainer = new HBox();
        averageImage = new ImageView();
        //averageImage.setImage(new Image(new File("../BDD/cropped&gray/average.jpg").toURI().toString()));
        averageImage.setFitWidth(188);
        averageImage.setFitHeight(188);
        controller.addObserver(new ImageViewController(averageImage));
        Label l = new Label("Average image");
        l.setStyle("-fx-rotate: -90; -fx-min-width: 100;");
        avgImContainer.setMaxWidth(270);
        avgImContainer.setPrefWidth(270);
        avgImContainer.getChildren().addAll(l, averageImage);
        avgImContainer.setAlignment(Pos.CENTER_RIGHT);

        // input layout
        /*
        HBox leftContainer = new HBox(5);
        leftContainer.setMaxWidth(420);
        leftContainer.setPrefWidth(420);
        leftContainer.setAlignment(Pos.CENTER_LEFT);
        VBox v = new VBox(30);
        HBox h = new HBox(100);
        statusIndicator = new Label("Status : Standby");
        Button learnLauncher = new Button("Learn");
        learnLauncher.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(MainViewOld.this.learningDirectoryChooser.isValid()){
                    //TODO: think where to put indication on compute operations
                    //maybe threading
                    MainViewOld.this.statusIndicator.setText("Status : Learning");
                    boolean status = MainViewOld.this.controller.saveAverageFace();
                    averageImage.setImage(new Image(new File("../BDD/cropped&gray/average.jpg").toURI().toString()));
                    MainViewOld.this.statusIndicator.setText("Status : "+(status?"Learned":"Failed"));
                }
            }
        });
        h.getChildren().addAll(learnLauncher, statusIndicator);
        h.setAlignment(Pos.BOTTOM_LEFT);
        v.getChildren().addAll(new Label("Learning files"), learningDirectoryChooser.getPane(), h);

        leftContainer.getChildren().add(v);

        // adding layouts to top part of the center
        top.getChildren().addAll(leftContainer, avgImContainer);

        */
        // bottom

        /*
        VBox vb = new VBox(30);
        HBox hb = new HBox(100);
        recognitionRate = new Label("Recognition rate : ?%");
        Button testingLauncher = new Button("Run");
        testingLauncher.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (MainViewOld.this.testingDirectoryChooser.isValid()){
                    MainViewOld.this.recognitionRate.setText("Recognition rate : any%");
                    MainViewOld.this.controller.compareDistances();
                    MainViewOld.this.recognitionRate.setText("Recognition rate : "+
                            MainViewOld.this.controller.getSuccessRate() +"%");
                }
            }
        });
        hb.getChildren().addAll(testingLauncher, recognitionRate);
        hb.setAlignment(Pos.BOTTOM_LEFT);
        vb.getChildren().addAll(new Label("Test files"), testingDirectoryChooser.getPane(), hb);
        btm.getChildren().add(vb);
        */
        p.getChildren().addAll(top, btm);
        return p;
    }
}
