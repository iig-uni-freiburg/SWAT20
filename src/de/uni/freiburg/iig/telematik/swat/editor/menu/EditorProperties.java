package de.uni.freiburg.iig.telematik.swat.editor.menu;

import java.awt.Color;
import java.net.URI;

import com.mxgraph.util.mxConstants;

import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Fill.GradientRotation;

public class EditorProperties {
	
	private static EditorProperties instance = null;
	
	public static EditorProperties getInstance(){
		if(instance == null){
			instance = new EditorProperties();
		}
		return instance;
	}
	
	public int getDefaultPlaceSize(){
		return 30;
	}
	
	public int getDefaultTransitionWidth(){
		return 30;
	}
	
	public int getDefaultTransitionHeight(){
		return 30;
	}
	
	public int getDefaultVerticalLabelOffset(){
		return 30;
	}
	
	public int getDefaultHorizontalLabelOffset(){
		return 0;
	}
	
	public int getDefaultTokenSize(){
		return 8;
	}

	public int getDefaultTokenDistance(){
		return 1;
	}

	public String getDefaultNodeColor() {
		return "#B6CAE4";
	}

	public String getDefaultGradientColor() {
		return null;
	}

	public GradientRotation getDefaultGradientDirection() {
		return null;
	}

	public URI getDefaultNodeImage() {
		return null;
	}

	public String getDefaultLineColor() {
		// TODO Auto-generated method stub
		return "#A6BAD4";
	}

	public String getDefaultFontFamily() {
		// TODO Auto-generated method stub
		return "Dialog";
	}

	public String getDefaultFontSize() {
		// TODO Auto-generated method stub
		return "11";
	}




}
