package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.util.ArrayList;
import java.util.List;

import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

/**
 * This Class is used to retrieve places and arcs which are between the
 * transitions of an counterexample. It is used when an path should be highlighted.
 * 
 * @author bernhard
 * 
 * @param <T>
 *            the FlowRelation which depends on the petri net type
 */
public class PlacesArcsAlgorithm<T extends AbstractFlowRelation> {
	/**
	 * Add nodes to the nodeList and the arcs to the arcList
	 * @param pnEditor the PNEditor containing the PN
	 * @param path the counterexample
	 * @param nodeList an empty list to which the places should be added
	 * @param arcList an empty list to which the arcs should be added
	 */
	public void addPlacesArcs(PNEditorComponent pnEditor, List<String> path,
			List<String> nodeList, List<String> arcList) {


		// System.out.println(nodemap);
		// get places to mark
		java.util.List<String> transitions = path;
		;
		AbstractPetriNet pn = pnEditor.getNetContainer().getPetriNet();
		nodeList.addAll(transitions);
		// we have to find the places and arcs in the path
		for (int i = 0; i < transitions.size() - 1; i++) {
			// first incoming places with no incoming edges
			java.util.List<T> placesBeforeThis = pn.getTransition(
					transitions.get(i)).getIncomingRelations();
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
			java.util.List<T> placesAfterRel = pn.getTransition(
					transitions.get(i)).getOutgoingRelations();
			java.util.List<String> placesAfterThis = new ArrayList<String>();
			for (T relation : placesAfterRel) {
				placesAfterThis.add(relation.getPlace().getName());
			}
			for (int j = i + 1; j < transitions.size(); j++) {

				java.util.List<T> placesBeforeRel = pn.getTransition(
						transitions.get(j)).getIncomingRelations();
				java.util.List<String> placesBefore = new ArrayList<String>();
				for (T relation : placesBeforeRel) {
					placesBefore.add(relation.getPlace().getName());
				}

				// add the intersection of both lists
				java.util.List<String> temp_list = Helpers.Intersection(
						placesAfterThis, placesBefore);
				for (String place : temp_list) {
					nodeList.add(place);
					// both edges
					arcList.add("arcTP_" + transitions.get(i) + place);
					arcList.add("arcPT_" + place + transitions.get(j));
				}
			}
		}
	}

}
