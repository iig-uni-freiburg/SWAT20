package de.uni.freiburg.iig.telematik.swat.workbench;

import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponentType;
import javax.swing.tree.DefaultMutableTreeNode;

import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sewol.log.LogView;
import de.uni.freiburg.iig.telematik.swat.analysis.Analysis;
import de.uni.freiburg.iig.telematik.swat.logs.LogModel;

public class SwatTreeNode extends DefaultMutableTreeNode {

        private static final long serialVersionUID = 8746333990209477776L;
        private final SwatComponentType objectType;

        private String displayName;

        public SwatTreeNode(Object userObject, SwatComponentType objectType) {
                super(userObject, false);
                this.objectType = objectType;
                setDisplayName();
                allowsChildren = true;
        }

        //TODO: Display name for AC, etc.
        @SuppressWarnings("rawtypes")
        private void setDisplayName() {
                switch (objectType) {
                        case PETRI_NET:
                                displayName = ((AbstractGraphicalPN) getUserObject()).getPetriNet().getName();
                                break;
                        case PETRI_NET_ANALYSIS:
                                displayName = ((Analysis) getUserObject()).getName();
                                break;
                        case AC_MODEL:
                                break;
                        case ANALYSIS_CONTEXT:
                                break;
                        case TIME_CONTEXT:
                                break;
                        case ARISTAFLOW_LOG:
                        case MXML_LOG:
                        case XES_LOG:
                                LogModel logModel = (LogModel) getUserObject();
                                displayName = logModel.getName();
                                break;
                        case LOG_VIEW:
                                LogView logView = (LogView) getUserObject();
                                displayName = logView.getName();
                                break;
                        default:
                                displayName = "GERD";
                }
        }

        public String getDisplayName() {
                return displayName;
        }

        /**
         * Update the displayName for JTree if Filename changed *
         */
        public void updateDisplayName() {
                setDisplayName();
        }

        public SwatComponentType getObjectType() {
                return objectType;
        }

        @Override
        public String toString() {
                return getDisplayName();
        }
}
