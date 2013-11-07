package de.unifreiburg.iig.bpworkbench2.editor.gui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import de.unifreiburg.iig.bpworkbench2.editor.gui.PNEditor;

public class PrintAction extends AbstractAction {

    protected boolean showDialog, success;

    public PrintAction() {
    
    }

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

//    protected void saveXmlPng(PTNEditor editor, String filename,
//            Color bg) throws IOException {
//        mxGraphComponent graphComponent = editor.getGraphComponent();
//        mxGraph graph = graphComponent.getGraph();
//
//        // Creates the image for the PNG file
//        BufferedImage image = mxCellRenderer.createBufferedImage(graph,
//                null, 1, bg, graphComponent.isAntiAlias(), null,
//                graphComponent.getCanvas());
//
//        // Creates the URL-encoded XML data
//        mxCodec codec = new mxCodec();
//        String xml = URLEncoder.encode(
//                mxUtils.getXml(codec.encode(graph.getModel())), "UTF-8");
//        mxPngEncodeParam param = mxPngEncodeParam.getDefaultEncodeParam(image);
//        param.setCompressedText(new String[]{"mxGraphModel", xml});
//
//        // Saves as a PNG file
//        FileOutputStream outputStream = new FileOutputStream(new File(filename));
//        try {
//            mxPngImageEncoder encoder = new mxPngImageEncoder(outputStream, param);
//            if (image != null) {
//                encoder.encode(image);
//                editor.setModified(false);
//                editor.setCurrentFile(new File(filename));
//            } else {
//                //some image error
//            }
//        } finally {
//            outputStream.close();
//        }
//    }

    public void actionPerformed(ActionEvent e) {
        success = false;
        System.out.println("\n//PRINT NET INFORMATION//\n");
        PNEditor editor = getEditor(e);
       System.out.println(editor.getNetContainer().getPetriNet());
       System.out.println(editor.getNetContainer().getPetriNetGraphics());
       System.out.println("////////////////////////");
//        System.out.println("blub");
        
//
//        if (editor != null) {
//            mxGraphComponent graphComponent = editor.getGraphComponent();
//            DefaultFileFilter xmlPngFilter = new DefaultFileFilter(".png",
//                    "PNG+XML file (.png)");
//            String filename = null;
//
//            if (showDialog || editor.getCurrentFile() == null) {
//                String path;
//
//                if (lastDir != null) {
//                    path = lastDir;
//                } else if (editor.getCurrentFile() != null) {
//                    path = editor.getCurrentFile().getParent();
//                } else {
//                    path = System.getProperty("user.dir");
//                }
//
//                JFileChooser fc = new JFileChooser(path);
//                fc.setFileFilter(xmlPngFilter);
//
//                if (fc.showDialog(null, "Save") != JFileChooser.APPROVE_OPTION) {
//                    return;
//                } else {
//                    lastDir = fc.getSelectedFile().getParent();
//                    Properties.getInstance().setLastDir(lastDir);
//                }
//
//                filename = fc.getSelectedFile().getAbsolutePath();
//                String ext = xmlPngFilter.getExtension();
//
//                if (!filename.toLowerCase().endsWith(ext)) {
//                    filename += ext;
//                }
//
//                if (new File(filename).exists()) {
//                    if (JOptionPane.showConfirmDialog(graphComponent,
//                            "Overwrite existing file?") != JOptionPane.YES_OPTION) {
//                        actionPerformed(e);
//                        return;
//                    }
//                }
//            } else {
//                filename = editor.getCurrentFile().getAbsolutePath();
//            }
//
//            try {
//                saveXmlPng(editor, filename, null);
//                editor.setModified(false);
//                editor.setCurrentFile(new File(filename));
//            } catch (Exception ex) {
//                //saving error
//            }
//            success = true;
//        }
    }

    public boolean isSuccess() {
        return success;
    }
}
