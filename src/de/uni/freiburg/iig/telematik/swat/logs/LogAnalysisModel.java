package de.uni.freiburg.iig.telematik.swat.logs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import com.thoughtworks.xstream.XStream;

public class LogAnalysisModel {

	private String logFile;
	private String ruleString;
	private List<Integer> wrong;
	private List<Integer> correct;
	private List<Integer> exception;
	private String createdBy;
	private String operatorString;

	public static LogAnalysisModel loadFromFile(File file) {
		XStream stream = new XStream();
		LogAnalysisModel model = (LogAnalysisModel) stream.fromXML(file);
		return model;
	}

	public void store(File file) throws IOException {
		XStream stream = new XStream();
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
		bw.write(stream.toXML(this));
		bw.close();
	}


}
