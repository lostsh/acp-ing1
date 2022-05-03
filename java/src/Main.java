import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.MainView;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = new MainView(primaryStage);

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Face recognition");
        primaryStage.show();
    }
    /*
    public static void main(String[] args) {
        Controller c = new Controller();
        c.extractLearn(new File("../BDD/cropped&gray/learn"));
        c.saveAverageFace();
        c.extractTest(new File("../BDD/cropped&gray/test/newphotosofpeopleinLearn"));
        c.compareDistances();
        System.out.println(c.getSuccessRate());
    }*/
}
