package de.uni.freiburg.iig.telematik.swat.jascha;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogReader;

import de.invation.code.toval.parser.ParserException;
import de.uni.freiburg.iig.telematik.sewol.log.Log;
import de.uni.freiburg.iig.telematik.sewol.log.LogEntry;
import de.uni.freiburg.iig.telematik.sewol.log.LogTrace;
import de.uni.freiburg.iig.telematik.sewol.parser.AbstractLogParser;
import de.uni.freiburg.iig.telematik.sewol.parser.LogParser;
import de.uni.freiburg.iig.telematik.sewol.parser.mxml.MXMLLogParser;
import de.uni.freiburg.iig.telematik.sewol.parser.xes.XESLogParser;
import de.uni.freiburg.iig.telematik.swat.logs.LogModel;
import de.uni.freiburg.iig.telematik.swat.plugin.sciff.LogParserAdapter;

public class HumanResourceExtractor {

	//The list contains the HumanResources extracted from the given logfile
	List<String> humanResources;
	List<LogTrace<LogEntry>> log;
	
	public HumanResourceExtractor(LogModel model) throws Exception {
		humanResources = new LinkedList<String>();
		switch (model.getType()) {
		case MXML:
		case XES:
			LogParserAdapter adapter = (LogParserAdapter) model.getLogReader();
			log = adapter.getOriginalLog().getFirstParsedLog();
			extractResources();
			break;
		default:
			throw new Exception("Can only parse XES or MXML logs");
		}
	}
	
	public HumanResourceExtractor(String filepath) {
		humanResources = new LinkedList<String>();
		extractResourcesOld(filepath);
	}
	
	public List<String> getHumanResources(){
		return humanResources;
	}
	
	public void createResources(ResourceStore store){
		for(String name: humanResources){
			store.instantiateResource(ResourceType.HUMAN, name);
		}		
	}
	
	//Adds the activities from the log and the corresponding resources to a resoucreContext
	public void addActivities(AwesomeResourceContext context, ResourceStore store){
		for (String originator:humanResources){
			for (LogTrace<LogEntry> trace : log){
				for (LogEntry logEntry: trace.getEntries()){
					if (originator.equals(logEntry.getOriginator())){
						context.addResourceUsage(logEntry.getActivity(), store.getResource(originator));
					}
					
				}
			}
			
		}
	}
	
	public void extractResources(){	
        HashSet<String> hashSet = new HashSet<String>();
        
			for (LogTrace<LogEntry> trace : log) {
				Set<String> set = trace.getDistinctOriginators();
				for(String entry:set){
					hashSet.add(entry);
					}
		}
        for(String human: hashSet){
        	humanResources.add(human);
        }
	}
	
	public void extractResourcesOld(String filepath){		
		LogParser parser = new LogParser();
		try {
	        List<List<LogTrace<LogEntry>>> localLog;
	        HashSet<String> hashSet = new HashSet<String>();	        
			//log = parser.parse(new File(filepath));
			localLog = parser.parse(filepath);
	        
	        //Outer list is always of size 1 but it also works if it's of greater size
	        for (int i = 0; i < localLog.size(); i++) {
				//System.out.println(i);
				//Inner list holds LogTraces which in turn hold LogEntries but we don't need to look at the LogEntries.
				for (LogTrace<LogEntry> trace : localLog.get(i)) {
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
	
	public void testMe(){
		for (LogTrace<LogEntry> trace:log){
			System.out.println(trace.getActivities());
		}
	}
}
