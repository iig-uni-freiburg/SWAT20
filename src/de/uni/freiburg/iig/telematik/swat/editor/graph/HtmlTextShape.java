package de.uni.freiburg.iig.telematik.swat.editor.graph;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Map;

import javax.swing.CellRendererPane;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.shape.mxHtmlTextShape;
import com.mxgraph.shape.mxITextShape;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxLightweightLabel;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;

public class HtmlTextShape extends mxHtmlTextShape {
@Override
/**
 * 
 */
public void paintShape(mxGraphics2DCanvas canvas, String text,
		mxCellState state, Map<String, Object> style)
{
	mxLightweightLabel textRenderer = mxLightweightLabel
			.getSharedInstance();
	CellRendererPane rendererPane = canvas.getRendererPane();
	Rectangle rect = state.getLabelBounds().getRectangle();
	Graphics2D g = canvas.getGraphics();

	if (textRenderer != null
			&& rendererPane != null
			&& (g.getClipBounds() == null || g.getClipBounds().intersects(
					rect)))
	{
		double scale = canvas.getScale();
		int x = rect.x;
		int y = rect.y;
		int w = rect.width;
		int h = rect.height;

		boolean horizontal = mxUtils.isTrue(style,
				mxConstants.STYLE_HORIZONTAL, true);
		String degree;

		     degree = (String) style.get(MXConstants.TEXT_ROTATION_DEGREE);
		     if (degree != null) {
		        if (degree.equals("90")) {
		            g.rotate(Math.PI / 2, x + w / 2, y + h / 2);
		            g.translate(w / 2 - h / 2, h / 2 - w / 2);
		        } else if (degree.equals("270")) {
		            g.rotate(-Math.PI / 2, x + w / 2, y + h / 2);
		            g.translate(w / 2 - h / 2, h / 2 - w / 2);
		        }

		        if (degree.equals("180")) {
		            g.rotate(Math.PI, x + w / 2, y + h / 2);
		        } else if (degree.equals("360")) {
		            g.rotate(Math.PI * 2, x + w / 2, y + h / 2);
		        }
		    }
		 

		// Replaces the linefeeds with BR tags
		if (isReplaceHtmlLinefeeds())
		{
			text = text.replaceAll("\n", "<br>");
		}

		// Renders the scaled text
		textRenderer.setText(createHtmlDocument(style, text));
		textRenderer.setFont(mxUtils.getFont(style, canvas.getScale()));
		g.scale(scale, scale);
		rendererPane.paintComponent(g, textRenderer, rendererPane,
				(int) (x / scale) + mxConstants.LABEL_INSET,
				(int) (y / scale) + mxConstants.LABEL_INSET,
				(int) (w / scale), (int) (h / scale), true);
	}
}

}
