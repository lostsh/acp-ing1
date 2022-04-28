package math;

import Jama.Matrix;
import data.ImageVector;

import java.util.ArrayList;

import Jama.EigenvalueDecomposition;

public class EigenMatrix {
	
	private Matrix A;
	private Matrix transA;
	private Matrix eigenVectors;
	private Matrix projectionMatrix;
	private double[] eigenValues;
	
	/*
	 * Create the eigenValue matrix, eigenVector matrix and projection matrix, while keeping only the k most important vectors and values only
	 */
	
	public EigenMatrix(Matrix A, int k) {
		this.A = A;
		transA = this.A.transpose();
		
		// Erreur à ajouter si k > nb de vecteurs propres ou k = 0
		
		Matrix L = transA.times(A);
		EigenvalueDecomposition structure = L.eig();
		Matrix pseudoVectors = structure.getV();
		Matrix D = structure.getD();
		
		eigenVectors = new Matrix( A.getRowDimension() , k );
		
		for( int i = 0; i < k; i++ ) {
			int j = pseudoVectors.getColumnDimension() - 1 - i;
			
			Matrix vector = pseudoVectors.getMatrix(0, pseudoVectors.getRowDimension() - 1, j,j );
			vector = A.times(vector);
			
			eigenVectors.setMatrix( 0, eigenVectors.getRowDimension() - 1, i, i, vector );
			
		}
		
		eigenValues = new double[k];
		
		for( int i = 0; i < k; i++ ) eigenValues[ i ] = D.get( D.getColumnDimension() - i - 1 , D.getColumnDimension() - i - 1 );
		
		
		Matrix projector = A.times(pseudoVectors).inverse();
		
		this.projectionMatrix = new Matrix(k , projector.getColumnDimension());
		
		for (int i = 0; i < k; i++) {
			this.projectionMatrix.setMatrix(i, i, 0, projector.getColumnDimension() - 1, projector.getMatrix(projector.getRowDimension() - 1 - i, projector.getRowDimension() - 1 - i, 0, projector.getColumnDimension() - 1));
		}
	}
	
	/*
	 * Give the A matrix that we gave in its trans form
	 * @return matrix 
	 */
	
	public Matrix getA() {
		return this.A;
	}
	
	/*
	 * Give the k most important eigenVectors in a matrix
	 * @return matrix 
	 */
	
	public Matrix getEigenVectors() {
		return this.eigenVectors;
	}
	
	public ArrayList<Matrix> getEigenVectorsList(){
		ArrayList<Matrix> vectorList = new ArrayList<Matrix>();
		for( int i = 0; i < eigenVectors.getColumnDimension(); i++ ) {
			vectorList.add( new Matrix(eigenVectors.getRowDimension(), 1) );
			
			for(  int j = 0; j < eigenVectors.getRowDimension(); j++) {
				vectorList.get(i).set(j, 0, eigenVectors.get(j, i) );
			}
		}
		return vectorList;
	}
	
	/*
	 * Give the list of k eigenValues associated to the vectors in the good order
	 * @return double[]
	 */
	
	public double[] getEigenValues() {
		return this.eigenValues;
	}
	
	/*
	 * Give the projection matrix
	 * @return matrix 
	 */
	
	public Matrix getProjectionMatrix() {
		return this.projectionMatrix;
	}
	
	
	
}
