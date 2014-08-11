package de.uni.freiburg.iig.telematik.swat.editor.menu;

import javax.swing.JPanel;
import javax.swing.JToolBar;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;

public class NodeToolBar extends JToolBar {

	private static final long serialVersionUID = -6491749112943066366L;
	protected NodePalettePanel palettePanel = null;

	public NodeToolBar(final PNEditor pnEditor, int orientation) throws ParameterException {
		super(orientation);
		Validate.notNull(pnEditor);
		setFloatable(false);
		add(getPalettePanel());
		
	}
	
	private JPanel getPalettePanel() throws ParameterException {
		if (palettePanel == null) {
			palettePanel = new NodePalettePanel();
		}
		return palettePanel;
	}

}
