package controller;

import data.Comparison;
import data.Image;
import data.ImageVector;
import math.Acp;
import math.EigenMatrix;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

public class Controller extends Observable {
    /**
     * Contains learned matrix
     */
    private ArrayList<File> learningFiles = null;
    private HashMap<String, ArrayList<ImageVector>> mappy = null;

    private ArrayList<File> testingFiles = null;
    private HashMap<String, ArrayList<ImageVector>> testing = null;

    private EigenMatrix matrix = null;

    private double successRate = -1;

    public static final double EPSILON = 0.31;
    //public static final double EPSILON = 10000;

    public void extractLearn(File directory){
        learningFiles = new ArrayList<>();
        for (File f : directory.listFiles()) if (f.isFile() && Acp.isImage(f.getName())) learningFiles.add(f);
        // extract image vectors
        mappy = Acp.extractPicturesVectors(directory.getPath());
        //learn();
        this.setChanged();
        this.notifyObservers(learningFiles);
    }

    public void learn(){
        matrix = Acp.getEigenMatrix(40, mappy);
        this.setChanged();
        this.notifyObservers();
    }

    public void saveAverageFace(String savePath){
        if(mappy != null){
            ImageVector averageFace = Acp.averageFace(mappy);
            getImage(averageFace, savePath).save();
            this.setChanged();
            this.notifyObservers();
        }
    }

    public void saveAverageFace() {
        saveAverageFace(getAverageImagePath());
    }

    public String getAverageImagePath(){
        return "../BDD/cropped&gray/average.jpg";
    }

    public void extractTest(File directory){
        if(mappy != null){
            testing = Acp.extractPicturesVectors(directory.getPath());
            testingFiles = new ArrayList<>();
            for (File f : directory.listFiles()) if (f.isFile() && Acp.isImage(f.getName())) testingFiles.add(f);

            //testing = Acp.projectImages(matrix, testing);

            //mappy = Acp.projectImages(matrix, mappy);

            this.setChanged();
            this.notifyObservers();
        }
    }

    public void test(){
        testing = Acp.projectImages(matrix, testing);
        mappy = Acp.projectImages(matrix, mappy);

        this.setChanged();
        this.notifyObservers();
    }

    public void compareDistances(){
        if(mappy != null && testing != null){
            successRate = Comparison.testDifferentBDDS(testing, mappy, EPSILON);
            successRate = successRate*100;

            this.setChanged();
            this.notifyObservers();
        }
    }

    public double getSuccessRate(){ return successRate; }

    public ArrayList<File> getLearningFiles(){ return learningFiles; }
    public ArrayList<File> getTestingFiles(){ return testingFiles; }

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
