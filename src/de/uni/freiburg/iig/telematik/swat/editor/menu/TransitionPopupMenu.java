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
import de.uni.freiburg.iig.telematik.swat.editor.actions.graphics.FillBackgroundColorAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.graphics.FillGradientColorAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.graphics.FillGradientDirectionAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.graphics.FillImageAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.graphics.LineShapeAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.graphics.LineStrokeColorAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.graphics.LineStyleAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.text.FontAlignCenterAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.text.FontAlignLeftAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.text.FontAlignRightAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.text.FontBoldStyleAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.text.FontItalicStyleAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.text.FontLineThroughStyleAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.text.FontRotationAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.text.FontUnderlineStyleAction;

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



