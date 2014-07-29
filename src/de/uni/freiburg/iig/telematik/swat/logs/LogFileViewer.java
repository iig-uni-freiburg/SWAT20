package de.uni.freiburg.iig.telematik.swat.logs;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
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
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.misc.FileHelper;
import de.uni.freiburg.iig.telematik.swat.sciff.presenter.LogSerializer;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponent;
import de.uni.freiburg.iig.telematik.swat.workbench.action.SciffAnalyzeAction;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;


/**
 * presents view on mxml files. extends {@link JEditorPane}, implements
 * {@link SwatComponent}
 **/
public class LogFileViewer extends JScrollPane implements SwatComponent {
	private static final long serialVersionUID = 7051631037013916120L;
	JComponent properties = null;
	private JButton analyzeButton = null;
	private static final String iconNameFormat = "../resources/icons/%s/%s-%s.png";
	private static int ICON_SIZE = 32;
	private LogModel model;

	//private JEditorPane editor;

	public LogModel getModel() {
		return model;
	}

	public void setModel(LogModel model) {
		this.model = model;
	}

	private LogFileViewer(File file) throws Exception {
		super();
		model = new LogModel(file);
		if (file.getName().endsWith("analysis")) {
			openAnalysisFile(file);
		} else {
			try {
				ICON_SIZE = SwatProperties.getInstance().getIconSize().getSize();
			} catch (PropertyException e) {
				// Stay with default value
			}
		}
	}

	public LogFileViewer(LogModel model) throws Exception {
		this(model.getFileReference());
		this.model = model;
	}

	private void openAnalysisFile(File file) throws Exception {
		LogFileViewer viewer = new LogFileViewer(LogSerializer.read(file).getLogFile());
	}

	@Override
	public JComponent getMainComponent() {
		try {
			JPanel pane = new JPanel();
			pane.setLayout(new GridLayout());
			JEditorPane editor = new JEditorPane(model.getFileReference().toURI().toURL());
			editor.setEditable(false);
			getViewport().add(editor);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}

	public JButton getSciffButton() {
		if (analyzeButton == null) {
			analyzeButton = new JButton("Analyze with SCIFF");
			ImageIcon icon;
			try {
				icon = IconFactory.getIcon("search");
				analyzeButton.setIcon(icon);
			} catch (ParameterException e) {
			} catch (PropertyException e) {
			} catch (IOException e) {
			}

			analyzeButton.addActionListener(new SciffAnalyzeAction(model.getFileReference()));
		}
		return analyzeButton;
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
		properties.add(getSciffButton());
		properties.validate();
		properties.repaint();
	}

}