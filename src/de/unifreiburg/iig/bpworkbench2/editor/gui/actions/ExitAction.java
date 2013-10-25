package de.unifreiburg.iig.bpworkbench2.editor.gui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import de.unifreiburg.iig.bpworkbench2.editor.gui.PTNEditor;
import de.unifreiburg.iig.bpworkbench2.editor.soul.Constants;
import de.unifreiburg.iig.bpworkbench2.editor.soul.Properties;

public class ExitAction extends AbstractAction {

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
        String filename = "New Diagram";
        try {
            filename = editor.getCurrentFile().getName();
        } catch (Exception ex) {
            //do nothing
        }
        if (editor != null) {
            if (editor.isModified()) {
                int answer = JOptionPane.showConfirmDialog(editor,
                        "File \"" + filename + "\" is modified. Save it ?");
                if (answer == JOptionPane.YES_OPTION) {
                    if (editor.getCurrentFile() == null) {
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
            FileOutputStream fos = new FileOutputStream(Constants.CONFIG_FILE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(Properties.getInstance());
            oos.flush();
            oos.close();
        } catch (Exception ex) {
        }
        System.exit(0);
    }
}
