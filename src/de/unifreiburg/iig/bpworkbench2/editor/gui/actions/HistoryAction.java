package de.unifreiburg.iig.bpworkbench2.editor.gui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import de.unifreiburg.iig.bpworkbench2.editor.gui.PTNEditor;
import de.unifreiburg.iig.bpworkbench2.editor.soul.Graph;

public class HistoryAction extends AbstractAction {

    protected boolean undo;

    public HistoryAction(boolean undo) {
        this.undo = undo;
    }

    public static PTNEditor getEditor(ActionEvent e) {
        if (e.getSource() instanceof Component) {
            Component component = (Component) e.getSource();
            while (component != null && !(component instanceof PTNEditor)) {
                component = component.getParent();
            }
            return (PTNEditor) component;
        }
        return null;
    }

    public void actionPerformed(ActionEvent e) {
        PTNEditor editor = getEditor(e);

        if (editor != null) {
            if (undo) {
                editor.getUndoManager().undo();
            } else {
                editor.getUndoManager().redo();
            }
            Graph graph = (Graph) editor.getGraphComponent().getGraph();
            graph.getDataHolder().updateData();
        }
    }
}
