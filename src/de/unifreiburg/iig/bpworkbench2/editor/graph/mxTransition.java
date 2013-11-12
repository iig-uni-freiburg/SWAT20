package de.unifreiburg.iig.bpworkbench2.editor.graph;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;

public class mxTransition extends mxCell {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8812064259672166289L;
	private boolean isTransition;

	public mxTransition(Object value, mxGeometry geometry, String style) {
		setValue(value);
		setGeometry(geometry);
		setStyle(style);
		isTransition(true);
	}

	private void isTransition(boolean b) {
		isTransition = b	;
	}
}