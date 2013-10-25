package com.mxgraph.shape;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Ellipse2D.Float;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxCellState;

import de.invation.code.toval.graphic.CircularPointGroup;
import de.invation.code.toval.graphic.GraphicUtils;
import de.invation.code.toval.graphic.PColor;
import de.invation.code.toval.graphic.Position;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.CPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPNNode;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTPlace;

public class PNPlaceShape extends mxBasicShape
{

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	
	protected static final Dimension defaultDimension = new Dimension(400,400);
	protected Dimension dimension = defaultDimension;
	protected Point center; 
	protected CircularPointGroup pointGroup;
	CircularPointGroup circularPointGroup;
	

	private int k;
	
	public Shape createShape(mxGraphics2DCanvas canvas, mxCellState state)
	{
		Rectangle temp = state.getRectangle();

		mxCell cell = (mxCell)state.getCell();
		state.setLabel("");
//Object value = ((mxCell)state.getCell()).getValue();
////		if((value == null) && (((mxCell)state.getCell()).getParent() !=null)){
////		AbstractGraphicalPN<?, ?, ?, ?, ?> n = (AbstractGraphicalPN<?, ?, ?, ?, ?>) ((mxCell)state.getCell()).getParent().getValue();
////		SortedMap<Integer, String> placeSortedMap = new TreeMap<Integer, String>();
////		
////		Collection<?> col = n.getPetriNet().getPlaces();
////		for(Object o:col){
////			if(o instanceof PTPlace)
////			{
////				PTPlace place = (PTPlace)o;
////
////				addPlaceToMap(placeSortedMap, place.getName(), "p");
////			}
////		
////		
////		}	
////		try {
////			n.getPetriNet().addPlace("p"+ getLowestIndex(placeSortedMap));
////		} catch (ParameterException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}	
////		int index = getLowestIndex(placeSortedMap);
////		addPlaceToMap(placeSortedMap, "p"+ index, "p");
////		
////		
//////		try {
//////			n.getPetriNet().addPlace(((mxCell)state.getCell()).getId(), "name");
//////		} catch (ParameterException e) {
//////			// TODO Auto-generated catch block
//////			e.printStackTrace();
//////		}
////		 ((mxCell)state.getCell()).setValue("p" + index);
////		 ((mxCell)state.getCell()).setId("p" + index);
////		 mxCell cell = ((mxCell)state.getCell());
////		  mxGeometry geom = new mxGeometry(0, 0, cell.getGeometry().getWidth(), 10);
////		  geom.setOffset(new mxPoint(0, cell.getGeometry().getHeight() + 5));
////          geom.setRelative(true);
////			mxCell label = new mxCell(n.getPetriNet().getPlace(cell.getId()).getLabel(), geom, "shape=none;fontSize=12");
////        
////        label.setVertex(true);
////        label.setConnectable(false);
////        cell.insert(label);
//////     cell.notify();
//////		 cell.setId("new3");
////		 state.setLabel("");
//////		 state.setCell(cell);
//////		cell.notifyAll();
////
//////		 
////		}
//		if(value instanceof AbstractGraphicalPN<?, ?, ?, ?, ?>){
//			AbstractGraphicalPN<?, ?, ?, ?, ?> n = (AbstractGraphicalPN<?, ?, ?, ?, ?>)  value;
//			Object place = n.getPetriNet().getPlace(((mxCell)state.getCell()).getId());
////			Counts all tokens
////			Iterator<Integer> countIt = tokenCount.iterator();
////			k = ((AbstractCPNPlace<CPNFlowRelation>) place).getTokenCount();
//			
//			
//			
//			
//			
//			
//			
//			
//			
//			
//			
//			
//			
//			
//			
//			
//			
//			
//			
//			k=1;
////			while(countIt.hasNext()){
////				k += countIt.next();}
//
//			
//			//get Number of Circles
//			Double dotNumber = 0.0;
//			int c = 0;
//			for(c=1; dotNumber<(k-1);c++){
//				
//				dotNumber +=new Double(((2*c)*Math.PI)).intValue();
//
//
//			}
//
//			
//			int diameter = Math.min(temp.height, temp.width);
//			diameter *= 0.3;
//			diameter = Math.max(diameter, 6);
//			
//			if(c>1){
//				diameter = Math.min(temp.width, temp.height);
//				diameter *= 0.8;
//				diameter /=( ((c-1)*2) +1);}//denominator:all circles in one row => 
//			//(80% of the available inner CircleSize) / (maximal amount of dots) = size for one dot
//
//			circularPointGroup = new CircularPointGroup(1, diameter);
//			CPNGraphics g;Map<String, Color> colors = null;
//			if( n.getPetriNetGraphics() instanceof CPNGraphics)
//			{
//				g = (CPNGraphics)				n.getPetriNetGraphics();
//				colors = g.getColors();
//			}
//			//			if();
////			Set<String> keyset = ((AbstractCPNPlace<CPNFlowRelation>) place).getState().support();
////
////			for (String s : keyset){
////				try {
////					
////					Color color = colors.get(s);
////					int number = ((AbstractCPNPlace<CPNFlowRelation>) place).getState().multiplicity(s);
////					PColor pco;
////					if(color !=null)
////					pco = new PColor(color.getRed(), color.getGreen(), color.getBlue());
////					else {pco = PColor.black;}
////					
////					circularPointGroup.addPoints(pco, number);
////				} catch (ParameterException e) {
////					// TODO Auto-generated catch block
////					e.printStackTrace();
////				}
////			}
//		
////for(TokenGraphics tg : n.getPetriNetGraphics().getTokenGraphics().get(place)){
////}
//				
//	
//		}
//
//		
//		this.dimension = dimension;
//		if(k>0){
////		this.dimension = new Dimension(circularPointGroup.getRequiredDiameter(), circularPointGroup.getRequiredDiameter());
////		center = new Point(temp.x + temp.width/2,temp.y + temp.height/2);
////		this.pointGroup = circularPointGroup;
////		Graphics g = drawPoints(canvas, temp);}
//		}
		Float shape = new Ellipse2D.Float(temp.x, temp.y, temp.width, temp.height);


		return shape;
	}

	
	protected Graphics drawPoints(mxGraphics2DCanvas canvas, Rectangle temp){
		Graphics g =canvas.getGraphics();
		Iterator<PColor> iter = pointGroup.getColors().iterator();
		PColor actColor;
		while(iter.hasNext()){
			actColor = iter.next();
			g.setColor(new Color(actColor.getRGB()));
			for(Position p: pointGroup.getCoordinatesFor(actColor)){
				GraphicUtils.fillCircle(g, (int) (center.getX()+p.getX()), (int) (center.getY()+p.getY()), pointGroup.getPointDiameter());
			}
		}
		return g;
	}

	public void paintShape(mxGraphics2DCanvas canvas, mxCellState state)
	{
		Shape shape = createShape(canvas, state);

		if (shape != null)
		{
			// Paints the background
			if (configureGraphics(canvas, state, true))
			{
				canvas.fillShape(shape, hasShadow(canvas, state));
//				canvas.fillShape(shape);
			}

			// Paints the foreground
			if (configureGraphics(canvas, state, false))
			{

				canvas.getGraphics().draw(shape);
//				mxCell cell = ((mxCell) state.getCell());
//				AbstractGraphicalPN<?, ?, ?, ?, ?> n = (AbstractGraphicalPN<?, ?, ?, ?, ?>) ((mxCell)state.getCell()).getParent().getValue();
//
//				if(n.getPetriNet().getTransition(((mxCell)cell).getId()) != null){
//
//	
//                mxGeometry geom = new mxGeometry(0, 0, ((mxCell) cell).getGeometry().getWidth(), 10);
//                geom.setOffset(new mxPoint(0, ((mxCell) cell).getGeometry().getHeight() + 5));
//                geom.setRelative(true);
//                mxCell label;
//                //if is for handling copy/paste
//                if (((mxCell) cell).getChildCount() == 0) {
//                    label = new mxCell(n.getPetriNet().getTransition(((mxCell)cell).getId()).getLabel(), geom, "shape=none;fontSize=12");
//                } else {
//                    label = (mxCell) ((mxCell) cell).getChildAt(0);
//                    label.setValue(n.getPetriNet().getTransition(((mxCell)cell).getId()).getLabel());
//                }
//                label.setVertex(true);
//                label.setConnectable(false);
//                ((mxCell) cell).insert(label);
//                 
////                canvas.getGraphics().
//				}
			}
		}
	}
//	/**
//	 * gets the NodeGraphicAttribute of the given Node. 
//	 * 
//	 * @param graphicalNet
//	 * @param node
//	 * @return nodeGraphics 
//	 */
//	private static NodeGraphics getNodeGraphics(
//			AbstractGraphicalPN<?, ?, ?, ?, ?> graphicalNet,
//			AbstractPNNode<?> node) {
//		NodeGraphics nodeGraphics = null;
//		Map<?, NodeGraphics> graphics = null;
//
//		if (node instanceof AbstractPlace)
//			graphics = graphicalNet.getPetriNetGraphics().getPlaceGraphics();
//		if (node instanceof AbstractTransition)
//			graphics = graphicalNet.getPetriNetGraphics()
//					.getTransitionGraphics();
//
//		for (Entry<?, ?> place : graphics.entrySet()) {
//			if (place.getKey() == node)
//				nodeGraphics = (NodeGraphics) place.getValue();
//		}
//		return nodeGraphics;
//	}
//	private void addPlaceToMap(Map<Integer, String> a, String string, String nameConvention) {
//		if(string.startsWith(nameConvention) && isInteger(string.substring(nameConvention.length()))){
//			Integer integer = new Integer(string.substring(nameConvention.length()));
//			
//
//			
//			a.put(integer,nameConvention + integer);
//		}
//		}
//	
//	public boolean isInteger(String string) {
//	    try {
//	        Integer.valueOf(string);
//	        return true;
//	    } catch (NumberFormatException e) {
//	        return false;
//	    }
//	}
//	public int getLowestIndex(SortedMap<Integer, String> a) {
//		if(!a.isEmpty()){
//		for(int i = 1;i<=a.lastKey();i++){
//			if(a.get(i) == null)
//			return i;
//		}
//		;
//		return a.lastKey()+1;}
//		else{return 1;}
//	}
}
