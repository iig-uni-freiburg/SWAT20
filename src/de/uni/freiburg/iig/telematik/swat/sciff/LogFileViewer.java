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
import javax.swing.JScrollPane;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.misc.FileHelper;
import de.uni.freiburg.iig.telematik.swat.resources.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponent;
import de.uni.freiburg.iig.telematik.swat.workbench.action.SciffAnalyzeAction;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;


/**
 * presents view on mxml files. extends {@link JEditorPane}, implements
 * {@link SwatComponent}
 **/
public class LogFileViewer extends JScrollPane implements SwatComponent {
	private static final long serialVersionUID = 7051631037013916120L;
	private File file = null;
	JComponent properties = null;
	private JButton runWithSciff = null;
	private static final String iconNameFormat = "../resources/icons/%s/%s-%s.png";
	private static int ICON_SIZE = 32;
	private JEditorPane editor;

	public LogFileViewer(File file) throws IOException {
		super(new JEditorPane(file.toURI().toURL()));
		// Initialize JEditorPane
		editor = new JEditorPane(file.toURI().toURL());
		this.file = file;
		editor.setEditable(false);
		//this.add(editor);
		validate();

		// get icon size
		try {
			ICON_SIZE = SwatProperties.getInstance().getIconSize().getSize();
		} catch (PropertyException e) {
			// Stay with default value
		}
		//SCIFFChecker test = new SCIFFChecker();
	}

	@Override
	public JComponent getMainComponent() {
		return this;
	}

	public JButton getSciffButton() {
		if (runWithSciff == null) {
			runWithSciff = new JButton("Analyze with SCIFF");
			ImageIcon icon;
			try {
				icon = IconFactory.getIcon("search");
				runWithSciff.setIcon(icon);
			} catch (ParameterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (PropertyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//ImageIcon icon = new ImageIcon(LogFileViewer.this.getClass().getResource(
			//		String.format(iconNameFormat, ICON_SIZE, "search", ICON_SIZE)));


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
		//Wegen Nimbus LookaAndFeel
		if (file == null)
			return "null";
		return file.getName();
	}

	public String toString() {
		return file.getPath();
	}

	public File getFile() {
		return file;
	}

	private void createPropertiesView() {
		properties = new JPanel();
		properties.setLayout(new FlowLayout());
		properties.add(new JLabel("Textual file"));
		properties.add(new JLabel("Size: " + file.length() / 1024 + "kB"));
		properties.add(new JLabel("Lines: " + FileHelper.getLinesCount(file.getAbsolutePath(), Charset.defaultCharset().toString())));
		properties.add(getSciffButton());
		properties.validate();
		properties.repaint();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
