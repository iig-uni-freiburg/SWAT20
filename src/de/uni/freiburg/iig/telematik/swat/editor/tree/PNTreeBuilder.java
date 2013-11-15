package de.uni.freiburg.iig.telematik.swat.editor.tree;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperty;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PropertiesView.PropertiesField;


public class PNTreeBuilder {

	public static PNTreeNode build(Map<String, HashMap<PNProperty, PropertiesField>> placeFields, Map<String, HashMap<PNProperty, PropertiesField>> transitionFields, Map<String, HashMap<PNProperty, PropertiesField>> arcFields) {
		  PNTreeNode rootNode = new PNTreeNode("PN", PNTreeNodeType.ROOT);
		  
		  //Places
		PNTreeNode placesNode = new PNTreeNode("Places", PNTreeNodeType.PLACES);
		for (Entry<String, HashMap<PNProperty, PropertiesField>> placeField : placeFields.entrySet()) {
				PNTreeNode placeNode = new PNTreeNode(placeField.getKey(), PNTreeNodeType.PLACE);
				placesNode.addChild(addPropertyNodes(placeField, placeNode));
		}
		
		  //transitions
		PNTreeNode transitionsNode = new PNTreeNode("transitions", PNTreeNodeType.TRANSITIONS);
		for (Entry<String, HashMap<PNProperty, PropertiesField>> transitionField : transitionFields.entrySet()) {
			PNTreeNode TransitionNode = new PNTreeNode(transitionField.getKey(), PNTreeNodeType.TRANSITION);	
			transitionsNode.addChild(addPropertyNodes(transitionField, TransitionNode));
		}
		
		  //arcs
		PNTreeNode arcsNode = new PNTreeNode("arcs", PNTreeNodeType.ARCS);
		for (Entry<String, HashMap<PNProperty, PropertiesField>> arcField : arcFields.entrySet()) {
			PNTreeNode ArcNode = new PNTreeNode(arcField.getKey(), PNTreeNodeType.ARC);	
			arcsNode.addChild(addPropertyNodes(arcField, ArcNode));
		
		}
		
		rootNode.addChild(placesNode);
		rootNode.addChild(transitionsNode);
		rootNode.addChild(arcsNode);
		return rootNode;
	}

	private static PNTreeNode addPropertyNodes(
			Entry<String, HashMap<PNProperty, PropertiesField>> placeField,
			PNTreeNode placeNode) {
		for(Entry<PNProperty, PropertiesField> o:placeField.getValue().entrySet()){
			PNTreeNode propNode = new PNTreeNode(o.getKey().toString(), PNTreeNodeType.LEAF,o.getValue());
		
		placeNode.addChild(propNode);
		}
		return placeNode;
		
	}

}