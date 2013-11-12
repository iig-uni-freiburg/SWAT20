package de.unifreiburg.iig.bpworkbench2.editor;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.TransferHandler;

import com.mxgraph.swing.util.mxGraphActions;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.unifreiburg.iig.bpworkbench2.editor.actions.UndoRedoAction;

public class EditorPopupMenu extends JPopupMenu {
	
	private static final long serialVersionUID = -2983257974918330746L;

	public EditorPopupMenu(PNEditor pnEditor) throws ParameterException {
		Validate.notNull(pnEditor);
		boolean selected = !pnEditor.getGraphComponent().getGraph().isSelectionEmpty();

		add(pnEditor.bind("Undo", new UndoRedoAction(pnEditor, true), "/images/undo.gif"));

		addSeparator();
		add(pnEditor.bind("Cut", TransferHandler.getCutAction(), "/images/cut.gif")).setEnabled(selected);
		add(pnEditor.bind("Copy", TransferHandler.getCopyAction(), "/images/copy.gif")).setEnabled(selected);
		add(pnEditor.bind("Paste", TransferHandler.getPasteAction(), "/images/paste.gif"));

		addSeparator();
		add(pnEditor.bind("Delete", mxGraphActions.getDeleteAction(), "/images/delete.gif")).setEnabled(selected);

		addSeparator();

		JMenu menu = (JMenu) add(new JMenu("Format"));
		// MenuBar.populateFormatMenu(menu, ptnEditor);
		add(pnEditor.bind("Edit", mxGraphActions.getEditAction())).setEnabled(selected);

		addSeparator();
		add(pnEditor.bind("selectVertices", mxGraphActions.getSelectVerticesAction()));
		add(pnEditor.bind("selectEdges", mxGraphActions.getSelectEdgesAction()));
	}
}
