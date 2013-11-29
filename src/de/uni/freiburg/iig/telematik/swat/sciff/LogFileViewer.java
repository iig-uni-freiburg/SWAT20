package de.uni.freiburg.iig.telematik.swat.sciff;

import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.invation.code.toval.properties.PropertyException;
import de.uni.freiburg.iig.telematik.swat.misc.FileHelper;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponent;
import de.uni.freiburg.iig.telematik.swat.workbench.action.SciffAnalyzeAction;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;


/**
 * presents view on mxml files. extends {@link JEditorPane}, implements
 * {@link SwatComponent}
 **/
public class LogFileViewer extends JEditorPane implements SwatComponent {
	private static final long serialVersionUID = 7051631037013916120L;
	private File file = null;
	JComponent properties = null;
	private JButton runWithSciff = null;
	private static final String iconNameFormat = "../resources/icons/%s/%s-%s.png";
	private static int ICON_SIZE = 32;

	public LogFileViewer(File file) throws IOException {
		// Initialize JEditorPane
		super(file.toURI().toURL());
		this.file = file;
		setEditable(false);

		// get icon size
		try {
			ICON_SIZE = SwatProperties.getInstance().getIconSize();
		} catch (PropertyException e) {
			// Stay with default value
		}
	}

	@Override
	public JComponent getMainComponent() {
		return this;
	}

	public JButton getSciffButton() {
		if (runWithSciff == null) {
			runWithSciff = new JButton("Analyze with SCIFF");
				ImageIcon icon = new ImageIcon(LogFileViewer.this.getClass().getResource(
					String.format(iconNameFormat, ICON_SIZE, "search", ICON_SIZE)));
			runWithSciff.setIcon(icon);
			runWithSciff.addActionListener(new SciffAnalyzeAction(file));
		}
		return runWithSciff;
	}


	@Override
	public JComponent getPropertiesView() {
		if (properties == null) {
			createPropertiesView();
		}
		return properties;
	}

	public String getName() {
		return file.getName();
	}

	public String toString() {
		return file.getPath();
	}

	private void createPropertiesView() {
		properties = new JPanel();
		properties.setLayout(new FlowLayout());
		properties.add(new JLabel("Textual file"));
		properties.add(new JLabel("Size: " + file.length() / 1024 + "kB"));
		properties.add(new JLabel("Lines: " + FileHelper.getLinesCount(file.getAbsolutePath(), Charset.defaultCharset().toString())));
		properties.add(getSciffButton());
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}


}