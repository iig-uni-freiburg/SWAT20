package de.uni.freiburg.iig.telematik.swat.editor.graph;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;

import de.invation.code.toval.validate.ParameterException;
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
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line.Shape;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line.Style;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Offset;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Position;
import de.uni.freiburg.iig.telematik.swat.editor.menu.EditorProperties;
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

	public static final String PlaceNamePrefix = "p";
	public static final String TransitionNamePrefix = "t";

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
	public static final String TEXT_ROTATION_DEGREE ="180";
	public static final String LABEL_POSITION_X ="labelposotionx";
	public static final String LABEL_POSITION_Y = "labelpositiony";
	private static final String DEFAULT_STOKEWIDTH = "1";
	public static final String GRADIENT_ROTATION = "Gradient_Rotation";
	public static final String LABEL_LINE_STYLE = "Label_Line_Style";
	public static final String LABEL_LINE_WIDTH = "labelStrokeWidth";
	public static final String LINE_STYLE = "Line_Style";
	public static final String STYLE_LABEL_GRADIENTCOLOR = "labelGradientColor";
	public static final String STYLE_LABEL_GRADIENT_DIRECTION = "labelGradientDirection";
	public static final String STYLE_LABEL_IMAGE = "labelImage";
	public static final String LABEL_LINE_SHAPE = "labelLineShape";
	public static final String FONT_DECORATION = "labelFontDecoration";
	public static final String FONT_STYLE = "labelFontStyle";
	public static final String FONT_WEIGHT = "labelFontWeight";
	private static final String STYLE_GRADIENT_ROTATION = "gradientRotation";
	private static final String LINE_SHAPE = "lineShape";

	
	
	public static String getNodeStyle(PNComponent type, NodeGraphics initialNodeGraphics, AnnotationGraphics annotationGraphics) throws ParameterException {
		Hashtable<String, Object> style = new Hashtable<String, Object>();

		style.put(MXConstants.LABEL_POSITION_X, EditorProperties.getInstance().getDefaultHorizontalLabelOffset());
		style.put(MXConstants.LABEL_POSITION_Y, EditorProperties.getInstance().getDefaultVerticalLabelOffset());
		switch (type) {
		case PLACE:
			style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
			break;
		case TRANSITION:
			style.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
			break;
		case ARC:
			style.put(MXConstants.LABEL_POSITION_X, 0);
			style.put(MXConstants.LABEL_POSITION_Y, 0);
			break;
		default:
			break;

		}
//		style.put(mxConstants.STYLE_FILLCOLOR, mxUtils.hexString(MXConstants.bluehigh));
//		style.put(mxConstants.STYLE_STROKEWIDTH, 2.0);
//		style.put(mxConstants.STYLE_STROKECOLOR, mxUtils.hexString(MXConstants.bluelow));
//		style.put(mxConstants.STYLE_FONTCOLOR, "#000000");
		NodeGraphics nodeGraphics = (initialNodeGraphics != null)? initialNodeGraphics:new NodeGraphics();
		
		Fill fill = (nodeGraphics.getFill() != null)? nodeGraphics.getFill(): new Fill();
				fill = nodeGraphics.getFill();
			
				String fillColor = (fill.getColor() != null)? fill.getColor():EditorProperties.getInstance().getDefaultNodeColor();
				style.put(mxConstants.STYLE_FILLCOLOR,fillColor);
	
				GradientRotation gradientRotation = (fill.getGradientRotation() != null)? fill.getGradientRotation():EditorProperties.getInstance().getDefaultGradientDirection();
				if(gradientRotation != null) style.put(MXConstants.STYLE_GRADIENT_ROTATION, gradientRotation);
				fill.setGradientRotation(gradientRotation);
				
				String gradientColor = (fill.getGradientColor() != null)? fill.getGradientColor(): EditorProperties.getInstance().getDefaultGradientColor();
				if(gradientColor != null)style.put(mxConstants.STYLE_GRADIENTCOLOR, gradientColor);
				fill.setGradientColor(gradientColor);
				
				URI image = (fill.getImage() != null)? fill.getImage():EditorProperties.getInstance().getDefaultNodeImage();
				if( image != null) style.put(mxConstants.STYLE_IMAGE, image);
				fill.setImage(image);
			nodeGraphics.setFill(new Fill(fillColor, gradientColor, gradientRotation, image));
		
		Line line = (nodeGraphics.getLine() != null)? nodeGraphics.getLine():new Line();
		
				String lineColor = (line.getColor() != null)? line.getColor():EditorProperties.getInstance().getDefaultLineColor();
				style.put(mxConstants.STYLE_STROKECOLOR,lineColor);
	
				Style lineStyle = (line.getStyle() != null)? line.getStyle(): Line.Style.SOLID;
				style.put(MXConstants.LINE_STYLE, lineStyle);
				
				Shape lineShape = (line.getShape() != null)? line.getShape():Shape.LINE;
				style.put(MXConstants.LINE_SHAPE, lineShape);	
				
				style.put(mxConstants.STYLE_STROKEWIDTH, Double.toString(line.getWidth()));
				
			nodeGraphics.setLine(new Line(lineColor, lineShape, lineStyle, line.getWidth()));

	
	

	getAnnotationGraphics(annotationGraphics, style);


		// Maybe there is a mxUtil Method for this TODO:CHECK IF
		String convertedStyle = style.toString().replaceAll(", ", ";");
		String shortendStyle = convertedStyle.substring(1, convertedStyle.length() - 1);

		return shortendStyle;
	}

	
	
	public static String getArcStyle(ArcGraphics arcGraphics, AnnotationGraphics annotationGraphics) throws ParameterException {
		Hashtable<String, Object> style = new Hashtable<String, Object>();
		
		Line line = (arcGraphics.getLine() != null)? arcGraphics.getLine():new Line();
			
		String lineColor = (line.getColor() != null)? line.getColor():EditorProperties.getInstance().getDefaultLineColor();
		style.put(mxConstants.STYLE_STROKECOLOR,lineColor);

		Style lineStyle = (line.getStyle() != null)? line.getStyle(): Line.Style.SOLID;
		style.put(MXConstants.LINE_STYLE, lineStyle);
		
		Shape lineShape = (line.getShape() != null)? line.getShape():Shape.LINE;
		style.put(MXConstants.LINE_SHAPE, lineShape);	
		
		style.put(mxConstants.STYLE_STROKEWIDTH, Double.toString(line.getWidth()));
		
	arcGraphics.setLine(new Line(lineColor, lineShape, lineStyle, line.getWidth()));
	
		getAnnotationGraphics(annotationGraphics, style);
		// Maybe there is a mxUtil Method for this TODO:CHECK IF
		String convertedStyle = style.toString().replaceAll(", ", ";");
		String shortendStyle = convertedStyle.substring(1, convertedStyle.length() - 1);

		return shortendStyle;
	}
	
	/**
	 * @param annotationGraphics
	 * @param style
	 * @throws ParameterException 
	 */
	protected static void getAnnotationGraphics(AnnotationGraphics initialAnnotationGraphics, Hashtable<String, Object> style) throws ParameterException {
		 AnnotationGraphics annotationGraphics = (initialAnnotationGraphics!= null)? initialAnnotationGraphics:new AnnotationGraphics();
				
			Offset offset = (annotationGraphics.getOffset() != null)? annotationGraphics.getOffset():new Offset(EditorProperties.getInstance().getDefaultHorizontalLabelOffset(),EditorProperties.getInstance().getDefaultVerticalLabelOffset());
				style.put(MXConstants.LABEL_POSITION_X, Double.toString(offset.getX()));
				style.put(MXConstants.LABEL_POSITION_Y, Double.toString(offset.getY()));
			annotationGraphics.setOffset(offset);
				
			Fill fill = (annotationGraphics.getFill() != null)? annotationGraphics.getFill():new Fill();
				
				String fillColor = (fill.getColor() != null)? fill.getColor():EditorProperties.getInstance().getDefaultNodeColor();
				style.put(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR,fillColor);
	
				GradientRotation gradientRotation = (fill.getGradientRotation() != null)? fill.getGradientRotation():EditorProperties.getInstance().getDefaultGradientDirection();
				if(gradientRotation != null) style.put(MXConstants.STYLE_LABEL_GRADIENT_DIRECTION, gradientRotation);
				
				String gradientColor = (fill.getGradientColor() != null)? fill.getGradientColor(): EditorProperties.getInstance().getDefaultGradientColor();
				if(gradientColor != null)style.put(MXConstants.STYLE_LABEL_GRADIENTCOLOR, gradientColor);
				
				URI image = (fill.getImage() != null)? fill.getImage():EditorProperties.getInstance().getDefaultNodeImage();
				if(image != null)style.put(MXConstants.STYLE_LABEL_IMAGE, image);
				
			annotationGraphics.setFill(new Fill(fillColor,gradientColor,gradientRotation,image));
			

			Font font = (annotationGraphics.getFont() != null)? annotationGraphics.getFont(): new Font();
				
				Align fontAlign = (font.getAlign() != null)? font.getAlign():Align.CENTER;
				style.put(mxConstants.STYLE_ALIGN, fontAlign);
				
				String fontFamily = (font.getFamily() != null)? font.getFamily():EditorProperties.getInstance().getDefaultFontFamily();
				style.put(mxConstants.STYLE_FONTFAMILY, fontFamily);
				
				String fontSize = (String) ((font.getSize() != null)?getSizeFromCSS(font.getSize()):EditorProperties.getInstance().getDefaultFontSize());
				style.put(mxConstants.STYLE_FONTSIZE, fontSize);
				
				String fontWeight = (font.getWeight() != null)? font.getWeight(): "normal"; 
				style.put(MXConstants.FONT_WEIGHT, fontWeight);
				
				String fontStyle = (font.getStyle() != null)? font.getStyle():"normal";
				style.put(MXConstants.FONT_STYLE, fontStyle);
				
				Decoration fontDecoration = (font.getDecoration() != null)? font.getDecoration():null;
				if(fontDecoration != null) style.put(MXConstants.FONT_DECORATION, fontDecoration);
					
				style.put(MXConstants.TEXT_ROTATION_DEGREE, font.getRotation());
				
			annotationGraphics.setFont(new Font(fontAlign, fontDecoration, fontFamily, font.getRotation(), fontSize, fontStyle, fontWeight));
	
			Line line = (annotationGraphics.getLine() != null)? annotationGraphics.getLine():new Line();
				
				String lineColor = (line.getColor() != null)? line.getColor():EditorProperties.getInstance().getDefaultLineColor();
				style.put(mxConstants.STYLE_LABEL_BORDERCOLOR,lineColor);
	
				Style lineStyle = (line.getStyle() != null)? line.getStyle(): Line.Style.SOLID;
				style.put(MXConstants.LABEL_LINE_STYLE, lineStyle);
				
				Shape lineShape = (line.getShape() != null)? line.getShape():Shape.LINE;
				style.put(MXConstants.LABEL_LINE_SHAPE, lineShape);	
				
				style.put(MXConstants.LABEL_LINE_WIDTH, Double.toString(line.getWidth()));
			annotationGraphics.setLine(new Line(lineColor, lineShape, lineStyle, line.getWidth()));
		
	}
	
	public static NodeGraphics getNodeGraphics(mxCellState state) throws ParameterException {
		PNGraphCell cell = (PNGraphCell) state.getCell();
		Position position = new Position(cell.getGeometry().getX(), cell.getGeometry().getY());
		Dimension dimension = new Dimension(cell.getGeometry().getWidth(), cell.getGeometry().getHeight());
		Fill fill = new Fill();
		String fillColor = (String) state.getStyle().get(mxConstants.STYLE_FILLCOLOR);
		if (fillColor != null) {
			fill.setColor(fillColor);
		}
		String gradientColor = (String) state.getStyle().get(mxConstants.STYLE_GRADIENTCOLOR);
		if (gradientColor != null) {
			fill.setGradientColor(gradientColor);
		}
		String gradientDirection = (String) state.getStyle().get("Gradient_Rotation");
		GradientRotation gradientRotation = null;
		if (gradientDirection != null) {
			fill.setGradientRotation(GradientRotation.getGradientRotation(gradientDirection));
		}

		String image = (String) state.getStyle().get(mxConstants.STYLE_IMAGE);
		if (image != null) {
			try {
				fill.setImage(new URI(image));
			} catch (URISyntaxException e) {
				throw new ParameterException(e.getMessage());
			}
		}
		return new NodeGraphics(position, dimension, fill, new Line());
	}
	
	private static Line getLine(mxCellState state) throws ParameterException {
		Line line = new Line();
		String color = (String) state.getStyle().get(mxConstants.STYLE_STROKECOLOR);
		state.getStyle().put(mxConstants.STYLE_FONTCOLOR, "#000000");
		line.setColor(color);
		line.setShape(Line.Shape.LINE);
		if(state.getStyle().get(MXConstants.LINE_STYLE) != null)
		line.setStyle(Line.Style.getStyle((String) state.getStyle().get(MXConstants.LINE_STYLE)));

		String lineWidth;
		if (state.getStyle().get(mxConstants.STYLE_STROKEWIDTH) instanceof String) {
			lineWidth = (String) state.getStyle().get(mxConstants.STYLE_STROKEWIDTH);
		} else if(state.getStyle().get(mxConstants.STYLE_STROKEWIDTH) != null)
			lineWidth = Double.toString((Double) state.getStyle().get(mxConstants.STYLE_STROKEWIDTH));
		else lineWidth = MXConstants.DEFAULT_STOKEWIDTH;
		
		line.setWidth(Double.parseDouble(lineWidth));
		return line;
	}
	
	public static AnnotationGraphics getAnnotationGraphics(mxCellState state) throws ParameterException {
		Map<String, Object> style = state.getStyle();
		mxPoint offset = new mxPoint();
		double offsetX;
		double offsetY;
		if(state.getLabelBounds() !=null){
		offsetX = state.getLabelBounds().getX()-state.getCenterX();
		offsetY = state.getLabelBounds().getY() -state.getCenterY();}
		else {
			offsetX = 0;
			offsetY = 0;
		}
				offset.setX(offsetX);
				offset.setY(offsetY);
		AnnotationGraphics annotation = null;
		Fill fill = new Fill();
		String fillColor = (String) style.get(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR);
		if (fillColor != null) {
			fill.setColor(fillColor);
		}
		Font font = new Font();
		String alignStr = (String) style.get(mxConstants.STYLE_ALIGN);
		Align align = Align.getAlign(alignStr);
		font.setAlign(align);
		String family = (String) style.get(mxConstants.STYLE_FONTFAMILY);
		font.setFamily(family);
		String size = (String) style.get(mxConstants.STYLE_FONTSIZE);
		font.setSize(size);
		// TODO: text-rotation?
		// String color = (String)
		// state.getStyle().get(mxConstants.STYLE_FONTCOLOR);;
		// font.setColor(size); TODO: Where should font color be set in pnml
		// standard?
		if (style.containsKey(mxConstants.STYLE_FONTSTYLE)) {

			int fontstyle = new Integer((String) state.getStyle().get(mxConstants.STYLE_FONTSTYLE));
			if (fontstyle == mxConstants.FONT_BOLD) {
				font.setWeight("bold");
				font.setStyle("normal");
			}
			if (fontstyle == mxConstants.FONT_ITALIC) {
				font.setWeight("normal");
				font.setStyle("italic");
			}
			// if (style == mxConstants.FONT_UNDERLINE) {
			// font.setDecoration(Decoration.UNDERLINE);
			// font.setWeight("normal");
			// font.setStyle("normal");
			// }
			if (fontstyle == mxConstants.FONT_BOLD + mxConstants.FONT_ITALIC) {
				font.setWeight("bold");
				font.setStyle("italic");
			}
			// if (style == mxConstants.FONT_UNDERLINE + mxConstants.FONT_BOLD)
			// {
			// font.setDecoration(Decoration.UNDERLINE);
			// font.setWeight("bold");
			// font.setStyle("normal");
			// }

			// if (style == mxConstants.FONT_UNDERLINE +
			// mxConstants.FONT_ITALIC) {
			// font.setDecoration(Decoration.UNDERLINE);
			// font.setWeight("normal");
			// font.setStyle("italic");
			// }
		}
		// decoration missing: overline and Line-through

		// Line at the moment expresses same like line in NodeGraphics
		annotation = new AnnotationGraphics(new Offset((int) offset.getX(), (int) offset.getY()), fill, getArcLineAnnotation(state), font);

		return annotation;
	}

	private static Line getLineAnnotation(mxCellState state) throws ParameterException {
		Line line = new Line();
		line.setShape(Line.Shape.LINE);
		String color;
		line.setStyle(Line.Style.getStyle((String) state.getStyle().get(MXConstants.LINE_STYLE)));
		String lineWidth = "1";
		line.setWidth(Double.parseDouble(lineWidth));
		if (state.getStyle().containsKey(mxConstants.STYLE_LABEL_BORDERCOLOR)) {
			color = (String) state.getStyle().get(mxConstants.STYLE_LABEL_BORDERCOLOR);
			line.setColor(color);

			return line;
		}
		return line;

	}
	private static Line getArcLineAnnotation(mxCellState state) throws ParameterException {
		Line line = new Line();
		line.setShape(Line.Shape.LINE);
		String color;
		String linestyle = (String) state.getStyle().get(MXConstants.LABEL_LINE_STYLE);
		if(linestyle != null)
		line.setStyle(Line.Style.getStyle(linestyle));
		else line.setStyle(Line.Style.SOLID);
		String lineWidth = "1";
		line.setWidth(Double.parseDouble(lineWidth));
		if (state.getStyle().containsKey(mxConstants.STYLE_LABEL_BORDERCOLOR)) {
			color = (String) state.getStyle().get(mxConstants.STYLE_LABEL_BORDERCOLOR);
			line.setColor(color);

			return line;
		}
		return line;

	}

	public static ArcGraphics getArcGraphics(mxCellState state) throws ParameterException {
		List<mxPoint> points = ((PNGraphCell) state.getCell()).getGeometry().getPoints();
		Vector<Position> positions = new Vector<Position>();
		if(points != null){
		for (mxPoint point : points) {
			positions.add(new Position(point.getX(), point.getY()));
		}}
		return new ArcGraphics(positions, getLine(state));
	}

	public static AnnotationGraphics getArcAnnotationGraphics(mxCellState state) throws ParameterException {
		
		return getAnnotationGraphics(state);
	}
	
	private static String getSizeFromCSS(String size) {
		if (size.equals("medium"))
			return EditorProperties.getInstance().getDefaultFontSize();
		// if(size.equals("small"))
		// return mxConstants.DEFAULT_FONTSIZE;...
		// Other cases...

		return size;
	}

}
