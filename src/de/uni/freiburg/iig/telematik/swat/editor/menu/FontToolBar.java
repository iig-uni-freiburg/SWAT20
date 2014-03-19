package de.uni.freiburg.iig.telematik.swat.editor.menu;

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
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperties.PNComponent;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTabView;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatToolbar;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeView;

public class FontToolBar extends JToolBar {

	private static final long serialVersionUID = -6491749112943066366L;


	// Actions
	private ShowHideLabelsAction showHideLabelsAction;

	private FontBoldStyleAction boldFontAction;
	private FontItalicStyleAction italicFontAction;
	private FontRotationAction textRotationAction;
	private FontUnderlineStyleAction underlineFontAction;
	private FontLineThroughStyleAction lineThroughFontaction;
	private FontAlignLeftAction alignLeftAction;
	private FontAlignCenterAction alignCenterAction;
	private FontAlignRightAction alignRightAction;
	private FillBackgroundColorAction backgroundColorAction;

	// Buttons
	private JButton showHideLabelsButton;
	private JButton textRotationButton;
	private JToggleButton boldFontButton = null;
	private JToggleButton alignCenterButton = null;
	private JToggleButton alignRightButton = null;
	private JToggleButton italicFontButton = null;
	private JToggleButton underlineFontButton = null;
	private JToggleButton alignLeftButton = null;
	private ButtonGroup alignmentGroup;
	private JComboBox fontBox = null;
	private JComboBox fontSizeBox = null;
	private JComboBox fontComboBox;
	private JComboBox fontSizeComboBox;


	// Tooltips
	private String showHideLabelsTooltip = "show/ hide labels";
	private String boldFontTooltip = "bold";
	private String italicFontTooltip = "italic";
	private String underlineFontTooltip = "underline";
	private String alignRightTooltip = "right";
	private String alingCenterTooltip = "center";
	private String alignLeftTooltip = "left";
	private String textRotationTooltip = "rotate text 90�";
	
	private String fontTooltip = "choose fontfamily";
	private String fontSizeTooltip = "fontsize";
	

	// further variables
	private PNEditor pnEditor = null;
	private PNGraphCell selectedCell = null;

	private String fontLabelText = "Font:";

	private JLabel fontLabel;

	public FontToolBar(final PNEditor pnEditor, int orientation) throws ParameterException {
		super(orientation);
		Validate.notNull(pnEditor);
//		setLayout(new WrapLayout(FlowLayout.LEFT));
		this.pnEditor = pnEditor;

		try {
			showHideLabelsAction = new ShowHideLabelsAction(pnEditor);
			showHideLabelsAction.setFontToolbar(this);
			boldFontAction = new FontBoldStyleAction(pnEditor);
			italicFontAction = new FontItalicStyleAction(pnEditor);
			underlineFontAction = new FontUnderlineStyleAction(pnEditor);
			lineThroughFontaction = new FontLineThroughStyleAction(pnEditor);
			alignLeftAction = new FontAlignLeftAction(pnEditor);
			alignCenterAction = new FontAlignCenterAction(pnEditor);
			alignRightAction = new FontAlignRightAction(pnEditor);
			textRotationAction = new FontRotationAction(pnEditor);	
			
		} catch (PropertyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		setFloatable(false);
		showHideLabelsButton = (JButton) add(showHideLabelsAction, false);
		setButtonSettings(showHideLabelsButton);
		fontLabel = new JLabel(fontLabelText);
		add(fontLabel);
		fontComboBox = getFontBox();
		add(fontComboBox);

		fontSizeComboBox = getFontSizeBox();
		add(fontSizeComboBox);

		addSeparator();

		boldFontButton = (JToggleButton) add(boldFontAction, true);
		italicFontButton = (JToggleButton) add(italicFontAction, true);
		underlineFontButton = (JToggleButton) add(underlineFontAction, true);

		addSeparator();

		alignLeftButton = (JToggleButton) add(alignLeftAction, true);
		alignCenterButton = (JToggleButton) add(alignCenterAction, true);
		alignRightButton = (JToggleButton) add(alignRightAction, true);

		alignmentGroup = new ButtonGroup();
		alignmentGroup.add(alignLeftButton);
		alignmentGroup.add(alignCenterButton);
		alignmentGroup.add(alignRightButton);

		addSeparator();

		textRotationButton = add(textRotationAction);

		addSeparator();
		
		showHideLabelsButton.setToolTipText(showHideLabelsTooltip);
		fontComboBox.setToolTipText(fontTooltip);
		fontSizeComboBox.setToolTipText(fontSizeTooltip);
		boldFontButton.setToolTipText(boldFontTooltip);
		italicFontButton.setToolTipText(italicFontTooltip);
		underlineFontButton.setToolTipText(underlineFontTooltip);
		alignLeftButton.setToolTipText(alignLeftTooltip);
		alignCenterButton.setToolTipText(alingCenterTooltip);
		alignRightButton.setToolTipText(alignRightTooltip);
		textRotationButton.setToolTipText(textRotationTooltip);

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


	
	private JComboBox getFontBox() {
		if (fontBox == null) {
			// Gets the list of available fonts from the local graphics
			// environment
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
					if (selectedCell != null) {
						String font = fontBox.getSelectedItem().toString();
						PNGraph graph = FontToolBar.this.pnEditor.getGraphComponent().getGraph();
						try {
							graph.setFontOfSelectedCellLabel(font);
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(FontToolBar.this.pnEditor, "Cannot set cell-font: " + e1.getMessage(), "Graph Exception", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			});
		}
		return fontBox;
	}

	private JComboBox getFontSizeBox() {
		if (fontSizeBox == null) {
			fontSizeBox = new JComboBox(new Object[] { "6pt", "8pt", "9pt", "10pt", "11pt", "12pt", "14pt", "18pt", "24pt", "30pt", "36pt", "48pt", "60pt" });
			fontSizeBox.setMinimumSize(new Dimension(100, 24));
			fontSizeBox.setPreferredSize(new Dimension(100, 24));
			fontSizeBox.setMaximumSize(new Dimension(100, 24));

			fontSizeBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (selectedCell != null) {
						String fontSize = fontSizeBox.getSelectedItem().toString().replace("pt", "");
						PNGraph graph = FontToolBar.this.pnEditor.getGraphComponent().getGraph();
						try {
							graph.setFontSizeOfSelectedCellLabel(fontSize);
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(FontToolBar.this.pnEditor, "Cannot set cell-font size: " + e1.getMessage(), "Graph Exception", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			});
		}
		return fontSizeBox;
	}


	public void setFontEnabled(boolean b) {
		
		fontLabel.setEnabled(b);
		getFontBox().setEnabled(b);
		getFontSizeBox().setEnabled(b);
		boldFontAction.setEnabled(b);
		italicFontAction.setEnabled(b);
		underlineFontAction.setEnabled(b);
		lineThroughFontaction.setEnabled(b);
		alignLeftAction.setEnabled(b);
		alignCenterAction.setEnabled(b);
		alignRightAction.setEnabled(b);
		textRotationAction.setEnabled(b);
		
		if(b)
			showHideLabelsAction.setShowIconImage();
		else
			showHideLabelsAction.setHideIconImage();
	}


	
	public void updateView(Set<PNGraphCell> selectedComponents) {
		if (!pnEditor.getGraphComponent().getGraph().isExecution()) {
			if (selectedComponents == null || selectedComponents.isEmpty()) {
				setFontEnabled(false);
				this.selectedCell = null;
				return;
			}
			if (!selectedComponents.isEmpty()) {
			
			
				
				// addImageAction.setEnabled(true);

				if (selectedComponents.size() >= 1) {
					// Enables Toolbar Buttons
					this.selectedCell = selectedComponents.iterator().next();
					boolean isPlaceCell = selectedCell.getType() == PNComponent.PLACE;
					boolean isTransitionCell = selectedCell.getType() == PNComponent.TRANSITION;
					boolean isTransitionSilent = false;
					if (isTransitionCell) {
						if(pnEditor.getGraphComponent().getGraph().getNetContainer().getPetriNet().containsTransition(selectedCell.getId()))
						 isTransitionSilent = pnEditor.getGraphComponent().getGraph().getNetContainer().getPetriNet().getTransition(selectedCell.getId()).isSilent();
					}
					boolean isArcCell = selectedCell.getType() == PNComponent.ARC;
					boolean labelSelected = pnEditor.getGraphComponent().getGraph().isLabelSelected();
					boolean isBold = false;
					boolean isItalic = false;
					boolean isUnderlined = false;
					boolean isAlignLeft = false;
					boolean isAlignCenter = false;
					boolean isAlignRight = false;

					String fontFamily = null;
					String fontSize = null;

					NodeGraphics nodeGraphics = null;
					AnnotationGraphics annotationGraphics = null;
					ArcGraphics arcGraphics = null;
					if (!isTransitionSilent) {
						showHideLabelsAction.setEnabled(true);
						switch (selectedCell.getType()) {
						case PLACE:
							annotationGraphics = pnEditor.getNetContainer().getPetriNetGraphics().getPlaceLabelAnnotationGraphics().get(selectedCell.getId());
							break;
						case TRANSITION:
							annotationGraphics = pnEditor.getNetContainer().getPetriNetGraphics().getTransitionLabelAnnotationGraphics().get(selectedCell.getId());
							break;
						case ARC:
							annotationGraphics = pnEditor.getNetContainer().getPetriNetGraphics().getArcAnnotationGraphics().get(selectedCell.getId());
							break;
						}
	
						if (annotationGraphics != null && labelSelected) {
							Font font = annotationGraphics.getFont();
							fontFamily = font.getFamily();
							fontSize = font.getSize();
							String fontWeight = font.getWeight();
							if (fontWeight.equals("bold"))
								isBold = true;
							String fontStyle = font.getStyle();
							if (fontStyle.equals("italic"))
								isItalic = true;
							Decoration fontDecoration = font.getDecoration();
							if (fontDecoration != null && fontDecoration.equals(Font.Decoration.UNDERLINE))
								isUnderlined = true;
							Align fontAlign = font.getAlign();
							if (fontAlign.equals(Font.Align.CENTER))
								isAlignCenter = true;
							else if (fontAlign.equals(Font.Align.LEFT))
								isAlignLeft = true;
							else if (fontAlign.equals(Font.Align.RIGHT))
								isAlignRight = true;

							getFontBox().setSelectedItem(fontFamily);
							getFontSizeBox().setSelectedItem(fontSize + "pt");

							if (annotationGraphics.isVisible()){
								showHideLabelsAction.setShowIconImage();
							}
							else{
								showHideLabelsAction.setHideIconImage();
							}
							showHideLabelsButton.repaint();

							if (!labelSelected) {
								alignmentGroup.clearSelection();
							}
						}

						boldFontButton.setSelected(isBold);
						italicFontButton.setSelected(isItalic);
						underlineFontButton.setSelected(isUnderlined);
						alignLeftButton.setSelected(isAlignLeft);
						alignCenterButton.setSelected(isAlignCenter);
						alignRightButton.setSelected(isAlignRight);
						setFontEnabled((labelSelected && (isPlaceCell || isTransitionCell)) || isArcCell);
					}

				} else {
					setFontEnabled(false);
					this.selectedCell = null;
				}
			}
		}
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

}
