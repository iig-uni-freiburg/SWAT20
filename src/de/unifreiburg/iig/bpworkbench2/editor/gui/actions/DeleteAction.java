package de.unifreiburg.iig.bpworkbench2.editor.gui.actions;

import com.mxgraph.io.*;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTPlace;
import de.unifreiburg.iig.bpworkbench2.editor.gui.*;
import de.unifreiburg.iig.bpworkbench2.editor.mxgraphmod.util.png.mxPNGzTXtDecoder;
import de.unifreiburg.iig.bpworkbench2.editor.soul.Graph;
import de.unifreiburg.iig.bpworkbench2.editor.soul.Properties;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Map;
import java.util.SortedMap;

import javax.swing.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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

		if (graph != null)
		{
//			System.out.println(graph.getSelectionCell().getClass());
			mxCell cell;
			if(graph.getSelectionCell() instanceof mxCell){
			cell =(mxCell) graph.getSelectionCell();
			Object value = cell.getValue();
			if(cell.getParent().getValue() != null){
				AbstractGraphicalPN<?, ?, ?, ?, ?> n = (AbstractGraphicalPN<?, ?, ?, ?, ?>) cell.getParent().getValue();
				String cellID = (String) cell.getId();
				if(cellID.startsWith("p"))
			try {
				n.getPetriNet().removePlace(cellID);
			} catch (ParameterException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
				
				if(cellID.startsWith("t"))
			try {
				n.getPetriNet().removeTransition(cellID);
			} catch (ParameterException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
				
				if(cellID.startsWith("arc")){
					
					try {
						n.getPetriNet().removeFlowRelation(cellID);
					} catch (ParameterException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}
			}
			

			graph.removeCells();
		}}
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
