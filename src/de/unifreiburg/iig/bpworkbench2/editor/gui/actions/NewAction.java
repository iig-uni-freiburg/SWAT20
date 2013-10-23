package de.unifreiburg.iig.bpworkbench2.editor.gui.actions;

import com.mxgraph.model.mxCell;

import de.unifreiburg.iig.bpworkbench2.editor.gui.PTNEditor;
import de.unifreiburg.iig.bpworkbench2.editor.soul.Graph;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.*;

public class NewAction extends AbstractAction {

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

    public void clean(PTNEditor editor) {
        Graph graph = (Graph) editor.getGraphComponent().getGraph();
        mxCell root = new mxCell();
        root.insert(new mxCell());
        graph.getModel().setRoot(root);
        editor.setModified(false);
        editor.setCurrentFile(null);
        editor.getGraphComponent().zoomAndCenter();
        graph.getDataHolder().clean();
        editor.getControlPanel().reset();
        editor.getUndoManager().clear();
    }

    public void actionPerformed(ActionEvent e) {
        PTNEditor editor = getEditor(e);
        String filename = "New Diagram";
        try {
            filename = editor.getCurrentFile().getName();
        } catch (Exception ex) {
            //do nothing
        }
        int answer = JOptionPane.NO_OPTION;
        if (editor.isModified()) {
            answer = JOptionPane.showConfirmDialog(editor,
                    "File \"" + filename + "\" is modified. Save it ?");
        }
        if (editor != null) {
            if (!editor.isModified() || answer == JOptionPane.NO_OPTION) {
                clean(editor);
            } else if (answer == JOptionPane.YES_OPTION) {
                SaveAction save;
                if (editor.getCurrentFile() == null) {
                    save = new SaveAction(true);
                    save.actionPerformed(e);
                } else {
                    save = new SaveAction(false);
                    save.actionPerformed(e);
                }
                if (save.isSuccess()) {
                    clean(editor);
                }
            }
        }
    }
}
