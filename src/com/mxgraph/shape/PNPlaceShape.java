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

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.view.mxCellState;

import de.invation.code.toval.graphic.CircularPointGroup;
import de.invation.code.toval.graphic.GraphicUtils;
import de.invation.code.toval.graphic.PColor;
import de.invation.code.toval.graphic.Position;
import de.invation.code.toval.graphic.VisualCircularPointGroup;

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
	
	public Shape createShape(mxGraphics2DCanvas canvas, mxCellState state)
	{
		Rectangle temp = state.getRectangle();
//		Counts all tokens
//			Iterator<Integer> countIt = tokenCount.iterator();
			int k = 5;
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

		CircularPointGroup circularPointGroup = new CircularPointGroup(1, diameter);
		circularPointGroup.addPoints(PColor.black, 1);
		circularPointGroup.addPoints(PColor.blue, 5);
		this.dimension = dimension;
		
		this.dimension = new Dimension(circularPointGroup.getRequiredDiameter(), circularPointGroup.getRequiredDiameter());
		center = new Point(temp.x + temp.width/2,temp.y + temp.height/2);
		this.pointGroup = circularPointGroup;
		Graphics g = drawPoints(canvas, temp);
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
			}
		}
	}
}
