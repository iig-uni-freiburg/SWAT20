package de.unifreiburg.iig.bpworkbench2.editor.gui;

import javax.swing.*;

import com.mxgraph.swing.util.mxGraphActions;

import de.unifreiburg.iig.bpworkbench2.editor.gui.actions.*;
import de.unifreiburg.iig.bpworkbench2.editor.soul.Properties;

public class PopupMenu extends JPopupMenu {

    public PopupMenu(PNMLEditor pnmlEditor) {
        boolean selected = !pnmlEditor.getGraphComponent().getGraph().isSelectionEmpty();

        add(pnmlEditor.bind("Undo", new HistoryAction(true),
                "/images/undo.gif"));

        addSeparator();
        add(pnmlEditor.bind("Cut", TransferHandler.getCutAction(),
                "/images/cut.gif")).setEnabled(selected);
        add(pnmlEditor.bind("Copy", TransferHandler.getCopyAction(),
                "/images/copy.gif")).setEnabled(selected);
        add(pnmlEditor.bind("Paste", TransferHandler.getPasteAction(),
                "/images/paste.gif"));

        addSeparator();
        add(pnmlEditor.bind("Delete", mxGraphActions.getDeleteAction(),
                "/images/delete.gif")).setEnabled(selected);

        addSeparator();

        JMenu menu = (JMenu) add(new JMenu("Format"));
        MenuBar.populateFormatMenu(menu, pnmlEditor);
        add(pnmlEditor.bind("Edit", mxGraphActions.getEditAction())).setEnabled(selected);

        addSeparator();
        add(pnmlEditor.bind("selectVertices", mxGraphActions.getSelectVerticesAction()));
        add(pnmlEditor.bind("selectEdges", mxGraphActions.getSelectEdgesAction()));
    }
}
