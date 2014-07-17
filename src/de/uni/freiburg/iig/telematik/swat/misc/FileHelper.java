package de.uni.freiburg.iig.telematik.swat.misc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.charset.Charset;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import de.invation.code.toval.file.FileReader;
import de.uni.freiburg.iig.telematik.swat.misc.OperatingSystem.OperatingSystems;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeView;

public class FileHelper {

	public static void main (String[] args) {
		//String[] command = { "/bin/sh", "-c", "echo hallo > /tmp/test.txt" };
		//runCommand(command);
		//final JOptionPane optionPane = new JOptionPane("run " + "bla", JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
		int i = JOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(SwatTreeView.getInstance()), "really delete?", "run "
				+ "bla bla bla", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		System.out.println(i);
	}

	public static void copyFile(File source, File destination) throws IOException {
		FileReader fr = new FileReader(source.getAbsolutePath());
		//BufferedWriter bw = new BufferedWriter(new FileOutputStream(destination));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destination)));

		String buffer;
		while ((buffer = fr.readLine()) != null) {
			bw.write(buffer);
			bw.write("\r\n");
		}
		fr.closeFile();
	}

	/** efficiently get number of lines in a file **/
	public static long getLinesCount(String fileName, String encodingName) {
		long linesCount = 0;
		File file = new File(fileName);
		FileInputStream fileIn = null;
		try {
			fileIn = new FileInputStream(file);
			Charset encoding = Charset.forName(encodingName);
			Reader fileReader = new InputStreamReader(fileIn, encoding);
			int bufferSize = 4096;
			Reader reader = new BufferedReader(fileReader, bufferSize);
			char[] buffer = new char[bufferSize];
			int prevChar = -1;
			int readCount = reader.read(buffer);
			while (readCount != -1) {
				for (int i = 0; i < readCount; i++) {
					int nextChar = buffer[i];
					switch (nextChar) {
					case '\r': {
						// The current line is terminated by a carriage return
						// or by a carriage return immediately followed by a
						// line feed.
						linesCount++;
						break;
					}
					case '\n': {
						if (prevChar == '\r') {
							// The current line is terminated by a carriage
							// return immediately followed by a line feed.
							// The line has already been counted.
						} else {
							// The current line is terminated by a line feed.
							linesCount++;
						}
						break;
					}
					}
					prevChar = nextChar;
				}
				readCount = reader.read(buffer);
			}
			if (prevChar != -1) {
				switch (prevChar) {
				case '\r':
				case '\n': {
					// The last line is terminated by a line terminator.
					// The last line has already been counted.
					break;
				}
				default: {
					// The last line is terminated by end-of-file.
					linesCount++;
				}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				fileIn.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return linesCount;
	}

	public static boolean removeLinkOnly(File file) {
		if (file == null)
			return false;

		OperatingSystems os = OperatingSystem.getOperatingSystem();

		String[] command = new String[3];
		String path = file.getPath();
		switch (os) {
		case win:
			command[0] = "cmd";
			command[1] = "/C";
			command[2] = "del \"" + path + "\"";
			break;
		case mac:
			command[0] = "/bin/sh";
			command[1] = "-c";
			command[2] = "rm \"" + path + "\"";
			break;
		default:
			command[0] = "/bin/sh";
			command[1] = "-c";
			command[2] = "rm \"" + path + "\"";
			break;
		}

		//command = "/bin/sh -c echo hallo > /tmp/test.txt";
		String readableCommand = "run command: " + " " + command[0] + " " + command[1] + " " + command[2];
		//final JOptionPane optionPane = new JOptionPane("run " + readableCommand, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
		//int i = JOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(SwatTreeView.getInstance()), "run " + readableCommand);
		int i = JOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(SwatTreeView.getInstance()), readableCommand, "delete?",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		System.out.println("USER: " + i);
		if (i == 0) {
			//user choice YES
			runCommand(command);
			return true;
		}
		return false;

	}

	static void runCommand(String[] command) {
		try {
			Process p = Runtime.getRuntime().exec(command);
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader err = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			String output = null;
			while ((output = in.readLine()) != null) {
				System.out.println(output);
			}
			output = null;
			while ((output = err.readLine()) != null) {
				System.out.println(output);
			}

			in.close();
			p.waitFor();
			System.out.println("ExitValue: " + p.exitValue());
			//			System.out.println(p.getInputStream().read());
			//			System.out.println(p.getErrorStream().read());
			p.exitValue();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
