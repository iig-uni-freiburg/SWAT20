package de.uni.freiburg.iig.telematik.swat.editor.graph;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.util.Map;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.shape.mxIShape;
import com.mxgraph.shape.mxRectangleShape;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;

public class RectangleShape extends mxRectangleShape {

@Override
/**
 * Configures the graphics object ready to paint.
 * @param canvas the canvas to be painted to
 * @param state the state of cell to be painted
 * @param background whether or not this is the background stage of 
 * 			the shape paint
 * @return whether or not the shape is ready to be drawn
 */
protected boolean configureGraphics(mxGraphics2DCanvas canvas,
		mxCellState state, boolean background)
{
	Map<String, Object> style = state.getStyle();

	if (background)
	{
		// Paints the background of the shape
		Paint fillPaint = hasGradient(canvas, state) ? createFillPaint(getGradientBounds(canvas, state), style)
				: null;

		if (fillPaint != null)
		{
			canvas.getGraphics().setPaint(fillPaint);

			return true;
		}
		else
		{
			Color color = getFillColor(canvas, state);
			canvas.getGraphics().setColor(color);

			return color != null;
		}
	}
	else
	{
		canvas.getGraphics().setPaint(null);
		Color color = getStrokeColor(canvas, state);
		canvas.getGraphics().setColor(color);
		//TODO: untersuche verhalten für Handler
		canvas.getGraphics().setStroke(canvas.createStroke(style));

		return color != null;
	}
}

public Paint createFillPaint(mxRectangle bounds, Map<String, Object> style)
{
	Color fillColor = mxUtils.getColor(style, mxConstants.STYLE_FILLCOLOR);
	Paint fillPaint = null;

	if (fillColor != null)
	{
		Color gradientColor = mxUtils.getColor(style,
				mxConstants.STYLE_GRADIENTCOLOR);

		if (gradientColor != null)
		{
			String gradientDirection = mxUtils.getString(style,
					mxConstants.STYLE_GRADIENT_DIRECTION);

			float x1 = (float) bounds.getX();
			float y1 = (float) bounds.getY();
			float x2 = (float) bounds.getX();
			float y2 = (float) bounds.getY();

			if (gradientDirection == null
					|| gradientDirection
							.equals(mxConstants.DIRECTION_SOUTH))
			{
				y2 = (float) (bounds.getY() + bounds.getHeight());
			}
			else if (gradientDirection.equals(mxConstants.DIRECTION_EAST))
			{
				x2 = (float) (bounds.getX() + bounds.getWidth());
			}
			else if (gradientDirection.equals(mxConstants.DIRECTION_NORTH))
			{
				y1 = (float) (bounds.getY() + bounds.getHeight());
			}
			else if (gradientDirection.equals(mxConstants.DIRECTION_WEST))
			{
				x1 = (float) (bounds.getX() + bounds.getWidth());
			}
			else if (gradientDirection.equals("south_east"))
			{
				y2 = (float) (bounds.getY() + bounds.getHeight());
				x2 = (float) (bounds.getX() + bounds.getWidth());
			}

			fillPaint = new GradientPaint(x1, y1, fillColor, x2, y2,
					gradientColor, true);
		}
	}

	return fillPaint;
}

}
