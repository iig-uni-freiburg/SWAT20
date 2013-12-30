package de.uni.freiburg.iig.telematik.swat.editor.graph;

import java.util.Map;

import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxUtils;

public class Utils extends mxUtils {
	/**
	 * Returns the paint bounds for the given label.
	 */
	public static mxRectangle getLabelPaintBounds(String label,
			Map<String, Object> style, boolean isHtml, mxPoint offset,
			mxRectangle vertexBounds, double scale)
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
			x += vertexBounds.getX();
			y += vertexBounds.getY();

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

	// Gets the global spacing and orientation
	boolean horizontal = isTrue(style, mxConstants.STYLE_HORIZONTAL, true);
	int spacing = (int) (getInt(style, mxConstants.STYLE_SPACING) * scale);

	// Gets the alignment settings
	Object align = getString(style, mxConstants.STYLE_ALIGN,
			mxConstants.ALIGN_CENTER);
	Object valign = getString(style, mxConstants.STYLE_VERTICAL_ALIGN,
			mxConstants.ALIGN_MIDDLE);

	// Gets the vertical spacing
	int top = (int) (getInt(style, mxConstants.STYLE_SPACING_TOP) * scale);
	int bottom = (int) (getInt(style, mxConstants.STYLE_SPACING_BOTTOM) * scale);

	// Gets the horizontal spacing
	int left = (int) (getInt(style, mxConstants.STYLE_SPACING_LEFT) * scale);
	int right = (int) (getInt(style, mxConstants.STYLE_SPACING_RIGHT) * scale);

	// Applies the orientation to the spacings and dimension
	if (!horizontal)
	{
		int tmp = top;
		top = right;
		right = bottom;
		bottom = left;
		left = tmp;

		double tmp2 = width;
		width = height;
		height = tmp2;
	}
	
    String degree = (String) style.get(MXConstants.TEXT_ROTATION_DEGREE);
    if (degree != null) {
       if (degree.equals("90")) {
          
       } else if (degree.equals("270")) {
        
       }

       if (degree.equals("180")) {
    
       } else if (degree.equals("360")) {
         
       }
   }

	// Computes the position of the label for the horizontal alignment
	if ((horizontal && align.equals(mxConstants.ALIGN_CENTER))
			|| (!horizontal && valign.equals(mxConstants.ALIGN_MIDDLE)))
	{
		x += (outerWidth - width) / 2 + left - right;
	}
	else if ((horizontal && align.equals(mxConstants.ALIGN_RIGHT))
			|| (!horizontal && valign.equals(mxConstants.ALIGN_BOTTOM)))
	{
		x += outerWidth - width - spacing - right;
	}
	else
	{
		x += spacing + left;
	}

	// Computes the position of the label for the vertical alignment
	if ((!horizontal && align.equals(mxConstants.ALIGN_CENTER))
			|| (horizontal && valign.equals(mxConstants.ALIGN_MIDDLE)))
	{
		y += (outerHeight - height) / 2 + top - bottom;
	}
	else if ((!horizontal && align.equals(mxConstants.ALIGN_LEFT))
			|| (horizontal && valign.equals(mxConstants.ALIGN_BOTTOM)))
	{
		y += outerHeight - height - spacing - bottom;
	}
	else
	{
		y += spacing + top;
	}

	return new mxRectangle(x, y, width, height);
}

}
