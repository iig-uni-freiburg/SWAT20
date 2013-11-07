package de.unifreiburg.iig.bpworkbench2.editor.relict;

//import math.Tree;
import java.awt.event.*;
import java.awt.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
//import net.miginfocom.swing.MigLayout;
//import math.simulation.Simulation;
//import math.simulation.Statistics;

import de.unifreiburg.iig.bpworkbench2.editor.soul.*;

public final class ControlPanel extends JPanel {

    private JTabbedPane root = new JTabbedPane(JTabbedPane.RIGHT),
            tree = new JTabbedPane(),
            simulation = new JTabbedPane();
    private JTable tableDi, tableDq, tableMarking,
            tableTree = new JTable(), tableQuery = new JTable(),
            tableReport = new JTable(), tableProbabilities = new JTable();
    private JPanel diagramPanel = new JPanel(),
            probabilityPanel = new JPanel(),
            markovProbabilityPanel = new JPanel(), treePanel = new JPanel(),
            graphPanel = new JPanel(), markovPanel = new JPanel();
    private Graph graph;

    public ControlPanel(Graph graph) {
        this.graph = graph;
        initComponents();
//        updateMatrix();

        //revalidate size on component resizing
        addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                int width = getWidth() - 10;
                int height = getHeight() - 5;
                root.setPreferredSize(new Dimension(width, height));
                revalidate();
            }
        });

    }

    private void initComponents() {
        //yeap, it's holy shit but dunno the way to make it better
        root.addTab(null,
                new ImageIcon(System.class.getResource("/images/matrixText.png")),
                createMatrixPanel(), "Matrix D");
        root.addTab(null,
                new ImageIcon(System.class.getResource("/images/treeText.png")),
                tree, "Tree analysis");
        root.addTab(null,
                new ImageIcon(System.class.getResource("/images/simulationText.png")),
                simulation, "Simulation results & analysis");

        tree.addTab("Tree", WrapFactory.wrapInScrollpane(treePanel));
        tree.addTab("Graph", WrapFactory.wrapInScrollPaneWithHistory(graphPanel));
        tree.addTab("Markov", WrapFactory.wrapInScrollPaneWithHistory(markovPanel));
        tree.addTab("Table", WrapFactory.wrapInScrollpane(tableTree));

        simulation.addTab("Query", WrapFactory.wrapInScrollpane(tableQuery));
        simulation.addTab("Report", WrapFactory.wrapInScrollpane(tableReport));
        simulation.addTab("Diagrams", WrapFactory.wrapInScrollpane(diagramPanel));
        simulation.addTab("Probability", WrapFactory.wrapInScrollpane(tableProbabilities));
        simulation.addTab("Markov graph", WrapFactory.wrapInScrollpane(markovProbabilityPanel));
        add(root);
    }

    private JPanel createMatrixPanel() {
        JPanel panel = new JPanel();

        JLabel label = new JLabel("Matrix Di:");
        panel.add(label);
        JScrollPane scrollpane = new JScrollPane();
        tableDi = new JTable();
        scrollpane.setPreferredSize(new Dimension(1600, 800));
        scrollpane.setViewportView(tableDi);
        panel.add(scrollpane);

        label = new JLabel("Matrix Dq:");
        panel.add(label);
        scrollpane = new JScrollPane();
        tableDq = new JTable();
        scrollpane.setPreferredSize(new Dimension(1600, 800));
        scrollpane.setViewportView(tableDq);
        panel.add(scrollpane);
        label = new JLabel("Marking:");
        panel.add(label);
        scrollpane = new JScrollPane();
        tableMarking = new JTable();
        scrollpane.setPreferredSize(new Dimension(1600, 40));
        scrollpane.setViewportView(tableMarking);
        scrollpane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        panel.add(scrollpane, "h 40!");
        return panel;
    }

//    public void updateMatrix() {
//        int[][] matrix = graph.getDataHolder().getMatrixDi();
//        int[] marking = graph.getDataHolder().getMarking();
//        if (matrix.length == 0) {
//            matrix = new int[1][0];
//        }
//        if (marking == null) {
//            marking = new int[0];
//        }
//
//        Integer[][] integerMatrix = null;
//        Integer[][] integerMarking = null;
//        integerMatrix = new Integer[matrix.length][matrix[0].length];
//        for (int i = 0; i < integerMatrix.length; i++) {
//            for (int j = 0; j < integerMatrix[i].length; j++) {
//                integerMatrix[i][j] = Integer.valueOf(matrix[i][j]);
//            }
//        }
//        tableDi.setModel(new DefaultTableModel(integerMatrix,
//                graph.getDataHolder().getColumnNames()));
//        matrix = graph.getDataHolder().getMatrixDq();
//        if (matrix.length == 0) {
//            matrix = new int[1][0];
//        }
//        integerMatrix = new Integer[matrix.length][matrix[0].length];
//        for (int i = 0; i < integerMatrix.length; i++) {
//            for (int j = 0; j < integerMatrix[i].length; j++) {
//                integerMatrix[i][j] = Integer.valueOf(matrix[i][j]);
//            }
//        }
//        tableDq.setModel(new DefaultTableModel(integerMatrix, graph.getDataHolder().getColumnNames()));
//        integerMarking = new Integer[1][marking.length];
//        for (int i = 0; i < integerMarking[0].length; i++) {
//            integerMarking[0][i] = Integer.valueOf(marking[i]);
//        }
//        tableMarking.setModel(new DefaultTableModel(integerMarking, graph.getDataHolder().getColumnNames()));
//    }

//    public void updateTree() {
//        cleanTreeTab();
////        treePanel.add(new Tree(graph).createTree());
////        treePanel.revalidate();
////        treePanel.repaint();
//
//        graphPanel.add(Factory.createTreeGraph(graph));
//        graphPanel.revalidate();
//        graphPanel.repaint();
//
//        markovPanel.add(Factory.createMarkovGraph(graph));
//        markovPanel.revalidate();
//        markovPanel.repaint();
//
//        tableTree.setModel(Factory.createTreeTable(graph));
//    }

//    public void updateSimulation(Simulation s) {
//        cleanSimulationTab();
//        Statistics statistics = new Statistics(s);
//        tableQuery.setModel(Factory.createQueryTable(s.getQuery()));
//        tableReport.setModel(Factory.createReportTable(statistics));
//        diagramPanel.add(Factory.createDiagramPanel(statistics));
//        tableProbabilities.setModel(Factory.createProbabilityTable(statistics));
//        markovProbabilityPanel.add(Factory.createMarkovProbabilityGraph(statistics));
//
//    }


    public void reset() {
//        updateMatrix();
        cleanTreeTab();
        cleanSimulationTab();
    }

    private void clean(JComponent component) {
        int counter = component.getComponentCount();
        for (int i = 0; i < counter; i++) {
            component.remove(0);
        }
        component.revalidate();
        component.repaint();
    }

    private void cleanTreeTab() {
        clean(treePanel);
        clean(graphPanel);
        clean(markovPanel);
        tableTree.setModel(new DefaultTableModel());
    }

    private void cleanSimulationTab() {
        tableQuery.setModel(new DefaultTableModel());
        tableReport.setModel(new DefaultTableModel());
        clean(diagramPanel);
        tableProbabilities.setModel(new DefaultTableModel());
        clean(markovProbabilityPanel);
    }
}
