package de.uni.freiburg.iig.telematik.swat.lola;

import java.awt.Dimension;
import java.awt.ScrollPane;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.invation.code.toval.file.FileUtils;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.prism.searcher.PrismSearcherFactory;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;


public class LolaRunner {

	File lolaPath;
	File uml2owfnPath;
	File analyzeFile;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			LolaRunner test = new LolaRunner(new File("/home/richard/service-tech.xml"));
			//test.transormToLolaFormat(new File("/home/richard/service-tech.xml"));
			String result = test.analyze();
			System.out.println(result);
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public LolaRunner(File analyzeFile) throws ParameterException {
		//String lolaPath = null;
			// Check if prism path is set as property.
		this.analyzeFile = analyzeFile;
			try {
			lolaPath = new File(getLolaPath());
			uml2owfnPath = new File(getUml2OWfnPath());
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
			//lolaPath = searchedPrismPath.getAbsolutePath();
			}
		//this.lolaPath = new File(lolaPath);
		System.out.println("lola Path is: " + this.lolaPath);
		System.out.println("uml2ofwn Path is: " + this.uml2owfnPath);
		
	}
	
	public String analyze() throws IOException, InterruptedException {
		transormToLolaFormat(analyzeFile);
		//Process p = Runtime.getRuntime().exec(lolaPath.getAbsolutePath());

		StringBuilder builder = new StringBuilder();

		//run lola
		File tmp = new File(FileUtils.getTempDir());
		FileNameExtensionFilter filter = new FileNameExtensionFilter("xml", "task");
		for (File task : tmp.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith("task");
			}
		})) {
			System.out.println("Task: " + task.toString());
			for (File lolaType : getLolaNets(new File(FileUtils.getTempDir()))) {
				System.out.println("Net: " + lolaType);
				String execute = "lola " + lolaType.getAbsolutePath() + " -a " + task.getAbsolutePath();
				System.out.println("executing: " + execute);
				Process p = Runtime.getRuntime().exec(execute);
				BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
				String result = null;
				while ((result = br.readLine()) != null) {
					builder.append(result);
					builder.append("\n");
					System.out.println(result);
				}

			}


		}
		createWindow(builder.toString());

		return builder.toString();
	}

	private void createWindow(final String string) throws IOException {
		FileWriter writer = new FileWriter(new File("/tmp/result.out"));
		writer.write(string);
		writer.close();
		final JFrame result = new JFrame();
		result.setSize(new Dimension(600, 600));
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.add(new JEditorPane(new File("/tmp/result.out").toURI().toURL()));
		result.add(scrollPane);
		result.setVisible(true);
		result.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent arg0) {
				result.dispose();
				
			}
			
			@Override
			public void windowClosing(WindowEvent arg0) {
				result.dispose();
				
			}
			
			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});

	}

	private String getLolaPath() throws IOException {
		//HACK: Only for Linux. Hardcoded. Put into property file
		Process p = Runtime.getRuntime().exec("which lola");
		BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		//asume there is a line
		try {
			p.waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return br.readLine();
	}

	private String getUml2OWfnPath() throws IOException {
		//HACK: Only for Linux. Hardcoded. Put into property file
		Process p = Runtime.getRuntime().exec("which uml2owfn");
		BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		//asume there is a line
		try {
			p.waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return br.readLine();
	}

	public void transormToLolaFormat(File xmlFile) throws IOException, InterruptedException {
		//Transform xml to lola-Format within tmp dir
		File tmpDir = new File(FileUtils.getTempDir());
		File tempFile = new File(tmpDir, analyzeFile.getName());
		copyFileUsingStream(analyzeFile, tempFile);
		String execCommand = uml2owfnPath.getAbsolutePath() + " " + "-i " + tempFile.getAbsolutePath()
				+ " -f lola -a soundness -a safe -a orJoin -p ctl -o";
		System.out.println("Running: " + execCommand);
		Process p = Runtime.getRuntime().exec(execCommand, null, new File("/tmp/"));
		//		p.getErrorStream();
		//		p.getOutputStream();
		//		p.getInputStream();
		p.waitFor();
		BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String output = null;
		while ((output = br.readLine()) != null) {
			System.out.println(output);
		}
	}

	private static void copyFileUsingStream(File source, File dest) throws IOException {
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(source);
			os = new FileOutputStream(dest);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
		} finally {
			is.close();
			os.close();
		}
	}

	private File[] getLolaNets(File dir) {
		File[] files = dir.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				// TODO Auto-generated method stub
				return name.endsWith("lola");
			}
		});
		return files;
	}


}
