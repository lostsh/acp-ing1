package controller;

import data.Comparison;
import data.Image;
import data.ImageVector;
import math.Acp;
import math.EigenMatrix;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Controller {
    /**
     * Contains learned matrix
     */
    private HashMap<String, ArrayList<ImageVector>> mappy = null;

    private HashMap<String, ArrayList<ImageVector>> testing = null;

    private EigenMatrix matrix = null;

    private double successRate = -1;

    //public static final double EPSILON = 0.31;
    public static final double EPSILON = 10000;

    public void extractLearn(File directory){
        mappy = Acp.extractPicturesVectors(directory.getPath());
        matrix = Acp.getEigenMatrix(40, mappy);
    }

    public boolean saveAverageFace(String savePath){
        if(mappy != null){
            ImageVector averageFace = Acp.averageFace(mappy);
            getImage(averageFace, savePath).save();
            return true;
        }
        return false;
    }

    public boolean saveAverageFace() {
        return saveAverageFace("../BDD/cropped&gray/average.jpg");
    }

    public void extractTest(File directory){
        testing = Acp.extractPicturesVectors(directory.getPath());
        testing = Acp.projectImages(matrix, testing);

        mappy = Acp.projectImages(matrix, mappy);
    }

    public void compareDistances(){
        if(mappy != null && testing != null){
            successRate = Comparison.testDifferentBDDS(testing, mappy, EPSILON);
            successRate = successRate*100;
        }
    }

    public double getSuccessRate(){ return successRate; }

    /**
     * Convert an ImageVector into an Image.
     * Used to link between low level Image class and ImageVector(bean)
     * @param vector ImageVector of the image to convert.
     * @param path already existing file for the Image.
     */
    public static Image getImage(ImageVector vector, String path){
        Image image = new Image(path);

        byte[] pixels = image.getPixels();

        int width = image.getWidth();
        int height = image.getHeight();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pixels[i * width + j] = (byte) vector.get(i * width + j);
            }
        }
        // apply modifications to Image
        image.setPixels(pixels);
        return image;
    }
}
