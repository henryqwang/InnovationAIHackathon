
public class LayerErrorDeltas {


	int numberOfNodes;
	double[] error;
	/**
	 * @return the numberOfNodes
	 */
	public int getNumberOfNodes() {
		return numberOfNodes;
	}
	/**
	 * @param numberOfNodes the numberOfNodes to set
	 */
	public void setNumberOfNodes(int numberOfNodes) {
		this.numberOfNodes = numberOfNodes;
	}
	/**
	 * @return the error
	 */
	public double[] getError() {
		return error;
	}
	/**
	 * @param error the error to set
	 */
	public void setError(double[] error) {
		this.error = error;
	}
	public LayerErrorDeltas(int numberOfNodes) {
		super();
		this.numberOfNodes = numberOfNodes;
		this.error = new double[numberOfNodes];
	} 
}


