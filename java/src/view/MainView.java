package view;

import controller.Controller;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

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
    Label testingStatusIndicator;
    Label recognitionRateIndicator;

    Controller controller;

    public MainView(Stage primaryStage){
        this.primaryStage = primaryStage;
        controller = new Controller();

        this.setCenter(createCenter());
        this.setLeft(createLeft());

        controller.update();
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

        //
        // Top part : learning
        //
        HBox top = new HBox(5);
        top.setPrefWidth(700);
        top.setPrefHeight(200);
        top.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-border-radius: 2px;");
        top.setPadding(new Insets(5));
        top.setAlignment(Pos.CENTER);

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
        HBox leftContainer = new HBox(5);
        leftContainer.setMaxWidth(420);
        leftContainer.setPrefWidth(420);
        leftContainer.setAlignment(Pos.CENTER_LEFT);

        VBox v = new VBox(30);
        learningDirectoryChooser = new CustomDirectoryChooser(primaryStage, "Learning directory");
        //TODO : add a controller to update the controller leanring directory
        learningDirectoryChooser.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                controller.setLearningDirectory(((CustomDirectoryChooser) o).getDirectory());
            }
        });

        HBox h = new HBox(100);

        // Button launch file extraction
        runLearningExtraction = new Button("Extract");
        ButtonLearningExtractionController learningExtractionController = new ButtonLearningExtractionController(controller, runLearningExtraction);
        runLearningExtraction.setOnAction(learningExtractionController);
        controller.addObserver(learningExtractionController);
        h.getChildren().add(runLearningExtraction);

        // Button launch learning
        runLearning = new Button("Learn");
        ButtonLearningController learningController = new ButtonLearningController(controller, runLearning);
        runLearning.setOnAction(learningController);
        controller.addObserver(learningController);
        h.getChildren().add(runLearning);

        // Label status
        learningStatusIndicator = new Label();
        controller.addObserver(new LabelLearningStatusController(controller, learningStatusIndicator));

        v.getChildren().addAll(new Label("Learning files"), learningDirectoryChooser.getPane(), h, learningStatusIndicator);

        leftContainer.getChildren().add(v);
        // adding layouts to top part of the center
        top.getChildren().addAll(leftContainer, avgImContainer);

        //
        // Bottom part : testing
        //
        HBox btm = new HBox(5);
        btm.setPrefSize(top.getPrefWidth(), top.getPrefHeight());
        btm.setStyle(top.getStyle());
        btm.setPadding(top.getPadding());

        VBox vb = new VBox(22);
        testingDirectoryChooser = new CustomDirectoryChooser(primaryStage, "Testing directory");
        testingDirectoryChooser.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                controller.setTestingDirectory(((CustomDirectoryChooser) o).getDirectory());
            }
        });

        HBox hb = new HBox(100);

        // Button extract testing files
        runRecognitionExtraction = new Button("Extract");
        ButtonTestingExtractionController testingExtractionController = new ButtonTestingExtractionController(controller, runRecognitionExtraction);
        runRecognitionExtraction.setOnAction(testingExtractionController);
        controller.addObserver(testingExtractionController);
        hb.getChildren().add(runRecognitionExtraction);

        // Button run testing
        runRecognition = new Button("Run");
        ButtonTestingController testingController = new ButtonTestingController(controller, runRecognition);
        runRecognition.setOnAction(testingController);
        controller.addObserver(testingController);
        hb.getChildren().add(runRecognition);

        // Label status
        testingStatusIndicator = new Label();
        controller.addObserver(new LabelTestingStatusController(controller, testingStatusIndicator));

        //TODO : recognition rate
        recognitionRateIndicator = new Label();
        controller.addObserver(new LabelController(controller, recognitionRateIndicator) {
            @Override
            public void update(Observable o, Object arg) {
                label.setText("Recognition Rate : ?%");
                if(arg instanceof Boolean) label.setText("Recognition Rate : "+controller.getSuccessRate()+"%");
            }
        });

        vb.getChildren().addAll(new Label("Test files"), testingDirectoryChooser.getPane(), hb, testingStatusIndicator, recognitionRateIndicator);
        btm.getChildren().add(vb);

        p.getChildren().addAll(top, btm);
        return p;
    }
}
