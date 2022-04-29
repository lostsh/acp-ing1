package data;

import java.util.ArrayList;
import java.util.HashMap;

public class Comparison {
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
    //RETURNS SUCCESS RATE
    public static double testDifferentBDDS(HashMap<String, ArrayList<ImageVector>> test, HashMap<String, ArrayList<ImageVector>> learn, double epsilon){
        int id;
        
        double wrong = 0;
        double right = 0;
        
        
    	for (String s : test.keySet()) {
        	//iterate through all images for that person
            for (ImageVector v : test.get(s)) {
            	System.out.println("Person with ID number " + s + " is getting tested");
            	id=compare(v, learn, epsilon);
            	
            	//assuming that ID numbers for people not in learn will be greater than the max ID number for people in Learn
            	//e.g. if there are 20 people in Learn, then they will have IDs between 1 and 20, 
            	//and therefore any ID number greater than 20 means the person is not in Learn
            	if (Integer.parseInt(s)<=(learn.keySet()).size()) {
            		if (id==Integer.parseInt(s)) {
            			right++;
            		} else {
            			wrong++;
            		}
            	} else {
            		 {
            			if (id==-1) {
            				right++;
            			} else {
            				wrong++;
            			}
            		}
            	}
            	
            }
        }
    	return (right/(right+wrong));
    }
    
    /**
     * Compares an ImageVector with all ImageVectors in Learn.
     * Returns the person's ID if found, -1 if not found
     * Test with different values of epsilon
     * @param test ImageVector of the image to compare.
     * @param bdd map of all ImageVecotrs in Learn.
     * @param epsilon how close we want the image to be to consider it's the same person.
     */
    public static int compare(ImageVector test, HashMap<String, ArrayList<ImageVector>> bdd, double epsilon){
        String res = new String("");
        boolean found = false;
        ArrayList<Integer> mode = new ArrayList<Integer>();
        int i;
        int id = -1;
        //iterate through all people in Learn
        for (String s : bdd.keySet()) {
        	i = 1;
        	//System.out.println("Checking person with ID number " + s);
        	//iterate through all images for that person
            for (ImageVector v : bdd.get(s)) {
            	//using euclidean distance
                if (v.distanceEuclidean(test)<epsilon) {
                	//for debugging and adjustments
                	res += " *** Test image matches person with ID number " + s + " (image number " + i + ")\n";
                	mode.add(Integer.parseInt(s));
                	found = true;
                }
                i++;
            }
        }
        if (!found) {
        	res = "No, person not found in the main database\n";
        } else {
        	id = calculateMode(mode);
        	res += "\tMATCH FOUND: person with ID number " + id + "\n";
        }
        System.out.println(res);
        return id;
    }
    
    /**
     * Compares mode for compare method
     * @param allID ArrayList of Integers, for each ID match found.
     */
    public static int calculateMode(ArrayList<Integer> allID) {
        int maxValue=0;
        int maxCount=0;

        for (int i = 0; i < allID.size(); ++i) {
            int count = 0;
            for (int j = 0; j < allID.size(); ++j) {
                if (allID.get(j).equals(allID.get(i))){
                	count++;
                }
            }
            if (count > maxCount) {
                maxCount = count;
                maxValue = allID.get(i);
            }
        }
        return maxValue;
    }
}
