package de.uni.freiburg.iig.telematik.swat.simulation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.WorkflowTimeMachine;

public class SimulationResultsWriter {
	
	private static File file;

	
	public static void writeIntoOneFile(WorkflowTimeMachine wtm, File path) throws IOException{
		StringBuilder b = new StringBuilder();
		HashMap<String, ArrayList<Double>> results = wtm.getResult();
		b.append(path.getAbsolutePath()+"/");
		for(String s:results.keySet())
			b.append(s+"-");
		
		String entryName = (String)results.keySet().toArray()[0];
		
		file = new File(b.toString()+".csv");
		FileWriter fw = new FileWriter(file);
		
		for(String name:results.keySet())
			fw.write(name+"\t");
		
		fw.write(System.getProperty("line.separator"));
		
		for (int i = 0; i<results.get(entryName).size();i++){
			for(String name: results.keySet()){
				try{
					fw.write(Double.toString(results.get(name).get(i)));
					} catch (Exception e) {
						fw.write("NaN");
						}
				fw.write("\t");
			}
			fw.write(System.getProperty("line.separator"));
		}
		fw.flush();
		fw.close();
	}
	
	public static File getFile(){
		return file;
	}
	
	public static void writeIntoIndividualFiles(WorkflowTimeMachine wtm, File path) throws IOException{
		for(Entry<String, ArrayList<Double>> entry:wtm.getResult().entrySet()){
			String currentNet=entry.getKey();
			ArrayList<Double>results=entry.getValue();
			File file = getFileNameFor(path, wtm, currentNet);
			FileWriter fw = new FileWriter(file);
			for(Double d:results){
				fw.write(Double.toString(d));
				fw.write(System.getProperty("line.separator"));
			}
			fw.flush();
			fw.close();
		}
		
	}
	
	static private File getFileNameFor(File path, WorkflowTimeMachine wtm, String currentNet){
		StringBuilder b = new StringBuilder();
		b.append(path.getAbsolutePath()+"/");
		for(String netNames:wtm.getResult().keySet()) //get names of remaining open nets
			b.append(netNames+"-");
		b.append("("+currentNet.toUpperCase()+").csv"); //point out simulated net
		System.out.println("FilePath: "+b.toString());
		return new File(b.toString());
	}

}
