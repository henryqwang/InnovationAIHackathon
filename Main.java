import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {

	private static final int INPUT_DIM = 6;
	private static final int HIDDEN_DIM2 = 8;
	private static final int HIDDEN_DIM3 = 8;
	private static final int OUTPUT_DIM = 1;
	
	private static final double LEARNING_RATE = 0.000000001;
	
	
	//Main helpers
	public static void printMatrix(double[][] matrix){
		for(int i = 0; i < matrix.length; i++){
			for(int j = 0; j<matrix[0].length; j++){
				System.out.print("["+matrix[i][j]+"]"+" ");
			}
			System.out.println();
		}
		System.out.println();
	}
	public static void printArray(double[] array){
		System.out.print("[");
		for(int i = 0; i<array.length-1; i++){
			System.out.print(array[i]+" | ");
		}
		System.out.print(array[array.length-1]+"]");
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
						}
					}
				}
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
			return result;
		}
	public static double findMax(double[] array){
		double max = array[0];
		for(int i = 0; i<array.length; i++){
			if(array[i] > max){
				max = array[i];
			}else{
				continue;
			}
		}
		return max;
	}
	public static double findMin(double[] array){
		double min = array[0];
		for(int i = 0; i<array.length; i++){
			if(array[i] < min){
				min = array[i];
			}else{
				continue;
			}
		}
		return min;
	}
	public static double findAverage(double[] array){
		double ave = 0;
		double m = (double)array.length;
		for(int i = 0; i<array.length; i++){
			ave += array[i];
		}
		return ave/m;
	}
	public static double[][] transpose(double [][] m)
	{
		double[][] temp = new double[m[0].length][m.length];
		for (int i = 0; i < m.length; i++)
			for (int j = 0; j < m[0].length; j++)
				temp[j][i] = m[i][j];
		return temp;
	}
	public static double[][] normalizeFeature(double[][] inputToBeNormalized){
		double[][] transposedInput = transpose(inputToBeNormalized); //6 by 3xx
		
		int rowSize = transposedInput.length;
		int colSize = transposedInput[0].length;
		for(int r = 0; r < rowSize - 1; r ++){ //We don't normalize the output, hence the -1
			double[] currentRow = transposedInput[r];
			double std = findMax(currentRow) - findMin(currentRow);
			double average = findAverage(currentRow);
			//System.out.println("For the current row, the std is: "+std+" and the ave is: "+average);
			for(int c = 0; c < colSize; c++){
				transposedInput[r][c] = (transposedInput[r][c] - average)/std;
			}
		}
		
		return transpose(transposedInput);
		
	}
	public static void main(String[] args) {
		
		double[][] fileInput = normalizeFeature(getInput("C:\\Users\\qice\\Desktop\\column vertebra input file.txt"));
		double[][] sampleTrainingSet = {{0,0,3,4,5,6,1.0},{0,1,4,5,6,7,0.0},{1,0,-3,-4,-5,-6, 0.0},{1,1,-5,-6,-7,-8, 1.0}};
		double[][] sampleInput = {{0,0,1,2,3,4},{0,1,-1,0,2,5},{1,0,9,2,-5,1},{1,1,-2,2,0,-6}};
		
		//printMatrix(fileInput);
		
		NeuralNetwork NW = new NeuralNetwork(INPUT_DIM, HIDDEN_DIM2, HIDDEN_DIM3, OUTPUT_DIM, LEARNING_RATE, fileInput);
		
//		NW.getHypothesis(sampleInput[0]);
//		NW.getHypothesis(sampleInput[1]);
//		NW.getHypothesis(sampleInput[2]);
//		NW.getHypothesis(sampleInput[3]);
		
//		//Abnormal case testing
		double[] abnormal1 = {63.0278175,22.55258597,39.60911701,40.47523153,98.67291675,-0.254399986};
//		double[] abnormal2 = {39.05695098,10.06099147,25.01537822,28.99595951,114.4054254,4.564258645};
//		double[] abnormal3 = {68.83202098,22.21848205,50.09219357,46.61353893,105.9851355,-3.530317314};
//		
//		//Normal case testing
//		double[] normal1 = {38.04655072,8.30166942,26.23683004,29.7448813,123.8034132,3.885773488};
		double[] normal2 = {50.16007802,-2.970024337,41.99999999,53.13010235,131.8024914,-8.290203373};
//		double[] normal3 = {82.90535054,29.89411893,58.25054221,53.01123161,110.7089577,6.079337831};
//		
//		System.out.println("Those should return abnormal (0): ");
		NW.getHypothesis(abnormal1);
//		NW.getHypothesis(abnormal2);
//		NW.getHypothesis(abnormal3);
//		System.out.println();
//		
//		System.out.println("Those should return normal (1): ");
//		NW.getHypothesis(normal1);
		NW.getHypothesis(normal2);
//		NW.getHypothesis(normal3);
//		System.out.println();
	}

}
