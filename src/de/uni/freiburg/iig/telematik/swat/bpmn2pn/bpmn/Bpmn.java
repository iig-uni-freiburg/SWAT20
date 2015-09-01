package de.uni.freiburg.iig.telematik.swat.bpmn2pn.bpmn;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetMarking;
import de.uni.freiburg.iig.telematik.sepia.serialize.PNSerialization;
import de.uni.freiburg.iig.telematik.sepia.serialize.SerializationException;
import de.uni.freiburg.iig.telematik.sepia.serialize.formats.PNSerializationFormat;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.BpmnElement;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.ServiceTask;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.Subprocess;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.Task;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.UserTask;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.gateways.ExclusiveGateway;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.gateways.MessageFlow;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.gateways.ParallelGateway;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.gateways.StartEvent;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.gateways.TerminationEvent;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.parser.XMLParser;

public class Bpmn<E> {
	private HashMap<String, BpmnElement> eventsHM = new HashMap<String, BpmnElement>();
	private HashMap<String, BpmnElement> elementsHM = new HashMap<String, BpmnElement>();
	private HashMap<String, BpmnElement> tasksHM = new HashMap<String, BpmnElement>();
	private HashMap<String, BpmnElement> subprocessesHM = new HashMap<String, BpmnElement>();
	private HashMap<String, BpmnElement> edgesHM = new HashMap<String, BpmnElement>();
	private HashMap<String, Pair<String, Pair<String, String>>> edgesPairHM = new HashMap<String, Pair<String, Pair<String, String>>>();
	private XMLParser xmlparser;
	private HashSet<String> activeEvents = new HashSet<String>();
	private HashMap<String, HashSet<String>> transitions = new HashMap<String, HashSet<String>>();
	private String startEvent, startEvent_id;
	private String endEvent, endEvent_id;
	private HashSet<String> terminationEvents = new HashSet<String>();
	private HashMap<String, String> mappingNode2EventId = new HashMap<String, String>();
	private String tmp;
	private String tmp2;
	IFNet ifNet = new IFNet();


	public HashMap<String, BpmnElement> getElementsHM() {
		return this.elementsHM;

	}


	public String findStartEvent() {
		String ha="";

		List<String> resulta;
		List<String> resultb1;
		List<String> resultb2;

		String out="";

		resulta = xmlparser
				.XPathHelper("//definitions/process/startEvent/@id");
		if (resulta.size() > 0) {

			for (String g : resulta) {
				out=g;
				return out;
			}

		}

		return "";

	}


	public String findFirstEdgeAfterStartEvent() {
		String ha="";

		List<String> resulta;
		List<String> resultb1;
		List<String> resultb2;

		String out="";

		resulta = xmlparser
				.XPathHelper("//definitions/process/startEvent/@id");
		if (resulta.size() > 0) {

			for (String g : resulta) {
				resultb1 = xmlparser
						.XPathHelper("//definitions/process/startEvent[@id='"
								+ g.toString() + "']/incoming/text()");

				resultb2 = xmlparser
						.XPathHelper("//definitions/process/startEvent[@id='"
								+ g.toString() + "']/outgoing/text()");
				if (resultb2.size() > 0) {
					for (String h : resultb2) {
						out = h.toString();
						ha=h;
					}
				}
				ParallelGateway ex = new ParallelGateway(g.toString(),
						new HashSet<String>(resultb1), new HashSet<String>(
								resultb2));
				return out;
			}

		}

		return "";

	}

	public int getElementsCount() {
		return elementsHM.size();
	}

	public int getTasksCount() {
		return tasksHM.size();
	}

	public int getEdgesCount() {
		return edgesHM.size();
	}

	public HashSet<ParallelGateway> getParallelGateways() {
		return (HashSet<ParallelGateway>) this
				.getGateways(new ParallelGateway());
	}


	public ParallelGateway getParallelGateway(String name) {
		HashSet<ParallelGateway> k = this.getParallelGateways();
		ParallelGateway r=null;
		for(ParallelGateway i : k) {
			if(i.getName().equals(name)) {
				r= (ParallelGateway)i;
			}
		}

		return r;
	}

	public HashSet<ExclusiveGateway> getExclusiveGateways() {
		return (HashSet<ExclusiveGateway>) this
				.getGateways(new ExclusiveGateway());
	}

	public HashSet<? extends BpmnElement> getGateways(BpmnElement type) {
		Class g = type.getClass();
		@SuppressWarnings("rawtypes")
		HashSet pg = new HashSet();
		for (BpmnElement a : elementsHM.values()) {
			if (a instanceof ExclusiveGateway) {
				if (type instanceof ExclusiveGateway)
					pg.add((ExclusiveGateway) a);
			}
			if (a instanceof ParallelGateway) {
				if (type instanceof ParallelGateway)
					pg.add((ParallelGateway) a);
			}
		}
		return (HashSet<ExclusiveGateway>) pg;
	}

	public Bpmn(String pathToFile) throws SerializationException, IOException {
		this.xmlparser = new XMLParser(pathToFile);
		this.generate();
		BpmnElement bpmn1 = this.elementsHM.get("assignApprover");
		System.out.println("Parallel Gateways");
		HashSet<ParallelGateway> pp;
		pp = getParallelGateways();
		for (ParallelGateway s : pp) {
			System.out.println("> " + s.getName().toString() + " InBound: "
					+ s.getInBound().toString() + " OutBound: "
					+ s.getOutBound().toString());
		}
		System.out.println();
		System.out.println("Exclusive Gateways");
		HashSet<ExclusiveGateway> ex;
		ex = getExclusiveGateways();
		for (ExclusiveGateway s : ex) {
			System.out.println("> " + s.getName().toString() + " InBound: "
					+ s.getInBound().toString() + " OutBound: "
					+ s.getOutBound().toString());
		}
		System.out.println("Edges");
		for (BpmnElement s : edgesHM.values()) {
			System.out.println("> " + s.getName().toString() + " InBound: "
					+ s.getInBound().toString() + " OutBound: "
					+ s.getOutBound().toString());
			String r1 = null, r2 = null;
			HashSet<String> k1 = s.getInBound();
			for (String s1 : k1) {
				r1 = s1;
			}
			HashSet<String> k2 = s.getOutBound();
			for (String s2 : k2) {
				r2 = s2;
			}
			Pair k = new Pair(r1, r2);
			edgesPairHM.put(s.getName().toString(), k);
		}
		IFNet ifnet = new IFNet();
		ifnet.addPlace("p_in");
		ifnet.getPlace("p_in").setColorCapacity("black", 1);
		Multiset<String> pInMarking = new Multiset<String>();
		pInMarking.add("black");
		IFNetMarking m = new IFNetMarking();
		m.set("p_in", pInMarking);
		ifnet.setInitialMarking(m);
		PNSerialization.serialize(ifnet, PNSerializationFormat.PNML,
				"bpmn-sample-definition");

	}

	private Boolean elementExists(String name) {
		return this.elementsHM.containsKey(name);
	}

	private BpmnElement getElement(String name) {
		BpmnElement bpmn = this.elementsHM.get(name);
		if (bpmn instanceof ParallelGateway)
			bpmn = (ParallelGateway) bpmn;
		if (bpmn instanceof ExclusiveGateway)
			bpmn = (ExclusiveGateway) bpmn;
		return bpmn;
	}

	public String findGateayById(String id) {
		String returnValue = "";
		String r = "";
		List<String> result;
		result = xmlparser.XPathHelper("//process/parallelGateway[@id='" + id
				+ "']");
		if (result.size() > 0)
			returnValue = "parallelGateway";
		result = xmlparser.XPathHelper("//process/exclusiveGateway[@id='" + id
				+ "']");
		if (result.size() > 0)
			returnValue = "exclusiveGateway";
		return returnValue;
	}

	public BpmnElement searchGeneralTasksTest(String name) {

		BpmnElement b = null;
		HashMap<String, BpmnElement> result = new HashMap<String, BpmnElement>();
		for (Object s : this.tasksHM.values()) {
			if (s instanceof UserTask) {
				if (s instanceof Task) {
					if (((UserTask) s).getInBound().contains(name))
						b = (BpmnElement) s;
				}
				;
			}
		}
		for (Object s : this.tasksHM.values()) {
			if (((Task) s).getInBound().contains(name))
				b = (BpmnElement) s;
		}
		for (Object s : this.elementsHM.values()) {
			if (s instanceof ParallelGateway) {
				if (((ParallelGateway) s).getInBound().contains(name)) {
					b = (BpmnElement) s;
				}
			}
			if (s instanceof ExclusiveGateway) {
				if (((ExclusiveGateway) s).getInBound().contains(name))
					b = (BpmnElement) s;
			}
			;
		}
		return b;
	}

	public BpmnElement searchGeneralTasksTest2(String name) {
		BpmnElement b = null;
		HashMap<String, BpmnElement> result = new HashMap<String, BpmnElement>();
		for (Object s : this.eventsHM.values()) {
			if (s instanceof TerminationEvent) {
				if (((TerminationEvent) s).getInBound().contains(name)) {
					b = (BpmnElement) s;
				}
			}
		}
		return b;
	}

	public BpmnElement searchGeneralTasksTestContains(String name) {
		BpmnElement b = null;
		HashMap<String, BpmnElement> result = new HashMap<String, BpmnElement>();
		for (Object s : this.tasksHM.values()) {
			if (s instanceof UserTask) {
				if (s instanceof Task) {
					if (((UserTask) s).getInBound().contains(name))
						b = (BpmnElement) s;
				}
				;
			}
		}
		return b;
	}

	public BpmnElement searchGeneralTasks(String name) {

		BpmnElement result = null;
		for (Object s : this.tasksHM.values()) {
			if (s instanceof Task) {
				Task t = (Task) s;
				if (((Task) s).getName().equals(name))
					result = t;
				;
			}
		}
		for (Object s : this.eventsHM.values()) {
			if (s instanceof StartEvent) {
				StartEvent t = (StartEvent) s;
				if (((StartEvent) s).getName().equals(name))
					result = t;
				;
			}
		}

		for (Object s : this.subprocessesHM.values()) {
			if (s instanceof Subprocess) {
				Subprocess t = (Subprocess) s;
				if (((Subprocess) s).getName().equals(name))
					result = t;
				;

			}
		}

		return result;
	}



	public HashMap<String, BpmnElement> getTasks() {
		HashMap<String, BpmnElement> result = new HashMap<String, BpmnElement>();
		for (Object s : this.tasksHM.values()) {
			if (s instanceof Task) {
				if (!(s instanceof UserTask)) {
					Task t = (Task) s;
					result.put(t.getName(), t);
				}
				;
			}
		}
		return result;
	}

	public HashMap<String, BpmnElement> getUserTasks() {
		HashMap<String, BpmnElement> result = new HashMap<String, BpmnElement>();
		for (Object s : this.tasksHM.values()) {
			if (s instanceof UserTask) {
				if (s instanceof Task) {
					Task t = (Task) s;
					result.put(t.getName(), t);
				}
				;
			}
		}
		return result;
	}

	public String findTaskById(String id) {
		String returnValue = "";
		String r = "";
		List<String> result;
		result = xmlparser.XPathHelper("//process/task[@id='" + id + "']");
		if (result.size() > 0)
			returnValue = "task";
		result = xmlparser.XPathHelper("//process/userTask[@id='" + id + "']");
		if (result.size() > 0)
			returnValue = "userTask";
		return returnValue;
	}

	private void generate() {




		List<String> resulta;
		List<String> resultb1;
		List<String> resultb2;
		resulta = xmlparser
				.XPathHelper("//definitions/process/exclusiveGateway/@id");
		if (resulta.size() > 0) {
			for (String g : resulta) {
				resultb1 = xmlparser
						.XPathHelper("//definitions/process/exclusiveGateway[@id='"
								+ g.toString() + "']/incoming/text()");

				resultb2 = xmlparser
						.XPathHelper("//definitions/process/exclusiveGateway[@id='"
								+ g.toString() + "']/outgoing/text()");

				ExclusiveGateway ex = new ExclusiveGateway(g.toString(),
						new HashSet<String>(resultb1), new HashSet<String>(
								resultb2));

				elementsHM.put(g.toString(), ex);
			}
		}
		resulta = xmlparser
				.XPathHelper("//definitions/process/parallelGateway/@id");
		if (resulta.size() > 0) {
			for (String g : resulta) {
				resultb1 = xmlparser
						.XPathHelper("//definitions/process/parallelGateway[@id='"
								+ g.toString() + "']/incoming/text()");
				if (resultb1.size() > 0) {
					for (String h : resultb1) {
						System.out.println("xin: " + h.toString());
					}
				}
				resultb2 = xmlparser
						.XPathHelper("//definitions/process/parallelGateway[@id='"
								+ g.toString() + "']/outgoing/text()");
				if (resultb2.size() > 0) {
					for (String h : resultb2) {
					}
				}
				ParallelGateway ex = new ParallelGateway(g.toString(),
						new HashSet<String>(resultb1), new HashSet<String>(
								resultb2));
				System.out.println("NAME, --> IN, --> OUT "
						+ ex.getName().toString());
				System.out.println("------> " + ex.getInBound().toString());
				System.out.println("------> " + ex.getOutBound().toString());
				System.out.println("ACCESS " + g.toString());
				elementsHM.put(g.toString(), ex);

				for (Object a : elementsHM.values()) {
					if (a instanceof ParallelGateway) {
						ParallelGateway b = (ParallelGateway) a;
						System.out.println("............. " + b.getName() + " "
								+ b.getInBound() + " " + b.getOutBound());
					}
				}
			}
		}

		resulta = xmlparser.XPathHelper("//definitions/process/startEvent/@id");
		if (resulta.size() > 0) {
			for (String g : resulta) {
				resultb1 = xmlparser
						.XPathHelper("//definitions/process/startEvent[@id='"
								+ g.toString() + "']/incoming/text()");
				if (resultb1.size() > 0) {
					for (String h : resultb1) {
						System.out.println("xin: " + h.toString());
					}

				}
				resultb2 = xmlparser
						.XPathHelper("//definitions/process/startEvent[@id='"
								+ g.toString() + "']/outgoing/text()");
				if (resultb2.size() > 0) {
					for (String h : resultb2) {
						System.out.println("out: " + h.toString());
					}
				}
				StartEvent ex = new StartEvent(g.toString(),
						new HashSet<String>(resultb1), new HashSet<String>(
								resultb2));
				System.out.println(ex.getInBound().toString());
				System.out.println(ex.getOutBound().toString());
				eventsHM.put(g.toString(), ex);
			}
		}


		resulta = xmlparser.XPathHelper("//definitions/process/endEvent/@id");
		if (resulta.size() > 0) {
			for (String g : resulta) {
				resultb1 = xmlparser
						.XPathHelper("//definitions/process/endEvent[@id='"
								+ g.toString() + "']/incoming/text()");
				if (resultb1.size() > 0) {
					for (String h : resultb1) {
						System.out.println("xin: " + h.toString());
					}

				}
				resultb2 = xmlparser
						.XPathHelper("//definitions/process/endEvent[@id='"
								+ g.toString() + "']/outgoing/text()");
				if (resultb2.size() > 0) {
					for (String h : resultb2) {
						System.out.println("out: " + h.toString());
					}
				}
				TerminationEvent ex = new TerminationEvent(g.toString(),
						new HashSet<String>(resultb1), new HashSet<String>(
								resultb2));
				System.out.println(ex.getInBound().toString());
				System.out.println(ex.getOutBound().toString());
				eventsHM.put(g.toString(), ex);
			}
		}
		resulta = xmlparser.XPathHelper("//definitions/process/userTask/@id");
		if (resulta.size() > 0) {
			for (String g : resulta) {
				resultb1 = xmlparser
						.XPathHelper("//definitions/process/userTask[@id='"
								+ g.toString() + "']/incoming/text()");
				if (resultb1.size() > 0) {
					for (String h : resultb1) {
						System.out.println("xin: " + h.toString());
					}
				}
				resultb2 = xmlparser
						.XPathHelper("//definitions/process/userTask[@id='"
								+ g.toString() + "']/outgoing/text()");
				if (resultb2.size() > 0) {
					for (String h : resultb2) {
						System.out.println("out: " + h.toString());
					}
				}
				UserTask ex = new UserTask(g.toString(), new HashSet<String>(
						resultb1), new HashSet<String>(resultb2));
				System.out.println(ex.getInBound().toString());
				System.out.println(ex.getOutBound().toString());
				tasksHM.put(g.toString(), ex);
			}
		}



		resulta = xmlparser.XPathHelper("//definitions/process/subProcess/@id");
		if (resulta.size() > 0) {
			for (String g : resulta) {
				resultb1 = xmlparser
						.XPathHelper("//definitions/process/subProcess[@id='"
								+ g.toString() + "']/incoming/text()");
				if (resultb1.size() > 0) {
					for (String h : resultb1) {
						System.out.println("xin: " + h.toString());
					}
				}
				resultb2 = xmlparser
						.XPathHelper("//definitions/process/subProcess[@id='"
								+ g.toString() + "']/outgoing/text()");
				if (resultb2.size() > 0) {
					for (String h : resultb2) {
						System.out.println("out: " + h.toString());
					}
				}
				Subprocess ex = new Subprocess(g.toString(), new HashSet<String>(
						resultb1), new HashSet<String>(resultb2), null, null);
				System.out.println(ex.getInBound().toString());
				System.out.println(ex.getOutBound().toString());
				subprocessesHM.put(g.toString(), ex);
			}
		}

		resulta = xmlparser.XPathHelper("//definitions/process/serviceTask/@id");
		if (resulta.size() > 0) {
			for (String g : resulta) {
				resultb1 = xmlparser
						.XPathHelper("//definitions/process/serviceTask[@id='"
								+ g.toString() + "']/incoming/text()");
				if (resultb1.size() > 0) {
					for (String h : resultb1) {
						System.out.println("xin: " + h.toString());
					}
				}
				resultb2 = xmlparser
						.XPathHelper("//definitions/process/serviceTask[@id='"
								+ g.toString() + "']/outgoing/text()");
				if (resultb2.size() > 0) {
					for (String h : resultb2) {
						System.out.println("out: " + h.toString());
					}
				}
				ServiceTask ex = new ServiceTask(g.toString(), new HashSet<String>(
						resultb1), new HashSet<String>(resultb2));
				System.out.println(ex.getInBound().toString());
				System.out.println(ex.getOutBound().toString());
				tasksHM.put(g.toString(), ex);
			}
		}

		resulta = xmlparser.XPathHelper("//definitions/process/task/@id");
		if (resulta.size() > 0) {
			for (String g : resulta) {
				resultb1 = xmlparser
						.XPathHelper("//definitions/process/task[@id='"
								+ g.toString() + "']/incoming/text()");
				if (resultb1.size() > 0) {
					for (String h : resultb1) {
						System.out.println("xin: " + h.toString());
					}

				}
				resultb2 = xmlparser
						.XPathHelper("//definitions/process/task[@id='"
								+ g.toString() + "']/outgoing/text()");
				if (resultb2.size() > 0) {
					for (String h : resultb2) {
						System.out.println("in: " + h.toString());
					}
				}
				Task ex = new Task(g.toString(), new HashSet<String>(resultb1),
						new HashSet<String>(resultb2), null, null);
				System.out.println(ex.getInBound().toString());
				System.out.println(ex.getOutBound().toString());
				tasksHM.put(g.toString(), ex);
			}
		}
		resulta = xmlparser
				.XPathHelper("//definitions/process/sequenceFlow/@id");
		if (resulta.size() > 0) {
			for (String g : resulta) {
				resultb1 = xmlparser
						.XPathHelper("//definitions/process/sequenceFlow[@id='"
								+ g.toString() + "']/@sourceRef");
				if (resultb1.size() > 0) {
					for (String h : resultb1) {
						System.out.println("in: " + h.toString());
					}
				}
				resultb2 = xmlparser
						.XPathHelper("//definitions/process/sequenceFlow[@id='"
								+ g.toString() + "']/@targetRef");
				if (resultb2.size() > 0) {
					for (String h : resultb2) {
						System.out.println("out: " + h.toString());
					}
				}
				MessageFlow ex = new MessageFlow(g.toString(), new HashSet<String>(
						resultb1), new HashSet<String>(resultb2));
				System.out.println(ex.getInBound().toString());
				System.out.println(ex.getOutBound().toString());
				edgesHM.put(g.toString(), ex);
			}
		}
		this.transitionFinder();
		List<String> result;
		result = xmlparser
				.XPathHelper("//definitions/process/startEvent/outgoing/text()");
		for (int i = 0; i < result.size(); i++) {
			String item = result.get(i);
			this.startEvent = item.toString();
		}
		result = xmlparser.XPathHelper("//definitions/process/startEvent/@id");
		for (int i = 0; i < result.size(); i++) {
			String item = result.get(i);
			this.startEvent_id = item.toString();
		}
		mappingNode2EventId.put(this.startEvent, this.startEvent_id);
		this.activeEvents.add(this.startEvent_id);
		result = xmlparser
				.XPathHelper("//definitions/process/endEvent/incoming/text()");
		for (int i = 0; i < result.size(); i++) {
			String item = result.get(i);
			this.endEvent = item.toString();
		}
		result = xmlparser.XPathHelper("//definitions/process/endEvent/@id");
		for (int i = 0; i < result.size(); i++) {
			String item = result.get(i);
			this.endEvent_id = item.toString();
		}
		mappingNode2EventId.put(this.endEvent, this.endEvent_id);
		result = xmlparser
				.XPathHelper("//definitions/process/endEvent/incoming/text()");
		for (int i = 0; i < result.size(); i++) {
			String item = result.get(i);
			this.terminationEvents.add(item.toString());
		}
		result = xmlparser
				.XPathHelper("//definitions/process/exclusiveGateway/incoming/text()");
		for (int i = 0; i < result.size(); i++) {
			String item = result.get(i);
			this.terminationEvents.add(item.toString());
		}
	}

	public Pair getEdge(String name) {
		Pair p = edgesPairHM.get(name);
		return p;
	}

	public HashSet<String> getActiveEvents() {
		return this.activeEvents;
	}

	public String getStartEvent() {
		return this.startEvent;
	}

	public String getStartEventId() {
		return this.startEvent_id;
	}

	public String getEndEvent() {
		return this.endEvent;
	}

	public String getEndEventId() {
		return this.endEvent_id;
	}

	public HashSet<String> getTerminationEvents() {
		return this.terminationEvents;
	}

	public boolean isTerminationEvent(String index) {
		return this.terminationEvents.contains(index);
	}

	public void explore() {
		for (String event : this.activeEvents) {
			System.out.println(event);
			this.lookupEventId(event);
		}
	}

	public void transitionFinder() {
		System.out.println("Start transition finder ...");
		List<String> result;
		List<String> result1;
		List<String> result2;
		for (String event : this.activeEvents) {
			result1 = xmlparser
					.XPathHelper("//definitions/process/sequenceFlow/@sourceRef");
			result2 = xmlparser
					.XPathHelper("//definitions/process/sequenceFlow/@targetRef");
			Object[] result1array = result1.toArray();
			Object[] result2array = result2.toArray();
			System.out.println("111111" + result1.size());
			String[] key = new String[result1.size()];
			String[] value = new String[result2.size()];
			System.out.println(result1.toString());
			for (int i = 0; i < result1.size(); i++) {
				String item = result1.get(i);
				this.tmp += item.toString();
				if (this.transitions.get(item.toString()) != null)
					this.transitions.get(item.toString()).add(
							(String) result2array[i]);
				else {
					HashSet<String> hs = new HashSet<String>();
					this.transitions.put(item.toString(), hs);
					this.transitions.get(item.toString()).add(
							(String) result2array[i]);
				}
			}
		}
	}

	public HashSet<String> getTargetSet(String node) {
		return this.transitions.get(node);
	}

	public HashMap<String, HashSet<String>> getTransitions() {
		return this.transitions;
	}

	public String getTmp() {
		return this.tmp;
	}

	public String getTmp2() {
		return this.tmp2;
	}

	public String lookupEventId(String node) {
		return this.mappingNode2EventId.get(node);
	}

	public BpmnElement findElementById(String name) {
		return this.searchGeneralTasks(name);

	}

	public BpmnElement findElementByIdTest(String name) {
		BpmnElement r = null;
		if (this.searchGeneralTasksTest(name) != null) {
			r = this.searchGeneralTasksTest(name);
		} else {
			r = this.searchGeneralTasksTest2(name);
		}
		return r;
	}

	public Boolean findElementByIdTestExists(String name) {
		Boolean result = true;
		if (this.searchGeneralTasksTest(name) == null)
			result = false;
		return result;
	}

	public Boolean findElementByIdTestContains(String name) {
		Boolean result = true;
		if (this.searchGeneralTasksTest(name) == null)
			result = false;
		if (this.searchGeneralTasksTest2(name) instanceof BpmnElement)
			result = true;
		return result;
	}

	public void searchAllTasks() {
	}
}