import java.lang.Math;
import java.util.Random;
public class NeuralNetwork {

	private int inputDim,  hiddenLayer2Dim,  hiddenLayer3Dim,  outputLayerDim;
	private Layer inputLayer, hiddenL2, hiddenL3, outputLayer;
	private InterLayerTheta theta1, theta2, theta3;
	private LayerErrorDeltas delta2,delta3,delta4;
	private BigDelta ddelta1, ddelta2, ddelta3;
	private CapitalD dd1,dd2,dd3;
	private double[] output;
	
	

	public NeuralNetwork(int inputDim, int hiddenLayer2Dim, int hiddenLayer3Dim, int outputLayerDim){
		this.inputDim = inputDim;
		this.hiddenLayer2Dim = hiddenLayer2Dim;
		this.hiddenLayer3Dim = hiddenLayer3Dim;
		this.outputLayerDim = outputLayerDim;

		// The +1 accounts for bias unit
		this.inputLayer = new Layer(inputDim +1);
		this.hiddenL2 = new Layer(hiddenLayer2Dim +1);
		this.hiddenL3 = new Layer(hiddenLayer3Dim +1);
		this.outputLayer = new Layer(outputLayerDim);
		
		this.delta2 = new LayerErrorDeltas(hiddenLayer2Dim +1);
		this.delta3 = new LayerErrorDeltas(hiddenLayer3Dim +1);
		this.delta4 = new LayerErrorDeltas(outputLayerDim);

		this.theta1 = new InterLayerTheta(hiddenLayer2Dim, inputDim+1);
		this.ddelta1= new BigDelta(hiddenLayer2Dim, inputDim+1);
		this.dd1= new CapitalD(hiddenLayer2Dim, inputDim+1);
		this.theta2 = new InterLayerTheta(hiddenLayer3Dim, hiddenLayer2Dim+1);
		this.ddelta2= new BigDelta(hiddenLayer3Dim, hiddenLayer2Dim+1);
		this.dd2= new CapitalD(hiddenLayer3Dim, hiddenLayer2Dim+1);
		this.theta3 = new InterLayerTheta(outputLayerDim, hiddenLayer3Dim+1);
		this.ddelta3= new BigDelta(outputLayerDim, hiddenLayer3Dim+1);
		this.dd3= new CapitalD(outputLayerDim, hiddenLayer3Dim+1);		

	}


	//HELPERS

	//Display helpers
	public void printMatrix(double[][] matrix){
		for(int i = 0; i < matrix.length; i++){
			for(int j = 0; j<matrix[0].length; j++){
				System.out.print("["+matrix[i][j]+"]"+" ");
			}
			System.out.println();
		}
	}
	public void printArray(double[] array){
		System.out.print("[");
		for(int i = 0; i<array.length-1; i++){
			System.out.print(array[i]+" | ");
		}
		System.out.print(array[array.length-1]+"]");
		System.out.println();
	}
	public void displayNetworkDimensions(){
		System.out.println("NEURAL NETWORK DIMENSIONS");
		System.out.println();
		System.out.println("Including bias unit, the input layer has: "+ this.inputLayer.getNumberOfNode()+" nodes.");
		System.out.println("Including bias unit, the 2nd layer has: "+ this.hiddenL2.getNumberOfNode()+" nodes.");
		System.out.println("Including bias unit, the 3rd layer has: "+ this.hiddenL3.getNumberOfNode()+" nodes.");
		System.out.println("The output layer has: "+ this.outputLayer.getNumberOfNode()+" nodes.");
		System.out.println();
		System.out.println("The theta1 matrix is: "+ this.theta1.getRowSize()+" by "+theta1.getColSize());
		System.out.println("The theta2 matrix is: "+ this.theta2.getRowSize()+" by "+theta2.getColSize());
		System.out.println("The theta3 matrix is: "+ this.theta3.getRowSize()+" by "+theta3.getColSize());
		System.out.println();
	}
	public void displayNetworkDetails(){
		System.out.println("NEURAL NETWORK DETAILS");
		System.out.println();
		System.out.println("The nodes of input layers are: "); printArray(this.inputLayer.getNodesVector());
		System.out.println();
		System.out.println("The theta1 matrix is: "); printMatrix(this.theta1.thetaMatrix);
		System.out.println();
		System.out.println("The nodes of 2nd layers are: "); printArray(this.hiddenL2.getNodesVector());
		System.out.println();
		System.out.println("The theta2 matrix is: "); printMatrix(this.theta2.thetaMatrix);
		System.out.println();
		System.out.println("The nodes of 3rd layers are: "); printArray(this.hiddenL3.getNodesVector());
		System.out.println();
		System.out.println("The theta3 matrix is: "); printMatrix(this.theta3.thetaMatrix);
		System.out.println();
		System.out.println("The nodes of output layers are: "); printArray(this.outputLayer.getNodesVector());

		System.out.println();

	}

	//Matrix multiplication
	public static double[][] matrixMultiply(double[][] matrixA, double[][] matrixB){

		double[][] result = new double[matrixA.length][matrixB[0].length];

		for(int i = 0; i<result.length; i++){
			for (int j = 0; j<result[0].length; j++){
				result[i][j] = 0;
			}
		}	
		if(matrixA[0].length != matrixB.length){
			System.out.println("Error: matrices dimension don't match.");
		}else{

			for(int i = 0; i<matrixA.length; i++){
				for(int j = 0; j<matrixB[0].length; j++){
					for(int k = 0; k< matrixA[0].length; k++){
						result[i][j] += matrixA[i][k] * matrixB[k][j];
					}
				}
			}
		}

		return result;
	}

	//Sigmoid Function
	public static double[] sigmoid(double[] x) 
	{
		int length= x.length;
		double[] result = new double[length];
		for(int i=0;i<length;i++)
		{
			result[i]=(1/( 1 + Math.pow(Math.E,(-1*x[i]))));
		}
		return result;
	}

	//Makes an array from a n by 1 matrix
	public static double[] matrixToArray(double[][] matrix){
		int length = matrix.length;
		double[] arrayToOutput = new double[length];

		for(int i=0; i<length; i++){
			arrayToOutput[i] = matrix[i][0];
		}
		return arrayToOutput;
	}

	//Makes a n by 1 matrix from an array
	public static double[][] arrayToMatrix(double[] array){
		int length = array.length;
		double[][] matrixToOutput = new double[length][1];

		for(int i=0; i<length; i++){
			matrixToOutput[i][0] = array[i];
		}
		return matrixToOutput;

	}
	
	/* A function that add  matrices elements -wise*/
	public static double[][] addMatrices(double[][] A, double[][] B)
	{
		  int aRows = A.length;
	      int aColumns = A[0].length;
	      int bRows = B.length;
	      int bColumns = B[0].length;
	      if (aColumns != bColumns||aRows != bRows) 
	      {
	        throw new IllegalArgumentException("A:Rows: " + aColumns + " did not match B:Columns " + bRows + ".");
	      }
	      double[][] C = new double[aRows][bColumns];
	      for (int i = 0; i < aRows; i++) 
	      {
	        for (int j = 0; j < bColumns; j++) 
	        {
	          C[i][j] = 0.00000;
	        }
	      }
	      for (int i = 0; i < aRows; i++) 
	      { // aRow
	          for (int j = 0; j < bColumns; j++) 
	          { // bColumn
	        	  C[i][j]=A[i][j]+B[i][j];
	          }
	      }
	      return C;
	 }
	
	/* A function that add subtract matrices elements -wise*/
	public static double[][] substractMatrices(double[][] A, double[][] B)
	{
		  int aRows = A.length;
	      int aColumns = A[0].length;
	      int bRows = B.length;
	      int bColumns = B[0].length;
	      if (aColumns != bColumns||aRows != bRows) 
	      {
	        throw new IllegalArgumentException("A:Rows: " + aColumns + " did not match B:Columns " + bRows + ".");
	      }
	      double[][] C = new double[aRows][bColumns];
	      for (int i = 0; i < aRows; i++) 
	      {
	        for (int j = 0; j < bColumns; j++) 
	        {
	          C[i][j] = 0.00000;
	        }
	      }
	      for (int i = 0; i < aRows; i++) 
	      { // aRow
	          for (int j = 0; j < bColumns; j++) 
	          { // bColumn
	        	  C[i][j]=A[i][j]-B[i][j];
	          }
	      }
	      return C;
	 }
	
	/* Element wise_multiplication*/
	public static double[][] MMultiplicationElementWise(double[][] A, double[][] B)
	{
		  int aRows = A.length;
	      int aColumns = A[0].length;
	      int bRows = B.length;
	      int bColumns = B[0].length;
	      if (aColumns != bColumns||aRows != bRows) 
	      {
	        throw new IllegalArgumentException("A:Rows: " + aColumns + " did not match B:Columns " + bRows + ".");
	      }
	      double[][] C = new double[aRows][bColumns];
	      for (int i = 0; i < aRows; i++) 
	      {
	        for (int j = 0; j < bColumns; j++) 
	        {
	          C[i][j] = 0.00000;
	        }
	      }
	      for (int i = 0; i < aRows; i++) 
	      { // aRow
	          for (int j = 0; j < bColumns; j++) 
	          { // bColumn
	        	  C[i][j]=A[i][j]*B[i][j];
	          }
	      }
	      return C;
	 }
	/* Cost function*/
	public double logisticCostPerTrainingExample(double hypothesis, double output, int defaultThreshold)
	{
		double y = output;
		if (y==1)
		{
			double answer = y*Math.log(hypothesis);
			if (answer < defaultThreshold)
			{
				return defaultThreshold;
			}
			else
			{
				return answer;
			}
		}        
		else
		{
			double answer =  (1-y)*Math.log(1-hypothesis);
			if (answer < defaultThreshold)
			{
				return defaultThreshold;
			}
			else
			{
				return answer;
			}
		}
	}
	
	//Randomly initialize all weights to some double within range [-epsilon, epsilon]
	public void randomInitializationOfWeights()
	{
		System.out.println("Initializing interlayer theta matrices... ");
		System.out.println();
		double epsilon = 0.1;
		//Populate theta1
		for(int i = 0; i<theta1.getRowSize(); i++){
			for(int j = 0; j<theta1.getColSize(); j++){
				Random rnd = new Random();
				double randomDouble = rnd.nextDouble();
				theta1.thetaMatrix[i][j] = randomDouble*(2*epsilon) - epsilon;
			}
		}
		//Populate theta1
		for(int i = 0; i<theta2.getRowSize(); i++){
			for(int j = 0; j<theta2.getColSize(); j++){
				Random rnd = new Random();
				double randomDouble = rnd.nextDouble();
				theta2.thetaMatrix[i][j] = randomDouble*(2*epsilon) - epsilon;
			}
		}
		//Populate theta1
		for(int i = 0; i<theta3.getRowSize(); i++){
			for(int j = 0; j<theta3.getColSize(); j++){
				Random rnd = new Random();
				double randomDouble = rnd.nextDouble();
				theta3.thetaMatrix[i][j] = randomDouble*(2*epsilon) - epsilon;
			}
		}

	}

	//Adds a +1 node to the first position of a layer vector as bias
	public static double[] addBiasUnitToVector(double[] layerVectorToBeConcatenated){

		double[] newArray = new double[layerVectorToBeConcatenated.length+1];
		newArray[0] = 1; //Hard coded
		for (int i = 1; i<newArray.length; i++){
			newArray[i] = layerVectorToBeConcatenated[i-1];
		}

		return newArray;

	}
	/* Create a vector of ones*/
	public double[] createVectorOfOnes(int size)
	{
		double[] result= new double[size];
		for(int i=0;i<size;i++)
		{
			result[i]=1;
		}
		return result;
	}
	
	//Forward Prop
	public void singleForwardPropagation(double[][] inputVector){
		System.out.println("Executing a iteration of forward propagation... ");
		System.out.println();
		//Since only 3 layers, use manual assignment instead of indexed FOR-loop


		inputLayer.setNodesVector(addBiasUnitToVector(matrixToArray(inputVector)));

		//Layer 1 -> Layer 2
		double[][] tmpL2 = matrixMultiply(theta1.thetaMatrix, arrayToMatrix(inputLayer.getNodesVector()));
		double[] newActivationValueVectorL2 = sigmoid(matrixToArray(tmpL2));
		hiddenL2.setNodesVector(addBiasUnitToVector(newActivationValueVectorL2));

		//Layer 2 -> Layer 3
		double[][] tmpL3 = matrixMultiply(theta2.thetaMatrix, arrayToMatrix(hiddenL2.getNodesVector()));
		double[] newActivationValueVectorL3 = sigmoid(matrixToArray(tmpL3));
		hiddenL3.setNodesVector(addBiasUnitToVector(newActivationValueVectorL3));

		//Layer 3 -> Output Layer
		double[][] tmpOutput = matrixMultiply(theta3.thetaMatrix, arrayToMatrix(hiddenL3.getNodesVector()));
		double[] newActivationValueVectorOutput = sigmoid(matrixToArray(tmpOutput));
		outputLayer.setNodesVector(addBiasUnitToVector(newActivationValueVectorOutput));

	}
  /* Function to compute transpose of a matrix*/
	
  public static double[][] transposeMatrix(double [][] m)
  {
       double[][] temp = new double[m[0].length][m.length];
       for (int i = 0; i < m.length; i++)
           for (int j = 0; j < m[0].length; j++)
               temp[j][i] = m[i][j];
       return temp;
   }
  
	public void singleBackPropagation(double[][] inputVector)
	{
		this.delta4.setError(matrixToArray(substractMatrices(arrayToMatrix(this.outputLayer.getNodesVector()),arrayToMatrix(this.output))));
		double[][]bigDeltaLayer3=this.ddelta3.getBigDeltaMatrix(); // get the matrix of big delta.
		double[][] result3=   transposeMatrix((arrayToMatrix(this.outputLayer.getNodesVector()))); // get a3
		double[][] deltalm4 = arrayToMatrix(this.delta4.getError()); // get the delta.
		this.ddelta3.setBigDeltaMatrix(addMatrices(bigDeltaLayer3, matrixMultiply(result3,deltalm4))); // update deltal.
		
		int length= this.hiddenL3.getNodesVector().length; // Get the length of the hidden layer vector use in substraction method with vector of ones.
		double[][] hiddenLayer3M=arrayToMatrix(this.hiddenL3.getNodesVector());// make the hidden layer into a matrix
		double[] onesInLayer3= createVectorOfOnes(length); // create an vector of ones with length equal to that of layer3.
		double[][] oneInLayer3M=arrayToMatrix(onesInLayer3); // make the ones into a matrix for each computation.
		double[][] oneMinusHidden3= substractMatrices(oneInLayer3M,hiddenLayer3M); // one - hiddenlayer3.
		double[][] hiden3MultiplyOneMinusHidden3=MMultiplicationElementWise(hiddenLayer3M,oneMinusHidden3); // hidden *(1-hidden)		
		this.delta3.setError( matrixToArray(MMultiplicationElementWise(matrixMultiply(this.theta3.thetaMatrix, arrayToMatrix(this.delta4.getError())), hiden3MultiplyOneMinusHidden3)));
		double[][]bigDeltaLayer2=this.ddelta2.getBigDeltaMatrix(); // get the matrix of big delta.
		double[][] result2=   transposeMatrix((arrayToMatrix(this.hiddenL3 .getNodesVector()))); // get a3
		double[][] deltalm3 = arrayToMatrix(this.delta3.getError()); // get the delta.
		this.ddelta2.setBigDeltaMatrix(addMatrices(bigDeltaLayer2, matrixMultiply(result2,deltalm3))); // update deltal.
		
		length= this.hiddenL2.getNodesVector().length; // Get the length of the hidden layer vector use in substraction method with vector of ones.
		double[][] hiddenLayer2M=arrayToMatrix(this.hiddenL2.getNodesVector());// make the hidden layer into a matrix
		double[] onesInLayer2= createVectorOfOnes(length); // create an vector of ones with length equal to that of layer3.
		double[][] oneInLayer2M=arrayToMatrix(onesInLayer3); // make the ones into a matrix for each computation.
		double[][] oneMinusHidden2= substractMatrices(oneInLayer3M,hiddenLayer3M); // one - hiddenlayer3.
		double[][] hiden2MultiplyOneMinusHidden2=MMultiplicationElementWise(hiddenLayer2M,oneMinusHidden2); // hidden *(1-hidden)		
		this.delta2.setError( matrixToArray(MMultiplicationElementWise(matrixMultiply(this.theta2.thetaMatrix, arrayToMatrix(this.delta3.getError())), hiden2MultiplyOneMinusHidden2)));
		double[][]bigDeltaLayer1=this.ddelta1.getBigDeltaMatrix(); // get the matrix of big delta.
		double[][] result1=   transposeMatrix((arrayToMatrix(this.hiddenL2.getNodesVector()))); // get a3
		double[][] deltalm2 = arrayToMatrix(this.delta2.getError()); // get the delta.
		this.ddelta1.setBigDeltaMatrix(addMatrices(bigDeltaLayer1, matrixMultiply(result1,deltalm2))); // update deltal.
		
		
		
	}
	public static double[][] getRow(int i,double[][] x)
	{
		double[][] result=new double[1][x[0].length];
		for(int j=0;i<x[1].length;j++)
			result[0][i]=x[i][j];
		return result;
	}
	

}