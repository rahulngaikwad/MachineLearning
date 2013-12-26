

public class Example {

	private double attribute;
	private int tagetAttribute;
	private double probability;
	
	
	public Example() {
		this.attribute = 0;
		this.tagetAttribute = 0;
		this.probability = 0;
	}

	public Example(double attribute, int tagetAttribute, double probability) {
		this.attribute = attribute;
		this.tagetAttribute = tagetAttribute;
		this.probability = probability;
	}

	public double getAttribute() {
		return attribute;
	}

	public void setAttribute(double attribute) {
		this.attribute = attribute;
	}

	public int getTagetAttribute() {
		return tagetAttribute;
	}

	public void setTagetAttribute(int tagetAttribute) {
		this.tagetAttribute = tagetAttribute;
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}
	
	public Example clone() {
		return new Example(attribute,tagetAttribute,probability);
	}

	@Override
	public String toString() {
		return "x=" + attribute + ", y="
				+ tagetAttribute + ", p=" + probability ;
	}
	
	
}
