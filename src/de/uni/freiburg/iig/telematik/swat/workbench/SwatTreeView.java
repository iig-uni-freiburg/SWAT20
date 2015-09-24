package de.uni.freiburg.iig.telematik.swat.workbench;

import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponentType;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;
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

import com.sun.xml.internal.bind.v2.runtime.reflect.Lister.Pack;

import de.invation.code.toval.misc.NamedComponent;
import de.invation.code.toval.misc.wd.ComponentListener;
import de.invation.code.toval.misc.wd.ProjectComponentException;
import de.invation.code.toval.validate.ExceptionDialog;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.swat.analysis.Analysis;
import de.uni.freiburg.iig.telematik.swat.logs.LogModel;
import de.uni.freiburg.iig.telematik.swat.logs.SwatLogType;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.SwatTreePopupMenu;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatStateListener;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatTreeViewListener;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.ViewComponent;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class SwatTreeView extends JTree implements SwatStateListener, ComponentListener {
	
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
	
	private static SwatTreeView instance = null;
	
	private DefaultMutableTreeNode rootNode;
	private DefaultMutableTreeNode petriNetNode = null;
	private DefaultMutableTreeNode logsNode = null;
	private DefaultMutableTreeNode xesLogNode = null;
	private DefaultMutableTreeNode mxmlLogNode = null;
	private DefaultMutableTreeNode aristaLogNode = null;
	private DefaultMutableTreeNode acModelsNode = null;
	private DefaultMutableTreeNode contextsNode = null;

	private SwatTreeView() throws Exception {
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
		
		SwatComponents.getInstance().addComponentListener(this);
		SwatComponents.getInstance().getContainerAristaflowLogs().addComponentListener(this);
		SwatComponents.getInstance().getContainerMXMLLogs().addComponentListener(this);
		SwatComponents.getInstance().getContainerXESLogs().addComponentListener(this);
		SwatComponents.getInstance().getContainerPetriNets().addComponentListener(this);
	}

	public static SwatTreeView getInstance() throws Exception {
            if(instance == null){
                instance = new SwatTreeView();
            }
            return instance;
	}

	public void removeAndUpdateSwatComponents() throws ProjectComponentException {
		rootNode.removeAllChildren();
		buildTree();
		treeModel.reload();
		expandAll();
		repaint();
	}

	@SuppressWarnings("rawtypes")
	private void buildTree() throws ProjectComponentException {
		rootNode.removeAllChildren();
		
		// Petri Nets
		if(SwatComponents.getInstance().containsPetriNets()){
			petriNetNode = new DefaultMutableTreeNode(PN_HEADING);
			rootNode.add(petriNetNode);
			
			for(AbstractGraphicalPN petriNet: SwatComponents.getInstance().getContainerPetriNets().getComponentsSorted()){
				SwatTreeNode node = new SwatTreeNode(petriNet, SwatComponentType.PETRI_NET);
				for(Analysis analysis: SwatComponents.getInstance().getContainerPetriNets().getContainerAnalysis(node.getDisplayName()).getComponents()){
					node.add(new SwatTreeNode(analysis, SwatComponentType.PETRI_NET_ANALYSIS));
				}
				petriNetNode.add(node);
			}
		}
		
		// Logs
		if(SwatComponents.getInstance().containsLogs()){
			logsNode = new DefaultMutableTreeNode(LOGS_HEADING);
			xesLogNode = new DefaultMutableTreeNode(XES_LOGS_HEADING);
			logsNode.add(xesLogNode);
			mxmlLogNode = new DefaultMutableTreeNode(MXML_LOGS_HEADING);
			logsNode.add(mxmlLogNode);
			aristaLogNode = new DefaultMutableTreeNode(ARISTA_LOGS_HEADING);
			logsNode.add(aristaLogNode);
			rootNode.add(logsNode);
			
			// XES-Logs
			for (LogModel logFile : SwatComponents.getInstance().getLogs(SwatLogType.XES)) {
				xesLogNode.add(new SwatTreeNode(logFile, SwatComponentType.XES_LOG));
			}
			
			// MXML-Logs
			for (LogModel logFile : SwatComponents.getInstance().getLogs(SwatLogType.MXML)) {
				mxmlLogNode.add(new SwatTreeNode(logFile, SwatComponentType.MXML_LOG));
			}
			
			// Aristaflow-Logs
			for (LogModel logFile : SwatComponents.getInstance().getLogs(SwatLogType.Aristaflow)) {
				aristaLogNode.add(new SwatTreeNode(logFile, SwatComponentType.ARISTAFLOW_LOG));
			}
		}
		
		if(SwatComponents.getInstance().containsProcessContexts()){
			contextsNode = new DefaultMutableTreeNode(CONTEXTS_HEADING);
			rootNode.add(contextsNode);
		}
		
		if(SwatComponents.getInstance().containsACModels()){
			acModelsNode = new DefaultMutableTreeNode(ACMODELS_HEADING);
			rootNode.add(acModelsNode);
		}
		expandAll();
		validate();
		repaint();
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
	public void componentsChanged() {
		System.out.println("Components Changed");
            try{
		buildTree();
		treeModel.reload();
		expandAll();
		repaint();
            } catch(Exception e){
                ExceptionDialog.showException(SwingUtilities.getWindowAncestor(SwatTreeView.this), "Tree View Exception", new Exception("Exception while building tree"), true, true);
            }
	}
        
        @Override
    public void componentAdded(Object component) throws ProjectComponentException {
        	//System.out.println("Component Added");
//        	if(component instanceof NamedComponent){
//        		SwatTreeNode node = new SwatTreeNode(component, SwatComponentType.PETRI_NET);
//        		petriNetNode.add(node);
//        	}
        	buildTree();
        	
        	
   
    }

    @Override
    public void componentRemoved(Object component) throws ProjectComponentException {
    	buildTree();
    }

    @Override
    public void componentRenamed(Object component, String oldName, String newName) throws ProjectComponentException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
