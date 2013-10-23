package de.unifreiburg.iig.bpworkbench2.editor.gui;

import javax.swing.JPanel;

import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.unifreiburg.iig.bpworkbench2.editor.soul.Graph;

public class CPNEditor extends PTNEditor {

	public CPNEditor(AbstractGraphicalPN<?, ?, ?, ?, ?> netContainer2) {
		super("PetriNet Editor", new GraphComponent(new Graph()));
//		super.netContainer = netContainer2;
//		System.out.println(netContainer2);
		graphComponent = visualizeGraph(netContainer2, graphComponent);
		
	}
}
