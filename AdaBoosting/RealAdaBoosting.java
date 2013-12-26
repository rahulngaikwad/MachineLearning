

import java.util.LinkedList;
import java.util.List;


public class RealAdaBoosting {
	int numberOfExamples;
	double epsilon;
	List<Example> exampleSet;
	List<Classifier> classifierSet;
	List<Classifier> selectedClassifier;
	
	public RealAdaBoosting(int numberOfIteration, int numberOfExamples,
			double epsilon, List<Example> exampleList, List<Classifier> hypothesis) {
		this.numberOfExamples = numberOfExamples;
		this.epsilon = epsilon;
		this.exampleSet = exampleList;
		this.classifierSet = hypothesis;
		this.selectedClassifier = new LinkedList<>();
	}
	
	public void iterate(int numberOfIteration){
		double boundOnNormalizationFactor = 1;
		
		for(int i = 1; i <= numberOfIteration; i++){
			Classifier bestClassifier = selectBestClassifier();
			bestClassifier.computeC(epsilon);
			double normalizationFactor = updateProbabilities(bestClassifier);
			boundOnNormalizationFactor *= normalizationFactor;
			//classifierSet.remove(bestClassifier);
			selectedClassifier.add(bestClassifier.clone());	

			System.out.println("--------------------------------------------------");
			System.out.println("Iterartion " + i);
			System.out.println("--------------------------------------------------");
			System.out.println("\n\nThe selected weak classifier: " + bestClassifier);
			System.out.println("The G error value of ht: " + bestClassifier.G);
			System.out.println("The weights Ct+ : " + bestClassifier.c_plus +", and  Ct- :"+ bestClassifier.c_minus);
			System.out.println("The probabilities normalization factor: " + normalizationFactor);
			System.out.print("The probabilities after normalization: ");
			for(int j = 0; j < exampleSet.size(); j++){
				System.out.print(exampleSet.get(j).getProbability() + ", ");
			}
			
			System.out.print("\nThe values Ft(Xi) for each one of the examples : ");
			int errorCount = 0;
			for(int l = 0; l < exampleSet.size(); l++){
				double targetAttribute  = computeTargetAttribute(l);
				System.out.print(targetAttribute + ",");
				
				if((exampleSet.get(l).getTagetAttribute()==-1 && targetAttribute >= 0.0) ||
				   (exampleSet.get(l).getTagetAttribute()== 1 && targetAttribute < 0.0)	) {
						errorCount++;
					}
			}
			
			System.out.println("\nThe error of the boosted classifier: " + (errorCount/(double)exampleSet.size()));
			System.out.println("The bound on E : " + boundOnNormalizationFactor);
			
		}
	}

	public Classifier selectBestClassifier(){
		Classifier bestClassifier = null;
		double minG = Integer.MAX_VALUE;
		for(int i = 0 ; i < classifierSet.size(); i++){
			Classifier classifier = classifierSet.get(i);
			classifier.computeP(exampleSet);
			classifier.computeG();
			if(classifier.G < minG ){
				minG = classifier.G;
				bestClassifier = classifier;
			}
		}
		return bestClassifier;
	}
	
	private double updateProbabilities(Classifier classifier) {
		double normalizationFactor = 0,q;
		for(int i = 0; i < exampleSet.size(); i++){	
			Example example = exampleSet.get(i);
			if(classifier.classifiedExamples.get(i).getTagetAttribute() == 1){
				q = Math.pow(Math.E, -1 * example.getTagetAttribute() * classifier.c_plus );
			} else {
				q = Math.pow(Math.E, -1 * example.getTagetAttribute() * classifier.c_minus );
			}
			example.setProbability(example.getProbability() * q );
			normalizationFactor += example.getProbability();
		}	
		
		for(int i = 0; i < exampleSet.size(); i++){
			exampleSet.get(i).setProbability(exampleSet.get(i).getProbability() / normalizationFactor);
		}
		return normalizationFactor;
	}
	
	private double computeTargetAttribute(int index) {
		double targetAttribute = 0;
		for(Classifier classifier : selectedClassifier){
			if(classifier.classifiedExamples.get(index).getTagetAttribute() == 1){
				targetAttribute +=  classifier.c_plus;
			}
			else {
				targetAttribute +=  classifier.c_minus;
			}
		}		
	return targetAttribute;
}
}
