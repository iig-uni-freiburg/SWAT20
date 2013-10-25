package de.unifreiburg.iig.bpworkbench2.editor.gui.actions;

import com.mxgraph.io.*;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParser;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.unifreiburg.iig.bpworkbench2.editor.gui.*;
import de.unifreiburg.iig.bpworkbench2.editor.mxgraphmod.util.png.mxPNGzTXtDecoder;
import de.unifreiburg.iig.bpworkbench2.editor.soul.Graph;
import de.unifreiburg.iig.bpworkbench2.editor.soul.Properties;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.URLDecoder;
import java.util.Map;

import javax.swing.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class OpenAction extends AbstractAction {

    protected String lastDir = Properties.getInstance().getLastDir();

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

    protected void resetEditor(PTNEditor editor) {
        editor.setModified(false);
        editor.getUndoManager().clear();
        editor.getGraphComponent().zoomAndCenter();
        editor.getControlPanel().reset();
    }

    protected void openXmlPng(PTNEditor editor, File file) throws IOException {
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
               Object test = codec.decode(document.getDocumentElement(), editor.getGraphComponent().getGraph().getModel());
//                editor.setCurrentFile(file);
//                resetEditor(editor);
//                graph.getDataHolder().updateData();
                return;
            }
        }
        JOptionPane.showMessageDialog(editor, "Image contains no diagram data");
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
                fc.setFileFilter( new DefaultFileFilter(".pnml",
                        "PNML file (.pnml)"));

                int rc = fc.showDialog(null, "Open");

                if (rc == JFileChooser.APPROVE_OPTION) {
                    lastDir = fc.getSelectedFile().getParent();
                    Properties.getInstance().setLastDir(lastDir);

                    try {
                    	 
//                        openXmlPng(editor, fc.getSelectedFile());
            			/*
            			 * PetriNet
            			 */
            			AbstractGraphicalPN<?, ?, ?, ?, ?> netContainer = new PNMLParser().parse(fc.getSelectedFile(),
            					true, false);
            			AbstractPetriNet<?, ?, ?, ?, ?> petriNet = netContainer.getPetriNet();

            			
            			//distinguish between different net-types to choose corresponding editor
            			if (petriNet instanceof PTNet) {
//            				editor = new PTNEditor(netContainer);
//            				resetEditor(editor);
//            				editor =  new PTNEditor(netContainer);
            				PTNEditor newEditor = new PTNEditor(netContainer);
            				JPanel frame = (JPanel) editor.getParent();
            				frame.add(newEditor);
            				frame.remove(editor);
            			
//            				frame.setVisible(true);
            				SwingUtilities.updateComponentTreeUI(frame);
//            				newEditor.getParent().remove(editor);
//            				newEditor.getParent().repaint();
////            				editor.setEnabled(false);
//            				newEditor.getParent().setVisible(true);
//            				newEditor.getParent().removeAll();
//            				editor.getGraphComponent().refresh();
//editor.getGraphComponent().refresh(); //wie öffnen realisieren? neuer editor oder in gegebenen graphcomponent aktualisieren?
            			}

                    } catch (IOException ex) {
                        //opening error
                    } catch (ParserException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ParameterException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                }
            }
        }
    }
}
