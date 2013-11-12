package de.unifreiburg.iig.bpworkbench2.editor.graph;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;

public class mxPlace extends mxCell {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8812064259672166289L;
	private boolean isPlace;

	public mxPlace(Object value, mxGeometry geometry, String style) {
		setValue(value);
		setGeometry(geometry);
		setStyle(style);
		isPlace(true);
	}

	private void isPlace(boolean b) {
		isPlace = b	;
	}
}
