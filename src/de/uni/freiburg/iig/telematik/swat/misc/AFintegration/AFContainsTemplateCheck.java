package de.uni.freiburg.iig.telematik.swat.misc.AFintegration;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import de.uni.freiburg.iig.telematik.swat.aristaFlow.AristaFlowToPnmlConverter;

public class AFContainsTemplateCheck {
	
	private AristaFlowToPnmlConverter parser;

	public AFContainsTemplateCheck(){
		
	}
	
	public AFContainsTemplateCheck(File template) throws Exception {
		try {
			setAFTemplate(template);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new Exception("Could not parse template",e);
		}
	}
	
	public void setAFTemplate(File file) throws ParserConfigurationException, SAXException, IOException{
		parser = new AristaFlowToPnmlConverter(file);
		parser.parse();
	}
	
	public boolean templateContainsActivity(String activity){
		return parser.containsActivity(activity);
	}

}
