

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RunBinaryAdaBoosting {

	private static  String INPUT_FILE = "C:\\Study\\ML\\Project\\Project2\\input\\adaboost-5.dat";
	
	static int numberOfIteration;
	static int numberOfExamples;
	static double epsilon;
	static List<Example> exampleList;
	static List<Classifier> hypothesis;

	public static void main(String[] args) throws IOException {
		if(args.length < 1 ){
			System.out.println("Please enter path of input file");
			Scanner scanner = new Scanner(System.in);
			INPUT_FILE = scanner.nextLine();
			scanner.close();
		} else {
			INPUT_FILE = args[0];
		}
		
		readInputFile(INPUT_FILE);
		
		generateAllHypothesis(exampleList);
		System.out.println("numberOfExamples = " + numberOfExamples);
		System.out.println("numberOfIteration = " + numberOfIteration);
		System.out.println("epsilon = " + epsilon);

		BinaryAdaBoosting adaBoost = new BinaryAdaBoosting(numberOfIteration, numberOfExamples, epsilon, exampleList, hypothesis);
		adaBoost.iterate(numberOfIteration);	
	}
	
	private static List<Classifier> generateAllHypothesis(List<Example> exampleList) throws IOException {
		hypothesis = new ArrayList<>();
		double firstDecisionPoint = exampleList.get(0).getAttribute()-1.0;
		hypothesis.add(new Classifier(true, firstDecisionPoint, exampleList));
		hypothesis.add(new Classifier(false, firstDecisionPoint, exampleList));
		
		for(int i = 0; i < exampleList.size()-1; i++){	
			double decisionPoint = (exampleList.get(i).getAttribute() + exampleList.get(i+1).getAttribute()) / 2.0 ; 
			hypothesis.add(new Classifier(true, decisionPoint, exampleList));
			hypothesis.add(new Classifier(false, decisionPoint, exampleList));
		}
		
		double lastDecisionPoint = exampleList.get(exampleList.size()-1).getAttribute()+1.0;
		hypothesis.add(new Classifier(true, lastDecisionPoint, exampleList));
		hypothesis.add(new Classifier(false, lastDecisionPoint, exampleList));
		
		return hypothesis;
	}
	
	private static void readInputFile(String inputFile) throws IOException {
		Scanner scanner = new Scanner(new File(inputFile));		
		 numberOfIteration = scanner.nextInt();
		 numberOfExamples = scanner.nextInt();
		 epsilon = scanner.nextDouble();
		 exampleList = new ArrayList<Example>(numberOfExamples);
		for(int i = 0; i < numberOfExamples; i++){
			Example example  = new Example();
			example.setAttribute(scanner.nextDouble());
			exampleList.add(example);
		}
		for(int i = 0; i < numberOfExamples; i++){
			exampleList.get(i).setTagetAttribute(scanner.nextInt());
		}
		for(int i = 0; i < numberOfExamples; i++){
			exampleList.get(i).setProbability(scanner.nextDouble());
		}	
		scanner.close();
	}

}
