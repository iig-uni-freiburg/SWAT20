package de.uni.freiburg.iig.telematik.swat.editor.menu;

import java.io.IOException;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.TransferHandler;

import com.mxgraph.swing.util.mxGraphActions;
import com.mxgraph.swing.util.mxGraphActions.SelectAction;
import com.mxgraph.util.mxResources;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.CopyAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.CutAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.DeleteAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.LayoutAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.MakeSilentAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.PasteAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.RedoAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.SaveAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.ShowHideLabelsAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.TransitionSilentAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.UndoAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.fill.FillBackgroundColorAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.fill.FillGradientColorAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.fill.FillGradientDirectionAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.fill.FillImageAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.font.FontAlignCenterAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.font.FontAlignLeftAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.font.FontAlignRightAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.font.FontBoldStyleAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.font.FontItalicStyleAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.font.FontLineThroughStyleAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.font.FontRotationAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.font.FontUnderlineStyleAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.line.LineShapeAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.line.LineStrokeColorAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.line.LineStyleAction;

public class TransitionPopupMenu extends JPopupMenu {
	
	private static final long serialVersionUID = -2983257974918330746L;

	public TransitionPopupMenu(PNEditor pnEditor) throws ParameterException, PropertyException, IOException {
		Validate.notNull(pnEditor);
		boolean selected = !pnEditor.getGraphComponent().getGraph().isSelectionEmpty();

		
		

		 JMenu submenu = (JMenu) add(new JMenu("Transition"));
			
		 submenu.add(new TransitionSilentAction(pnEditor, "silent",
		 true));
		 submenu.add(new TransitionSilentAction(pnEditor, "not silent",
		 false));		
		 
		 
	}
}



