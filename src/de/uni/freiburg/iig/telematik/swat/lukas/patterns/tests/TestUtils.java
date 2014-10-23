package de.uni.freiburg.iig.telematik.swat.lukas.patterns.tests;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import de.uni.freiburg.iig.telematik.jawl.parser.ParsingMode;
import de.uni.freiburg.iig.telematik.jawl.parser.xes.XESLogParser;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.swat.lukas.IOUtils;
import de.uni.freiburg.iig.telematik.swat.lukas.modelchecker.adapter.prism.modelconvertor.IFNetConverter;
import de.uni.freiburg.iig.telematik.swat.lukas.modelchecker.adapter.sciff.SCIFFExecutor;
import de.uni.freiburg.iig.telematik.swat.lukas.modelchecker.adapter.sciff.SCIFFResult;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.ResourcePattern;
import de.uni.freiburg.iig.telematik.swat.plugin.sciff.LogParserAdapter;

public class TestUtils {
	
	private final String mWorkingDir = System.getProperty("user.dir");
	private final String mResDir = File.separator + "res" + File.separator + 
			"de" + File.separator + "uni" + File.separator + "freiburg" + 
			File.separator + "iig" + File.separator + "telematik" + File.separator + "swat" + 
			File.separator + "lukas_test_resources" + File.separator;
	
	private File mIFNetFile;
	private File mPropertyFile;
	private ResourcePattern mResourcePattern;
	private String mProcessLogFileName;
	private boolean mIsAntiPattern;
	
	public TestUtils(IFNet net, CompliancePattern pattern) {
		mIsAntiPattern = pattern.isAntiPattern();
		IFNetConverter converter = new IFNetConverter(net);
		mIFNetFile = IOUtils.writeToFile(mWorkingDir, "model.pm",
				converter.convert().toString());
		mPropertyFile = IOUtils.writeToFile(mWorkingDir, "property.pctl",
				pattern.getPrismProperty(false));
		
	}
	
	public TestUtils(String pLogFileName, ResourcePattern pattern) {
		
		mProcessLogFileName = pLogFileName;
		mResourcePattern = pattern;
		
	}
	
	private String execToString(String command) throws Exception {
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    CommandLine commandline = CommandLine.parse(command);
	    DefaultExecutor exec = new DefaultExecutor();
	    PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
	    exec.setStreamHandler(streamHandler);
	    exec.execute(commandline);
	    return(outputStream.toString());
	}
	
	public boolean isPropertySatisfied() throws Exception {
		
		if (mIFNetFile != null) {
			
			String command1 = "prism " + mIFNetFile.getAbsolutePath() + " " + mPropertyFile.getAbsolutePath();
			String output1 = execToString(command1);
			IOUtils.deleteFile(mIFNetFile);
			IOUtils.deleteFile(mPropertyFile);
			boolean b = output1.contains("Result: true");
			return (mIsAntiPattern)? !b : b;
			
		} else {
			XESLogParser parser = new XESLogParser();
			parser.parse(new File(mWorkingDir + mResDir +mProcessLogFileName), ParsingMode.COMPLETE);
			LogParserAdapter adapter = new LogParserAdapter(parser);
			SCIFFExecutor sciffExec = new SCIFFExecutor(adapter);
			ArrayList<ResourcePattern> patterns = new ArrayList<ResourcePattern>();
			patterns.add(mResourcePattern);
			ArrayList<SCIFFResult> res = sciffExec.analyze(patterns);
			return res.get(0).isSatisfied();
			
		}
	}
	
}
