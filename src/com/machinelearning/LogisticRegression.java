package com.machinelearning;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LogisticRegression {
	private static final int NUM_EPOCHS = 10000;
	private static final double LEARNING_RATE = 0.000001;
	
	private int numTrainingExamples = 0;
	private int numFeatures = 0;
	private int[][] featureMatrix;
	private int[] LabelVector;
	
	// controle de pesos
	private double[] weights;
	
	/*
	 * Testa o algoritmo de regress�o log�stica, assumindo
     * Os dados foram carregados corretamente nas vari�veis da inst�ncia
	 * Definido acima.
	 */
	public void testLogisticRegression()
	{
		int numNeg = 0;
		int numPos = 0;
		int numCorrectNeg = 0;
		int numCorrectPos = 0;
		for(int i = 0; i < featureMatrix.length; i++)
		{
			int classification = getClassification(featureMatrix[i]);
			if(LabelVector[i] == 0) {
				numNeg++;
				if(classification == LabelVector[i])
					numCorrectNeg++;
			} else {
				numPos++;
				if(classification == LabelVector[i])
					numCorrectPos++;
			}
		}
		
		System.out.println("Classe 0: testado " + numNeg + ", corretamente classificado " + numCorrectNeg);
		System.out.println("Classe 1: testado " + numPos + ", Corretamente classificado " + numCorrectPos);
		System.out.println("Geral: testado " + (numNeg+numPos) + ", correctly classified " + (numCorrectPos+numCorrectNeg));
		System.out.println("Precisao = " + (double)(numCorrectPos+numCorrectNeg) / (numNeg+numPos));
	}
	
	/*
	 * Calcula o termo linear na fun��o sigmoid ("z")
	 */
	private double calculateLinearTerm(int[] featureVector)
	{
		double linearTerm = 0;
		for(int i = 0; i < featureVector.length; i++)
		{
			linearTerm += weights[i] * (double)featureVector[i];
		}
		return linearTerm;
	}
	
	/*
	 * Obt�m a nossa classifica��o bin�ria para um determinado vetor de caracter�sticas
	 */
	private int getClassification(int[] featureVector) {
		double logOdds = calculateLinearTerm(featureVector);
		
		if(logOdds > 0)
			return 1;
		return 0;
	}
	
	/*
	 * Regress�o log�stica usando lote
     * Gradiente descendente
	 */
	public void trainLogisticRegression()
	{
		// Inicializar: pesos = 0 para todos os termos
		// adicione 1 para o termo 
		weights = new double[numFeatures+1];
		
		//inicializacao dos pesos
		for(int i = 0; i < weights.length; i++) 
			weights[i] = 0;
		
		for(int i = 0; i < NUM_EPOCHS; i++)
		{
			// add 1 for bias term
			double[] gradient = new double[numFeatures + 1];
			
			//inicializacao dos gradients
			for(int g = 0; g < gradient.length; g++) 
				gradient[g] = 0;
			
			/*for(int row = 0; row < featureMatrix.length; row++)
			{
				for(int col = 0; col < featureMatrix[0].length; col++)
				{
					gradient[col] += (double)featureMatrix[row][col] * ((double)LabelVector[row] - sigmoid(featureMatrix[row]));
				}
			}
			
			for(int j = 0; j < weights.length; j++)
				weights[j] += LEARNING_RATE * gradient[j];*/
			
			for(int row = 0; row < featureMatrix.length; row++)
			{
				for(int col = 0; col < featureMatrix[0].length; col++)
				{
					weights[col] += LEARNING_RATE * ((double)featureMatrix[row][col] * ((double)LabelVector[row] - sigmoid(featureMatrix[row])));
				}
			}
		}
	}
	

	/*
	 * Calcula a funcao sigmoid (da formula / (1 + e^(-z))
	 */
	private double sigmoid(int[] featureVector) {
		
		double linearTerm = calculateLinearTerm(featureVector);
		return 1.0 / (1.0 + Math.exp(-linearTerm));
	}

	/*
	 * O arquivo de entrada � sempre do formato
     * <N�mero de recursos>
     * <N�mero de exemplos de treinamento>
     * <... dados ...>
     * Este m�todo l� essas constantes e configura as vari�veis de inst�ncia apropriadas.
	 */
	private void readFileConstants(BufferedReader input) throws NumberFormatException, IOException
	{
		// Obtenha recursos num�ricos e exemplos de treinamento num
		numFeatures = Integer.parseInt(input.readLine());
		numTrainingExamples = Integer.parseInt(input.readLine());
		
		// adiciona 1 para o termo bias
		featureMatrix = new int[numTrainingExamples][numFeatures+1];
		LabelVector = new int[numTrainingExamples];
		
	}
	
	/*
	 * L� no vetor de dados de recursos e de verdade de terra de
     * Arquivo de entrada dado.
	 */
	public void readFeatureData(String fname)
	{
		try {

			BufferedReader input = new BufferedReader(new FileReader(fname));
			readFileConstants(input);
			
			String[] lineVector;
			int i = 0;
			for(String line = input.readLine(); line != null; line = input.readLine()) {
				// bias term
				featureMatrix[i][0] = 1;
				
				lineVector = line.split(" ");
				for(int j = 0; j < lineVector.length - 1; j++)
				{
					// semi-colon denotes the end of the feature data
					if(lineVector[j].indexOf(':') != -1) {
						lineVector[j] = lineVector[j].substring(0, 1);
					}
					featureMatrix[i][j+1] = Integer.parseInt(lineVector[j]);
				}
				LabelVector[i] = Integer.parseInt(lineVector[lineVector.length-1]);

				i++;
			}
			input.close();
			
			//printFeatureMatrix();
			
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		} 
	}
}
