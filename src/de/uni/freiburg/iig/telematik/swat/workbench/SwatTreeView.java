package de.uni.freiburg.iig.telematik.swat.workbench;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.swat.logs.LogModel;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatComponentsListener;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatStateListener;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatTreeViewListener;

@SuppressWarnings("serial")
public class SwatTreeView extends JTree implements SwatStateListener, SwatComponentsListener {

	private DefaultMutableTreeNode root;
	private DefaultTreeModel treeModel;
	
	private Set<SwatTreeViewListener> listeners = new HashSet<SwatTreeViewListener>();
	
	private static SwatTreeView swatTreeInstance = new SwatTreeView();

	@SuppressWarnings("rawtypes")
	private SwatTreeView() {
		root = new DefaultMutableTreeNode("Working Directory");
		treeModel = new DefaultTreeModel(root);
        Color bgcolor = UIManager.getColor ( "Panel.background" );
		this.setBackground(bgcolor);
		this.setModel(treeModel);
		this.setShowsRootHandles(true);
		this.setEditable(false);
		createChildren();

		// this.setSelectionInterval(0,0);
		addMouseListener(new TreeViewMouseAdapter());
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

	public void update() {
		treeModel.reload();
		expandAll();
		repaint();
	}

	@SuppressWarnings("rawtypes")
	private void createChildren() {
		// Petri Nets

		DefaultMutableTreeNode petriNets = new DefaultMutableTreeNode("Petri Nets");
		for(AbstractGraphicalPN petriNet: SwatComponents.getInstance().getPetriNets()){
			petriNets.add(new SwatTreeNode(petriNet, SwatComponentType.PETRI_NET, SwatComponents.getInstance().getFile(petriNet)));
		}
		root.add(petriNets);

		// LogFiles
		DefaultMutableTreeNode logFiles = new DefaultMutableTreeNode("Logfiles");
		for (LogModel logFile : SwatComponents.getInstance().getLogFiles()) {
			logFiles.add(new SwatTreeNode(logFile, SwatComponentType.LOG_FILE, logFile.getFileReference()));
		}
		root.add(logFiles);

		//XML Files
		DefaultMutableTreeNode xmlFiles = new DefaultMutableTreeNode("XML files");
		for (LogModel xmlFileViewer : SwatComponents.getInstance().getXMLFiles()) {
			xmlFiles.add(new SwatTreeNode(xmlFileViewer, SwatComponentType.XML_FILE, xmlFileViewer.getFileReference()));
		}
		root.add(xmlFiles);
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
	
	public class SwatTreeNode extends DefaultMutableTreeNode {

		private static final long serialVersionUID = 8746333990209477776L;
		private SwatComponentType objectType = null;
		private String displayName = null;
		private File fileReference;

		public SwatTreeNode(Object userObject, SwatComponentType objectType, File fileReference) {
			super(userObject, false);
			this.objectType = objectType;
			this.fileReference = fileReference;
			setDisplayName();
		}

		public File getFileReference() {
			return fileReference;
		}

		@SuppressWarnings("rawtypes")
		private void setDisplayName() {
			switch (objectType) {
			case LABELING:
				//TODO:
				break;
			case PETRI_NET:
				displayName = SwatComponents.getInstance().getFileName((AbstractGraphicalPN) getUserObject());
				break;
			case PETRI_NET_ANALYSIS:
				displayName = (String) this.getUserObject();
				break;
			case LOG_FILE:
				// userObject is of instance LogFileViewer
				displayName = ((LogModel) this.getUserObject()).getName();
				break;
			case XML_FILE:
				displayName = ((LogModel) this.getUserObject()).getName();

			}
		}

		public String getDisplayName() {
			setDisplayName();
			return displayName;
		}

		/** Update the displayName for JTree if Filename changed **/
		public void updateDisplayName() {
			setDisplayName();
		}

		public SwatComponentType getObjectType() {
			return objectType;
		}

		@Override
		public String toString() {
			return getDisplayName();
		}

	}

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
	public SwatTreeNode getComponent(SwatComponent component) {
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
					//TODO: Trigger rename process
				} else {
					// Left click on tree node
					notifyComponentSelected(swatNode);
				}
			} else if (e.getClickCount() == 2 && e.getButton() == 1 && !e.isPopupTrigger()) {
				// Double click on tree node
				notifyComponentActivated(swatNode);
			}
		}
	}

	@Override
	public void analysisAdded(SwatTreeNode node, Object AnalysisElement) {
		// TODO Auto-generated method stub

	}

	@Override
	public void modelChanged() {
		removeAndUpdateSwatComponents();

	}

	@Override
	public void elementRemoved(Object elem) {
		removeAndUpdateSwatComponents();
	}

	@Override
	public void nodeAdded(SwatTreeNode node) {
		if (node.getObjectType().equals(SwatComponentType.LOG_FILE))
			find("Logfiles").add(node);
		if (node.getObjectType().equals(SwatComponentType.PETRI_NET))
			find("Petri Nets").add(node);

		update();
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

}
