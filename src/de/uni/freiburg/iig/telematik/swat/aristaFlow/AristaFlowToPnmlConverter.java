package de.uni.freiburg.iig.telematik.swat.aristaFlow;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTTransition;
import de.uni.freiburg.iig.telematik.swat.aristaFlow.AristaFlowElement.PTequivalent;
import de.uni.freiburg.iig.telematik.swat.editor.PTNetEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.graphpopup.LayoutAction;

public class AristaFlowToPnmlConverter {

	private File aristaFlowTemplate;
	private PTNet net;
	private Element startNode;
	private Map<String, AristaFlowElement> elements = new HashMap<String, AristaFlowElement>();
	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		AristaFlowToPnmlConverter converter = new AristaFlowToPnmlConverter(new File("/tmp/af.template"));
		converter.parse();
		GraphicalPTNet ifnet = new GraphicalPTNet();
		ifnet.setPetriNet(converter.getNet());
		PTNetEditor editor = new PTNetEditor(ifnet, new File("/tmp/test.pnml"));
		JFrame panel = new JFrame();
		panel.setSize(300, 300);
		panel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel.add(editor);
		panel.setVisible(true);
		LayoutAction la = new LayoutAction(editor, "horizontalHierarchical", false);
		la.actionPerformed(new ActionEvent(converter, 0, "command"));
		
		
	}

	public AristaFlowToPnmlConverter(File file) {
		aristaFlowTemplate = file;
		this.net = new PTNet();
	}

	public PTNet getNet() {
		return net;
	}

	public PTNet parse() throws ParserConfigurationException, SAXException, IOException {
		Document doc = getDOM();
		insertElements(doc);
		//insertTransition(doc);
		insertTransitionsFromEdges(doc);
		//processSplitAndJoinNodes(doc);

		aristaFlowElementsToIFnet();

		enrichNames(doc);

		return net;

	}

	public PTNetEditor getEditor(File file) {
		GraphicalPTNet ifnet = new GraphicalPTNet();
		ifnet.setPetriNet(getNet());
		PTNetEditor editor = new PTNetEditor(ifnet, file);
		LayoutAction la = new LayoutAction(editor, "horizontalHierarchical", false);
		la.actionPerformed(new ActionEvent(this, 0, "command"));
		return editor;
	}

	/** get all Names from AristaFlow Template **/
	private void enrichNames(Document doc) {
		Map<String, String> names = new HashMap<String, String>();
		NodeList nodes = doc.getElementsByTagName("node");
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if(node.getNodeType()==Node.ELEMENT_NODE){
				Element element = (Element)node;
				names.put(element.getAttribute("id"), getValue("name", element));
			}
		}
		
		//store names into IFnet
		for (PTTransition transition : net.getTransitions()) {
			System.out.println("Setting name: " + transition.getLabel() + " to: " + names.get(transition.getLabel()));
			if (names.get(transition.getLabel()) != null)
			transition.setLabel(names.get(transition.getLabel()));
		}
		
	}

	/** traverse all Elements and put into PTModel **/
	private void insertElements(Document doc) {
		NodeList nodes = doc.getElementsByTagName("structuralNodeData");
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if(node.getNodeType()==Node.ELEMENT_NODE){
				Element element = (Element)node;
				System.out.println("Node name: " + element.getNodeName() + " Type: " + getValue("type", element) + " ID: "
						+ element.getAttribute("nodeID"));
				switch (ArisaFlowType.valueOf(getValue("type", element))) {
				case NT_ENDFLOW:
					//net.addPlace(element.getAttribute("nodeID"), element.getAttribute("nodeID"));
					elements.put(element.getAttribute("nodeID"), new AristaFlowElement(element.getAttribute("nodeID"),
							AristaFlowElement.PTequivalent.PLACE));
					break;
				case NT_STARTFLOW:
					startNode = element;
					//net.addPlace(element.getAttribute("nodeID"), element.getAttribute("nodeID"));
					elements.put(element.getAttribute("nodeID"), new AristaFlowElement(element.getAttribute("nodeID"),
							AristaFlowElement.PTequivalent.PLACE));
					break;
				case NT_XOR_SPLIT:
				case NT_XOR_JOIN:
				case NT_AND_JOIN:
				case NT_AND_SPLIT:
				case NT_ENDLOOP:
					//net.addPlace(element.getAttribute("nodeID"), element.getAttribute("nodeID"));
					elements.put(element.getAttribute("nodeID"), new AristaFlowElement(element.getAttribute("nodeID"),
							AristaFlowElement.PTequivalent.PLACE));
					break;
				case NT_NORMAL:
					//net.addTransition(element.getAttribute("nodeID"), element.getAttribute("nodeID"));
					elements.put(element.getAttribute("nodeID"), new AristaFlowElement(element.getAttribute("nodeID"),
							AristaFlowElement.PTequivalent.TRANSITION));
					break;
				default:
					break;
				}
			}

		}
	}
	
	private void insertTransitionsFromEdges(Document doc) {
		NodeList nodes = doc.getElementsByTagName("edge");
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				String currentElement = element.getAttribute("destinationNodeID");
				PTequivalent currentElemType = elements.get(currentElement).type;
				String source = element.getAttribute("sourceNodeID");
				PTequivalent sourceType = elements.get(source).type;

				if (sourceType.equals(PTequivalent.PLACE) && currentElemType.equals(PTequivalent.PLACE)) {
					//add Aditional Transition
					String transitionName = source + currentElement;
					elements.put(transitionName, new AristaFlowElement(transitionName, PTequivalent.TRANSITION));
					elements.get(currentElement).addIncomingRelation(transitionName);
					elements.get(source).addOutgoingRelation(transitionName);
					//elements.get(transitionName).addOutgoingRelation(source);
					//elements.get(transitionName).addIncomingRelation(currentElement);
				}

				else if (sourceType.equals(PTequivalent.TRANSITION) && currentElemType.equals(PTequivalent.TRANSITION)) {
					//add additional place
					String placeName = source + currentElement;
					elements.put(placeName, new AristaFlowElement(placeName, PTequivalent.PLACE));
					elements.get(placeName).addIncomingRelation(source);
					elements.get(placeName).addOutgoingRelation(currentElement);
				}
				else {

				//test for transition and place
				elements.get(currentElement).addIncomingRelation(source);
					elements.get(source).addOutgoingRelation(currentElement);
				}
			}

		}
		
		//		nodes = doc.getElementsByTagName("structuralNodeData");
		//		Element lastNode = this.startNode;
		//		for (int i = 0; i < nodes.getLength(); i++){
		//			Node node = nodes.item(i);
		//			if (node.getNodeType() == Node.ELEMENT_NODE) {
		//				Element element = (Element) node;
		//				ArisaFlowType type = ArisaFlowType.valueOf(getValue("type", element));
		//				switch (type) {
		//				case NT_NORMAL:
		//					if (getValue("type", lastNode).equals(ArisaFlowType.NT_NORMAL)) {
		//						//insert additional place
		//						String newPlaceName = lastNode.getAttribute("nodeID") + element.getAttribute("nodeID");
		//						elements.put(newPlaceName, new AristaFlowElement(newPlaceName, AristaFlowElement.PTequivalent.PLACE));
		//						elements.get(newPlaceName).addIncomingRelation(lastNode.getAttribute("nodeID"));
		//						elements.get(newPlaceName).addOutgoingRelation(element.getAttribute("nodeID"));
		//						elements.get(lastNode.getAttribute("nodeID")).outgoingLinks.add(newPlaceName);
		//						elements.get(element.getAttribute("nodeID")).incomingLinks.add(newPlaceName);
		//					}
		//						break;
		//
		//				default:
		//					break;
		//				}
		//			}
		//		}
	}

	/** TODO: rename in insert needed Places and change function accordingly **/
	private void insertTransition(Document doc) {
		NodeList nodes = doc.getElementsByTagName("structuralNodeData");
		Element lastNode = this.startNode;
		String connectsTo;
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				String currNode = element.getAttribute("nodeID");
				System.out.println("Node name: " + element.getNodeName() + " Type: " + getValue("type", element) + " ID: "
						+ element.getAttribute("nodeID"));

				switch (ArisaFlowType.valueOf(getValue("type", element))) {
				case NT_STARTFLOW:
				
				case NT_XOR_JOIN:
				case NT_AND_JOIN:
					connectsTo = getValue("correspondingBlockNodeID", element);
					elements.get(currNode).addIncomingRelation(connectsTo);
				case NT_XOR_SPLIT:
				case NT_AND_SPLIT:
					connectsTo = getValue("correspondingBlockNodeID", element);
					elements.get(currNode).addOutgoingRelation(connectsTo);
					elements.get(lastNode.getAttribute("nodeID")).addOutgoingRelation(currNode);
					break;

				case NT_ENDLOOP:
					break;
				case NT_NORMAL:
					String currentElement = element.getAttribute("nodeID");
					if(getValue("type", lastNode).equals(ArisaFlowType.NT_STARTFLOW.toString())){
						//Connect with startnode
						System.out.println("Adding " + lastNode.getAttribute("nodeID") + " to " + element.getAttribute("nodeID"));
						//net.addFlowRelationPT(lastNode.getAttribute("nodeID"), element.getAttribute("nodeID"));

						elements.get(currentElement).addIncomingRelation(lastNode.getAttribute("nodeID"));
					} else if (getValue("type", lastNode).equals(ArisaFlowType.NT_NORMAL.toString())) {
						//connect transition with transition
						String newPlaceName = lastNode.getAttribute("nodeID") + element.getAttribute("nodeID");
						//net.addPlace(lastNode.getAttribute("nodeID") + element.getAttribute("nodeID"));
						elements.put(newPlaceName, new AristaFlowElement(newPlaceName, AristaFlowElement.PTequivalent.PLACE));
						//						net.addFlowRelationPT(lastNode.getAttribute("nodeID") + element.getAttribute("nodeID"),
						//								element.getAttribute("nodeID"));
						elements.get(newPlaceName).addIncomingRelation(lastNode.getAttribute("nodeID"));
						elements.get(newPlaceName).addOutgoingRelation(element.getAttribute("nodeID"));
						elements.get(lastNode.getAttribute("nodeID")).outgoingLinks.add(newPlaceName);
						//						net.addFlowRelationTP(lastNode.getAttribute("nodeID"),
						//								lastNode.getAttribute("nodeID") + element.getAttribute("nodeID"));
						elements.get(element.getAttribute("nodeID")).incomingLinks.add(newPlaceName);
					} else {
						//transition follows on Join node within xml file
						elements.get(currentElement).addIncomingRelation(lastNode.getAttribute("splitNodeID"));
					}
					lastNode = element;
					break;


				default:
					break;
				}
			}


		}
	}

	private void processSplitAndJoinNodes(Document doc) {
		NodeList nodes = doc.getElementsByTagName("structuralNodeData");
		//Element lastNode = this.startNode;
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				String currNode = element.getAttribute("nodeID");
				System.out.println("Node name: " + element.getNodeName() + " Type: " + getValue("type", element) + " ID: "
						+ element.getAttribute("nodeID"));

				switch (ArisaFlowType.valueOf(getValue("type", element))) {
				case NT_STARTFLOW:
					break;
				case NT_XOR_SPLIT:
				case NT_XOR_JOIN:
				case NT_ENDLOOP:
					break;
				case NT_NORMAL:
					String splitNode = getValue("splitNodeID", element);
					//test if it contains a splitNode different to n0
					if (!splitNode.contains("n0")) {
						//process this node
						elements.get(currNode).addIncomingRelation(splitNode);
						elements.get(splitNode).addOutgoingRelation(currNode);
					}

					break;
				default:
					break;
				}
			}

		}
	}

	private void aristaFlowElementsToIFnet() {
		net = new PTNet();
		for (AristaFlowElement element : elements.values()) {
			addAristaFlowElementToNet(element);
		}
		//make transitions
		for (AristaFlowElement element : elements.values()) {
			try {
				addAristaFlowElementTransitions(element);
			} catch (ParameterException e) {
			}
		}
		
	}

	private void addAristaFlowElementToNet(AristaFlowElement element) {
		switch (element.type) {
		case PLACE:
			net.addPlace(element.internalName);
			break;
		case TRANSITION:
			net.addTransition(element.internalName);
			break;
		default:
			break;
		}
	}

	private void addAristaFlowElementTransitions(AristaFlowElement element) {
		switch (element.type) {
		case PLACE:
			for (String incoming : element.incomingLinks) {
				net.addFlowRelationTP(incoming, element.internalName);
			}
			for (String outcoming : element.outgoingLinks) {
				net.addFlowRelationPT(element.internalName, outcoming);
			}
		case TRANSITION:
			for (String incoming : element.incomingLinks) {
				net.addFlowRelationPT(incoming, element.internalName);
			}
			for (String outcoming : element.outgoingLinks) {
				net.addFlowRelationTP(element.internalName, outcoming);
			}
			break;
		default:
			break;
		}
	}

	private Document getDOM() throws ParserConfigurationException, SAXException, IOException {
		 DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		 DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		 Document doc = dBuilder.parse(aristaFlowTemplate);
		 doc.getDocumentElement().normalize();
		return doc;
	}

	private static String getValue(String tag, Element element) {
		NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
		Node node = (Node) nodes.item(0);
		return node.getNodeValue();
	}



	public enum ArisaFlowType {
		NT_STARTFLOW, NT_ENDFLOW, NT_NORMAL, NT_AND_SPLIT, NT_AND_JOIN, NT_XOR_JOIN, NT_XOR_SPLIT, NT_ENDLOOP;
	}
}

