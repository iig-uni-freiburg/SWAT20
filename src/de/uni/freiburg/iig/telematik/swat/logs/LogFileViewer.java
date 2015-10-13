package de.uni.freiburg.iig.telematik.swat.logs;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogReader;

import de.invation.code.toval.file.FileUtils;
import de.invation.code.toval.file.MonitoredInputStream;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sewol.parser.ParsingMode;
import de.uni.freiburg.iig.telematik.sewol.parser.mxml.MXMLLogParser;
import de.uni.freiburg.iig.telematik.sewol.parser.xes.XESLogParser;
import de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.sciff.AristaFlowParser;
import de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.sciff.AristaFlowParser.whichTimestamp;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.plugin.sciff.LogParserAdapter;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.action.SciffAnalyzeAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.ViewComponent;


/**
 * presents view on mxml files. extends {@link JEditorPane}, implements
 * {@link ViewComponent}
 **/
public class LogFileViewer extends JScrollPane implements ViewComponent {
	private static final long serialVersionUID = 7051631037013916120L;
	private JComponent properties = null;
	private JButton analyzeButton = null;
	private JComponent mainComponent;
	private JProgressBar bar = new JProgressBar();

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
			mainComponent=getEditorField();
			getViewport().add(mainComponent);
			mainComponent.revalidate();
			mainComponent.repaint();
		} catch (MalformedURLException e) {
			Workbench.errorMessage("Could not generate Log viewer, URL malformed", e, true);
		} catch (IOException e) {
			Workbench.errorMessage("Could not generate Log viewer, I/O Error", e, true);
		}
		return this;
	}
	

	private JComponent getEditorField() throws MalformedURLException, IOException {
		if(model.getFileReference().length()>2097152l){
			//do not show editor
			JLabel label=new JLabel("file too big to dislpay - analysis is possible but might run into performance problems");
			label.setPreferredSize(new Dimension(200, 100));
			label.setSize(200, 100);
			return label;
		} else {
			JEditorPane editor = new JEditorPane(model.getFileReference().toURI().toURL());
			editor.setEditable(false);
			return editor;
		}
		
	}

	public JButton getSciffButton() throws ParameterException, PropertyException, IOException {
		if (analyzeButton == null) {
			analyzeButton = new JButton("Analyze with SCIFF");
			ImageIcon icon;
				icon = IconFactory.getIcon("search");
				analyzeButton.setIcon(icon);

			analyzeButton.addActionListener(new SciffAnalyzeAction(model.getFileReference()));
		}
		return analyzeButton;
	}

	@Override
	public JComponent getPropertiesView() {
		if (properties == null) {
                    try {
                        createPropertiesView();
                    } catch (IOException | ParameterException | PropertyException ex) {
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

	private void createPropertiesView() throws IOException, ParameterException, PropertyException {
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
	
	public ISciffLogReader loadLogReader() throws Exception{
		ISciffLogReader logReader = null;
		
		//remove MainComponent, add ProgressBar
		getViewport().removeAll();
		getViewport().add(bar);
		bar.setVisible(true);
		getViewport().revalidate();
		getViewport().repaint();
		revalidate();
		repaint();
		
		MonitoredInputStream mis = getInputStream();
		
		if(!getModel().hasLogReaderSet()){
			switch (getModel().getType()) {
			case Aristaflow:
				logReader = new AristaFlowParser(getFileReference());
				((AristaFlowParser)logReader).parse(whichTimestamp.BOTH);
				break;
			case MXML:
				MXMLLogParser mxmlParser = new MXMLLogParser();
				mxmlParser.parse(mis, ParsingMode.COMPLETE);
				logReader=new LogParserAdapter(mxmlParser);
				break;
			case XES:
				XESLogParser xesParser= new XESLogParser();
				xesParser.parse(getFileReference(), ParsingMode.COMPLETE);
				logReader=new LogParserAdapter(xesParser);
				break;
			default:
				break;
			}
			
			if(logReader!=null)
				getModel().setLogReader(logReader);	
		}
		
		//remove progress bar, add main Component
		getViewport().removeAll();
		getViewport().add(mainComponent);
		getViewport().revalidate();
		getViewport().repaint();
		revalidate();
		repaint();

		return getModel().getLogReader();
	}
	
	public File getFileReference(){
		return getModel().getFileReference();
	}
	
	private MonitoredInputStream getInputStream() throws FileNotFoundException{
		MonitoredInputStream mis = new MonitoredInputStream(new FileInputStream(getFileReference()), getFileReference().length(), 1024 * 1024 * 5);
		mis.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				
				SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					System.out.println("new progress "+mis.getProgress());
					bar.setVisible(true);
					bar.setValue(mis.getProgress());
					
				}
			});
				
				
			}
		});
		return mis;
	}



}
