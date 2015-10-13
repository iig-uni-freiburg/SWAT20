package de.uni.freiburg.iig.telematik.swat.logs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.MessageDigest;

import de.invation.code.toval.file.FileUtils;
import de.invation.code.toval.misc.NamedComponent;
import de.uni.freiburg.iig.telematik.sewol.parser.ParsingMode;
import de.uni.freiburg.iig.telematik.sewol.parser.mxml.MXMLLogParser;
import de.uni.freiburg.iig.telematik.sewol.parser.xes.XESLogParser;
import de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.sciff.AristaFlowParser;
import de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.sciff.AristaFlowParser.whichTimestamp;
import de.uni.freiburg.iig.telematik.swat.plugin.sciff.LogParserAdapter;

import java.util.Objects;

import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogReader;

public class LogModel implements NamedComponent{

	private File fileReference;
	private String name;
	private SwatLogType type = null;
	private ISciffLogReader logReader;
	
	public LogModel(File fileReference, SwatLogType type) {
		super();
		setFileReference(fileReference);
		setName(FileUtils.getFileWithoutEnding(fileReference));
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public File getFileReference() {
		return fileReference;
	}

	public void setFileReference(File fileReference) {
		this.fileReference = fileReference;
	}

	public SwatLogType getType() {
		return type;
	}

	@Override
	public int hashCode() {
		try {
			return getMD5Checksum(fileReference);
		} catch (Exception e) {
			return -1;
		}
	}

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LogModel other = (LogModel) obj;
        if (!Objects.equals(this.fileReference, other.fileReference)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

	//from http://stackoverflow.com/questions/304268/getting-a-files-md5-checksum-in-java
	public static byte[] createChecksum(File filename) throws Exception {
		InputStream fis = new FileInputStream(filename);

		byte[] buffer = new byte[1024];
		MessageDigest complete = MessageDigest.getInstance("MD5");
		int numRead;

		do {
			numRead = fis.read(buffer);
			if (numRead > 0) {
				complete.update(buffer, 0, numRead);
			}
		} while (numRead != -1);

		fis.close();
		return complete.digest();
	}

	public static int getMD5Checksum(File filename) throws Exception {
		byte[] b = createChecksum(filename);
		int result = 0;

		for (int i = 0; i < b.length; i++) {
			result += ((b[i] & 0xff) + 0x100);
		}
		return result;
	}
	
	@Override
	public LogModel clone(){
		return new LogModel(new File(getFileReference().getAbsolutePath()), type);
	}
	
	public ISciffLogReader getLogReader() throws Exception{
		if(logReader==null){
			switch (getType()) {
			case Aristaflow:
				logReader = new AristaFlowParser(getFileReference());
				((AristaFlowParser)logReader).parse(whichTimestamp.BOTH);
				break;
			case MXML:
				MXMLLogParser mxmlParser = new MXMLLogParser();
				mxmlParser.parse(fileReference, ParsingMode.COMPLETE);
				logReader=new LogParserAdapter(mxmlParser);
				break;
			case XES:
				XESLogParser xesParser= new XESLogParser();
				xesParser.parse(getFileReference(), ParsingMode.COMPLETE);
				logReader=new LogParserAdapter(xesParser);
				break;
			default:
				break;
			}
		}
		return logReader;
	}
	
	public void setLogReader(ISciffLogReader reader){
		this.logReader=reader;
	}
	
	public boolean hasLogReaderSet(){
		return logReader!=null;
	}

}
