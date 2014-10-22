package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.event.ActionEvent;

import javax.swing.SwingUtilities;

import de.invation.code.toval.graphic.dialog.FileNameDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeNode;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;

public class RenameAction extends AbstractWorkbenchAction {

	public RenameAction() {
		super("Rename");
	}

	private static final long serialVersionUID = 365967570541436082L;

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		SwatTreeNode node = (SwatTreeNode) Workbench.getInstance().getTreeView().getSelectionPath().getLastPathComponent();
		
		switch(node.getObjectType()){
		case LABELING:
			break;
		case PETRI_NET:
			String oldName = node.getDisplayName();
			String newName = requestNetName("Enter new name for Petri net", "Rename Petri net");
			if(newName != null && !newName.equals(oldName))
				SwatComponents.getInstance().renamePetriNet(oldName, newName);
			break;
		case PETRI_NET_ANALYSIS:
			break;
		default:
			break;
		}
	}

	private String requestNetName(String message, String title) {
		return new FileNameDialog(SwingUtilities.getWindowAncestor(Workbench.getInstance()), message, title, false).requestInput();
	}
}
