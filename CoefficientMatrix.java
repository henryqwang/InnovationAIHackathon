
public class CoefficientMatrix {


	public int rowSize, colSize;
	//Matrices as public fields (facilitates updates for either element-wise update or entire matrix update)
	public double[][] thetaMatrix;
	public double[][] deltaMatrix;
	public double[][] dAccumulator;

	//Constructor
	public CoefficientMatrix(int rowSize, int colSize){
		this.rowSize = rowSize;
		this.colSize = colSize;
		this.thetaMatrix = new double[rowSize][colSize];
		this.deltaMatrix = new double[rowSize][colSize];
		this.dAccumulator = new double[rowSize][colSize];

	}
}
