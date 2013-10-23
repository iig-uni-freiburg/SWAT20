package de.unifreiburg.iig.bpworkbench2.editor.gui.actions;

import com.mxgraph.io.*;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.*;
import com.mxgraph.util.png.mxPngEncodeParam;
import com.mxgraph.util.png.mxPngImageEncoder;
import com.mxgraph.view.mxGraph;

import de.uni.freiburg.iig.telematik.sepia.serialize.PNSerialization;
import de.uni.freiburg.iig.telematik.sepia.serialize.formats.PNSerializationFormat;
import de.unifreiburg.iig.bpworkbench2.editor.gui.*;
import de.unifreiburg.iig.bpworkbench2.editor.mxgraphmod.util.png.*;
import de.unifreiburg.iig.bpworkbench2.editor.soul.Properties;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;

import javax.swing.*;

public class SaveAction extends AbstractAction {

    protected boolean showDialog, success;
    protected String lastDir = de.unifreiburg.iig.bpworkbench2.editor.soul.Properties.getInstance().getLastDir();

    public SaveAction(boolean showDialog) {
        this.showDialog = showDialog;
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

    protected void saveXmlPng(PTNEditor editor, String filename,
            Color bg) throws IOException {
        mxGraphComponent graphComponent = editor.getGraphComponent();
        mxGraph graph = graphComponent.getGraph();

        // Creates the image for the PNG file
        BufferedImage image = mxCellRenderer.createBufferedImage(graph,
                null, 1, bg, graphComponent.isAntiAlias(), null,
                graphComponent.getCanvas());

        // Creates the URL-encoded XML data
        mxCodec codec = new mxCodec();
        String xml = URLEncoder.encode(
                mxUtils.getXml(codec.encode(graph.getModel())), "UTF-8");
        mxPngEncodeParam param = mxPngEncodeParam.getDefaultEncodeParam(image);
        param.setCompressedText(new String[]{"mxGraphModel", xml});

        // Saves as a PNG file
        FileOutputStream outputStream = new FileOutputStream(new File(filename));
        try {
            mxPngImageEncoder encoder = new mxPngImageEncoder(outputStream, param);
            if (image != null) {
                encoder.encode(image);
                editor.setModified(false);
                editor.setCurrentFile(new File(filename));
            } else {
                //some image error
            }
        } finally {
            outputStream.close();
        }
    }

    public void actionPerformed(ActionEvent e) {
        success = false;
        PTNEditor editor = getEditor(e);

        if (editor != null) {
            mxGraphComponent graphComponent = editor.getGraphComponent();
            DefaultFileFilter xmlPngFilter = new DefaultFileFilter(".pnml",
                    "PNML file (.pnml)");
            String filename = null;

            if (showDialog || editor.getCurrentFile() == null) {
                String path;

                if (lastDir != null) {
                    path = lastDir;
                } else if (editor.getCurrentFile() != null) {
                    path = editor.getCurrentFile().getParent();
                } else {
                    path = System.getProperty("user.dir");
                }

                JFileChooser fc = new JFileChooser(path);
                fc.setFileFilter(xmlPngFilter);

                if (fc.showDialog(null, "Save") != JFileChooser.APPROVE_OPTION) {
                    return;
                } else {
                    lastDir = fc.getSelectedFile().getParent();
                    Properties.getInstance().setLastDir(lastDir);
                }

                filename = fc.getSelectedFile().getAbsolutePath();
                String ext = xmlPngFilter.getExtension();

                if (!filename.toLowerCase().endsWith(ext)) {
                    filename += ext;
                }

                if (new File(filename).exists()) {
                    if (JOptionPane.showConfirmDialog(graphComponent,
                            "Overwrite existing file?") != JOptionPane.YES_OPTION) {
                        actionPerformed(e);
                        return;
                    }
                }
            } else {
                filename = editor.getCurrentFile().getAbsolutePath();
            }

            try {
//            	editor.getNetContainer();
        		PNSerialization.serialize(editor.getNetContainer(), PNSerializationFormat.PNML, filename);

//                saveXmlPng(editor, filename, null);
                editor.setModified(false);
                editor.setCurrentFile(new File(filename));
            } catch (Exception ex) {
                //saving error
            }
            success = true;
        }
    }

    public boolean isSuccess() {
        return success;
    }
}
