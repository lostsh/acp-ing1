import java.util.ArrayList;
import java.util.HashMap;

import data.Image;
import data.ImageVector;
import math.Acp;
import math.eigenMatrix;

public class Main {
    public static void main(String[] args) {
        System.out.println("[+]\t Start extracting faces");
        long startTime = System.currentTimeMillis();
        
        //get images from Learn
        HashMap<String, ArrayList<ImageVector>> mappy;
        mappy = Acp.extractPicturesVectors("../BDD/cropped&gray/learn");
        System.out.printf("[*]\tExtraction time : %ds\n", (System.currentTimeMillis() - startTime)/1000);
        
        //print all ImageVectors from Learn
        printAll(mappy);
        
        startTime = System.currentTimeMillis();
        
        //get average face
        ImageVector averageFace = math.Acp.averageFace(mappy);
        System.out.println("\t\t- "+averageFace.toString().substring(0, 45)+" [...])");
        System.out.printf("[*]\tAverage compute time : %ds\n", (System.currentTimeMillis() - startTime)/1000);

        //TODO : for later: better to not use Image class there
        // we may put Image processing func into it own class later
        //save and display average face
        Image i = getImage(averageFace, "../BDD/cropped&gray/average.jpg");
        i.show();
        i.save();
        
        
        
        /****/
        /*this part tests the comparisons of photos using the class Comparison in package data*/
        
        //change the method call in Comparison.compare to switch distance methods
        //for Euclidean distance
        double epsilon = 100; 
        
        //for Manhattan distance
        //double epsilon = 2500000; 
        
        double successrate1, successrate2, successrate3;
        
        //these photos are exactly the same as random ones in Learn
        //the comparison should always be able to find the exact photo
        HashMap<String, ArrayList<ImageVector>> shouldBeYes;
        shouldBeYes = Acp.extractPicturesVectors("../BDD/cropped&gray/test/photosinLearn");
        
        //these photos are of people not present in Learn
        //the comparison should always say no, the person isn't in Learn
        HashMap<String, ArrayList<ImageVector>> shouldBeNo;
        shouldBeNo = Acp.extractPicturesVectors("../BDD/cropped&gray/test/peoplenotinLearn");
        
        //these are previously unseen photos of people in Learn - the person is present in Learn but the exact photo isn't
        //this is the trickiest one to get right
        //this will show us if the program can recognise the same person from new photos
        HashMap<String, ArrayList<ImageVector>> shouldBeMaybe;
        shouldBeMaybe = Acp.extractPicturesVectors("../BDD/cropped&gray/test/newphotosofpeopleinLearn");
        // works perfectly
        successrate1 = data.Comparison.testDifferentBDDS(shouldBeYes, mappy, epsilon);
        //works perfectly
        successrate2 = data.Comparison.testDifferentBDDS(shouldBeNo, mappy, epsilon);
        //actually works OK with epsilon=10000
        successrate3 = data.Comparison.testDifferentBDDS(shouldBeMaybe, mappy, epsilon);
        
        System.out.println("Success rate for photosinLearn : " + successrate1*100 + "%");
        System.out.println("Success rate for peoplenotinLearn : " + successrate2*100 + "%");
        System.out.println("Success rate for newphotosofpeopleinLearn : " + successrate3*100 + "%");
        
        eigenMatrix M = Acp.getEigenMatrix(10, mappy);
        System.out.println("EigenFaces : ");
        M.getEigenVectors().print(0, 5);
        
        System.out.println("[+]\t Done");
    }



    /**
     * Display all ImageVector in Sys out.
     * @param mappy Map of ArrList of ImageVector
     */
    public static void printAll(HashMap<String, ArrayList<ImageVector>> mappy) {
        // out image vectors
        for (String s : mappy.keySet()) {
            System.out.printf("\t- Face Name :[%s] => %d vectors\n", s, mappy.get(s).size());
            for (ImageVector v : mappy.get(s)) {
                String vector = v.toString();
                System.out.println("\t\t- " + vector.substring(0, 45) + " [...] " + vector.substring(vector.length() - 45));
            }
        }
    }

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