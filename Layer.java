
public class Layer {

	//Vectors as public fields (facilitate updates)
	public int numberOfNodes;
	public double[] nodesActivationVector; //Corresponds to activation values
	public double[] nodesErrorVector; //Corresponds to lowercase delta values, note that this doesn't exist for L1 (inputs)
	
	//Constructor
	public Layer(int numberOfNodes){
		this.numberOfNodes = numberOfNodes;
		this.nodesActivationVector = new double[numberOfNodes];
		this.nodesErrorVector = new double [numberOfNodes];
	}
}
