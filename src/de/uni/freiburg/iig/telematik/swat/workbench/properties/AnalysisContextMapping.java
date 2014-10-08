package de.uni.freiburg.iig.telematik.swat.workbench.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class AnalysisContextMapping implements java.io.Serializable {

	public String contextname;
	public String acmodel;

	public AnalysisContextMapping(String contextname, String acmodel) {
		super();
		this.contextname = contextname;
		this.acmodel = acmodel;
	}

	public AnalysisContextMapping() {
		super();
	}

//	public AnalysisContextMapping AnalysisContextMapping(File fileName) {
//		return load(fileName.getName());
//	}

	public String getContextname() {
		return contextname;
	}

	public void setContextname(String contextname) {
		this.contextname = contextname;
	}

	public String getAcmodel() {
		return acmodel;
	}

	public void setAcmodel(String acmodel) {
		this.acmodel = acmodel;
	}

	private AnalysisContextMapping load(String fileName) {

		FileInputStream fileIn;
		AnalysisContextMapping object = null;
		try {
			fileIn = new FileInputStream(fileName);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			object = (AnalysisContextMapping) in.readObject();
			in.close();
			fileIn.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return object;
	}

	private void save(AnalysisContextMapping object, String fileName) {
		try {
			FileOutputStream fileOut = new FileOutputStream(fileName);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(object);
			out.close();
			fileOut.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	public AnalysisContextMapping load(File context) {
		this.contextname = context.getName();
		return load(contextname);
	}
}