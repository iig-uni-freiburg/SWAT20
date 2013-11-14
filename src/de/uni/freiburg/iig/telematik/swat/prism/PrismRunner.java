package de.uni.freiburg.iig.telematik.swat.prism;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.swat.prism.searcher.PrismSearcherFactory;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

/**
 * Starts (and finds) Prism Model Checker. Use {@link #execPrism}
 **/
public class PrismRunner {
	
	private File prismPath;

	/**
	 * Creates a new PrismRunner.
	 * @throws ParameterException If the prism path is neither stores nor cannot be determined automatically.
	 */
	public PrismRunner() throws ParameterException {
		String prismPath = null;
		// Check if prism path is set as property.
		try {
			prismPath = SwatProperties.getInstance().getPrismPath();
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
			prismPath = searchedPrismPath.getAbsolutePath();
		}
		this.prismPath = new File(prismPath);
	}

	/**
	 * Run Prism Model Checker with given Path to model and one or more properties
	 * @throws ParameterException 
	 * @throws IOException 
	 **/
	public String execPrism(String modelPath, String... propertyPath) throws ParameterException, IOException {
		Validate.directory(modelPath);
		Validate.notNull(propertyPath);
		Validate.notEmpty(propertyPath);
		
		// TODO: make it threaded?
		// prepare property String
		String propertyString = generatePropertyString(propertyPath);
		Process p = null;

		try {
			// Start Prism
			Runtime rt = Runtime.getRuntime();
			p = rt.exec(prismPath.toString() + " " + modelPath + propertyString);
			// Read in- and outputstream
			InputStream in = p.getInputStream();
//			OutputStream out = p.getOutputStream();
//			InputStream err = p.getErrorStream();

			// get result
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String buffer = "";
			StringBuilder stringBuilder = new StringBuilder();
			while ((buffer = br.readLine()) != null) {
				stringBuilder.append(buffer);
			}
			String result = stringBuilder.toString();
			p.destroy();
			return result;

		} catch (IOException e) {
//			log.log(Level.SEVERE, "Could not start PRISM Model Checker");
			throw e;
		} finally {
			try {
				p.destroy();
			} catch (NullPointerException e) {}
		}
	}

	private String generatePropertyString(String[] propertyPaths) throws ParameterException {
		StringBuilder sb = new StringBuilder(100);
		for (String propertyPath: propertyPaths) {
			Validate.directory(propertyPath);
			sb.append(" ");
			sb.append(propertyPath);
		}
		return sb.toString();
	}

}