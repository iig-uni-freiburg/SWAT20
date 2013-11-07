package de.unifreiburg.iig.bpworkbench2.editor.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class DeleteAction extends AbstractAction {

	/**
	 * 
	 */
//	private static final long serialVersionUID = -8212339796803275529L;

	/**
	 * 
	 * @param name
	 */
	public DeleteAction(String name)
	{
		super(name);
	}

	/**
	 * 
	 */
	public void actionPerformed(ActionEvent e)
	{
		mxGraph graph = getGraph(e);

//		if (graph != null)
//		{
////			System.out.println(graph.getSelectionCell().getClass());
//			mxCell cell;
//			if(graph.getSelectionCell() instanceof mxCell){
//			cell =(mxCell) graph.getSelectionCell();
//			Object value = cell.getValue();
//			if(cell.getParent().getValue() != null){
//				AbstractGraphicalPN<?, ?, ?, ?, ?> n = (AbstractGraphicalPN<?, ?, ?, ?, ?>) cell.getParent().getValue();
//				String cellID = (String) cell.getId();
//				if(cellID.startsWith("p"))
//			try {
//				n.getPetriNet().removePlace(cellID);
//			} catch (ParameterException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//				
//				if(cellID.startsWith("t"))
//			try {
//				n.getPetriNet().removeTransition(cellID);
//			} catch (ParameterException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//				
//				if(cellID.startsWith("arc")){
//					
//					try {
//						n.getPetriNet().removeFlowRelation(cellID);
//					} catch (ParameterException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//					
//				}
//			}
//			

			graph.removeCells();
//		}
//	}
	}
	/**
	 * 
	 * @param e
	 * @return Returns the graph for the given action event.
	 */
	public static final mxGraph getGraph(ActionEvent e)
	{
		Object source = e.getSource();

		if (source instanceof mxGraphComponent)
		{
			return ((mxGraphComponent) source).getGraph();
		}

		return null;
	}

}
