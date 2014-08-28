package de.uni.freiburg.iig.telematik.swat.editor.actions.text;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;
import de.uni.freiburg.iig.telematik.swat.editor.menu.FontToolBar;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;

@SuppressWarnings("serial")
public class ShowHideTokensOnArcsAction extends AbstractPNEditorAction {
	private Image visible;
	private Image invisible;
	private FontToolBar fontTB;

	public ShowHideTokensOnArcsAction(PNEditor editor) throws ParameterException, PropertyException, IOException {
		super(editor, "visible", IconFactory.getIcon("constraintsinvisible"));
		visible = getIcon().getImage();
		invisible = IconFactory.getIcon("constraintsvisible").getImage();

	}

	public void actionPerformed(ActionEvent e) {
		PNGraph graph = getEditor().getGraphComponent().getGraph();

		if (getIcon().getImage() == visible) {
			graph.setTokenOnArcVisibility(false);
			fontTB.setTokenOnArcEnabled(false);
			
		}
		else if (getIcon().getImage() == invisible) {
			graph.setTokenOnArcVisibility(true);
			fontTB.setTokenOnArcEnabled(true);

		}

	}

	public void setHideIconImage() {
		getIcon().setImage(invisible);

	}

	public void setShowIconImage() {
		getIcon().setImage(visible);
	}

	public void setFontToolbar(FontToolBar fontToolBar) {
		fontTB = fontToolBar;
		
	}

}