package de.uni.freiburg.iig.telematik.swat.workbench.dialog;

import javax.swing.JPopupMenu;

import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeNode;
import de.uni.freiburg.iig.telematik.swat.workbench.action.DeleteAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.RenameAction;

public class SwatTreePopupMenu extends JPopupMenu {
	SwatTreeNode node;

	public SwatTreePopupMenu(SwatTreeNode node) {
		this.node = node;
		generateEntries();

	}

	private void generateEntries() {
		add(new DeleteAction("Delete " + node.getDisplayName()));
		add(new RenameAction("Rename " + node.getDisplayName()));
	}

}
