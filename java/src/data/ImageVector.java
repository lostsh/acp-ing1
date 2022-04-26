package data;

import java.util.ArrayList;

import static java.lang.Math.*;

import Jama.Matrix;

/**
 * Contain vectorized image.
 * Bean to be used everywhere we need to manipulate the image.
 */
public class ImageVector {

    /**
     * Vector is a double array
     */
    private ArrayList<Double> vector;

    public ImageVector() {
        vector = new ArrayList<>();
    }

    /**
     * Add a value to the vector.
     *
     * @param value value to add.
     */
    public void add(double value) {
        vector.add(value);
    }

    /**
     * Concatenate current ImageVector with given
     * ImageVector.
     *
     * @param iv vector to add.
     */
    public void concat(ImageVector iv) {
        for (int i = 0; i < iv.getDimension(); i++) {
            vector.add(iv.get(i));
        }
    }

    public double get(int index) {
        return vector.get(index);
    }
    
    public double set(int index, Double val) {
        return vector.set(index, val);
    }
    
    public Matrix toMatrix() {
    	int taille = this.getDimension();
    	Matrix mat = new Matrix( taille, 1);
    	
    	for( int i = 0; i < taille; i++ ) {
    		
    		mat.set(i, 0, this.get(i));
    	}
    	
    	return mat;
    }
    
    @Override
    public ImageVector clone() {
    	ImageVector newVector = new ImageVector();
    	for(  int i = 0; i < this.getDimension(); i++ ) {
    		newVector.add(this.get(i));
    	}
    	return newVector;
    }
    
    public ImageVector addSoustract(ImageVector vec2, boolean soustract) {
    	ImageVector res = new ImageVector();
    	
        if( soustract ) {
        	for( int i = 0; i<vec2.getDimension(); i++ ) {
        		res.add( this.get(i) - vec2.get(i) );
        	}
        } else {
        	for( int i = 0; i<vec2.getDimension(); i++ ) {
        		res.add( this.get(i) + vec2.get(i) );
        	}
        }
        return res;
    }


    /**
     * Get the vector size.
     *
     * @return number of dimensions.
     */
    public int getDimension() {
        return vector.size();
    }

    /**
     * Compare two ImageVector and return the Euclidean distance between.
     *
     * @return distance between two ImageVector.
     */
    public double distanceEuclidean(ImageVector iv) {
        int size = min(this.getDimension(), iv.getDimension());
        double sum = 0;
        for (int i = 0; i < size; i++) {
            sum += pow((this.get(i) - iv.get(i)), 2);
        }
        return sqrt(sum);
    }
    
    /**
     * Compare two ImageVector and return the Manhattan distance between.
     *
     * @return distance between two ImageVector.
     */
    public double distanceManhattan(ImageVector iv) {
        int size = min(this.getDimension(), iv.getDimension());
        double sum = 0;
        for (int i = 0; i < size; i++) {
            sum += abs(this.get(i) - iv.get(i));
        }
        return (sum);
    }

    /**
     * Show vector values.
     * Something like : V(2,34,4,...).
     *
     * @return the values.
     */
    @Override
    public String toString() {
        StringBuilder vec = new StringBuilder("ImageVector(");
        vector.forEach(i -> vec.append(i + ","));
        return getDimension() > 0 ? vec.deleteCharAt(vec.length() - 1).append(")").toString() : "ImageVector(Empty)";
    }
}