package de.uni.freiburg.iig.telematik.swat.editor.graph;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.util.Map;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.shape.mxEllipseShape;
import com.mxgraph.shape.mxIShape;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;

import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill.GradientRotation;

public class EllipseShape extends mxEllipseShape {

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

			if (gradientColor != null) {
				GradientRotation gradientRotation = GradientRotation.getGradientRotation(mxUtils.getString(style, "Gradient_Rotation"));

				float x1 = (float) bounds.getX();
				float y1 = (float) bounds.getY();
				float x2 = (float) bounds.getX();
				float y2 = (float) bounds.getY();

				switch(gradientRotation){
				case DIAGONAL:
					y2 = (float) (bounds.getY() + bounds.getHeight());
					x2 = (float) (bounds.getX() + bounds.getWidth());
					break;
				case HORIZONTAL:
					x2 = (float) (bounds.getX() + bounds.getWidth());
					break;
				case VERTICAL:
					y2 = (float) (bounds.getY() + bounds.getHeight());
					break;
				default:
					break;
				
				}

				fillPaint = new GradientPaint(x1, y1, fillColor, x2, y2,
						gradientColor, true);
			}
		}

		return fillPaint;
	}
}
