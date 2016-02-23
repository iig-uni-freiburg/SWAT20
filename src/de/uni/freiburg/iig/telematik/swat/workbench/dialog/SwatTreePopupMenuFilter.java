package de.uni.freiburg.iig.telematik.swat.workbench.dialog;

import de.uni.freiburg.iig.telematik.sewol.log.filter.ContainsFilter;
import de.uni.freiburg.iig.telematik.sewol.log.filter.MaxEventsFilter;
import de.uni.freiburg.iig.telematik.sewol.log.filter.MinEventsFilter;
import de.uni.freiburg.iig.telematik.sewol.log.filter.TimeFilter;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeFilterNode;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeNode;
import de.uni.freiburg.iig.telematik.swat.workbench.action.AddFilterAction;
import javax.swing.JPopupMenu;

import de.uni.freiburg.iig.telematik.swat.workbench.action.DeleteFilterAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.EditFilterAction;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JSeparator;

public class SwatTreePopupMenuFilter extends JPopupMenu {

        private final SwatTreeFilterNode node;

        public SwatTreePopupMenuFilter(SwatTreeFilterNode node) {
                this.node = node;
                generateEntries();
        }

        private void generateEntries() {
                for (AddFilterAction action : getAddFilterActions(node.getParentTreeNode())) {
                        add(action);
                }
                add(new JSeparator());
                add(new JSeparator());
                add(new EditFilterAction(node.getParentTreeNode()));
                add(new DeleteFilterAction(node.getParentTreeNode()));
        }

        protected static List<AddFilterAction> getAddFilterActions(SwatTreeNode viewNode) {
                List<AddFilterAction> actions = new ArrayList<>(4);
                actions.add(new AddFilterAction(viewNode, new ContainsFilter()));
                actions.add(new AddFilterAction(viewNode, new MaxEventsFilter()));
                actions.add(new AddFilterAction(viewNode, new MinEventsFilter()));
                actions.add(new AddFilterAction(viewNode, new TimeFilter()));
                return actions;
        }
}
