package de.uni.freiburg.iig.telematik.swat.editor.menu;

import java.io.IOException;

import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.PopUpToolBarAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.pn.ChecKSoundnessAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.pn.CheckValidityAction;
import de.uni.freiburg.iig.telematik.swat.editor.exception.EditorToolbarException;
import de.uni.freiburg.iig.telematik.swat.editor.menu.toolbars.TokenToolBar;
import de.uni.freiburg.iig.telematik.swat.editor.menu.toolbars.TokenlabelToolBar;

public class CPNToolBar extends AbstractToolBar {
	
	// further variables
	private PNEditor pnEditor = null;
	private boolean ignoreZoomChange = false;
	private Mode mode = Mode.EDIT;

	private enum Mode {
		EDIT, PLAY
	}
	private TokenToolBar tokenToolbar;
	private TokenlabelToolBar tokenlabelToolbar;
	private PopUpToolBarAction tokenAction;
	private PopUpToolBarAction editTokenlabelAction;
	private CheckValidityAction checkValidityAction;
	private ChecKSoundnessAction checkSoundnessAction;
	private JToggleButton tokenButton;
	private JToggleButton checkValidityButton;
	private JToggleButton checkSoundnessButton;

	public CPNToolBar(final PNEditor pnEditor, int orientation) throws EditorToolbarException {
		super(pnEditor, orientation);
	}
	
	@Override
	protected void addNetSpecificToolbarButtons() {
		tokenButton = (JToggleButton) add(tokenAction, true);

		tokenAction.setButton(tokenButton);

		checkValidityButton = (JToggleButton) add(checkValidityAction, true);
		checkSoundnessButton = (JToggleButton) add(checkSoundnessAction, true);
		
	}

	@Override
	protected void createAdditionalToolbarActions(PNEditor pnEditor) {
		try {
		if (pnEditor.getGraphComponent().getGraph().getNetContainer().getPetriNet().getNetType() == NetType.CPN
				|| pnEditor.getGraphComponent().getGraph().getNetContainer().getPetriNet().getNetType() == NetType.IFNet) {
			tokenToolbar = new TokenToolBar(pnEditor, JToolBar.HORIZONTAL);

				tokenAction = new PopUpToolBarAction(pnEditor, "Token", "marking", tokenToolbar);

			tokenlabelToolbar = new TokenlabelToolBar(pnEditor, JToolBar.HORIZONTAL);
			editTokenlabelAction = new PopUpToolBarAction(pnEditor, "Tokenlabel", "tokenlabel", tokenlabelToolbar);
			if (pnEditor.getGraphComponent().getGraph().getNetContainer().getPetriNet().getNetType() == NetType.CPN) {
				checkValidityAction = new CheckValidityAction(pnEditor);
				checkSoundnessAction = new ChecKSoundnessAction(pnEditor);
			}
		}
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void updateGlobalTokenConfigurer() {
		tokenToolbar.updateView();
	}

}
