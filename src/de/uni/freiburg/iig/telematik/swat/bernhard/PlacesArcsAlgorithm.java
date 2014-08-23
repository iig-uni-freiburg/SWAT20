package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTFlowRelation;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;

public class PlacesArcsAlgorithm<T extends AbstractFlowRelation> {

	public void addPlacesArcs(PNEditor pnEditor, List<String> path, List<String> nodeList, List<String> arcList) {
		Map<String, PNGraphCell> nodemap = pnEditor.getGraphComponent()
				.getGraph().nodeReferences;
		Map<String, PNGraphCell> arcmap = pnEditor.getGraphComponent()
				.getGraph().arcReferences;

		// System.out.println(nodemap);
		// get places to mark
		java.util.List<String> transitions = path;
		;
		AbstractPetriNet pn = pnEditor.getNetContainer().getPetriNet();
		nodeList.addAll(transitions);
		// we have to find the places and arcs in the path
		for (int i = 0; i < transitions.size() - 1; i++) {
			// first incoming places with no incoming edges
				java.util.List<T> placesBeforeThis = pn
						.getTransition(transitions.get(i))
						.getIncomingRelations();
				for (T relation : placesBeforeThis) {
					if (relation.getPlace().getIncomingRelations().size() == 0) {
						// add the place name
						String placeName = relation.getPlace().getName();
						nodeList.add(placeName);
						// add the arc
						arcList.add("arcPT_" + placeName + transitions.get(i));
					}
				}
				// then places between this transition and the next
				java.util.List<T> placesAfterRel = pn
						.getTransition(transitions.get(i))
						.getOutgoingRelations();
				java.util.List<String> placesAfter = new ArrayList<String>();
				for (T relation : placesAfterRel) {
					placesAfter.add(relation.getPlace().getName());
				}
				java.util.List<T> placesBeforeRel = pn
						.getTransition(transitions.get(i + 1))
						.getIncomingRelations();
				java.util.List<String> placesBefore = new ArrayList<String>();
				for (T relation : placesBeforeRel) {
					placesBefore.add(relation.getPlace().getName());
				}
				// add the intersection of both lists
				java.util.List<String> temp_list = Helpers.Intersection(
						placesAfter, placesBefore);
				for (String place : temp_list) {
					nodeList.add(place);
					// both edges
					arcList.add("arcTP_" + transitions.get(i) + place);
					arcList.add("arcPT_" + place + transitions.get(i + 1));
				}
			} 
		}
	
}
