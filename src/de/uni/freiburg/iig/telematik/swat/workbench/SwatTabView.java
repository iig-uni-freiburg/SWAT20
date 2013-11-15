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
import de.uni.freiburg.iig.telematik.swat.editor.PTNetEditor;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeView.SwatTreeNode;

@SuppressWarnings("serial")
public class SwatTabView extends JTabbedPane {
	
	private Map<Object, Component> openedSwatComponents = new HashMap<Object, Component>();

	public SwatTabView() {}

	public void componentSelected(SwatTreeNode node) {
		for(Object openedComponent: openedSwatComponents.keySet()){
			if(openedComponent == node.getUserObject()){
				setSelectedComponent(openedSwatComponents.get(openedComponent));
				return;
			}
		}
	}
	
	public boolean containsComponent(SwatTreeNode node){
		return openedSwatComponents.keySet().contains(node.getUserObject());
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
	
	private void addSwatComponent(SwatComponent swatComponent, String tabName){
		addTab(tabName, swatComponent.getMainComponent());
		
	}

	@SuppressWarnings("rawtypes")
	public void addNewTab(SwatTreeNode node) {
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
