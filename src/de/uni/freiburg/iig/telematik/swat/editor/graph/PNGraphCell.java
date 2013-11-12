package de.uni.freiburg.iig.telematik.swat.editor.graph;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;

import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperties.PNComponent;

public class PNGraphCell extends mxCell{
	
	private static final long serialVersionUID = -7473579924136509825L;
	
	private PNComponent type = null;
	
	public PNGraphCell(PNComponent type) {
		super();
		this.type = type;
	}

	public PNGraphCell(Object value, mxGeometry geometry, String style, PNComponent type) {
		super(value, geometry, style);
		this.type = type;
	}

	public PNGraphCell(Object value, PNComponent type) {
		super(value);
		this.type = type;
	}
	
	public PNComponent getType(){
		return type;
	}

}
