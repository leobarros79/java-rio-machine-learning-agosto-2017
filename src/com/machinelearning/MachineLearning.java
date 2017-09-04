package com.machinelearning;

public class MachineLearning {
	// parametros
	private String dataDir = "datasets/";
	private String trainFile = "heart-train.txt";
	private String testFile = "heart-test.txt";

	public void doNaiveBayes() {

		String dataDir = "datasets/";
		String trainFile = "heart-train.txt";
		String testFile = "heart-test.txt";
		// parametro
		// usar lugar ou nao?
		boolean useLaplace = true;

		NaiveBayes nb = new NaiveBayes(useLaplace);
		nb.readFeatureData(dataDir + trainFile);
		nb.trainNaiveBayes();

		nb.readFeatureData(dataDir + testFile);
		nb.testNaiveBayes();
	}

	public void doLogisticRegression() {
		LogisticRegression lr = new LogisticRegression();
		lr.readFeatureData(dataDir + trainFile);
		lr.trainLogisticRegression();

		lr.readFeatureData(dataDir + testFile);
		lr.testLogisticRegression();
	}

	public static void main(String[] args) {
		MachineLearning ml = new MachineLearning();
		ml.doNaiveBayes();
//		ml.doLogisticRegression();
	}
}
