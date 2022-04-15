package math;

import Jama.Matrix;
import Jama.EigenvalueDecomposition;

public class eigenMatrix {
	
	private Matrix A;
	private Matrix transA;
	private Matrix eigenVectors;
	private double[] eigenValues;
	
	public eigenMatrix(Matrix A, int k) {
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
		
		for( int i = 0; i < eigenValues.length; i++ ) System.out.println( eigenValues[i] );
		
	}
	
	public Matrix getA() {
		return this.A;
	}
	
	public double[] getEigenValues() {
		return this.eigenValues;
	}
	
	
	
}
