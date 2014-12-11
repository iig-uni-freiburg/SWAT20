package de.uni.freiburg.iig.telematik.swat.editor.menu;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import com.mxgraph.model.mxGraphModel;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.ACModel;
import de.uni.freiburg.iig.telematik.swat.editor.actions.acmodel.AddAccessControlAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.ifanalysis.AddAnalysisContextAction;
import de.uni.freiburg.iig.telematik.swat.editor.graph.IFNetGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.change.AccessControlChange;
import de.uni.freiburg.iig.telematik.swat.editor.graph.change.AnalysisContextChange;
import de.uni.freiburg.iig.telematik.swat.editor.menu.toolbars.SubjectClearanceToolBar;
import de.uni.freiburg.iig.telematik.swat.editor.menu.toolbars.TokenlabelToolBar;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;
import de.uni.freiburg.iig.telematik.wolfgang.actions.pn.ChecKSoundnessAction;
import de.uni.freiburg.iig.telematik.wolfgang.actions.pn.CheckValidityAction;
import de.uni.freiburg.iig.telematik.wolfgang.exception.EditorToolbarException;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraph;
import de.uni.freiburg.iig.telematik.wolfgang.menu.AbstractToolBar;
import de.uni.freiburg.iig.telematik.wolfgang.menu.toolbars.TokenToolBar;

public class IFNetToolBar extends AbstractToolBar {


	private static final int DEFAULT_TB_HEIGHT = 50;

	protected static final String NO_SELECTION = "no selection...";

	protected static final String NO_SELECTION_TIME = "no time context...";

	private JComboBox comboAnalysisContextModel = null;
	private JComboBox comboAccessControlModel = null;

	private TokenToolBar tokenToolbar;



	private PopUpToolBarAction tokenAction;
	private JToggleButton tokenButton;
	private ChecKSoundnessAction checkSoundnessAction;
	private CheckValidityAction checkValidityAction;


	private AddAnalysisContextAction addAnalysisContextAction;

	private JToggleButton addAnalysisContextbutton;

	private AddAccessControlAction addAccessControlAction;

	private JToggleButton addAccessControlbutton;

	private PopUpToolBarAction editSubjectClearanceAction;

	private JToggleButton editSubjectClearanceButton;

	private PopUpToolBarAction editTokenlabelAction;

	private JToggleButton editTokenlabelButton;

	private TokenlabelToolBar tokenlabelToolbar;

	private SubjectClearanceToolBar editSubjectClearanceToolbar;

	private JComboBox acoSelectionBox;

	private ItemListener il;

	private Object acSelectionBox;

	private JLabel linkLabel;
	

	private String linkLabelTooltip = "Shows if Access Control Model is in sync with Analysis Context";



	public IFNetToolBar(final PNEditor pnEditor, int orientation) throws EditorToolbarException {
		super(pnEditor, orientation);

	}

	private JLabel getLinkLabel() {
		ImageIcon link = null;
		try {
			link = IconFactory.getIcon("link");
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
		JLabel label = new JLabel(link);
		label.setToolTipText(linkLabelTooltip);
		return label;
	}


	private JComboBox getComboAnalysisContextModel() {
		if (comboAnalysisContextModel == null) {
			comboAnalysisContextModel = new JComboBox();
			comboAnalysisContextModel.setBounds(102, 78, 190, 27);

			updateAnalysisContextModelComboBox(null);

			comboAnalysisContextModel.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {
					try {
						String analysisContextModelName = null;
						if (comboAnalysisContextModel.getSelectedItem() != null)
							analysisContextModelName = comboAnalysisContextModel.getSelectedItem().toString();
						AnalysisContext anyalysisContextModel;
						if (analysisContextModelName != null && !analysisContextModelName.contentEquals(NO_SELECTION)) {
							PNGraph graph = pnEditor.getGraphComponent().getGraph();
							if (graph instanceof IFNetGraph) {
								anyalysisContextModel = SwatComponents.getInstance().getAnalysisContext(pnEditor.getNetContainer().getPetriNet().getName(), analysisContextModelName);

								((mxGraphModel) pnEditor.getGraphComponent().getGraph().getModel()).execute(new AnalysisContextChange(pnEditor, anyalysisContextModel));

							}

						} else {
							anyalysisContextModel = null;
							((mxGraphModel) pnEditor.getGraphComponent().getGraph().getModel()).execute(new AnalysisContextChange(pnEditor, null));

						}
					} catch (ParameterException e1) {
						JOptionPane.showMessageDialog(pnEditor.getGraphComponent(), "Cannot update view.", "Internal Exception", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
			});
		}
		comboAnalysisContextModel.setMinimumSize(new Dimension(200, 24));
		comboAnalysisContextModel.setPreferredSize(new Dimension(200, 24));
		comboAnalysisContextModel.setMaximumSize(new Dimension(200, 24));
		return comboAnalysisContextModel;
	}
	
	private JComboBox getComboAccessControlModel() {
		if (comboAccessControlModel == null) {
			comboAccessControlModel = new JComboBox();
			comboAccessControlModel.setBounds(102, 78, 190, 27);

			updateAccessControlModelComboBox(null);

			comboAccessControlModel.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {
					try {
						String accessControlModelName = null;
						if (comboAccessControlModel.getSelectedItem() != null)
							accessControlModelName = comboAccessControlModel.getSelectedItem().toString();
						ACModel accessControlModel;
						if (accessControlModelName != null && !accessControlModelName.contentEquals(NO_SELECTION)) {
							PNGraph graph = pnEditor.getGraphComponent().getGraph();
							if (graph instanceof IFNetGraph) {
								accessControlModel = SwatComponents.getInstance().getACModel(accessControlModelName);

								((mxGraphModel) pnEditor.getGraphComponent().getGraph().getModel()).execute(new AccessControlChange(pnEditor, accessControlModel));

							}

						} else {
							accessControlModel = null;
							((mxGraphModel) pnEditor.getGraphComponent().getGraph().getModel()).execute(new AccessControlChange(pnEditor, null));

						}
					} catch (ParameterException e1) {
						JOptionPane.showMessageDialog(pnEditor.getGraphComponent(), "Cannot update view.", "Internal Exception", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
			});
		}
		comboAccessControlModel.setMinimumSize(new Dimension(200, 24));
		comboAccessControlModel.setPreferredSize(new Dimension(200, 24));
		comboAccessControlModel.setMaximumSize(new Dimension(200, 24));
		return comboAccessControlModel;
	}

	@SuppressWarnings("rawtypes")
	private void updateAnalysisContextModelComboBox(String modelName) {
		DefaultComboBoxModel theModel = (DefaultComboBoxModel) comboAnalysisContextModel.getModel();
		theModel.removeAllElements();
		List<AnalysisContext> acModels = SwatComponents.getInstance().getAnalysisContexts(pnEditor.getNetContainer().getPetriNet().getName());
		theModel.addElement(NO_SELECTION);
		if (acModels != null) {
			for (AnalysisContext acModel : acModels) {
				if (acModel != null)
					theModel.addElement(acModel.getName());
			}
		}
		if (modelName != null) {
			comboAnalysisContextModel.setSelectedItem(modelName);
		}

	}
	
	@SuppressWarnings("rawtypes")
	private void updateAccessControlModelComboBox(String modelName) {
		DefaultComboBoxModel theModel = (DefaultComboBoxModel) comboAccessControlModel.getModel();
		theModel.removeAllElements();
		 Collection<ACModel> acModels = SwatComponents.getInstance().getACModels();
		theModel.addElement(NO_SELECTION);
		if (acModels != null) {
			for (ACModel acModel : acModels) {
				if (acModel != null)
					theModel.addElement(acModel.getName());
			}
		}
		if (modelName != null) {
			comboAccessControlModel.setSelectedItem(modelName);
		}

	}




	
	@Override
	protected void addNetSpecificToolbarButtons() {
		if (tokenAction != null) {
			tokenButton = (JToggleButton) add(tokenAction, true);

			tokenAction.setButton(tokenButton);

//			checkValidityButton = (JToggleButton) add(checkValidityAction, true);
//			checkSoundnessButton = (JToggleButton) add(checkSoundnessAction, true);

		}

		if (addAnalysisContextAction != null) {
			addSeparator();
			addAccessControlbutton = (JToggleButton) add(addAccessControlAction, true);
			acoSelectionBox = getComboAccessControlModel();
			add(acoSelectionBox);
			linkLabel = getLinkLabel();
			add(linkLabel);
			addAnalysisContextbutton = (JToggleButton) add(addAnalysisContextAction, true);
			acoSelectionBox = getComboAnalysisContextModel();
			add(acoSelectionBox);

			editTokenlabelButton = (JToggleButton) add(editTokenlabelAction, true);
			editTokenlabelAction.setButton(editTokenlabelButton);
			editSubjectClearanceButton = (JToggleButton) add(editSubjectClearanceAction, true);
			editSubjectClearanceAction.setButton(editSubjectClearanceButton);
		}
		
	}

	@Override
	protected void createAdditionalToolbarActions(PNEditor pnEditor) {
		
			try {
				tokenToolbar = new TokenToolBar(pnEditor, JToolBar.HORIZONTAL);
				tokenAction = new PopUpToolBarAction(pnEditor, "Token", "marking", tokenToolbar);
			tokenlabelToolbar = new TokenlabelToolBar(pnEditor, JToolBar.HORIZONTAL);
			editTokenlabelAction = new PopUpToolBarAction(pnEditor, "Tokenlabel", "tokenlabel", tokenlabelToolbar);
			addAccessControlAction = new AddAccessControlAction(pnEditor);
			addAnalysisContextAction = new AddAnalysisContextAction(pnEditor);
			tokenlabelToolbar = new TokenlabelToolBar(pnEditor, JToolBar.HORIZONTAL);
			editTokenlabelAction = new PopUpToolBarAction(pnEditor, "Tokenlabel", "tokenlabel", tokenlabelToolbar);
			editSubjectClearanceToolbar = new SubjectClearanceToolBar(pnEditor, JToolBar.HORIZONTAL);
			editSubjectClearanceAction = new PopUpToolBarAction(pnEditor, "Edit Clearance", "user_shield", editSubjectClearanceToolbar);
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
	
	public void updateTokenlabelConfigurer() {
		tokenlabelToolbar.updateView();
	}

	public void updateSubjectClearanceConfigurer() {
		editSubjectClearanceToolbar.updateView();
	}

	public void addAnalysisContextToComboBox(String name) {
		comboAnalysisContextModel.addItem(name);
		comboAnalysisContextModel.setSelectedItem(name);
	}
	

}
