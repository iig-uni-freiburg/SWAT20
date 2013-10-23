package de.unifreiburg.iig.bpworkbench2.editor.soul;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.model.*;
import com.mxgraph.util.*;
import com.mxgraph.view.*;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Dimension;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line.Shape;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line.Style;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Position;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTTransition;

public class Graph extends mxGraph {

    private DataHolder dataHolder;
    int i=0;
    Map<String, NodeGraphics> placeGraphics = new HashMap<String, NodeGraphics>();

    public Graph() {
        setAlternateEdgeStyle("edgeStyle=mxEdgeStyle.ElbowConnector;elbow=vertical");
        setMultigraph(true);
        setCellsEditable(false);
        setDisconnectOnMove(false);
        setExtendParents(false); //disables extending parents after adding children
        setVertexLabelsMovable(false);
//        dataHolder = new DataHolder(this);
    }

//    public DataHolder getDataHolder() {
//        return dataHolder;
//    }

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
            } 
        
        else{
        	AbstractGraphicalPN<?, ?, ?, ?, ?> n = (AbstractGraphicalPN<?, ?, ?, ?, ?>) (edgeSource).getParent().getValue();

        	if(edgeSource.getStyle().contentEquals("shape=placeShape;") && edgeSource.getId() != edgeTarget.getId()){
        		System.out.println(edgeSource.getId());
        		System.out.println(edgeTarget);
        	try {
        		System.out.println(edgeSource.getId() + "#"+ edgeTarget.getId());
				AbstractFlowRelation<?,?,?>fr = n.getPetriNet().addFlowRelationPT(edgeSource.getId(), edgeTarget.getId());
				((mxCell) edge).setId(fr.getName());
			} catch (ParameterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	}
        	else if(edgeTarget.getStyle().contentEquals("shape=transitionShape;")){

        			SortedMap<Integer, String> transitionSortedMap = new TreeMap<Integer, String>();

        			Collection<?> col = n.getPetriNet().getTransitions();
        			System.out.println(n.getPetriNet().getTransitions().size());
        			for(Object o:col){
        				System.out.println(o.getClass());
        				if(o instanceof PTTransition)
        				{
        					PTTransition transition = (PTTransition)o;

        					addTransitionToMap(transitionSortedMap, transition.getName(), "t");
        				}


        			}	
        			try {
        				n.getPetriNet().addTransition("t"+ getLowestIndex(transitionSortedMap));
        			} catch (ParameterException e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			}	
        			int id = getLowestIndex(transitionSortedMap);
        			addTransitionToMap(transitionSortedMap, "t"+ id, "t");
        			edgeTarget.setValue("t" + id);
        			edgeTarget.setId("t" + id);
           		  mxGeometry geom = new mxGeometry(0, 0, edgeTarget.getGeometry().getWidth(), 10);

        			 geom.setOffset(new mxPoint(0, edgeTarget.getGeometry().getHeight() + 5));
                     geom.setRelative(true);
                     System.out.println();
                     
           			mxCell label = new mxCell(n.getPetriNet().getTransition("t" + id).getLabel(), geom, "shape=none;fontSize=12");
                   
                   label.setVertex(true);
                   label.setConnectable(false);
                   edgeTarget.insert(label);
   
               	try {
    				AbstractFlowRelation<?,?,?>fr = n.getPetriNet().addFlowRelationPT(edgeSource.getId(), edgeTarget.getId());
    				((mxCell) edge).setId(fr.getName());
    			} catch (ParameterException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}

        			}
        		
        		
        		
        		
        
        		
        		
        	

        	System.out.println(edgeTarget + "<----ID");
        	if(edgeSource.getStyle().contentEquals("shape=transitionShape;") && edgeSource.getId() != edgeTarget.getId())
            	try {
            		AbstractFlowRelation<?,?,?>fr = n.getPetriNet().addFlowRelationTP(edgeSource.getId(), edgeTarget.getId());
    				((mxCell) edge).setId(fr.getName());
    				
    			} catch (ParameterException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
        	else if(edgeTarget.getStyle().contentEquals("shape=placeShape;")){
            	SortedMap<Integer, String> placeSortedMap = new TreeMap<Integer, String>();
    		
    		Collection<?> col = n.getPetriNet().getPlaces();
    		System.out.println(n.getPetriNet().getPlaces().size());
    		for(Object o:col){
    			System.out.println(o.getClass());
    			if(o instanceof PTPlace)
    			{
    				PTPlace place = (PTPlace)o;

    				addPlaceToMap(placeSortedMap, place.getName(), "p");
    			}
    		
    		
    		}	
    		try {
    		n.getPetriNet().addPlace("p"+ getLowestIndex(placeSortedMap));
    		} catch (ParameterException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}	
    		System.out.println(placeSortedMap + "before");
    		int id = getLowestIndex(placeSortedMap);
    		System.out.println(id);
    		addPlaceToMap(placeSortedMap, "p"+ id, "p");
    		
    		System.out.println("MAP" + placeSortedMap);
    		

    		 edgeTarget.setValue("p" + id);
    		edgeTarget.setId("p" + id);
//    		 mxCell cell = ((mxCell)state.getCell());
    		  mxGeometry geom = new mxGeometry(0, 0, edgeTarget.getGeometry().getWidth(), 10);
    		  geom.setOffset(new mxPoint(0, edgeTarget.getGeometry().getHeight() + 5));
              geom.setRelative(true);
              System.out.println();
              
    			mxCell label = new mxCell(n.getPetriNet().getPlace("p" + id).getLabel(), geom, "shape=none;fontSize=12");
            
            label.setVertex(true);
            label.setConnectable(false);
            edgeTarget.insert(label);

        	try {
				AbstractFlowRelation<?,?,?>fr = n.getPetriNet().addFlowRelationTP(edgeSource.getId(), edgeTarget.getId());
				((mxCell) edge).setId(fr.getName());
			} catch (ParameterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		}
    			
        	
        }
        }
    }

    @Override
    public void cellsAdded(Object[] cells, Object parent, Integer index, Object source, Object target, boolean absolute) {
        super.cellsAdded(cells, parent, index, source, target, absolute);
        //creating names 
System.out.println("ADDEDDDDDDDDDDDDDDDDDD");
        for (Object object : cells) {
        	System.out.println(object + "###########################");
            if (object instanceof mxCell) {
                mxCell cell = (mxCell) object;
                Object value = cell.getValue();
                if (cell.getParent().getValue() instanceof AbstractGraphicalPN<?, ?, ?, ?, ?>) {
                	
                	
                	
            		if((value == null) && (cell.getParent() !=null)){
            			System.out.println(cell.getParent().getValue());
                    	AbstractGraphicalPN<?, ?, ?, ?, ?>	n = (AbstractGraphicalPN<?, ?, ?, ?, ?>) cell.getParent().getValue();
            		if(cell.getStyle().contentEquals("shape=placeShape;")){
                    	SortedMap<Integer, String> placeSortedMap = new TreeMap<Integer, String>();
            		
            		Collection<?> col = n.getPetriNet().getPlaces();
            		System.out.println(n.getPetriNet().getPlaces().size());
            		for(Object o:col){
            			System.out.println(o.getClass());
            			if(o instanceof PTPlace)
            			{
            				PTPlace place = (PTPlace)o;

            				addPlaceToMap(placeSortedMap, place.getName(), "p");
            			}
            		
            		
            		}	
            		try {
            			String placeName = "p"+ getLowestIndex(placeSortedMap);
            		n.getPetriNet().addPlace(placeName);
            		PTMarking initialMarking = new PTMarking();
            		int ressources = 1;
					n.getPetriNet().getPlace(placeName).setCapacity(ressources );
            		initialMarking.set(placeName, ressources);
            		
					if(n.getPetriNet() instanceof PTNet){
						PTNet ptNet = (PTNet) n.getPetriNet();
						ptNet.setInitialMarking(initialMarking);
					};
					
//            	String color;
//				Shape shape;
//				Style style;
//				double width;
				Line line = new Line();
				Fill fill = new Fill();
				Dimension dimension = new Dimension(Dimension.DEFAULT_DIMENSION_X, Dimension.DEFAULT_DIMENSION_Y);
				double x = cell.getGeometry().getCenterX();
				double y = cell.getGeometry().getCenterY();
				Position position = new Position(x, y);
				NodeGraphics nodeGraphics = new NodeGraphics(position, dimension, fill,line);
				placeGraphics.put(placeName, nodeGraphics );
				n.getPetriNetGraphics().setPlaceGraphics(placeGraphics);
            		} catch (ParameterException e) {
            			// TODO Auto-generated catch block
            			e.printStackTrace();
            		}	
            		System.out.println(placeSortedMap + "before");
            		int id = getLowestIndex(placeSortedMap);
            		System.out.println(id);
            		addPlaceToMap(placeSortedMap, "p"+ id, "p");
            		
            		System.out.println("MAP" + placeSortedMap);
            		

            		 cell.setValue("p" + id);
            		cell.setId("p" + id);
//            		 mxCell cell = ((mxCell)state.getCell());
            		  mxGeometry geom = new mxGeometry(0, 0, cell.getGeometry().getWidth(), 10);
            		  geom.setOffset(new mxPoint(0, cell.getGeometry().getHeight() + 5));
//                      geom.setRelative(true);
                      System.out.println();
                      
            			mxCell label = new mxCell(n.getPetriNet().getPlace("p" + id).getLabel(), geom, "shape=none;fontSize=12");
                    
                    label.setVertex(true);
                    
                    label.setConnectable(false);
                    cell.insert(label);
                 

		 
            		}
            		
            		if(cell.getStyle().contentEquals("shape=transitionShape;")){

            			SortedMap<Integer, String> transitionSortedMap = new TreeMap<Integer, String>();

            			Collection<?> col = n.getPetriNet().getTransitions();
            			System.out.println(n.getPetriNet().getTransitions().size());
            			for(Object o:col){
            				System.out.println(o.getClass());
            				if(o instanceof PTTransition)
            				{
            					PTTransition transition = (PTTransition)o;

            					addTransitionToMap(transitionSortedMap, transition.getName(), "t");
            				}


            			}	
            			try {
            				n.getPetriNet().addTransition("t"+ getLowestIndex(transitionSortedMap));
            			} catch (ParameterException e) {
            				// TODO Auto-generated catch block
            				e.printStackTrace();
            			}	
            			int id = getLowestIndex(transitionSortedMap);
            			addTransitionToMap(transitionSortedMap, "t"+ id, "t");
            			cell.setValue("t" + id);
            			 cell.setId("t" + id);
               		  mxGeometry geom = new mxGeometry(0, 0, cell.getGeometry().getWidth(), 10);

            			 geom.setOffset(new mxPoint(0, cell.getGeometry().getHeight() + 5));
                         geom.setRelative(true);
                         System.out.println();
                         
               			mxCell label = new mxCell(n.getPetriNet().getTransition("t" + id).getLabel(), geom, "shape=none;fontSize=12");
                       
                       label.setVertex(true);
                       label.setConnectable(false);
                       cell.insert(label);

            			// mxCell cell = ((mxCell)state.getCell());
            			// cell.setId("new3");
//            			 state.setLabel("");
            			// state.setCell(cell);
            			//cell.notifyAll();

            			// 
            			}
            			
            			
            		}
            		
            		}
//                    CellInfo info = (CellInfo) cell.getValue();
//                    if (info.isPlace()) {
//                        info.setName("P" + (dataHolder.getLastPlaceName() + 1));
//                    }
//                    if (info.isTransition()) {
//                        info.setName("T" + (dataHolder.getLastTransitionName() + 1));
//                    }
//                    dataHolder.updateData();

                    //adding labels
//                    getModel().beginUpdate();
//                    {
//                        mxGeometry geom = new mxGeometry(0, 0, cell.getGeometry().getWidth(), 10);
//                        geom.setOffset(new mxPoint(0, cell.getGeometry().getHeight() + 5));
//                        geom.setRelative(true);
//                        mxCell label;
//                       System.out.println(n.getPetriNet());
//                        //if is for handling copy/paste
//                        if (cell.getChildCount() == 0) {
//							label = new mxCell(cell.getId(), geom, "shape=none;fontSize=12");
//							i++;
//                        } else {
//                            label = (mxCell) cell.getChildAt(0);
//                            label.setValue( "name2");
//                            
//                        }
//                        label.setVertex(true);
//                        label.setConnectable(false);
//                        cell.insert(label);
//                    }
//                    getModel().endUpdate();
                	System.out.println("BOKAAAAAAAAAAAAAAAAAA");
                    refresh();
                }
            }
        }
//    }

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
    
    private void addPlaceToMap(Map<Integer, String> a, String string, String nameConvention) {
		if(string.startsWith(nameConvention) && isInteger(string.substring(nameConvention.length()))){
			Integer integer = new Integer(string.substring(nameConvention.length()));
			

			
			a.put(integer,nameConvention + integer);
System.out.println("ADDED"+ nameConvention + integer);
		}
		}
	private void addTransitionToMap(Map<Integer, String> a, String string, String nameConvention) {
		if(string.startsWith(nameConvention) && isInteger(string.substring(nameConvention.length()))){
			Integer integer = new Integer(string.substring(nameConvention.length()));
			

			
			a.put(integer,nameConvention + integer);
System.out.println("ADDED"+ nameConvention + integer);
		}
		}
	public boolean isInteger(String string) {
	    try {
	        Integer.valueOf(string);
	        return true;
	    } catch (NumberFormatException e) {
	        return false;
	    }
	}
	public int getLowestIndex(SortedMap<Integer, String> a) {
		if(!a.isEmpty()){
		for(int i = 1;i<=a.lastKey();i++){
			if(a.get(i) == null)
			return i;
		}
		;
		return a.lastKey()+1;}
		else{return 1;}
	}
	
	@Override
	/**
	 * Moves the specified cells by the given vector, disconnecting the cells
	 * using disconnectGraph if disconnect is true. This method fires
	 * mxEvent.CELLS_MOVED while the transaction is in progress.
	 */
	public void cellsMoved(Object[] cells, double dx, double dy,
			boolean disconnect, boolean constrain)
	{
		if (cells != null && (dx != 0 || dy != 0))
		{
			model.beginUpdate();
			try
			{
				if (disconnect)
				{
					disconnectGraph(cells);
				}

				for (int i = 0; i < cells.length; i++)
				{
					translateCell(cells[i], dx, dy);

					if (constrain)
					{
						constrainChild(cells[i]);
					}
				}

				if (isResetEdgesOnMove())
				{
					resetEdges(cells);
				}

				fireEvent(new mxEventObject(mxEvent.CELLS_MOVED, "cells",
						cells, "dx", dx, "dy", dy, "disconnect", disconnect));
				for(Object o :cells){
					if(o instanceof mxCell){
						mxCell cell = (mxCell) o;
						  if (cell.getParent()!= null && cell.getParent().getValue() instanceof AbstractGraphicalPN<?, ?, ?, ?, ?>) {
			                    	AbstractGraphicalPN<?, ?, ?, ?, ?>	n = (AbstractGraphicalPN<?, ?, ?, ?, ?>) cell.getParent().getValue();
			                    	NodeGraphics pG = n.getPetriNetGraphics().getPlaceGraphics().get(cell.getId());
									pG.getPosition().setX(cell.getGeometry().getCenterX());
									pG.getPosition().setY(cell.getGeometry().getCenterY());
			                    	
					}
						  }
				}
			}
			finally
			{
				model.endUpdate();
			}
		}
	}
}
