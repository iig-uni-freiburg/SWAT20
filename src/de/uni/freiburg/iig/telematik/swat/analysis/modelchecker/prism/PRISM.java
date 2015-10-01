package de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.prism;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;

import de.invation.code.toval.file.FileUtils;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.ModelChecker;
import de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.prism.modeltranlator.CPNAdapter;
import de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.prism.modeltranlator.IFNetAdapter;
import de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.prism.modeltranlator.PTNetConverter;
import de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.prism.modeltranlator.PlaceException;
import de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.prism.modeltranlator.PrismModelAdapter;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class PRISM extends ModelChecker {
	
private String mPrismPath;
	
	private PrismModelAdapter mConverter;
	
	private String mFilesPath = System.getProperty("user.home")+System.getProperty("file.separator");
	
	private final String mPropertiesFileName = "properties";
	
	private final String mModelFileName = "model.pm";
	
	private final String mStatesFileName = "states";
	
	private PRISM(AbstractPetriNet<?,?,?,?,?> net, File prismPath) {
		TransitionToIDMapper.createMap(net);
                if(prismPath==null){
		try {
			mPrismPath = SwatProperties.getInstance().getPrismPath();
//			if (!mPrismPath.endsWith(File.separator)) {
//				mPrismPath += File.separator;
//			}
		} catch (ParameterException e) {
			Workbench.errorMessage("Could not load PRISM", e, true);
			e.printStackTrace();
		} catch (PropertyException e) {
			Workbench.errorMessage("Could not load PRISM. Please set PRISM path", e, true);
			e.printStackTrace();
		} catch (IOException e) {
			Workbench.errorMessage("Could not load PRISM", e, true);
			e.printStackTrace();
		}
                } else {
                    mPrismPath=prismPath.getAbsolutePath()+System.getProperty("file.separator");
                }
	}
	

	public PRISM(PTNet petriNet) {
		this((AbstractPetriNet<?,?,?,?,?>) petriNet,null);
		mConverter = new PTNetConverter(petriNet);
	}

	public PRISM(CPN petriNet) {
		this((AbstractPetriNet<?,?,?,?,?>) petriNet,null);
		mConverter = new CPNAdapter(petriNet);
	}

	public PRISM(IFNet petriNet) {
		this((AbstractPetriNet<?,?,?,?,?>) petriNet,null);
		mConverter = new IFNetAdapter(petriNet);
	}
        
        public PRISM(IFNet petriNet, File prismPath){
            this((AbstractPetriNet<?,?,?,?,?>) petriNet,prismPath);
		mConverter = new IFNetAdapter(petriNet);
        }

	@Override
	public void run(ArrayList<CompliancePattern> patterns) throws PrismException {
		File modelFile = null;
		try {
			modelFile = FileUtils.writeFile(mFilesPath, mModelFileName, mConverter.translate().toString());
		} catch (PlaceException e1) {
			Workbench.errorMessage(e1.getMessage(), e1, true);
		} catch (Exception e) {
			throw new PrismException("Cannot write mode file to disk", e);
		}
		
		String properties = "";
		for (CompliancePattern pattern : patterns) {
			if (!pattern.isInstantiated()) continue;
			properties += pattern.getFormalization() + "\n\n";
		}
		File propertiesFile = null;
		try {
			propertiesFile = FileUtils.writeFile(mFilesPath, mPropertiesFileName, properties);
		} catch(Exception e){
			throw new PrismException("Cannot write properties file to disk", e);
		}
		
		try {
			String resultStr = execToString(modelFile, propertiesFile);
			if (mConverter.isBoundedNet()) {
				String states = FileUtils.readStringFromFile(mFilesPath + File.separator + mStatesFileName);
				new PrismOutputHandler(patterns, resultStr, states);
			} else {
				new PrismOutputHandler(patterns, resultStr);
			}
		} catch (ExecuteException e) {
			throw new PrismException("Prism execution exception", e);
		} catch (IOException e) {
			throw new PrismException("Net might not be bounded - Prism I/O exception, File: " + mFilesPath + File.separator
					+ mStatesFileName, e);
		}

	}
	
	private String execToString(File model, File properties) throws ExecuteException, IOException {
		String command;
		String prism = (System.getProperty("os.name").contains("Windows"))? "prism.bat" : "prism";
		
		if (mConverter.isBoundedNet()) {
			command = mPrismPath + " " + model.getAbsolutePath() +  " " + properties.getAbsolutePath()
					+ " -exportstates " + mStatesFileName;
		} else {
			command = mPrismPath + " " + model.getAbsolutePath() + " " + properties.getAbsolutePath() + " -ex";
		}
		
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
	    CommandLine commandline = CommandLine.parse(command);
	    DefaultExecutor exec = new DefaultExecutor();
	    PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream);
		//Workbench.consoleMessage("Executing: " + command);
	    exec.setStreamHandler(streamHandler);
	    System.out.println("Executing "+command);
	    exec.execute(commandline);
	    System.out.println("ID-Mapping: "+TransitionToIDMapper.getMapping());
	    return(outputStream.toString());
	    
	}

}
