package de.uni.freiburg.iig.telematik.swat.editor.menu;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
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
import de.uni.freiburg.iig.telematik.swat.editor.actions.AddImageAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.AlignCenterAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.AlignLeftAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.AlignRightAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.BackgroundColorAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.BoldStyleAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.CopyAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.CutAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.GradientDirectionAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.GradientColorAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.ItalicStyleAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.LineCurveAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.LineThroughStyleAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.PasteAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.RedoAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.SaveAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.LineStyleAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.ShowHideLabelsAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.StrokeColorAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.UnderlineStyleAction;
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

	private BoldStyleAction boldFontAction;

	private ItalicStyleAction italicFontAction;

	private UnderlineStyleAction underlineFontAction;

	private LineThroughStyleAction lineThroughFontaction;

	private AlignLeftAction alignLeftAction;

	private Action alignRightAction;

	private Action alignCenterAction;

	private Action strokeColorAction;

	private BackgroundColorAction backgroundColorAction;


	private JComboBox strokeWeightBox;

	private LineStyleAction lineStyleAction;

	private GradientDirectionAction gradientDirectionAction;

	private GradientColorAction gradientColor;

	private ShowHideLabelsAction showHideLabelsAction;

	private LineCurveAction lineCurveAction;

	private AddImageAction addImageAction;

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
			boldFontAction = new BoldStyleAction(pnEditor);
			italicFontAction = new ItalicStyleAction(pnEditor);
			underlineFontAction = new UnderlineStyleAction(pnEditor);
			lineThroughFontaction = new LineThroughStyleAction(pnEditor);
			alignLeftAction = new AlignLeftAction(pnEditor);
			alignCenterAction = new AlignCenterAction(pnEditor);
			alignRightAction = new AlignRightAction(pnEditor);
			strokeColorAction = new StrokeColorAction(pnEditor);
			backgroundColorAction = new BackgroundColorAction(pnEditor);
			lineStyleAction = new LineStyleAction(pnEditor);
			gradientDirectionAction = new GradientDirectionAction(pnEditor);
			gradientColor = new GradientColorAction(pnEditor);
			showHideLabelsAction = new ShowHideLabelsAction(pnEditor);
			lineCurveAction = new LineCurveAction(pnEditor);
			addImageAction = new AddImageAction(pnEditor);
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
		add(addImageAction);
		addSeparator();
		
		add(getFontBox());

		add(getFontSizeBox());

		addSeparator();
		
		add(boldFontAction);
		add(italicFontAction);
		add(underlineFontAction);
		add(lineThroughFontaction);
		addSeparator();		
		
		add(alignLeftAction);
		add(alignCenterAction);
		add(alignRightAction);

		addSeparator();
		
		add(strokeColorAction);
		add(getStrokeWeightBox());
		add(backgroundColorAction);
		
add(lineStyleAction);

add(gradientDirectionAction);
add(gradientColor);
add(showHideLabelsAction);
add(lineCurveAction);


		addSeparator();
		
		add(getZoomBox());
		
		deactivate();
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
			strokeWeightBox = new JComboBox(new Object[] { "0px", "1px", "2px", "4px", "5px", "6px", "7px", "8px", "9px", "10px", "11px", "12px" });
			strokeWeightBox.setMinimumSize(new Dimension(100, 24));
			strokeWeightBox.setPreferredSize(new Dimension(100, 24));
			strokeWeightBox.setMaximumSize(new Dimension(100, 24));
			add(strokeWeightBox);
	
			strokeWeightBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(selectedCell != null){
						String strokeWeight = strokeWeightBox.getSelectedItem().toString().replace("px", "");
						PNGraph graph = ToolBar.this.pnEditor.getGraphComponent().getGraph();
						try {
							graph.setStrokeWeightOfSelectedCell(strokeWeight);
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(ToolBar.this.pnEditor, "Cannot set cell-font size: " + e1.getMessage(), "Graph Exception", JOptionPane.ERROR_MESSAGE);
						}
					}
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
//		getStrokeWeightBox().setEnabled(false);
		backgroundColorAction.setEnabled(false);
	}
	
	public void updateView(Set<PNGraphCell> selectedComponents){
		System.out.println(selectedComponents);
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

				// Initialize fields.
			} else {
				getFontBox().setEnabled(false);
				getFontSizeBox().setEnabled(false);
				this.selectedCell = null;
			}
		}
	}

	
}
