package de.uni.freiburg.iig.telematik.swat.sciff;

import java.io.File;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponent;

public class LogFileViewer extends JEditorPane implements SwatComponent {
	private File file = null;
	JComponent properties = null;

	public LogFileViewer(File file) throws IOException {
		super(file.toURI().toURL());
		this.file = file;
		// editor = new JEditorPane(file.toURI().toURL());
		setEditable(false);
	}

	@Override
	public JComponent getMainComponent() {
		return this;
	}


	@Override
	public JComponent getPropertiesView() {
		if (properties == null) {
			properties = new JPanel();
			properties.add(new JLabel("Textual file"));
		}
		return properties;
	}

	public String getName() {
		return file.getName();
	}

	public String toString() {
		return file.getPath();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}


}
