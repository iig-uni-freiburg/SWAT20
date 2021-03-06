package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.SwingUtilities;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParser;
import de.uni.freiburg.iig.telematik.sewol.log.LogView;
import de.uni.freiburg.iig.telematik.sewol.log.LogViewSerialization;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.logs.LogModel;
import de.uni.freiburg.iig.telematik.swat.logs.LogViewContainer;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponentNameDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeNode;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponentType;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatPNContainer;

public class DuplicateAction extends AbstractWorkbenchAction {

        public DuplicateAction(String name) {
                super(name);
                loadAndSetIcon();
        }

        public DuplicateAction() {
                super("duplicate entry");
                loadAndSetIcon();
        }

        private static final long serialVersionUID = -5583721064769029821L;

        @Override
        protected void doFancyStuff(ActionEvent e) throws Exception {
                SwatTreeNode node = (SwatTreeNode) Workbench.getInstance().getTreeView().getSelectionPath().getLastPathComponent();
                try {
                        duplicateNode(node);
                } catch (Exception e1) {
                        throw new Exception("Could not duplicate", e1);
                }

        }

        private void duplicateNode(SwatTreeNode node) throws Exception {
                switch (node.getObjectType()) {
                        case ARISTAFLOW_LOG:
                        case MXML_LOG:
                        case XES_LOG:
                                duplicateLog(node);
                                break;
                        case PETRI_NET:
                                duplicateNet(node);
                                break;
                        case LOG_VIEW:
                                LogViewContainer container = SwatComponents.getInstance().getContainerLogViews();
                                File viewFile = container.getComponentFile(node.getDisplayName());
                                LogView newView = LogViewSerialization.parse(viewFile);
                                LogModel model = SwatComponents.getInstance().getLog(newView.getParentLogName());
                                model.addLogView(newView);
                                newView.setName(requestNewName("New name for log view", "new name", node.getObjectType()));
                                container.addComponent(newView, true);
                                break;
                        default:
                                Workbench.errorMessage("Cannot duplicate entry", new Exception("Cannot duplicate entry " + node.getDisplayName()), true);
                }

        }

        private void duplicateNet(SwatTreeNode node) throws Exception {
                SwatPNContainer container = SwatComponents.getInstance().getContainerPetriNets();
                File netFile = container.getComponentFile(node.getDisplayName());
                PNMLParser parser = new PNMLParser<>();
                AbstractGraphicalPN newNet = parser.parse(netFile);
                newNet.setName(requestNewName("New name for net", "new name", node.getObjectType()));
                container.addComponent(newNet, true);

//		AbstractGraphicalPN net = (AbstractGraphicalPN) container.getComponent(node.getDisplayName());
//		String name = requestNewName("New name for net", "new name");
//		AbstractGraphicalPN newNet = new GraphicalPTNet(net.getPetriNet().clone(),net.getPetriNetGraphics());
        }

        private void duplicateLog(SwatTreeNode node) throws Exception {
                SwatComponents component = SwatComponents.getInstance();
                File logFile = ((LogModel) node.getUserObject()).getFileReference();
                String name = requestNewName("New name for log", "new name", node.getObjectType());

                switch (node.getObjectType()) {
                        case ARISTAFLOW_LOG:
//			element = component.getContainerAristaflowLogs().getComponent(node.getDisplayName()).clone();
//			element.setName(name);
                                component.getContainerAristaflowLogs().addComponent(logFile, name);
                                break;
                        case MXML_LOG:
//			element=component.getContainerMXMLLogs().getComponent(node.getDisplayName()).clone();
//			element.setName(name);
                                component.getContainerAristaflowLogs().addComponent(logFile, name);
                                break;
                        case XES_LOG:
//			element=component.getContainerXESLogs().getComponent(node.getDisplayName()).clone();
//			element.setName(name);
                                component.getContainerAristaflowLogs().addComponent(logFile, name);
                                break;
                        default:
                                break;
                }

        }

        private void loadAndSetIcon() {
                try {
                        setIcon(IconFactory.getIcon("data"));
                } catch (ParameterException | PropertyException | IOException e) {
                        Workbench.errorMessage("Could not load logo 'data'", e, false);
                }
        }

        private String requestNewName(String message, String title, SwatComponentType type) throws Exception {
                return new SwatComponentNameDialog(SwingUtilities.getWindowAncestor(Workbench.getInstance()), message, title, false, type).requestInput();
        }

}
