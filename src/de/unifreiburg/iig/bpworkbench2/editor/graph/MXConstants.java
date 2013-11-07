package de.unifreiburg.iig.bpworkbench2.editor.graph;

import java.awt.Color;
import java.awt.image.BufferedImage;

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
	
	public static final String PNPlace = "pnPlace";
	public static final String PNPlaceShape = "shape=" + PNPlace + ";"  +"fillColor="+Integer.toHexString(MXConstants.bluehigh.getRGB())+ ";"  + "strokeWidth=2.0;"+ "strokeColor="+Integer.toHexString(MXConstants.bluelow.getRGB())+ ";" + "labelBackgroundColor="+Integer.toHexString(MXConstants.blueBG.getRGB())+";";
	public static final String PNTransition = "pnTransition";
	public static final String PNTransitionShape = "shape=" + PNTransition + ";"  +"fillColor="+Integer.toHexString(MXConstants.bluehigh.getRGB())+ ";" + "strokeWidth=2.0;"+ "strokeColor="+Integer.toHexString(MXConstants.bluelow.getRGB())+ ";"+"labelBackgroundColor="+Integer.toHexString(MXConstants.blueBG.getRGB())+";";
	public static final String PlaceNamePrefix = "p";
	public static final String TransitionNamePrefix = "t";
	public static final String PNLabelStyle ="shape=none;fontSize=12;fontColor="+ Integer.toHexString(MXConstants.bluelow.getRGB())+ ";";
	
}
