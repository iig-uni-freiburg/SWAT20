package de.unifreiburg.iig.bpworkbench2.editor.soul;

import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.model.*;
import com.mxgraph.util.*;
import com.mxgraph.view.*;

public class Graph extends mxGraph {

    private DataHolder dataHolder;

    public Graph() {
        setAlternateEdgeStyle("edgeStyle=mxEdgeStyle.ElbowConnector;elbow=vertical");
        setMultigraph(true);
        setCellsEditable(false);
        setDisconnectOnMove(false);
        setExtendParents(false); //disables extending parents after adding children
        setVertexLabelsMovable(false);
        dataHolder = new DataHolder(this);
    }

    public DataHolder getDataHolder() {
        return dataHolder;
    }

    @Override
    public void cellConnected(Object edge, Object terminal, boolean source, mxConnectionConstraint constraint) {
        super.cellConnected(edge, terminal, source, constraint);

        //making edges parallel
        mxParallelEdgeLayout layout = new mxParallelEdgeLayout(this);
        layout.execute(getDefaultParent());

        //removing edges in place-to-place and tr2tr connections
        mxCell edgeSource = (mxCell) ((mxCell) edge).getSource();
        mxCell edgeTarget = (mxCell) ((mxCell) edge).getTarget();
//        if (edgeSource != null && edgeTarget != null) {
//            CellInfo sourceInfo = (CellInfo) edgeSource.getValue();
//            CellInfo targetInfo = (CellInfo) edgeTarget.getValue();
//            if (sourceInfo.getType().equals(targetInfo.getType())) {
//                removeCells(new Object[]{edge});
//            } else {
//                dataHolder.updateData();
//            }
//        }
    }

    @Override
    public void cellsAdded(Object[] cells, Object parent, Integer index, Object source, Object target, boolean absolute) {
        super.cellsAdded(cells, parent, index, source, target, absolute);

        //creating names
        for (Object object : cells) {
            if (object instanceof mxCell) {
                mxCell cell = (mxCell) object;
                if (cell.getValue() instanceof CellInfo) {
                    CellInfo info = (CellInfo) cell.getValue();
                    if (info.isPlace()) {
                        info.setName("P" + (dataHolder.getLastPlaceName() + 1));
                    }
                    if (info.isTransition()) {
                        info.setName("T" + (dataHolder.getLastTransitionName() + 1));
                    }
                    dataHolder.updateData();

                    //adding labels
                    getModel().beginUpdate();
                    {
                        mxGeometry geom = new mxGeometry(0, 0, cell.getGeometry().getWidth(), 10);
                        geom.setOffset(new mxPoint(0, cell.getGeometry().getHeight() + 5));
                        geom.setRelative(true);
                        mxCell label;
                        //if is for handling copy/paste
                        if (cell.getChildCount() == 0) {
                            label = new mxCell(info.getName(), geom, "shape=none;fontSize=12");
                        } else {
                            label = (mxCell) cell.getChildAt(0);
                            label.setValue(info.getName());
                        }
                        label.setVertex(true);
                        label.setConnectable(false);
                        cell.insert(label);
                    }
                    getModel().endUpdate();
                    refresh();
                }
            }
        }
    }

    @Override
    public void cellsRemoved(Object[] cells) {
        super.cellsRemoved(cells);
        dataHolder.updateData();
    }

    @Override
    public void cellsResized(Object[] cells, mxRectangle[] bounds) {
        super.cellsResized(cells, bounds);

        //handling label position
        for (Object object : cells) {
            mxCell cell = (mxCell) object;
            mxCell child = (mxCell) cell.getChildAt(0);
            mxGeometry geometry = new mxGeometry(0, 0, cell.getGeometry().getWidth(), 10);
            geometry.setRelative(true); //important!
            geometry.setOffset(new mxPoint(0, cell.getGeometry().getHeight() + 5));
            child.setGeometry(geometry);

            if (cell.getChildCount() > 1) {
                child = (mxCell) cell.getChildAt(1);
                geometry = new mxGeometry(0, 0, cell.getGeometry().getWidth(), 10);
                geometry.setRelative(true); //important!
                geometry.setOffset(new mxPoint(0, -10));
                child.setGeometry(geometry);
            }
        }
    }
}
