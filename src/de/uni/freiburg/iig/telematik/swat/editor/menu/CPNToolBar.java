package de.uni.freiburg.iig.telematik.swat.editor.menu;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.swing.Action;
import javax.swing.Box.Filler;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import com.mxgraph.model.mxGraphModel;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraphView;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.ACModel;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.PopUpToolBarAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.acmodel.AddAccessControlAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.history.RedoAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.history.UndoAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.ifanalysis.AddAnalysisContextAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.mode.EnterEditingAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.mode.EnterExecutionAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.mode.ReloadExecutionAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.mode.ToggleModeAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.nodes.NodeToolBarAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.pn.ChecKSoundnessAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.pn.CheckValidityAction;
import de.uni.freiburg.iig.telematik.swat.editor.exception.EditorToolbarException;
import de.uni.freiburg.iig.telematik.swat.editor.graph.IFNetGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.swat.editor.graph.change.AccessControlChange;
import de.uni.freiburg.iig.telematik.swat.editor.graph.change.AnalysisContextChange;
import de.uni.freiburg.iig.telematik.swat.editor.menu.toolbars.ExportToolBar;
import de.uni.freiburg.iig.telematik.swat.editor.menu.toolbars.FontToolBar;
import de.uni.freiburg.iig.telematik.swat.editor.menu.toolbars.GraphicsToolBar;
import de.uni.freiburg.iig.telematik.swat.editor.menu.toolbars.NodeToolBar;
import de.uni.freiburg.iig.telematik.swat.editor.menu.toolbars.SubjectClearanceToolBar;
import de.uni.freiburg.iig.telematik.swat.editor.menu.toolbars.TokenToolBar;
import de.uni.freiburg.iig.telematik.swat.editor.menu.toolbars.TokenlabelToolBar;
import de.uni.freiburg.iig.telematik.swat.editor.menu.toolbars.ZoomToolBar;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.TimeContext;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatState;

public class CPNToolBar extends AbstractToolBar {

	private static final long serialVersionUID = -6491749112943066366L;

	private static final int DEFAULT_TB_HEIGHT = 50;

	protected static final String NO_SELECTION = "no selection...";

	protected static final String NO_SELECTION_TIME = "no time context...";

	private JComboBox comboAnalysisContextModel = null;
	private JComboBox comboAccessControlModel = null;
	
	private JComboBox comboTimeContextModel = null;
	


	// further variables
	private PNEditor pnEditor = null;
	private boolean ignoreZoomChange = false;
	private Mode mode = Mode.EDIT;

	private enum Mode {
		EDIT, PLAY
	}

	// Actions
	// private SaveAction saveAction = null;
	private UndoAction undoAction = null;
	private RedoAction redoAction = null;
	private EnterExecutionAction enterExecutionAction;
	private EnterEditingAction enterEditingAction;
	private PopUpToolBarAction fontAction;
	private PopUpToolBarAction graphicsAction;
	private PopUpToolBarAction zoomAction;
	private NodeToolBarAction nodeAction;
	private PopUpToolBarAction exportAction;
	private ToggleModeAction toggleModeAction;
	private ReloadExecutionAction reloadExecutionAction;
	// Buttons
	// private JButton saveButton;
	private JToggleButton undoButton;
	private JToggleButton redoButton;
	private JToggleButton fontButton = null;
	private JToggleButton graphicsButton = null;
	private JButton enterExecutionButton;
	private JButton reloadExecutionButton;
	private JButton enterEditingButton;
	private JToggleButton zoomButton;
	private JToggleButton nodeButton;
	private JToggleButton exportButton;
	private JButton toggleModeButton;

	// Sub-Toolbars
	private FontToolBar fontToolbar;
	private GraphicsToolBar graphicsToolbar;
	private ExportToolBar exportToolbar;
	private ZoomToolBar zoomToolbar;
	private NodeToolBar nodeToolbar;
	private TokenToolBar tokenToolbar;

	// Tooltips
	private String executionButtonTooltip = "execution mode";
	private String editingButtonTooltip = " editing mode";
	private String fontTooltip = "font";
	private String saveButtonTooltip = "save";
	private String exportButtonTooltip = "export to pdf";
	private String undoTooltip = "undo";
	private String redoTooltip = "redo";

	private PopUpToolBarAction tokenAction;
	private JToggleButton tokenButton;
	private ChecKSoundnessAction checkSoundnessAction;
	private JToggleButton checkSoundnessButton;
	private CheckValidityAction checkValidityAction;
	private JToggleButton checkValidityButton;

	private JToggleButton transformButton;

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



	public CPNToolBar(final PNEditor pnEditor, int orientation) throws EditorToolbarException {
		super(pnEditor, orientation);
		
		



	}



	private void createToolbarActions(final PNEditor pnEditor) throws PropertyException, IOException {


			tokenToolbar = new TokenToolBar(pnEditor, JToolBar.HORIZONTAL);
			tokenAction = new PopUpToolBarAction(pnEditor, "Token", "marking", tokenToolbar);
			tokenlabelToolbar = new TokenlabelToolBar(pnEditor, JToolBar.HORIZONTAL);
			editTokenlabelAction = new PopUpToolBarAction(pnEditor, "Tokenlabel", "tokenlabel", tokenlabelToolbar);
				checkValidityAction = new CheckValidityAction(pnEditor);
				checkSoundnessAction = new ChecKSoundnessAction(pnEditor);
			
		

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

	private JComboBox getComboTimeContextModel() {
		if (comboTimeContextModel == null) {
			comboTimeContextModel = new JComboBox();
			comboTimeContextModel.setMinimumSize(new Dimension(200, 24));
			comboTimeContextModel.setPreferredSize(new Dimension(200, 24));
			comboTimeContextModel.setMaximumSize(new Dimension(200, 24));

			comboTimeContextModel.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent arg0) {
					if (arg0.getItem() instanceof TimeContext)
						SwatState.getInstance().setActiveContext(pnEditor.getNetContainer().getPetriNet().getName(),
								((TimeContext) arg0.getItem()).getName());
				}
			});

		}

		updateTimeContextModelComboBox();


		return comboTimeContextModel;
	}

	private void updateTimeContextModelComboBox() {
		DefaultComboBoxModel theModel = (DefaultComboBoxModel) comboTimeContextModel.getModel();
		theModel.removeAllElements();
		List<TimeContext> timeContexts = SwatComponents.getInstance().getTimeContexts(pnEditor.getNetContainer().getPetriNet().getName());
		theModel.addElement(NO_SELECTION_TIME);
		if (timeContexts != null && !timeContexts.isEmpty()) {
			for (TimeContext context : timeContexts) {
				theModel.addElement(context);
			}
		}
		comboTimeContextModel.repaint();
	}

	private void zoomButtonSettings() {
		final mxGraphView view = pnEditor.getGraphComponent().getGraph().getView();
		double scale = view.getScale();
		int scaleInt = (int) (scale * 100);
		zoomButton.setVerticalAlignment(SwingConstants.CENTER);
		zoomButton.setText(scaleInt + "%  ");
		zoomButton.setIconTextGap(-5);

		// Sets the zoom in the zoom combo the current value
		mxIEventListener scaleTracker = new mxIEventListener() {
			public void invoke(Object sender, mxEventObject evt) {
				ignoreZoomChange = true;

				try {
				} finally {
					ignoreZoomChange = false;
				}
				if (!ignoreZoomChange) {
					double scale = view.getScale();
					int scaleInt = (int) (scale * 100);
					zoomButton.setText(scaleInt + "%");
					zoomButton.setIconTextGap(-5);
				}
			}
		};

		// Installs the scale tracker to update the value in the combo box
		// if the zoom is changed from outside the combo box
		view.getGraph().getView().addListener(mxEvent.SCALE, scaleTracker);
		view.getGraph().getView().addListener(mxEvent.SCALE_AND_TRANSLATE, scaleTracker);

		// Invokes once to sync with the actual zoom value
		scaleTracker.invoke(null, null);
	}

	private void setButtonSettings(final JButton button) {
		button.setBorderPainted(false);
		button.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				button.setBorderPainted(false);
				super.mouseReleased(e);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				button.setBorderPainted(true);
				super.mousePressed(e);
			}

		});
	}

	private JComponent add(Action action, boolean asToggleButton) {
		if (!asToggleButton)
			return super.add(action);
		JToggleButton b = createToggleActionComponent(action);
		b.setAction(action);
		add(b);
		return b;
	}

	protected JToggleButton createToggleActionComponent(Action a) {
		JToggleButton b = new JToggleButton() {
			private static final long serialVersionUID = -3143341784881719155L;

			protected PropertyChangeListener createActionPropertyChangeListener(Action a) {
				return super.createActionPropertyChangeListener(a);
			}
		};
		if (a != null && (a.getValue(Action.SMALL_ICON) != null || a.getValue(Action.LARGE_ICON_KEY) != null)) {
			b.setHideActionText(true);
		}
		b.setHorizontalTextPosition(JButton.CENTER);
		b.setVerticalTextPosition(JButton.BOTTOM);
		b.setBorderPainted(false);
		return b;
	}

	public void updateView(Set<PNGraphCell> selectedComponents) throws EditorToolbarException {
		switch (mode) {
		case EDIT:
			fontToolbar.updateView(selectedComponents);
			try {
				graphicsToolbar.updateView(selectedComponents);
			} catch (PropertyException e) {
				throw new EditorToolbarException("Invalid Property.\nReason: " + e.getMessage());
			} catch (IOException e) {
				throw new EditorToolbarException("Invalid File Path.\nReason: " + e.getMessage());
			}
			break;
		case PLAY:
			break;

		}
	}
	


	public void setExecutionMode() {
		mode = Mode.PLAY;
		pnEditor.getGraphComponent().getGraph().clearSelection();
		pnEditor.getGraphComponent().getGraph().setExecution(true);
		pnEditor.getGraphComponent().highlightEnabledTransitions();
		toggleModeButton.setText("PLAY");
		setExecutionButtonsVisible(true);
		setEditButtonsVisible(false);

	}

	private void setEditButtonsVisible(boolean b) {
		enterEditingButton.setVisible(b);
		undoButton.setVisible(b);
		redoButton.setVisible(b);
		nodeButton.setVisible(b);
		fontButton.setVisible(b);
		graphicsButton.setVisible(b);
		if (nodeAction.getDialog() != null && nodeButton.isSelected())
			nodeAction.getDialog().setVisible(b);
		if (fontAction.getDialog() != null && fontButton.isSelected())
			fontAction.getDialog().setVisible(b);
		if (graphicsAction.getDialog() != null && graphicsButton.isSelected())
			graphicsAction.getDialog().setVisible(b);
	}

	private void setExecutionButtonsVisible(boolean b) {
		enterExecutionButton.setVisible(b);
		reloadExecutionButton.setVisible(b);
	}

	public void setEditingMode() {
		mode = Mode.EDIT;
		pnEditor.getGraphComponent().removeCellOverlays();
		pnEditor.getGraphComponent().getGraph().enterEditingMode();
		toggleModeButton.setText("EDIT");
		setEditButtonsVisible(true);
		setExecutionButtonsVisible(false);
	}

	public JButton getExecutionButton() {
		return enterExecutionButton;

	}

	public GraphicsToolBar getGraphicsToolbar() {
		// TODO Auto-generated method stub
		return graphicsToolbar;
	}

	public void updateGlobalTokenConfigurer() {
		tokenToolbar.updateView();
		tokenlabelToolbar.updateView();

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

	@Override
	protected void addNetSpecificToolbarButtons() {
		// TODO Auto-generated method stub
		
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

}
