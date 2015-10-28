package de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.sciff.presenter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.List;

import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogReader;
import org.processmining.analysis.sciffchecker.logic.reasoning.CheckerReport;

import com.thoughtworks.xstream.XStream;

import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sewol.parser.LogParser;
import de.uni.freiburg.iig.telematik.sewol.parser.LogParserInterface;
import de.uni.freiburg.iig.telematik.sewol.parser.LogParsingFormat;
import de.uni.freiburg.iig.telematik.sewol.parser.ParsingMode;
import de.uni.freiburg.iig.telematik.swat.aristaFlow.AristaFlowParser;
import de.uni.freiburg.iig.telematik.swat.aristaFlow.AristaFlowParser.whichTimestamp;
import de.uni.freiburg.iig.telematik.swat.plugin.sciff.LogParserAdapter;

public class LogSerializer {
	private String logFile;
	private String ruleString;
	private List<Integer> wrong;
	private List<Integer> correct;
	private List<Integer> exception;
	private String createdBy;
	private String operatorString;

	private LogSerializer(SciffPresenter presenter) {

		//extract Filereference to logFile
		try {
			this.logFile = presenter.getLogFile().getCanonicalPath();
		} catch (IOException e) {
			this.logFile = presenter.getLogFile().getAbsolutePath();
		}

		//get wrong and wright indices
		this.wrong = presenter.getReport().wrongInstances();
		this.correct = presenter.getReport().correctInstances();
		this.exception = presenter.getReport().exceptionInstances();
		this.ruleString = presenter.getRuleString();

		//get Author
		this.createdBy = presenter.getCreatedByString();
	}

	public static void main(String[] args) throws Exception {
		SciffPresenter presenter = read(new File("/tmp/test.xml"));
		presenter.show();
	}


	public static void store(File file, SciffPresenter presenter) {
		store(file, new LogSerializer(presenter));
	}


	private static void store(final File file, final LogSerializer serializer) {

		new Thread(new Runnable() {
			@Override
			public void run() {
				XStream stream = new XStream();
				try {
					BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UTF-8")));
					bw.write(stream.toXML(serializer));
					bw.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}).start();
	}

	public static SciffPresenter read(File file) throws Exception {
		//XStream stream = new XStream(new DomDriver());
		XStream stream = new XStream();
		LogSerializer serializer = (LogSerializer) stream.fromXML(file);
		return serializer.generatePresenter();
	}

	private SciffPresenter generatePresenter() throws Exception {
		SciffPresenter presenter = new SciffPresenter(generateReport(), null, this.ruleString, new File(logFile));
		presenter.setOperatorString(operatorString);
		presenter.setCreatedByString(createdBy);
		return presenter;
	}

	private CheckerReport generateReport() throws Exception {
		CheckerReport report = new CheckerReport(generateLogReader());

		//add wrong instances
		for (int w : wrong) {
			report.addWrong(w);
		}

		//add correct instances
		for (int c : correct) {
			report.addCorrect(c);
		}

		//add exceptional instances
		for (int e : exception) {
			report.addException(e);
		}

		return report;
	}

	private ISciffLogReader generateLogReader() throws Exception {
		if (logFile.endsWith("mxml"))
			return createMxmlParser();
		return createAristaFlowParser();
	}

	private ISciffLogReader createAristaFlowParser() throws Exception {
		AristaFlowParser parser = new AristaFlowParser(new File(logFile));
		parser.parse(whichTimestamp.BOTH);
		return parser;
	}

	private ISciffLogReader createMxmlParser() throws ParserException, ParameterException, IOException {
		LogParserInterface parser = LogParser.getParser(new File(logFile), LogParsingFormat.XES);
		parser.parse(new File(logFile), ParsingMode.COMPLETE);
		return new LogParserAdapter(parser);
	}


}
