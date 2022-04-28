package view;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class MainView extends Application {

    ImageView averageImage = new ImageView();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
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

    private Pane createPane(int width, int height){
        Pane p = new Pane();
        p.setPrefWidth(width);
        p.setPrefHeight(height);
        p.setStyle("-fx-border-color: black; -fx-border-width: 2px; -fx-border-radius: 2px;");
        return p;
    }

    private Pane createTop(){
        VBox p = new VBox();
        p.setPadding(new Insets(0, 0, 5, 0));
        MenuBar m = new MenuBar();
        Menu mm = new Menu("File");
        mm.getItems().addAll(new MenuItem("Add picture"), new MenuItem("Exit"));
        m.getMenus().add(mm);
        p.getChildren().add(m);
        return p;
    }

    private Pane createFileField(String placeholder){
        HBox p = new HBox(5);
        TextField input = new TextField();
        input.setPromptText(placeholder);
        input.setPrefWidth(275);
        Button button = new Button("Browse");
        p.getChildren().addAll(input, button);
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
        l.setStyle("-fx-rotate: -90;");
        avgImContainer.setMaxWidth(340);
        avgImContainer.setPrefWidth(340);
        avgImContainer.getChildren().addAll(l, averageImage);
        avgImContainer.setAlignment(Pos.CENTER_RIGHT);

        // input layout
        HBox leftContainer = new HBox(5);
        leftContainer.setMaxWidth(350);
        leftContainer.setPrefWidth(350);
        leftContainer.setAlignment(Pos.CENTER);
        VBox v = new VBox(30);
        HBox h = new HBox(100);
        h.getChildren().addAll(new Button("Learn"), new Label("Status : Standby"));
        h.setAlignment(Pos.BOTTOM_LEFT);
        v.getChildren().addAll(new Label("Learning files"), createFileField("Learning path"), h);

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
        hb.getChildren().addAll(new Button("Run"), new Label("Recognition rate : ?%"));
        hb.setAlignment(Pos.BOTTOM_LEFT);
        vb.getChildren().addAll(new Label("Test files"), createFileField("Test path"), hb);
        //btm.getChildren().add(createPane(700, 200));
        btm.getChildren().add(vb);

        p.getChildren().addAll(top, btm);
        return p;
    }

    private Pane createLeft(){
        VBox p = new VBox(5);
        ListView<String> learningFiles = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList("One", "two", "tree");
        learningFiles.setItems(items);
        learningFiles.setMaxSize(150, 200);
        learningFiles.setPrefSize(150, 200);

        ListView<File> testingFiles = new ListView<>();
        testingFiles.setItems(FXCollections.observableArrayList(Arrays.asList(new File("../BDD/cropped&gray/test/newphotosofpeopleinLearn").listFiles())));
        testingFiles.setMaxSize(150, 200);
        testingFiles.setPrefSize(150, 200);
        p.getChildren().addAll(learningFiles, testingFiles);
        p.setPadding(new Insets(0, 5, 5, 5));
        //p.setAlignment(Pos.BASELINE_CENTER);
        return p;
    }
}
