package de.uni.freiburg.iig.telematik.swat.lukas;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class PrismExecutor {
	
	private String mPrismPath;
	
	private PrismConverter mConverter;
	
	private String mFilesPath = System.getProperty("user.dir");

	public PrismExecutor(AbstractPetriNet<?,?,?,?,?,?,?> net) {
		
		if (net instanceof IFNet) {
			mConverter = new IFNetConverter((IFNet) net);
		} else if (net instanceof CWN) {
			mConverter = new CWNConverter((CWN) net);
		} else if (net instanceof PTNet) {
			mConverter = new PTNetConverter((PTNet) net);
		} else { 
			try {
				throw new Exception("No prism converter implemented for this net type!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		try {
			mPrismPath = SwatProperties.getInstance().getPrismPath();
			if (!mPrismPath.endsWith(File.separator)) {
				mPrismPath += File.separator;
			}
		} catch (ParameterException e) {
			e.printStackTrace();
		} catch (PropertyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public PrismResult anaylaze(ArrayList<CompliancePattern> patterns) {
		
		String properties = "";
		for (CompliancePattern pattern : patterns) {
			properties = "P=? [" + pattern.toString() + "]\n";
		}
		
		File modelFile = IOUtils.writeToFile(mFilesPath, "properties", properties);
		File propertiesFile = IOUtils.writeToFile(mFilesPath, "model.pm", mConverter.convert().toString());
		
		try {
			String resultStr = execToString(modelFile, propertiesFile);
			PrismResult pRes = new PrismResult(patterns, resultStr);
			return pRes;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	private String execToString(File model, File properties) throws Exception {
		
		String command = mPrismPath + "prism " + model.getAbsolutePath() + " " + properties.getAbsolutePath();;
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    CommandLine commandline = CommandLine.parse(command);
	    DefaultExecutor exec = new DefaultExecutor();
	    PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
	    exec.setStreamHandler(streamHandler);
	    exec.execute(commandline);
	    return(outputStream.toString());
	    
	}

}