package de.unifreiburg.iig.bpworkbench2.editor.gui;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.*;

import com.mxgraph.swing.util.*;

import de.unifreiburg.iig.bpworkbench2.editor.gui.actions.*;
import de.unifreiburg.iig.bpworkbench2.editor.soul.Graph;
import de.unifreiburg.iig.bpworkbench2.editor.soul.Properties;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;

import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
//import math.simulation.Simulation;

public class ToolBar extends JToolBar {

    public ToolBar(final PTNEditor pnmlEditor, int orientation) {
        super(orientation);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(3, 3, 3, 3),
                getBorder()));
        setFloatable(false);

        add(pnmlEditor.bind("New", new NewAction(), "/images/new.gif"));
        add(pnmlEditor.bind("Open...", new OpenAction(), "/images/open.gif"));
        add(pnmlEditor.bind("Save", new SaveAction(false), "/images/save.gif"));
        addSeparator();

        add(pnmlEditor.bind("Cut", TransferHandler.getCutAction(), "/images/cut.gif"));
        add(pnmlEditor.bind("Copy", TransferHandler.getCopyAction(), "/images/copy.gif"));
        add(pnmlEditor.bind("Paste", TransferHandler.getPasteAction(), "/images/paste.gif"));
        addSeparator();

        add(pnmlEditor.bind("Delete", new DeleteAction("delete"), "/images/delete.gif"));
        addSeparator();

        add(pnmlEditor.bind("Undo", new HistoryAction(true), "/images/undo.gif"));
        add(pnmlEditor.bind("Redo", new HistoryAction(false), "/images/redo.gif"));

        addSeparator();
//        Action temp = new AbstractAction("", new ImageIcon(Editor.class.getResource("/images/connector.gif"))) {
//
//            public void actionPerformed(ActionEvent e) {
//                Simulation s = new Simulation((Graph) editor.getGraphComponent().getGraph());
////                s.step();
//            }
//        };
//        add(temp);
//        add(new JLabel("Steps:"));
//        final JTextField field = new JTextField(String.valueOf(Properties.getInstance().getTests()), 8);
//        field.setMaximumSize(new Dimension(50, 50));
//        field.addKeyListener(new KeyAdapter() {
//
//            @Override
//            public void keyTyped(KeyEvent e) {
//                char c = e.getKeyChar();
//                if (!Character.isDigit(c)) {
//                    e.consume();
//                }
//                super.keyPressed(e);
//                SwingUtilities.invokeLater(new Thread() {
//
//                    @Override
//                    public void run() {
//                        if (!field.getText().equals("")) {
//                            Properties.getInstance().setTests(Integer.parseInt(field.getText()));
//                        }
//                    }
//                });
//            }
//        });
//        add(field);
//        Action temp = new AbstractAction("", new ImageIcon(PNMLEditor.class.getResource("/images/connector.gif"))) {
//
//            public void actionPerformed(ActionEvent e) {
//
////                try {
////                    Simulation s = new Simulation((Graph) editor.getGraphComponent().getGraph());
////                    s.run();
////                    editor.getControlPanel().updateSimulation(s);
////                } catch (Exception ex) {
////                    JOptionPane.showMessageDialog(null, "Error. Make sure graph isn't empty and doesn't have terminal vertices");
////                }
//
//            }
//        };
//        add(temp);
    }
}
