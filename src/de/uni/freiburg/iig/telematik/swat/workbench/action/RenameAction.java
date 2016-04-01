package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import de.invation.code.toval.graphic.dialog.FileNameDialog;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTabView;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeNode;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeView;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;

public class RenameAction extends AbstractWorkbenchAction {

        private static final long serialVersionUID = 365967570541436082L;

        public RenameAction() {
                super("Rename");
                setTooltip("rename current entry");
                setAcceleratorKey(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
                try {
                        setIcon(IconFactory.getIcon("rename"));
                } catch (ParameterException | PropertyException | IOException e) {
                }
        }

        public RenameAction(String name, Icon icon) {
                super(name);
                setAcceleratorKey(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
                if (icon != null) {
                        setIcon(icon);
                }
        }

        public RenameAction(String string) {
                super(string);
                setAcceleratorKey(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
                setTooltip("Rename");
                setAcceleratorKey(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
                try {
                        setIcon(IconFactory.getIcon("rename"));
                } catch (ParameterException | PropertyException | IOException e) {
                }
        }

        @Override
        protected void doFancyStuff(ActionEvent e) throws Exception {

                SwatTreeNode node = (SwatTreeNode) Workbench.getInstance().getTreeView().getSelectionPath().getLastPathComponent();
                String oldName, newName;
                boolean open = SwatTabView.getInstance().containsComponent(node);

                switch (node.getObjectType()) {
                        case LABELING:
                                break;
                        case PETRI_NET:
                                oldName = node.getDisplayName();
                                newName = requestNetName("Enter new name for Petri net", "Rename Petri net");
                                if (newName != null && !newName.equals(oldName)) {
                                        if (SwatTabView.getInstance().closeTabAndAskUser(oldName)) {
                                                SwatComponents.getInstance().getContainerPetriNets().renameComponent(oldName, newName);
                                        }
                                }
                                SwatTreeView.getInstance().componentsChanged();
                                break;
                        case MXML_LOG:
                                oldName = node.getDisplayName();
                                newName = requestNetName("Enter new name for Log", "Rename Log");
                                if (newName != null && !newName.equals(oldName)) {
                                    if (SwatTabView.getInstance().closeTabAndAskUser(oldName)) {
                                    	SwatComponents.getInstance().getContainerMXMLLogs().renameComponent(oldName, newName);
                                    }
                                }
                                break;
                        case XES_LOG:
                                oldName = node.getDisplayName();
                                newName = requestNetName("Enter new name for Log", "Rename Log");
                                if (newName != null && !newName.equals(oldName)) {
                                    if (SwatTabView.getInstance().closeTabAndAskUser(oldName)) {
                                    	SwatComponents.getInstance().getContainerXESLogs().renameComponent(oldName, newName);
                                    }
                                }
                                break;
                        case ARISTAFLOW_LOG:
                                oldName = node.getDisplayName();
                                newName = requestNetName("Enter new name for Log", "Rename Log");
                                if (newName != null && !newName.equals(oldName)) {
                                    if (SwatTabView.getInstance().closeTabAndAskUser(oldName)) {
                                    	SwatComponents.getInstance().getContainerAristaflowLogs().renameComponent(oldName, newName);
                                    }
                                }
                                break;
                        case LOG_VIEW:
                                oldName = node.getDisplayName();
                                newName = requestNetName("Enter new name for log view", "Rename Log View");
                                if (newName != null && !newName.equals(oldName)) {
                                    if (SwatTabView.getInstance().closeTabAndAskUser(oldName)) {
                                        SwatComponents.getInstance().getContainerLogViews().renameComponent(oldName, newName);
                                    }
                                }
                                break;
                        default:
                                break;
                }
        }

        private String requestNetName(String message, String title) throws Exception {
                return FileNameDialog.showDialog(SwingUtilities.getWindowAncestor(Workbench.getInstance()), message, title, false);
        }
}
