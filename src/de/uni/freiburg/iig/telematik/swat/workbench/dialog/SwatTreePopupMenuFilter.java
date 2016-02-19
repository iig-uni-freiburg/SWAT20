package de.uni.freiburg.iig.telematik.swat.workbench.dialog;

import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeFilterNode;
import javax.swing.JPopupMenu;

import de.uni.freiburg.iig.telematik.swat.workbench.action.AddFilterAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.DeleteFilterAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.EditFilterAction;

public class SwatTreePopupMenuFilter extends JPopupMenu {

        private final SwatTreeFilterNode node;

        public SwatTreePopupMenuFilter(SwatTreeFilterNode node) {
                this.node = node;
                generateEntries();
        }

        private void generateEntries() {
                add(new AddFilterAction(node.getParentTreeNode()));
                add(new EditFilterAction(node.getParentTreeNode()));
                add(new DeleteFilterAction(node.getParentTreeNode()));
        }
}
