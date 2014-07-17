package de.uni.freiburg.iig.telematik.swat.logs;

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
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;


/**
 * presents view on mxml files. extends {@link JEditorPane}, implements
 * {@link SwatComponent}
 **/
public class XMLFileViewer extends JEditorPane implements SwatComponent {
	private static final long serialVersionUID = 7051631037013916120L;
	JComponent properties = null;
	private JButton runWithLoLA = null;
	private static final String iconNameFormat = "../resources/icons/%s/%s-%s.png";
	private static int ICON_SIZE = 32;
	private LogModel model;

	public LogModel getModel() {
		return model;
	}

	public void setModel(LogModel model) {
		this.model = model;
	}

	public XMLFileViewer(File file) throws IOException {
		// Initialize JEditorPane
		super(file.toURI().toURL());

		model = new LogModel(file);
		setEditable(false);

		// get icon size
		try {
			ICON_SIZE = SwatProperties.getInstance().getIconSize().getSize();
		} catch (PropertyException e) {
			// Stay with default value
		}
		//SCIFFChecker test = new SCIFFChecker();
	}

	public XMLFileViewer(LogModel model) throws IOException {
		this(model.getFileReference());
		this.model = model;
	}

	@Override
	public JComponent getMainComponent() {
		return this;
	}

	public JButton getLolaButton() {
		if (runWithLoLA == null) {
			runWithLoLA = new JButton("Analyze with LoLA");
				ImageIcon icon = new ImageIcon(XMLFileViewer.this.getClass().getResource(
					String.format(iconNameFormat, ICON_SIZE, "detective", ICON_SIZE)));
			runWithLoLA.setIcon(icon);
			//runWithLoLA.addActionListener(new LolaAnalyzeAction(file));
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
		//for Nimbus LookAndFeel
		if (model == null)
			return "null";
		return model.getName();
	}

	public String toString() {
		return model.getName();
	}

	public File getFile() {
		return model.getFileReference();
	}

	private void createPropertiesView() {
		properties = new JPanel();
		properties.setLayout(new FlowLayout());
		properties.add(new JLabel("Textual file"));
		properties.add(new JLabel("Size: " + model.getFileReference().length() / 1024 + "kB"));
		properties.add(new JLabel("Lines: "
				+ FileHelper.getLinesCount(model.getFileReference().getAbsolutePath(), Charset.defaultCharset().toString())));
		properties.add(getLolaButton());
	}

}
