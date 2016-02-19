package de.uni.freiburg.iig.telematik.swat.workbench.dialog;

import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeFilterNode;
import javax.swing.JPopupMenu;

import de.uni.freiburg.iig.telematik.swat.workbench.action.AddFilterAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.DeleteAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.DuplicateAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.RenameAction;

public class SwatTreePopupMenuFilter extends JPopupMenu {

        SwatTreeFilterNode node;

        public SwatTreePopupMenuFilter(SwatTreeFilterNode node) {
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
