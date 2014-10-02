package de.uni.freiburg.iig.telematik.swat.editor.menu;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.swing.Action;
import javax.swing.Box.Filler;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxIGraphModel;
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
import de.uni.freiburg.iig.telematik.seram.accesscontrol.acl.ACLModel;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.rbac.RBACModel;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.PopUpToolBarAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.acmodel.AddAccessControlAction;
//import de.uni.freiburg.iig.telematik.swat.editor.actions.FontAction;
//import de.uni.freiburg.iig.telematik.swat.editor.actions.SaveAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.history.RedoAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.history.UndoAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.ifanalysis.AddAnalysisContextAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.ifanalysis.EditSubjectClearanceAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.ifanalysis.EditTokenlabelAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.mode.EnterEditingAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.mode.EnterExecutionAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.mode.ReloadExecutionAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.mode.ToggleModeAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.nodes.NodeToolBarAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.pn.ChecKSoundnessAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.pn.CheckValidityAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.pn.TransformCPNtoCWNAction;
import de.uni.freiburg.iig.telematik.swat.editor.graph.IFNetGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.swat.editor.graph.change.AnalysisContextChange;
import de.uni.freiburg.iig.telematik.swat.editor.graph.change.TokenColorChange;
import de.uni.freiburg.iig.telematik.swat.editor.menu.acmodel.SWATACModelDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;

public class ToolBar extends JToolBar {

	private static final long serialVersionUID = -6491749112943066366L;


	private static final int DEFAULT_TB_HEIGHT = 50;

	protected static final String NO_SELECTION = "no selection...";
	
	private JComboBox comboAnalysisContextModel = null;

	
	// further variables
	private PNEditor pnEditor = null;
	private boolean ignoreZoomChange = false;
	private Mode mode = Mode.EDIT;
	private enum Mode {
		EDIT, PLAY
	}

	// Actions
//	private SaveAction saveAction = null;
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
//	private JButton saveButton;
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
	
	//Sub-Toolbars
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


	private TransformCPNtoCWNAction transformAction;


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


	private JComboBox acSelectionBox;


	private ItemListener il;	

	public ToolBar(final PNEditor pnEditor, int orientation) throws ParameterException {
		super(orientation);
		Validate.notNull(pnEditor);
		// setLayout(new WrapLayout(FlowLayout.LEFT));
		this.pnEditor = pnEditor;

		try {
//			saveAction = new SaveAction(pnEditor);
			exportToolbar = new ExportToolBar(pnEditor, JToolBar.HORIZONTAL);
			exportAction = new PopUpToolBarAction(pnEditor, "Export", "export", exportToolbar);

			toggleModeAction = new ToggleModeAction(pnEditor);
			enterExecutionAction = new EnterExecutionAction(pnEditor);
			reloadExecutionAction = new ReloadExecutionAction(pnEditor);
			enterEditingAction = new EnterEditingAction(pnEditor);

			undoAction = new UndoAction(pnEditor);
			redoAction = new RedoAction(pnEditor);

			nodeToolbar = new NodeToolBar(pnEditor, JToolBar.HORIZONTAL);
			nodeAction = new NodeToolBarAction(pnEditor, "Node", nodeToolbar);

			fontToolbar = new FontToolBar(pnEditor, JToolBar.HORIZONTAL);
			fontAction = new PopUpToolBarAction(pnEditor, "Font", "text", fontToolbar);

			graphicsToolbar = new GraphicsToolBar(pnEditor, JToolBar.HORIZONTAL);
			graphicsAction = new PopUpToolBarAction(pnEditor, "Graphics", "bg_color", graphicsToolbar);

			zoomToolbar = new ZoomToolBar(pnEditor, JToolBar.HORIZONTAL);
			zoomAction = new PopUpToolBarAction(pnEditor, "Zoom", "zoom_in", zoomToolbar);
			if (pnEditor.getGraphComponent().getGraph().getNetContainer().getPetriNet().getNetType() == NetType.CPN
					|| pnEditor.getGraphComponent().getGraph().getNetContainer().getPetriNet().getNetType() == NetType.IFNet) {
				tokenToolbar = new TokenToolBar(pnEditor, JToolBar.HORIZONTAL);
				tokenAction = new PopUpToolBarAction(pnEditor, "Token", "marking", tokenToolbar);
				if (pnEditor.getGraphComponent().getGraph().getNetContainer().getPetriNet().getNetType() == NetType.CPN) {
				checkValidityAction = new CheckValidityAction(pnEditor);
				checkSoundnessAction = new ChecKSoundnessAction(pnEditor);
				transformAction = new TransformCPNtoCWNAction(pnEditor);}

			}

			if (pnEditor.getGraphComponent().getGraph().getNetContainer().getPetriNet().getNetType() == NetType.IFNet) {

				addAccessControlAction = new AddAccessControlAction(pnEditor);
				addAnalysisContextAction = new AddAnalysisContextAction(pnEditor);
//				editTokenlabelAction = new EditTokenlabelAction(pnEditor);
				tokenlabelToolbar = new TokenlabelToolBar(pnEditor, JToolBar.HORIZONTAL);
				editTokenlabelAction = new PopUpToolBarAction(pnEditor, "Tokenlabel", "tokenlabel", tokenlabelToolbar);
				editSubjectClearanceToolbar = new SubjectClearanceToolBar(pnEditor, JToolBar.HORIZONTAL);
				editSubjectClearanceAction = new PopUpToolBarAction(pnEditor, "Edit Clearance", "user_shield", editSubjectClearanceToolbar);
//				editSubjectClearanceAction = new EditSubjectClearanceAction(pnEditor);

			}
			setFloatable(false);

		} catch (PropertyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

//		saveButton = add(saveAction);
//		setButtonSettings(saveButton);
		
		exportButton = (JToggleButton) add(exportAction, true);
		exportAction.setButton(exportButton);

		addSeparator();

		toggleModeButton = add(toggleModeAction);
		toggleModeButton.setBorderPainted(false);
		toggleModeButton.setIconTextGap(0);
		toggleModeButton.setText("EDIT");
		enterExecutionButton = add(enterExecutionAction);
		setButtonSettings(enterExecutionButton);

		enterEditingButton = add(enterEditingAction);
		setButtonSettings(enterEditingButton);

		add(new Filler(new Dimension(0, 0), new Dimension(20, 0), new Dimension(30, 0)));
		reloadExecutionButton = add(reloadExecutionAction);
		setButtonSettings(reloadExecutionButton);
		setExecutionButtonsVisible(false);

		undoButton = (JToggleButton) add(undoAction, true);
		redoButton = (JToggleButton) add(redoAction, true);

		nodeButton = (JToggleButton) add(nodeAction, true);

		nodeAction.setButton(nodeButton);

		fontButton = (JToggleButton) add(fontAction, true);
		fontAction.setButton(fontButton);

		graphicsButton = (JToggleButton) add(graphicsAction, true);
		graphicsAction.setButton(graphicsButton);

		zoomButton = (JToggleButton) add(zoomAction, true);
		zoomButtonSettings();

		zoomAction.setButton(zoomButton);
		
	if(tokenAction != null){
		tokenButton = (JToggleButton) add(tokenAction, true);
	
		tokenAction.setButton(tokenButton);
		
		checkValidityButton = (JToggleButton) add(checkValidityAction, true);
		checkSoundnessButton = (JToggleButton) add(checkSoundnessAction, true);
		transformButton = (JToggleButton) add(transformAction, true);
		
		
	}
	
	if(addAnalysisContextAction != null){
		addSeparator();
		addAccessControlbutton = (JToggleButton) add(addAccessControlAction, true);

		addAnalysisContextbutton = (JToggleButton) add(addAnalysisContextAction, true);
		acSelectionBox = getComboAnalysisContextModel();
		add(acSelectionBox);

		editTokenlabelButton = (JToggleButton) add(editTokenlabelAction, true);
		editTokenlabelAction.setButton(editTokenlabelButton);
		editSubjectClearanceButton = (JToggleButton) add(editSubjectClearanceAction, true);
		editSubjectClearanceAction.setButton(editSubjectClearanceButton);
	}
	
	
		doLayout();


		exportButton.setToolTipText(exportButtonTooltip);
		enterExecutionButton.setToolTipText(executionButtonTooltip);
		enterEditingButton.setToolTipText(editingButtonTooltip);

		undoButton.setToolTipText(undoTooltip);
		redoButton.setToolTipText(redoTooltip);
		fontButton.setToolTipText(fontTooltip );

	}

	private JComboBox getComboAnalysisContextModel() {
		if (comboAnalysisContextModel == null) {
			comboAnalysisContextModel = new JComboBox();
			comboAnalysisContextModel.setBounds(102, 78, 190, 27);

			updateAnalysisContextModelComboBox(null);

			comboAnalysisContextModel.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {
					System.out.println("Source: " + e.getSource());
					System.out.println(!e.getItem().toString().contentEquals(NO_SELECTION));
					try {
						String analysisContextModelName = null;
						if (comboAnalysisContextModel.getSelectedItem() != null)
							analysisContextModelName = comboAnalysisContextModel.getSelectedItem().toString();
						AnalysisContext anyalysisContextModel;
						if (analysisContextModelName != null && !analysisContextModelName.contentEquals(NO_SELECTION)) {
							PNGraph graph = pnEditor.getGraphComponent().getGraph();
							if (graph instanceof IFNetGraph) {
								anyalysisContextModel = SwatComponents.getInstance().getIFAnalysisContextForNetWithName(pnEditor.getNetContainer().getPetriNet().getName(), analysisContextModelName);

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

	@SuppressWarnings("rawtypes")
	private void updateAnalysisContextModelComboBox(String modelName){
		DefaultComboBoxModel theModel = (DefaultComboBoxModel) comboAnalysisContextModel.getModel();
		theModel.removeAllElements();
		List<AnalysisContext> acModels = SwatComponents.getInstance().getIFAnalysisContextForNet(pnEditor.getNetContainer().getPetriNet().getName());
		theModel.addElement(NO_SELECTION);
		if(acModels != null){
		for(AnalysisContext acModel: acModels){
			System.out.println(acModel);
			if(acModel != null)
				theModel.addElement(acModel.getName());
		}
		}
		if(modelName != null){
			comboAnalysisContextModel.setSelectedItem(modelName);
		}

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

	public void updateView(Set<PNGraphCell> selectedComponents) {
		switch (mode) {
		case EDIT:
			fontToolbar.updateView(selectedComponents);
			graphicsToolbar.updateView(selectedComponents);
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
		if(nodeAction.getDialog()!=null && nodeButton.isSelected())
		nodeAction.getDialog().setVisible(b);
		if(fontAction.getDialog()!=null && fontButton.isSelected())
		fontAction.getDialog().setVisible(b);
		if(graphicsAction.getDialog()!=null && graphicsButton.isSelected())
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


}
