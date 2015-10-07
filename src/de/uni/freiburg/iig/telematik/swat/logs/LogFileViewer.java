package de.uni.freiburg.iig.telematik.swat.logs;

import de.invation.code.toval.file.FileUtils;

import java.awt.Component;
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
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.action.SciffAnalyzeAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.ViewComponent;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * presents view on mxml files. extends {@link JEditorPane}, implements
 * {@link ViewComponent}
 **/
public class LogFileViewer extends JScrollPane implements ViewComponent {
	private static final long serialVersionUID = 7051631037013916120L;
	private JComponent properties = null;
	private JButton analyzeButton = null;

	private LogModel model;

	public LogFileViewer(LogModel model) throws Exception {
		setModel(model);
	}
	
	public LogModel getModel() {
		return model;
	}

	public void setModel(LogModel model) {
		this.model = model;
	}

	@Override
	public JComponent getMainComponent() {
		try {
//			JPanel pane = new JPanel();
//			pane.setLayout(new GridLayout());
			
			getViewport().add(getEditorField());
		} catch (MalformedURLException e) {
			Workbench.errorMessage("Could not generate Log viewer, URL malformed", e, true);
		} catch (IOException e) {
			Workbench.errorMessage("Could not generate Log viewer, I/O Error", e, true);
		}
		return this;
	}
	

	private Component getEditorField() throws MalformedURLException, IOException {
		if(model.getFileReference().length()>2097152l){
			//do not show editor
			JLabel label=new JLabel("file too big to dislpay - analysis is possible but might run into performance problems");
			return label;
		} else {
			JEditorPane editor = new JEditorPane(model.getFileReference().toURI().toURL());
			editor.setEditable(false);
			return editor;
		}
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
                    try {
                        createPropertiesView();
                    } catch (IOException ex) {
                        Workbench.errorMessage("Could not get properties", ex, true);
                    }
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

	private void createPropertiesView() throws IOException {
		properties = new JPanel();
		properties.setLayout(new FlowLayout());
		properties.add(new JLabel("Textual file"));
		properties.add(new JLabel("Size: " + model.getFileReference().length() / 1024 + "kB"));
		properties.add(new JLabel("Lines: "+ FileUtils.getLineCount(model.getFileReference().getAbsolutePath(), Charset.defaultCharset().toString())));
		properties.add(getSciffButton());
		properties.validate();
		properties.repaint();
	}

	public int hashCode() {
		return model.hashCode();
	}

}
