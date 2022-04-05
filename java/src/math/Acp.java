package math;

import data.Image;
import data.ImageVector;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Acp {

    /**
     * Get the BDD dir and extract the Vectors
     * @param directory BDD containing pictures
     * @return Map associating [personName => Vector]
     */
    public static HashMap<String, ArrayList<ImageVector>> extractPicturesVectors(String directory){
        HashMap<String, ArrayList<ImageVector>> vectors = new HashMap<>();

        File picsDir = new File(directory);

        if(picsDir.listFiles() != null) {
            for (File f : picsDir.listFiles()) {
                if (f.isFile() && isImage(f.getName())) {

                    // Create image and do process
                    Image img = new Image(f.getPath());
                    // TODO: extract vector
                    ImageVector iv = img.getVector();
                    //System.out.println(iv);

                    // Add vector to the vectors of the person
                    String pictureName = f.getName().substring(0, f.getName().indexOf("-"));
                    if(!vectors.containsKey(pictureName)) vectors.put(pictureName, new ArrayList<>());
                    vectors.get(pictureName).add(iv);
                }
            }
        }else{
            System.err.println("[!]\tNot Found or Empty directory");
        }
        // remove in prod (just for debug)
        if(true) System.out.println("[+]\tExtract finished.\n\tExtracted "+vectors.keySet().size()+" files.");
        return vectors;
    }

    public static boolean isImage(String file){
        return file.contains(".png") || file.contains(".jpg") || file.contains(".PNG") || file.contains(".JPG")
                || file.contains(".jpeg") || file.contains(".JPEG");
    }
}