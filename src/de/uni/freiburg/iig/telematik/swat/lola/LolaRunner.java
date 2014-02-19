package de.uni.freiburg.iig.telematik.swat.lola;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;


public class LolaRunner {

	String lolaDir;
	private PTNet net;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LolaRunner runner = new LolaRunner();
		for (TESTS test : TESTS.values()) {
			System.out.println(test.toString() + ": " + runner.getExecString(test));
		}

	}

	public LolaRunner(PNEditor editor) {
		this((PTNet) editor.getNetContainer().getPetriNet());
	}

	public LolaRunner() {
		try {
			this.lolaDir = SwatProperties.getInstance().getLolaPath();
		} catch (PropertyException e) {
			JOptionPane.showMessageDialog(null, "LoLA Path not set", "Path error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (ParameterException e) {
			JOptionPane.showMessageDialog(null, "LoLA Path not set", "Path error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Cannot access LoLA path or path not set", "Path error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}

	}

	public LolaRunner(PTNet net) {
		this.net = net;
		//get defined lola path
		try {
			this.lolaDir = SwatProperties.getInstance().getLolaPath();
		} catch (PropertyException e) {
			JOptionPane.showMessageDialog(null, "LoLA Path not set", "Path error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (ParameterException e) {
			JOptionPane.showMessageDialog(null, "LoLA Path not set", "Path error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Cannot access LoLA path or path not set",
					"Path error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	public HashMap<TESTS, String> analyse() {
		HashMap<TESTS, String> result = new HashMap<LolaRunner.TESTS, String>();
		for (TESTS test : TESTS.values()) {
			try {
				result.put(test, exec(test));
			} catch (IOException e) {
				result.put(test, "ERROR");
				JOptionPane.showMessageDialog(null, "Could not run test: " + test, "Analyse error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
		return result;
	}

	private String exec(TESTS test) throws IOException {
		LolaTransformator lolaTransformator = new LolaTransformator(net);
		//StringBuilder list = new StringBuilder();
		ArrayList<String> list = new ArrayList<String>(5);
		Process p = Runtime.getRuntime().exec(getExecString(test));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
		BufferedReader reader = new BufferedReader(new InputStreamReader(p.getErrorStream()));

		//feed lola-process with PTnet through STDIN
		writer.write(lolaTransformator.getNetAsLolaFormat());
		writer.flush();
		writer.close();

		//get results
		String result;
		while ((result = reader.readLine()) != null) {
			list.add(result);
			System.out.println(result);
			//list.append("\r\n");
		}
		//return second last entry -> its the result
		if (test == TESTS.HOME)
			return list.get(list.size() - 1);
		return list.get(list.size() - 2);
	}

	public String getExecString(TESTS test) {
		//depending on the OS, return the execution string to run lola
		String OS = System.getProperty("os.name").toLowerCase();
		if (OS.contains("win"))
			return getExecStringWin(test);
		if (OS.contains("mac"))
			return getExecStringMac(test);
		if (OS.contains("nux") || OS.contains("sunos"))
			return getExecStringLinux(test);

		return null;

	}

	private String getExecStringLinux(TESTS test) {
		return new File(lolaDir, "lola-" + test.toString().toLowerCase()).getAbsolutePath();
	}

	private String getExecStringMac(TESTS test) {
		return new File(lolaDir, "lola-" + test.toString().toLowerCase()).getAbsolutePath();
	}

	private String getExecStringWin(TESTS test) {
		return new File(lolaDir, "lola-" + test.toString().toLowerCase() + ".exe").getAbsolutePath();
	}

	public enum TESTS {
		BOUNDEDNET, DEADLOCK, HOME
	}
}
