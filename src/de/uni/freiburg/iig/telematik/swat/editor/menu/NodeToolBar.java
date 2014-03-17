package de.uni.freiburg.iig.telematik.swat.editor.menu;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxResources;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraphView;

import de.invation.code.toval.graphic.component.DisplayFrame;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AnnotationGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.ArcGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Font;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Font.Align;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Font.Decoration;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.graphics.FillBackgroundColorAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.text.FontAlignCenterAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.text.FontAlignLeftAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.text.FontAlignRightAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.text.FontBoldStyleAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.text.FontItalicStyleAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.text.FontLineThroughStyleAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.text.FontRotationAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.text.FontUnderlineStyleAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.text.ShowHideLabelsAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.zoom.ZoomInAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.zoom.ZoomOutAction;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperties.PNComponent;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTabView;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatToolbar;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeView;

public class NodeToolBar extends JToolBar {

	private static final long serialVersionUID = -6491749112943066366L;



	// Actions

	private ZoomInAction zoomInAction;
	
	// Buttons
	private JButton zoomInButton;

	// Tooltips
	private String zoomInTooltip = "zoom in";

	private String zoomOutTooltip = "zoom out";
	


	// further variables
	private PNEditor pnEditor = null;
	private PNGraphCell selectedCell = null;

	private String fontLabelText = "Font:";

	private JLabel fontLabel;

	private ZoomOutAction zoomOutAction;

	private JButton zoomOutButton;



	protected NodePalettePanel palettePanel = null;



	public NodeToolBar(final PNEditor pnEditor, int orientation) throws ParameterException {
		super(orientation);
		Validate.notNull(pnEditor);
//		setLayout(new WrapLayout(FlowLayout.LEFT));
		this.pnEditor = pnEditor;

		// Installs the scale tracker to update the value in the combo box
		// if the zoom is changed from outside the combo box

//		try {
//
//			zoomInAction = new ZoomInAction(pnEditor);	
//			zoomOutAction = new ZoomOutAction(pnEditor);	
//			
//		} catch (PropertyException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		setFloatable(false);



//
//		zoomInButton = add(zoomInAction);
//		setButtonSettings(zoomInButton);
//		
//		zoomOutButton = add(zoomOutAction);
//		setButtonSettings(zoomOutButton);
		
		add(getPalettePanel());

		
		
		
	
		
		
	}
	private JPanel getPalettePanel() throws ParameterException {
		if (palettePanel == null) {
			palettePanel = new NodePalettePanel();
		}
		return palettePanel;
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
		return b;
	}
	
//	private JComboBox getZoomBox() {
//		if (true) {
//			final mxGraphView view = pnEditor.getGraphComponent().getGraph().getView();
//		
//
//			// Sets the zoom in the zoom combo the current value
//			mxIEventListener scaleTracker = new mxIEventListener() {
//				public void invoke(Object sender, mxEventObject evt) {
//					ignoreZoomChange = true;
//
//					try {
//					} finally {
//						ignoreZoomChange = false;
//					}
//				}
//			};
//
//			// Installs the scale tracker to update the value in the combo box
//			// if the zoom is changed from outside the combo box
//			view.getGraph().getView().addListener(mxEvent.SCALE, scaleTracker);
//			view.getGraph().getView().addListener(mxEvent.SCALE_AND_TRANSLATE, scaleTracker);
//
//			// Invokes once to sync with the actual zoom value
//			scaleTracker.invoke(null, null);
//
//			zoomBox.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					mxGraphComponent graphComponent = pnEditor.getGraphComponent();
//
//					// Zoomcombo is changed when the scale is changed in the
//					// diagram
//					// but the change is ignored here
//					if (!ignoreZoomChange) {
//
//						if (zoom.equals(mxResources.get("page"))) {
//							graphComponent.setPageVisible(true);
//							graphComponent.setZoomPolicy(mxGraphComponent.ZOOM_POLICY_PAGE);
//						} else if (zoom.equals(mxResources.get("width"))) {
//							graphComponent.setPageVisible(true);
//							graphComponent.setZoomPolicy(mxGraphComponent.ZOOM_POLICY_WIDTH);
//						} else if (zoom.equals(mxResources.get("actualSize"))) {
//							graphComponent.zoomActual();
//						} else {
//							try {
//								zoom = zoom.replace("%", "");
//								double scale = Math.min(16, Math.max(0.01, Double.parseDouble(zoom) / 100));
//								graphComponent.zoomTo(scale, graphComponent.isCenterZoom());
//							} catch (Exception ex) {
//								JOptionPane.showMessageDialog(pnEditor, ex.getMessage());
//							}
//						}
//					}
//				}
//			});
//		}
//		return zoomBox;
//	}

//	private JComponent add(Action action, boolean asToggleButton) {
//		if (!asToggleButton)
//			return super.add(action);
//		JToggleButton b = createToggleActionComponent(action);
//		b.setAction(action);
//		add(b);
//		return b;
//	}




	
	
	
	


	

//	public void updateView(Set<PNGraphCell> selectedComponents) {
//		if (!pnEditor.getGraphComponent().getGraph().isExecution()) {
//			if (selectedComponents == null || selectedComponents.isEmpty()) {
////				deactivate();
//				this.selectedCell = null;
//				return;
//			}
//			if (!selectedComponents.isEmpty()) {
//			
//
//			
//				
//				// addImageAction.setEnabled(true);
//
//				if (selectedComponents.size() >= 1) {
//					// Enables Toolbar Buttons
//					this.selectedCell = selectedComponents.iterator().next();
//					boolean isPlaceCell = selectedCell.getType() == PNComponent.PLACE;
//					boolean isTransitionCell = selectedCell.getType() == PNComponent.TRANSITION;
//					boolean isTransitionSilent = false;
//					if (isTransitionCell) {
//						if(pnEditor.getGraphComponent().getGraph().getNetContainer().getPetriNet().containsTransition(selectedCell.getId()))
//						 isTransitionSilent = pnEditor.getGraphComponent().getGraph().getNetContainer().getPetriNet().getTransition(selectedCell.getId()).isSilent();
//					}
//					boolean isArcCell = selectedCell.getType() == PNComponent.ARC;
//					boolean labelSelected = pnEditor.getGraphComponent().getGraph().isLabelSelected();
//					boolean isBold = false;
//					boolean isItalic = false;
//					boolean isUnderlined = false;
//					boolean isAlignLeft = false;
//					boolean isAlignCenter = false;
//					boolean isAlignRight = false;
//
//					String fontFamily = null;
//					String fontSize = null;
//
//					AnnotationGraphics annotationGraphics = null;
//
//					if (!isTransitionSilent) {
//	
//						switch (selectedCell.getType()) {
//						case PLACE:
//							annotationGraphics = pnEditor.getNetContainer().getPetriNetGraphics().getPlaceLabelAnnotationGraphics().get(selectedCell.getId());
//							break;
//						case TRANSITION:
//							annotationGraphics = pnEditor.getNetContainer().getPetriNetGraphics().getTransitionLabelAnnotationGraphics().get(selectedCell.getId());
//							break;
//						case ARC:
//							annotationGraphics = pnEditor.getNetContainer().getPetriNetGraphics().getArcAnnotationGraphics().get(selectedCell.getId());
//							break;
//						}
//
//
//						if (annotationGraphics != null && labelSelected) {
//							Font font = annotationGraphics.getFont();
//							fontFamily = font.getFamily();
//							fontSize = font.getSize();
//							String fontWeight = font.getWeight();
//							if (fontWeight.equals("bold"))
//								isBold = true;
//							String fontStyle = font.getStyle();
//							if (fontStyle.equals("italic"))
//								isItalic = true;
//							Decoration fontDecoration = font.getDecoration();
//							if (fontDecoration != null && fontDecoration.equals(Font.Decoration.UNDERLINE))
//								isUnderlined = true;
//							Align fontAlign = font.getAlign();
//							if (fontAlign.equals(Font.Align.CENTER))
//								isAlignCenter = true;
//							else if (fontAlign.equals(Font.Align.LEFT))
//								isAlignLeft = true;
//							else if (fontAlign.equals(Font.Align.RIGHT))
//								isAlignRight = true;
//
//							getFontBox().setSelectedItem(fontFamily);
//							getFontSizeBox().setSelectedItem(fontSize + "pt");
//
//						
//							if (!labelSelected) {
//								alignmentGroup.clearSelection();
//							}	
//						}
//
//						boldFontButton.setSelected(isBold);
//						italicFontButton.setSelected(isItalic);
//						underlineFontButton.setSelected(isUnderlined);
//						alignLeftButton.setSelected(isAlignLeft);
//						alignCenterButton.setSelected(isAlignCenter);
//						alignRightButton.setSelected(isAlignRight);
//						setFontEnabled((labelSelected && (isPlaceCell || isTransitionCell)) || isArcCell);
//					}
//
//				} else {
//					setFontEnabled(false);
//					this.selectedCell = null;
//				}
//			}
//		}
//	}

	
//	public void updateView(Set<PNGraphCell> selectedComponents) {
//		if (!pnEditor.getGraphComponent().getGraph().isExecution()) {
//			if (selectedComponents == null || selectedComponents.isEmpty()) {
//				this.selectedCell = null;
//				return;
//			}
//			if (!selectedComponents.isEmpty()) {
//			
//			
//				
//				// addImageAction.setEnabled(true);
//
//				if (selectedComponents.size() >= 1) {
//					// Enables Toolbar Buttons
//					this.selectedCell = selectedComponents.iterator().next();
//					boolean isPlaceCell = selectedCell.getType() == PNComponent.PLACE;
//					boolean isTransitionCell = selectedCell.getType() == PNComponent.TRANSITION;
//					boolean isTransitionSilent = false;
//					if (isTransitionCell) {
//						if(pnEditor.getGraphComponent().getGraph().getNetContainer().getPetriNet().containsTransition(selectedCell.getId()))
//						 isTransitionSilent = pnEditor.getGraphComponent().getGraph().getNetContainer().getPetriNet().getTransition(selectedCell.getId()).isSilent();
//					}
//					boolean isArcCell = selectedCell.getType() == PNComponent.ARC;
//					boolean labelSelected = pnEditor.getGraphComponent().getGraph().isLabelSelected();
//					boolean isBold = false;
//					boolean isItalic = false;
//					boolean isUnderlined = false;
//					boolean isAlignLeft = false;
//					boolean isAlignCenter = false;
//					boolean isAlignRight = false;
//
//					String fontFamily = null;
//					String fontSize = null;
//
//					NodeGraphics nodeGraphics = null;
//					AnnotationGraphics annotationGraphics = null;
//					ArcGraphics arcGraphics = null;
//					if (!isTransitionSilent) {
//						showHideLabelsAction.setEnabled(true);
//						switch (selectedCell.getType()) {
//						case PLACE:
//							annotationGraphics = pnEditor.getNetContainer().getPetriNetGraphics().getPlaceLabelAnnotationGraphics().get(selectedCell.getId());
//							break;
//						case TRANSITION:
//							annotationGraphics = pnEditor.getNetContainer().getPetriNetGraphics().getTransitionLabelAnnotationGraphics().get(selectedCell.getId());
//							break;
//						case ARC:
//							annotationGraphics = pnEditor.getNetContainer().getPetriNetGraphics().getArcAnnotationGraphics().get(selectedCell.getId());
//							break;
//						}
//	
//						if (annotationGraphics != null && labelSelected) {
//							Font font = annotationGraphics.getFont();
//							fontFamily = font.getFamily();
//							fontSize = font.getSize();
//							String fontWeight = font.getWeight();
//							if (fontWeight.equals("bold"))
//								isBold = true;
//							String fontStyle = font.getStyle();
//							if (fontStyle.equals("italic"))
//								isItalic = true;
//							Decoration fontDecoration = font.getDecoration();
//							if (fontDecoration != null && fontDecoration.equals(Font.Decoration.UNDERLINE))
//								isUnderlined = true;
//							Align fontAlign = font.getAlign();
//							if (fontAlign.equals(Font.Align.CENTER))
//								isAlignCenter = true;
//							else if (fontAlign.equals(Font.Align.LEFT))
//								isAlignLeft = true;
//							else if (fontAlign.equals(Font.Align.RIGHT))
//								isAlignRight = true;
//
//							getFontBox().setSelectedItem(fontFamily);
//							getFontSizeBox().setSelectedItem(fontSize + "pt");
//
//							if (annotationGraphics.isVisible()){
//								showHideLabelsAction.setShowIconImage();
//							}
//							else{
//								showHideLabelsAction.setHideIconImage();
//							}
//							showHideLabelsButton.repaint();
//
//							if (!labelSelected) {
//								alignmentGroup.clearSelection();
//							}
//						}
//
//						boldFontButton.setSelected(isBold);
//						italicFontButton.setSelected(isItalic);
//						underlineFontButton.setSelected(isUnderlined);
//						alignLeftButton.setSelected(isAlignLeft);
//						alignCenterButton.setSelected(isAlignCenter);
//						alignRightButton.setSelected(isAlignRight);
//						setFontEnabled((labelSelected && (isPlaceCell || isTransitionCell)) || isArcCell);
//					}
//
//				} else {
//					setFontEnabled(false);
//					this.selectedCell = null;
//				}
//			}
//		}
//	}
//



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

}
