import java.util.ArrayList;
import java.util.HashMap;

import data.Image;
import data.ImageVector;
import math.Acp;

public class Main {
    public static void main(String[] args) {
        System.out.println("[+]\t Start extracting faces");
        long startTime = System.currentTimeMillis();
        
        //get images from Learn
        HashMap<String, ArrayList<ImageVector>> mappy;
        mappy = Acp.extractPicturesVectors("../BDD/cropped&gray/learn");

        System.out.printf("[*]\tExtraction time : %ds\n", (System.currentTimeMillis() - startTime)/1000);
        
        //print all ImageVectors from Learn
        //printAll(mappy);

        startTime = System.currentTimeMillis();
        
        //get average face
        ImageVector averageFace = averageFace(mappy);
        System.out.println("\t\t- "+averageFace.toString().substring(0, 45)+" [...])");
        System.out.printf("[*]\tAverage compute time : %ds\n", (System.currentTimeMillis() - startTime)/1000);

        //TODO : for later: better to not use Image class there
        // we may put Image processing func into it own class later
        //save and display average face
        Image i = getImage(averageFace, "../BDD/cropped&gray/average.jpg");
        i.show();
        i.save();
        
        
        
        /****/
        /*this part tests the comparisons of photos*/
        
        double epsilon = 10400;
        
        //this tests all images in Learn against all other images in Learn
        //test(mappy, epsilon);
        
        
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
        //testDifferentBDDS(shouldBeYes, mappy, epsilon);
        //works perfectly
        //testDifferentBDDS(shouldBeNo, mappy, epsilon);
        //actually works OK with epsilon=10000
        testDifferentBDDS(shouldBeMaybe, mappy, epsilon);
        

        System.out.println("[+]\t Done");
    }

    /**
     * Compute average face from ImageVectors.
     * @param mappy Map of ArrList of ImageVector.
     * @return ImageVector average face.
     */
    public static ImageVector averageFace(HashMap<String, ArrayList<ImageVector>> mappy) {
        ImageVector averageFace = new ImageVector();

        // get vector length and init
        int vectorSize = (mappy.values().stream().findFirst().get()).stream().findFirst().get().getDimension();
        for (int j = 0; j<vectorSize; j++) averageFace.add(0);

        // get total number of vectors into the map
        double numberOfVectors = mappy.get(mappy.keySet().stream().findFirst().get()).size()*mappy.size();
        System.out.println("Total number of vectors : "+numberOfVectors);

        // Iterate over vectors to compute average face
        for (String s: mappy.keySet()) { // each person
            for(ImageVector v : mappy.get(s)){ // each vector
                //average pixel value is : value + (value / numberOfVectors)
                //if(v.getDimension() != vectorSize) //TODO : throw new ImageVectorSizeException...
                for(int i=0;i<v.getDimension();i++){
                    averageFace.set(i, (v.get(i)/numberOfVectors) + averageFace.get(i));
                }
            }
        }

        return  averageFace;
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
    
    /**
     * manages the comparison by calling the method compare
     * Tests all images in Learn against all other images in Learn
     * Test with different values of epsilon
     * @param mappy map of all ImageVectors in Learn.
     * @param epsilon how close we want the image to be to consider it's the same person.
     */
    //THIS ONE TESTS EVERYTHING IN A SINGLE BDD AGAINST ITSELF
    public static void test(HashMap<String, ArrayList<ImageVector>> mappy, double epsilon){
        int num;
        for (String s : mappy.keySet()) {
        	num = 1;
        	//iterate through all images for that person
            for (ImageVector v : mappy.get(s)) {
            	System.out.println("Person with ID number " + s + ", image number " + num + " is getting tested");
            	System.out.println(compare(v, mappy, epsilon));
            	num++;
            }
        }
    }
    
    
    /**
     * manages the comparison by calling the method compare
     * Tests all images in one bdd against all images in Learn
     * Test with different values of epsilon
     * @param learn map of all ImageVectors in Learn.
     * @param test map of all ImageVectors in Test.
     * @param epsilon how close we want the image to be to consider it's the same person.
     */
    //THIS ONE TESTS EVERYTHING IN ONE BDD AGAINST ANOTHER BDD (Learn)
    public static void testDifferentBDDS(HashMap<String, ArrayList<ImageVector>> test, HashMap<String, ArrayList<ImageVector>> learn, double epsilon){
        for (String s : test.keySet()) {
        	//iterate through all images for that person
            for (ImageVector v : test.get(s)) {
            	System.out.println("Person with ID number " + s + " is getting tested");
            	System.out.println(compare(v, learn, epsilon));
            }
        }
    }
    
    /**
     * Compares an ImageVector with all ImageVectors in Learn.
     * Can be used for final comparison once the ACP part is ready
     * Returns a String with the person's ID if found, a rejection String if not found
     * Test with different values of epsilon
     * @param test ImageVector of the image to compare.
     * @param bdd map of all ImageVecotrs in Learn.
     * @param epsilon how close we want the image to be to consider it's the same person.
     */
    public static String compare(ImageVector test, HashMap<String, ArrayList<ImageVector>> bdd, double epsilon){
        String res = new String("");
        boolean found = false;
        int i;
        //iterate through all people in Learn
        for (String s : bdd.keySet()) {
        	i = 1;
        	//System.out.println("Checking person with ID number " + s);
        	//iterate through all images for that person
            for (ImageVector v : bdd.get(s)) {
            	//using euclidean distance
                if (v.compare(test)<epsilon) {
                	res += " *** Test image matches person with ID number " + s + " (image number " + i + ")\n";
                	found = true;
                }
                i++;
            }
        }
        if (!found) {
        	res = "No, person not found in the main database\n";
        }
        return res;
    }
}