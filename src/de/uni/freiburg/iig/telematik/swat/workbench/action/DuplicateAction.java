package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.SwingUtilities;

import de.invation.code.toval.misc.NamedComponent;
import de.invation.code.toval.misc.wd.ProjectComponentException;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParser;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.logs.LogModel;
import de.uni.freiburg.iig.telematik.swat.workbench.PNNameDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeNode;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatPNContainer;

public class DuplicateAction extends AbstractWorkbenchAction {

	public DuplicateAction(String name) {
		super(name);
		loadAndSetIcon();
	}
	
	public DuplicateAction(){
		super("duplicate entry");
		loadAndSetIcon();
	}

	private static final long serialVersionUID = -5583721064769029821L;

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		SwatTreeNode node = (SwatTreeNode) Workbench.getInstance().getTreeView().getSelectionPath().getLastPathComponent();
		duplicateNode(node);

	}
	
	private void duplicateNode(SwatTreeNode node) throws Exception {
		switch (node.getObjectType()) {
		case ARISTAFLOW_LOG:
		case MXML_LOG:
		case XES_LOG:
			duplicateLog(node);
			break;
		case PETRI_NET:
			duplicateNet(node);
			break;
		default:
			Workbench.errorMessage("Cannot duplicate entry", new Exception("Cannot duplicate entry "+node.getDisplayName()), true);
		}
		
	}

	private void duplicateNet(SwatTreeNode node) throws Exception {
		SwatPNContainer container = SwatComponents.getInstance().getContainerPetriNets();
		File netFile = container.getComponentFile(node.getDisplayName());
		PNMLParser parser = new PNMLParser<>();
		AbstractGraphicalPN newNet = parser.parse(netFile);
		newNet.setName(requestNewName("New name for net", "new name"));
		container.addComponent(newNet, true);
		
//		AbstractGraphicalPN net = (AbstractGraphicalPN) container.getComponent(node.getDisplayName());
//		String name = requestNewName("New name for net", "new name");
//		AbstractGraphicalPN newNet = new GraphicalPTNet(net.getPetriNet().clone(),net.getPetriNetGraphics());

	}

	private void duplicateLog(SwatTreeNode node) throws Exception {
		SwatComponents component = SwatComponents.getInstance();
		LogModel element;
		String name = requestNewName("New name for log", "new name");
		switch (node.getObjectType()) {
		case ARISTAFLOW_LOG:
			element = component.getContainerAristaflowLogs().getComponent(node.getDisplayName()).clone();
			element.setName(name);
			component.getContainerAristaflowLogs().addComponent(element,true);
			break;
		case MXML_LOG:
			element=component.getContainerMXMLLogs().getComponent(node.getDisplayName()).clone();
			element.setName(name);
			component.getContainerMXMLLogs().addComponent(element, true);
		case XES_LOG:
			element=component.getContainerXESLogs().getComponent(node.getDisplayName()).clone();
			element.setName(name);
			component.getContainerXESLogs().addComponent(element, true);
		default:
			break;
		}
		
	}

	private void loadAndSetIcon(){
		try {
			setIcon(IconFactory.getIcon("data"));
		} catch (ParameterException | PropertyException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private String requestNewName(String message, String title) throws Exception {
		return new PNNameDialog(SwingUtilities.getWindowAncestor(Workbench.getInstance()), message, title, false).requestInput();
	}

}
