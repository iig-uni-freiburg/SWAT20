package de.uni.freiburg.iig.telematik.swat.editor.menu;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Set;

import javax.swing.Action;
import javax.swing.Box.Filler;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraphView;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.PopUpToolBarAction;
//import de.uni.freiburg.iig.telematik.swat.editor.actions.FontAction;
//import de.uni.freiburg.iig.telematik.swat.editor.actions.SaveAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.history.RedoAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.history.UndoAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.mode.EnterEditingAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.mode.EnterExecutionAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.mode.ReloadExecutionAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.mode.ToggleModeAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.nodes.NodeToolBarAction;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;

public class ToolBar extends JToolBar {

	private static final long serialVersionUID = -6491749112943066366L;

	
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

	// Tooltips
	private String executionButtonTooltip = "execution mode";
	private String editingButtonTooltip = " editing mode";
	private String fontTooltip = "font";
	private String saveButtonTooltip = "save";
	private String exportButtonTooltip = "export to pdf";
	private String undoTooltip = "undo";
	private String redoTooltip = "redo";

	

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

		doLayout();

//		saveButton.setToolTipText(saveButtonTooltip);
		exportButton.setToolTipText(exportButtonTooltip);
		enterExecutionButton.setToolTipText(executionButtonTooltip);
		enterEditingButton.setToolTipText(editingButtonTooltip);

		undoButton.setToolTipText(undoTooltip);
		redoButton.setToolTipText(redoTooltip);
		fontButton.setToolTipText(fontTooltip );

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

}
