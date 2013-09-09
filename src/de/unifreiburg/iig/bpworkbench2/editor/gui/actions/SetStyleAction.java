package de.unifreiburg.iig.bpworkbench2.editor.gui.actions;

import com.mxgraph.swing.util.mxGraphActions;
import com.mxgraph.view.mxGraph;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

public class SetStyleAction extends AbstractAction {

    protected String value;

    public SetStyleAction(String value) {
        this.value = value;
    }

    public void actionPerformed(ActionEvent e) {
        mxGraph graph = mxGraphActions.getGraph(e);

        if (graph != null && !graph.isSelectionEmpty()) {
            graph.setCellStyle(value);
        }
    }
}
