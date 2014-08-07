package de.uni.freiburg.iig.telematik.swat.lukas;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;

public class TestUtils {
	
	private String mFilesPath = System.getProperty("user.dir");
	private File mIFNetFile;
	private File mPropertyFile;
	
	public TestUtils(IFNet net, CompliancePattern pattern) {
		IFNetConverter converter = new IFNetConverter(net);
		mIFNetFile = IOUtils.writeToFile(mFilesPath, "model.pm",
				converter.convert().toString());
		mPropertyFile = IOUtils.writeToFile(mFilesPath, "property.pctl",
				pattern.getPrismProperty(false));
		
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

		String command1 = "prism " + mIFNetFile.getAbsolutePath() + " " + mPropertyFile.getAbsolutePath();
		String output1 = execToString(command1);
		IOUtils.deleteFile(mIFNetFile);
		IOUtils.deleteFile(mPropertyFile);
		return output1.contains("Result: true");
	}
	
}
