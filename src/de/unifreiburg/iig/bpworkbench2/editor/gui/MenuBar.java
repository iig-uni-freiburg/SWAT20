package de.unifreiburg.iig.bpworkbench2.editor.gui;

import java.awt.event.*;
import java.io.File;

import javax.swing.*;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxGraphActions;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;





import de.unifreiburg.iig.bpworkbench2.editor.gui.actions.*;
import de.unifreiburg.iig.bpworkbench2.editor.soul.Graph;
import de.unifreiburg.iig.bpworkbench2.editor.soul.Properties;

public class MenuBar extends JMenuBar {

    public MenuBar(final PTNEditor editor) {
        final mxGraphComponent graphComponent = editor.getGraphComponent();
        final mxGraph graph = graphComponent.getGraph();
        JMenu menu = null;
        JMenu submenu = null;
        JMenuItem item = null;

        menu = add(new JMenu("File"));
        menu.add(editor.bind("New", new NewAction(), "/images/new.gif"));
        menu.add(editor.bind("Open...", new OpenAction(), "/images/open.gif"));
        menu.addSeparator();
        menu.add(editor.bind("Save", new de.unifreiburg.iig.bpworkbench2.editor.gui.actions.SaveAction(false), "/images/save.gif"));
        menu.add(editor.bind("Save As...", new de.unifreiburg.iig.bpworkbench2.editor.gui.actions.SaveAction(true), "/images/saveas.gif"));
        menu.addSeparator();
        menu.add(editor.bind("Exit", new de.unifreiburg.iig.bpworkbench2.editor.gui.actions.ExitAction()));

        menu = add(new JMenu("Edit"));
        menu.add(editor.bind("Undo", new de.unifreiburg.iig.bpworkbench2.editor.gui.actions.HistoryAction(true), "/images/undo.gif"));
        menu.add(editor.bind("Redo", new de.unifreiburg.iig.bpworkbench2.editor.gui.actions.HistoryAction(false), "/images/redo.gif"));
        menu.addSeparator();
        menu.add(editor.bind("Cut", TransferHandler.getCutAction(), "/images/cut.gif"));
        menu.add(editor.bind("Copy", TransferHandler.getCopyAction(), "/images/copy.gif"));
        menu.add(editor.bind("Paste", TransferHandler.getPasteAction(), "/images/paste.gif"));
        menu.addSeparator();
        menu.add(editor.bind("Delete", new de.unifreiburg.iig.bpworkbench2.editor.gui.actions.DeleteAction("delete"), "/images/delete.gif"));

        menu = add(new JMenu("Format"));
        populateFormatMenu(menu, editor);

//        menu = add(new JMenu("Layout"));
//        menu.add(editor.graphLayout("verticalHierarchical", true));
//        menu.add(editor.graphLayout("horizontalHierarchical", true));
//        menu.addSeparator();
//        menu.add(editor.graphLayout("verticalStack", false));
//        menu.add(editor.graphLayout("horizontalStack", false));
//        menu.addSeparator();
//        menu.add(editor.graphLayout("verticalTree", true));
//        menu.add(editor.graphLayout("horizontalTree", true));
//        menu.addSeparator();
//        menu.add(editor.graphLayout("placeEdgeLabels", false));
//        menu.add(editor.graphLayout("parallelEdges", false));
//        menu.addSeparator();
//        menu.add(editor.graphLayout("organicLayout", true));
//        menu.add(editor.graphLayout("circleLayout", true));

        menu = add(new JMenu("Analysis"));
        submenu = (JMenu) menu.add(new JMenu("Tree"));

        submenu.add(new JCheckBoxMenuItem("Show descriptions") {

            {
                setSelected(Properties.getInstance().isTreeShowDescriptions());
                addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        Properties.getInstance().setTreeShowDescriptions(isSelected());
//                        editor.getControlPanel().updateTree();
                    }
                });
            }
        });
        submenu.add(new JCheckBoxMenuItem("Skip immediates") {

            {
                setSelected(Properties.getInstance().isTreeSkipImmediates());
                addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        Properties.getInstance().setTreeSkipImmediates(isSelected());
//                        editor.getControlPanel().updateTree();
                    }
                });
            }
        });
        submenu.add(new JSeparator());
        submenu.add(new AbstractAction("Create tree") {

            public void actionPerformed(ActionEvent e) {
                try {
//                    editor.getControlPanel().updateTree();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(graphComponent, "Error");
                }
            }
        });

        submenu = (JMenu) menu.add(new JMenu("Simulation"));
//        submenu.add(new JCheckBoxMenuItem("Show probabilities") {
//
//            {
//                setSelected(Properties.getInstance().isDivide());
//                addActionListener(new ActionListener() {
//
//                    public void actionPerformed(ActionEvent e) {
//                        try {
//                            Properties.getInstance().setDivide(isSelected());
//                            Simulation s = new Simulation((Graph) editor.getGraphComponent().getGraph());
//                            s.run();
//                            editor.getControlPanel().updateSimulation(s);
//                        } catch (Exception ex) {
//                            JOptionPane.showMessageDialog(null, "Error. Make sure graph isn't empty and doesn't have terminal vertices");
//                        }
//                    }
//                });
//            }
//        });
        
     
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        submenu.add(new AbstractAction("Set test length...") {

            public void actionPerformed(ActionEvent e) {
                Properties.getInstance().setTests(Integer.valueOf(JOptionPane.showInputDialog("Input test length")));
            }
        });
        submenu.add(new AbstractAction("Set delta T...") {

            public void actionPerformed(ActionEvent e) {
                Properties.getInstance().setDeltaT(Double.valueOf(JOptionPane.showInputDialog("Input delta T")));
            }
        });
        submenu.add(new AbstractAction("Set t0...") {

            public void actionPerformed(ActionEvent e) {
                Properties.getInstance().setT0(Double.valueOf(JOptionPane.showInputDialog("Input t0")));
            }
        });
        submenu.add(new JSeparator());
//        submenu.add(new AbstractAction("Run...") {
//
//            public void actionPerformed(ActionEvent e) {
//                try {
//                    Simulation s = new Simulation((Graph) editor.getGraphComponent().getGraph());
//                    s.run();
//                    editor.getControlPanel().updateSimulation(s);
//                } catch (Exception ex) {
//                    JOptionPane.showMessageDialog(null, "Error. Make sure graph isn't empty and doesn't have terminal vertices");
//                }
//            }
//        });

//        menu = add(new JMenu("Options"));
//        submenu = (JMenu) menu.add(new JMenu("Connections"));
//        submenu.add(new TogglePropertyItem(graphComponent, ("connectable"), "Connectable"));
//        submenu.add(new TogglePropertyItem(graph, "Connectable Edges", "ConnectableEdges"));
//        submenu.addSeparator();
//        submenu.add(new TogglePropertyItem(graph, "Disconnect On Move", "DisconnectOnMove"));
//        submenu = (JMenu) menu.add(new JMenu("Validation"));
//        submenu.addSeparator();
//        submenu.add(new TogglePropertyItem(graph, "Allow Loops", "AllowLoops"));
//        submenu.add(new TogglePropertyItem(graph, "Multigraph", "Multigraph"));

        menu = add(new JMenu(("Help")));
        item = menu.add(new JMenuItem(("About")));
        item.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                editor.about();
            }
        });
    }

    public static void populateFormatMenu(JMenu menu, PTNEditor pnmlEditor) {
        JMenu submenu = (JMenu) menu.add(new JMenu("Label"));
        submenu.add(pnmlEditor.bind("Rotate Label",
                new ToggleAction(mxConstants.STYLE_HORIZONTAL, true)));
        submenu.add(pnmlEditor.bind(("Hide"), new ToggleAction(
                mxConstants.STYLE_NOLABEL)));

        submenu = (JMenu) menu.add(new JMenu(("Connector")));
        submenu.add(pnmlEditor.bind("Straight",
                new SetStyleAction("straight"),
                "/images/straight.gif"));
        submenu.add(pnmlEditor.bind("Horizontal",
                new SetStyleAction(""),
                "/images/connect.gif"));
        submenu.add(pnmlEditor.bind("Vertical",
                new SetStyleAction("vertical"),
                "/images/vertical.gif"));
        submenu.add(pnmlEditor.bind("Entity Relation",
                new SetStyleAction("edgeStyle=mxEdgeStyle.EntityRelation"),
                "/images/entity.gif"));
        submenu.add(pnmlEditor.bind("Arrow", new SetStyleAction(
                "arrow"), "/images/arrow.gif"));
        submenu.add(pnmlEditor.bind("Plain", new ToggleAction(
                mxConstants.STYLE_NOEDGESTYLE)));

        menu.addSeparator();
        menu.add(pnmlEditor.bind("Rounded", new ToggleAction(
                mxConstants.STYLE_ROUNDED)));
    }
}
