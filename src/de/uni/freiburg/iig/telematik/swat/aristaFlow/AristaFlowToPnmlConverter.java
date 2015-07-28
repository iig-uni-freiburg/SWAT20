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

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalIFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetTransition;
import de.uni.freiburg.iig.telematik.swat.aristaFlow.AristaFlowElement.PTequivalent;
import de.uni.freiburg.iig.telematik.wolfgang.actions.graphpopup.LayoutAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.IFNetEditorComponent;
import java.time.Clock;
import jdk.nashorn.internal.runtime.regexp.RegExp;
import sun.misc.Regexp;

public class AristaFlowToPnmlConverter {

    private File aristaFlowTemplate;
    private IFNet net;
    private Map<String, AristaFlowElement> elements = new HashMap<String, AristaFlowElement>();

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        AristaFlowToPnmlConverter converter = new AristaFlowToPnmlConverter(new File("/tmp/af.template"));
        converter.parse();
        GraphicalIFNet ifnet = new GraphicalIFNet(new IFNet());
        ifnet.setPetriNet(converter.getNet());
        IFNetEditorComponent editor = new IFNetEditorComponent(ifnet);
        //ifnet, new File("/tmp/test.pnml")
        JFrame panel = new JFrame();
        panel.setSize(300, 300);
        panel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.add(editor);
        panel.setVisible(true);
        LayoutAction la = new LayoutAction(editor, "horizontalHierarchical", false);
        la.actionPerformed(new ActionEvent(converter, 0, "command"));
    }

    public boolean containsActivity(String activityName) {
        for (Map.Entry<String, AristaFlowElement> element : elements.entrySet()) {
            if (element.getValue().displayName.equalsIgnoreCase(activityName)) {
                return true;
            }
        }
        return false;
    }
    
    public String getOriginator(String activityName){
        for (Map.Entry<String, AristaFlowElement> element : elements.entrySet()) {
            if (element.getValue().displayName.equalsIgnoreCase(activityName)) {
                return element.getValue().getOriginator();
            }
        }
        return "";
    }
    
    public boolean bySamePerson(String activityName1, String activityName2) throws NullPointerException{
        AristaFlowElement element1=null;
        AristaFlowElement element2=null;
        for (Map.Entry<String, AristaFlowElement> element : elements.entrySet()) {
            if (element.getValue().displayName.equalsIgnoreCase(activityName1)) {
                element1=element.getValue();
            }
            if (element.getValue().displayName.equalsIgnoreCase(activityName2)) {
                element2=element.getValue();
            }
        }
        if(element1!=null && element2!=null){
            return element1.getOriginator().equalsIgnoreCase(element2.getOriginator());
    }
        return false;
        
    }
    
    public void printAllEntries(){
        System.out.println("Entries in PNML-Converter:");
        for(Map.Entry<String, AristaFlowElement> keys:elements.entrySet()){
            System.out.println(keys.getKey()+" ("+keys.getValue().displayName+", "+keys.getValue().internalName+"), Originator: "+keys.getValue().getOriginator());
        }
    }

    private void loadOriginators(Document doc) {
        //Originator is located under <nodes><node><staffAssignmentRule>
        NodeList nodes = doc.getElementsByTagName("node");
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String id = element.getAttribute("id");
                String originator = element.getElementsByTagName("staffAssignmentRule").item(0).getTextContent();
                //format this string
                try{
                originator=originator.split("\\'")[1];
                elements.get(id).setOriginator(originator);
                }
                catch(IndexOutOfBoundsException ex){
                    //no valid field, ignore
                }
            }
        }
    }

    public AristaFlowToPnmlConverter(File file) {
        aristaFlowTemplate = file;
        this.net = new IFNet();
    }

    public IFNet getNet() {
        return net;
    }
    
    public void parseWithoutIfnet() throws ParserConfigurationException, SAXException, IOException{
        Document doc = getDOM();
        insertElements(doc);
        insertTransitionsFromEdges(doc);
        //aristaFlowElementsToIFnet();
        loadNames(doc);
        //setInitialMarking();
        loadOriginators(doc);
    }

    public IFNet parse() throws ParserConfigurationException, SAXException, IOException {
        Document doc = getDOM();
        insertElements(doc);
        //insertTransition(doc);
        insertTransitionsFromEdges(doc);
        //processSplitAndJoinNodes(doc);

        aristaFlowElementsToIFnet();

        loadNames(doc);

        setInitialMarking();
        
        loadOriginators(doc);

        return net;
    }

    private void setInitialMarking() {
        IFNetMarking marking = new IFNetMarking();
        Multiset<String> initialSet = new Multiset<String>();
        initialSet.add("black");
        marking.set("n0", initialSet);
        net.setInitialMarking(marking);
    }

    public AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> getGraphicalPN() {
        GraphicalIFNet net = new GraphicalIFNet(new IFNet());
        net.setPetriNet(getNet());
        return net;
    }

    /**
     * get all Names from AristaFlow Template and put inside IFnet*
     */
    private void loadNames(Document doc) {
        Map<String, String> names = new HashMap<String, String>();
        NodeList nodes = doc.getElementsByTagName("node");
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                names.put(element.getAttribute("id"), getValue("name", element));
                elements.get(element.getAttribute("id")).setDisplayName(getValue("name", element));
            }
        }

        //store names for IFnet
        for (AbstractIFNetTransition transition : net.getTransitions()) {
            //System.out.println("Setting name: " + transition.getLabel() + " to: " + names.get(transition.getLabel()));
            if (names.get(transition.getLabel()) != null) {
                transition.setLabel(names.get(transition.getLabel()));
            }
        }

    }

    /**
     * traverse all Elements and put into PTModel *
     */
    private void insertElements(Document doc) {
        NodeList nodes = doc.getElementsByTagName("structuralNodeData");
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
				//				System.out.println("Node name: " + element.getNodeName() + " Type: " + getValue("type", element) + " ID: "
                //						+ element.getAttribute("nodeID"));
                switch (ArisaFlowType.valueOf(getValue("type", element))) {
                    case NT_ENDFLOW:
                        //net.addPlace(element.getAttribute("nodeID"), element.getAttribute("nodeID"));
                        elements.put(element.getAttribute("nodeID"), new AristaFlowElement(element.getAttribute("nodeID"),
                                AristaFlowElement.PTequivalent.PLACE));
                        break;
                    case NT_STARTFLOW:
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
                } else if (sourceType.equals(PTequivalent.TRANSITION) && currentElemType.equals(PTequivalent.TRANSITION)) {
                    //add additional place
                    String placeName = source + currentElement;
                    elements.put(placeName, new AristaFlowElement(placeName, PTequivalent.PLACE));
                    elements.get(placeName).addIncomingRelation(source);
                    elements.get(placeName).addOutgoingRelation(currentElement);
                } else {

                    //test for transition and place
                    elements.get(currentElement).addIncomingRelation(source);
                    elements.get(source).addOutgoingRelation(currentElement);
                }
            }

        }

    }

    private void aristaFlowElementsToIFnet() {
        net = new IFNet();
        for (AristaFlowElement element : elements.values()) {
            addAristaFlowElementToNet(element);
        }
        //make transitions
        for (AristaFlowElement element : elements.values()) {
            try {
                addAristaFlowElementTransitions(element);
            } catch (ParameterException e) {
                //Workbench.errorMessage("Problem generating IFnet with element " + element.displayName, e, false);
            }
        }

    }

    private void addAristaFlowElementToNet(AristaFlowElement element) {
        switch (element.type) {
            case PLACE:
                net.addPlace(element.internalName);
                //net.getPlace(element.internalName).setCapacity(1);
                net.getPlace(element.internalName).setColorCapacity("black", 1);
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
