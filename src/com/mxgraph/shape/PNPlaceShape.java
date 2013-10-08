package com.mxgraph.shape;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Ellipse2D.Float;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxCellState;

import de.invation.code.toval.graphic.CircularPointGroup;
import de.invation.code.toval.graphic.GraphicUtils;
import de.invation.code.toval.graphic.PColor;
import de.invation.code.toval.graphic.Position;
import de.invation.code.toval.graphic.VisualCircularPointGroup;
import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.CPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.TokenGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNPlace;

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

		
		
		
Object value = ((mxCell)state.getCell()).getValue();
		if((value == null) && (((mxCell)state.getCell()).getParent() !=null)){
			System.out.println(((mxCell)state.getCell()).getParent().getValue());
//		System.out.println(((mxCell)state.getCell()).getParent().getValue());
		AbstractGraphicalPN<?, ?, ?, ?, ?> n = (AbstractGraphicalPN<?, ?, ?, ?, ?>) ((mxCell)state.getCell()).getParent().getValue();
		try {
			n.getPetriNet().addPlace(((mxCell)state.getCell()).getId(), "sonew");
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 ((mxCell)state.getCell()).setValue(n);
		 mxCell cell = ((mxCell)state.getCell());
//		 cell.setId("new3");
		 state.setLabel("sonew");
//		 state.setCell(cell);
//		cell.notifyAll();

//		 
		}
		if(value instanceof AbstractGraphicalPN<?, ?, ?, ?, ?>){
			AbstractGraphicalPN<?, ?, ?, ?, ?> n = (AbstractGraphicalPN<?, ?, ?, ?, ?>)  value;
			Object place = n.getPetriNet().getPlace(((mxCell)state.getCell()).getId());
//			Counts all tokens
//			Iterator<Integer> countIt = tokenCount.iterator();
			k = ((AbstractCPNPlace<CPNFlowRelation>) place).getTokenCount();
			System.out.println(k);
//			while(countIt.hasNext()){
//				k += countIt.next();}

			
			//get Number of Circles
			Double dotNumber = 0.0;
			int c = 0;
			for(c=1; dotNumber<(k-1);c++){
				
				dotNumber +=new Double(((2*c)*Math.PI)).intValue();


			}

			
			int diameter = Math.min(temp.height, temp.width);
			diameter *= 0.3;
			diameter = Math.max(diameter, 6);
			
			if(c>1){
				diameter = Math.min(temp.width, temp.height);
				diameter *= 0.8;
				diameter /=( ((c-1)*2) +1);}//denominator:all circles in one row => 
			//(80% of the available inner CircleSize) / (maximal amount of dots) = size for one dot

			circularPointGroup = new CircularPointGroup(1, diameter);
			CPNGraphics g;Map<String, Color> colors = null;
			if( n.getPetriNetGraphics() instanceof CPNGraphics)
			{
				g = (CPNGraphics)				n.getPetriNetGraphics();
				colors = g.getColors();
			}
			//			if();
			Set<String> keyset = ((AbstractCPNPlace<CPNFlowRelation>) place).getState().support();

			for (String s : keyset){
//				System.out.println(s);
				try {
//					System.out.println(((AbstractCPNPlace<CPNFlowRelation>) place).getState().multiplicity(s));
					
					Color color = colors.get(s);
//					System.out.println(color + "-" + s);
					int number = ((AbstractCPNPlace<CPNFlowRelation>) place).getState().multiplicity(s);
					PColor pco;
					if(color !=null)
					pco = new PColor(color.getRed(), color.getGreen(), color.getBlue());
					else {pco = PColor.black;}
					
					circularPointGroup.addPoints(pco, number);
				} catch (ParameterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
//for(TokenGraphics tg : n.getPetriNetGraphics().getTokenGraphics().get(place)){
//	System.out.println(tg.getColorName().);
//}
				
	
		}

		
		this.dimension = dimension;
		if(k>0){
		this.dimension = new Dimension(circularPointGroup.getRequiredDiameter(), circularPointGroup.getRequiredDiameter());
		center = new Point(temp.x + temp.width/2,temp.y + temp.height/2);
		this.pointGroup = circularPointGroup;
		Graphics g = drawPoints(canvas, temp);}
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
//				canvas.fillShape(shape, hasShadow(canvas, state));
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
}