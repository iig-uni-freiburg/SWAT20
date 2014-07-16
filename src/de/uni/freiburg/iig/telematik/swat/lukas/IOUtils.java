package de.uni.freiburg.iig.telematik.swat.lukas;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class IOUtils {
	
	public static File writeToFile(String path, String filename, String content) {
		
		FileOutputStream fop = null;
		File file = null;
		try {
			path += (path.endsWith(File.separator))? "" : File.separator;
			file = new File(path + filename);
			fop = new FileOutputStream(file);
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			// get the content in bytes
			byte[] contentInBytes = content.getBytes();
 
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	public static void deleteFile(File file) {
		try{
    		file.delete();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
	}

	public static String readFile(String filepath, Charset encoding) {
		
		String str = "";
		try {
			byte[] encoded;
			encoded = Files.readAllBytes(Paths.get(filepath));
		    str = new String(encoded, encoding);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
		  
	}
}
