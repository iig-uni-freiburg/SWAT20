package de.uni.freiburg.iig.telematik.swat.lukas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JOptionPane;

import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;

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
			JOptionPane.showMessageDialog(Workbench.getInstance(), "Could not save to " + file + "\br Reason: " + e.getMessage());
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

	/*public static String readFile(String filepath, Charset encoding) {
		
		String str = "";
		try {
			byte[] encoded;
			encoded = Files.readAllBytes(Paths.get(filepath));
		    str = new String(encoded, encoding);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
		  
	}*/
	
	public static String readFile(String filepath) {
		
		BufferedReader bufferedReader;
		try {
			bufferedReader = new BufferedReader(new FileReader(filepath));
			StringBuffer stringBuffer = new StringBuffer();
			String line = null;
			
			while((line = bufferedReader.readLine())!=null){
				stringBuffer.append(line).append("\n");
			}
			bufferedReader.close();
			return stringBuffer.toString();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
