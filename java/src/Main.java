import java.util.ArrayList;
import java.util.HashMap;

import data.Image;
import data.ImageVector;
import math.Acp;

public class Main {
    public static void main(String[] args) {
        System.out.println("[+]\t Start extracting faces");
        long startTime = System.currentTimeMillis();

        HashMap<String, ArrayList<ImageVector>> mappy;
        mappy = Acp.extractPicturesVectors("../BDD/cropped&gray/learn");

        System.out.printf("[*]\tExtraction time : %ds\n", (System.currentTimeMillis() - startTime)/1000);

        printAll(mappy);

        startTime = System.currentTimeMillis();
        ImageVector averageFace = averageFace(mappy);
        System.out.println("\t\t- "+averageFace.toString().substring(0, 45)+" [...])");
        System.out.printf("[*]\tAverage compute time : %ds\n", (System.currentTimeMillis() - startTime)/1000);

        //TODO : for later: better to not use Image class there
        // we may put Image processing func into it own class later
        Image i = getImage(averageFace, "../BDD/cropped&gray/average.jpg");
        i.show();
        i.save();

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
        // sout image vectors
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