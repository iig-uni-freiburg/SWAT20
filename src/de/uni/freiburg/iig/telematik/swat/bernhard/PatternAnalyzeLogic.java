package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.awt.Color;
import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import com.mxgraph.swing.handler.mxCellMarker;

import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTFlowRelation;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;

public class PatternAnalyzeLogic {
	public static final int MAX_PATTERNS = 10;

	public PatternAnalyzeLogic() {
		super();
	}
	/**
	 * hightlight the path given by the Counterexample
	 * @param ce
	 */
	public void highLightCounterExample(PNEditor pneditor) {
		Map<String, PNGraphCell> nodemap = pneditor.getGraphComponent().getGraph().nodeReferences;
		Map<String, PNGraphCell> arcmap = pneditor.getGraphComponent().getGraph().arcReferences;

		//System.out.println(nodemap);
		//get places to mark
		java.util.List<String> transitions=Arrays.asList("t0", "t1", "t2");

		AbstractPetriNet pn=pneditor.getNetContainer().getPetriNet();
		java.util.List<String> nodeList=new ArrayList<String>();
		java.util.List<String> arcList=new ArrayList<String>();
		nodeList.addAll(transitions);
		
		// we have to find the places and arcs in the path
		
		for(int i=0; i < transitions.size() - 1; i++) {
			// first incoming places with no incoming edges
			java.util.List<PTFlowRelation> placesBeforeThis=pn.getTransition(transitions.get(i)).getIncomingRelations();
			for(PTFlowRelation relation:placesBeforeThis) {
				if(relation.getPlace().getIncomingRelations().size() == 0) {
					// add the place name
					String placeName=relation.getPlace().getName();
					nodeList.add(placeName);
					// add the arc
					arcList.add("arcPT_"+placeName+transitions.get(i));
				}
			}
			// then places between this transition and the next
			java.util.List<PTFlowRelation> placesAfterRel=pn.getTransition(transitions.get(i)).getOutgoingRelations();
			java.util.List<String> placesAfter=new ArrayList<String>();
			for(PTFlowRelation relation:placesAfterRel) {
				placesAfter.add(relation.getPlace().getName());
			}
			java.util.List<PTFlowRelation> placesBeforeRel=pn.getTransition(transitions.get(i+1)).getIncomingRelations();
			java.util.List<String> placesBefore=new ArrayList<String>();
			for(PTFlowRelation relation:placesBeforeRel) {
				placesBefore.add(relation.getPlace().getName());
			}
			// add the intersection of both lists
			java.util.List<String> temp_list=Helpers.Intersection(placesAfter, placesBefore);
			for(String place:temp_list ) {
				nodeList.add(place);
				// both edges
				arcList.add("arcTP_"+transitions.get(i)+place);
				arcList.add("arcPT_"+place+transitions.get(i+1));
			}
		}
		// mark all places or transitions
		for(String place:nodeList) {
			PNGraphCell cell=nodemap.get(place);
			final mxCellMarker marker = new mxCellMarker(pneditor.getGraphComponent());
			marker.highlight(pneditor.getGraphComponent().getGraph().getView().getState(cell), Color.RED);
		}

		for(String place:arcList) {
			PNGraphCell cell=arcmap.get(place);
			final mxCellMarker marker = new mxCellMarker(pneditor.getGraphComponent());
			marker.highlight(pneditor.getGraphComponent().getGraph().getView().getState(cell), Color.RED);
		}
		//System.out.println(arcmap);
		
		pneditor.requestFocus();
	}
	
}
