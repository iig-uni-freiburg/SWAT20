package de.unifreiburg.iig.bpworkbench2.helper;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.unifreiburg.iig.bpworkbench2.controller.SWAT2Controller;
import de.unifreiburg.iig.bpworkbench2.gui.SplitGui;
import de.unifreiburg.iig.bpworkbench2.logging.BPLog;

public class SwatProperties {
	static Logger log = BPLog.getLogger(SplitGui.class.getName());
	// private static SwatProperties props = new SwatProperties();
	private static Properties property = new Properties();
	private static URL propertyFile;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

	public static String getProperty(String key) {
		return property.getProperty(key);

	}

	public static String getProperty(String key, String defaultValue) {
		return property.getProperty(key, defaultValue);
	}

	private Object getObject(Object key) {
		return property.get(key);
	}

	public static Object setProperty(String key, String value) {
		Object result = property.setProperty(key, value);
		store();
		return result;
	}

	private SwatProperties() {
		propertyFile = SWAT2Controller.class.getResource("../ressources/swat2config.properties");
		System.out.println("Prop-File: " + propertyFile.getFile());
		if (propertyFile == null) {
			// create Property file
			createPropFile();
		}
		// load Properties
		try {
			property.load(new FileReader(propertyFile.getFile()));
		} catch (FileNotFoundException e) {
			// Create Properties file
			createPropFile();
			try {
				// try again to read the newly generated property file
				property.load(new FileReader(propertyFile.getFile()));
			} catch (FileNotFoundException e1) {
				log.log(Level.SEVERE, "Could not load or create property file");
				e1.printStackTrace();
			} catch (IOException e1) {
				log.log(Level.SEVERE, "Could not load or create property file");
				e1.printStackTrace();
			}
		} catch (IOException e) {
			log.log(Level.SEVERE, "Could not load property file. IO Exception");
			// e.printStackTrace();
		}
	}

	private void createPropFile() {
		try {
			Writer writer = new FileWriter("../ressources/swat2config.properties");
			property.store(writer, "SWAT2.0 Config");
			writer.flush();
			writer.close();
			writer = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.log(Level.SEVERE, "Could not create or store properties file");
			e.printStackTrace();
		}
		propertyFile = SWAT2Controller.class.getResource("../ressources/swat2config.properties");

	}

	public static void store() {
		try {
			Writer writer = new FileWriter(propertyFile.getFile());
			writer.flush();
			writer.close();
			writer = null;
		} catch (IOException e) {
			log.log(Level.SEVERE, "Could not save to property file");
			e.printStackTrace();
		}
	}
}
