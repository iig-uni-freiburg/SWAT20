package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sewol.log.LogView;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.logs.LogModel;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeNode;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeView;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;

public class DeleteAction extends AbstractWorkbenchAction {

        int commandKey = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

        private static final long serialVersionUID = 2735994196284600328L;

        public DeleteAction(String name) {
                super(name);
                setTooltip("Delete currently selected item");
                setAcceleratorKey(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, InputEvent.SHIFT_DOWN_MASK));
                try {
                        this.setIcon(IconFactory.getIcon("delete"));
                } catch (ParameterException | PropertyException | IOException e) {
                }
        }

        public DeleteAction(String name, Icon icon) {
                super(name);
                if (icon != null) {
                        setIcon(icon);
                }
                setTooltip("Delete currently selected item");
                setAcceleratorKey(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, InputEvent.SHIFT_DOWN_MASK));
        }

        public DeleteAction() {
                this("");
        }

        @Override
        protected void doFancyStuff(ActionEvent e) throws Exception {
                //get selected item
                try {
                        SwatTreeNode selectedNode = (SwatTreeNode) SwatTreeView.getInstance().getSelectionPath().getLastPathComponent();
                        int userAnswer = JOptionPane.showConfirmDialog(Workbench.getInstance(), "Delete " + selectedNode.getDisplayName() + " from DISK?", null, JOptionPane.YES_NO_OPTION);
                        if (userAnswer != JOptionPane.YES_OPTION) {
                                return;
                        }
                        switch (selectedNode.getObjectType()) {
                            case PETRI_NET:
                                SwatComponents.getInstance().getContainerPetriNets().removeComponent(selectedNode.getDisplayName(), true);
                                break;
                            case PETRI_NET_ANALYSIS:
                        		SwatTreeNode parentNode = (SwatTreeNode) SwatTreeView.getInstance().getSelectionPath().getParentPath().getLastPathComponent();
                        		SwatComponents.getInstance().getContainerPetriNets().getContainerAnalysis(parentNode.getDisplayName()).removeComponent(selectedNode.getDisplayName(), true, true);
                                SwatTreeView.getInstance().componentsChanged();
                        		break;
                            case MXML_LOG:
                                SwatComponents.getInstance().getContainerMXMLLogs().removeComponent(selectedNode.getDisplayName(), true);
                                break;
                            case ARISTAFLOW_LOG:
                                SwatComponents.getInstance().getContainerAristaflowLogs().removeComponent(selectedNode.getDisplayName(), true);
                                break;
                            case XES_LOG:
                                SwatComponents.getInstance().getContainerXESLogs().removeComponent(selectedNode.getDisplayName(), true);
                                break;
                            case LOG_VIEW:
                                LogView view = SwatComponents.getInstance().getContainerLogViews().getComponent(selectedNode.getDisplayName());
                                LogModel parentModel = SwatComponents.getInstance().getLog(view.getParentLogName());
                                parentModel.removeLogView(view);
                                SwatComponents.getInstance().getContainerLogViews().removeComponent(selectedNode.getDisplayName(), true);
                                break;
                            default:
                                break;
                        }
                } catch (NullPointerException e1) {
                        Workbench.errorMessage("No marked element to delete", e1, true);
                }
        }
}
