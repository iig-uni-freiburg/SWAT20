package de.uni.freiburg.iig.telematik.swat.editor.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JTextField;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperty;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PropertiesView.PropertiesField;


public class PNTreeBuilder {
	

	public static PNTreeNode build(Map<String, HashMap<PNProperty, PropertiesField>> placeFields, Map<String, HashMap<PNProperty, PropertiesField>> transitionFields, Map<String, HashMap<PNProperty, PropertiesField>> arcFields) {
		  PNTreeNode rootNode = new PNTreeNode("PN", PNTreeNodeType.ROOT);
		  
		  //Places
		PNTreeNode placesNode = new PNTreeNode("Places", PNTreeNodeType.PLACES);
		for (Entry<String, HashMap<PNProperty, PropertiesField>> placeField : placeFields.entrySet()) {
				placesNode.add(addPropertyNodes2(placeField, PNTreeNodeType.PLACE));
		}
		
		  //transitions
		PNTreeNode transitionsNode = new PNTreeNode("transitions", PNTreeNodeType.TRANSITIONS);
		for (Entry<String, HashMap<PNProperty, PropertiesField>> transitionField : transitionFields.entrySet()) {
			PNTreeNode TransitionNode = new PNTreeNode(transitionField.getKey(), PNTreeNodeType.TRANSITION);	
			transitionsNode.add(addPropertyNodes(transitionField, TransitionNode));
		}
		
		  //arcs
		PNTreeNode arcsNode = new PNTreeNode("arcs", PNTreeNodeType.ARCS);
		for (Entry<String, HashMap<PNProperty, PropertiesField>> arcField : arcFields.entrySet()) {
			PNTreeNode ArcNode = new PNTreeNode(arcField.getKey(), PNTreeNodeType.ARC);	
			arcsNode.add(addPropertyNodes(arcField, ArcNode));
		
		}
		
		rootNode.add(placesNode);
		rootNode.add(transitionsNode);
		rootNode.add(arcsNode);
//		  System.out.println( new PNTreePath(placesNode.getPath()));
		  
		return rootNode;
	}

	private static MutableTreeNode addPropertyNodes2(Entry<String, HashMap<PNProperty, PropertiesField>> placeField, PNTreeNodeType place) {
		PNTreeNode placeNode = new PNTreeNode(placeField.getKey(), place);
		for(Entry<PNProperty, PropertiesField> o:placeField.getValue().entrySet()){
			PNTreeNode propNode = null;
		
		switch(o.getKey()){
		case ARC_WEIGHT:
			break;
		case PLACE_LABEL:
			placeNode.setTextfield(o.getValue());
			break;
		case PLACE_SIZE:
			propNode = new PNTreeNode(o.getKey().toString(), PNTreeNodeType.LEAF,o.getValue(), o.getKey());
			placeNode.add(propNode);
			break;
		case TRANSITION_LABEL:
//			placeNode.setUserObject(o.getValue().getText());
			break;
		case TRANSITION_SIZE:
			break;
		default:
	
			break;
		
		}
		
		
		}
		return placeNode;
	}

	private static PNTreeNode addPropertyNodes(
			Entry<String, HashMap<PNProperty, PropertiesField>> placeField,
			PNTreeNode placeNode) {
		for(Entry<PNProperty, PropertiesField> o:placeField.getValue().entrySet()){
			PNTreeNode propNode = new PNTreeNode(o.getKey().toString(), PNTreeNodeType.LEAF,o.getValue(), o.getKey());
		placeNode.add(propNode);
		switch(o.getKey()){
		case ARC_WEIGHT:
			break;
		case PLACE_LABEL:
//			placeNode.setUserObject(o.getValue().getText());
			break;
		case PLACE_SIZE:
			break;
		case TRANSITION_LABEL:
//			placeNode.setUserObject(o.getValue().getText());
			break;
		case TRANSITION_SIZE:
			break;
		default:
			break;
		
		}
		
		}
		return placeNode;
		
	}
	


}