package de.uni.freiburg.iig.telematik.swat.workbench;

import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponentType;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import de.invation.code.toval.misc.wd.ComponentListener;
import de.invation.code.toval.misc.wd.ProjectComponentException;
import de.invation.code.toval.validate.ExceptionDialog;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sewol.log.LogView;
import de.uni.freiburg.iig.telematik.sewol.log.filter.AbstractLogFilter;
import de.uni.freiburg.iig.telematik.swat.analysis.Analysis;
import de.uni.freiburg.iig.telematik.swat.logs.LogModel;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.SwatTreePopupMenu;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.SwatTreePopupMenuLog;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatStateListener;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatTreeViewListener;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.ViewComponent;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;

public class SwatTreeView extends JTree implements SwatStateListener, ComponentListener {

        private static final String PN_HEADING = "Petri nets";
        private static final String LOGS_HEADING = "Process Logs";
        private static final String CONTEXTS_HEADING = "Execution Contexts";
        private static final String ACMODELS_HEADING = "Access Control Models";
        private static final Color DEFAULT_BG_COLOR = UIManager.getColor("Panel.background");
//        private static final Color BG_COLOR = Color.white;
//        private static final Color BORDER_COLOR = new Color(237, 237, 237);

        private final DefaultTreeModel defaultTreeModel;

        private final Set<SwatTreeViewListener> listeners = new HashSet<>();

        private static SwatTreeView instance = null;

        private final DefaultMutableTreeNode rootNode;
        private DefaultMutableTreeNode petriNetNode = null;
        private DefaultMutableTreeNode logsNode = null;
        private DefaultMutableTreeNode acModelsNode = null;
        private DefaultMutableTreeNode contextsNode = null;

        private SwatTreeView() throws Exception {
                rootNode = new DefaultMutableTreeNode("Working Directory");
                defaultTreeModel = new DefaultTreeModel(rootNode);
                this.setBackground(DEFAULT_BG_COLOR);
                this.setModel(defaultTreeModel);
                this.setShowsRootHandles(true);
                this.setEditable(false);
                setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                buildTree();
                setToggleClickCount(0);//prevent collapse on double click

                // this.setSelectionInterval(0,0);
                addMouseListener(new TreeViewMouseAdapter());

                SwatComponents.getInstance().addComponentListener(this);
                SwatComponents.getInstance().getContainerAristaflowLogs().addComponentListener(this);
                SwatComponents.getInstance().getContainerMXMLLogs().addComponentListener(this);
                SwatComponents.getInstance().getContainerXESLogs().addComponentListener(this);
                SwatComponents.getInstance().getContainerPetriNets().addComponentListener(this);
                SwatComponents.getInstance().getContainerLogViews().addComponentListener(this);
        }

        public static SwatTreeView getInstance() throws Exception {
                if (instance == null) {
                        instance = new SwatTreeView();
                }
                return instance;
        }

        public void removeAndUpdateSwatComponents() throws ProjectComponentException {
                rootNode.removeAllChildren();
                buildTree();
                defaultTreeModel.reload();
                expandAll();
                repaint();
        }

        @SuppressWarnings("rawtypes")
        private void buildTree() throws ProjectComponentException {
                rootNode.removeAllChildren();

                // Petri Nets
                if (SwatComponents.getInstance().containsPetriNets()) {
                        petriNetNode = new DefaultMutableTreeNode(PN_HEADING);
                        rootNode.add(petriNetNode);

                        for (AbstractGraphicalPN petriNet : SwatComponents.getInstance().getContainerPetriNets().getComponentsSorted()) {
                                SwatTreeNode node = new SwatTreeNode(petriNet, SwatComponentType.PETRI_NET);
                                for (Analysis analysis : SwatComponents.getInstance().getContainerPetriNets().getContainerAnalysis(node.getDisplayName()).getComponents()) {
                                        node.add(new SwatTreeNode(analysis, SwatComponentType.PETRI_NET_ANALYSIS));
                                }
                                petriNetNode.add(node);
                        }
                }

                // Logs
                if (SwatComponents.getInstance().containsLogs()) {
                        logsNode = new DefaultMutableTreeNode(LOGS_HEADING);
                        rootNode.add(logsNode);

                        // Logs
                        List<LogModel> logFiles = SwatComponents.getInstance().getLogs();
                        Collections.sort(logFiles);
                        for (LogModel logModel : logFiles) {
                                SwatComponentType logType = null;
                                switch (logModel.getType()) {
                                        case Aristaflow:
                                                logType = SwatComponentType.ARISTAFLOW_LOG;
                                                break;
                                        case MXML:
                                                logType = SwatComponentType.MXML_LOG;
                                                break;
                                        case XES:
                                                logType = SwatComponentType.XES_LOG;
                                                break;
                                }

                                final DefaultMutableTreeNode logNode = new SwatTreeNode(logModel, logType);

                                for (LogView view : logModel.getLogViews()) {
                                        DefaultMutableTreeNode viewNode = new SwatTreeNode(view, SwatComponentType.LOG_VIEW);

                                        // list filters
                                        Iterator<AbstractLogFilter> filterIterator = view.getFilters().iterator();
                                        while (filterIterator.hasNext()) {
                                                AbstractLogFilter filter = filterIterator.next();
                                                viewNode.add(new DefaultMutableTreeNode(filter.toString()));
                                        }
                                        
                                        logNode.add(viewNode);
                                }

                                logsNode.add(logNode);
                        }
                }

                if (SwatComponents.getInstance().containsProcessContexts()) {
                        contextsNode = new DefaultMutableTreeNode(CONTEXTS_HEADING);
                        rootNode.add(contextsNode);
                }

                if (SwatComponents.getInstance().containsACModels()) {
                        acModelsNode = new DefaultMutableTreeNode(ACMODELS_HEADING);
                        rootNode.add(acModelsNode);
                }
                expandAll();
                validate();
                repaint();
        }

        public void expandAll() {
                try {
                        for (int i = 0; i < getRowCount(); i++) {
                                expandRow(i);
                        }
                } catch (Exception e) {
                }
        }

        @Override
        public void operatingModeChanged() {
        }

        public void addTreeViewListener(SwatTreeViewListener listener) {
                listeners.add(listener);
        }

        private void notifyComponentSelected(SwatTreeNode node) {
                for (SwatTreeViewListener listener : listeners) {
                        listener.componentSelected(node);
                }
        }

        private void notifyComponentActivated(SwatTreeNode node) {
                for (SwatTreeViewListener listener : listeners) {
                        listener.componentActivated(node);
                }
        }

        @SuppressWarnings("unchecked")
        public SwatTreeNode getComponent(ViewComponent component) {
                Enumeration<SwatTreeNode> children = rootNode.children();
                while (children.hasMoreElements()) {
                        SwatTreeNode node = children.nextElement();
                        if (node.getUserObject().equals(component)) {
                                return node;
                        }
                }
                return null;
        }

        private class TreeViewMouseAdapter extends MouseAdapter {

                @Override
                public void mouseClicked(MouseEvent e) {

                        if (!(e.getSource() instanceof SwatTreeView)) {
                                return;
                        }

                        if (getSelectionPath() == null) {
                                return;
                        }

                        Object selectedNode = getSelectionPath().getLastPathComponent();
                        if (selectedNode == null) {
                                return;
                        }
                        if (!(selectedNode instanceof SwatTreeNode)) {
                                return;
                        }

                        SwatTreeNode swatNode = (SwatTreeNode) selectedNode;
                        if (e.getClickCount() == 1) {
                                if (e.isPopupTrigger()) {
                                        // Right click on tree node
                                        showPopup(e);
                                } else {
                                        // Left click on tree node
                                        notifyComponentSelected(swatNode);
                                }
                        } else if (e.getClickCount() == 2 && e.getButton() == 1 && !e.isPopupTrigger()) {
                                // Double click on tree node
                                notifyComponentActivated(swatNode);
                        }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                        if (e.isPopupTrigger()) {
                                showPopup(e);
                        }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                        if (e.isPopupTrigger()) {
                                showPopup(e);
                        }
                }

                private void showPopup(MouseEvent e) {
                        int selRow = getRowForLocation(e.getX(), e.getY());
                        TreePath selPath = getPathForLocation(e.getX(), e.getY());
                        setSelectionPath(selPath);
                        if (selRow > -1) {
                                setSelectionRow(selRow);
                        }

                        if (getSelectionPath() == null) {
                                return;
                        }

                        Object selectedNode = getSelectionPath().getLastPathComponent();
                        if (selectedNode instanceof SwatTreeNode) {
                                SwatTreeNode swatNode = (SwatTreeNode) selectedNode;
                                SwatTreePopupMenu menu;
                                if (swatNode.getObjectType() == SwatComponentType.ARISTAFLOW_LOG || swatNode.getObjectType() == SwatComponentType.MXML_LOG || swatNode.getObjectType() == SwatComponentType.XES_LOG) {
                                        menu = new SwatTreePopupMenuLog(swatNode);
                                } else {
                                        menu = new SwatTreePopupMenu(swatNode);
                                }
                                menu.show((Component) e.getSource(), e.getX(), e.getY());
                        }
                }
        }

        private DefaultMutableTreeNode find(String s) {
                @SuppressWarnings("unchecked")
                Enumeration<DefaultMutableTreeNode> e = ((DefaultMutableTreeNode) getModel().getRoot()).depthFirstEnumeration();
                while (e.hasMoreElements()) {
                        DefaultMutableTreeNode node = e.nextElement();
                        if (node.toString().equalsIgnoreCase(s)) {
                                return node;
                        }
                }
                return null;
        }

        @Override
        public void componentsChanged() {
//                System.out.println("Components Changed");
                try {
                        buildTree();
                        defaultTreeModel.reload();
                        expandAll();
                        repaint();
                } catch (Exception e) {
                        ExceptionDialog.showException(SwingUtilities.getWindowAncestor(SwatTreeView.this), "Tree View Exception", new Exception("Exception while building tree"), true, true);
                }
        }

        @Override
        public void componentAdded(Object component) throws ProjectComponentException {
                //System.out.println("Component Added");
//        	if(component instanceof NamedComponent){
//        		SwatTreeNode node = new SwatTreeNode(component, SwatComponentType.PETRI_NET);
//        		petriNetNode.add(node);
//        	}
                buildTree();

        }

        @Override
        public void componentRemoved(Object component) throws ProjectComponentException {
                buildTree();
        }

        @Override
        public void componentRenamed(Object component, String oldName, String newName) throws ProjectComponentException {
                buildTree();
        }

}
