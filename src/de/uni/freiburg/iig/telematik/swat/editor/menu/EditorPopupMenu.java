package de.uni.freiburg.iig.telematik.swat.editor.menu;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.TransferHandler;

import com.mxgraph.swing.util.mxGraphActions;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.UndoAction;

public class EditorPopupMenu extends JPopupMenu {
	
	private static final long serialVersionUID = -2983257974918330746L;

	public EditorPopupMenu(PNEditor pnEditor) throws ParameterException {
		Validate.notNull(pnEditor);
		boolean selected = !pnEditor.getGraphComponent().getGraph().isSelectionEmpty();
//
//		add(pnEditor.bind("Undo", new UndoRedoAction(pnEditor, true), "/images/undo.gif"));
//
//		addSeparator();
//		add(pnEditor.bind("Cut", TransferHandler.getCutAction(), "/images/cut.gif")).setEnabled(selected);
//		add(pnEditor.bind("Copy", TransferHandler.getCopyAction(), "/images/copy.gif")).setEnabled(selected);
//		add(pnEditor.bind("Paste", TransferHandler.getPasteAction(), "/images/paste.gif"));
//
//		addSeparator();
//		add(pnEditor.bind("Delete", mxGraphActions.getDeleteAction(), "/images/delete.gif")).setEnabled(selected);
//
//		addSeparator();
//		
//		add(pnEditor.bind("Edit", mxGraphActions.getEditAction())).setEnabled(selected);
//
//		addSeparator();
//		add(pnEditor.bind("selectVertices", mxGraphActions.getSelectVerticesAction()));
//		add(pnEditor.bind("selectEdges", mxGraphActions.getSelectEdgesAction()));
//		
//		JMenu submenu = (JMenu) add(new JMenu("Layout"));
//
//		submenu.add(pnEditor.bind("verticalHierarchical",pnEditor.graphLayout("verticalHierarchical", true)));
//		submenu.add(pnEditor.bind("horizontalHierarchical",pnEditor.graphLayout("horizontalHierarchical", true)));
//
//		submenu.addSeparator();
//
//		submenu.add(pnEditor.bind("verticalPartition",pnEditor.graphLayout("verticalPartition", false)));
//		submenu.add(pnEditor.bind("horizontalPartition",pnEditor.graphLayout("horizontalPartition", false)));
//
//		submenu.addSeparator();
//
//		submenu.add(pnEditor.bind("verticalStack",pnEditor.graphLayout("verticalStack", false)));
//		submenu.add(pnEditor.bind("horizontalStack",pnEditor.graphLayout("horizontalStack", false)));
//
//		submenu.addSeparator();
//
//		submenu.add(pnEditor.bind("verticalTree",pnEditor.graphLayout("verticalTree", true)));
//		submenu.add(pnEditor.bind("horizontalTree",pnEditor.graphLayout("horizontalTree", true)));
//
//		submenu.addSeparator();
//
//		submenu.add(pnEditor.bind("placeEdgeLabels",pnEditor.graphLayout("placeEdgeLabels", false)));
//		submenu.add(pnEditor.bind("parallelEdges",pnEditor.graphLayout("parallelEdges", false)));
//
//		submenu.addSeparator();
//
//		submenu.add(pnEditor.bind("organicLayout",pnEditor.graphLayout("organicLayout", true)));
//		submenu.add(pnEditor.bind("circleLayout",pnEditor.graphLayout("circleLayout", true)));
	}
}
