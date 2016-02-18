package de.uni.freiburg.iig.telematik.swat.workbench.dialog;

import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeNode;
import de.uni.freiburg.iig.telematik.swat.workbench.action.NewViewAction;

public class SwatTreePopupMenuLog extends SwatTreePopupMenu {

	public SwatTreePopupMenuLog(SwatTreeNode node) {
		super(node);
		generateEntries();
	}

	private void generateEntries() {
		add(new NewViewAction("Add new view"));
	}
}
