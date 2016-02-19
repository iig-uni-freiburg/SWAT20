package de.uni.freiburg.iig.telematik.swat.workbench;

import de.uni.freiburg.iig.telematik.sewol.log.filter.AbstractLogFilter;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponentType;

/**
 *
 * @author Adrian Lange <lange@iig.uni-freiburg.de>
 */
public class SwatTreeFilterNode extends SwatTreeNode {

        private final AbstractLogFilter filter;
        private final SwatTreeNode parentTreeNode;

        private String displayName = null;

        public SwatTreeFilterNode(AbstractLogFilter filter, SwatTreeNode parentTreeNode) {
                super(filter, SwatComponentType.LOG_VIEW_FILTER);
                this.filter = filter;
                this.parentTreeNode = parentTreeNode;
                setDisplayName();
                allowsChildren = true;
        }

        private void setDisplayName() {
                displayName = filter.toString();
        }

        @Override
        public String getDisplayName() {
                return displayName;
        }

        public AbstractLogFilter getFilter() {
                return filter;
        }

        public SwatTreeNode getParentTreeNode() {
                return parentTreeNode;
        }

        @Override
        public String toString() {
                return getDisplayName();
        }
}
