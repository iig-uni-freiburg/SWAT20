package de.uni.freiburg.iig.telematik.swat.editor.graph.change;

import com.mxgraph.model.mxIGraphModel.mxAtomicGraphModelChange;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.SecurityLevel;
import de.uni.freiburg.iig.telematik.swat.editor.IFNetEditor;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.graph.IFNetGraph;
import de.uni.freiburg.iig.telematik.swat.editor.menu.IFNetToolBar;

public class TokenSecurityLevelChange extends mxAtomicGraphModelChange {

	protected String name;
	SecurityLevel value;
	protected SecurityLevel previous;
	private IFNetGraph graph;
	private IFNetEditor editor;

	public TokenSecurityLevelChange() {
		this(null, null, null);
	}

	public TokenSecurityLevelChange(PNEditor editor2, String name, SecurityLevel sl) {
		this.editor = (IFNetEditor) editor2;
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
		((IFNetToolBar)editor.getEditorToolbar()).updateTokenlabelConfigurer();

	}

	protected SecurityLevel valueForCellChanged(String label, SecurityLevel value) {
		SecurityLevel oldValue = graph.getSecurityLabelForTokenlabel(label);
		graph.updateSecurityLabelForTokenlabel(label, value);

		return oldValue;
	}

}
