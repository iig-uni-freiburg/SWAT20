package de.unifreiburg.iig.bpworkbench2.editor.soul;

import java.util.Map;

import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.model.*;
import com.mxgraph.util.*;
import com.mxgraph.view.*;

import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;

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
        if (edgeSource != null && edgeTarget != null) {
            System.out.println(edgeSource.getStyle());
//            CellInfo targetInfo = (CellInfo) edgeTarget.getValue();
            if (edgeSource.getStyle().contentEquals(edgeTarget.getStyle().toString())) {
                removeCells(new Object[]{edge});
            } else {
                dataHolder.updateData();
            }
        }
    }

    @Override
    public void cellsAdded(Object[] cells, Object parent, Integer index, Object source, Object target, boolean absolute) {
        super.cellsAdded(cells, parent, index, source, target, absolute);
System.out.println("testiiiiiiiiiiiiiiiiiiiiii");
        //creating names 
        for (Object object : cells) {
            if (object instanceof mxCell) {
                mxCell cell = (mxCell) object;
                
                if (cell.getParent().getValue() instanceof AbstractGraphicalPN<?, ?, ?, ?, ?>) {
                	AbstractGraphicalPN<?, ?, ?, ?, ?>	n = (AbstractGraphicalPN<?, ?, ?, ?, ?>) cell.getParent().getValue();
//                    CellInfo info = (CellInfo) cell.getValue();
//                    if (info.isPlace()) {
//                        info.setName("P" + (dataHolder.getLastPlaceName() + 1));
//                    }
//                    if (info.isTransition()) {
//                        info.setName("T" + (dataHolder.getLastTransitionName() + 1));
//                    }
//                    dataHolder.updateData();

                    //adding labels
                    getModel().beginUpdate();
                    {
                        mxGeometry geom = new mxGeometry(0, 0, cell.getGeometry().getWidth(), 10);
                        geom.setOffset(new mxPoint(0, cell.getGeometry().getHeight() + 5));
                        geom.setRelative(true);
                        mxCell label;
//                       System.out.println(n.getPetriNet().getPlace(cell.getId()).getLabel());
                        //if is for handling copy/paste
                        if (cell.getChildCount() == 0) {
                            label = new mxCell("name" , geom, "shape=none;fontSize=12");
                        } else {
                            label = (mxCell) cell.getChildAt(0);
                            label.setValue( "name2");
                            
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
    @Override
	/**
	 * Returns a string or DOM node that represents the label for the given
	 * cell. This implementation uses <convertValueToString> if <labelsVisible>
	 * is true. Otherwise it returns an empty string.
	 * 
	 * @param cell <mxCell> whose label should be returned.
	 * @return Returns the label for the given cell.
	 */
	public String getLabel(Object cell)
	{
		String result = "";

		if (cell != null)
		{
			mxCellState state = view.getState(cell);
			Map<String, Object> style = (state != null) ? state.getStyle()
					: getCellStyle(cell);

			if (labelsVisible
					&& !mxUtils.isTrue(style, mxConstants.STYLE_NOLABEL, false))
			{
				result = convertValueToString(cell);
				if(result.contains("de.uni.freiburg.iig.telematik.sepia")){
//					System.out.println(((mxCell)cell).getValue());
					AbstractGraphicalPN<?, ?, ?, ?, ?> n = (AbstractGraphicalPN<?, ?, ?, ?, ?>)((mxCell)cell).getValue();
					System.out.println(((mxCell)cell).getId());
					if(n.getPetriNet().getPlace(((mxCell)cell).getId()) != null){
//					result = n.getPetriNet().getPlace(((mxCell)cell).getId()).getLabel();
					result = "";
					  getModel().beginUpdate();
	                    {
	                        mxGeometry geom = new mxGeometry(0, 0, ((mxCell) cell).getGeometry().getWidth(), 10);
	                        geom.setOffset(new mxPoint(0, ((mxCell) cell).getGeometry().getHeight() + 5));
	                        geom.setRelative(true);
	                        mxCell label;
	                        //if is for handling copy/paste
	                        if (((mxCell) cell).getChildCount() == 0) {
	                            label = new mxCell(n.getPetriNet().getPlace(((mxCell)cell).getId()).getLabel(), geom, "shape=none;fontSize=12");
	                        } else {
	                            label = (mxCell) ((mxCell) cell).getChildAt(0);
	                            label.setValue(n.getPetriNet().getPlace(((mxCell)cell).getId()).getLabel());
	                        }
	                        label.setVertex(true);
	                        label.setConnectable(false);
	                        ((mxCell) cell).insert(label);
	                    }
	                    getModel().endUpdate();
					}
					if(n.getPetriNet().getTransition(((mxCell)cell).getId()) != null){
//						result = n.getPetriNet().getPlace(((mxCell)cell).getId()).getLabel();
						result = "";
						  getModel().beginUpdate();
		                    {
		                        mxGeometry geom = new mxGeometry(0, 0, ((mxCell) cell).getGeometry().getWidth(), 10);
		                        geom.setOffset(new mxPoint(0, ((mxCell) cell).getGeometry().getHeight() + 5));
		                        geom.setRelative(true);
		                        mxCell label;
		                        //if is for handling copy/paste
		                        if (((mxCell) cell).getChildCount() == 0) {
		                            label = new mxCell(n.getPetriNet().getTransition(((mxCell)cell).getId()).getLabel(), geom, "shape=none;fontSize=12");
		                        } else {
		                            label = (mxCell) ((mxCell) cell).getChildAt(0);
		                            label.setValue(n.getPetriNet().getTransition(((mxCell)cell).getId()).getLabel());
		                        }
		                        label.setVertex(true);
		                        label.setConnectable(false);
		                        ((mxCell) cell).insert(label);
		                    }
		                    getModel().endUpdate();
						}
				}
			}
			
		}

		return result;
	}
}
