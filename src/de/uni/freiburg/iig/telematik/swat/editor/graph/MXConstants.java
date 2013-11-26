package de.uni.freiburg.iig.telematik.swat.editor.graph;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.mxgraph.swing.util.mxSwingConstants;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxCellState;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AnnotationGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.ArcGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Dimension;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill.GradientRotation;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Font;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Font.Align;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Font.Decoration;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Offset;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Position;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperties.PNComponent;

public abstract class MXConstants {

	public static final String CONTAINER = "container";
	public static final String PLACE = "place";
	public static final String TRANSITION = "transition";
	public static final String CONFIG_FILE = "PetriNet.cfg";
	public static final String ROOT_COLOR = "#aaaaaa";
	public static final String ROOT_FONT_COLOR = "#ffffff";
	public static final String TERMINAL_COLOR = "#ffaaaa";
	public static final String IMMEDIATE_COLOR = "#dddddd";
	public static final String SIMULATION_START = "Start";
	public static final String SIMULATION_END = "End";
	public static final String SIMULATION_PLAN = "Plan";
	public static final String SIMULATION_EXEC = "Execute";

	public static final Color bluelow = new Color(107, 134, 167);
	public static final Color bluemid = new Color(214, 227, 242);
	public static final Color bluehigh = new Color(182, 202, 228);
	public static final Color blueBG = new Color(234, 243, 252);

	public static BufferedImage EMPTY_IMAGE;
	static {
		try {
			MXConstants.EMPTY_IMAGE = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		} catch (Exception e) {
			// Occurs when running on GAE, BufferedImage is a
			// blacklisted class
			MXConstants.EMPTY_IMAGE = null;
		}
	}

	public static final String DEFAULT_PLACE_SHAPE = "shape=ellipse;" + "fillColor=" + Integer.toHexString(MXConstants.bluehigh.getRGB()) + ";" + "strokeWidth=2.0;" + "strokeColor="
			+ Integer.toHexString(MXConstants.bluelow.getRGB()) + ";" + "labelBackgroundColor=" + Integer.toHexString(MXConstants.blueBG.getRGB()) + ";";
	public static final String DEFAULT_TRANSITION_SHAPE = "shape=rectangle;" + "fillColor=" + Integer.toHexString(MXConstants.bluehigh.getRGB()) + ";" + "strokeWidth=2.0;" + "strokeColor="
			+ Integer.toHexString(MXConstants.bluelow.getRGB()) + ";" + "labelBackgroundColor=" + Integer.toHexString(MXConstants.blueBG.getRGB()) + ";";
	public static final String DEFAULT_ARC_SHAPE = "";
	//
	public static final String PlaceNamePrefix = "p";
	public static final String TransitionNamePrefix = "t";
	public static final String PNLabelStyle = "shape=none;fontSize=12;fontColor=" + Integer.toHexString(MXConstants.bluelow.getRGB()) + ";";
	public static final String SHAPE_CIRCLE = "circle";
	public static final Color SHADOW_COLOR = Color.gray;
	public static final Color DEFAULT_VALID_COLOR = MXConstants.bluehigh;
	public static final Color DEFAULT_INVALID_COLOR = Color.RED;
	public static final Color RUBBERBAND_BORDERCOLOR = new Color(51, 153, 255);
	public static final Color RUBBERBAND_FILLCOLOR = new Color(51, 153, 255, 80);
	public static final Color HANDLE_BORDERCOLOR = Color.black;
	public static final Color HANDLE_FILLCOLOR = MXConstants.bluelow;
	public static final Color LABEL_HANDLE_FILLCOLOR = Color.orange;
	public static final Color LOCKED_HANDLE_FILLCOLOR = MXConstants.bluelow;
	public static final Color CONNECT_HANDLE_FILLCOLOR = MXConstants.bluelow;
	public static final Color EDGE_SELECTION_COLOR = MXConstants.bluelow;
	public static final Color VERTEX_SELECTION_COLOR = MXConstants.bluelow;

	public static String getStyle(PNComponent type, NodeGraphics nodeGraphics, AnnotationGraphics annotationGraphics) {
		Hashtable<String, Object> style = new Hashtable<String, Object>() {
		};
		switch (type) {
		case PLACE:
			style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
			break;
		case TRANSITION:
			style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
			break;

		}
		if (annotationGraphics != null) {
			style.put(mxConstants.STYLE_SPACING_LEFT, Double.toString(annotationGraphics.getOffset().getX()));
			style.put(mxConstants.STYLE_SPACING_TOP, Double.toString(annotationGraphics.getOffset().getY()));
			if(annotationGraphics.getFill() != null)
			style.put(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, annotationGraphics.getFill());
			
			if(annotationGraphics.getFont() != null){
				Font font = annotationGraphics.getFont();
				style.put(mxConstants.STYLE_ALIGN, font.getAlign());
				if(font.getFamily() != null)
				style.put(mxConstants.STYLE_FONTFAMILY, font.getFamily());
				if(font.getSize() != null) //TODO: Not working, except integer
				style.put(mxConstants.STYLE_FONTSIZE, checkCSS(font.getSize()));
				int fontStyle = 0;
				if (font.getWeight() != null && font.getWeight().equals("bold"))
					fontStyle += mxConstants.FONT_BOLD;
				if (font.getStyle() != null && font.getStyle().equals("italic"))
					fontStyle += mxConstants.FONT_ITALIC;
				if (font.getDecoration() != null && font.getDecoration().equals(Decoration.UNDERLINE))
					fontStyle += mxConstants.FONT_UNDERLINE;
				style.put(mxConstants.STYLE_FONTSTYLE, fontStyle);	
					
				
			}
			
			if(annotationGraphics.getLine() != null){
			
				Line line = annotationGraphics.getLine();
				if(line.getColor() != null)
					style.put(mxConstants.STYLE_LABEL_BORDERCOLOR, line.getColor());

			
		}
			
		}
		style.put(mxConstants.STYLE_FILLCOLOR, Integer.toHexString(MXConstants.bluehigh.getRGB()));
		style.put(mxConstants.STYLE_STROKEWIDTH, 2.0);
		style.put(mxConstants.STYLE_STROKECOLOR, Integer.toHexString(MXConstants.bluelow.getRGB()));
		style.put(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, Integer.toHexString(MXConstants.blueBG.getRGB()));
		String convertedStyle = style.toString().replaceAll(", ", ";");
		String shortendStyle = convertedStyle.substring(1, convertedStyle.length() - 1);
		System.out.println(shortendStyle);
		return shortendStyle;
	}

	private static Object checkCSS(String size) {
		if(size.equals("medium"))
			return mxConstants.DEFAULT_FONTSIZE;
//		if(size.equals("small"))
//			return mxConstants.DEFAULT_FONTSIZE;...
		//What else cases with medium

		return size;
	}

	public static String getStyle(ArcGraphics arcGraphics, AnnotationGraphics annotationGraphics) {
		// TODO: Only line attributes!
		// if(arcGraphics == null){
		if (true) {
			return null;
		}
		return null;
	}

	public static NodeGraphics getNodeGraphics(mxCellState state) throws ParameterException {
		Position position = new Position(state.getX(), state.getY());
		Dimension dimension = new Dimension(state.getWidth(), state.getHeight());
		Fill fill = new Fill();
		String fillColor = (String) state.getStyle().get(mxConstants.STYLE_FILLCOLOR);
		if (fillColor != null) {
			fill.setColor(fillColor);
		}
		String gradientColor = (String) state.getStyle().get(mxConstants.STYLE_GRADIENTCOLOR);
		if (gradientColor != null) {
			fill.setGradientColor(gradientColor);
		}
		GradientRotation gradientRotation = null;
		String gradientDirecion = (String) state.getStyle().get(mxConstants.STYLE_GRADIENT_DIRECTION);
		if (gradientDirecion != null) {
			if (gradientDirecion.equals(mxConstants.DIRECTION_EAST) || gradientDirecion.equals(mxConstants.DIRECTION_WEST)) {
				gradientRotation = GradientRotation.HORIZONTAL;
			} else if (gradientDirecion.equals(mxConstants.DIRECTION_SOUTH) || gradientDirecion.equals(mxConstants.DIRECTION_NORTH)) {
				gradientRotation = GradientRotation.VERTICAL;
			}
		}
		if (gradientRotation != null) {
			fill.setGradientRotation(gradientRotation);
		}
		String image = (String) state.getStyle().get(mxConstants.STYLE_IMAGE);
		if (image != null) {
			try {
				fill.setImage(new URI(image));
			} catch (URISyntaxException e) {
				throw new ParameterException(e.getMessage());
			}
		}

		return new NodeGraphics(position, dimension, fill, getLine(state));
	}

	/**
	 * @param state
	 * @param n
	 * @param annotation
	 * @return
	 * @throws ParameterException
	 */
	public static AnnotationGraphics getAnnotationGraphics(mxCellState state) throws ParameterException {
System.out.println(((PNGraphCell) state.getCell()).getGeometry().getOffset());
mxPoint offset = new mxPoint();	
if(((PNGraphCell) state.getCell()).getGeometry().getOffset() != null)
		offset = state.getAbsoluteOffset();
else {offset.setX(Double.parseDouble((String) state.getStyle().get(mxConstants.STYLE_SPACING_LEFT)));
offset.setY(Double.parseDouble((String) state.getStyle().get(mxConstants.STYLE_SPACING_TOP)));
}
		AnnotationGraphics annotation = null;
		
		Fill fill = new Fill();
		String fillColor = (String) state.getStyle().get(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR);
		if (fillColor != null) {
			fill.setColor(fillColor);
		}
		
		
		Font font = new Font();
		String alignStr = (String) state.getStyle().get(mxConstants.STYLE_ALIGN);
		Align align = Align.getAlign(alignStr);
		font.setAlign(align );
		String family = (String) state.getStyle().get(mxConstants.STYLE_FONTFAMILY);
		font.setFamily(family);
		String size = (String) state.getStyle().get(mxConstants.STYLE_FONTSIZE);;
		font.setSize(size);
//		String color = (String) state.getStyle().get(mxConstants.STYLE_FONTCOLOR);;
//		font.setColor(size); TODO: Where should font color be set in pnml standard?
		if(state.getStyle().containsKey(mxConstants.STYLE_FONTSTYLE)){
		int style = new Integer((String) state.getStyle().get(mxConstants.STYLE_FONTSTYLE));
		if(style == mxConstants.FONT_BOLD){
			font.setWeight("bold");
			font.setStyle("normal");}
		if(style == mxConstants.FONT_ITALIC){
			font.setWeight("normal");
			font.setStyle("italic");
			}
		if(style ==  mxConstants.FONT_UNDERLINE){
			font.setDecoration(Decoration.UNDERLINE);
			font.setWeight("normal");
			font.setStyle("normal");
			}
		if(style ==  mxConstants.FONT_BOLD +  mxConstants.FONT_ITALIC){
			font.setWeight("bold");
			font.setStyle("italic");
			}
		if(style ==  mxConstants.FONT_UNDERLINE + mxConstants.FONT_BOLD){
			font.setDecoration(Decoration.UNDERLINE);
			font.setWeight("bold");
			font.setStyle("normal");
			}
		
		if(style ==  mxConstants.FONT_UNDERLINE +  mxConstants.FONT_ITALIC){
			font.setDecoration(Decoration.UNDERLINE);
			font.setWeight("normal");
			font.setStyle("italic");
			}
		}
		//decoration missing: overline and Line-through

			//Line at the moment expresses same like line in NodeGraphics
		annotation = new AnnotationGraphics(new Offset((int) offset.getX(), (int) offset.getY()), fill, getLineAnnotation(state), font);

		return annotation;
	}

	private static Line getLine(mxCellState state) throws ParameterException {
		Line line = new Line();
		line.setShape(Line.Shape.LINE);
		String strokeStyle = (String) state.getStyle().get(mxConstants.STYLE_DASHED);
		boolean dashed = new Boolean(strokeStyle);
		if (dashed) {
			line.setStyle(Line.Style.DASH);
		} else {
			line.setStyle(Line.Style.SOLID);
		}
		String lineWidth = (String) state.getStyle().get(mxConstants.STYLE_STROKEWIDTH);
		// Validate.positiveInteger(lineWidth);
		line.setWidth(Double.parseDouble(lineWidth));
		return line;
	}
	
	//What means Line in Annotationgraphics?
	
	private static Line getLineAnnotation(mxCellState state) throws ParameterException {
		Line line = new Line();
		line.setShape(Line.Shape.LINE);
		String color;
		if(state.getStyle().containsKey(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR)){
			color = (String) state.getStyle().get(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR);
		line.setColor(color);
		line.setStyle(Line.Style.SOLID);
		String lineWidth = "1";
		line.setWidth(Double.parseDouble(lineWidth));
		return line;}
		return null;
		
	}

	public static ArcGraphics getArcGraphics(mxCellState state) throws ParameterException {
		List<mxPoint> points = ((PNGraphCell) state.getCell()).getGeometry().getPoints();
		Vector<Position> positions = new Vector<Position>();
		for (mxPoint point : points) {
			positions.add(new Position(point.getX(), point.getY()));
		}
		return new ArcGraphics(positions, getLine(state));
	}
}
