package com.machinelearning;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class NaiveBayes 
{
	private boolean useLaplace = true;
	
	private int numTrainingExamples = 0;
	private int numFeatures = 0;
	private int[][] featureMatrix;
	private int[] LabelVector;
	
	private int posCount = 0;
	private int negCount = 0;
	private int[] featureCountsPos;
	private int[] featureCountsNeg;
	
	public NaiveBayes(boolean useLap) {
		useLaplace = useLap;
	}
	
	private int getClassification(int[] featureVector)
	{
		double posDenom = posCount;
		double negDenom = negCount;
		
		if(useLaplace) {
			posDenom += featureCountsPos.length;
			negDenom += featureCountsNeg.length;
		}
		double logProbPos = Math.log((double) posCount / (posCount + negCount));
		double logProbNeg = Math.log((double) negCount / (posCount + negCount));

		double posClass = 0;
		double negClass = 0;
		for(int i = 0; i < featureVector.length; i++)
		{
			if(featureVector[i] == 1) {
				// tem um "1" na posição i e é de classe pos ou neg
				posClass = featureCountsPos[i];
				negClass = featureCountsNeg[i];
			} else {
				// tem um "0" na posição i e é de classe positiva ou negativa
				posClass = posCount - featureCountsPos[i];
				negClass = negCount - featureCountsNeg[i];
			}
			
			if(useLaplace) {
				posClass += 1;
				negClass += 1;
			}
			
			logProbPos += Math.log( posClass / posDenom );
			logProbNeg += Math.log( negClass / negDenom );
		}
		
		if(logProbPos > logProbNeg)
			return 1;
		
		return 0;
		
	}
	
	
	public void testNaiveBayes()
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
		System.out.println("Classe 1: testado " + numPos + ", corretamente classificado " + numCorrectPos);
		System.out.println("Geral: testado " + (numNeg+numPos) + ", corretamente classificado " + (numCorrectPos+numCorrectNeg));
		System.out.println("Precisao = " + (double)(numCorrectPos+numCorrectNeg) / (numNeg+numPos));
		
	}
	
	
	/*
	* Treina o modelo de classificação de bayes nao inicializando uma matriz
	* Para contagens de classes positivas e uma para contagens de classe negativa. Isso então
	* Itera sobre os dados e acrescenta o número de lugares onde vemos um
	* 1 com Y = 1 e o número de lugares que vemos um 1 com Y = 0.	 
	*/
	public void trainNaiveBayes()
	{
		featureCountsPos = new int[numFeatures];
		featureCountsNeg = new int[numFeatures];
		
		for(int i = 0; i < featureMatrix.length; i++)
		{
			//Calculate the num of positive instance or negative instance
			if(LabelVector[i] == 1) 
				posCount++;
			else 
				negCount++;
			
			for(int j = 0; j < featureMatrix[0].length; j++)
			{
				if(LabelVector[i] == 1)
					featureCountsPos[j] += featureMatrix[i][j];
				else
					featureCountsNeg[j] += featureMatrix[i][j];
			}
		}
	}
	
	/*
	 * O arquivo de entrada é sempre do formato
     * <Número de recursos>
     * <Número de exemplos de treinamento>
     * <... dados ...>
     * <Feature data>: <label>
     * Este método lê essas constantes e configura as variáveis de instância apropriadas.
	 */
	private void readFileConstants(BufferedReader input) throws NumberFormatException, IOException
	{
		// Obtenha recursos numéricos e exemplos de treinamento num
		numFeatures = Integer.parseInt(input.readLine());
		numTrainingExamples = Integer.parseInt(input.readLine());
		
		featureMatrix = new int[numTrainingExamples][numFeatures];
		LabelVector = new int[numTrainingExamples];
	}
	
	/*
	* Lê nos dados de recursos e no vetor de verdade terrestre de
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

				lineVector = line.split(" ");
				for(int j = 0; j < lineVector.length - 1; j++)
				{
					// ponto-virro indica o fim dos dados da característica
					if(lineVector[j].indexOf(':') != -1) {
						lineVector[j] = lineVector[j].substring(0, 1);
					}
					featureMatrix[i][j] = Integer.parseInt(lineVector[j]);
				}
				// A última posição da linha é "Label"
				LabelVector[i] = Integer.parseInt(lineVector[lineVector.length-1]);
				i++;
			}
			input.close();
			
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		} 
	}
	
}
