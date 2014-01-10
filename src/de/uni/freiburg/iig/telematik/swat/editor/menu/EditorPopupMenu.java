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
		System.out.println("hey");
		Validate.notNull(pnEditor);
		boolean selected = !pnEditor.getGraphComponent().getGraph().isSelectionEmpty();

		
		 addSeparator();
		 add(new CutAction(pnEditor, TransferHandler.getCutAction())).setEnabled(selected);
		 add(new CopyAction(pnEditor, TransferHandler.getCopyAction())).setEnabled(selected);
		 add(new PasteAction(pnEditor, TransferHandler.getPasteAction()));
		
		 addSeparator();
		 add(new DeleteAction(pnEditor, TransferHandler.getPasteAction())).setEnabled(selected);
		
		 addSeparator();
		 // Creates the format menu
//		 JMenu menu = (JMenu) add(new JMenu(mxResources.get("format")));
//
//		 EditorMenuBar.populateFormatMenu(menu, editor);
//
//		 // Creates the shape menu
//		 menu = (JMenu) add(new JMenu(mxResources.get("shape")));
//
//		 EditorMenuBar.populateShapeMenu(menu, editor);
//
//		 addSeparator();
//
//		 add(
//		 editor.bind(mxResources.get("edit"), mxGraphActions
//		 		.getEditAction())).setEnabled(selected);

		 addSeparator();
add(new SelectAction("vertices"));
add(new SelectAction("edges"));


		 addSeparator();
add(new SelectAction("selectAll"));


		
		 JMenu submenu = (JMenu) add(new JMenu("Layout"));
		
		 submenu.add(pnEditor.graphLayout("verticalHierarchical",
		 true));
		 submenu.add(pnEditor.graphLayout("horizontalHierarchical",
		 true));
		
		 submenu.addSeparator();
		
		 submenu.add(pnEditor.graphLayout("verticalPartition",
		 false));
		 submenu.add(pnEditor.graphLayout("horizontalPartition",
		 false));
		
		 submenu.addSeparator();
//		
//		 submenu.add(pnEditor.bind("verticalStack",pnEditor.graphLayout("verticalStack",
//		 false)));
//		 submenu.add(pnEditor.bind("horizontalStack",pnEditor.graphLayout("horizontalStack",
//		 false)));
//		
//		 submenu.addSeparator();
//		
//		 submenu.add(pnEditor.bind("verticalTree",pnEditor.graphLayout("verticalTree",
//		 true)));
//		 submenu.add(pnEditor.bind("horizontalTree",pnEditor.graphLayout("horizontalTree",
//		 true)));
//		
//		 submenu.addSeparator();
//		
//		 submenu.add(pnEditor.bind("placeEdgeLabels",pnEditor.graphLayout("placeEdgeLabels",
//		 false)));
//		 submenu.add(pnEditor.bind("parallelEdges",pnEditor.graphLayout("parallelEdges",
//		 false)));
//		
//		 submenu.addSeparator();
//		
//		 submenu.add(pnEditor.bind("organicLayout",pnEditor.graphLayout("organicLayout",
//		 true)));
//		 submenu.add(pnEditor.bind("circleLayout",pnEditor.graphLayout("circleLayout",
//		 true)));
		 
	

			




				
		 
		 
	}
}



