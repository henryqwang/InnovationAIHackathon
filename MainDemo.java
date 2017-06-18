import java.lang.Math;
import java.io.*;
public class MainDemo {
	
	//---------------------------------------CONSTANTS---------------------------------------//
	
	final static int NUMBER_Of_FEATURES=6;
	final static int NUMBER_OF_NODE_IN_FIRST_LAYER=10;
	final static int NUMBER_OF_NODE_IN_SECOND_LAYER=10;
	final static int NUMBER_OF_NODE_IN_OUTPUT_LAYER=1;
	
	final static double LEARNING_RATE_ALPHA = 0.01;
	
	//----------------------------------------HELPERS----------------------------------------//

	//Display Helpers
	
	public static void printMatrix(double[][] matrix){
		for(int i = 0; i < matrix.length; i++){
			for(int j = 0; j<matrix[0].length; j++){
				System.out.print("["+matrix[i][j]+"]"+" ");
			}
			System.out.println();
		}
	}
	public static void printArray(double[] array){
		System.out.print("[");
		for(int i = 0; i<array.length-1; i++){
			System.out.print(array[i]+" | ");
		}
		System.out.print(array[array.length-1]+"]");
		System.out.println();
	}


	//Operational Helpers
	
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
				System.out.println();
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
						System.out.print(result[numberOfLines-13][i]+" ");
					}
				}
				
			}
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		return result;
	}

	
	public static void main(String[] args) {
	
		/* read in the file*/
		//double[][] inputs=getInput("C:\\Users\\qice\\Desktop\\column vertebra input file.txt");

		
		//--------------------------------OBJECT INITIALIZATION---------------------------------//
//		NeuralNetwork NW = new NeuralNetwork(NUMBER_Of_FEATURES, NUMBER_OF_NODE_IN_FIRST_LAYER, NUMBER_OF_NODE_IN_SECOND_LAYER, NUMBER_OF_NODE_IN_OUTPUT_LAYER, LEARNING_RATE_ALPHA);
//		NW.displayNetworkDimensions();
//		NW.displayNetworkDetails();
//		
//		NW.randomInitializationOfWeights();
//		NW.displayNetworkDetails();
//		
//		
//		double[] inputFeatures = {1, 2, 3, 4, 5, 6};
//		NW.singleForwardPropagation(arrayToMatrix(inputFeatures));
//		NW.displayNetworkDetails();
		

	}

}
