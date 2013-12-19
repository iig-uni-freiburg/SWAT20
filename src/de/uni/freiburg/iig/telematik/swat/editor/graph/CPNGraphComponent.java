package de.uni.freiburg.iig.telematik.swat.editor.graph;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import de.uni.freiburg.iig.telematik.swat.editor.CPNEditor;

public class CPNGraphComponent extends PNGraphComponent {

	private static final long serialVersionUID = -1698182711658593407L;

	public CPNGraphComponent(CPNGraph cpnGraph, CPNEditor cpnEditor) {
		super(cpnGraph, cpnEditor);
	}

	@Override
	public CPNGraph getGraph() {
		return (CPNGraph) super.getGraph();
	}

	@Override
	protected boolean doubleClickOnPlace(PNGraphCell cell, MouseEvent e) {
		// TODO Table which lists constraints
		return false;
	}

	@Override
	protected boolean doubleClickOnArc(PNGraphCell cell, MouseEvent e) {
		// TODO Table which lists constraints
		return false;
	}

	@Override
	protected boolean mouseWheelOnPlace(PNGraphCell cell, MouseWheelEvent e) {
		// TODO decrementing or incrementing tokennumber of selected color with
		// mousewheel
		return false;
	}

}
