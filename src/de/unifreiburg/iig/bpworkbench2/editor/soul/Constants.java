package de.unifreiburg.iig.bpworkbench2.editor.soul;

import java.awt.Color;

import de.unifreiburg.iig.bpworkbench2.editor.gui.mxConstants;

public abstract class Constants {

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
    
	public static final String PNPlace = "pnPlace";
	public static final String PNPlaceShape = "shape=" + PNPlace + ";"  +"fillColor="+Integer.toHexString(Constants.bluehigh.getRGB())+ ";"  + "strokeWidth=2.0;"+ "strokeColor="+Integer.toHexString(Constants.bluelow.getRGB())+ ";" + "labelBackgroundColor="+Integer.toHexString(Constants.blueBG.getRGB())+";";
	public static final String PNTransition = "pnTransition";
	public static final String PNTransitionShape = "shape=" + PNTransition + ";"  +"fillColor="+Integer.toHexString(Constants.bluehigh.getRGB())+ ";" + "strokeWidth=2.0;"+ "strokeColor="+Integer.toHexString(Constants.bluelow.getRGB())+ ";"+"labelBackgroundColor="+Integer.toHexString(Constants.blueBG.getRGB())+";";
	public static final String PlaceNamePrefix = "p";
	public static final String TransitionNamePrefix = "t";
	public static final String PNLabelStyle ="shape=none;fontSize=12;fontColor="+ Integer.toHexString(Constants.bluelow.getRGB())+ ";";
 

	
}
