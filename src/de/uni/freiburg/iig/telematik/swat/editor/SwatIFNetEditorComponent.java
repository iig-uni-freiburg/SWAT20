package de.uni.freiburg.iig.telematik.swat.editor;

import javax.swing.JToolBar;

import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalIFNet;
import de.uni.freiburg.iig.telematik.swat.editor.menu.IFNetToolBar;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.IFNetEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.exception.EditorToolbarException;
import de.uni.freiburg.iig.telematik.wolfgang.menu.AbstractToolBar;

public class SwatIFNetEditorComponent extends IFNetEditorComponent {

	private static final long serialVersionUID = 3873685137931170597L;

	public SwatIFNetEditorComponent() {
		super();
	}

	public SwatIFNetEditorComponent(GraphicalIFNet netContainer, boolean askForLayout) {
		super(netContainer, askForLayout);
	}

	public SwatIFNetEditorComponent(GraphicalIFNet netContainer, LayoutOption layoutOption) {
		super(netContainer, layoutOption);
	}

	public SwatIFNetEditorComponent(GraphicalIFNet netContainer) {
		super(netContainer);
	}

	@Override
	protected AbstractToolBar createNetSpecificToolbar() throws EditorToolbarException {
		return new IFNetToolBar(this, JToolBar.HORIZONTAL);
	}
	
}
