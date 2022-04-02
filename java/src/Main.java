import java.util.ArrayList;
import java.util.HashMap;

import data.Image;
import data.ImageVector;
import math.Acp;

public class Main {
    public static void main(String[] args) {
    	HashMap<String, ArrayList<ImageVector>> mappy = new HashMap<>();
    	
        System.out.println("Hello fraises");

        mappy = Acp.extractPicturesVectors("../BDD/cropped&gray/learn");
        
        ImageVector averageFace = averageFace(mappy);
        System.out.println(averageFace.toString());

        System.out.println("Done");
        
    }
    
    public static ImageVector averageFace(HashMap<String, ArrayList<ImageVector>> mappy) {
    	
    	ArrayList<ImageVector> vects = new ArrayList<ImageVector>();
    	ImageVector averageFace = new ImageVector();
    	ImageVector currentFace = new ImageVector();
    	int pixNum; //should be 160000
    	int numVect; //should be 12
    	int mapSize = mappy.size(); //should be 20
    	int avFaceSize = 160000;
    	Double val, val2;
    	
		for (int j = 0; j<avFaceSize; j++) {
			averageFace.add(0);
		}
    	
        for (String s: mappy.keySet()) {
        	vects = mappy.get(s);
        	numVect = vects.size();
        	
        	for (int i = 0; i<numVect; i++) {
        		currentFace = vects.get(i);
        		pixNum = currentFace.getDimension();
        		if (pixNum != avFaceSize) {
        			System.err.println("[!]\tImage sizes not regular");
        		}
    			for (int j = 0; j<pixNum; j++) {
    				val = currentFace.get(j)/(numVect*mapSize);
    				val2 = currentFace.get(j);
    				averageFace.set(j, val + val2);
    			}
        	}
        }
    	return averageFace;
    }
    
    
    public static void printAll(HashMap<String, ArrayList<ImageVector>> mappy) {
        /* Prints every Image Vector
         * Just for testing
         */
    	ArrayList<ImageVector> vects = new ArrayList<ImageVector>();
    	
        for (String s: mappy.keySet()) {
        	System.out.println(s);
        	vects = mappy.get(s);
        	for (int i = 0; i<vects.size(); i++) {
        		System.out.println(vects.get(i).toString());
        	}
        }
    }
    

    public static void example(){
        /* Just an example of how it works :
         * Use Image class to import the file
         * Then we use Image vector to manipulate Image.
         * (Once you extracted the vector from the Image,
         * you do not manipulate the Image object)
         */
        Image example = new Image("../BDD/cropped&gray/learn/1-01.jpg");

        // gui from IJ lib, do not use that, its juste for example
        // to show you what it look like
        example.show();

        /* This is how to manipulate an Image
         * from grey shades Image to b&w Image
         */
        byte[] pixels = example.getPixels();

        int width = example.getWidth();
        int height = example.getHeight();

        //Iterate over the picture
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){

                //convert byte pixel value to Integer
                int pix = pixels[ i*width + j ] & 0xff;

                //if px value greater than 128 its black
                // 128 is an arbitrary value
                if (pix <= 128)
                    pixels[i * width + j] = (byte) 0; // white
                else
                    pixels[i * width + j] = (byte) 255; // black
            }
        }

        // apply modifications to Image
        example.setPixels(pixels);
    }
}