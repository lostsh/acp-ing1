package view;

import controller.Controller;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class ImageViewController implements Observer {

    private ImageView imageView;

    public ImageViewController(ImageView imageView){
        this.imageView = imageView;
    }

    @Override
    public void update(Observable o, Object arg) {
        Controller c = (Controller) o;
        imageView.setImage(new Image(new File(c.getAverageImagePath()).toURI().toString()));
    }
}
