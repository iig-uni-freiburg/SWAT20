package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.SwingUtilities;

import de.invation.code.toval.graphic.dialog.FileNameDialog;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeNode;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;

public class RenameAction extends AbstractWorkbenchAction {

	public RenameAction() {
		super("Rename");
		setTooltip("rename current PT-Net");
		try {
			setIcon(IconFactory.getIcon("rename"));
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

	private static final long serialVersionUID = 365967570541436082L;

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		SwatTreeNode node = (SwatTreeNode) Workbench.getInstance().getTreeView().getSelectionPath().getLastPathComponent();
		String oldName, newName;
		
		switch(node.getObjectType()){
		case LABELING:
			break;
		case PETRI_NET:
			oldName = node.getDisplayName();
			newName = requestNetName("Enter new name for Petri net", "Rename Petri net");
			if(newName != null && !newName.equals(oldName))
				SwatComponents.getInstance().renamePetriNet(oldName, newName);
			break;
		case PETRI_NET_ANALYSIS:
			break;
		case MXML_LOG:
		case XES_LOG:
		case ARISTAFLOW_LOG:
			oldName = node.getDisplayName();
			newName = requestNetName("Enter new name for Petri net", "Rename Petri net");

		default:
			break;
		}
	}

	private String requestNetName(String message, String title) {
		return new FileNameDialog(SwingUtilities.getWindowAncestor(Workbench.getInstance()), message, title, false).requestInput();
	}
}
