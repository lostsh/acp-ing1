package view;
/*
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
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;

public class MainViewOld extends Application {

    Stage primaryStage;

    ImageView averageImage = null;

    Label statusIndicator = null;

    Label recognitionRate = null;

    CustomDirectoryChooser learningDirectoryChooser;
    ListView<File> learningFiles;

    CustomDirectoryChooser testingDirectoryChooser;
    ListView<File> testingFiles;

    Controller controller = null;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        learningDirectoryChooser = new CustomDirectoryChooser(primaryStage, "Learning directory");
        learningDirectoryChooser.addObserver((o, arg) -> learningUpdated());
        testingDirectoryChooser = new CustomDirectoryChooser(primaryStage, "Testing directory");
        testingDirectoryChooser.addObserver((o, arg) -> testingUpdated());
        controller = new Controller();
        averageImage = new ImageView();
        // layouts
        BorderPane root = new BorderPane();
        root.setTop(createTop());
        root.setCenter(createCenter());
        root.setLeft(createLeft());

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Face Recognition");
        primaryStage.show();
    }

    private Pane createTop(){
        VBox p = new VBox();
        p.setPadding(new Insets(0, 0, 5, 0));
        MenuBar m = new MenuBar();
        Menu mm = new Menu("File");
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(event -> System.exit(0));
        mm.getItems().addAll(new MenuItem("Add picture"), exit);
        m.getMenus().add(mm);
        p.getChildren().add(m);
        return p;
    }

    private Pane createCenter(){
        VBox p = new VBox(5);
        p.setPadding(new Insets(0, 5, 5, 0));
        p.setAlignment(Pos.BASELINE_CENTER);

        HBox top = new HBox(5);
        top.setPrefWidth(700);
        top.setPrefHeight(200);
        top.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-border-radius: 2px;");
        top.setPadding(new Insets(5));
        top.setAlignment(Pos.CENTER);

        // ImageView container layout
        HBox avgImContainer = new HBox();
        averageImage.setImage(new Image(new File("../BDD/cropped&gray/average.jpg").toURI().toString()));
        averageImage.setFitWidth(188);
        averageImage.setFitHeight(188);
        Label l = new Label("Average image");
        l.setStyle("-fx-rotate: -90; -fx-min-width: 100;");
        avgImContainer.setMaxWidth(270);
        avgImContainer.setPrefWidth(270);
        avgImContainer.getChildren().addAll(l, averageImage);
        avgImContainer.setAlignment(Pos.CENTER_RIGHT);

        // input layout
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

        // bottom
        HBox btm = new HBox(5);
        btm.setPrefWidth(700);
        btm.setPrefHeight(200);
        btm.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-border-radius: 2px;");
        btm.setPadding(new Insets(5));
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

        p.getChildren().addAll(top, btm);
        return p;
    }

    private Pane createLeft(){
        VBox p = new VBox(5);
        learningFiles = new ListView<>();
        learningFiles.setFocusTraversable(false);
        learningFiles.setMaxSize(150, 200);
        learningFiles.setPrefSize(150, 200);

        testingFiles = new ListView<>();
        testingFiles.setMaxSize(150, 200);
        testingFiles.setPrefSize(150, 200);
        p.getChildren().addAll(learningFiles, testingFiles);
        p.setPadding(new Insets(0, 5, 5, 5));
        return p;
    }

    private void learningUpdated(){
        statusIndicator.setText("Status : Standby");
        if(learningDirectoryChooser.isValid()){
            learningFiles.setItems(FXCollections.observableArrayList(
                    learningDirectoryChooser.getDirectory().listFiles()
            ));
            //TODO: think where to put the extraction to avoid await when select dir
            //TODO: real thread usage : check synchronisation and update components
            new Thread(() -> {
                controller.extractLearn(learningDirectoryChooser.getDirectory());
            }).start();
            //controller.extractLearn(learningDirectoryChooser.getDirectory());
        }
    }

    private void testingUpdated(){
        recognitionRate.setText("Recognition rate : ?%");
        if(testingDirectoryChooser.isValid()){
            testingFiles.setItems(FXCollections.observableArrayList(
                    testingDirectoryChooser.getDirectory().listFiles()
            ));
            //TODO: real thread usage : check synchronisation and update components
            controller.extractTest(testingDirectoryChooser.getDirectory());

            //new Thread(() -> {
            //    controller.extractTest(testingDirectoryChooser.getDirectory());
            //}).start();
        }
    }
}
*/