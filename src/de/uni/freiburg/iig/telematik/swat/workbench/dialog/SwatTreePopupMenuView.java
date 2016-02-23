package de.uni.freiburg.iig.telematik.swat.workbench.dialog;

import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeNode;
import de.uni.freiburg.iig.telematik.swat.workbench.action.AddFilterAction;
import javax.swing.JSeparator;

public class SwatTreePopupMenuView extends SwatTreePopupMenu {

        public SwatTreePopupMenuView(SwatTreeNode node) {
                super(node);
                generateEntries(node);
        }

        private void generateEntries(SwatTreeNode node) {
                add(new JSeparator());
                add(new JSeparator());
                for (AddFilterAction action : SwatTreePopupMenuFilter.getAddFilterActions(node)) {
                        add(action);
                }
        }
}
