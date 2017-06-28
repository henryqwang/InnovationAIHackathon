import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class NeuralNetwork {	

	private Layer layer1, layer2, layer3, layer4;
	private CoefficientMatrix CM1, CM2, CM3;

	private double learningRate;

	private double[][] trainingSet;
	private int numberOfTrainingSets;

	private double hypothesis;
	private double output;

	//--------------------------------------CONSTRUCTOR--------------------------------------//
	public NeuralNetwork(int inputDim, int layer2Dim, int layer3Dim, int outputLayerDim, double learningRate, double[][] trainingSet){


		// The +1 accounts for bias unit, all layers have an extra bias unit but the output layer
		this.layer1 = new Layer(inputDim +1);
		this.layer2 = new Layer(layer2Dim +1);
		this.layer3 = new Layer(layer3Dim + 1);
		this.layer4 = new Layer(outputLayerDim);

		this.CM1 = new CoefficientMatrix(layer2Dim, inputDim+1);
		this.CM2 = new CoefficientMatrix(layer3Dim, layer2Dim+1);
		this.CM3 = new CoefficientMatrix(outputLayerDim, layer3Dim+1);

		this.learningRate = learningRate;

		this.trainingSet = trainingSet;
		this.numberOfTrainingSets = trainingSet.length;

		//printMatrix(trainingSet);
		//fixedInitializationOfWeights(); //For debugging purpose only
		randomInitializationOfWeights();
		gradientDescent();
	}


	//----------------------------------------HELPERS----------------------------------------//

	//Display Helpers
	public void print(){
		System.out.println();
		return;
	}
	public void print(String s){
		System.out.println(s);
		return;
	}
	public void print(double d){
		System.out.println(d);
		return;
	}
	public void printMatrix(double[][] matrix){
		for(int i = 0; i < matrix.length; i++){
			for(int j = 0; j<matrix[0].length; j++){
				System.out.print("["+matrix[i][j]+"]"+" ");
			}
			System.out.println();
		}
		System.out.println();
	}
	public void printArray(double[] array){
		System.out.print("[");
		for(int i = 0; i<array.length-1; i++){
			System.out.print(array[i]+" | ");
		}
		System.out.print(array[array.length-1]+"]");
		System.out.println();
	}
	public void showMatrixDimensions(double[][] matrix){
		int rowSize = matrix.length;
		int colSize = matrix[0].length;
		System.out.println("The matrix is: "+rowSize+" by "+colSize);
		System.out.println();
	}
	public void showMatrixDimensions(double[] array){
		int rowSize = array.length;

		System.out.println("The matrix is: "+rowSize+" by 1");
		System.out.println();
	}
	public void displayNetworkDimensions(){
		System.out.println("--- NEURAL NETWORK DIMENSIONS ---");
		System.out.println();
		System.out.println("Without bias unit, the input layer has: "+ (this.layer1.numberOfNodes-1) +" nodes.");
		System.out.println("Without bias unit, the 2nd layer has: "+ (this.layer2.numberOfNodes-1) +" nodes.");
		System.out.println("Without bias unit, the 3nd layer has: "+ (this.layer3.numberOfNodes-1) +" nodes.");
		System.out.println("The output layer has: "+ this.layer4.numberOfNodes+" nodes.");
		System.out.println();
		System.out.println("The theta1 matrix is: "+ this.CM1.rowSize+" by "+CM1.colSize);
		System.out.println("The theta2 matrix is: "+ this.CM2.rowSize+" by "+CM2.colSize);
		System.out.println("The theta3 matrix is: "+ this.CM3.rowSize+" by "+CM3.colSize);
		System.out.println();
	}
	public void displayNetworkDetails(){
		System.out.println("--- NEURAL NETWORK DETAILS ---");
		System.out.println();
		System.out.println("The activation nodes of input layers are: "); printArray(this.layer1.nodesActivationVector);
		System.out.println();
		System.out.println("The theta1 matrix is: "); printMatrix(CM1.thetaMatrix);
		System.out.println();
		System.out.println("The activation nodes of 2nd layers are: "); printArray(this.layer2.nodesActivationVector);
		System.out.println();
		System.out.println("The theta2 matrix is: "); printMatrix(CM2.thetaMatrix);
		System.out.println();
		System.out.println("The activation nodes of 3rd layers are: "); printArray(this.layer3.nodesActivationVector);
		System.out.println();
		System.out.println("The theta3 matrix is: "); printMatrix(CM3.thetaMatrix);
		System.out.println();
		System.out.println("The activation nodes of output layers are: "); printArray(this.layer4.nodesActivationVector);
		System.out.println();
	}
	public void displayLayersActivation(){
		System.out.println("--- LAYER ACTIVATION DETAILS ---");
		System.out.println();
		System.out.println("L1 (input): "); printArray(layer1.nodesActivationVector);
		System.out.println("L2 (hidden): "); printArray(layer2.nodesActivationVector);
		System.out.println("L3 (hidden): "); printArray(layer3.nodesActivationVector);
		System.out.println("L4 (output): "); printArray(layer4.nodesActivationVector);
		System.out.println();
	}
	public void displayLayersError(){
		System.out.println("--- LAYER ERROR DETAILS ---");
		System.out.println();
		System.out.println("L1 (input): "); printArray(layer1.nodesErrorVector);
		System.out.println("L2 (hidden): "); printArray(layer2.nodesErrorVector);
		System.out.println("L3 (hidden): "); printArray(layer3.nodesErrorVector);
		System.out.println("L4 (output): "); printArray(layer4.nodesErrorVector);
		System.out.println();
	}
	public void displayThetas(){
		System.out.println("--- INTER-LAYER THETA MATRICES DETAILS ---");
		System.out.println();
		System.out.println("The theta1 matrix is: "); printMatrix(CM1.thetaMatrix);
		System.out.println("The theta2 matrix is: "); printMatrix(CM2.thetaMatrix);
		System.out.println("The theta3 matrix is: "); printMatrix(CM3.thetaMatrix);
		System.out.println();
	}
	public void displayCapDeltas(){
		System.out.println("--- INTER-LAYER DELTA MATRICES DETAILS ---");
		System.out.println();
		System.out.println("The capital delta1 matrix is: "); printMatrix(CM1.deltaMatrix);
		System.out.println("The capital delta2 matrix is: "); printMatrix(CM2.deltaMatrix);
		System.out.println("The capital delta3 matrix is: "); printMatrix(CM3.deltaMatrix);
		System.out.println();
	}
	public void displayD(){
		System.out.println("--- INTER-LAYER D MATRICES DETAILS ---");
		System.out.println();
		System.out.println("The D1 matrix is: "); printMatrix(CM1.dAccumulator);
		System.out.println("The D2 matrix is: "); printMatrix(CM2.dAccumulator);
		System.out.println("The D3 matrix is: "); printMatrix(CM3.dAccumulator);
		System.out.println();
	}

	//File IO Helper
	public static double[][] getInput(String path) 
	{
		/* read in the file*/
		BufferedReader reader=null;
		try
		{
			reader= new BufferedReader(new FileReader(path));
		} catch(FileNotFoundException fnfex)
		{
			System.out.println(fnfex.getMessage()+"The file was not found");
		}
		/*Get the file's data and store it in a string*/
		double[][] result= new double[310][7];
		//String text ="";
		String line;
		int numberOfLines=0;
		try {
			while((line=reader.readLine()) != null)
			{
				//System.out.println();
				numberOfLines++;
				if(numberOfLines>12)
				{
					String[] words = line.split(",");
					for(int i=0;i<7;i++)
					{
						if(words[i].equals("Abnormal"))
							result[numberOfLines-13][i]=0.0;
						else if(words[i].equals("Normal"))
							result[numberOfLines-13][i]=1.0;
						else	
							result[numberOfLines-13][i]=Double.parseDouble(words[i]);
						//System.out.print(result[numberOfLines-13][i]+" ");
					}
				}
			}
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		return result;
	}
	
	//Operational Helpers

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

	//This method is created to avoid constantly changing between double[] (a vector) and double[][] (a matrix with only 1 column /single-vector matrix)
	//Greatly simplifies syntax
	public static double[] matrixMultiplyVector(double[][] matrix, double[] vector){
		if(matrix[0].length != vector.length){
			throw new RuntimeException("Matrix-vector dimensions don't agree for multiplication.");
		}
		double[] resultArray = new double[matrix.length];
		for(int i = 0; i < matrix.length; i++){
			resultArray[i] = 0;
		}
		for(int j = 0; j < matrix.length; j++){
			for(int k = 0; k < matrix[0].length; k++){
				resultArray[j] += matrix[j][k] * vector[k];
			}
		}
		return resultArray;
	}

	//Matrix transpose calculator
	public static double[][] transpose(double [][] m)
	{
		double[][] temp = new double[m[0].length][m.length];
		for (int i = 0; i < m.length; i++)
			for (int j = 0; j < m[0].length; j++)
				temp[j][i] = m[i][j];
		return temp;
	}

	//A function that multiply matrices element-wise
	public static double[][] elementWiseMultiplyMatrices(double[][] A, double[][] B)
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
		{
			for (int j = 0; j < bColumns; j++) 
			{
				C[i][j]=A[i][j]*B[i][j];
			}
		}
		return C;
	}

	public static double[] elementWiseMultiplyVectors(double[] vectorA, double[] vectorB){
		if(vectorA.length != vectorB.length){
			throw new RuntimeException("Dimensions don't match");
		}
		double[] result = new double[vectorA.length];
		for (int i = 0; i < vectorA.length; i++){
			result[i] = vectorA[i] * vectorB[i];
		}
		return result;
	}

	public static double[][] scalarMultiplyMatrix(double scalar, double[][] matrix){
		double [][] newMatrix = new double[matrix.length][matrix[0].length];
		for(int i=0; i<matrix.length; i++){
			for(int j=0; j<matrix[0].length; j++){
				newMatrix[i][j] = matrix[i][j] * scalar;
			}
		}
		return newMatrix;
	}
	
	public static double[] scalarMultiplyVector(double scalar, double[] vector){
		double [] newVector = new double[vector.length];
		for(int i=0; i<vector.length; i++){
			newVector[i] = vector[i]*scalar;
		}
		return newVector;
	}
	
	public static double[] scalarMinusVector(double scalar, double[] vector){
		double [] newVector = new double[vector.length];
		for(int i=0; i<vector.length; i++){
			newVector[i] = scalar- vector[i];
		}
		return newVector;
	}
	
	//A function that subtract 2 matrices element-wise
	public static double[][] subtractMatrices(double[][] A, double[][] B)
	{
		int aRows = A.length;
		int aColumns = A[0].length;
		int bRows = B.length;
		int bColumns = B[0].length;
		if (aColumns != bColumns||aRows != bRows) 
		{
			throw new IllegalArgumentException("A:Rows: " + aColumns + " did not match B:Columns: " + bRows + ".");
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

	//Takes an index and return the corresponding row of matrix in form of array
	public static double[] getRow(int index, double[][] matrix){
		double[] row = new double[matrix[0].length];

		for(int i = 0; i < matrix[0].length; i++){
			row[i] = matrix[index][i];
		}
		return row;
	}
	//Makes an array from a n by 1 matrix (column vector)
	public static double[] matrixToArray(double[][] matrix){
		int length = matrix.length;
		double[] arrayToOutput = new double[length];

		for(int i=0; i<length; i++){
			arrayToOutput[i] = matrix[i][0];
		}
		return arrayToOutput;
	}

	//Makes a n by 1 matrix (column vector) from an array
	public static double[][] arrayToMatrix(double[] array){
		int length = array.length;
		double[][] matrixToOutput = new double[length][1];

		for(int i=0; i<length; i++){
			matrixToOutput[i][0] = array[i];
		}
		return matrixToOutput;
	}

	//-----------------------------NEURAL NETWORK FUNCTIONALITIES-----------------------------//

	//Randomly initialize all weights to some double within range [-epsilon, epsilon]
	public void randomInitializationOfWeights(){
		System.out.println("Initializing random inter-layer theta matrices... ");
		System.out.println();
		double epsilon = 1;
		//Populate theta1
		for(int i = 0; i<CM1.rowSize; i++){
			for(int j = 0; j<CM1.colSize; j++){
				Random rnd = new Random();
				double randomDouble = rnd.nextDouble();
				CM1.thetaMatrix[i][j] = randomDouble*(2*epsilon) - epsilon;
			}
		}
		//Populate theta2
		for(int i = 0; i<CM2.rowSize; i++){
			for(int j = 0; j<CM2.colSize; j++){
				Random rnd = new Random();
				double randomDouble = rnd.nextDouble();
				CM2.thetaMatrix[i][j] = randomDouble*(2*epsilon) - epsilon;
			}
		}

	}

	//For debugging tractability purposes
	public void fixedInitializationOfWeights(){
		System.out.println("Initializing fixed inter-layer theta matrices... ");
		System.out.println();
		//Populate theta1
		for(int i = 0; i<CM1.rowSize; i++){
			for(int j = 0; j<CM1.colSize; j++){

				CM1.thetaMatrix[i][j] =(j+i*(CM1.colSize))*0.01;
			}
		}
		//Populate theta2
		for(int i = 0; i<CM2.rowSize; i++){
			for(int j = 0; j<CM2.colSize; j++){

				CM2.thetaMatrix[i][j] = (j+i*(CM2.colSize))*0.01;
			}
		}
		//Populate theta3
		for(int i = 0; i<CM3.rowSize; i++){
			for(int j = 0; j<CM3.colSize; j++){

				CM3.thetaMatrix[i][j] = (j+i*(CM3.colSize))*0.01;
			}
		}
		print("Theta matrices initialized.");

		displayNetworkDimensions();
	}

	//Adds a +1 node to the first position of a layer vector as bias
	public static double[] addBiasUnit(double[] layerVectorToBeConcatenated){

		double[] newArray = new double[layerVectorToBeConcatenated.length+1];
		newArray[0] = 1; //Hard coded
		for (int i = 1; i<newArray.length; i++){
			newArray[i] = layerVectorToBeConcatenated[i-1];
		}

		return newArray;

	}
	//Adds a +1 node to the first position of a layer vector as bias
	public static double[] removeBiasUnit(double[] layerVectorToBeShortened){

		double[] newArray = new double[layerVectorToBeShortened.length-1];
		//Assuming the head is the bias to be removed
		for (int i = 0; i<newArray.length; i++){
			newArray[i] = layerVectorToBeShortened[i+1];
		}

		return newArray;

	}

	//Logistic Cost
	public static double logisticCostPerTrainingExample(double hypothesis, double output, int defaultThreshold){
		double y = output;
		//System.out.println("The hypothesis is: "+hypothesis);
		//System.out.println("The actual output is:"+output);
		if (y==1){
			double answer = y*Math.log(hypothesis);
			if (answer < -defaultThreshold){
				//System.out.println("The cost is hence: "+ defaultThreshold);
				return defaultThreshold;
			}else{
				//System.out.println("The cost is hence: "+ -answer);
				return -answer;
			}
		}

		else{
			double answer =  (1-y)*Math.log(1-hypothesis);
			if (answer < -defaultThreshold){
				//System.out.println("The cost is hence: "+ defaultThreshold);
				return defaultThreshold;
			}else{
				//System.out.println("The cost is hence: "+ -answer);
				return -answer;
			}
		}
	}

	//Forward propagation for one single input training set
	public void singleForwardPropagation(double[] singleTrainingSet){

		layer1.nodesActivationVector =  addBiasUnit(singleTrainingSet);

		double[] z2 = matrixMultiplyVector(CM1.thetaMatrix, layer1.nodesActivationVector);
		layer2.nodesActivationVector = addBiasUnit(sigmoid(z2));

		double[] z3 = matrixMultiplyVector(CM2.thetaMatrix, layer2.nodesActivationVector);
		layer3.nodesActivationVector = addBiasUnit(sigmoid(z3));

		double[] z4 = matrixMultiplyVector(CM3.thetaMatrix, layer3.nodesActivationVector);
		layer4.nodesActivationVector = sigmoid(z4);

		hypothesis = layer4.nodesActivationVector[0];
	 
	}

	//Back propagation for all training set (equivalent of one iteration in gradient descent)
	//This gets all partial derivatives in matrix form, which we later need in gradient descent (for theta updates)
	public void backwardPropagation(){
		double currentCost = 0.0;
		for(int i = 0; i < numberOfTrainingSets; i++){
			//System.out.println("Backward propagation for training set: #"+(i+1));
			double[] currentInput = Arrays.copyOfRange(trainingSet[i], 0, trainingSet[i].length - 1);
			if(currentInput.length != layer1.numberOfNodes - 1){
				System.out.println("The input give has length: "+currentInput.length);
				System.out.println("The input is expected to have length: "+layer1.numberOfNodes);
				throw new RuntimeException();
			}
			output = trainingSet[i][trainingSet[i].length - 1];

			singleForwardPropagation(currentInput);
			
			currentCost += logisticCostPerTrainingExample(hypothesis, output, 5);

			//Computation of error vectors per layer (lower case deltas)
			//Delta 3
			double[] delta4 = new double[1];
			delta4[0] = hypothesis - output;
			layer4.nodesErrorVector = delta4;

			//Delta 3
			double[] delta3A = matrixMultiplyVector(transpose(CM3.thetaMatrix), layer4.nodesErrorVector);
			double[] delta3B = scalarMinusVector(1, layer3.nodesActivationVector);
			double[] delta3tmp = elementWiseMultiplyVectors(delta3A, layer3.nodesActivationVector);
			double[] delta3 = elementWiseMultiplyVectors(delta3tmp, delta3B);
			layer3.nodesErrorVector = delta3;

			//Delta 2
			//CAREFUL: in order for matrix-vector multiplication to match, removing the bias unit error (which doesn't participate in the gradient calculation is necessary)
			double[] delta2A = matrixMultiplyVector(transpose(CM2.thetaMatrix), removeBiasUnit(layer3.nodesErrorVector));
			double[] delta2B = scalarMinusVector(1, layer2.nodesActivationVector);
			double[] delta2tmp = elementWiseMultiplyVectors(delta2A, layer2.nodesActivationVector);
			double[] delta2 = elementWiseMultiplyVectors(delta2tmp, delta2B);
			layer2.nodesErrorVector = delta2;


			//Computation of error matrices (upper case deltas)
			//Cap delta 3
			double [][] capDeltaUpdate3 = matrixMultiply(arrayToMatrix(layer4.nodesErrorVector), transpose(arrayToMatrix(layer3.nodesActivationVector)));
			CM3.deltaMatrix = addMatrices(CM3.deltaMatrix, capDeltaUpdate3);

			//Cap delta 2
			double[] adjustedErrorLayer3 = removeBiasUnit(layer3.nodesErrorVector);
			double[][] capDeltaUpdate2 = matrixMultiply(arrayToMatrix(adjustedErrorLayer3), transpose(arrayToMatrix(layer2.nodesActivationVector))); 
			CM2.deltaMatrix = addMatrices(CM2.deltaMatrix, capDeltaUpdate2);

			//Cap delta 1
			double[] adjustedErrorLayer2 = removeBiasUnit(layer2.nodesErrorVector);
			double[][] capDeltaUpdate1 = matrixMultiply(arrayToMatrix(adjustedErrorLayer2), transpose(arrayToMatrix(layer1.nodesActivationVector))); 
			CM1.deltaMatrix = addMatrices(CM1.deltaMatrix, capDeltaUpdate1);
		}

		System.out.println("In this backpropagation, the average cost is: "+ (currentCost/(double)numberOfTrainingSets));
		//Computation of capital D matrices
		//These will be equivalent of partial derivative terms that will be used in gradient descent algorithm
		CM1.dAccumulator = scalarMultiplyMatrix(1.0/numberOfTrainingSets, CM1.deltaMatrix);
		CM2.dAccumulator = scalarMultiplyMatrix(1.0/numberOfTrainingSets, CM2.deltaMatrix);
		CM3.dAccumulator = scalarMultiplyMatrix(1.0/numberOfTrainingSets, CM3.deltaMatrix);
	}

	public void gradientDescent(){
		for(int t = 0; t < 1000000; t++){
			System.out.println("Gradient descent iteration #"+(t+1));
			
			backwardPropagation();

			//Update theta matrices
			double[][] theta1Update = scalarMultiplyMatrix(learningRate, CM1.dAccumulator);
			double[][] theta2Update = scalarMultiplyMatrix(learningRate, CM2.dAccumulator);
			double[][] theta3Update = scalarMultiplyMatrix(learningRate, CM3.dAccumulator);
			
//			System.out.println("In the current iteration, the theta1 update term is: ");
//			printMatrix(theta1Update);
//			System.out.println("In the current iteration, the theta2 update term is: ");
//			printMatrix(theta2Update);
//			System.out.println("In the current iteration, the theta3 update term is: ");
//			printMatrix(theta3Update);
			
			CM1.thetaMatrix = subtractMatrices(CM1.thetaMatrix, theta1Update);
			CM2.thetaMatrix = subtractMatrices(CM2.thetaMatrix, theta2Update);
			CM3.thetaMatrix = subtractMatrices(CM3.thetaMatrix, theta3Update);

			System.out.println();
		}
	}

	public void getHypothesis(double[] userEnteredQuery){
		
		singleForwardPropagation(userEnteredQuery);

		System.out.println("After training via gradient descent, the hypothesis generated by the neural network is: "+hypothesis);
		
	}

}
