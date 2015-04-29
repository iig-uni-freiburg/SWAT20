package de.uni.freiburg.iig.telematik.swat.editor.graph.change;

import com.mxgraph.model.mxIGraphModel.mxAtomicGraphModelChange;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.SecurityLevel;
import de.uni.freiburg.iig.telematik.swat.editor.menu.IFNetToolBar;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.IFNetEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.graph.IFNetGraph;

public class TokenSecurityLevelChange extends mxAtomicGraphModelChange {

	protected String name;
	SecurityLevel value;
	protected SecurityLevel previous;
	private IFNetGraph graph;
	private IFNetEditorComponent editor;

	public TokenSecurityLevelChange() {
		this(null, null, null);
	}

	public TokenSecurityLevelChange(PNEditorComponent editor2, String name, SecurityLevel sl) {
		this.editor = (IFNetEditorComponent) editor2;
		this.graph = (IFNetGraph) editor2.getGraphComponent().getGraph();
		this.name = name;
		this.value = sl;
		this.previous = this.value;
	}

	public void setCell(String value) {
		name = value;
	}
	
	public Object getName() {
		return name;
	}

	public void setValue(SecurityLevel value) {
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	public void setPrevious(SecurityLevel value) {
		previous = value;
	}

	public Object getPrevious() {
		return previous;
	}

	public void execute() {
		value = previous;
		previous = valueForCellChanged(name, previous);
		//TODO: Where to update now?
//		((IFNetToolBar)editor.getEditorToolbar()).updateTokenlabelConfigurer();

	}

	protected SecurityLevel valueForCellChanged(String label, SecurityLevel value) {
		//TODO: Adapt to new ACStructure 
//		SecurityLevel oldValue = graph.getSecurityLabelForTokenlabel(label);
//		graph.updateSecurityLabelForTokenlabel(label, value);
//
//		return oldValue;
		return null;
	}

}
