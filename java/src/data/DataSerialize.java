package data;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Used to controller the Serialization process.
 * Save and restore HashMap data from filesystem.
 */
public class DataSerialize implements Serializable {

    /**
     * Serialization file.
     */
    private File file;

    /**
     * Data : Each id => List of ImageVector.
     */
    private HashMap<String, ArrayList<ImageVector>> vectors;

    /**
     * Create a serialization controller.
     * @param serializeFile serialized File.
     */
    public DataSerialize(File serializeFile){
        this.vectors = new HashMap<>();
        this.file = serializeFile;
    }

    /**
     * Get if file exist.
     * @return true if the file is valid.
     */
    public boolean exist(){
        return file.exists() && file.isFile() && file.canRead();
    }

    /**
     * Get the current HashMap containing data.
     * @return HashMap containing data.
     */
    public HashMap<String, ArrayList<ImageVector>> getVectors(){
        return vectors;
    }

    /**
     * Override the current HashMap with given data.
     * @param vectors the HashMap containing data.
     */
    public void setVectors(HashMap<String, ArrayList<ImageVector>> vectors){
        this.vectors = vectors;
    }

    /**
     * Serialize the Hashmap containing data.
     */
    public void serialize(){
        try {
            ObjectOutputStream oout = new ObjectOutputStream(new FileOutputStream(file));
            oout.writeObject(vectors);
            oout.close();
        } catch (IOException e) {
            System.err.println("File in/out exception.\n"+
                    e.getMessage());
        }
    }

    /**
     * deserialize the Hashmap containing data.
     */
    public void unSerialize(){
        try {
            ObjectInputStream oin = new ObjectInputStream(new FileInputStream(file));
            vectors = (HashMap<String, ArrayList<ImageVector>>) oin.readObject();
            oin.close();
        } catch (FileNotFoundException e) {
            System.err.println("Serialized file not found.\n"+
                    e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Serialized class not found.\n"+
                    e.getMessage());
        } catch (IOException e) {
            System.err.println("File in/out exception.\n"+
                    e.getMessage());
        }
    }
}
