

import java.util.ArrayList;
import java.util.List;

public class Classifier {

	boolean isLowerPositive;
	double decisionPoint; 
	List<Example> classifiedExamples;
	double epsilon,alpha;
	double p_plus_right, p_minus_right, p_plus_wrong, p_minus_wrong, G, c_plus, c_minus;
	
	public Classifier(boolean isLowerPositive, double decisionPoint,
			List<Example> originalList) {
		this.isLowerPositive = isLowerPositive;
		this.decisionPoint = decisionPoint;
		this.classifiedExamples = classifyAllExamples(originalList);
	}

	public List<Example> classifyAllExamples(List<Example> originalList) {
			classifiedExamples = new ArrayList<>(originalList.size());
			for(int i = 0; i < originalList.size(); i++){
				Example example = originalList.get(i).clone();
				classify(example);
				classifiedExamples.add(example);
			}
		return classifiedExamples;
	}

	private void classify(Example example) {
		example.setTagetAttribute(getTargetAttribute(example));
	}

	private int getTargetAttribute(Example example) {
		if(example.getAttribute() < decisionPoint){
			return isLowerPositive ? 1 : -1;
		}	
	   return isLowerPositive ? -1 : 1;
	}
	
	public double computeAlpha() {	
		alpha = 0.5 * Math.log( (1 - epsilon) / epsilon);
		return alpha;
	}
	
	public void computeEpsilon(List<Example> exampleSet){
	 epsilon = 0;
		for(int i = 0; i < exampleSet.size(); i++){
			if(classifiedExamples.get(i).getTagetAttribute() != exampleSet.get(i).getTagetAttribute()){
				epsilon += exampleSet.get(i).getProbability();
			}
		}
	}
	
	public double computeG( ){
		G = Math.sqrt(p_plus_right * p_minus_wrong) + Math.sqrt(p_plus_wrong * p_minus_right);
		return G;
	}
	
	public void computeC(double  epsilon) {
		c_plus = 0.5 * Math.log((p_plus_right + epsilon) / (p_minus_wrong + epsilon) );
		c_minus = 0.5 * Math.log((p_plus_wrong + epsilon) / (p_minus_right + epsilon) );
	}
	
	public void computeP(List<Example>  exampleSet) {
		p_plus_right = p_plus_wrong = p_minus_right = p_minus_wrong = 0;
		for(int i = 0; i < exampleSet.size(); i++){	
			if(exampleSet.get(i).getTagetAttribute() == 1){
				if(classifiedExamples.get(i).getTagetAttribute()==1){
					p_plus_right += exampleSet.get(i).getProbability();
				} else {
					p_plus_wrong += exampleSet.get(i).getProbability();
				}
			} else {
				if(classifiedExamples.get(i).getTagetAttribute()==-1){
					p_minus_right += exampleSet.get(i).getProbability();
				} else {
					p_minus_wrong += exampleSet.get(i).getProbability();
				}
			}
		}
	}
		
	@Override
	public String toString() {
		if(isLowerPositive){
			return "I(x < " + decisionPoint+")";
		}
	  return "I(x > " + decisionPoint+")";
	}

	public Classifier clone() {
		Classifier cloned  =  new Classifier(isLowerPositive, decisionPoint,classifiedExamples);
		cloned.epsilon = epsilon;
		cloned.alpha = alpha;
		cloned.p_plus_right = p_plus_right; 
		cloned.p_plus_wrong = p_plus_wrong;
		cloned.p_minus_right = p_minus_right; 
		cloned.p_minus_wrong = p_minus_wrong;
		cloned.G = G;
		cloned.c_plus = c_plus;
		cloned.c_minus = c_minus;
		return cloned;
	}
}
