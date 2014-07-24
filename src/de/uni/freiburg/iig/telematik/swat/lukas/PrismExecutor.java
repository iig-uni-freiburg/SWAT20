package de.uni.freiburg.iig.telematik.swat.lukas;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
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
			properties += "P=? [" + pattern.toString() + "]\n\n";
		}
		
		File modelFile = IOUtils.writeToFile(mFilesPath, "model.pm", mConverter.convert().toString());
		File propertiesFile = IOUtils.writeToFile(mFilesPath, "properties", properties);
		
		try {
			String resultStr = execToString(modelFile, propertiesFile);
			PrismResult pRes = new PrismResult(patterns, resultStr);
			return pRes;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	private String execToString(File model, File properties) {
		
		String command;
		String prism = (System.getProperty("os.name").contains("Windows"))? "prism.bat" : "prism";
		
		if (mConverter.isUnboundedNet()) {
			command = mPrismPath + File.separator + "bin" + File.separator + prism + " " + model.getAbsolutePath() + 
					" " + properties.getAbsolutePath(); 
		} else {
			command = mPrismPath + File.separator + "bin" + File.separator + prism + " " + model.getAbsolutePath() + 
					" " + properties.getAbsolutePath() + " -ex";
		}
		
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
	    CommandLine commandline = CommandLine.parse(command);
	    DefaultExecutor exec = new DefaultExecutor();
	    PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream);
	    exec.setStreamHandler(streamHandler);
	    try {
			exec.execute(commandline);
		} catch (ExecuteException e) {
			System.out.println("SUBPROCESS OUTPUT: " + errorStream.toString());
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return(outputStream.toString());
	    
	}

}
