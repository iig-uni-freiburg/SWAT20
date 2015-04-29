package de.uni.freiburg.iig.telematik.swat.workbench;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import de.invation.code.toval.misc.soabase.SOABase;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.Labeling;
import de.uni.freiburg.iig.telematik.sewol.accesscontrol.AbstractACModel;
import de.uni.freiburg.iig.telematik.swat.analysis.Analysis;
import de.uni.freiburg.iig.telematik.swat.ext.MultiSplitPane;
import de.uni.freiburg.iig.telematik.swat.logs.LogModel;
import de.uni.freiburg.iig.telematik.swat.logs.SwatLog;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.TimeContext;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.SwatTreePopupMenu;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatComponentsListener;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatStateListener;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatTreeViewListener;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.ViewComponent;

@SuppressWarnings("serial")
public class SwatTreeView extends JTree implements SwatStateListener, SwatComponentsListener {
	
	private static final String PN_HEADING = "Petri nets";
	private static final String LOGS_HEADING = "Process logs";
	private static final String CONTEXTS_HEADING = "Execution contexts";
	private static final String	ACMODELS_HEADING = "Access control models";
	private static final String XES_LOGS_HEADING = "XES Logs";
	private static final String MXML_LOGS_HEADING = "MXML Logs";
	private static final String ARISTA_LOGS_HEADING = "AristaFlow Logs";
	private static final Color DEFAULT_BG_COLOR = UIManager.getColor("Panel.background");
	private static final Color BG_COLOR = Color.white;
	private static final Color BORDER_COLOR = new Color(237,237,237);


	private DefaultTreeModel treeModel;
	
	private Set<SwatTreeViewListener> listeners = new HashSet<SwatTreeViewListener>();
	
	private static SwatTreeView swatTreeInstance = new SwatTreeView();
	
	private DefaultMutableTreeNode rootNode;
	private DefaultMutableTreeNode petriNetNode = null;
	private DefaultMutableTreeNode logsNode = null;
	private DefaultMutableTreeNode xesLogNode = null;
	private DefaultMutableTreeNode mxmlLogNode = null;
	private DefaultMutableTreeNode aristaLogNode = null;
	private DefaultMutableTreeNode acModelsNode = null;
	private DefaultMutableTreeNode contextsNode = null;

	private SwatTreeView() {
		rootNode = new DefaultMutableTreeNode("Working Directory");
		treeModel = new DefaultTreeModel(rootNode);
		this.setBackground(DEFAULT_BG_COLOR);
		this.setModel(treeModel);
		this.setShowsRootHandles(true);
		this.setEditable(false);
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		buildTree();
		setToggleClickCount(0);//prevent collapse on double click

		// this.setSelectionInterval(0,0);
		addMouseListener(new TreeViewMouseAdapter());
	}

	public static SwatTreeView getInstance() {
		return swatTreeInstance;
	}

	public void removeAndUpdateSwatComponents() {
		rootNode.removeAllChildren();
		buildTree();
		treeModel.reload();
		expandAll();
		repaint();
	}

	@SuppressWarnings("rawtypes")
	private void buildTree() {
		rootNode.removeAllChildren();
		
		// Petri Nets
		if(SwatComponents.getInstance().containsPetriNets()){
			petriNetNode = new DefaultMutableTreeNode(PN_HEADING);
			rootNode.add(petriNetNode);
			
			for(AbstractGraphicalPN petriNet: SwatComponents.getInstance().getPetriNetsSorted()){
				SwatTreeNode node = new SwatTreeNode(petriNet, SwatComponentType.PETRI_NET);
				for(Analysis analysis: SwatComponents.getInstance().getAnalyses(node.getDisplayName())){
					node.add(new SwatTreeNode(analysis, SwatComponentType.PETRI_NET_ANALYSIS));
				}
				petriNetNode.add(node);
			}
		}
		
		// Logs
		if(SwatComponents.getInstance().containsLogs()){
			logsNode = new DefaultMutableTreeNode(LOGS_HEADING);
			rootNode.add(logsNode);
			
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
		}
		
		if(SwatComponents.getInstance().containsContexts()){
			contextsNode = new DefaultMutableTreeNode(CONTEXTS_HEADING);
			rootNode.add(contextsNode);
		}
		
		if(SwatComponents.getInstance().containsACModels()){
			acModelsNode = new DefaultMutableTreeNode(ACMODELS_HEADING);
			rootNode.add(acModelsNode);
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
	public SwatTreeNode getComponent(ViewComponent component) {
		Enumeration<SwatTreeNode> children = rootNode.children();
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
					showPopup(e);
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
				showPopup(e);
			}
		}

		public void mouseReleased(MouseEvent e) {
			if (e.isPopupTrigger()) {
				showPopup(e);
			}
		}

		private void showPopup(MouseEvent e) {
			Object selectedNode = getSelectionPath().getLastPathComponent();
			if (selectedNode instanceof SwatTreeNode) {
				SwatTreeNode swatNode = (SwatTreeNode) selectedNode;
				SwatTreePopupMenu menu = new SwatTreePopupMenu(swatNode);
				menu.show((Component) e.getSource(), e.getX(), e.getY());
			}
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
	public void petriNetRemoved(AbstractGraphicalPN net) {
		//TODO
	}

	@Override
	public void petriNetRenamed(AbstractGraphicalPN net) {}

	@Override
	public void acModelAdded(AbstractACModel acModel) {}

	@Override
	public void acModelRemoved(AbstractACModel acModel) {}

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
		buildTree();
		treeModel.reload();
		expandAll();
		repaint();
	}

	@Override
	public void contextAdded(SOABase soaBase) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextRemoved(SOABase soaBase) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void labelingAdded(String netID, String analysisContextName, Labeling labeling) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void labelingRemoved(String netID, String analysisContextName, Labeling labeling) {
		// TODO Auto-generated method stub
		
	}

}
