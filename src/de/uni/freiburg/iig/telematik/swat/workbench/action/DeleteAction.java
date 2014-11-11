package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeNode;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeView;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;

public class DeleteAction extends AbstractWorkbenchAction {

	private static final long serialVersionUID = 2735994196284600328L;

	public DeleteAction(String name) {
		super(name);
		setTooltip("Delete currently selected item");
		setAcceleratorKey(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		try {
			this.setIcon(IconFactory.getIcon("delete"));
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public DeleteAction(String name, Icon icon) {
		super(name);
		setTooltip("Delete currently selected item");
		setAcceleratorKey(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
	}

	public DeleteAction() {
		this("");
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		//get selected item
		try {
		SwatTreeNode selectedNode = (SwatTreeNode) SwatTreeView.getInstance().getSelectionPath().getLastPathComponent();
		int userAnswer = JOptionPane.showConfirmDialog(Workbench.getInstance(), "Delete " + selectedNode.getDisplayName() + " from DISK?");

		if (userAnswer != JOptionPane.YES_OPTION)
			return;

		switch (selectedNode.getObjectType()) {
		case PETRI_NET:
			SwatComponents.getInstance().removePetriNet(selectedNode.getDisplayName(), true);
			break;
		case MXML_LOG:
		case ARISTAFLOW_LOG:
		case XES_LOG:
			SwatComponents.getInstance().removeLogFile(selectedNode.getDisplayName(), true);
		default:
			break;
		}
		} catch (NullPointerException e1) {
			//nothing marked in TreeView
		}

	}

}
