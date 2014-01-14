package de.uni.freiburg.iig.telematik.swat.lola;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.swat.prism.searcher.PrismSearcherFactory;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class LolaRunner {

	File lolaPath;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			LolaRunner test = new LolaRunner();
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public LolaRunner() throws ParameterException {
			String lolaPath = null;
			// Check if prism path is set as property.
			try {
			lolaPath = getLolaPath();
			} catch (Exception e) {
				// Exception while extracting prism path.
				// -> Search for prism path
				File searchedPrismPath = null;
				try {
					searchedPrismPath = PrismSearcherFactory.getPrismSearcher().getPrismPath();
				} catch (Exception e1) {
					// Exception while searching for prism path
				}
				if(searchedPrismPath == null)
					throw new ParameterException("Cannot find Prism executable.");
				try {
					SwatProperties.getInstance().setPrismPath(searchedPrismPath.getAbsolutePath());
				} catch (Exception e1) {
					// Exception while setting the prism path
					throw new ParameterException("Cannot store Prism path in swat properties.\n Path: "+searchedPrismPath.getAbsolutePath());
				}
				lolaPath = searchedPrismPath.getAbsolutePath();
			}
		this.lolaPath = new File(lolaPath);
		System.out.println("lola Path is: " + this.lolaPath);
		
	}
	
	public String analyze(GraphicalPTNet net, Object... property) {
		return null;
	}

	private String getLolaPath() throws IOException {
		//HACK: Only for Linux. Hardcoded. Put into property file
		Process p = Runtime.getRuntime().exec("which lola");
		BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		//asume there is a line
		return br.readLine();
	}

}
