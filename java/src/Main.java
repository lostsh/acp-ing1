import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import data.Image;
import data.ImageVector;
import math.Acp;
import math.EigenMatrix;

public class Main {
    public static void main(String[] args) {
        Controller c = new Controller();
        c.extractLearn(new File("../BDD/cropped&gray/learn"));
        c.saveAverageFace();
        c.extractTest(new File("../BDD/cropped&gray/test/newphotosofpeopleinLearn"));
        c.compareDistances();
        System.out.println(c.getSuccessRate());
    }
    /*
    public static void main(String[] args) {
        System.out.println("[+]\t Start extracting faces");
        long startTime = System.currentTimeMillis();
        
        //get images from Learn
        HashMap<String, ArrayList<ImageVector>> mappy;
        mappy = Acp.extractPicturesVectors("../BDD/cropped&gray/learn");
        System.out.printf("[*]\tExtraction time : %ds\n", (System.currentTimeMillis() - startTime)/1000);
        
        //print all ImageVectors from Learn
        printAll(mappy);
        
        //with epsilon=0.1054, k=50 gives 100,100,6
        //with epsilon=0.1054, k=40 gives 100,100,18
        //with epsilon=0.1054, k=30 gives 100,100,18
        //with epsilon=0.1054, k=20 gives 100,100,25
        //with epsilon=0.1054, k=15 gives 100,92,43
        //with epsilon=0.1054, k=12 gives 91,64,56
        //with epsilon=0.1054, k=11 gives 95,57,75
        //with epsilon=0.1054, k=10 gives 95,57,75
        //with epsilon=0.1054, k=9 gives 91,14,62
        //with epsilon=0.1054, k=5 gives 50,0,31
        EigenMatrix M = Acp.getEigenMatrix(40, mappy);
        startTime = System.currentTimeMillis();
        
        //get average face
        ImageVector averageFace = math.Acp.averageFace(mappy);
        System.out.println("\t\t- "+averageFace.toString().substring(0, 45)+" [...])");
        System.out.printf("[*]\tAverage compute time : %ds\n", (System.currentTimeMillis() - startTime)/1000);

        //TODO : for later: better to not use Image class there
        // we may put Image processing func into it own class later
        //save and display average face
        
        //Image i = getImage(averageFace, "../BDD/cropped&gray/average.jpg");
        //i.show();
        //i.save();
        
        
        
        //this part tests the comparisons of photos using the class Comparison in package data
        
        //change the method call in Comparison.compare to switch distance methods
        //for Euclidean distance
        //with k=7
        //0.1 gives 83,0,56
        //0.05 gives 100,71,31
        
        //with k=10
        //0.175 gives 62,0,43
        //0.15 gives 83,0,56
        //0.125 gives 91,14,62
        //0.11 gives 95, 50,75
        //0.1054 gives 95,57,75***
        //0.105 gives 91,57,75
        //0.1 gives 91,57,68
        //0.09 gives 100,64,56
        //0.075 gives 100,85,43
        //0.05 gives 100,100,37
        
        //with k=30
        //0.28 gives 91,28,75
        //0.25 gives 100,42,68
        //0.2 gives 100,85,62
        //0.15 gives 100,100,37
        
        //with k=40
        //0.5 gives 87,21,75
        //0.4 gives 100,50,75
        //0.35 100,64,68
        //0.32 gives 100,64,81
        //0.31 gives 95,71,81**peak
        //0.302 gives 95,71,75
        //0.3 gives 100,78,68
        //0.2 gives 100,100,37
        double epsilon = 0.31; 
        
        //for Manhattan distance
        //0.31 gives 87,7,68
        //0.3 gives 87,14,75
        //0.275 gives 87,35,75
        //0.25 gives 95,50,75***
        //0.245 gives 95,50,75
        //0.24 gives 91,57,68
        //0.225 gives 95,64,62
        //0.2 gives 95,85,43
        //0.1054 gives 100,100,31
        
        double successrate1, successrate2, successrate3;
        
        //these photos are exactly the same as random ones in Learn
        //the comparison should always be able to find the exact photo
        HashMap<String, ArrayList<ImageVector>> shouldBeYes;
        shouldBeYes = Acp.projectImages(M, Acp.extractPicturesVectors("../BDD/cropped&gray/test/photosinLearn"));
        
        //these photos are of people not present in Learn
        //the comparison should always say no, the person isn't in Learn
        HashMap<String, ArrayList<ImageVector>> shouldBeNo;
        shouldBeNo = Acp.projectImages(M,Acp.extractPicturesVectors("../BDD/cropped&gray/test/peoplenotinLearn"));
        
        //these are previously unseen photos of people in Learn - the person is present in Learn but the exact photo isn't
        //this is the trickiest one to get right
        //this will show us if the program can recognise the same person from new photos
        HashMap<String, ArrayList<ImageVector>> shouldBeMaybe;
        shouldBeMaybe = Acp.projectImages(M,Acp.extractPicturesVectors("../BDD/cropped&gray/test/newphotosofpeopleinLearn"));
        mappy = Acp.projectImages(M, mappy);
        // works perfectly
        successrate1 = data.Comparison.testDifferentBDDS(shouldBeYes, mappy, epsilon);
        //works perfectly
        successrate2 = data.Comparison.testDifferentBDDS(shouldBeNo, mappy, epsilon);
        //actually works OK with epsilon=10000
        successrate3 = data.Comparison.testDifferentBDDS(shouldBeMaybe, mappy, epsilon);
        
        //System.out.println("mappy: ");
        //Main.printAll(shouldBeYes);
        
        System.out.println("Success rate for photosinLearn : " + successrate1*100 + "%");
        System.out.println("Success rate for peoplenotinLearn : " + successrate2*100 + "%");
        System.out.println("Success rate for newphotosofpeopleinLearn : " + successrate3*100 + "%");
        
        //System.out.println("EigenFaces : ");
        //M.getEigenVectors().print(0, 5);
        //M.getProjectionMatrix().print(0, 5);
        System.out.println("[+]\t Done");
    }*/



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
     *//*
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
    }*/
}
