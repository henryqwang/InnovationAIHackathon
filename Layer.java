
public class Layer {
	
	//Private Fields
	private int numberOfNode;
	private double[] nodesVector;
	
	//Constructor
	public Layer(int numberOfNode){
		this.numberOfNode = numberOfNode;
		this.nodesVector = new double[this.numberOfNode];
	}
	
	//Getters & Setters
	public int getNumberOfNode() {
		return numberOfNode;
	}
	public void setNumberOfNode(int numberOfNode) {
		this.numberOfNode = numberOfNode;
	}
	public double[] getNodesVector() {
		return nodesVector;
	}
	public void setNodesVector(double[] nodesVector) {
		this.nodesVector = nodesVector;
	}
	
}
