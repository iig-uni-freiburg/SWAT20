package com.mxgraph.shape;

import java.awt.Rectangle;
import java.util.Collection;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.util.mxSwingConstants;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTTransition;

public class PNTransitionShape extends mxBasicShape
{

	/**
	 * 
	 */
	public void paintShape(mxGraphics2DCanvas canvas, mxCellState state)
	{
		Map<String, Object> style = state.getStyle();
state.setLabel("");


////state.getCell()
//mxCell cell = (mxCell) state.getCell();
//Object value = ((mxCell)state.getCell()).getValue();
//
//
//if((value == null) && (((mxCell)state.getCell()).getParent() !=null)){

//AbstractGraphicalPN<?, ?, ?, ?, ?> n = (AbstractGraphicalPN<?, ?, ?, ?, ?>) ((mxCell)state.getCell()).getParent().getValue();
//SortedMap<Integer, String> transitionSortedMap = new TreeMap<Integer, String>();
//
//Collection<?> col = n.getPetriNet().getTransitions();
//for(Object o:col){

//	if(o instanceof PTTransition)
//	{
//		PTTransition transition = (PTTransition)o;
//
//		addTransitionToMap(transitionSortedMap, transition.getName(), "t");
//	}
//
//
//}	
//try {
//	n.getPetriNet().addTransition("t"+ getLowestIndex(transitionSortedMap));
//} catch (ParameterException e) {
//	// TODO Auto-generated catch block
//	e.printStackTrace();
//}	
//int index = getLowestIndex(transitionSortedMap);
//addTransitionToMap(transitionSortedMap, "t"+ index, "t");
//
//
////try {
////	n.getPetriNet().addTransition(((mxCell)state.getCell()).getId(), "name");
////} catch (ParameterException e) {
////	// TODO Auto-generated catch block
////	e.printStackTrace();
////}
// ((mxCell)state.getCell()).setValue("t" + index);
// ((mxCell)state.getCell()).setId("t" + index);
// cell.setValue("t" + index);
// cell.setId("t" + index);
//// mxCell cell = ((mxCell)state.getCell());
//// cell.setId("new3");
// state.setLabel("");
//// state.setCell(cell);
////cell.notifyAll();
//
//// 
//}


















		if (mxUtils.isTrue(style, mxConstants.STYLE_ROUNDED, false))
		{
			Rectangle tmp = state.getRectangle();

			int x = tmp.x;
			int y = tmp.y;
			int w = tmp.width;
			int h = tmp.height;
			int radius = getArcSize(w, h);

			boolean shadow = hasShadow(canvas, state);
			int shadowOffsetX = (shadow) ? mxConstants.SHADOW_OFFSETX : 0;
			int shadowOffsetY = (shadow) ? mxConstants.SHADOW_OFFSETY : 0;

			if (canvas.getGraphics().hitClip(x, y, w + shadowOffsetX,
					h + shadowOffsetY))
			{
				// Paints the optional shadow
				if (shadow)
				{
					canvas.getGraphics().setColor(mxSwingConstants.SHADOW_COLOR);
					canvas.getGraphics().fillRoundRect(
							x + mxConstants.SHADOW_OFFSETX,
							y + mxConstants.SHADOW_OFFSETY, w, h, radius,
							radius);
				}

				// Paints the background
				if (configureGraphics(canvas, state, true))
				{
					canvas.getGraphics().fillRoundRect(x, y, w, h, radius,
							radius);
				}

				// Paints the foreground
				if (configureGraphics(canvas, state, false))
				{
					canvas.getGraphics().drawRoundRect(x, y, w, h, radius,
							radius);
				}
			}
		}
		else
		{
			Rectangle rect = state.getRectangle();

			// Paints the background
			if (configureGraphics(canvas, state, true))
			{
				canvas.fillShape(rect, hasShadow(canvas, state));
			}

			// Paints the foreground
			if (configureGraphics(canvas, state, false))
			{
				canvas.getGraphics().drawRect(rect.x, rect.y, rect.width,
						rect.height);
			}
		}
	}

	/**
	 * Computes the arc size for the given dimension.
	 * 
	 * @param w Width of the rectangle.
	 * @param h Height of the rectangle.
	 * @return Returns the arc size for the given dimension.
	 */
	public int getArcSize(int w, int h)
	{
		int arcSize;

		if (w <= h)
		{
			arcSize = (int) Math.round(h
					* mxConstants.RECTANGLE_ROUNDING_FACTOR);

			if (arcSize > (w / 2))
			{
				arcSize = w / 2;
			}
		}
		else
		{
			arcSize = (int) Math.round(w
					* mxConstants.RECTANGLE_ROUNDING_FACTOR);

			if (arcSize > (h / 2))
			{
				arcSize = h / 2;
			}
		}
		return arcSize;
	}
	
	private void addTransitionToMap(Map<Integer, String> a, String string, String nameConvention) {
		if(string.startsWith(nameConvention) && isInteger(string.substring(nameConvention.length()))){
			Integer integer = new Integer(string.substring(nameConvention.length()));
			

			
			a.put(integer,nameConvention + integer);
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

}
