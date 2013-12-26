package mlproject1.id3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class Partition{
	String name;
	List<Instance> instances;

	public Partition(){
		this.name = null;
		this.instances = null;
	}
	
	public Partition(String name, List<Instance> instances){
		this.name = name;
		this.instances = instances;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Instance> getInstances() {
		return instances;
	}

	public void setInstances(List<Instance> instances) {
		this.instances = instances;
	}

	public int getNoOfInstances(){
		return instances.size();
	}
	
	@Override
	public String toString() {
		String instanceList = "";
		for(Instance instance : instances){
			instanceList += instance.getInstanceId() + ",";
		}
		return "Partition " + name + ", instances=[" + instanceList + "]";
	}
	
	/**
	 * @param p1
	 * @param p2
	 * @return
	 */
	private double entropy(double p1, double p2){
		double ans = 0;
		if(p1 != 0){
			ans += p1*(-1)*Math.log10(p1);
		}		
		if(p2 != 0){
			ans += p2*(-1)*Math.log10(p2);
		}
		return ans/Math.log10(2);
	}
	
	/**
	 * returns entropy of a partition
	 * @return
	 */
	
	public double getEntropy(){
		int[] count = new int[2];
		for(Instance instance : instances){
			count[instance.getTargetAttribute()]++;
		}	
		double p1 = (double)count[0]/getNoOfInstances();
		double p2 = (double)count[1]/getNoOfInstances();
		return entropy(p1,p2);
	}
	
	/**
	 * returns entropy for given attribute
	 * @param attributeIndex : Attribute Index
	 * @return entropy
	 */
	public double getEntropyByAttribute(int attributeIndex){
		int[] instanceCount = new int[3];
		for(Instance instance : instances){
			int attributeValue = instance.getAttributes().get(attributeIndex);
			instanceCount[attributeValue]++;
		}		
		double ans = 0;
		for(int attributeValue = 0; attributeValue < 3; attributeValue++){
			ans += (double)instanceCount[attributeValue]/getNoOfInstances()*getEntropyByAttributeValue(attributeIndex,attributeValue);
		}
		return ans;
	}
	
	/**
	 * returns entropy for given attribute and for given value
	 * @param index : Attribute Index
	 * @param value : Attribute value
	 * @return entropy
	 */
	private double getEntropyByAttributeValue(int index, int value) {
		int noOfInstances = 1;
		int[] instanceCountByTargetAttribute = new int[2];
		for(Instance instance : instances){
			if(instance.getAttributes().get(index) == value){	
				noOfInstances++;
				instanceCountByTargetAttribute[instance.getTargetAttribute()]++;
			}
		}			
		if(noOfInstances > 1){
			noOfInstances--;
		}
		double p1 = (double)instanceCountByTargetAttribute[0]/noOfInstances;
		double p2 = (double)instanceCountByTargetAttribute[1]/noOfInstances;
		return entropy(p1,p2);
	}

	public List<Partition> devidePartitionByAttribute(int attributeIndex){
		List<Partition> partitions = new ArrayList<>(3);
		for(int i = 0; i < 3.; i++){
			partitions.add(new Partition(getName()+i,new ArrayList<Instance>()));
		}
		
		for(Instance instance : instances){
			int attributeValue = instance.getAttributes().get(attributeIndex);
			Partition partition = partitions.get(attributeValue);
			partition.getInstances().add(instance);
		}	
		
		int counter = 1;		
		for(Iterator<Partition> itr = partitions.iterator(); itr.hasNext(); ){
			Partition partition = itr.next();
			if(partition.getInstances().size() == 0){
				itr.remove();
			}
			else{
				partition.setName(getName() + counter++);
			}
		}
		return partitions;
	}
	public static void main(String[] args ){
		Partition p = new Partition();
		System.out.println(p.entropy((double)1, (double)1));
	}
}