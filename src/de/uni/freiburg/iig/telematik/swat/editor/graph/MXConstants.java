package de.uni.freiburg.iig.telematik.swat.editor.graph;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Vector;

import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.ArcGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Dimension;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill.GradientRotation;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Line;
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
	static{
        try{
            MXConstants.EMPTY_IMAGE = new BufferedImage(1, 1,BufferedImage.TYPE_INT_RGB);
        }
        catch (Exception e){
            // Occurs when running on GAE, BufferedImage is a
            // blacklisted class
            MXConstants.EMPTY_IMAGE = null;
        }
    }
	
	public static final String DEFAULT_PLACE_SHAPE = "shape=ellipse;"  +"spacingLeft=0;"+"spacingTop=30;"+ "fillColor="+Integer.toHexString(MXConstants.bluehigh.getRGB())+ ";"  + "strokeWidth=2.0;"+ "strokeColor="+Integer.toHexString(MXConstants.bluelow.getRGB())+ ";" + "labelBackgroundColor="+Integer.toHexString(MXConstants.blueBG.getRGB())+";";
	public static final String DEFAULT_TRANSITION_SHAPE = "shape=rectangle;"+"spacingLeft=0;"+"spacingTop=30;"  +"fillColor="+Integer.toHexString(MXConstants.bluehigh.getRGB())+ ";" + "strokeWidth=2.0;"+ "strokeColor="+Integer.toHexString(MXConstants.bluelow.getRGB())+ ";"+"labelBackgroundColor="+Integer.toHexString(MXConstants.blueBG.getRGB())+";";
	public static final String DEFAULT_ARC_SHAPE = "";
	
//	public static final String PNPlace = "pnPlace";
//	public static final String PNPlaceShape = "shape=" + PNPlace + ";"  +"fillColor="+Integer.toHexString(MXConstants.bluehigh.getRGB())+ ";"  + "strokeWidth=2.0;"+ "strokeColor="+Integer.toHexString(MXConstants.bluelow.getRGB())+ ";" + "labelBackgroundColor="+Integer.toHexString(MXConstants.blueBG.getRGB())+";";
//	public static final String PNTransition = "pnTransition";
//	public static final String PNTransitionShape = "shape=" + PNTransition + ";"  +"fillColor="+Integer.toHexString(MXConstants.bluehigh.getRGB())+ ";" + "strokeWidth=2.0;"+ "strokeColor="+Integer.toHexString(MXConstants.bluelow.getRGB())+ ";"+"labelBackgroundColor="+Integer.toHexString(MXConstants.blueBG.getRGB())+";";
//	
	public static final String PlaceNamePrefix = "p";
	public static final String TransitionNamePrefix = "t";
	public static final String PNLabelStyle ="shape=none;fontSize=12;fontColor="+ Integer.toHexString(MXConstants.bluelow.getRGB())+ ";";
	
	public static String getStyle(PNComponent type, NodeGraphics nodeGraphics){
//		getCellStyle(cell).put(mxConstants.STYLE_SPACING_LEFT, -30);
		//TODO:
//		if(nodeGraphics == null){
		if(true){
			if (type == PNComponent.PLACE)
				return DEFAULT_PLACE_SHAPE;
			if (type == PNComponent.TRANSITION)
				return DEFAULT_TRANSITION_SHAPE;
		}
		return null;
	}
	
	public static String getStyle(ArcGraphics arcGraphics){
		//TODO: Only line attributes!
//		if(arcGraphics == null){
		if(true){
			return null;
		}
		return null;
	}
	
	
	public static NodeGraphics getNodeGraphics(PNGraphCell cell) throws ParameterException{
		Position position = new Position(cell.getGeometry().getX(), cell.getGeometry().getY());
		Dimension dimension = new Dimension(cell.getGeometry().getWidth(), cell.getGeometry().getHeight());
		Fill fill = new Fill();
		String fillColor = cell.getAttribute(mxConstants.STYLE_FILLCOLOR);
		if(fillColor != null){
			fill.setColor(fillColor);
		}
		String gradientColor = cell.getAttribute(mxConstants.STYLE_GRADIENTCOLOR);
		if(gradientColor != null){
			fill.setGradientColor(gradientColor);
		}
		GradientRotation gradientRotation = null;
		String gradientDirecion = cell.getAttribute(mxConstants.STYLE_GRADIENT_DIRECTION);
		if(gradientDirecion != null){
		if(gradientDirecion.equals(mxConstants.DIRECTION_EAST) || gradientDirecion.equals(mxConstants.DIRECTION_WEST)){
			gradientRotation = GradientRotation.HORIZONTAL;
		} else if(gradientDirecion.equals(mxConstants.DIRECTION_SOUTH) || gradientDirecion.equals(mxConstants.DIRECTION_NORTH)){
			gradientRotation = GradientRotation.VERTICAL;
		}
		}
		if(gradientRotation != null){
			fill.setGradientRotation(gradientRotation);
		}
		String image = cell.getAttribute(mxConstants.STYLE_IMAGE);
		if(image != null){
			try {
				fill.setImage(new URI(image));
			} catch (URISyntaxException e) {
				throw new ParameterException(e.getMessage());
			}
		}
		
		return new NodeGraphics(position, dimension, fill, getLine(cell));
	}
	
	private static Line getLine(PNGraphCell cell) throws ParameterException{
		Line line = new Line();
		line.setShape(Line.Shape.LINE);
		String strokeStyle = cell.getAttribute(mxConstants.STYLE_DASHED);
		boolean dashed = new Boolean(strokeStyle);
		if(dashed){
			line.setStyle(Line.Style.DASH);
		} else {
			line.setStyle(Line.Style.SOLID);
		}
		String lineWidth = cell.getAttribute(mxConstants.STYLE_STROKEWIDTH);
		Validate.positiveInteger(lineWidth);
		line.setWidth(Double.parseDouble(lineWidth));
		return line;
	}
	
	public static ArcGraphics getArcGraphics(PNGraphCell cell) throws ParameterException{
		List<mxPoint> points = cell.getGeometry().getPoints();
		Vector<Position> positions = new Vector<Position>();
		for(mxPoint point: points){
			positions.add(new Position(point.getX(), point.getY()));
		}
		return new ArcGraphics(positions, getLine(cell));
	}
}
