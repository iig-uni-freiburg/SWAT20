package de.uni.freiburg.iig.telematik.swat.workbench.dialog;

import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeNode;
import de.uni.freiburg.iig.telematik.swat.workbench.action.AddFilterAction;

public class SwatTreePopupMenuView extends SwatTreePopupMenu {

        public SwatTreePopupMenuView(SwatTreeNode node) {
                super(node);
                generateEntries();
        }

        private void generateEntries() {
                add(new AddFilterAction("Add new filter"));
        }
}
