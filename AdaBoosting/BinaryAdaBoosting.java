

import java.util.LinkedList;
import java.util.List;


public class BinaryAdaBoosting {
	int numberOfExamples;
	double epsilon;
	List<Example> exampleSet;
	List<Classifier> classifierSet;
	List<Classifier> selectedClassifier;
	
	public BinaryAdaBoosting(int numberOfIteration, int numberOfExamples,
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
			bestClassifier.computeAlpha();
			double normalizationFactor = updateProbabilities(bestClassifier);
			boundOnNormalizationFactor *= normalizationFactor;
			//classifierSet.remove(bestClassifier);
			selectedClassifier.add(bestClassifier.clone());	
		
			System.out.println("\n\n--------------------------------------------------");
			System.out.println("Iterartion " + i);
			System.out.println("--------------------------------------------------");
			System.out.println("The selected weak classifier: " + bestClassifier);
			System.out.println("The error of ht: " + bestClassifier.epsilon);
			System.out.println("The weight of ht: " + bestClassifier.alpha);
			System.out.println("The probabilities normalization factor: " + normalizationFactor);
			System.out.print("The probabilities after normalization: ");
			for(int j = 0; j < exampleSet.size(); j++){
				System.out.print(exampleSet.get(j).getProbability() + ", ");
			}
			System.out.print("\nThe boosted classifier: ");
			for(int k = 0; k < selectedClassifier.size(); k++){
				System.out.print(selectedClassifier.get(k).alpha +"*" + selectedClassifier.get(k) + " + ");
			}
			System.out.println("\nThe error of the boosted classifier: " + (getErrorCountOfBoostedClassifier()/(double)exampleSet.size()));
			System.out.println("The bound on Et : " + boundOnNormalizationFactor);
			
		}
	}

	public Classifier selectBestClassifier(){
		Classifier bestClassifier = null;
		double minEpsilon = 1.0;
		for(int i = 0 ; i < classifierSet.size(); i++){
			Classifier classifier = classifierSet.get(i);
			classifier.computeEpsilon(exampleSet);
			if(classifier.epsilon < minEpsilon ){
				minEpsilon = classifier.epsilon;
				bestClassifier = classifier;
			}
		}
		return bestClassifier;
	}
	
	private double updateProbabilities(Classifier classifier) {
		double normalizationFactor = 0, q;
		for(int i = 0; i < exampleSet.size(); i++){	
			if(exampleSet.get(i).getTagetAttribute() == classifier.classifiedExamples.get(i).getTagetAttribute()){
				q = Math.pow(Math.E, -1 * classifier.alpha);
			} else {
				q = Math.pow(Math.E, classifier.alpha);
			}
			exampleSet.get(i).setProbability(q*exampleSet.get(i).getProbability());
			normalizationFactor += exampleSet.get(i).getProbability();
		}	
		for(int i = 0; i < exampleSet.size(); i++){
			exampleSet.get(i).setProbability(exampleSet.get(i).getProbability() / normalizationFactor);
		}
		return normalizationFactor;
	}
	
	private int getErrorCountOfBoostedClassifier() {
	int errorCount = 0;
		for(int i = 0; i < exampleSet.size(); i++){
			double targetAttribute = 0;
			for(Classifier classifier : selectedClassifier){
				targetAttribute += classifier.alpha * classifier.classifiedExamples.get(i).getTagetAttribute();	
			}
			
			if((exampleSet.get(i).getTagetAttribute()==-1 && targetAttribute >= 0.0) ||
			   (exampleSet.get(i).getTagetAttribute()== 1 && targetAttribute < 0.0)	) {
				errorCount++;
			}
		}	
		return errorCount;
	}
}
