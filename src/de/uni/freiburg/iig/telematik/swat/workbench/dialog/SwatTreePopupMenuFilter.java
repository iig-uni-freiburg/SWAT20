package de.uni.freiburg.iig.telematik.swat.workbench.dialog;

import javax.swing.JPopupMenu;

import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeNode;
import de.uni.freiburg.iig.telematik.swat.workbench.action.AddFilterAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.DeleteAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.DuplicateAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.RenameAction;

public class SwatTreePopupMenuFilter extends JPopupMenu {
	SwatTreeNode node;

	public SwatTreePopupMenuFilter(SwatTreeNode node) {
		this.node = node;
		generateEntries();
	}

	private void generateEntries() {
                add(new AddFilterAction("Add new filter"));
		add(new DeleteAction("Delete filter"));
		add(new RenameAction("Edit filter"));
		add(new DuplicateAction("Duplicate filter"));
	}

}
