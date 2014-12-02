package de.uni.freiburg.iig.telematik.swat.workbench;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.ACModel;
import de.uni.freiburg.iig.telematik.swat.logs.LogModel;
import de.uni.freiburg.iig.telematik.swat.logs.SwatLog;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.TimeContext;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.SwatTreePopupMenu;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatComponentsListener;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatStateListener;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatTreeViewListener;

@SuppressWarnings("serial")
public class SwatTreeView extends JTree implements SwatStateListener, SwatComponentsListener {
	
	private static final String PN_HEADING = "Petri nets";
	private static final String XES_LOGS_HEADING = "XES Logs";
	private static final String MXML_LOGS_HEADING = "MXML Logs";
	private static final String ARISTA_LOGS_HEADING = "AristaFlow Logs";


	private DefaultTreeModel treeModel;
	
	private Set<SwatTreeViewListener> listeners = new HashSet<SwatTreeViewListener>();
	
	private static SwatTreeView swatTreeInstance = new SwatTreeView();
	
	private DefaultMutableTreeNode root;
	private DefaultMutableTreeNode petriNetNode = null;
	private DefaultMutableTreeNode xesLogNode = null;
	private DefaultMutableTreeNode mxmlLogNode = null;
	private DefaultMutableTreeNode aristaLogNode = null;

	@SuppressWarnings("rawtypes")
	private SwatTreeView() {
		root = new DefaultMutableTreeNode("Working Directory");
		treeModel = new DefaultTreeModel(root);
        Color bgcolor = UIManager.getColor ( "Panel.background" );
		this.setBackground(bgcolor);
		this.setModel(treeModel);
		this.setShowsRootHandles(true);
		this.setEditable(false);
		initialize();
		setToggleClickCount(0);//prevent collapse on double click

		// this.setSelectionInterval(0,0);
		addMouseListener(new TreeViewMouseAdapter());
	}
	
	private void initialize(){
		petriNetNode = new DefaultMutableTreeNode(PN_HEADING);
		root.add(petriNetNode);
		xesLogNode = new DefaultMutableTreeNode(XES_LOGS_HEADING);
		root.add(xesLogNode);
		mxmlLogNode = new DefaultMutableTreeNode(MXML_LOGS_HEADING);
		root.add(mxmlLogNode);
		aristaLogNode = new DefaultMutableTreeNode(ARISTA_LOGS_HEADING);
		root.add(aristaLogNode);
		createChildren();
		expandAll();
	}

	public static SwatTreeView getInstance() {
		return swatTreeInstance;
	}

	public void removeAndUpdateSwatComponents() {
		root.removeAllChildren();
		createChildren();
		treeModel.reload();
		expandAll();

		repaint();
	}

	@SuppressWarnings("rawtypes")
	private void createChildren() {
		// Petri Nets
		petriNetNode.removeAllChildren();
		xesLogNode.removeAllChildren();
		mxmlLogNode.removeAllChildren();
		aristaLogNode.removeAllChildren();

		for(AbstractGraphicalPN petriNet: SwatComponents.getInstance().getPetriNetsSorted()){
			SwatTreeNode node = new SwatTreeNode(petriNet, SwatComponentType.PETRI_NET);
			for(Analysis analysis: SwatComponents.getInstance().getAnalyses(node.getDisplayName())){
				node.add(new SwatTreeNode(analysis, SwatComponentType.PETRI_NET_ANALYSIS));
			}
			petriNetNode.add(node);
		}

		// XES-Logs
		for (LogModel logFile : SwatComponents.getInstance().getLogs(SwatLog.XES)) {
			xesLogNode.add(new SwatTreeNode(logFile, SwatComponentType.XES_LOG));
		}
		
		// MXML-Logs
		for (LogModel logFile : SwatComponents.getInstance().getLogs(SwatLog.MXML)) {
			mxmlLogNode.add(new SwatTreeNode(logFile, SwatComponentType.MXML_LOG));
		}
		
		// Aristaflow-Logs
		for (LogModel logFile : SwatComponents.getInstance().getLogs(SwatLog.Aristaflow)) {
			aristaLogNode.add(new SwatTreeNode(logFile, SwatComponentType.ARISTAFLOW_LOG));
		}

		expandAll();

	}
	
	public void expandAll() {
		try {
			for (int i = 0; i < getRowCount(); i++) {
				expandRow(i);
			}
		} catch (Exception e) {
		}
	}


	@Override
	public void operatingModeChanged() {}
	
	
	public void addTreeViewListener(SwatTreeViewListener listener){
		listeners.add(listener);
	}
	
	private void notifyComponentSelected(SwatTreeNode node){
		for(SwatTreeViewListener listener: listeners){
			listener.componentSelected(node);
		}
	}
	
	private void notifyComponentActivated(SwatTreeNode node){
		for(SwatTreeViewListener listener: listeners){
			listener.componentActivated(node);
		}
	}
	
	@SuppressWarnings("unchecked")
	public SwatTreeNode getComponent(WorkbenchComponent component) {
		Enumeration<SwatTreeNode> children = root.children();
		while (children.hasMoreElements()) {
			SwatTreeNode node = children.nextElement();
			if (node.getUserObject().equals(component))
				return node;
		}
		return null;
	}





	
	private class TreeViewMouseAdapter extends MouseAdapter {
		
		@Override
		public void mouseClicked(MouseEvent e) {
			
			if(!(e.getSource() instanceof SwatTreeView))
				return;
			
			if(getSelectionPath() == null)
				return;
			Object selectedNode = getSelectionPath().getLastPathComponent();
			if(selectedNode == null)
				return;
			if(!(selectedNode instanceof SwatTreeNode))
				return;
	
			SwatTreeNode swatNode = (SwatTreeNode) selectedNode;
			if(e.getClickCount()==1){
				if(e.isPopupTrigger()){
					// Right click on tree node
					showPopup(swatNode, e);
				} else {
					// Left click on tree node
					notifyComponentSelected(swatNode);
				}
			} else if (e.getClickCount() == 2 && e.getButton() == 1 && !e.isPopupTrigger()) {
				// Double click on tree node
				notifyComponentActivated(swatNode);
			}
		}

		public void mousePressed(MouseEvent e) {
			if (e.isPopupTrigger()) {
				Object selectedNode = getSelectionPath().getLastPathComponent();
				SwatTreeNode swatNode = (SwatTreeNode) selectedNode;
				showPopup(swatNode, e);
			}
		}

		public void mouseReleased(MouseEvent e) {
			if (e.isPopupTrigger()) {
				Object selectedNode = getSelectionPath().getLastPathComponent();
				SwatTreeNode swatNode = (SwatTreeNode) selectedNode;
				showPopup(swatNode, e);
			}
		}

		private void showPopup(SwatTreeNode node, MouseEvent e) {
			SwatTreePopupMenu menu = new SwatTreePopupMenu(node);
			menu.show((Component) e.getSource(), e.getX(), e.getY());
		}
	}

	private DefaultMutableTreeNode find(String s) {
	    @SuppressWarnings("unchecked")
		Enumeration<DefaultMutableTreeNode> e = ((DefaultMutableTreeNode) getModel().getRoot()).depthFirstEnumeration();
	    while (e.hasMoreElements()) {
	        DefaultMutableTreeNode node = e.nextElement();
	        if (node.toString().equalsIgnoreCase(s)) {
				return node;
	        }
	    }
	    return null;
	}

	@Override
	public void petriNetAdded(AbstractGraphicalPN net) {}

	@Override
	public void petriNetRemoved(AbstractGraphicalPN net) {}

	@Override
	public void petriNetRenamed(AbstractGraphicalPN net) {}

	@Override
	public void acModelAdded(ACModel acModel) {}

	@Override
	public void acModelRemoved(ACModel acModel) {}

	@Override
	public void analysisContextAdded(String netID, AnalysisContext context) {}

	@Override
	public void analysisContextRemoved(String netID, AnalysisContext context) {}

	@Override
	public void analysisAdded(String netID, Analysis analysis) {}

	@Override
	public void analysisRemoved(String netID, Analysis analysis) {}

	@Override
	public void timeContextAdded(String netID, TimeContext context) {}

	@Override
	public void timeContextRemoved(String netID, TimeContext context) {}

	@Override
	public void logAdded(LogModel log) {}

	@Override
	public void logRemoved(LogModel log) {}

	@Override
	public void componentsChanged() {
		createChildren();
		treeModel.reload();
		expandAll();
		repaint();
	}

}
