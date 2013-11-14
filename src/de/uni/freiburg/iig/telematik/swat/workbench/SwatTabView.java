package de.uni.freiburg.iig.telematik.swat.workbench;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalCPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalIFNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeView.SwatTreeNode;
import de.unifreiburg.iig.bpworkbench2.editor.PTNetEditor;

@SuppressWarnings("serial")
public class SwatTabView extends JTabbedPane implements SwatTreeViewListener{
	
	private Map<Object, Component> openedSwatComponents = new HashMap<Object, Component>();

	public SwatTabView() {}

	@Override
	public void componentSelected(SwatTreeNode node) {
		for(Object openedComponent: openedSwatComponents.keySet()){
			if(openedComponent == node.getUserObject()){
				setSelectedComponent(openedSwatComponents.get(openedComponent));
				return;
			}
		}
	}

	@Override
	public void componentActivated(SwatTreeNode node) {
		if(openedSwatComponents.keySet().contains(node.getUserObject()))
			componentSelected(node);
		addNewTab(node);
	}
	
	private JTextArea getTextArea(String text){
		JTextArea newArea = new JTextArea(text);
		newArea.setEditable(false);
		return newArea;
	}
	
	@SuppressWarnings("rawtypes")
	private void addPNEditor(AbstractGraphicalPN petriNet, String tabName) throws ParameterException{
		if(petriNet instanceof GraphicalPTNet){
			addTab(tabName, new PTNetEditor((GraphicalPTNet) petriNet, SwatComponents.getInstance().getFile(petriNet)));
		} else if(petriNet instanceof GraphicalCPN){
			
		} else if(petriNet instanceof GraphicalIFNet){
			
		}
		openedSwatComponents.put(petriNet, getComponentAt(getComponentCount()-1));
		setSelectedIndex(getTabCount()-1);
	}

	@SuppressWarnings("rawtypes")
	private void addNewTab(SwatTreeNode node) {
		try {
			switch (node.getObjectType()) {
			case LABELING:
				// TODO:
				break;
			case PETRI_NET:
				addPNEditor((AbstractGraphicalPN) node.getUserObject(), node.getDisplayName());
				break;
			}
		} catch (ParameterException e) {
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(getParent()), "Cannot display component in new tab.\nReason: "+e.getMessage(), "SWAT Exception", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	

}
