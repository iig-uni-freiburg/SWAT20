package de.unifreiburg.iig.bpworkbench2.editor.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import de.unifreiburg.iig.bpworkbench2.editor.graph.MXConstants;
import de.unifreiburg.iig.bpworkbench2.editor.gui.PNEditor;

public class ExitAction extends AbstractAction {

    public static PNEditor getEditor(ActionEvent e) {
        if (e.getSource() instanceof Component) {
            Component component = (Component) e.getSource();
            while (component != null && !(component instanceof PNEditor)) {
                component = component.getParent();
            }
            return (PNEditor) component;
        }
        return null;
    }

    public void actionPerformed(ActionEvent e) {
        PNEditor editor = getEditor(e);
        String filename = "New Diagram";
        try {
            filename = editor.getFileReference().getName();
        } catch (Exception ex) {
            //do nothing
        }
        if (editor != null) {
            if (editor.isModified()) {
                int answer = JOptionPane.showConfirmDialog(editor,
                        "File \"" + filename + "\" is modified. Save it ?");
                if (answer == JOptionPane.YES_OPTION) {
                    if (editor.getFileReference() == null) {
                        new SaveAction(true).actionPerformed(e);
                    } else {
                        new SaveAction(false).actionPerformed(e);
                    }
                }
                if (answer == JOptionPane.CANCEL_OPTION) {
                    return;
                }
            }
        }
        try {
            FileOutputStream fos = new FileOutputStream(MXConstants.CONFIG_FILE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(Properties.getInstance());
            oos.flush();
            oos.close();
        } catch (Exception ex) {
        }
        System.exit(0);
    }
}
