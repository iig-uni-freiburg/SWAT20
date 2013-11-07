package de.unifreiburg.iig.bpworkbench2.editor.relict;

//import math.Tree;
import com.mxgraph.io.mxCodec;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.*;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;

import de.unifreiburg.iig.bpworkbench2.editor.gui.*;
import de.unifreiburg.iig.bpworkbench2.editor.gui.actions.ExitAction;
import de.unifreiburg.iig.bpworkbench2.editor.soul.MXConstants;


import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.*;

//import org.jfree.chart.*;
//import org.jfree.chart.plot.*;
//import org.jfree.chart.renderer.category.BarRenderer;
//import org.jfree.data.category.DefaultCategoryDataset;
import org.w3c.dom.Document;


public abstract class Factory {

    //data for recursion
    private static Object parent, previous;

    public static JFrame createFrame() {
        JFrame frame = new JFrame();
        final PNEditor editor = new PNEditor();
        frame.add(editor);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                //is there a better way?
                ActionEvent event = new ActionEvent(editor, e.getID(), null);
                new ExitAction().actionPerformed(event);
            }
        });
        frame.setJMenuBar(new de.unifreiburg.iig.bpworkbench2.editor.relict.MenuBar(editor));
        frame.setSize(870, 640);
        frame.setLocationRelativeTo(null);
        editor.updateTitle();
        return frame;
    }

//    public static mxGraphComponent createTreeGraph(Graph graph) {
//        mxGraph out = new mxGraph() {
//
//            {
//                setCellsDeletable(false);
//                setCellsDisconnectable(false);
//                setCellsResizable(false);
//                setCellsEditable(false);
//                setCellsLocked(true);
//                setCellsSelectable(false);
//            }
//        };
////        JTree tree = new Tree(graph).createTree();
//        parent = out.getDefaultParent();
//        TreeModel model = tree.getModel();
//        DefaultMutableTreeNode node = (DefaultMutableTreeNode) model.getRoot();
//        out.getModel().beginUpdate();
//        try {
//            NodeInfo info = (NodeInfo) node.getUserObject();
//            int length = (int) (info.getName().length() * 7);
//            previous = out.insertVertex(parent, null, info.getName(), 0, 0, length,
//                    20, "strokeColor=#000000;fillColor=" + Constants.ROOT_COLOR + ";rounded=1;fontColor=" + Constants.ROOT_FONT_COLOR);
//        } finally {
//            out.getModel().endUpdate();
//        }
//        createGraphChildren(out, node);
//        mxHierarchicalLayout layout = new mxHierarchicalLayout(out);
//        layout.execute(parent);
//        mxGraphComponent graphComponent = new mxGraphComponent(out) {
//
//            {
//                mxCodec codec = new mxCodec();
//                Document doc = mxUtils.loadDocument(Tree.class.getResource(
//                        "/default-style.xml").toString());
//                codec.decode(doc.getDocumentElement(), graph.getStylesheet());
//                setBorder(null);
//            }
//        };
//        try {
//            BufferedImage image = mxCellRenderer.createBufferedImage(out,
//                    null, 1, null, graphComponent.isAntiAlias(), null,
//                    graphComponent.getCanvas());
//            FileOutputStream outputStream = null;
//            outputStream = new FileOutputStream(new File("!!tree.png"));
//            try {
//                mxPngImageEncoder encoder = new mxPngImageEncoder(outputStream, null);
//                if (image != null) {
//                    encoder.encode(image);
//                } else {
//                    //some image error
//                }
//            } finally {
//                outputStream.close();
//            }
//        } catch (Exception ex) {
//            System.out.println("smth wrong with image autosaving");
//        }
//        return graphComponent;
//    }
//
//    private static void createGraphChildren(mxGraph out, DefaultMutableTreeNode node) {
//        Object cycleParent = null;
//        for (int i = 0; i < node.getChildCount(); i++) {
//            if (node.getChildCount() > 1) {
//                cycleParent = previous;
//            }
//            DefaultMutableTreeNode newnode = (DefaultMutableTreeNode) node.getChildAt(i);
//            out.getModel().beginUpdate();
//            try {
//                NodeInfo info = (NodeInfo) newnode.getUserObject();
//                int length = (int) (info.getName().length() * 7);
//                String color = "#ffffff";
//                if (info.isTerminal()) {
//                    color = Constants.TERMINAL_COLOR;
//                }
//                if (info.isImmediate()) {
//                    color = Constants.IMMEDIATE_COLOR;
//                }
//                int fontStyle = 0;
//                fontStyle += (info.isDuplicate()) ? 2 : 0;
//                fontStyle += (info.isCovering()) ? 1 : 0;
//                color += ";fontStyle=" + fontStyle;
//                Object current = out.insertVertex(parent, null, info.getName(), 0, 0, length,
//                        18, "strokeColor=#000000;fillColor=" + color + ";rounded=1");
//                out.insertEdge(parent, null, null, previous, current);
//                previous = current;
//            } finally {
//                out.getModel().endUpdate();
//            }
//            createGraphChildren(out, newnode);
//            previous = cycleParent;
//        }
//    }

//    public static mxGraphComponent createMarkovGraph(Graph graph) {
//        mxGraph out = new mxGraph() {
//
//            {
//                setCellsDeletable(false);
//                setCellsDisconnectable(false);
//                setCellsResizable(false);
//                setCellsEditable(false);
//            }
//        };
////        JTree tree = new Tree(graph).createTree();
//        parent = out.getDefaultParent();
//        TreeModel model = tree.getModel();
//        DefaultMutableTreeNode node = (DefaultMutableTreeNode) model.getRoot();
//        out.getModel().beginUpdate();
//        try {
//            NodeInfo info = (NodeInfo) node.getUserObject();
//            int length = (int) (info.getName().length() * 7);
//            previous = out.insertVertex(parent, null, info.getName(), 0, 0, length,
//                    18, "strokeColor=#000000;fillColor=" + Constants.ROOT_COLOR + ";rounded=1;fontColor=" + Constants.ROOT_FONT_COLOR);
//        } finally {
//            out.getModel().endUpdate();
//        }
//        createMarkovGraphChildren(out, node);
//        mxCircleLayout layout = new mxCircleLayout(out);
//        layout.execute(parent);
//        mxParallelEdgeLayout layoutEdges = new mxParallelEdgeLayout(out, 40);
//        layoutEdges.execute(parent);
//        mxGraphComponent graphComponent = new mxGraphComponent(out) {
//
//            {
//                mxCodec codec = new mxCodec();
//                Document doc = mxUtils.loadDocument(Tree.class.getResource(
//                        "/default-style.xml").toString());
//                codec.decode(doc.getDocumentElement(), graph.getStylesheet());
//                setBorder(null);
//            }
//        };
//        try {
//            BufferedImage image = mxCellRenderer.createBufferedImage(out,
//                    null, 1, null, graphComponent.isAntiAlias(), null,
//                    graphComponent.getCanvas());
//            FileOutputStream outputStream = null;
//            outputStream = new FileOutputStream(new File("!!markov.png"));
//            try {
//                mxPngImageEncoder encoder = new mxPngImageEncoder(outputStream, null);
//                if (image != null) {
//                    encoder.encode(image);
//                } else {
//                    //some image error
//                }
//            } finally {
//                outputStream.close();
//            }
//        } catch (Exception ex) {
//            System.out.println("smth wrong with image autosaving");
//        }
//        return graphComponent;
//    }

    public static void createMarkovGraphChildren(mxGraph out, DefaultMutableTreeNode node) {
        Object cycleParent = null;
        for (int i = 0; i < node.getChildCount(); i++) {
            if (node.getChildCount() > 1) {
                cycleParent = previous;
            }
            DefaultMutableTreeNode newnode = (DefaultMutableTreeNode) node.getChildAt(i);
            out.getModel().beginUpdate();
            try {
                int length = (int) (((NodeInfo) node.getUserObject()).getName().length() * 7);
                boolean found = false;
                Object[] cells = out.getChildCells(out.getDefaultParent());
                Object current = null;
                for (Object object : cells) {
                    mxCell cell = (mxCell) object;
                    if (!cell.isEdge()) {
                        if ((cell.getValue()).toString().equals(((NodeInfo) newnode.getUserObject()).getName())) {
                            found = true;
                            current = cell;
                            break;
                        }
                    }
                }
                if (!found) {
                    NodeInfo info = (NodeInfo) newnode.getUserObject();
                    String color = "#ffffff";
                    if (info.isTerminal()) {
                        color = MXConstants.TERMINAL_COLOR;
                    }
                    if (info.isImmediate()) {
                        color = MXConstants.IMMEDIATE_COLOR;
                    }
                    int fontStyle = 0;
                    fontStyle += (info.isDuplicate()) ? 2 : 0;
                    fontStyle += (info.isCovering()) ? 1 : 0;
                    color += ";fontStyle=" + fontStyle;
                    current = out.insertVertex(parent, null, info.getName(), 0, 0, length,
                            18, "strokeColor=#000000;fillColor=" + color + ";rounded=1");
                }
                out.insertEdge(parent, null, ((NodeInfo) newnode.getUserObject()).getFullPath(), previous, current);
                previous = current;
            } finally {
                out.getModel().endUpdate();
            }
            createMarkovGraphChildren(out, newnode);
            previous = cycleParent;
        }
    }

//    public static DefaultTableModel createTreeTable(Graph graph) {
//        Tree tree = new Tree(graph);
//        boolean buffer = Properties.getInstance().isTreeSkipImmediates();
//        Properties.getInstance().setTreeSkipImmediates(true);
//        tree.createTree();
//        Properties.getInstance().setTreeSkipImmediates(buffer);
//        ArrayList<NodeInfo> everyone = tree.getEveryone();
//        Object[][] omegaArray = new Object[everyone.size() - tree.getDeleted()][5];
//        String[] columnNames = {"Level", "Start", "Path", "End", "Description"};
//        int i = 0;
//        for (NodeInfo info : everyone) {
//            if (!info.isImmediate()) {
//                DefaultMutableTreeNode node = info.getNode();
//                String start;
//                try {
//                    NodeInfo parentInfo = (NodeInfo) ((DefaultMutableTreeNode) node.getParent()).getUserObject();
//                    start = parentInfo.getName();
//                } catch (Exception ex) {
//                    start = "root";
//                }
//                omegaArray[i][0] = node.getLevel();
//                omegaArray[i][1] = start;
//                omegaArray[i][2] = info.getFullPath();
//                omegaArray[i][3] = info.getName();
//                omegaArray[i][4] = info.getDescription();
//                i++;
//            }
//        }
//        return new DefaultTableModel(omegaArray, columnNames);
//    }

//    public static DefaultTableModel createReportTable(Statistics statistics) {
//        String[] columnNames = {"Number", "Code", "Frequency", "Stay time", "Return time", "Probability"};
//        Object[][] omegaArray = new Object[statistics.getFrequency().length][columnNames.length];
//        for (int i = 0; i < statistics.getFrequency().length; i++) {
//            omegaArray[i][0] = "M" + i;
//            omegaArray[i][1] = statistics.getExisting().get(i);
//            omegaArray[i][2] = statistics.getFrequency()[i];
//            omegaArray[i][3] = statistics.getStayTime()[i];
//            omegaArray[i][4] = statistics.getReturnTime()[i];
//            omegaArray[i][5] = statistics.getProbability()[i];
//        }
//        return new DefaultTableModel(omegaArray, columnNames);
//    }
//
//    public static JPanel createDiagramPanel(Statistics statistics) {
//        JPanel panel = new JPanel(new MigLayout());
//        panel.add(createChart("Frequency", statistics.getFrequency()), "wrap");
//        panel.add(createChart("Stay time", statistics.getAverageStayTime()), "wrap");
//        panel.add(createChart("Return time", statistics.getAverageReturnTime()), "wrap");
//        panel.add(createChart("Probability", statistics.getProbability()), "wrap");
//        return panel;
//    }
//
//    public static DefaultTableModel createQueryTable(ArrayList<Event> list) {
//        String[] columnNames = {"Number", "Time", "Type", "Task", "Marking"};
//        Object[][] omegaArray = new Object[list.size()][columnNames.length];
//        Utils.numberFormat.setMaximumFractionDigits(6);
//        for (int i = 0; i < list.size(); i++) {
//            Event event = list.get(i);
//            omegaArray[i][0] = event.getNumber();
//            omegaArray[i][1] = Utils.numberFormat.format(event.getTime());
//            omegaArray[i][2] = event.getType();
//            omegaArray[i][3] = event.getTask();
//            omegaArray[i][4] = event.getMarking();
//        }
//
//        return new DefaultTableModel(omegaArray, columnNames);
//    }
//
//    public static DefaultTableModel createProbabilityTable(Statistics statistics) {
//        ArrayList<String> list = new ArrayList<String>();
//        for (Event event : statistics.getQuery()) {
//            if (event.getType().equals(Constants.SIMULATION_END)) {
//                break;
//            }
//            if (Utils.getMarkingIndex(list, event.getMarking()) == -1) {
//                list.add(event.getMarking());
//            }
//        }
//        String[] columnNames = new String[list.size() + 2];
//        Object[][] omegaArray = new Object[list.size()][columnNames.length];
//        int[][] subOmegaArray = new int[list.size()][list.size()];
//        columnNames[0] = " ";
//        for (int i = 0; i < list.size(); i++) {
//            columnNames[i + 1] = "M" + i;
//            omegaArray[i][0] = "M" + i;
//        }
//        columnNames[columnNames.length - 1] = "Vi";
//        String prevMarking = "doNothing";
//        for (Event event : statistics.getQuery()) {
//            if (event.getType().equals(Constants.SIMULATION_END)) {
//                break;
//            }
//            String currentMarking = event.getMarking();
//            if (!currentMarking.equals(prevMarking) && !prevMarking.equals("doNothing")) {
//                int row = Utils.getMarkingIndex(list, prevMarking);
//                int column = Utils.getMarkingIndex(list, currentMarking);
//                subOmegaArray[row][column]++;
//            }
//            prevMarking = currentMarking;
//        }
//
//
//        for (int i = 0; i < subOmegaArray.length; i++) {
//            omegaArray[i][omegaArray[i].length - 1] = Math.round(statistics.getStayTime()[i] / Properties.getInstance().getDeltaT());
//        }
//
//        for (int i = 0; i < subOmegaArray.length; i++) {
//            int hits = Integer.valueOf(omegaArray[i][omegaArray[i].length - 1].toString());
//            for (int j = 0; j < subOmegaArray[i].length; j++) {
//                hits -= subOmegaArray[i][j];
//            }
//            subOmegaArray[i][i] = hits;
//        }
//        for (int i = 0; i < subOmegaArray.length; i++) {
//            for (int j = 0; j < subOmegaArray[i].length; j++) {
//                omegaArray[i][j + 1] = (Properties.getInstance().isDivide()) ? Utils.numberFormat.format(1.0 * subOmegaArray[i][j] / Double.parseDouble(omegaArray[i][omegaArray[i].length - 1].toString())) : subOmegaArray[i][j];
//            }
//        }
//
//        double[][] probabilityMatrix = new double[subOmegaArray.length][subOmegaArray[0].length];
//        for (int i = 0; i < subOmegaArray.length; i++) {
//            for (int j = 0; j < subOmegaArray[i].length; j++) {
//                probabilityMatrix[i][j] = 1.0 * subOmegaArray[i][j] / Double.valueOf(omegaArray[i][omegaArray[i].length - 1].toString());
//            }
//        }
//        statistics.setProbabilityMatrix(probabilityMatrix);
//        return new DefaultTableModel(omegaArray, columnNames);
//    }

//    private static ChartPanel createChart(String title, double[] array) {
//        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//        for (int i = 0; i < array.length; i++) {
//            dataset.addValue(array[i], "Amount", "M" + i);
//        }
//        JFreeChart chart = ChartFactory.createBarChart(
//                title,
//                null,
//                "Amount",
//                dataset,
//                PlotOrientation.VERTICAL,
//                false,
//                true,
//                false);
//        CategoryPlot plot = (CategoryPlot) chart.getPlot();
//        BarRenderer renderer = ((BarRenderer) plot.getRenderer());
////        StandardCategoryItemLabelGenerator labelGen = new StandardCategoryItemLabelGenerator("{2}", new DecimalFormat("0"));
////        renderer.setBaseItemLabelGenerator(labelGen);
////        renderer.setBaseItemLabelsVisible(true);
////        ItemLabelPosition itemlabelposition = new ItemLabelPosition(ItemLabelAnchor.CENTER, TextAnchor.BOTTOM_CENTER);
////        renderer.setBasePositiveItemLabelPosition(itemlabelposition);
//        plot.setRangeGridlinePaint(Color.red);
//        ChartPanel cph = new ChartPanel(chart);
//        cph.setPreferredSize(new Dimension(600, 200));
//        return cph;
//    }

//    public static mxGraphComponent createMarkovProbabilityGraph(Statistics statistics) {
//        mxGraph out = new mxGraph() {
//
//            {
//                setCellsDeletable(false);
//                setCellsDisconnectable(false);
//                setCellsResizable(false);
//                setCellsEditable(false);
//            }
//        };
//        parent = out.getDefaultParent();
//        ArrayList<mxCell> list = new ArrayList<mxCell>();
//        out.getModel().beginUpdate();
//        try {
//            for (int i = 0; i < statistics.getProbabilityMatrix().length; i++) {
//                list.add((mxCell) out.insertVertex(parent, null, "M" + i, 0, 0, 30, 30, "shape=ellipse;perimeter=ellipsePerimeter;strokeColor=#000000;fillColor=#ffffff"));
//            }
//            for (int i = 0; i < statistics.getProbabilityMatrix().length; i++) {
//                for (int j = 0; j < statistics.getProbabilityMatrix().length; j++) {
//                    if (statistics.getProbabilityMatrix()[i][j] != 0) {
//                        out.insertEdge(parent, null, Utils.numberFormat.format(statistics.getProbabilityMatrix()[i][j]), list.get(i), list.get(j));
//                    }
//                }
//            }
//        } finally {
//            out.getModel().endUpdate();
//        }
//        mxCircleLayout layout = new mxCircleLayout(out);
//        layout.setRadius(200);
//        layout.execute(parent);
//        mxGraphComponent graphComponent = new mxGraphComponent(out) {
//
//            {
//                mxCodec codec = new mxCodec();
//                Document doc = mxUtils.loadDocument(Tree.class.getResource(
//                        "/default-style.xml").toString());
//                codec.decode(doc.getDocumentElement(), graph.getStylesheet());
//                setBorder(null);
//            }
//        };
//
//        return graphComponent;
//    }
}
