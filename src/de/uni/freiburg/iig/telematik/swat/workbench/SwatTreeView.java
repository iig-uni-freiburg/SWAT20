package de.uni.freiburg.iig.telematik.swat.workbench;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.swat.sciff.LogFileViewer;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatStateListener;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatTreeViewListener;

@SuppressWarnings("serial")
public class SwatTreeView extends JTree implements SwatStateListener {

	private DefaultMutableTreeNode root;
	private DefaultTreeModel treeModel;
	
	private Set<SwatTreeViewListener> listeners = new HashSet<SwatTreeViewListener>();
	
	@SuppressWarnings("rawtypes")
	public SwatTreeView() {
		root = new DefaultMutableTreeNode("Working Directory");
		treeModel = new DefaultTreeModel(root);
		this.setModel(treeModel);
		this.setShowsRootHandles(true);
		this.setEditable(false);
		createChildren();

		// this.setSelectionInterval(0,0);
		addMouseListener(new TreeViewMouseAdapter());
	}

	public void removeAndUpdateSwatComponents() {
		root.removeAllChildren();
		createChildren();
		treeModel.reload();
		repaint();
	}

	@SuppressWarnings("rawtypes")
	private void createChildren() {
		// Petri Nets
		for(AbstractGraphicalPN petriNet: SwatComponents.getInstance().getPetriNets()){
			root.add(new SwatTreeNode(petriNet, SwatComponentType.PETRI_NET));
		}

		// LogFiles
		for (LogFileViewer logFileViewer : SwatComponents.getInstance().getLogFiles()) {
			root.add(new SwatTreeNode(logFileViewer, SwatComponentType.LOG_FILE));
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
	
	public class SwatTreeNode extends DefaultMutableTreeNode {
		
		private SwatComponentType objectType = null;
		private String displayName = null;

		public SwatTreeNode(Object userObject, SwatComponentType objectType) {
			super(userObject, false);
			this.objectType = objectType;
			setDisplayName();
		}
		
		@SuppressWarnings("rawtypes")
		private void setDisplayName(){
			switch(objectType){
			case LABELING:
				//TODO:
				break;
			case PETRI_NET:
				displayName = SwatComponents.getInstance().getFileName((AbstractGraphicalPN) getUserObject()); 
				break;
			case LOG_FILE:
				// userObject if of instance LogFileViewer
				displayName = ((LogFileViewer) this.getUserObject()).getName();
			}
		}
		
		public String getDisplayName(){
			return displayName;
		}
		
		public SwatComponentType getObjectType(){
			return objectType;
		}

		@Override
		public String toString() {
			return getDisplayName();
		}
		
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

}
