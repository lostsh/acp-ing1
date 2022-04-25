package math;

import data.Image;
import data.ImageVector;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import Jama.Matrix;


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
    
    public static HashMap<String, ArrayList<ImageVector>> projectImages(eigenMatrix M, HashMap<String, ArrayList<ImageVector>> map) {
    	HashMap<String, ArrayList<ImageVector>> vectors = new HashMap<>();
    	for (String ivName : map.keySet()) {
    		ArrayList<ImageVector> ivList = map.get(ivName);
    		ArrayList<ImageVector> vList = new ArrayList<ImageVector>();
    		for (ImageVector iv : ivList) {
    			ImageVector v = new ImageVector();
    			Matrix pv = M.getProjectionMatrix().times(iv.toMatrix());
    			for (int i = 0; i<pv.getRowDimension(); i++) {
    				v.add(pv.get(i, 0));
    			}
    			vList.add(v);
    		}
    		vectors.put(ivName, vList);
    	}
        return vectors;
    }
    
    public static HashMap<String, ArrayList<ImageVector>> projectImagesAlternativ(eigenMatrix M, HashMap<String, ArrayList<ImageVector>> map) {
    	HashMap<String, ArrayList<ImageVector>> vectors = new HashMap<>();
    	ImageVector avgVec = averageFace(map);
    	ArrayList<Matrix> eigenVectorList = M.getEigenVectorsList();
    	for (String ivName : map.keySet()) {
    		ArrayList<ImageVector> ivList = map.get(ivName);
    		vectors.put( ivName, new ArrayList<ImageVector>() );
    		for (int g = 0; g < ivList.size(); g++ ) {
    			vectors.get(ivName).add( new ImageVector() );
    			for( int i = 0; i < eigenVectorList.size(); i++ ) {
    				vectors.get(ivName).get(g).add( eigenVectorList.get(i).transpose().times( map.get(ivName).get(g).addSoustract(avgVec, true).toMatrix() ).get(0, 0) );
    			}
    		}
    	}
        return vectors;
    }
    
    
    public static eigenMatrix getEigenMatrix(int k, HashMap<String, ArrayList<ImageVector>> map) {
    	HashMap<String, ArrayList<ImageVector>> mappy = normalizeVector(map);
    	Matrix transA = createMatrixTrans(mappy);
        return (new eigenMatrix( transA.transpose(), k ));
    	
    }

    public static boolean isImage(String file){
        return file.contains(".png") || file.contains(".jpg") || file.contains(".PNG") || file.contains(".JPG")
                || file.contains(".jpeg") || file.contains(".JPEG");
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
        int numberOfVectors = 0;
        for( String key : mappy.keySet() ) numberOfVectors += mappy.get(key).size();

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
    
    public static HashMap<String, ArrayList<ImageVector>> realCopy(HashMap<String, ArrayList<ImageVector>> map) {
    	HashMap<String, ArrayList<ImageVector>> newMap = new HashMap<String, ArrayList<ImageVector>>();
    	
    	for( String key : map.keySet() ) {
    		newMap.put(key, new ArrayList<>());
    		for( int i = 0; i < map.get(key).size(); i++ ) {
    			newMap.get(key).add(map.get(key).get(i).clone());
    		}
    	}
    	return newMap;
    }
    
    
    public static HashMap<String, ArrayList<ImageVector>> normalizeVector(HashMap<String, ArrayList<ImageVector>> map) {
		HashMap<String, ArrayList<ImageVector>> newMap = realCopy(map);
    	ImageVector avgVec = averageFace(map);
    	ImageVector res = new ImageVector();
    	
    	for( String key : map.keySet() ) {
    		
    		for( int i = 0; i < map.get(key).size(); i++ ) {
    			res = map.get(key).get(i).addSoustract(avgVec, true);
    			newMap.get(key).set(i, res);
    		}
    	}
    	return newMap;
    }
    
    public static Matrix createMatrixTrans( HashMap<String, ArrayList<ImageVector>> map ) {
    	Matrix mat;
    	int nbLigne = 0;
    	int nbColonne = 0;
    	int res = 0;
    	
    	for( String key : map.keySet() ) {
    		if( nbColonne == 0 ) {
    			nbColonne = map.get(key).get(0).getDimension();
    		}
    		nbLigne += map.get(key).size();
    	}
    	
    	mat = new Matrix( nbLigne, nbColonne);
    	
    	for( String key : map.keySet() ) {
    		for( int i = 0; i < map.get(key).size(); i++ ) {
    			for( int j = 0; j < nbColonne; j++ ) {
    				mat.set(res + i, j, map.get(key).get(i).get(j) );
    			}
    		}
			res += map.get(key).size();
    	}
    	return mat;
    }
    
    public static Matrix prodPerso( Matrix mat1, Matrix mat2 ) {
    	int nbLigne1 = mat1.getRowDimension();
    	int nbLigne2= mat2.getRowDimension();
    	int nbColonne2 = mat2.getColumnDimension();
    	Matrix res = new Matrix(nbLigne1, nbColonne2);
    	double somme = 0;
    	
    	for( int k = 0; k< nbLigne1; k++ ) {
    		for( int i = 0; i< nbColonne2; i++ ) {
    			somme = 0;
    			for( int j = 0; j< nbLigne2; j++ ) {
    	    		somme += mat1.get(k, j) * mat2.get(j, i);
    	    	}
    			res.set( k, i, somme);
        	}
    	}
    	
    	return res;
    }
    
    // Tests with the new projection
    public static void main(String[] args) {
        System.out.println("[+]\t Start extracting faces");
        long startTime = System.currentTimeMillis();
        
        //get images from Learn
        HashMap<String, ArrayList<ImageVector>> mappy;
        mappy = Acp.extractPicturesVectors("../BDD/cropped&gray/learn");
        System.out.printf("[*]\tExtraction time : %ds\n", (System.currentTimeMillis() - startTime)/1000);
        
        //print all ImageVectors from Learn
        //printAll(mappy);
        
        eigenMatrix M = Acp.getEigenMatrix(20, mappy);
        
        //get average face
        //ImageVector averageFace = math.Acp.averageFace(mappy);
        //System.out.println("\t\t- "+averageFace.toString().substring(0, 45)+" [...])");
        //System.out.printf("[*]\tAverage compute time : %ds\n", (System.currentTimeMillis() - startTime)/1000);

        //TODO : for later: better to not use Image class there
        // we may put Image processing func into it own class later
        //save and display average face
        
        //Image i = getImage(averageFace, "../BDD/cropped&gray/average.jpg");
        //i.show();
        //i.save();
        
        
        
        /****/
        /*this part tests the comparisons of photos using the class Comparison in package data*/
        
        //change the method call in Comparison.compare to switch distance methods
        //for Euclidean distance
        double epsilon = 0.0000001; 
        
        //for Manhattan distance
        //double epsilon = 2500000; 
        
        double successrate1, successrate2, successrate3;
        
        //these photos are exactly the same as random ones in Learn
        //the comparison should always be able to find the exact photo
        HashMap<String, ArrayList<ImageVector>> shouldBeYes;
        shouldBeYes = Acp.projectImagesAlternativ(M, Acp.extractPicturesVectors("../BDD/cropped&gray/test/photosinLearn"));
        // shouldBeYes = Acp.extractPicturesVectors("../BDD/cropped&gray/test/photosinLearn");
        
        //these photos are of people not present in Learn
        //the comparison should always say no, the person isn't in Learn
        HashMap<String, ArrayList<ImageVector>> shouldBeNo;
        shouldBeNo = Acp.projectImagesAlternativ(M,Acp.extractPicturesVectors("../BDD/cropped&gray/test/peoplenotinLearn"));
        // shouldBeNo = Acp.extractPicturesVectors("../BDD/cropped&gray/test/peoplenotinLearn");
        
        //these are previously unseen photos of people in Learn - the person is present in Learn but the exact photo isn't
        //this is the trickiest one to get right
        //this will show us if the program can recognise the same person from new photos
        HashMap<String, ArrayList<ImageVector>> shouldBeMaybe;
        shouldBeMaybe = Acp.projectImagesAlternativ(M,Acp.extractPicturesVectors("../BDD/cropped&gray/test/newphotosofpeopleinLearn"));
        // shouldBeMaybe = Acp.extractPicturesVectors("../BDD/cropped&gray/test/newphotosofpeopleinLearn");
        
        
        mappy = Acp.projectImagesAlternativ(M, mappy);
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
    }
    
}


