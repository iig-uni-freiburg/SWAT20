package de.unifreiburg.iig.bpworkbench2.editor.gui.actions;

import com.mxgraph.swing.util.mxGraphActions;
import com.mxgraph.view.mxGraph;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

public class ToggleAction extends AbstractAction {

    protected String key;
    protected boolean defaultValue;

    public ToggleAction(String key) {
        this(key, false);
    }

    public ToggleAction(String key, boolean defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public void actionPerformed(ActionEvent e) {
        mxGraph graph = mxGraphActions.getGraph(e);

        if (graph != null) {
            graph.toggleCellStyles(key, defaultValue);
        }
    }
}
