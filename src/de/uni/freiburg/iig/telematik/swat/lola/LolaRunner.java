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
	private LolaTransformator lolaTransformator;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LolaRunner runner = new LolaRunner();
		for (LOLA_TEST test : LOLA_TEST.values()) {
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
		this.lolaTransformator = new LolaTransformator(net);
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

	public HashMap<LOLA_TEST, String> analyse() {
		//Test for boundedness

		if (!userContinueIfUnbound())
			return null;

		HashMap<LOLA_TEST, String> result = new HashMap<LolaRunner.LOLA_TEST, String>();
		for (LOLA_TEST test : LOLA_TEST.values()) {
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

	/** If true user wants analysis even on unbounded net **/
	private boolean userContinueIfUnbound() {
		try {
			if (!bounded()) {
				int result = JOptionPane.showConfirmDialog(null, "Net is not bounded. Continue? \n\r This might take a long time",
						"WARNING", JOptionPane.YES_NO_OPTION);
				if (result != JOptionPane.YES_OPTION)
					return false;

			}
		} catch (IOException e1) {
			int result = JOptionPane.showConfirmDialog(null, "Boundedness of net is unknown. Continue? \n\r This might take a long time",
					"WARNING", JOptionPane.YES_NO_OPTION);
			if (result != JOptionPane.YES_OPTION)
				return false;
		}
		return true;
	}

	private boolean bounded() throws IOException {
		return !exec(LOLA_TEST.BOUNDEDNET).toLowerCase().contains("unbound");
	}

	private String exec(LOLA_TEST test) throws IOException {

		//StringBuilder list = new StringBuilder();
		ArrayList<String> list = new ArrayList<String>(5); //Store result
		Process p = Runtime.getRuntime().exec(getExecString(test));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
		BufferedReader reader;
		if (test == LOLA_TEST.ONEBOUNDED)
			reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		else
			reader = new BufferedReader(new InputStreamReader(p.getErrorStream()));

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
		if (test == LOLA_TEST.HOME)
			return list.get(list.size() - 1);
		if (test == LOLA_TEST.ONEBOUNDED)
			return testOneBoundedResult(list);
		return list.get(list.size() - 2);
	}

	private String testOneBoundedResult(ArrayList<String> list) {
		for (String s : list) {
			if (s.contains("BAD: 1"))
				return "Net is NOT 1-bounded";
		}
		return "Net IS 1-bounded";
	}

	public String getExecString(LOLA_TEST test) {
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

	private String getExecStringLinux(LOLA_TEST test) {
		if (test == LOLA_TEST.ONEBOUNDED) {
			return new File(lolaDir, "lola-boundedgraph").getAbsolutePath() + " --capacity=1 -M";
		}
		return new File(lolaDir, "lola-" + test.toString().toLowerCase()).getAbsolutePath();
	}

	private String getExecStringMac(LOLA_TEST test) {
		if (test == LOLA_TEST.ONEBOUNDED) {
			return new File(lolaDir, "lola-boundedgraph").getAbsolutePath() + " --capacity=1 -M";
		}
		return new File(lolaDir, "lola-" + test.toString().toLowerCase()).getAbsolutePath();
	}

	private String getExecStringWin(LOLA_TEST test) {
		if (test == LOLA_TEST.ONEBOUNDED) {
			return new File(lolaDir, "lola-boundedgraph.exe").getAbsolutePath() + " --capacity=1 -M";
		}
		return new File(lolaDir, "lola-" + test.toString().toLowerCase() + ".exe").getAbsolutePath();
	}

	public enum LOLA_TEST {
		BOUNDEDNET, DEADLOCK, HOME, ONEBOUNDED
	}
}
