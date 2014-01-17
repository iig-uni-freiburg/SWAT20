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
import de.uni.freiburg.iig.telematik.swat.editor.actions.FillBackgroundColorAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.FillGradientColorAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.FillGradientDirectionAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.FillImageAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.FontAlignCenterAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.FontAlignLeftAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.FontAlignRightAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.FontBoldStyleAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.FontItalicStyleAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.FontLineThroughStyleAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.FontRotationAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.FontUnderlineStyleAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.LayoutAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.LineShapeAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.LineStrokeColorAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.LineStyleAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.PasteAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.RedoAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.SaveAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.ShowHideLabelsAction;
import de.uni.freiburg.iig.telematik.swat.editor.actions.UndoAction;

public class EditorPopupMenu extends JPopupMenu {
	
	private static final long serialVersionUID = -2983257974918330746L;

	public EditorPopupMenu(PNEditor pnEditor) throws ParameterException, PropertyException, IOException {
		Validate.notNull(pnEditor);
		boolean selected = !pnEditor.getGraphComponent().getGraph().isSelectionEmpty();

		
		
		 JMenu submenu = (JMenu) add(new JMenu("Layout"));
		
		 submenu.add(new LayoutAction(pnEditor, "verticalHierarchical",
		 false));
		 submenu.add(new LayoutAction(pnEditor, "horizontalHierarchical",
		 false));

		 submenu.addSeparator();
		
		 submenu.add(new LayoutAction(pnEditor, "verticalStack",
		 false));
		 submenu.add(new LayoutAction(pnEditor, "horizontalStack",
		 false));
		
		 submenu.addSeparator();
		
		 submenu.add(new LayoutAction(pnEditor, "verticalTree",
		 true));
		 submenu.add(new LayoutAction(pnEditor, "horizontalTree",
		 true));
		
		 submenu.addSeparator();
		
		 submenu.add(new LayoutAction(pnEditor, "placeEdgeLabels",
		 false));
		 submenu.add(new LayoutAction(pnEditor, "parallelEdges",
		 false));
		
		 submenu.addSeparator();
		
		 submenu.add(new LayoutAction(pnEditor, "organicLayout",
		 true));
		 submenu.add(new LayoutAction(pnEditor, "circleLayout",
		 true));
		 
	

			




				
		 
		 
	}
}



