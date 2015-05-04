package de.uni.freiburg.iig.telematik.swat.logs;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

import de.invation.code.toval.file.FileUtils;

public class LogModel {

	private File fileReference;
	private String name;
	private SwatLog type = null;
	
	public LogModel(File fileReference, SwatLog type) {
		super();
		setFileReference(fileReference);
		setName(FileUtils.getName(fileReference));
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

	public SwatLog getType() {
		return type;
	}

	@Override
	public int hashCode() {
		try {
			return getMD5Checksum(fileReference);
		} catch (Exception e) {
			//e.printStackTrace();
			return -1;
		}
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

}
