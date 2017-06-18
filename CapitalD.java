
public class CapitalD {
	private double[][] DMatrix;

	//Constructor
	public CapitalD(int rowSize, int colSize){
		this.DMatrix = new double[rowSize][colSize];

	}

	public double[][] getDMatrix() {
		return DMatrix;
	}

	public void setDMatrix(double[][] dMatrix) {
		DMatrix = dMatrix;
	}

	

}
