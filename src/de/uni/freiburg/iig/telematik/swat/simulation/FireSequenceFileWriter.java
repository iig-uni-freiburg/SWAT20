package de.uni.freiburg.iig.telematik.swat.simulation;

import java.io.File;
import java.io.FileWriter;
import java.io.FilterWriter;
import java.io.IOException;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.FireElement;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.FireSequence;

public class FireSequenceFileWriter {

	static final String newLine = System.getProperty("line.separator");
	
	public static void writeToDisk(FireSequence fireSequence) throws IOException {
		File activityFile = new File(getPath()+"activities_"+getfileName(fireSequence)+".csv");
		File resourceFile = new File(getPath()+"resources_"+getfileName(fireSequence)+".csv");
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
		activities.append("#Activity Starttime Endtime"+newLine);
		for(FireElement element:fireSequence.getSequence()){
			activities.append(getTransitionName(element));
			activities.append(" ");
			activities.append(element.getTime());
			activities.append(" ");
			activities.append(element.getEndTime());
			activities.append(newLine);
		}
		return activities.toString();
		
	}
	
	private static String getResourceString(FireSequence sequence){
		StringBuilder resources = new StringBuilder();
		resources.append("#ResourceUsage Starttime Endtime"+newLine);
		for (FireElement elemen:sequence.getSequence()){
			for(String resource:elemen.getResources()){
				resources.append(resource.replaceAll(" ", "_"));
				resources.append(" ");
				resources.append(elemen.getTime());
				resources.append(" ");
				resources.append(elemen.getEndTime());
				resources.append(newLine);
			}
		}
		return resources.toString();
	}
	
	private static String getTransitionName(FireElement fireElement){
		return fireElement.getTransitionName().replaceAll(" ", "_");
	}

}
