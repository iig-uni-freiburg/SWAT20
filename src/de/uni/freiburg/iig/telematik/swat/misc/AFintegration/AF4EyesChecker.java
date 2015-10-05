package de.uni.freiburg.iig.telematik.swat.misc.AFintegration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.swat.aristaFlow.AristaFlowToPnmlConverter;

public class AF4EyesChecker {
	
	public static void main (String[] args){
		AF4EyesChecker test = new AF4EyesChecker();
		LinkedList<String> list1 = new LinkedList<>();
		list1.add("Gerd");
		list1.add("Hans");
		list1.add("Friedrich");
		list1.add("Friedrich");
		
		LinkedList<String> list2 = new LinkedList<>();
		list2.add("Dieter");
		list2.add("Otto");
		list2.add("Friedrich");
		list2.add("Friedrich");
		list2.add("Gerd");
		
		System.out.println(test.checkLists(list1, list2));
		System.out.println("Violating users: ");
		for(String s:test.getViolatingUsers(list1, list2)){
			System.out.print(s+" ");
		}
		
		//AF-template test
		
		AF4EyesChecker test2= new AF4EyesChecker();
		try {
			System.out.println("----- template check ------");
			boolean checkResult = test2.check(new File("/tmp/af.template"), "Sign Form", "Open Account", getOriginatorMapping());
			System.out.println("Should be false: "+checkResult);		
			List<String> violatingUsers = test2.getViolatingUsers(new File("/tmp/af.template"), "Sign Form", "Open Account", getOriginatorMapping());
			System.out.println(printList(violatingUsers));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private AristaFlowToPnmlConverter parser;
	
	public AF4EyesChecker(){
		
	}
	
	public boolean check(File templateFile, String activity1, String activity2, Map<String, List<String>> globalSARUsersMapping) throws Exception {
		parser = new AristaFlowToPnmlConverter(templateFile);
		try {
			parser.parse();
			checkContains(activity1);
			checkContains(activity2);
			List<String> activity1users = getUsersFor(activity1, globalSARUsersMapping);
			List<String> activity2users = getUsersFor(activity2, globalSARUsersMapping);
			return checkLists(activity1users, activity2users);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new Exception("Could not parse template",e);
		}
	}
	
	public List<String> getViolatingUsers (File templateFile, String activity1, String activity2, Map<String, List<String>> globalSARUsersMapping) throws Exception {
		
		try {
			parser.parse();
			List<String> activity1users = getUsersFor(activity1, globalSARUsersMapping);
			List<String> activity2users = getUsersFor(activity2, globalSARUsersMapping);
			return getViolatingUsers(activity1users, activity2users);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new Exception("Could not parse template",e);
		}
		
	}
	
	private List<String> getUsersFor(String activity1, Map<String, List<String>> globalSARUsersMapping) {
		String originator = parser.getOriginator(activity1);
		return globalSARUsersMapping.get(originator);
	}

	/**returns false if at least one user is contained in both sets. Returns true if both sets are disjunct**/
	public boolean checkLists(List<String> users1, List<String> users2) {
		Multiset<String> users1Set = asMultiset(users1);
		Multiset<String> users2Set = asMultiset(users2);
		Multiset<String> intersection = users1Set.intersection(users2Set);
		if (intersection.isEmpty()) return true;
		return false;
	}
	
	/** returns the list of users that are contained in both sets**/
	public List<String> getViolatingUsers(List<String> users1, List<String> users2){
		Multiset<String> users1Set = asMultiset(users1);
		Multiset<String> users2Set = asMultiset(users2);
		Multiset<String> intersection = users1Set.intersection(users2Set);
		return asList(intersection);
	}
	
	
	private <T extends Object> Multiset<T> asMultiset(List<T> list){
		Multiset<T> result = new Multiset<>();
		for (T t:list){
			result.add(t);
		}
		return result;
	}
	
	private <T extends Object> List<T> asList(Multiset<T> multiSet) {
		LinkedList<T> result = new LinkedList<>();
		for(T t: multiSet.support()){
			result.add(t);
		}
		return result;
	}
	
	public void checkContains(String activityName) throws Exception{
		if(!parser.containsActivity(activityName)) throw new Exception("template does not contain acitivity "+activityName);
	}
	
	private static Map<String, List<String>> getOriginatorMapping(){
		HashMap<String, List<String>> mapping = new HashMap<>();
		
		
		
		LinkedList<String> list1 = new LinkedList<>();
		list1.add("Gerd");
		list1.add("Hans");
		list1.add("Friedrich");
		list1.add("Friedrich");
		
		//Activity "Sign Form"
		mapping.put("OrgPosition(name='Bank_Manager') AND Agent(id!=%i:NodePerforming-AgentID-#8%)", list1);
		
		LinkedList<String> list2 = new LinkedList<>();
		list2.add("Dieter");
		list2.add("Otto");
		list2.add("Friedrich");
		list2.add("Friedrich");
		list2.add("Gerd");
		
		//Activity "Open Account"
		mapping.put("OrgPosition(name='Post_Processor') AND Agent(id=%i:NodePerforming-AgentID-#8%)", list2);
		return mapping;
	}
	
	private static <T> String printList(List<T> list){
		String s ="";
		for(T t:list){
			s+=t+" ";
		}
		return s;
	}
	


}
