
public class InterLayerTheta {
	//Private Fields
	private int rowSize, colSize;
	//Matrix as a public field (facilitates updates for either element-wise update or entire matrix update)
	public double[][] thetaMatrix;
	
	//Constructor
	public InterLayerTheta(int rowSize, int colSize){
		this.rowSize = rowSize;
		this.colSize = colSize;
		this.thetaMatrix = new double[rowSize][colSize];
		
	}

	//Getters & Setters
	public int getRowSize() {
		return rowSize;
	}

	public void setRowSize(int rowSize) {
		this.rowSize = rowSize;
	}

	public int getColSize() {
		return colSize;
	}

	public void setColSize(int colSize) {
		this.colSize = colSize;
	}
		
}
