package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.lukas.IOUtils;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;
/**
 * a static class helping to store and load analysis
 * it uses XStream to serialize Lists of PatternSettings
 * @author bernhard
 *
 */
public class AnalysisStore {
	// PREFIX and SUFFIX of a file
	public static final String PREFIX="analysis_";
	public static final String SUFFIX=".xml";
	public static boolean store(List<PatternSetting> patternSettings, String netName) {
		XStream xstream=new XStream();
		String xml=xstream.toXML(patternSettings);
		String path=null;
		try {
			path=SwatProperties.getInstance().getNetWorkingDirectory();
			path+=System.getProperty("file.separator");
			path+=netName+System.getProperty("file.separator");
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (path == null) {
			return false;
		}
		AnalysisNameChooser ac = new AnalysisNameChooser(null,"Choose Name", path);
		String name = ac.requestInput();
		if(name != null) {
			IOUtils.writeToFile(path, PREFIX+name+SUFFIX, xml);
		}
		return true;
	}
	/**
	 * load a List of PatternSettings from a File f
	 * @param f the saved file
	 * @return a List of PatternSettings
	 */
	public static List<PatternSetting> loadFromFile(File f) {
		XStream xstream=new XStream();
		ArrayList<PatternSetting> list=(ArrayList<PatternSetting>) xstream.fromXML(f);
		return list;
	}
	/**
	 * remove PREFIX and SUFFIX from the filename
	 * @param
	 * @return
	 */
	public static String getDisplayNameforFilename(String filename) {
		String s=filename.replace(PREFIX, "");
		return s.replace(SUFFIX, "");
	}


}
