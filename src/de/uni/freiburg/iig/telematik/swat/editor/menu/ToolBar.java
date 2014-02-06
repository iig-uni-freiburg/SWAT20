package de.uni.freiburg.iig.telematik.swat.editor.menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.TransferHandler;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxGraphTransferable;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxResources;
import com.mxgraph.view.mxGraphView;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AnnotationGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.ArcGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill.GradientRotation;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Font;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Font.Align;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Font.Decoration;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line.Shape;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line.Style;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.CopyAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.CutAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.EnterEditingAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.EnterExecutionAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.PasteAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.RedoAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.SaveAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.ShowHideLabelsAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.UndoAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.fill.FillBackgroundColorAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.fill.FillColorSelectionAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.fill.FillGradientColorAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.fill.FillGradientRotationDiagonal;
import de.uni.freiburg.iig.telematik.swat.editor.actions.fill.FillGradientRotationHorizontal;
import de.uni.freiburg.iig.telematik.swat.editor.actions.fill.FillGradientRotationVertical;
import de.uni.freiburg.iig.telematik.swat.editor.actions.fill.FillNoFillAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.font.FontAlignCenterAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.font.FontAlignLeftAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.font.FontAlignRightAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.font.FontBoldStyleAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.font.FontItalicStyleAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.font.FontLineThroughStyleAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.font.FontRotationAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.font.FontUnderlineStyleAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.line.LineLineAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.line.LineColorSelectionAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.line.LineCurveAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.line.LineDotAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.line.LineDashAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.line.LineSolidAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.line.LineNoFillAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.line.LineShapeAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.line.LineStrokeColorAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.line.LineStyleAction;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.swat.editor.graph.Utils;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperties.PNComponent;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class ToolBar extends JToolBar {

	private static final long serialVersionUID = -6491749112943066366L;

	private boolean ignoreZoomChange = false;

	// Actions
	private Action alignRightAction;
	private Action alignCenterAction;
	private Action strokeColorAction;
	private Action gradientVerticalAction;
	private Action gradientHorizontalAction;
	private Action gradientDiagonalAction;
	private FillGradientColorAction gradientColorAction;
	private FillNoFillAction noFillAction;
	private LineNoFillAction noLineAction;
	private SaveAction saveAction = null;
	private CutAction cutAction = null;
	private CopyAction copyAction = null;
	private PasteAction pasteAction = null;
	private UndoAction undoAction = null;
	private RedoAction redoAction = null;
	private FontBoldStyleAction boldFontAction;
	private FontItalicStyleAction italicFontAction;
	private LineStyleAction lineStyleAction;
	private FillColorSelectionAction colorSelectionAction;
	private ShowHideLabelsAction showHideLabelsAction;
	private LineShapeAction lineShapeAction;
	// private FillImageAction addImageAction;
	private FontRotationAction textRotationAction;
	private FontUnderlineStyleAction underlineFontAction;
	private FontLineThroughStyleAction lineThroughFontaction;
	private FontAlignLeftAction alignLeftAction;
	private FillBackgroundColorAction backgroundColorAction;

	// Buttons
	private JButton saveButton;
	private JButton cutButton;
	private JButton copyButton;
	private JButton pasteButton;
	private JButton undoButton;
	private JButton redoButton;
	private JButton lineStyleButton;
	private JButton lineShapeButton;
	private JButton textRotationButton;
	private JButton showHideLabelsButton;
	private JButton strokeColorButton;
	private JToggleButton boldFontButton = null;
	private JToggleButton alignCenterButton = null;
	private JToggleButton alignRightButton = null;
	private JToggleButton italicFontButton = null;
	private JToggleButton underlineFontButton = null;
	private JToggleButton alignLeftButton = null;
	private JToggleButton backgroundColorButton;
	private JToggleButton gradientColorButton;
	private JToggleButton colorSelectionButton;
	private JToggleButton gradientDiagonalButton;
	private JToggleButton gradientHorizontalButton;
	private JToggleButton gradientVerticalButton;
	private JToggleButton noFillButton;
	private ButtonGroup fillGroup;
	private ButtonGroup gradientDirectionGroup;
	private ButtonGroup alignmentGroup;
	private JComboBox fontBox = null;
	private JComboBox fontSizeBox = null;
	private JComboBox zoomBox = null;
	private JComboBox fontComboBox;
	private JComboBox fontSizeComboBox;
	private JComboBox strokeWeightBox;

	// Tooltips
	private String boldFontTooltip = "bold";
	private String italicFontTooltip = "italic";
	private String underlineFontTooltip = "underline";
	private String alignRightTooltip = "right";
	private String alingCenterTooltip = "center";
	private String alignLeftTooltip = "left";
	private String textRotationTooltip = "rotate text 90¡";
	private String backgroundColorTooltip = "fill color";
	private String gradientColorTooltip = "gradient color";
	private String noFillTooltip = "no fill";
	private String noLineTooltip = "no line";
	private String colorSelectionTooltip = " select color";
	private String gradientDiagonalTooltip = "diagonal rotation";
	private String gradientHorizontalTooltip = "hirizontal rotation";
	private String gradientVerticalTooltip = "vertical rotation";
	private String saveButtonTooltip = "save";
	private String cutButtonTooltip = "cut";
	private String copyTooltip = "copy";
	private String pasteTooltip = "paste";
	private String undoTooltip = "undo";
	private String redoTooltip = "redo";
	private String fontTooltip = "choose fontfamily";
	private String fontSizeTooltip = "fontsize";
	private String strokeWeightTooltip = "set strokeweight";
	private String strokeColorTooltip = "set stroke color";
	private String lineStyleTooltip = "switch solid/ dash/ dot";
	private String lineShapeTooltip = "switch line/curve";
	private String showHideLabelsTooltip = "show/ hide labels";
	private String curveTooltip = "curve";
	private String lineTooltip = "line";
	private String lineSolidTooltip = "solid";
	private String lineDashTooltip = "dash";
	private String lineDotTooltip = "dot";

	// further variables
	private PNEditor pnEditor = null;
	private PNGraphCell selectedCell = null;
	private FillStyle fillStyle = FillStyle.SOLID;
	private LineStyle lineStyle = LineStyle.NORMAL;
	private Color currentFillColor;

	private JLabel fillLabel;

	private String fontLabelText = "Font:";
	private String fillLabelText = "Fill:";
	private String lineLabelText = "Line:";

	private JLabel lineLabel;

	private JLabel fontLabel;


	private LineDashAction lineDashAction;

	private LineSolidAction lineSolidAction;

	private LineDotAction lineDotAction;

	private LineColorSelectionAction lineColorSelectionAction;

	private JToggleButton lineSolidButton;

	private JToggleButton lineDashButton;

	private JToggleButton lineDotButton;

	private LineLineAction lineAction;

	private LineCurveAction curveAction;

	private JToggleButton lineButton;

	private JToggleButton curveButton;

	private JToggleButton noLineButton;

	private ButtonGroup lineGroup;

	private ButtonGroup lineStyleGroup;

	private JToggleButton lineColorSelectionButton;

	private int strokeWeight = (int) Line.DEFAULT_WIDTH;

	private EnterExecutionAction enterExecutionAction;
	private EnterEditingAction enterEditingAction;

	private JToggleButton enterExecutionButton;

	private JToggleButton enterEditingButton;

	private ButtonGroup editOrExecutionGroup;

	private String executionButtonTooltip = "execution mode";

	private String editingButtonTooltip = " editing mode";



	public ToolBar(final PNEditor pnEditor, int orientation) throws ParameterException {
		super(orientation);
		Validate.notNull(pnEditor);
		setLayout(new WrapLayout(FlowLayout.LEFT));
		this.pnEditor = pnEditor;

		try {
			saveAction = new SaveAction(pnEditor);
			enterExecutionAction = new EnterExecutionAction(pnEditor);
			enterEditingAction = new EnterEditingAction(pnEditor);
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
			gradientColorAction = new FillGradientColorAction(pnEditor);
			lineStyleAction = new LineStyleAction(pnEditor);
			
			showHideLabelsAction = new ShowHideLabelsAction(pnEditor);
			lineShapeAction = new LineShapeAction(pnEditor);

			// addImageAction = new FillImageAction(pnEditor); //could be
			// activated, working
			textRotationAction = new FontRotationAction(pnEditor);
			gradientHorizontalAction = new FillGradientRotationHorizontal(pnEditor);
			gradientVerticalAction = new FillGradientRotationVertical(pnEditor);
			gradientDiagonalAction = new FillGradientRotationDiagonal(pnEditor);
			colorSelectionAction = new FillColorSelectionAction(pnEditor);
			
			lineAction = new LineLineAction(pnEditor);
			curveAction = new LineCurveAction(pnEditor);
			noLineAction = new LineNoFillAction(pnEditor);
			lineDashAction = new LineDashAction(pnEditor);
			lineSolidAction = new LineSolidAction(pnEditor);
			lineDotAction = new LineDotAction(pnEditor);
			lineColorSelectionAction = new LineColorSelectionAction(pnEditor);

			noFillAction = new FillNoFillAction(pnEditor);
			
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3), getBorder()));
		setFloatable(false);

		// BoxLayout layout = new BoxLayout(this, BoxLayout.LINE_AXIS);
		// setLayout(layout);

		saveButton = add(saveAction);
		addSeparator();
		
		enterExecutionButton = (JToggleButton)  add(enterExecutionAction,true);
		enterEditingButton = (JToggleButton) add(enterEditingAction,true);
		editOrExecutionGroup = new ButtonGroup();
		addSeparator();

		cutButton = add(cutAction);
		copyButton = add(copyAction);
		pasteButton = add(pasteAction);
		addSeparator();
		addSeparator();

		undoButton = add(undoAction);
		redoButton = add(redoAction);

		addSeparator();
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

		// add(addImageAction);
		fillLabel = new JLabel(fillLabelText);
		add(fillLabel);
		backgroundColorButton = (JToggleButton) nestedAdd(backgroundColorAction);
		gradientColorButton = (JToggleButton) nestedAdd(gradientColorAction);
		noFillButton = (JToggleButton) nestedAdd(noFillAction);

		fillGroup = new ButtonGroup();
		fillGroup.add(backgroundColorButton);
		fillGroup.add(gradientColorButton);
		fillGroup.add(noFillButton);

		gradientVerticalButton = (JToggleButton) nestedAdd(gradientVerticalAction);
		gradientHorizontalButton = (JToggleButton) nestedAdd(gradientHorizontalAction);
		gradientDiagonalButton = (JToggleButton) nestedAdd(gradientDiagonalAction);

		gradientDirectionGroup = new ButtonGroup();
		gradientDirectionGroup.add(gradientVerticalButton);
		gradientDirectionGroup.add(gradientHorizontalButton);
		gradientDirectionGroup.add(gradientDiagonalButton);

		colorSelectionButton = (JToggleButton) nestedAdd(colorSelectionAction);

		setUpFillPanel();
		addSeparator();
		
		
		lineLabel = new JLabel(lineLabelText);
		add(lineLabel);
		lineButton = (JToggleButton) nestedAdd(lineAction);
		curveButton = (JToggleButton) nestedAdd(curveAction);
		noLineButton = (JToggleButton) nestedAdd(noLineAction);

		lineGroup = new ButtonGroup();
		lineGroup.add(lineButton);
		lineGroup.add(curveButton);

		lineSolidButton = (JToggleButton) nestedAdd(lineSolidAction);
		lineDashButton = (JToggleButton) nestedAdd(lineDashAction);
		lineDotButton = (JToggleButton) nestedAdd(lineDotAction);

		lineStyleGroup = new ButtonGroup();
		lineStyleGroup.add(lineSolidButton);
		lineStyleGroup.add(lineDashButton);
		lineStyleGroup.add(lineDotButton);

		lineColorSelectionButton = (JToggleButton) nestedAdd(lineColorSelectionAction);

		setUpLinePanel();
	

//		lineLabel = new JLabel(lineLabelText);
//		add(lineLabel);

//		strokeColorButton = add(strokeColorAction);
		
//		noLineButton =  add(noLineAction);
		strokeWeightBox = getStrokeWeightBox();
		add(strokeWeightBox);

//		lineStyleButton = (JButton) add(lineStyleAction, false);
//		lineShapeButton = (JButton) add(lineShapeAction, false);
		addSeparator();

		showHideLabelsButton = (JButton) add(showHideLabelsAction, false);

		addSeparator();

		add(getZoomBox());

		deactivate();
		doLayout();

		saveButton.setToolTipText(saveButtonTooltip);
		enterExecutionButton.setToolTipText(executionButtonTooltip);
		enterEditingButton.setToolTipText(editingButtonTooltip);
		cutButton.setToolTipText(cutButtonTooltip);
		copyButton.setToolTipText(copyTooltip);
		pasteButton.setToolTipText(pasteTooltip);
		undoButton.setToolTipText(undoTooltip);
		redoButton.setToolTipText(redoTooltip);
		fontComboBox.setToolTipText(fontTooltip);
		fontSizeComboBox.setToolTipText(fontSizeTooltip);
		boldFontButton.setToolTipText(boldFontTooltip);
		italicFontButton.setToolTipText(italicFontTooltip);
		underlineFontButton.setToolTipText(underlineFontTooltip);
		alignLeftButton.setToolTipText(alignLeftTooltip);
		alignCenterButton.setToolTipText(alingCenterTooltip);
		alignRightButton.setToolTipText(alignRightTooltip);
		textRotationButton.setToolTipText(textRotationTooltip);
		backgroundColorButton.setToolTipText(backgroundColorTooltip);
		gradientColorButton.setToolTipText(gradientColorTooltip);
		noFillButton.setToolTipText(noFillTooltip);
		gradientVerticalButton.setToolTipText(gradientVerticalTooltip);
		gradientHorizontalButton.setToolTipText(gradientHorizontalTooltip);
		gradientDiagonalButton.setToolTipText(gradientDiagonalTooltip);
		colorSelectionButton.setToolTipText(colorSelectionTooltip);
//		strokeColorButton.setToolTipText(strokeColorTooltip);
		noLineButton.setToolTipText(noLineTooltip);
		strokeWeightBox.setToolTipText(strokeWeightTooltip);
//		lineStyleButton.setToolTipText(lineStyleTooltip);
//		lineShapeButton.setToolTipText(lineShapeTooltip);
		showHideLabelsButton.setToolTipText(showHideLabelsTooltip);
		
		lineButton.setToolTipText(lineTooltip);
		curveButton.setToolTipText(curveTooltip);
		noLineButton.setToolTipText(noLineTooltip);
		lineSolidButton.setToolTipText(lineSolidTooltip);
		lineDashButton.setToolTipText(lineDashTooltip);
		lineDotButton.setToolTipText(lineDotTooltip);
		lineColorSelectionButton.setToolTipText(strokeColorTooltip);
		



	}

	private void setUpFillPanel() {
		int iconSize = 0;
		try {
			iconSize = SwatProperties.getInstance().getIconSize().getSize();
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JPanel pane = new JPanel();
		pane.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		pane.setMinimumSize(new Dimension(iconSize * 2, iconSize));
		pane.setPreferredSize(new Dimension(iconSize * 2, iconSize));
		pane.setMaximumSize(new Dimension(iconSize * 2, iconSize));

		c.fill = GridBagConstraints.HORIZONTAL;

		c.weightx = 0.5;
		c.weighty = 0.3;

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		pane.add(backgroundColorButton, c);

		c.weighty = 0.3;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 1;
		pane.add(gradientColorButton, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.weighty = 0.3;
		c.gridx = 0;
		c.gridy = 2;
		pane.add(noFillButton, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 0.3;
		c.weightx = 0.5;
		c.gridx = 4;
		c.gridy = 0;
		pane.add(gradientVerticalButton, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 0.3;
		c.weightx = 0.5;
		c.gridx = 4;
		c.gridy = 1;
		pane.add(gradientHorizontalButton, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 0.3;
		c.weightx = 0.5;
		c.gridx = 4;
		c.gridy = 2;
		pane.add(gradientDiagonalButton, c);

		// c.fill = GridBagConstraints.HORIZONTAL;
		// c.ipady = 0; //reset to default
		c.weighty = 1.0; // request any extra vertical space
		// c.anchor = GridBagConstraints.LAST_LINE_START; //bottom of space
		// c.insets = new Insets(10,0,0,0); //top padding
		c.gridx = 1;
		c.gridwidth = 3;
		c.gridheight = 3;
		c.gridy = 0;
		pane.add(colorSelectionButton, c);

		add(pane);
	}
	
	private void setUpLinePanel() {
		int iconSize = 0;
		try {
			iconSize = SwatProperties.getInstance().getIconSize().getSize();
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JPanel pane = new JPanel();
		pane.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		pane.setMinimumSize(new Dimension(iconSize * 2, iconSize));
		pane.setPreferredSize(new Dimension(iconSize * 2, iconSize));
		pane.setMaximumSize(new Dimension(iconSize * 2, iconSize));

		c.fill = GridBagConstraints.HORIZONTAL;

		c.weightx = 0.5;
		c.weighty = 0.3;

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		pane.add(lineButton, c);

		c.weighty = 0.3;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 1;
		pane.add(curveButton, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.weighty = 0.3;
		c.gridx = 0;
		c.gridy = 2;
		pane.add(noLineButton, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 0.3;
		c.weightx = 0.5;
		c.gridx = 4;
		c.gridy = 0;
		pane.add(lineSolidButton, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 0.3;
		c.weightx = 0.5;
		c.gridx = 4;
		c.gridy = 1;
		pane.add(lineDashButton, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 0.3;
		c.weightx = 0.5;
		c.gridx = 4;
		c.gridy = 2;
		pane.add(lineDotButton, c);

		// c.fill = GridBagConstraints.HORIZONTAL;
		// c.ipady = 0; //reset to default
		c.weighty = 1.0; // request any extra vertical space
		// c.anchor = GridBagConstraints.LAST_LINE_START; //bottom of space
		// c.insets = new Insets(10,0,0,0); //top padding
		c.gridx = 1;
		c.gridwidth = 3;
		c.gridheight = 3;
		c.gridy = 0;
		pane.add(lineColorSelectionButton, c);

		add(pane);
	}

	private JComponent add(Action action, boolean asToggleButton) {
		if (!asToggleButton)
			return super.add(action);
		JToggleButton b = createToggleActionComponent(action);
		b.setAction(action);
		add(b);
		return b;
	}

	private JComponent nestedAdd(Action action) {
		JToggleButton b = createToggleActionComponent(action);
		b.setAction(action);
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

	private JComboBox getFontSizeBox() {
		if (fontSizeBox == null) {
			fontSizeBox = new JComboBox(new Object[] { "6pt", "8pt", "9pt", "10pt", "11pt", "12pt", "14pt", "18pt", "24pt", "30pt", "36pt", "48pt", "60pt" });
			fontSizeBox.setMinimumSize(new Dimension(100, 24));
			fontSizeBox.setPreferredSize(new Dimension(100, 24));
			fontSizeBox.setMaximumSize(new Dimension(100, 24));
			add(fontSizeBox);

			fontSizeBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (selectedCell != null) {
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

	private JComboBox getStrokeWeightBox() {
		if (strokeWeightBox == null) {
			strokeWeightBox = new JComboBox(new Object[] { "0px", "1px", "2px", "3px", "4px", "5px", "6px", "7px", "8px", "9px", "10px", "11px", "12px" });
			strokeWeightBox.setSelectedIndex(1);
			strokeWeightBox.setMinimumSize(new Dimension(100, 24));
			strokeWeightBox.setPreferredSize(new Dimension(100, 24));
			strokeWeightBox.setMaximumSize(new Dimension(100, 24));
			add(strokeWeightBox);

			strokeWeightBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (e.getSource() instanceof JComboBox) {
						if (selectedCell != null) {
							String strokeWeight = strokeWeightBox.getSelectedItem().toString().replace("px", "");
							PNGraph graph = ToolBar.this.pnEditor.getGraphComponent().getGraph();
							try {
								graph.setStrokeWeightOfSelectedCell(strokeWeight);
							} catch (Exception e1) {
								JOptionPane.showMessageDialog(ToolBar.this.pnEditor, "Cannot set cell-font size: " + e1.getMessage(), "Graph Exception", JOptionPane.ERROR_MESSAGE);
							}
						}
					}
				}
			});
		}
		return strokeWeightBox;
	}

	private JComboBox getZoomBox() {
		if (zoomBox == null) {
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

					// Zoomcombo is changed when the scale is changed in the
					// diagram
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

	public void deactivate() {
		copyAction.setEnabled(false);
		pasteAction.setEnabled(isCellInClipboard());
		cutAction.setEnabled(false);
//		editOrExecutionGroup.clearSelection();
		setFontEnabled(false);

		setLineEnabled(false);

		setFillEnabled(false);

		showHideLabelsAction.setEnabled(false);
		// addImageAction.setEnabled(false);

	}

	private void setFontEnabled(boolean b) {
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
	}

	private void setLineEnabled(boolean b) {
		lineLabel.setEnabled(b);
		strokeColorAction.setEnabled(b);
		noLineAction.setEnabled(b);
		getStrokeWeightBox().setEnabled(b);
		lineStyleAction.setEnabled(b);
		lineShapeAction.setEnabled(b);
		lineDotAction.setEnabled(b);
		lineDashAction.setEnabled(b);
		lineShapeAction.setEnabled(b);
		lineAction.setEnabled(b);
		curveAction.setEnabled(b);
		lineColorSelectionAction.setEnabled(b);
		noLineAction.setEnabled(b);
	}

	private void setFillEnabled(boolean b) {
		fillLabel.setEnabled(b);
		backgroundColorAction.setEnabled(b);
		backgroundColorAction.setEnabled(b);
		gradientColorAction.setEnabled(b);
		noFillAction.setEnabled(b);
		gradientDiagonalAction.setEnabled(b);
		gradientHorizontalAction.setEnabled(b);
		gradientVerticalAction.setEnabled(b);
		colorSelectionAction.setEnabled(b);
	}

	private boolean isCellInClipboard() {
		Clipboard clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();

		return clipBoard.isDataFlavorAvailable(mxGraphTransferable.dataFlavor);

	}

	public void updateView(Set<PNGraphCell> selectedComponents) {
		if(!pnEditor.getGraphComponent().getGraph().isExecution()){
		if (selectedComponents == null || selectedComponents.isEmpty()) {
			deactivate();
			this.selectedCell = null;
			return;
		}
		if (!selectedComponents.isEmpty()) {
			copyAction.setEnabled(true);
			pasteAction.setEnabled(isCellInClipboard());
			cutAction.setEnabled(true);

			setLineEnabled(true);
			setFillEnabled(true);
			showHideLabelsAction.setEnabled(true);
			// addImageAction.setEnabled(true);

			if (selectedComponents.size() == 1) {
				// Enables Toolbar Buttons
				this.selectedCell = selectedComponents.iterator().next();
				boolean isPlaceCell = selectedCell.getType() == PNComponent.PLACE;
				boolean isTransitionCell = selectedCell.getType() == PNComponent.TRANSITION;
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
				switch (selectedCell.getType()) {
				case PLACE:
					nodeGraphics = pnEditor.getNetContainer().getPetriNetGraphics().getPlaceGraphics().get(selectedCell.getId());
					annotationGraphics = pnEditor.getNetContainer().getPetriNetGraphics().getPlaceLabelAnnotationGraphics().get(selectedCell.getId());
					break;
				case TRANSITION:
					nodeGraphics = pnEditor.getNetContainer().getPetriNetGraphics().getTransitionGraphics().get(selectedCell.getId());
					annotationGraphics = pnEditor.getNetContainer().getPetriNetGraphics().getTransitionLabelAnnotationGraphics().get(selectedCell.getId());
					break;
				case ARC:
					arcGraphics = pnEditor.getNetContainer().getPetriNetGraphics().getArcGraphics().get(selectedCell.getId());
					annotationGraphics = pnEditor.getNetContainer().getPetriNetGraphics().getArcAnnotationGraphics().get(selectedCell.getId());
					break;
				}

				if (nodeGraphics != null && !labelSelected) {

					Fill fill = nodeGraphics.getFill();
					setFillToolbar(fill, false);

					Line line = nodeGraphics.getLine();
					setLineToolbar(line);

				}

				if (arcGraphics != null && isArcCell) {
					Line line = arcGraphics.getLine();
					setLineToolbar(line);
					
//					setLineStyleButton(line);
//					setLineShapeButton(line);
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

					if (annotationGraphics.isVisible())
						showHideLabelsAction.setShowIconImage();
					else
						showHideLabelsAction.setHideIconImage();
					showHideLabelsButton.repaint();

					if (!labelSelected) {
						alignmentGroup.clearSelection();
					}
					
					Fill fill = annotationGraphics.getFill();
					setFillToolbar(fill, true);
					gradientColorAction.setEnabled(false);
					gradientDiagonalAction.setEnabled(false);
					gradientHorizontalAction.setEnabled(false);
					gradientVerticalAction.setEnabled(false);

					// normalFillButton.setBackground(new Color(255, 0, 0,
					// 100));
					// normalFillButton.setForeground(new Color(255, 0,
					// 255));
					backgroundColorButton.repaint();
					
					Line line = annotationGraphics.getLine();
					
					setLineToolbar(line);
					curveAction.setEnabled(false);
				}

				boldFontButton.setSelected(isBold);
				italicFontButton.setSelected(isItalic);
				underlineFontButton.setSelected(isUnderlined);
				alignLeftButton.setSelected(isAlignLeft);
				alignCenterButton.setSelected(isAlignCenter);
				alignRightButton.setSelected(isAlignRight);
				setFontEnabled((labelSelected && (isPlaceCell || isTransitionCell)) || isArcCell);


			} else {
				setFontEnabled(false);
				this.selectedCell = null;
			}
		}
		}
	}

	private void setFillToolbar(Fill fill, boolean isLabel) {
		boolean isFillSolid = false;
		boolean isFillGradient = false;
		boolean isFillEmpty = false;
		boolean isGradientDiagonal = false;
		boolean isGradientVertical = false;
		boolean isGradientHorizontal = false;
		if (fill != null) {
			String colorString = fill.getColor();
			String gradientString = fill.getGradientColor();
			GradientRotation gradientRotation = fill.getGradientRotation();
			boolean containsFillColor = false;
			boolean containsGradientColor = false;
			boolean containsGradientRotation = false;
			Color fillColor = FillGradientColorAction.DEFAULT_FILL_COLOR;
			if (colorString != null) {
				if (!colorString.equals("transparent")) {
					fillColor = Utils.parseColor(colorString);
					containsFillColor = true;
				} else {
					containsFillColor = false;
					fillColor = FillBackgroundColorAction.DEFAULT_FILL_COLOR;
				}
			}
			
			if(fillColor == null)
				containsFillColor = false;
			
			Color gradientColor = FillGradientColorAction.DEFAULT_GRADIENT_COLOR;
			if (gradientString != null) {
				gradientColor = Utils.parseColor(gradientString);
				containsGradientColor = true;
			}
			

			if (gradientRotation != null) {
				containsGradientRotation = true;
			} else
				gradientRotation = GradientRotation.VERTICAL;

			try {
				// backgroundColorAction.setFillColor(fillColor);
				// gradientColorAction.setFillColor(fillColor,
				// gradientColor);

				if (!containsFillColor) {
					try {
						setFillStyle(FillStyle.NOFILL, null, null, null);
						isFillEmpty = true;
						
					} catch (PropertyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (containsFillColor && containsGradientColor && containsGradientRotation &&!isLabel) {
					setFillStyle(FillStyle.GRADIENT, fillColor, gradientColor, gradientRotation);
					isFillGradient = true;
				} else {
					setFillStyle(FillStyle.SOLID, fillColor, fillColor, gradientRotation);
					isFillSolid = true;
				}
				currentFillColor = fillColor;
				if (containsGradientRotation) {
					switch (gradientRotation) {
					case DIAGONAL:
						isGradientDiagonal = true;
						break;
					case HORIZONTAL:
						isGradientHorizontal = true;
						break;
					case VERTICAL:
						isGradientVertical = true;
						break;
					default:
						break;

					}
				}

			} catch (PropertyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			gradientColorButton.repaint();
			backgroundColorButton.repaint();
			colorSelectionButton.repaint();

			fillGroup.clearSelection();
			backgroundColorButton.setSelected(isFillSolid);
			gradientColorButton.setSelected(isFillGradient);
			noFillButton.setSelected(isFillEmpty);

			gradientDirectionGroup.clearSelection();
			gradientDiagonalButton.setSelected(isGradientDiagonal);
			gradientHorizontalButton.setSelected(isGradientHorizontal);
			gradientVerticalButton.setSelected(isGradientVertical);

			// normalFillButton.setBackground(new Color(255, 0, 0,
			// 100));
			// normalFillButton.setForeground(new Color(255, 0,
			// 255));
			backgroundColorButton.repaint();
		}
		
	}

	private LineStyle getLineStyle() {
		// TODO Auto-generated method stub
		return lineStyle;
	}

	private void setLineStyle(LineStyle nofill, Color fillColor, Style linestyle, boolean isLineCurve) {
	

		switch (nofill) {
		case NOFILL:
			lineColorSelectionAction.setNoFill();
			break;
		case NORMAL:
			if(fillColor != null)
				try {
					lineColorSelectionAction.setFillColor(fillColor, 1.0, linestyle, isLineCurve);
				} catch (PropertyException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			break;
		default:
			break;
		}
			if (fillColor != null){
			try {
				lineAction.setFillColor(fillColor, 1.0);
				curveAction.setLineColor(fillColor);
			} catch (PropertyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}

			colorSelectionButton.repaint();
		}
	
	private void setLineToolbar(Line line){
		boolean isLineEmpty = false;
		boolean isLineLine = false;
		boolean isLineCurve = false;
		boolean isLineStraight = false;
		boolean isLineSolid = false;
		boolean isLineDashed = false;
		boolean isLineDotted = false;
		Color fillColor = null;
		if (line != null) {
			String lineColorString = line.getColor();
			Style lineStyle = line.getStyle();
			Shape lineShape = line.getShape();
			

			boolean containsLineColor = false;
			boolean containsStyle = false;
			Color lineColor = LineColorSelectionAction.DEFAULT_FILL_COLOR;
			
			
			switch (lineShape) {
			case CURVE:
				isLineCurve = true;
				break;
			case LINE:
				isLineLine = true;
				break;
			}
			if (lineColorString != null) {
				if (!lineColorString.equals("transparent")) {
					fillColor = Utils.parseColor(lineColorString);
					containsLineColor = true;
				} else {
					containsLineColor = false;
				}
			}
			if(fillColor == null)
				containsLineColor = false;
		

			if (lineStyle != null) {
				containsStyle = true;
			} else
				lineStyle = Style.SOLID;

		

				if (!containsLineColor) {
				
						setLineStyle(LineStyle.NOFILL, null, lineStyle, isLineCurve);
						isLineEmpty = true;
				
				}  else {
					setLineStyle(LineStyle.NORMAL, fillColor, lineStyle, isLineCurve);

				}
				currentFillColor = fillColor;
				if (containsStyle) {
					switch (lineStyle) {
					case DASH:
						isLineDashed = true;
						break;
					case DOT:
						isLineDotted = true;
						break;
					case SOLID:
						isLineSolid = true;
						break;

//					case DIAGONAL:
//						isGradientDiagonal = true;
//						break;
//					case HORIZONTAL:
//						isGradientHorizontal = true;
//						break;
//					case VERTICAL:
//						isGradientVertical = true;
//						break;
//					default:
//						break;

					}
				}

			

			curveButton.repaint();
			lineButton.repaint();
			lineColorSelectionButton.repaint();

		
			lineGroup.clearSelection();
			lineButton.setSelected(isLineLine);
			curveButton.setSelected(isLineCurve);
			noLineButton.setSelected(isLineEmpty);
			
			lineStyleGroup.clearSelection();
			lineDotButton.setSelected(isLineDotted);
			lineDashButton.setSelected(isLineDashed);
			lineSolidButton.setSelected(isLineSolid);
			lineButton.repaint();
			
		strokeWeight = (int) line.getWidth();
		getStrokeWeightBox().setSelectedItem(strokeWeight + "px");
//		setLineStyleButton(line);
//		setLineShapeButton(line);

		}
	}
	

//	/**
//	 * @param line
//	 */
//	protected void setLineStyleButton(Line line) {
//		Style lineStyle = line.getStyle();
//
//		switch (lineStyle) {
//		case DASH:
//			lineStyleAction.setDashIconImage();
//			break;
//		case DOT:
//			lineStyleAction.setDotconImage();
//			break;
//		case SOLID:
//			lineStyleAction.setSolidIconImage();
//			break;
//
//		}
//
//		lineStyleButton.repaint();
//	}

//	/**
//	 * @param line
//	 */
//	protected void setLineShapeButton(Line line) {
//		Shape lineShape = line.getShape();
//		switch (lineShape) {
//		case CURVE:
//			lineShapeAction.setCurveIconImage();
//			break;
//		case LINE:
//			lineShapeAction.setLineIconImage();
//			break;
//		}
//
//		lineShapeButton.repaint();
//	}

	public FillStyle getFillStyle() {
		return fillStyle;
	}

	public void setFillStyle(FillStyle fillStyle, Color fillColor, Color gradientColor, GradientRotation rotation) throws PropertyException, IOException {
		this.fillStyle = fillStyle;
		switch (fillStyle) {

		case SOLID:
			if (fillColor != null)
				backgroundColorButton.setSelected(true);
			colorSelectionAction.setFillColor(fillColor, fillColor, rotation);

			break;
		case GRADIENT:
			if (gradientColor != null) {
				colorSelectionAction.setFillColor(fillColor, gradientColor, rotation);

			} else {
				colorSelectionAction.setFillColor(currentFillColor, FillGradientColorAction.DEFAULT_GRADIENT_COLOR, GradientRotation.VERTICAL);
			}

			gradientColorButton.setSelected(true);

			break;
		case NOFILL:
			noFillButton.setSelected(true);
			colorSelectionAction.setNoFill();
			break;
		default:
			break;

		}
		if (fillColor != null)
			backgroundColorAction.setFillColor(fillColor);
		if (fillColor == gradientColor)
			gradientColor = FillGradientColorAction.DEFAULT_GRADIENT_COLOR;
		if (fillColor != null && gradientColor != null)
			gradientColorAction.setFillColor(fillColor, gradientColor);
		colorSelectionButton.repaint();

	}

	public enum FillStyle {
		SOLID, GRADIENT, NOFILL
	}

	public void setFillStyle(FillStyle style) {
		this.fillStyle = style;

	}
	
	public enum LineStyle {
		NORMAL, NOFILL
	}

	public void setLineStyle(LineStyle nofill) {
		this.lineStyle = nofill;

	}

	public void setExecutionMode() {
		deactivate();
		undoAction.setEnabled(false);
		redoAction.setEnabled(false);
		editOrExecutionGroup.clearSelection();
		enterExecutionButton.setSelected(true);
		enterEditingButton.setSelected(false);
		
	}
	
	public void setEditingMode() {
		deactivate();
		enterExecutionAction.setExecutionImage();
		enterExecutionButton.repaint();
		editOrExecutionGroup.clearSelection();
		enterExecutionButton.setSelected(false);
		enterEditingButton.setSelected(true);
		
	}

	public JToggleButton getExecutionButton() {
		return enterExecutionButton;
		
	}

}
