package de.uni.freiburg.iig.telematik.swat.lola;

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
import de.uni.freiburg.iig.telematik.swat.workbench.action.LolaAnalyzeAction;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;


/**
 * presents view on mxml files. extends {@link JEditorPane}, implements
 * {@link SwatComponent}
 **/
public class XMLFileViewer extends JEditorPane implements SwatComponent {
	private static final long serialVersionUID = 7051631037013916120L;
	private File file = null;
	JComponent properties = null;
	private JButton runWithLoLA = null;
	private static final String iconNameFormat = "../resources/icons/%s/%s-%s.png";
	private static int ICON_SIZE = 32;

	public XMLFileViewer(File file) throws IOException {
		// Initialize JEditorPane
		super(file.toURI().toURL());
		this.file = file;
		setEditable(false);

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

	public JButton getLolaButton() {
		if (runWithLoLA == null) {
			runWithLoLA = new JButton("Analyze with LoLA");
				ImageIcon icon = new ImageIcon(XMLFileViewer.this.getClass().getResource(
					String.format(iconNameFormat, ICON_SIZE, "search", ICON_SIZE)));
			runWithLoLA.setIcon(icon);
			runWithLoLA.addActionListener(new LolaAnalyzeAction(file));
		}
		return runWithLoLA;
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

	public File getFile() {
		return file;
	}

	private void createPropertiesView() {
		properties = new JPanel();
		properties.setLayout(new FlowLayout());
		properties.add(new JLabel("Textual file"));
		properties.add(new JLabel("Size: " + file.length() / 1024 + "kB"));
		properties.add(new JLabel("Lines: " + FileHelper.getLinesCount(file.getAbsolutePath(), Charset.defaultCharset().toString())));
		properties.add(getLolaButton());
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
