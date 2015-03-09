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
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;


public class LolaRunner {

	private final String confirm = "Net is not bounded. Continue? \n\r This might take a long time";
	private final String unknown = "Boundedness of net is unknown. Continue? \n\r This might take a long time";
	String lolaDir;
	private LolaTransformator lolaTransformator;

	//private PTNet net;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LolaRunner runner = new LolaRunner();
		for (LOLA_TEST test : LOLA_TEST.values()) {
			System.out.println(test.toString() + ": " + runner.getExecString(test));
		}

	}

	public LolaRunner(PNEditorComponent editor) {
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
		//this.net = net;
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

	public void changePTNet(PTNet net) {
		this.lolaTransformator = new LolaTransformator(net);
	}

	public void changePTNet(PNEditorComponent editor) {
		this.lolaTransformator = new LolaTransformator(editor);
	}

	public HashMap<LOLA_TEST, String> analyse() {
		//Test for boundedness

		if (!userContinueIfUnbound())
			return null;

		HashMap<LOLA_TEST, String> result = new HashMap<LolaRunner.LOLA_TEST, String>();
		for (LOLA_TEST test : LOLA_TEST.values()) {
			try {
				Long start = System.currentTimeMillis();
				//result.put(test, execExitCode(test) + " (" + (System.currentTimeMillis() - start) + "ms)");
				result.put(test, exec(test) + " (" + (System.currentTimeMillis() - start) + "ms)");
			} catch (IOException e) {
				result.put(test, "ERROR");
				//JOptionPane.showMessageDialog(null, "Could not run test: " + test, "Analyse error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
		return result;
	}

	/** If true user wants analysis even on unbounded net **/
	private boolean userContinueIfUnbound() {
		try {
			if (!isBounded()) {
				int result = JOptionPane.showConfirmDialog(null, confirm, "WARNING", JOptionPane.YES_NO_OPTION);
				if (result != JOptionPane.YES_OPTION)
					return false;

			}
		} catch (IOException e1) {
			int result = JOptionPane.showConfirmDialog(null, unknown, "WARNING", JOptionPane.YES_NO_OPTION);
			if (result != JOptionPane.YES_OPTION)
				return false;
		}
		return true;
	}

	public boolean isBounded() throws IOException {
		//return !exec(LOLA_TEST.BOUNDEDNET).toLowerCase().contains("unbound");
		return execWithExitCode(LOLA_TEST.BOUNDEDNET);
	}

	private boolean execWithExitCode(LOLA_TEST test) throws IOException {

		//StringBuilder list = new StringBuilder();
		Process p = Runtime.getRuntime().exec(getExecString(test));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));

		//feed lola-process with PTnet through STDIN
		writer.write(lolaTransformator.getNetAsLolaFormat());
		writer.flush();
		writer.close();

		//get exit code
		try {
			p.waitFor();
			System.out.println("Exit code for " + test + ": " + p.exitValue());
			return interpretResult(test, p.exitValue());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	private boolean interpretResult(LOLA_TEST test, int exitCode) {
		switch (test) {
		case BOUNDEDNET:
			return exitCode == 1;
		case DEADLOCK:
			return exitCode == 1;
		case HOME:
			return exitCode == 0;
		case ONEBOUNDED:
			return exitCode == 1; //exit code always 0... can't do.
		default:
			return false;
		}
	}

	private String exec(LOLA_TEST test) throws IOException {

		//StringBuilder list = new StringBuilder();
		Process p = Runtime.getRuntime().exec(getExecString(test));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
		BufferedReader reader;
		//One-Bound test writes on STDOUT. On other tests LoLA uses ERROUT
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
		ArrayList<String> list = new ArrayList<String>(5); //Store result

		while ((result = reader.readLine()) != null) {
			list.add(result);
			//System.out.println(result);
		}
		reader.close();

		//get exit code. Available AFTER InputStream is read.
		try {
			p.waitFor();
		} catch (InterruptedException e) {
		}

		System.out.println("Exit code for " + test + ": " + p.exitValue());

		if (test == LOLA_TEST.HOME)
			return list.get(list.size() - 1); //use second last entry for HOME marking -> its the result
		if (test == LOLA_TEST.ONEBOUNDED)
			return testOneBoundedResult(list); // Output for ONE-Bounded test is tricky
		return list.get(list.size() - 2); //use this index as result for the remaining tests
	}

	private String testOneBoundedResult(ArrayList<String> list) {
		for (String s : list) {
			if (s.contains("BAD: 1"))
				return "Net is NOT 1-bounded";
		}
		return "Net IS 1-bounded";
	}

	public boolean isOneBounded(){
		try {
			String result = exec(LOLA_TEST.ONEBOUNDED);
			if (result == "Net IS 1-bounded")
				return true;
			return false;
		} catch (IOException e) {
			return false;
		}
	}

	private String getExecString(LOLA_TEST test) {
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
