package de.uni.freiburg.iig.telematik.swat.editor.graph.change;

import com.mxgraph.model.mxIGraphModel.mxAtomicGraphModelChange;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.SecurityLevel;
import de.uni.freiburg.iig.telematik.swat.editor.SwatIFNetEditorComponent;
import de.uni.freiburg.iig.telematik.swat.editor.graph.SwatIFNetGraph;
import de.uni.freiburg.iig.telematik.swat.editor.menu.IFNetToolBar;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

public class TokenSecurityLevelChange extends mxAtomicGraphModelChange {

	protected String name;
	SecurityLevel value;
	protected SecurityLevel previous;
	private SwatIFNetGraph graph;
	private SwatIFNetEditorComponent editor;

	public TokenSecurityLevelChange() {
		this(null, null, null);
	}

	public TokenSecurityLevelChange(SwatIFNetEditorComponent editor, String name, SecurityLevel sl) {
		this.editor = editor;
		this.graph = (SwatIFNetGraph) editor.getGraphComponent().getGraph();
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

	}

	protected SecurityLevel valueForCellChanged(String label, SecurityLevel value) {
		SecurityLevel oldValue = graph.getSecurityLabelForTokenlabel(label);
		graph.updateSecurityLabelForTokenlabel(label, value);

		return oldValue;
	}

}
