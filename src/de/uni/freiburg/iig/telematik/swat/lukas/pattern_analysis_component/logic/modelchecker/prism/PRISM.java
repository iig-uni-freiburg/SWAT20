package de.uni.freiburg.iig.telematik.swat.lukas.pattern_analysis_component.logic.modelchecker.prism;

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
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.swat.lukas.IOUtils;
import de.uni.freiburg.iig.telematik.swat.lukas.pattern_analysis_component.logic.modelchecker.ModelChecker;
import de.uni.freiburg.iig.telematik.swat.lukas.pattern_analysis_component.logic.modelchecker.prism.modeltranlator.CPNAdapter;
import de.uni.freiburg.iig.telematik.swat.lukas.pattern_analysis_component.logic.modelchecker.prism.modeltranlator.IFNetAdapter;
import de.uni.freiburg.iig.telematik.swat.lukas.pattern_analysis_component.logic.modelchecker.prism.modeltranlator.PTNetConverter;
import de.uni.freiburg.iig.telematik.swat.lukas.pattern_analysis_component.logic.modelchecker.prism.modeltranlator.PrismModelAdapter;
import de.uni.freiburg.iig.telematik.swat.lukas.pattern_creator_component.logic.patterns.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class PRISM extends ModelChecker {
	
private String mPrismPath;
	
	private PrismModelAdapter mConverter;
	
	private String mFilesPath = System.getProperty("user.dir");
	
	private final String mPropertiesFileName = "properties";
	
	private final String mModelFileName = "model.pm";
	
	private final String mStatesFileName = "states";
	
	private PRISM(AbstractPetriNet<?,?,?,?,?,?,?> net) {
		TransitionToIDMapper.createMap(net);
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
	

	public PRISM(PTNet petriNet) {
		this((AbstractPetriNet<?,?,?,?,?,?,?>) petriNet);
		mConverter = new PTNetConverter(petriNet);
	}

	public PRISM(CPN petriNet) {
		this((AbstractPetriNet<?,?,?,?,?,?,?>) petriNet);
		mConverter = new CPNAdapter(petriNet);
	}

	public PRISM(IFNet petriNet) {
		this((AbstractPetriNet<?,?,?,?,?,?,?>) petriNet);
		mConverter = new IFNetAdapter(petriNet);
	}

	@Override
	public void run(ArrayList<CompliancePattern> patterns) {
		
		File modelFile = IOUtils.writeToFile(mFilesPath, mModelFileName, mConverter.translate().toString());
		String properties = "";
		for (CompliancePattern pattern : patterns) {
			if (!pattern.isInstantiated()) continue;
			properties += pattern.getFormalization() + "\n\n";
		}
		File propertiesFile = IOUtils.writeToFile(mFilesPath, mPropertiesFileName, properties);
		try {
			String resultStr = execToString(modelFile, propertiesFile);
			if (mConverter.isBoundedNet()) {
				
				String states = IOUtils.readFile(mFilesPath + File.separator + mStatesFileName);
				new PrismOutputHandler(patterns, resultStr, states);
				
			} else {
				new PrismOutputHandler(patterns, resultStr);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	private String execToString(File model, File properties) {
		
		String command;
		String prism = (System.getProperty("os.name").contains("Windows"))? "prism.bat" : "prism";
		
		if (mConverter.isBoundedNet()) {
			command = mPrismPath + "bin" + File.separator + prism + " " + model.getAbsolutePath() + 
					" " + properties.getAbsolutePath() + " -exportstates " + mStatesFileName; 
		} else {
			command = mPrismPath + "bin" + File.separator + prism + " " + model.getAbsolutePath() + 
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
			System.out.println(errorStream.toString());
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return(outputStream.toString());
	    
	}

}
