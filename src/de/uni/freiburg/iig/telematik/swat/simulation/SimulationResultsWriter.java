package de.uni.freiburg.iig.telematik.swat.simulation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import de.invation.code.toval.misc.wd.ProjectComponentException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.FireElement;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.FireSequence;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.WorkflowTimeMachine;

public class SimulationResultsWriter {
	
	private static File file;
	static final String newLine = System.getProperty("line.separator");

	
//	public static void writeIntoOneFile(WorkflowTimeMachine wtm, File path) throws IOException{
//		StringBuilder b = new StringBuilder();
//		HashMap<String, ArrayList<Double>> results = wtm.getResult();
//		b.append(path.getAbsolutePath()+"/");
//		for(String s:results.keySet())
//			b.append(s+"-");
//		
//		String entryName = (String)results.keySet().toArray()[0];
//		
//		file = new File(b.toString()+".csv");
//		FileWriter fw = new FileWriter(file);
//		
//		for(String name:results.keySet())
//			fw.write(name+"\t");
//		
//		fw.write(System.getProperty("line.separator"));
//		
//		for (int i = 0; i<results.get(entryName).size();i++){
//			for(String name: results.keySet()){
//				try{
//					fw.write(Double.toString(results.get(name).get(i)));
//					} catch (Exception e) {
//						fw.write("NaN");
//						}
//				fw.write("\t");
//			}
//			fw.write(System.getProperty("line.separator"));
//		}
//		fw.flush();
//		fw.close();
//	}
	
	public static File getFile(){
		return file;
	}
	
	public static void writeTimeResults(WorkflowTimeMachine wtm, File path) throws IOException{
		for(Entry<String, ArrayList<Double>> entry:wtm.getResult().entrySet()){
			String currentNet=entry.getKey();
			ArrayList<Double>results=entry.getValue();
			File file = getFileNameFor(path, wtm, currentNet,"timing");
			FileWriter fw = new FileWriter(file);
			for(Double d:results){
				fw.write(Double.toString(d));
				fw.write(newLine);
			}
			fw.flush();
			fw.close();
		}
		
	}
	
	static private File getFileNameFor(File path, WorkflowTimeMachine wtm, String currentNet, String preffix){
		StringBuilder b = new StringBuilder();
		b.append(path.getAbsolutePath()+"/");
		b.append(preffix.toUpperCase()+"-");
		for(String netNames:wtm.getResult().keySet()) //get names of remaining open nets
			b.append(netNames+"-");
		b.append("("+currentNet.toUpperCase()+").csv"); //point out simulated net
		System.out.println("FilePath: "+b.toString());
		return new File(b.toString());
	}
	
	static public void writeCostsResults(WorkflowTimeMachine wtm, File path) throws ProjectComponentException, IOException {
		HashMap<String, LinkedList<Double>> costs = generateCostMap(wtm);
		for(String netName:costs.keySet()){
			//File path = new File("/home/richard/Dokumente/diss/Ausarbeitung mit Vorlage/img/costs/");
			File file = getFileNameFor(path, wtm, netName,"cost");
			try {
				FileWriter fw = new FileWriter(file);
				for(Double cost:costs.get(netName)){
					fw.write(Double.toString(cost));
					fw.write(newLine);
				}
				fw.flush();
				fw.close();
			} catch (IOException e) {
				// Do nothing
			}
		}
		
	}

	private static HashMap<String, LinkedList<Double>> generateCostMap(WorkflowTimeMachine wtm) throws ProjectComponentException, IOException {
		HashMap<String, LinkedList<Double>> costResult = new HashMap<>();
		for(String netName:wtm.getResult().keySet()){
			LinkedList<Double> resultList = new LinkedList<>();
			ArchitectureResults ar = new ArchitectureResults(wtm);
			double deadline = ar.getDeadlineFor(netName);
			TimedNet net = wtm.getNets().get(netName);
			for(double result: wtm.getResult().get(netName)){
				double cost=0;
				if(result>deadline){ //net has not met deadline
					cost += deadline * net.getCostPerTimeUnit(); //cost to deadline
					cost += (result-deadline)*net.getCostPerTimeUnitAfterDeadline(); //costs after deadline
				} else { //net is within deadline
					cost += result*net.getCostPerTimeUnit();
				}
				resultList.add(cost);
			}
			costResult.put(netName, resultList);
		}
		return costResult;
	}
	
	public static void writeFireSequence(File path, FireSequence fireSequence) throws IOException {
		File activityFile = new File(path,"Gannt-activities_"+getfileName(fireSequence)+".csv");
		File resourceFile = new File(path,"Gannt-resources_"+getfileName(fireSequence)+".csv");
		getActivityString(fireSequence);
		
		FileWriter fw1 = new FileWriter(activityFile);
		fw1.write(getActivityString(fireSequence));
		fw1.close();
		
		FileWriter fw2 = new FileWriter(resourceFile);
		fw2.write(getResourceString(fireSequence));
		fw2.close();
	}
	
	private static String getfileName(FireSequence fireSequence) {
		StringBuilder fileNameBuilder = new StringBuilder();
		for (String s:fireSequence.getNetNames()){
			fileNameBuilder.append(s+"-");
		}
		return fileNameBuilder.toString();
	}
	
	private static String getPath(){
		return"/home/richard/Dokumente/diss/Ausarbeitung mit Vorlage/img/gantt/";
	}

	private static String getActivityString(FireSequence fireSequence) {
		StringBuilder activities = new StringBuilder();
		//activities.append("#Activity Starttime Endtime UsedResources"+newLine);
		for(FireElement element:fireSequence.getSequence()){
			activities.append(getTransitionName(element));
			activities.append("\t");
			activities.append(element.getTime());
			activities.append("\t");
			activities.append(element.getEndTime());
			activities.append("\t");
			activities.append(getResourceUsage(element));
			activities.append(newLine);
		}
		return activities.toString();
		
	}
	
	private static String getResourceUsage(FireElement element) {
		StringBuilder res=new StringBuilder();
		for (String s: element.getResources()){
			res.append(s.replaceAll(" ", "-"));
			res.append("+");
		}
		res.deleteCharAt(res.length()-1);
		return res.toString();
	}

	private static String getResourceString(FireSequence sequence){
		StringBuilder resources = new StringBuilder();
		//resources.append("#ResourceUsage Starttime Endtime"+newLine);
		for (FireElement elemen:sequence.getSequence()){
			for(String resource:elemen.getResources()){
				resources.append(resource.replaceAll(" ", "_"));
				resources.append("\t");
				resources.append(elemen.getTime());
				resources.append("\t");
				resources.append(elemen.getEndTime());
				resources.append("\t");
				resources.append(elemen.getTransitionName().replaceAll(" ", "-"));
				resources.append(newLine);
			}
		}
		return resources.toString();
	}
	
	private static String getTransitionName(FireElement fireElement){
		return fireElement.getTransitionName().replaceAll(" ", "-");
	}
	
	public static void writePerformance(WorkflowTimeMachine wtm, File path) throws IOException, ProjectComponentException{
		File file = new File(path,"PERFORMANCE.csv");
		FileWriter fw = new FileWriter(file);
		ArchitectureResults ar = new ArchitectureResults(wtm);
		fw.write("Overall Performance: "+ar.getArchitecturePerformance());
		fw.write(" Overall Costs: "+ar.totalGeneratedCosts());
		fw.write(newLine);
		fw.close();
		
	}

}
