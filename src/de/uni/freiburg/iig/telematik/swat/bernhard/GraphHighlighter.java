package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.mxgraph.swing.handler.mxCellMarker;

import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.swat.lukas.Operand;
import de.uni.freiburg.iig.telematik.swat.lukas.PlacePredicate;
import de.uni.freiburg.iig.telematik.swat.lukas.Transition;


public class GraphHighlighter {
	
	private PNEditor mPNEditor;
	private AbstractPetriNet<?,?,?,?,?,?,?> mNet;
	private Map<String, mxCellMarker> markerReference;
	private ArrayList<String> mPlaceNames;
	private ArrayList<String> mTransitionNames;
	private Map<String, PNGraphCell> mNodemap;
	private Map<String, PNGraphCell> mArcmap; 
	
	// arcList.add("arcTP_" + transitions.get(i) + place);
	// arcList.add("arcPT_" + place + transitions.get(j));
	
	private GraphHighlighter(PNEditor pnEditor) {
		mPNEditor = pnEditor;
		mNet = pnEditor.netContainer.getPetriNet();
		markerReference = new HashMap<String, mxCellMarker>();
		mPlaceNames = new ArrayList<String>();
		mTransitionNames = new ArrayList<String>();
		mNodemap = pnEditor.getGraphComponent().getGraph().nodeReferences;
		mArcmap = pnEditor.getGraphComponent().getGraph().arcReferences;
	}
	
	public GraphHighlighter(PNEditor pnEditor, ArrayList<Operand> operands) {
		this(pnEditor);
		mPNEditor = pnEditor;
		initPlacesAndTransitions(operands);
	}
	
	private void initPlacesAndTransitions(ArrayList<Operand> operands) {
		for (Operand op : operands) {
			if (op instanceof Transition) {
				mTransitionNames.add(op.getName());
			} else if (op instanceof PlacePredicate) {
				mPlaceNames.add(op.getName());
			}
		}
	}

	public void highlightOn() {
		
		highlightNodes();
		highlightArcs();

	}

	private void highlightArcs() {
		
		AbstractFlowRelation<?,?,?> flowRel;
		for (String placeName : mPlaceNames) {
			for (String transitionName : mTransitionNames) {
				String flowRelName = "arcTP_" + transitionName + placeName;
				flowRel = mNet.getFlowRelation(flowRelName);
				if (flowRel != null) {
					PNGraphCell cell = mArcmap.get(flowRelName);
					final mxCellMarker marker = getCellMarker(cell);
					marker.highlight(mPNEditor.getGraphComponent().getGraph().getView()
							.getState(cell), Color.RED);
				} else {
					flowRelName = "arcPT_" + placeName + transitionName;
					flowRel = mNet.getFlowRelation(flowRelName);
					if (flowRel != null) {
						PNGraphCell cell = mArcmap.get(flowRelName);
						final mxCellMarker marker = getCellMarker(cell);
						marker.highlight(mPNEditor.getGraphComponent().getGraph().getView()
								.getState(cell), Color.RED);
					}
				}
			}
		}
	}

	private void highlightNodes() {
		ArrayList<String> nodeNames = new ArrayList<String>();
		nodeNames.addAll(mPlaceNames);
		nodeNames.addAll(mTransitionNames);
		for (String node : nodeNames) {
			PNGraphCell cell = mNodemap.get(node);
			final mxCellMarker marker = getCellMarker(cell);
			marker.highlight(mPNEditor.getGraphComponent().getGraph().getView()
					.getState(cell), Color.RED);
		}
	}
	
	public void highlightOff() {
		
		for (String cell : markerReference.keySet()) {
			markerReference.get(cell).reset();
		}
		
	}
	
	private mxCellMarker getCellMarker(PNGraphCell cell) {
		
		if (!markerReference.containsKey(cell.getId())) {
			markerReference.put(cell.getId(),
					new mxCellMarker(mPNEditor.getGraphComponent()));
		}
		return markerReference.get(cell.getId());
	}

}
