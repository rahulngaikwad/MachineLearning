package mlproject1.id3;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class ID3 {
	private static final String NAME_OF_FILES = "Enetr name of files 1.Dataset 2.Input Partiotion 3.Output Partition";
	public static void main(String[] args) throws IOException {
		System.out.println(NAME_OF_FILES);		
		Scanner scanner = new Scanner(System.in);
		Logger logger  = Logger.getLogger(ID3.class.getName());
		
		String dataset = scanner.next();
		String inputPartition = scanner.next();
		String outputPartition = scanner.next();
		scanner.close();
		
		List<Instance> instanceList = readDataset(dataset);		
		List<Partition> partitionList = readInputPartitions(inputPartition, instanceList);
		
		for(Instance instance : instanceList){
			System.out.println(instance);
		}
		
		Partition selectedPartition = null;
		double maxF = -1;
		int selectedFeature = -1;
		
		for(Partition partition : partitionList){			
			double entropyOfPartition = partition.getEntropy();
			System.out.println(partition);
			System.out.println("Entropy(" + partition.getName() + ") = " + entropyOfPartition);
			int noOfFeatures = partition.getInstances().get(0).getAttributes().size();		
			double maxGain = -1;
			int feature = -1;
			for(int i = 0; i < noOfFeatures; i++){
				double entropy = partition.getEntropyByAttribute(i);
				double gain = entropyOfPartition - entropy;
				if(gain > maxGain){
					maxGain = gain;
					feature = i;
				}
				System.out.print("Entropy(" +  partition.getName() + "|A" + (i+1) + ") = " + entropy );
				System.out.println("\t Gain(" +  partition.getName() +", A" + (i+1) + ") = " + gain);				
			}
			
			double f = (double)partition.getNoOfInstances()/instanceList.size() * maxGain;
			System.out.println(" F = " + f);
			if(f > maxF){
				maxF = f;
				selectedPartition = partition;
				selectedFeature = feature;
			}		
		}
				
		List<Partition> newPartitions= selectedPartition.devidePartitionByAttribute(selectedFeature);
		int index = partitionList.indexOf(selectedPartition);
		partitionList.remove(selectedPartition);
		partitionList.addAll(index, newPartitions);
		
		System.out.print("Partition "+ selectedPartition.getName() + " was replaced with partition ");
		for(int i = 1; i <= newPartitions.size() ; i++){
			System.out.print(selectedPartition.getName() + i +", ");
		}
		System.out.print("using Feature " + (selectedFeature + 1) );
		
		PrintWriter output = new PrintWriter(new FileWriter(outputPartition, false));
		for(Partition partition : partitionList){	
			output.println( partition);
		}
		output.close();
	}

	private static List<Partition> readInputPartitions(String inputPartitionFile, List<Instance> instanceList) throws IOException {
		Scanner lineScanner = new Scanner(new File(inputPartitionFile));
		List<Partition> partitionList = new ArrayList<Partition>();
		
		while(lineScanner.hasNextLine()){
			String line = lineScanner.nextLine();
			if(line.length() > 0) {
				Scanner textscanner = new Scanner(line);			
			    String partitionName = textscanner.next();
			    List<Instance> partitionInstances = new ArrayList<>();	    
				while(textscanner.hasNext()){
					int instanceId = textscanner.nextInt();
					partitionInstances.add(instanceList.get(instanceId-1));
				}
				partitionList.add(new Partition(partitionName,partitionInstances));	
				textscanner.close();
			}
		}	
		lineScanner.close();
		return partitionList;
	}

	private static List<Instance> readDataset(String datasetFile) throws IOException {
		Scanner scanner = new Scanner(new File(datasetFile));		
		int numberOfInstances = scanner.nextInt();
		int numberOfAttributes = scanner.nextInt();		
		ArrayList<Instance> instanceList = new ArrayList<Instance>(numberOfInstances);
		
		for(int i = 1; i <= numberOfInstances; i++){
			Instance instance = new Instance();
			instance.setInstanceId(i);			
			ArrayList<Integer> attributeList = new ArrayList<Integer>(numberOfAttributes-1);
			for(int j = 1; j < numberOfAttributes; j++){
				attributeList.add(scanner.nextInt());
			}		
			instance.setAttributes(attributeList);
			instance.setTargetAttribute(scanner.nextInt());
			instanceList.add(instance);
		}
		scanner.close();
		return instanceList;
	}
	
}
