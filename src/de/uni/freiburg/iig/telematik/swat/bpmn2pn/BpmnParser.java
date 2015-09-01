package de.uni.freiburg.iig.telematik.swat.bpmn2pn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.bpmn.Pair;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.gateways.EndEvent;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.gateways.ExclusiveGateway;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.gateways.InclusiveGateway;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.gateways.MessageFlow;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.gateways.ParallelGateway;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.gateways.SequenceFlow;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.gateways.StartEvent;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.gateways.TerminationEvent;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.parser.XMLParser;
public class BpmnParser {
	private HashMap<String, BpmnElement> edgesHM = new HashMap<String, BpmnElement>();
	private HashMap<String, BpmnElement> messagesHM = new HashMap<String, BpmnElement>();
	private HashMap<String, BpmnElement> tasksHM = new HashMap<String, BpmnElement>();
	private HashMap<String, BpmnElement> eventsHM = new HashMap<String, BpmnElement>();
	private HashMap<String, BpmnElement> elementsHM = new HashMap<String, BpmnElement>();
	private HashMap<String, BpmnElement> subprocessesHM = new HashMap<String, BpmnElement>();
	private HashMap<String, Pair<String, Pair<String, String>>> edgesPairHM = new HashMap<String, Pair<String, Pair<String, String>>>();
	private HashMap<String, Pair<String, Pair<String, String>>> messagesPairHM = new HashMap<String, Pair<String, Pair<String, String>>>();
	private Document doc;
	private XPath xpath;
	private XMLParser xmlparser;
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
	public HashMap<String, BpmnElement> getAllElements() {
		HashMap<String, BpmnElement> result = new HashMap<String, BpmnElement>();
		for (Object s : this.tasksHM.values()) {
			if (s instanceof Task) {
				if (!(s instanceof UserTask)) {
					Task t = (Task) s;
					result.put(t.getName(), t);
				}
				;
			}
			if (s instanceof UserTask) {
				UserTask t = (UserTask) s;
				result.put(t.getName(), t);
			}
		}
		return result;
	}
	public HashSet<ParallelGateway> getParallelGateways() {
		return (HashSet<ParallelGateway>) this
				.getGateways(new ParallelGateway());
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
			if (a instanceof InclusiveGateway) {

				if (type instanceof InclusiveGateway)
				{	pg.add((InclusiveGateway) a);
				}
			}
		}
		return (HashSet<ExclusiveGateway>) pg;
	}

	public HashSet<? extends BpmnElement> getEvents(BpmnElement type) {
		Class g = type.getClass();
		@SuppressWarnings("rawtypes")
		HashSet pg = new HashSet();
		for (BpmnElement a : eventsHM.values()) {
			if (a instanceof StartEvent) {
				if (type instanceof StartEvent)
					pg.add((StartEvent) a);
			}
			if (a instanceof EndEvent) {
				if (type instanceof EndEvent)
					pg.add((EndEvent) a);
			}
			if (a instanceof TerminationEvent) {
				if (type instanceof TerminationEvent)
					pg.add((TerminationEvent) a);
			}
		}
		return (HashSet<ExclusiveGateway>) pg;
	}

	public BpmnParser(String pathToFile) {
		this.xmlparser = new XMLParser(pathToFile);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(false);
		DocumentBuilder builder;
		this.doc = null;
		try {
			builder = factory.newDocumentBuilder();
			this.doc = builder.parse(pathToFile);
			XPathFactory xpathFactory = XPathFactory.newInstance();
			this.xpath = xpathFactory.newXPath();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}   
	}

	public List<String> XPathHelper(String query) {
		List<String> list = new ArrayList<>();
		try {
			XPathExpression expr =
					this.xpath.compile(query);
			NodeList nodes = (NodeList) expr.evaluate(this.doc, XPathConstants.NODESET);
			for (int i = 0; i < nodes.getLength(); i++)
				list.add(nodes.item(i).getNodeValue());
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return list;
	}

	public int hierachyLaneLookup(String identifier) {
		List<String> resulta;
		List<String> resultb1;
		List<String> resultb2;
		List<String> startEvents;
		List<String> endEvents;
		int result = -1;
		resulta = xmlparser
				.XPathHelper("/definitions/process/laneSet/lane/flowNodeRef[text()='"+identifier+"']");
		if(resulta.size() > 0) {
			result=1;
		} else {
			result=0;
		}
		return result;		
	}

	public int hierachySubprocessLookup(String identifier) {
		List<String> resulta;
		List<String> resultb1;
		List<String> resultb2;
		List<String> startEvents;
		List<String> endEvents;
		int result = -1;
		resulta = xmlparser
				.XPathHelper("/definitions/process/subProcess//endEvent[@id='"+identifier+"']");
		if(resulta.size() > 0) {

			result=1;
		} else {
			result=0;

		}
		return result;		
	}

	public void generate() {
		List<String> resulta;
		List<String> resultb1;
		List<String> resultb2;
		List<String> startEvents;
		List<String> endEvents;	

		// @DOC MessageFlow
		resulta = xmlparser
				.XPathHelper("//definitions/collaboration/messageFlow/@id");
		if (resulta.size() > 0) {
			for (String g : resulta) {
				resultb1 = xmlparser
						.XPathHelper("//definitions/collaboration/messageFlow[@id='"
								+ g.toString() + "']/@sourceRef");
				resultb2 = xmlparser
						.XPathHelper("//definitions/collaboration/messageFlow[@id='"
								+ g.toString() + "']/@targetRef");
				MessageFlow ex = new MessageFlow(g.toString(), new HashSet<String>(
						resultb1), new HashSet<String>(resultb2));
				messagesHM.put(g.toString(), ex);
			}
		}

		// @DOC Tasks
		resulta = xmlparser.XPathHelper("//definitions/process//task/@id");
		if (resulta.size() > 0) {
			String  x="";
			for (String g : resulta) {
				resultb1 = xmlparser
						.XPathHelper("//definitions/process//task[@id='"
								+ g.toString() + "']/incoming/text()");
				resultb2 = xmlparser
						.XPathHelper("//definitions/process//task[@id='"
								+ g.toString() + "']/outgoing/text()");
				HashSet<String> k = new HashSet<String>();
				HashSet<String> j2 =null;
				HashSet<String> j =				this.aaa(g.toString());
				Task ex = new Task(g.toString(), new HashSet<String>(resultb1),
						new HashSet<String>(resultb2), j2, j);	
				tasksHM.put(g.toString(), ex);
			}
		}

		// @DOC ReceiveTask treated as Task
		resulta = xmlparser.XPathHelper("//definitions/process//receiveTask/@id");
		if (resulta.size() > 0) {
			String  x="";
			for (String g : resulta) {
				resultb1 = xmlparser
						.XPathHelper("//definitions/process//receiveTask[@id='"
								+ g.toString() + "']/incoming/text()");
				resultb2 = xmlparser
						.XPathHelper("//definitions/process//receiveTask[@id='"
								+ g.toString() + "']/outgoing/text()");
				HashSet<String> k = new HashSet<String>();
				HashSet<String> j2 =null;
				HashSet<String> j =				this.aaa(g.toString());
				Task ex = new Task(g.toString(), new HashSet<String>(resultb1),
						new HashSet<String>(resultb2), j2, j);	
				tasksHM.put(g.toString(), ex);
			}
		}

		// @DOC ServiceTask treated as Task
		resulta = xmlparser.XPathHelper("//definitions/process//serviceTask/@id");
		if (resulta.size() > 0) {
			String  x="";
			for (String g : resulta) {
				resultb1 = xmlparser
						.XPathHelper("//definitions/process//serviceTask[@id='"
								+ g.toString() + "']/incoming/text()");
				resultb2 = xmlparser
						.XPathHelper("//definitions/process//serviceTask[@id='"
								+ g.toString() + "']/outgoing/text()");
				HashSet<String> k = new HashSet<String>();
				HashSet<String> j2 =null;
				HashSet<String> j =				this.aaa(g.toString());
				Task ex = new Task(g.toString(), new HashSet<String>(resultb1),
						new HashSet<String>(resultb2), j2, j);	
				tasksHM.put(g.toString(), ex);
			}
		}

		// @DOC Exclusive Gateway
		resulta = xmlparser
				.XPathHelper("//definitions/process//exclusiveGateway/@id");
		if (resulta.size() > 0) {
			for (String g : resulta) {
				resultb1 = xmlparser
						.XPathHelper("//definitions/process//exclusiveGateway[@id='"
								+ g.toString() + "']/incoming/text()");
				resultb2 = xmlparser
						.XPathHelper("//definitions/process//exclusiveGateway[@id='"
								+ g.toString() + "']/outgoing/text()");
				ExclusiveGateway ex = new ExclusiveGateway(g.toString(),
						new HashSet<String>(resultb1), new HashSet<String>(
								resultb2));
				elementsHM.put(g.toString(), ex);
			}
		}

		// @DOC Inclusive Gateway
		resulta = xmlparser
				.XPathHelper("//definitions/process//inclusiveGateway/@id");
		if (resulta.size() > 0) {

			for (String g : resulta) {
				resultb1 = xmlparser
						.XPathHelper("//definitions/process//inclusiveGateway[@id='"
								+ g.toString() + "']/incoming/text()");
				resultb2 = xmlparser
						.XPathHelper("//definitions/process//inclusiveGateway[@id='"
								+ g.toString() + "']/outgoing/text()");
				InclusiveGateway ex = new InclusiveGateway(g.toString(),
						new HashSet<String>(resultb1), new HashSet<String>(
								resultb2));
				elementsHM.put(g.toString(), ex);
				System.out.println("!!!!");
			}
		}

		// @DOC ParallelGateway
		resulta = xmlparser
				.XPathHelper("//definitions/process/parallelGateway/@id");
		if (resulta.size() > 0) {
			for (String g : resulta) {
				resultb1 = xmlparser
						.XPathHelper("//definitions/process/parallelGateway[@id='"
								+ g.toString() + "']/incoming/text()");
				resultb2 = xmlparser
						.XPathHelper("//definitions/process/parallelGateway[@id='"
								+ g.toString() + "']/outgoing/text()");
				ParallelGateway ex = new ParallelGateway(g.toString(),
						new HashSet<String>(resultb1), new HashSet<String>(
								resultb2));
				elementsHM.put(g.toString(), ex);
			}
		}

		// @DOC UserTask treated as Task
		resulta = xmlparser.XPathHelper("//definitions/process/userTask/@id");
		if (resulta.size() > 0) {
			for (String g : resulta) {
				//System.out.println("> " + g.toString());
				resultb1 = xmlparser
						.XPathHelper("//definitions/process/userTask[@id='"
								+ g.toString() + "']/incoming/text()");
				resultb2 = xmlparser
						.XPathHelper("//definitions/process/userTask[@id='"
								+ g.toString() + "']/outgoing/text()");
				Task ex = new Task(g.toString(), new HashSet<String>(resultb1),
						new HashSet<String>(resultb2), null, null);
				tasksHM.put(g.toString(), ex);
			}
		}

		// @DOC Sequence Flow
		resulta = xmlparser
				.XPathHelper("//definitions/process//sequenceFlow/@id");
		if (resulta.size() > 0) {
			for (String g : resulta) {
				//System.out.println(">>>> " + g.toString());
				resultb1 = xmlparser
						.XPathHelper("//definitions/process//sequenceFlow[@id='"
								+ g.toString() + "']/@sourceRef");
				resultb2 = xmlparser
						.XPathHelper("//definitions/process//sequenceFlow[@id='"
								+ g.toString() + "']/@targetRef");
				SequenceFlow ex = new SequenceFlow(g.toString(), new HashSet<String>(
						resultb1), new HashSet<String>(resultb2));
				edgesHM.put(g.toString(), ex);
			}
		}

		// @DOC Start Event
		resulta = xmlparser.XPathHelper("//definitions/process//startEvent/@id");
		if (resulta.size() > 0) {
			for (String g : resulta) {
				//System.out.println("> " + g.toString());
				resultb1 = xmlparser
						.XPathHelper("//definitions/process//startEvent[@id='"
								+ g.toString() + "']/incoming/text()");
				resultb2 = xmlparser
						.XPathHelper("//definitions/process//startEvent[@id='"
								+ g.toString() + "']/outgoing/text()");
				StartEvent ex = new StartEvent(g.toString(),
						new HashSet<String>(resultb1), new HashSet<String>(
								resultb2));
				eventsHM.put(g.toString(), ex);
			}
		}

		// @DOC End Event
		resulta = xmlparser.XPathHelper("//definitions/process//endEvent/@id");
		if (resulta.size() > 0) {
			for (String g : resulta) {
				//System.out.println("> " + g.toString());
				resultb1 = xmlparser
						.XPathHelper("//definitions/process//endEvent[@id='"
								+ g.toString() + "']/incoming/text()");
				resultb2 = xmlparser
						.XPathHelper("//definitions/process//endEvent[@id='"
								+ g.toString() + "']/outgoing/text()");
				TerminationEvent ex = new TerminationEvent(g.toString(),
						new HashSet<String>(resultb1), new HashSet<String>(
								resultb2));
				eventsHM.put(g.toString(), ex);

			}
		}

		// @DOC Sub-Process
		resulta = xmlparser.XPathHelper("//definitions/process//subProcess/@id");
		if (resulta.size() > 0) {
			for (String g : resulta) {
				//System.out.println("> " + g.toString());
				resultb1 = xmlparser
						.XPathHelper("//definitions/process//subProcess[@id='"
								+ g.toString() + "']/incoming/text()");
				resultb2 = xmlparser
						.XPathHelper("//definitions/process//subProcess[@id='"
								+ g.toString() + "']/outgoing/text()");
				HashSet<String> startEventsHashSet= new HashSet<String>();
				startEvents = xmlparser
						.XPathHelper("//definitions/process//subProcess[@id='"
								+ g.toString() + "']/startEvent/@id");
				if (startEvents.size() > 0) {
					for (String h : startEvents) {
						startEventsHashSet.add(h.toString());
					}
				}
				HashSet<String> endEventsHashSet= new HashSet<String>();
				endEvents = xmlparser
						.XPathHelper("//definitions/process//subProcess[@id='"
								+ g.toString() + "']/endEvent/@id");
				if (endEvents.size() > 0) {
					for (String h : endEvents) {
						endEventsHashSet.add(h.toString());
					}
				}
				Subprocess ex = new Subprocess(g.toString(),
						new HashSet<String>(resultb1), new HashSet<String>(
								resultb2), null, null, null, null, startEventsHashSet, endEventsHashSet);
				subprocessesHM.put(g.toString(), ex);
			}
		}

		// @DOC create edgesPairHM from edgesHM
		for (BpmnElement s : edgesHM.values()) {
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
		// @DOC create messagesPairHM from messagesHM
		for (BpmnElement s : messagesHM.values()) {
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
			messagesPairHM.put(s.getName().toString(), k);
		}
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
	public HashSet<String> aaa(String identifier) {
		HashSet<String> res=new HashSet<String>();
		String h = null;
		HashMap<String, Pair<String, Pair<String, String>>> j = new HashMap<String, Pair<String, Pair<String, String>>>();
		for(String g : messagesHM.keySet()) {
			if(messagesHM.get(g).getInBound().contains(identifier)) {
				h=g;
				res.add(g);
			}
		}
		return res;
	}
	public Boolean bbb(String identifier) {
		HashSet<String> res=new HashSet<String>();
		Boolean h = false;
		HashMap<String, Pair<String, Pair<String, String>>> j = new HashMap<String, Pair<String, Pair<String, String>>>();
		if(messagesHM.containsKey(identifier)) h=true;
		return h;
	}

	public BpmnElement searchGeneralTasksTest(String name) {
		BpmnElement b = null;
		for (Object s : this.subprocessesHM.values()) {	
			Subprocess kk = (Subprocess) s;
		}
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
		for (String s : this.messagesHM.keySet()) {
			if(s.equals(name)) {
				MessageFlow h = (MessageFlow) this.messagesHM.get(s);
				for(String hhh : h.getOutBound()) {					
					BpmnElement bp = this.findElementById(hhh);
					if(bp instanceof StartEvent) {
						b= (BpmnElement) bp;
					} else
						if(bp instanceof Task) {
							b= (BpmnElement) bp;
						}
					break;	
				}	
			}			
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
			if (s instanceof InclusiveGateway) {
				if (((InclusiveGateway) s).getInBound().contains(name))
					b = (BpmnElement) s;
			}
			;
		}
		for (Object s : this.subprocessesHM.values()) {
			if (((Subprocess) s).getInBound().contains(name)) {
				b = (BpmnElement) s;
				List<String> lls = getDelegationInLinks(((Subprocess)s).getName());				
				for(String uio : lls) {
					String name2=uio; // 54
					for (Object s2 : this.eventsHM.values()) {
						if (s2 instanceof StartEvent) {
							if (((StartEvent) s2).getName().equals(name2)) {
								b = (BpmnElement) s2;
							}
						}
					}
				}
			}
		}
		return b;
	}
	public String findStartEventWithinSubprocess(String name) {

		return name;
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
			if (s instanceof TerminationEvent) {
				TerminationEvent t = (TerminationEvent) s;
				if (((TerminationEvent) s).getName().equals(name))
					result = t;
				;
			}
		}
		for (Object s : this.elementsHM.values()) {
			if (s instanceof ExclusiveGateway) {
				ExclusiveGateway t = (ExclusiveGateway) s;
				if (((ExclusiveGateway) s).getName().equals(name))
					result = t;
				;
			}
			if (s instanceof InclusiveGateway) {
				InclusiveGateway t = (InclusiveGateway) s;
				if (((InclusiveGateway) s).getName().equals(name))
					result = t;
				;
			}
			if (s instanceof ParallelGateway) {
				ParallelGateway t = (ParallelGateway) s;
				if (((ParallelGateway) s).getName().equals(name))
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
	public BpmnElement findElementById(String name) {
		return this.searchGeneralTasks(name);

	}
	public BpmnElement findElementById2(String name) {
		return this.searchGeneralTasks(name);

	}
	public BpmnElement findElementByIdTest(String name) {

		BpmnElement r = null;
		if (this.searchGeneralTasksTest(name) != null) {
			r = this.searchGeneralTasksTest(name); // for messages checky 2
		} else {
			r = this.searchGeneralTasksTest2(name);
		}
		return r;
	}
	public Pair getEdge(String name) {
		Pair p = edgesPairHM.get(name);
		if(p==null)
			p = messagesPairHM.get(name);
		return p;
	}
	public HashMap<String, Pair<String, Pair<String, String>>> getE(String identifier) {
		HashMap<String, Pair<String, Pair<String, String>>> j = new HashMap<String, Pair<String, Pair<String, String>>>();
		for(String g : messagesPairHM.keySet()) {

			if(messagesPairHM.get(g).getFirst().equals(identifier)) {
				j.put(g, messagesPairHM.get(g));
			}

		}
		return j; 
	}

	public String getDataStore(String id) {
		String r=null;
		List<String> result = null;
		List<String> resultb1 = null;
		List<String> resultb2 = null;
		String sResult=null;
		result = xmlparser.XPathHelper("//definitions/process//dataStoreReference[@id='"+id+"']/@dataStoreRef");
		if(result.size()==1) r=result.get(0);
		return r;			
	}

	public HashSet<Pair> getDataStoreReferences() {
		List<String> resulta;
		List<String> resultb1 = null;
		List<String> resultb2 = null;
		HashSet<Pair> hsPair = new HashSet<Pair>();
		resulta = xmlparser.XPathHelper("//definitions/process//dataStoreReference/@id");
		if (resulta.size() > 0) {
			String  x="";
			for (String g : resulta) {
				resultb1 = xmlparser
						.XPathHelper("//definitions/process//association[@sourceRef='"
								+ g.toString() + "']/@targetRef");
				System.out.println("===="+resultb1.size());
				Pair p = new Pair(g.toString(), resultb1);
				hsPair.add(p);				
			}
		}
		return hsPair;	
	}
	public Pair getMessage(String name) {
		Pair p = messagesPairHM.get(name);
		return p;
	}
	public HashMap<String, Pair<String, Pair<String, String>>> getMessages() {
		HashMap<String, Pair<String, Pair<String, String>>> p = messagesPairHM;
		return p;
	}
	public HashSet<StartEvent> getStartEvents() {
		return (HashSet<StartEvent>) this
				.getEvents(new StartEvent());
	}
	public HashSet<TerminationEvent> getEndEvents() {
		return (HashSet<TerminationEvent>) this
				.getEvents(new TerminationEvent());
	}

	public HashSet<TerminationEvent> getTerminationEvents() {
		return (HashSet<TerminationEvent>) this
				.getEvents(new TerminationEvent());
	}	
	public Boolean findElementByIdTestContains(String name) {
		Boolean result = true;
		if (this.searchGeneralTasksTest(name) == null)
			result = false;
		if (this.searchGeneralTasksTest2(name) instanceof BpmnElement)
			result = true;
		return result;
	}
	public void mergePlaces(String name, String name2, IFNet ifnet, String h) {
	}
	public HashSet<Subprocess> getSubprocesses() {
		HashSet pg = new HashSet();
		for (BpmnElement a : subprocessesHM.values()) {
			pg.add((Subprocess) a);
		}
		return (HashSet<Subprocess>) pg;
	}
	public List<String> getDelegationOutLinks(String string) {
		List<String> resulta;
		List<String> resultb1;
		List<String> resultb2;
		resultb1 = xmlparser
				.XPathHelper("//definitions/process//subProcess[endEvent[@id='"+string+"']]/outgoing/text()");
		return resultb1;
	}
	public List<String> getDelegationInLinks(String string) {
		List<String> resulta;
		List<String> resultb1;
		List<String> resultb2;
		resultb1 = xmlparser
				.XPathHelper("//definitions/process//subProcess[@id='"+string+"']/startEvent/@id");
		return resultb1;
	}
	public HashSet<InclusiveGateway> getInclusiveGateways() {
		return (HashSet<InclusiveGateway>) this
				.getGateways(new InclusiveGateway());
	}
}    