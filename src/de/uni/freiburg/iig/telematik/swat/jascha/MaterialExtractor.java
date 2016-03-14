package de.uni.freiburg.iig.telematik.swat.jascha;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.uni.freiburg.iig.telematik.sewol.log.DataAttribute;
import de.uni.freiburg.iig.telematik.sewol.log.LogEntry;
import de.uni.freiburg.iig.telematik.sewol.log.LogTrace;

public class MaterialExtractor {
	
	List<String> materials;
	List<LogTrace<LogEntry>> log;
	//String AttributeName = "Material";
	String[] AttributeNames = {"Material","Resource","org:resource"};

	public MaterialExtractor(List<LogTrace<LogEntry>> log) {
		this.log = log;
		materials = extractMaterials();
	}

	private List<String> extractMaterials() {
		HashSet<String> materialSet = new HashSet<String>();
		Set<DataAttribute> DA_list;
		for (LogTrace<LogEntry> trace : log) {
			for (LogEntry logEntry : trace.getEntries()) {
				DA_list = logEntry.getMetaAttributes();
				for (DataAttribute DA : DA_list) {
					for (String AttributeName : AttributeNames) {
						if (DA.name.equalsIgnoreCase(AttributeName)) {
							materialSet.add(DA.value.toString());
							// break; // Possible there was only one Entry of Type "Material".
						}
					}
				}

			}
		}
		return hashSetToList(materialSet);
	}
	
	private List<String> hashSetToList(HashSet<String> hashset){
		List<String> result = new LinkedList<String>();
		for (String entry: hashset){
			result.add(entry);			
		}
		return result;
	}
	
	public void createResources(ResourceStore store){
		for(String name: materials){
			store.instantiateResource(ResourceType.SIMPLE, name);
		}		
	}
	
	public List<String> getMaterialNames(){
		return materials;
	}

}
