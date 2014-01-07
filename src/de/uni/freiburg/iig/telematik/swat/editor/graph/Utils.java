package de.uni.freiburg.iig.telematik.swat.editor.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.awt.Stroke;
import java.net.URI;
import java.util.Map;

import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUtils;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractObjectGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AnnotationGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.ArcGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill.GradientRotation;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line.Style;

public class Utils extends mxUtils {
	/**
	 * Returns the paint bounds for the given label.
	 * @param centery 
	 * @param centerx 
	 * @param centery 
	 * @param centerx 
	 */
	public static mxRectangle getLabelPaintBounds(String label,
			Map<String, Object> style, boolean isHtml, mxPoint offset,
 double centerx, double centery, mxRectangle vertexBounds, double scale)
	{
		double wrapWidth = 0;

		if (isHtml
				&& vertexBounds != null
				&& mxUtils.getString(style, mxConstants.STYLE_WHITE_SPACE,
						"nowrap").equals("wrap"))
		{
			wrapWidth = vertexBounds.getWidth();
		}

		mxRectangle size = mxUtils.getLabelSize(label, style, isHtml, scale,
				wrapWidth);

		// Measures font with full scale and scales back
		size.setWidth(size.getWidth() / scale);
		size.setHeight(size.getHeight() / scale);

		double x = offset.getX();
		double y = offset.getY();
		double width = 0;
		double height = 0;

		if (vertexBounds != null)
		{
			x += vertexBounds.getCenterX();
			y += vertexBounds.getCenterY();

			if (mxUtils.getString(style, mxConstants.STYLE_SHAPE, "").equals(
					mxConstants.SHAPE_SWIMLANE))
			{
				// Limits the label to the swimlane title
				boolean horizontal = mxUtils.isTrue(style,
						mxConstants.STYLE_HORIZONTAL, true);
				double start = mxUtils.getDouble(style,
						mxConstants.STYLE_STARTSIZE,
						mxConstants.DEFAULT_STARTSIZE)
						* scale;

				if (horizontal)
				{
					width += vertexBounds.getWidth();
					height += start;
				}
				else
				{
					width += start;
					height += vertexBounds.getHeight();
				}
			}
			else
			{
				width += vertexBounds.getWidth();
				height += vertexBounds.getHeight();
			}
		}
//style.put(MXConstants.LABEL_POSITION_X, x-centerx);
//System.out.println(x-centerx);
//style.put(MXConstants.LABEL_POSITION_Y, y-centery);
//System.out.println(y-centery);
		return Utils.getScaledLabelBounds(x, y, size, width, height, style,
				scale);
	}

	
	
/**
 * Returns the bounds for a label for the given location and size, taking
 * into account the alignment and spacing in the specified style, as well as
 * the width and height of the rectangle that contains the label. (For edge
 * labels this width and height is 0.) The scale is used to scale the given
 * size and the spacings in the specified style.
 */
public static mxRectangle getScaledLabelBounds(double x, double y,
		mxRectangle size, double outerWidth, double outerHeight,
		Map<String, Object> style, double scale)
{
	double inset = mxConstants.LABEL_INSET * scale;

	// Scales the size of the label
	// FIXME: Correct rounded font size and not-rounded scale
	double width = size.getWidth() * scale + 2 * inset;
	double height = size.getHeight() * scale + 2 * inset;

//	// Gets the global spacing and orientation
//	boolean horizontal = isTrue(style, mxConstants.STYLE_HORIZONTAL, true);
//	int spacing = (int) (getInt(style, mxConstants.STYLE_SPACING) * scale);

	// Gets the alignment settings
	Object align = getString(style, mxConstants.STYLE_ALIGN,
			mxConstants.ALIGN_CENTER);
	Object valign = getString(style, mxConstants.STYLE_VERTICAL_ALIGN,
			mxConstants.ALIGN_MIDDLE);

//	// Gets the vertical spacing
//	int top = (int) (getInt(style, mxConstants.STYLE_SPACING_TOP) * scale);
//	int bottom = (int) (getInt(style, mxConstants.STYLE_SPACING_BOTTOM) * scale);
//
//	// Gets the horizontal spacing
//	int left = (int) (getInt(style, mxConstants.STYLE_SPACING_LEFT) * scale);
//	int right = (int) (getInt(style, mxConstants.STYLE_SPACING_RIGHT) * scale);

//	// Applies the orientation to the spacings and dimension
//	if (!horizontal)
//	{
//		int tmp = top;
//		top = right;
//		right = bottom;
//		bottom = left;
//		left = tmp;
//
//		double tmp2 = width;
//		width = height;
//		height = tmp2;
//	}
	
    String degree = (String) style.get(MXConstants.TEXT_ROTATION_DEGREE);
    if (degree != null) {
       if (degree.equals("90") || degree.equals("270")) {
   		double tmp2 = width;
   		width = height;
   		height = tmp2;


//       if (degree.equals("180")) {
//    
//       } else if (degree.equals("360")) {
//         
//       }
   }
    }
//	// Computes the position of the label for the horizontal alignment
//	if ((horizontal && align.equals(mxConstants.ALIGN_CENTER))
//			|| (!horizontal && valign.equals(mxConstants.ALIGN_MIDDLE)))
//	{
//		x += (outerWidth - width) / 2;
//	}
//	else if ((horizontal && align.equals(mxConstants.ALIGN_RIGHT))
//			|| (!horizontal && valign.equals(mxConstants.ALIGN_BOTTOM)))
//	{
//		x += outerWidth - width - spacing - right;
//	}
//	else
//	{
//		x += spacing + left;
//	}

//	// Computes the position of the label for the vertical alignment
//	if ((!horizontal && align.equals(mxConstants.ALIGN_CENTER))
//			|| (horizontal && valign.equals(mxConstants.ALIGN_MIDDLE)))
//	{
//		y += (outerHeight - height) / 2;
//	}
//	else if ((!horizontal && align.equals(mxConstants.ALIGN_LEFT))
//			|| (horizontal && valign.equals(mxConstants.ALIGN_BOTTOM)))
//	{
//		y += outerHeight - height - spacing - bottom;
//	}
//	else
//	{
//		y += spacing + top;
//	}
//		int spacing = (int) (getInt(style, MXConstants.LABEL_POSITION_X) * scale);
		int labelPositionX = (int) (getInt(style, MXConstants.LABEL_POSITION_X) * scale);
		int labelPositionY = (int) (getInt(style, MXConstants.LABEL_POSITION_Y) * scale);
//		labelPositionX = 20;
//		labelPositionY = 50;
		
	return new mxRectangle(x + labelPositionX, y+labelPositionY, width, height);
}

public static Object createFillPaint(mxRectangle bounds, Map<String, Object> style) {
	Color fillColor = mxUtils.getColor(style, mxConstants.STYLE_FILLCOLOR);
	
	Paint fillPaint = null;

	if (fillColor != null) {
		Color gradientColor = mxUtils.getColor(style, mxConstants.STYLE_GRADIENTCOLOR);

		if (gradientColor != null) {
			GradientRotation gradientRotation = GradientRotation.getGradientRotation(mxUtils.getString(style, MXConstants.GRADIENT_ROTATION));

			float x1 = (float) bounds.getX();
			float y1 = (float) bounds.getY();
			float x2 = (float) bounds.getX();
			float y2 = (float) bounds.getY();

			switch (gradientRotation) {
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

			fillPaint = new GradientPaint(x1, y1, fillColor, x2, y2, gradientColor, true);
		}
	}

	return fillPaint;
}




//public static Object createFillPaint(mxRectangle bounds, Fill fill) {
//	System.out.println(fill.getColor());
//	System.out.println(fill.getGradientColor());
//	System.out.println(fill.getGradientRotation());
//	System.out.println("#####");
//
//		Color fillColor = parseColor(fill.getColor());
//		
//		Paint fillPaint = null;
//
//		if (fillColor != null) {
//			Color gradientColor = parseColor(fill.getGradientColor());
//			if (gradientColor != null && fill.getGradientRotation() != null) {
//				System.out.println(fill.getGradientRotation());
//
//				float x1 = (float) bounds.getX();
//				float y1 = (float) bounds.getY();
//				float x2 = (float) bounds.getX();
//				float y2 = (float) bounds.getY();
//
//				switch (fill.getGradientRotation()) {
//				case DIAGONAL:
//					y2 = (float) (bounds.getY() + bounds.getHeight());
//					x2 = (float) (bounds.getX() + bounds.getWidth());
//					break;
//				case HORIZONTAL:
//					x2 = (float) (bounds.getX() + bounds.getWidth());
//					break;
//				case VERTICAL:
//					y2 = (float) (bounds.getY() + bounds.getHeight());
//					break;
//				default:
//					break;
//
//				}
//
//				fillPaint = new GradientPaint(x1, y1, fillColor, x2, y2, gradientColor, true);
//			}
//		}
//
//		return fillPaint;
//	}

public static Stroke createStroke(Map<String, Object> style, double scale) {
	String lineStyleString = mxUtils.getString(style, MXConstants.LINE_STYLE);
	double width = mxUtils.getFloat(style, mxConstants.STYLE_STROKEWIDTH, 1) * scale;
	return getStrokeForLineStyle(style, scale, lineStyleString, width);
}

public static Stroke createLabelStroke(Map<String, Object> style, double scale) {
	String lineStyleString = mxUtils.getString(style, MXConstants.LABEL_LINE_STYLE);
	double width = mxUtils.getFloat(style, MXConstants.LABEL_LINE_WIDTH, 1) * scale;
	return getStrokeForLineStyle(style, scale, lineStyleString, width);
	}



/**
 * @param style
 * @param scale
 * @param lineStyleString
 * @param width
 * @return
 */
protected static Stroke getStrokeForLineStyle(Map<String, Object> style, double scale, String lineStyleString, double width) {
	Style linestyle;
	if(lineStyleString!=null)
	linestyle = Line.Style.getStyle(lineStyleString);
	else linestyle = Line.Style.SOLID;

	float f;
	switch (linestyle) {
	case DASH:
		float[] dashPattern = mxUtils.getFloatArray(style, mxConstants.STYLE_DASH_PATTERN, mxConstants.DEFAULT_DASHED_PATTERN, " ");
		float[] scaledDashPattern = new float[dashPattern.length];
		f = (width>0)?(float) width:1;
		for (int i = 0; i < dashPattern.length; i++) {
			scaledDashPattern[i] = (float) (dashPattern[i] * scale * f);
		}
		return new BasicStroke((float) width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10.0f, scaledDashPattern, 0.0f);
	case DOT:
		f = (width>0)?(float) width:1;
		float[] dash = { 0.0f, f * 2 };
		return new BasicStroke((float) width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 1.0f, dash, 10.0f);
	case SOLID:
		return new BasicStroke((float) width);
	}
	return null;
}


public static AbstractObjectGraphics getPNGraphics(PNGraph graph, PNGraphCell cell) {
	switch (cell.getType()) {
	case PLACE:
		if(graph.isLabelSelected())
		return graph.getNetContainer().getPetriNetGraphics().getPlaceLabelAnnotationGraphics().get(cell.getId());
		else return graph.getNetContainer().getPetriNetGraphics().getPlaceGraphics().get(cell.getId());
	case TRANSITION:
		if(graph.isLabelSelected())
		return graph.getNetContainer().getPetriNetGraphics().getTransitionLabelAnnotationGraphics().get(cell.getId());
		else
		return graph.getNetContainer().getPetriNetGraphics().getTransitionGraphics().get(cell.getId());
	case ARC:
		if(graph.isLabelSelected())
		return graph.getNetContainer().getPetriNetGraphics().getArcAnnotationGraphics().get(cell.getId());
		else
		return graph.getNetContainer().getPetriNetGraphics().getArcGraphics().get(cell.getId());
		
	}
	return null;
}



public static void updateGraphics(PNGraph graph, PNGraphCell cell, String key, Object value, boolean isLabel) throws ParameterException {
//	graphics = graph.getNetContainer().getPetriNetGraphics().getPlaceGraphics();
	AbstractObjectGraphics graphics = getPNGraphics(graph,cell, isLabel);
	if(graphics instanceof NodeGraphics)
		updateNodeGraphics((NodeGraphics) graphics,key,value);
	if(graphics instanceof ArcGraphics)
		updateArcGraphics((ArcGraphics) graphics,key,value);
	if(graphics instanceof AnnotationGraphics)
		updateAnnotationGraphics((AnnotationGraphics) graphics,key,value);

		
//	if(!graph.isLabelSelected()){
//	switch(cell.getType()){
//	case ARC:
//		ArcGraphics arcGraphics = graph.getNetContainer().getPetriNetGraphics().getArcGraphics().get(cell.getId());
//		break;
//	case PLACE:
//		NodeGraphics placeGraphics = graph.getNetContainer().getPetriNetGraphics().getPlaceGraphics().get(cell.getId());
//		updateNodeGraphics(placeGraphics,key,value);
//		break;
//	case TRANSITION:
//		NodeGraphics transitionGraphics = graph.getNetContainer().getPetriNetGraphics().getTransitionGraphics().get(cell.getId());
//		break;
//	
	}



private static AbstractObjectGraphics getPNGraphics(PNGraph graph, PNGraphCell cell, boolean isLabel) {
		switch (cell.getType()) {
		case PLACE:
			if(isLabel)
			return graph.getNetContainer().getPetriNetGraphics().getPlaceLabelAnnotationGraphics().get(cell.getId());
			else return graph.getNetContainer().getPetriNetGraphics().getPlaceGraphics().get(cell.getId());
		case TRANSITION:
			if(isLabel)
			return graph.getNetContainer().getPetriNetGraphics().getTransitionLabelAnnotationGraphics().get(cell.getId());
			else
			return graph.getNetContainer().getPetriNetGraphics().getTransitionGraphics().get(cell.getId());
		case ARC:
			if(isLabel)
			return graph.getNetContainer().getPetriNetGraphics().getArcAnnotationGraphics().get(cell.getId());
			else
			return graph.getNetContainer().getPetriNetGraphics().getArcGraphics().get(cell.getId());
			
		}
		return null;
	}




private static void updateAnnotationGraphics(AnnotationGraphics graphics, String key, Object value) throws ParameterException {
	if(key.equals(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR)){
		graphics.getFill().setColor((String) value);
		}


	if(key.equals(mxConstants.STYLE_LABEL_BORDERCOLOR)){
		graphics.getLine().setColor((String) value);
		}
	if(key.equals(MXConstants.LABEL_LINE_WIDTH)){
		graphics.getLine().setColor((String) value);
		}
	
}



private static void updateArcGraphics(ArcGraphics graphics, String key, Object value) {
	// TODO Auto-generated method stub
	
}



private static void updateNodeGraphics(NodeGraphics graphics, String key, Object value) throws ParameterException {
		if (key.equals(mxConstants.STYLE_FILLCOLOR)) {
			graphics.getFill().setColor((String) value);
		}
		if (key.equals(mxConstants.STYLE_GRADIENTCOLOR)) {
			graphics.getFill().setGradientColor((String) value);
		}
		if (key.equals(mxConstants.STYLE_GRADIENT_DIRECTION)) {
			graphics.getFill().setGradientRotation(GradientRotation.getGradientRotation((String) value));
		}
		if (key.equals(mxConstants.STYLE_IMAGE)) {
			graphics.getFill().setImage((URI) value);
		}
		
		if (key.equals(mxConstants.STYLE_STROKEWIDTH)) {
			graphics.getLine().setWidth((Double) value);
		}
		if (key.equals(mxConstants.STYLE_STROKECOLOR)) {
			graphics.getLine().setColor((String) value);
		}
		if (key.equals(MXConstants.LINE_STYLE)) {
			graphics.getLine().setStyle(Line.Style.getStyle((String) value));
		}
		if (key.equals(mxConstants.STYLE_ROUNDED)) {
			if(key.equals("true"))
			graphics.getLine().setShape(Line.Shape.CURVE);
			if(key.equals("false"))
				graphics.getLine().setShape(Line.Shape.LINE);

		}


}










	

}
