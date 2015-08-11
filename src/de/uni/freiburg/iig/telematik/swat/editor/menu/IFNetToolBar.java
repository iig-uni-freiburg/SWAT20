package de.uni.freiburg.iig.telematik.swat.editor.menu;

import java.io.IOException;

import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.actions.IFNetContextAbstractAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.acmodel.EditAccessControlAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.ifanalysis.EditAnalysisContextAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.ifanalysis.EditLabelingAction;
import de.uni.freiburg.iig.telematik.swat.editor.menu.toolbars.SubjectClearanceToolBar;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.wolfgang.actions.PopUpToolBarAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.exception.EditorToolbarException;
import de.uni.freiburg.iig.telematik.wolfgang.menu.AbstractToolBar;

public class IFNetToolBar extends AbstractToolBar {

	private EditAnalysisContextAction editAnalysisContextAction;
	private EditAccessControlAction editAccessControlAction;
	private PopUpToolBarAction editSubjectClearanceAction;
	private JToggleButton editSubjectClearanceButton;
	private SubjectClearanceToolBar editSubjectClearanceToolbar;
//	private TokenToolBar tokenToolbar;
	private PopUpToolBarAction tokenAction;
	private EditLabelingAction editLabelingAction;
	private JToggleButton tokenButton;


	public IFNetToolBar(final PNEditorComponent pnEditor, int orientation) throws EditorToolbarException {
		super(pnEditor, orientation);

	}

	@Override
	protected void createAdditionalToolbarActions(PNEditorComponent pnEditor) throws ParameterException, PropertyException, IOException {

//		tokenToolbar = new TokenToolBar(pnEditor, JToolBar.HORIZONTAL);
//		tokenAction = new PopUpToolBarAction(pnEditor, "Token", IconFactory.getIcon("marking"), tokenToolbar);

		editAccessControlAction = new EditAccessControlAction(pnEditor);

		editAnalysisContextAction = new EditAnalysisContextAction(pnEditor);
		editLabelingAction = new EditLabelingAction(pnEditor);

		editSubjectClearanceToolbar = new SubjectClearanceToolBar(pnEditor, JToolBar.HORIZONTAL);
		editSubjectClearanceAction = new PopUpToolBarAction(pnEditor, "Edit Clearance", IconFactory.getIcon("user_shield"), editSubjectClearanceToolbar);
	}

	@Override
	protected void addNetSpecificToolbarButtons() {
		if (tokenAction != null) {
			tokenButton = (JToggleButton) add(tokenAction, true);
			tokenAction.setButton(tokenButton);
		}

		if (editAnalysisContextAction != null) {
			addSeparator();

			addIFContextAction(editAccessControlAction, null, editAnalysisContextAction);

			addIFContextAction(editAnalysisContextAction, editAccessControlAction, editLabelingAction);

			addIFContextAction(editLabelingAction, editAnalysisContextAction, null);

			editSubjectClearanceButton = (JToggleButton) add(editSubjectClearanceAction, true);
			editSubjectClearanceAction.setButton(editSubjectClearanceButton);
			editLabelingAction.setSubjectClearanceButton(editSubjectClearanceButton);

			
			editAccessControlAction.setSelectedModel(null);
		}

	}

	private void addIFContextAction(IFNetContextAbstractAction ifContextAction, IFNetContextAbstractAction previousAction, IFNetContextAbstractAction followingAction) {
		JToggleButton ifContextbutton = (JToggleButton) add(ifContextAction, true);
		ifContextAction.setPreceedingDependency(previousAction);
		ifContextAction.setFollowingDependency(followingAction);
		ifContextAction.setButton(ifContextbutton);
	}

	public void updateSubjectClearanceConfigurer() {
		editSubjectClearanceToolbar.updateView();
	}

	@Override
	protected void setNetSpecificButtonsVisible(boolean b) {

	}

	@Override
	protected JToolBar createPropertyCheckToolbar() throws ParameterException, PropertyException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
