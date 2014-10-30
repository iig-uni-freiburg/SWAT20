package de.uni.freiburg.iig.telematik.swat.editor.graph.change;

import java.awt.Color;

import com.mxgraph.model.mxIGraphModel.mxAtomicGraphModelChange;

import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;

public class TokenColorChange extends mxAtomicGraphModelChange {

	protected String name;
	Color value;
	protected Color previous;
	private PNGraph graph;
	private PNEditor editor;

	public TokenColorChange() {
		this(null, null, null);
	}

	public TokenColorChange(PNEditor editor, String name, Color color) {
		this.editor = editor;
		this.graph = editor.getGraphComponent().getGraph();
		this.name = name;
		this.value = color;
		this.previous = this.value;
	}

	public void setCell(String value) {
		name = value;
	}

	public Object getName() {
		return name;
	}

	public void setValue(Color value) {
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	public void setPrevious(Color value) {
		previous = value;
	}

	public Object getPrevious() {
		return previous;
	}

	public void execute() {
		value = previous;
		previous = valueForCellChanged(name, previous);
		editor.getEditorToolbar().updateGlobalTokenConfigurer();
		editor.getGraphComponent().getGraph().updateViews();
	}

	protected Color valueForCellChanged(String name, Color value) {
		Color oldValue = graph.getTokenColorForName(name);

		graph.updateTokenColor(name, value);

		return oldValue;
	}

}
