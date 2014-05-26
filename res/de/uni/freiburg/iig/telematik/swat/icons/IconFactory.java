package de.uni.freiburg.iig.telematik.swat.icons;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.swing.ImageIcon;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class IconFactory {
	
	private static final String imagePathFormat = "%s/%s-%s.png";
	
	public static ImageIcon getIcon(String name) throws ParameterException, PropertyException, IOException{
		return getIcon(name, SwatProperties.getInstance().getIconSize());
	}
	
	private static ImageIcon getIcon(String name, IconSize size) throws ParameterException{
		System.out.println("name: " + name);
		System.out.println("size: " + size);
		Validate.notNull(name);
		Validate.notEmpty(name);
		Validate.notNull(size);
		System.out.println("ddd");
		
		String imagePath = String.format(imagePathFormat, size.getSize(), name, size.getSize());
		URL imageURL = IconFactory.class.getResource(imagePath);
		try {
			return new ImageIcon(imageURL);
		} catch(Exception e){
			throw new ParameterException("Cannot load icon with name \""+name+"\": " + e.getMessage());
		}
	}
	
	private static Image getIconImage(String name, IconSize size) throws ParameterException{
		Validate.notNull(name);
		Validate.notEmpty(name);
		Validate.notNull(size);
		
		String imagePath = String.format(imagePathFormat, size.getSize(), name, size.getSize());
		URL imageURL = IconFactory.class.getResource(imagePath);
		try {
			return new ImageIcon(imageURL).getImage();
		} catch(Exception e){
			throw new ParameterException("Cannot load icon with name \""+name+"\": " + e.getMessage());
		}
	}
	
	public enum IconSize{
		SMALL(26), MEDIUM(32), LARGE(48);
		
		private int size = 0;
		
		private IconSize(int size){
			this.size = size;
		}
		
		public int getSize(){
			return size;
		}
	}
	
	public static void main(String[] args) throws ParameterException, PropertyException, IOException {
		IconFactory.getIcon("save");
	}

}
