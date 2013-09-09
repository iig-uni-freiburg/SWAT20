package de.unifreiburg.iig.bpworkbench2.editor.gui.actions;

import com.mxgraph.io.*;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;

import de.unifreiburg.iig.bpworkbench2.editor.gui.*;
import de.unifreiburg.iig.bpworkbench2.editor.mxgraphmod.util.png.mxPNGzTXtDecoder;
import de.unifreiburg.iig.bpworkbench2.editor.soul.Graph;
import de.unifreiburg.iig.bpworkbench2.editor.soul.Properties;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.URLDecoder;
import java.util.Map;

import javax.swing.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class OpenAction extends AbstractAction {

    protected String lastDir = Properties.getInstance().getLastDir();

    public static PNMLEditor getEditor(ActionEvent e) {
        if (e.getSource() instanceof Component) {
            Component component = (Component) e.getSource();
            while (component != null && !(component instanceof PNMLEditor)) {
                component = component.getParent();
            }
            return (PNMLEditor) component;
        }
        return null;
    }

    protected void resetEditor(PNMLEditor editor) {
        editor.setModified(false);
        editor.getUndoManager().clear();
        editor.getGraphComponent().zoomAndCenter();
        editor.getControlPanel().reset();
    }

    protected void openXmlPng(PNMLEditor editor, File file) throws IOException {
        Graph graph = (Graph) editor.getGraphComponent().getGraph();
        Map<String, String> text = mxPNGzTXtDecoder.decodezTXt(new FileInputStream(file));
//        System.out.println(text);
        if (text != null) {
            String value = text.get("mxGraphModel");
            if (value != null) {
                Document document = mxUtils.parseXml(URLDecoder.decode(value, "UTF-8"));
                mxCodec codec = new mxCodec(document);
                Element dE = document.getDocumentElement();
                mxIGraphModel model = editor.getGraphComponent().getGraph().getModel();
                System.out.println(dE + "-" + model);
               Object test = codec.decode(document.getDocumentElement(), editor.getGraphComponent().getGraph().getModel());
               System.out.println(test);
//                editor.setCurrentFile(file);
//                resetEditor(editor);
//                graph.getDataHolder().updateData();
                return;
            }
        }
        JOptionPane.showMessageDialog(editor, "Image contains no diagram data");
    }

    public void actionPerformed(ActionEvent e) {
        PNMLEditor editor = getEditor(e);
        String filename = "New Diagram";
        try {
            filename = editor.getCurrentFile().getName();
        } catch (Exception ex) {
            //do nothing
        }
        if (editor != null) {
            int answer = JOptionPane.NO_OPTION;
            if (editor.isModified()) {
                answer = JOptionPane.showConfirmDialog(editor,
                        "File \"" + filename + "\" is modified. Save it ?");
            }
            switch (answer) {
                case JOptionPane.YES_OPTION:
                    SaveAction save;
                    if (editor.getCurrentFile() == null) {
                        save = new SaveAction(true);
                        save.actionPerformed(e);
                    } else {
                        save = new SaveAction(false);
                        save.actionPerformed(e);
                    }
                    break;
                case JOptionPane.CANCEL_OPTION:
                    return;
            }
            mxGraph graph = editor.getGraphComponent().getGraph();

            if (graph != null) {
                String path = (lastDir != null) ? lastDir : System.getProperty("user.dir");

                JFileChooser fc = new JFileChooser(path);
                fc.setFileFilter(new DefaultFileFilter(".png", "XML+PNG file (.png)"));

                int rc = fc.showDialog(null, "Open");

                if (rc == JFileChooser.APPROVE_OPTION) {
                    lastDir = fc.getSelectedFile().getParent();
                    Properties.getInstance().setLastDir(lastDir);

                    try {
                        openXmlPng(editor, fc.getSelectedFile());
                    } catch (IOException ex) {
                        //opening error
                    }
                }
            }
        }
    }
}
