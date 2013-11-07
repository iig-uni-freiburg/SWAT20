package de.unifreiburg.iig.bpworkbench2.editor.gui;

import java.awt.MenuBar;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.TransferHandler;

import com.mxgraph.swing.util.mxGraphActions;

import de.unifreiburg.iig.bpworkbench2.editor.gui.actions.HistoryAction;

public class PopupMenu extends JPopupMenu {

    public PopupMenu(PNEditor ptnEditor) {
        boolean selected = !ptnEditor.getGraphComponent().getGraph().isSelectionEmpty();

        add(ptnEditor.bind("Undo", new HistoryAction(true),
                "/images/undo.gif"));

        addSeparator();
        add(ptnEditor.bind("Cut", TransferHandler.getCutAction(),
                "/images/cut.gif")).setEnabled(selected);
        add(ptnEditor.bind("Copy", TransferHandler.getCopyAction(),
                "/images/copy.gif")).setEnabled(selected);
        add(ptnEditor.bind("Paste", TransferHandler.getPasteAction(),
                "/images/paste.gif"));

        addSeparator();
        add(ptnEditor.bind("Delete", mxGraphActions.getDeleteAction(),
                "/images/delete.gif")).setEnabled(selected);

        addSeparator();

        JMenu menu = (JMenu) add(new JMenu("Format"));
//        MenuBar.populateFormatMenu(menu, ptnEditor);
        add(ptnEditor.bind("Edit", mxGraphActions.getEditAction())).setEnabled(selected);

        addSeparator();
        add(ptnEditor.bind("selectVertices", mxGraphActions.getSelectVerticesAction()));
        add(ptnEditor.bind("selectEdges", mxGraphActions.getSelectEdgesAction()));
    }
}
