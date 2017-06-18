
public class BigDelta {

	private double[][] bigDeltaMatrix;

	//Constructor
	public BigDelta(int rowSize, int colSize){
		this.bigDeltaMatrix = new double[rowSize][colSize];

	}

	public double[][] getBigDeltaMatrix() {
		return bigDeltaMatrix;
	}

	public void setBigDeltaMatrix(double[][] bigDeltaMatrix) {
		this.bigDeltaMatrix = bigDeltaMatrix;
	}
}
