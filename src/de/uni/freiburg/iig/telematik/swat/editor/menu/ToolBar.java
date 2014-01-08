package de.uni.freiburg.iig.telematik.swat.editor.menu;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.TransferHandler;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxResources;
import com.mxgraph.view.mxGraphView;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.FillImageAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.FontAlignCenterAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.FontAlignLeftAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.FontAlignRightAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.FillBackgroundColorAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.FontBoldStyleAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.CopyAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.CutAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.FillGradientColorAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.FillGradientDirectionAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.FontItalicStyleAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.LineCurveAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.LineStyleAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.FontLineThroughStyleAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.PasteAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.RedoAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.SaveAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.ShowHideLabelsAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.LineStrokeColorAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.FontRotationAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.FontUnderlineStyleAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.UndoAction;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperties.PNComponent;

public class ToolBar extends JToolBar {

	private static final long serialVersionUID = -6491749112943066366L;

	private boolean ignoreZoomChange = false;
	
	private SaveAction saveAction = null;
	private CutAction cutAction = null;
	private CopyAction copyAction = null;
	private PasteAction pasteAction = null;
	private UndoAction undoAction = null;
	private RedoAction redoAction = null;
	private JComboBox fontBox = null;
	private JComboBox fontSizeBox = null;
	private JComboBox zoomBox = null;
	
	private PNEditor pnEditor = null;
	
	private PNGraphCell selectedCell = null;

	private FontBoldStyleAction boldFontAction;
	private JToggleButton boldFontButton = null;

	private FontItalicStyleAction italicFontAction;
	private JToggleButton italicFontButton = null;

	private FontUnderlineStyleAction underlineFontAction;
	private JToggleButton underlineFontButton = null;

	private FontLineThroughStyleAction lineThroughFontaction;

	private FontAlignLeftAction alignLeftAction;
	private JToggleButton alignLeftButton = null;
	
	private Action alignRightAction;
	private JToggleButton alignRightButton = null;

	private Action alignCenterAction;
	private JToggleButton alignCenterButton = null;

	private Action strokeColorAction;

	private FillBackgroundColorAction backgroundColorAction;

	private JComboBox strokeWeightBox;

	private LineStyleAction lineStyleAction;

	private FillGradientDirectionAction gradientDirectionAction;

	private FillGradientColorAction gradientColor;

	private ShowHideLabelsAction showHideLabelsAction;

	private LineCurveAction lineCurveAction;

	private FillImageAction addImageAction;

	private FontRotationAction textRotationAction;

	public ToolBar(final PNEditor pnEditor, int orientation) throws ParameterException {
		super(orientation);
		Validate.notNull(pnEditor);
		this.pnEditor = pnEditor;
		
		try {
			saveAction = new SaveAction(pnEditor);
			cutAction = new CutAction(pnEditor, TransferHandler.getCutAction());
			copyAction = new CopyAction(pnEditor, TransferHandler.getCopyAction());
			pasteAction = new PasteAction(pnEditor, TransferHandler.getPasteAction());
			undoAction = new UndoAction(pnEditor);
			redoAction = new RedoAction(pnEditor);
			boldFontAction = new FontBoldStyleAction(pnEditor);
			italicFontAction = new FontItalicStyleAction(pnEditor);
			underlineFontAction = new FontUnderlineStyleAction(pnEditor);
			lineThroughFontaction = new FontLineThroughStyleAction(pnEditor);
			alignLeftAction = new FontAlignLeftAction(pnEditor);
			alignCenterAction = new FontAlignCenterAction(pnEditor);
			alignRightAction = new FontAlignRightAction(pnEditor);
			strokeColorAction = new LineStrokeColorAction(pnEditor);
			backgroundColorAction = new FillBackgroundColorAction(pnEditor);
			lineStyleAction = new LineStyleAction(pnEditor);
			gradientDirectionAction = new FillGradientDirectionAction(pnEditor);
			gradientColor = new FillGradientColorAction(pnEditor);
			showHideLabelsAction = new ShowHideLabelsAction(pnEditor);
			lineCurveAction = new LineCurveAction(pnEditor);
			addImageAction = new FillImageAction(pnEditor);
			textRotationAction = new FontRotationAction(pnEditor);
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3), getBorder()));
		setFloatable(false);

//		BoxLayout layout = new BoxLayout(this, BoxLayout.LINE_AXIS);
//		setLayout(layout);
	
		add(saveAction);
		addSeparator();

		add(cutAction);
		add(copyAction);
		add(pasteAction);
		addSeparator();
		addSeparator();
	
		add(undoAction);
		add(redoAction);
		
		addSeparator();
		
		add(getFontBox());

		add(getFontSizeBox());
		
		

		addSeparator();
		
		boldFontButton = (JToggleButton) add(boldFontAction, true);
		italicFontButton = (JToggleButton) add(italicFontAction, true);
		underlineFontButton = (JToggleButton) add(underlineFontAction, true);
		addSeparator();		
		
		alignLeftButton = (JToggleButton) add(alignLeftAction, true);
		alignCenterButton = (JToggleButton) add(alignCenterAction, true);
		alignRightButton = (JToggleButton) add(alignRightAction, true);
		ButtonGroup alignmentGroup = new ButtonGroup();
		alignmentGroup.add(alignLeftButton);
		alignmentGroup.add(alignCenterButton);
		alignmentGroup.add(alignRightButton);

		addSeparator();
		
		add(textRotationAction);

		addSeparator();	
		
		add(addImageAction);
		add(backgroundColorAction);
		add(gradientDirectionAction);
		add(gradientColor);
		add(strokeColorAction);
		add(getStrokeWeightBox());
		
		add(lineStyleAction);
		add(lineCurveAction);
		addSeparator();

		add(showHideLabelsAction);



		addSeparator();
		
		add(getZoomBox());
		
		deactivate();
	}
	
	private JComponent add(Action action, boolean asToggleButton){
		if(!asToggleButton)
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
		return b;
	}
	
	private JComboBox getFontBox(){
		if(fontBox == null){
			// Gets the list of available fonts from the local graphics environment
			// and adds some frequently used fonts at the beginning of the list
			GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
			List<String> fonts = new ArrayList<String>();
			fonts.addAll(Arrays.asList(new String[] { "Helvetica", "Verdana", "Times New Roman", "Garamond", "Courier New", "-" }));
			fonts.addAll(Arrays.asList(env.getAvailableFontFamilyNames()));
			fontBox = new JComboBox(fonts.toArray());
			fontBox.setMinimumSize(new Dimension(200, 24));
			fontBox.setPreferredSize(new Dimension(200, 24));
			fontBox.setMaximumSize(new Dimension(200, 24));
	
			fontBox.addActionListener(new ActionListener() {
	
				public void actionPerformed(ActionEvent e) {
					if(selectedCell != null){
						String font = fontBox.getSelectedItem().toString();
						PNGraph graph = ToolBar.this.pnEditor.getGraphComponent().getGraph();
						try {
							graph.setFontOfSelectedCellLabel(font);
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(ToolBar.this.pnEditor, "Cannot set cell-font: " + e1.getMessage(), "Graph Exception", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			});
		}
		return fontBox;
	}
	
	private JComboBox getFontSizeBox(){
		if(fontSizeBox == null){
			fontSizeBox = new JComboBox(new Object[] { "6pt", "8pt", "9pt", "10pt", "12pt", "14pt", "18pt", "24pt", "30pt", "36pt", "48pt", "60pt" });
			fontSizeBox.setMinimumSize(new Dimension(100, 24));
			fontSizeBox.setPreferredSize(new Dimension(100, 24));
			fontSizeBox.setMaximumSize(new Dimension(100, 24));
			add(fontSizeBox);
	
			fontSizeBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(selectedCell != null){
						String fontSize = fontSizeBox.getSelectedItem().toString().replace("pt", "");
						PNGraph graph = ToolBar.this.pnEditor.getGraphComponent().getGraph();
						try {
							graph.setFontSizeOfSelectedCellLabel(fontSize);
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(ToolBar.this.pnEditor, "Cannot set cell-font size: " + e1.getMessage(), "Graph Exception", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			});
		}
		return fontSizeBox;
	}
	
	
	private JComboBox getStrokeWeightBox(){
		if(strokeWeightBox == null){
			strokeWeightBox = new JComboBox(new Object[] { "0px", "1px", "2px","3px", "4px", "5px", "6px", "7px", "8px", "9px", "10px", "11px", "12px" });
			strokeWeightBox.setSelectedIndex(1);
			strokeWeightBox.setMinimumSize(new Dimension(100, 24));
			strokeWeightBox.setPreferredSize(new Dimension(100, 24));
			strokeWeightBox.setMaximumSize(new Dimension(100, 24));
			add(strokeWeightBox);
	
			strokeWeightBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(e.getSource() instanceof JComboBox){
					if(selectedCell != null){
						String strokeWeight = strokeWeightBox.getSelectedItem().toString().replace("px", "");
						PNGraph graph = ToolBar.this.pnEditor.getGraphComponent().getGraph();
						try {
							graph.setStrokeWeightOfSelectedCell(strokeWeight);
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(ToolBar.this.pnEditor, "Cannot set cell-font size: " + e1.getMessage(), "Graph Exception", JOptionPane.ERROR_MESSAGE);
						}
					}}
				}
			});
		}
		return strokeWeightBox;
	}
	
	private JComboBox getZoomBox(){
		if(zoomBox == null){
			final mxGraphView view = pnEditor.getGraphComponent().getGraph().getView();
			zoomBox = new JComboBox(new Object[] { "400%", "200%", "150%", "100%", "75%", "50%", mxResources.get("page"), mxResources.get("width"), mxResources.get("actualSize") });
			zoomBox.setMinimumSize(new Dimension(100, 24));
			zoomBox.setPreferredSize(new Dimension(100, 24));
			zoomBox.setMaximumSize(new Dimension(100, 24));
			zoomBox.setMaximumRowCount(9);
	
			// Sets the zoom in the zoom combo the current value
			mxIEventListener scaleTracker = new mxIEventListener() {
				public void invoke(Object sender, mxEventObject evt) {
					ignoreZoomChange = true;
	
					try {
						zoomBox.setSelectedItem((int) Math.round(100 * view.getScale()) + "%");
					} finally {
						ignoreZoomChange = false;
					}
				}
			};
	
			// Installs the scale tracker to update the value in the combo box
			// if the zoom is changed from outside the combo box
			view.getGraph().getView().addListener(mxEvent.SCALE, scaleTracker);
			view.getGraph().getView().addListener(mxEvent.SCALE_AND_TRANSLATE, scaleTracker);
	
			// Invokes once to sync with the actual zoom value
			scaleTracker.invoke(null, null);
	
			zoomBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					mxGraphComponent graphComponent = pnEditor.getGraphComponent();
	
					// Zoomcombo is changed when the scale is changed in the diagram
					// but the change is ignored here
					if (!ignoreZoomChange) {
						String zoom = zoomBox.getSelectedItem().toString();
	
						if (zoom.equals(mxResources.get("page"))) {
							graphComponent.setPageVisible(true);
							graphComponent.setZoomPolicy(mxGraphComponent.ZOOM_POLICY_PAGE);
						} else if (zoom.equals(mxResources.get("width"))) {
							graphComponent.setPageVisible(true);
							graphComponent.setZoomPolicy(mxGraphComponent.ZOOM_POLICY_WIDTH);
						} else if (zoom.equals(mxResources.get("actualSize"))) {
							graphComponent.zoomActual();
						} else {
							try {
								zoom = zoom.replace("%", "");
								double scale = Math.min(16, Math.max(0.01, Double.parseDouble(zoom) / 100));
								graphComponent.zoomTo(scale, graphComponent.isCenterZoom());
							} catch (Exception ex) {
								JOptionPane.showMessageDialog(pnEditor, ex.getMessage());
							}
						}
					}
				}
			});
		}
		return zoomBox;
	}
	
	public void deactivate(){
		copyAction.setEnabled(false);
		pasteAction.setEnabled(false);
		cutAction.setEnabled(false);
		getFontBox().setEnabled(false);
		getFontSizeBox().setEnabled(false);
		boldFontAction.setEnabled(false);
		italicFontAction.setEnabled(false);
		underlineFontAction.setEnabled(false);
		lineThroughFontaction.setEnabled(false);
		alignLeftAction.setEnabled(false);
		alignCenterAction.setEnabled(false);
		alignRightAction.setEnabled(false);
		strokeColorAction.setEnabled(false);
		getStrokeWeightBox().setEnabled(false);
		backgroundColorAction.setEnabled(false);
		lineStyleAction.setEnabled(false);
		gradientDirectionAction.setEnabled(false);
		gradientColor.setEnabled(false);
		showHideLabelsAction.setEnabled(false);
		lineCurveAction.setEnabled(false);
		addImageAction.setEnabled(false);
	}
	
	public void updateView(Set<PNGraphCell> selectedComponents){
		if(selectedComponents == null || selectedComponents.isEmpty()){
			deactivate();
			this.selectedCell = null;
			return;
		}
		if(!selectedComponents.isEmpty()){
			copyAction.setEnabled(true);
			pasteAction.setEnabled(true);
			cutAction.setEnabled(true);
			strokeColorAction.setEnabled(true);
			backgroundColorAction.setEnabled(true);
			getStrokeWeightBox().setEnabled(true);
			lineStyleAction.setEnabled(true);
			gradientDirectionAction.setEnabled(true);
			gradientColor.setEnabled(true);
			showHideLabelsAction.setEnabled(true);
			lineCurveAction.setEnabled(true);
			addImageAction.setEnabled(true);
			if(selectedComponents.size() == 1){
				this.selectedCell = selectedComponents.iterator().next();
				boolean isPlaceCell = selectedCell.getType() == PNComponent.PLACE;
				boolean isTransitionCell = selectedCell.getType() == PNComponent.TRANSITION;
				boolean isArcCell = selectedCell.getType() == PNComponent.ARC;
				boolean labelSelected = pnEditor.getGraphComponent().getGraph().isLabelSelected();
				getFontBox().setEnabled((labelSelected && (isPlaceCell || isTransitionCell)) || isArcCell);
				getFontSizeBox().setEnabled((labelSelected && (isPlaceCell || isTransitionCell)) || isArcCell);
				boldFontAction.setEnabled((labelSelected && (isPlaceCell || isTransitionCell)) || isArcCell);
				italicFontAction.setEnabled((labelSelected && (isPlaceCell || isTransitionCell)) || isArcCell);
				underlineFontAction.setEnabled((labelSelected && (isPlaceCell || isTransitionCell)) || isArcCell);
				lineThroughFontaction.setEnabled((labelSelected && (isPlaceCell || isTransitionCell)) || isArcCell);
				alignLeftAction.setEnabled((labelSelected && (isPlaceCell || isTransitionCell)) || isArcCell);
				alignCenterAction.setEnabled((labelSelected && (isPlaceCell || isTransitionCell)) || isArcCell);
				alignRightAction.setEnabled((labelSelected && (isPlaceCell || isTransitionCell)) || isArcCell);
//				getStrokeWeightBox().setEnabled((labelSelected && (isPlaceCell || isTransitionCell)) || isArcCell);
//				int strokeWeight = 0;
//				if(isArcCell){
//					strokeWeight = (int) pnEditor.getNetContainer().getPetriNetGraphics().getArcGraphics().get(selectedCell.getId()).getLine().getWidth();
//				} else if(isPlaceCell){
//					strokeWeight = (int) pnEditor.getNetContainer().getPetriNetGraphics().getPlaceGraphics().get(selectedCell.getId()).getLine().getWidth();					
//				} else if(isTransitionCell){
//					strokeWeight = (int) pnEditor.getNetContainer().getPetriNetGraphics().getTransitionGraphics().get(selectedCell.getId()).getLine().getWidth();					
//					
//				}
//				getStrokeWeightBox().setSelectedItem(strokeWeight + "px");

				// Initialize fields.
			} else {
				getFontBox().setEnabled(false);
				getFontSizeBox().setEnabled(false);
				this.selectedCell = null;
			}
		}
	}

	
}
