package de.uni.freiburg.iig.telematik.swat.jascha;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.invation.code.toval.parser.ParserException;
import de.uni.freiburg.iig.telematik.sewol.log.LogEntry;
import de.uni.freiburg.iig.telematik.sewol.log.LogTrace;
import de.uni.freiburg.iig.telematik.sewol.parser.LogParser;

public class HumanResourceExtractor {

	//The list contains the HumanResources extracted from the given logfile
	List<String> humanResources;
	
	public HumanResourceExtractor(String filepath) {
		humanResources = new LinkedList<String>();
		extractResources(filepath);
	}
	
	public List<String> getHumanResources(){
		return humanResources;
	}
	
	public void createResources(ResourceStore store){
		for(String name: humanResources){
			store.instantiateResource(ResourceType.HUMAN, name);
		}		
	}
	
	public void extractResources(String filepath){
		
		LogParser parser = new LogParser();

		try {
	        List<List<LogTrace<LogEntry>>> log;
	        List<String> HumanResources = new LinkedList<>();
	        HashSet<String> hashSet = new HashSet<String>();	        
			//log = parser.parse(new File(filepath));
			log = parser.parse(filepath);
	        
	        //Outer list is always of size 1 but it also works if it's of greater size
	        for (int i = 0; i < log.size(); i++) {
				//System.out.println(i);
				//Inner list holds LogTraces which in turn hold LogEntries but we don't need to look at the LogEntries.
				for (LogTrace trace : log.get(i)) {
					//String test = trace.getDistinctOriginators().toString();
					Set<String> set = trace.getDistinctOriginators();
					for(String entry:set){
						hashSet.add(entry);
					}
					
					//System.out.println(hashSet);
				}
				
				
				//System.out.println(hashSet);
			}
	        //System.out.println(hashSet);
	        for(String human: hashSet){
	        	humanResources.add(human);
	        	//System.out.println("Creating HumanResource named " + human + "...");
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

}
