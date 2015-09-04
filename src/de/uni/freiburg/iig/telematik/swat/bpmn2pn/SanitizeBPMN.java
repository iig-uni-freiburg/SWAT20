package de.uni.freiburg.iig.telematik.swat.bpmn2pn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SanitizeBPMN {
	
	static File file;
	
	public static void main (String[] args) throws IOException{
		System.out.println(SanitizeBPMN.sanitize(new File("/tmp/bpmn.bpmn")));
	}
	
	public SanitizeBPMN (File file){
		this.file=file;
	}
	
	public static String sanitize(File file) throws IOException{
		try(BufferedReader br = new BufferedReader(new FileReader(file))) {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();
		    while (line != null) {
		        sb.append(line.replace("_", "id"));
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    String everything = sb.toString();
		    return everything;
		} 
	}
	
	public static String sanitize (File in, File out) throws IOException{
		String all = sanitize(in);
		FileWriter fw = new FileWriter(out);
		fw.write(all);
		fw.close();
		return all;
		
	}

}
