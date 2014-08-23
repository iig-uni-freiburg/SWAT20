package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SpringLayout;
import javax.swing.tree.DefaultMutableTreeNode;

import org.processmining.analysis.sciffchecker.gui.util.SpringUtilities;

import com.thoughtworks.xstream.XStream;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.lukas.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.lukas.IOUtils;
import de.uni.freiburg.iig.telematik.swat.lukas.OperandType;
import de.uni.freiburg.iig.telematik.swat.lukas.ParamValue;
import de.uni.freiburg.iig.telematik.swat.lukas.Parameter;
import de.uni.freiburg.iig.telematik.swat.lukas.PatternFactory;
import de.uni.freiburg.iig.telematik.swat.lukas.PatternResult;
import de.uni.freiburg.iig.telematik.swat.lukas.PrismExecutor;
import de.uni.freiburg.iig.telematik.swat.lukas.PrismResult;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponent;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponentType;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeView;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeView.SwatTreeNode;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.MessageDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;
import de.uni.freiburg.iig.telematik.swat.logs.LogFileViewer;
/**
 * this class represents a panel to be added on the right side
 * 
 * @author bernhard
 * 
 */
public abstract class AnalyzePanel implements LoadSave {
	private JPanel content;
	private JPanel propertyPanel;
	private JLabel analysisName;
	private JButton editButton, runButton, saveButton;
	private String fileName;
	protected PatternWindow patternWindow;
	protected InformationReader objectInformationReader;
	protected PatternFactory patternFactory;
	protected List<PatternSetting> patternSettings;


	protected abstract String getCorrectValue(ParamValue val);
	protected abstract void addCounterExampleButton(PatternResult result, JPanel newPanel);
	protected abstract void analyze();
	
	public void objectChanged() {
		objectInformationReader.update();
		// update the parameter boxes
		patternWindow.netChanged();
	}

	public InformationReader getInformationReader() {
		return objectInformationReader;
	}

	public AnalyzePanel(SwatComponent component, String file) {
		fileName = file.split("[.]")[0];
		patternFactory = new PatternFactory(component);
		patternSettings = new ArrayList<PatternSetting>();
		initGui();

	}

	private void initGui() {
		content = new JPanel(new GridBagLayout());
		analysisName = new JLabel("Analysis from " + getDateShort());
		editButton = new JButton("Edit");
		runButton = new JButton("Run");
		saveButton = new JButton("Save");
		propertyPanel = new JPanel();
		propertyPanel
				.setLayout(new BoxLayout(propertyPanel, BoxLayout.Y_AXIS));
		Box box = Box.createVerticalBox();

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(editButton);
		buttonPanel.add(runButton);
		buttonPanel.add(saveButton);
		box.add(Helpers.jPanelLeft(analysisName));
		box.add(buttonPanel);

		box.add(Helpers.jPanelLeft(new JLabel("Patterns to Check: ")));
		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.add(propertyPanel, BorderLayout.NORTH);
		JScrollPane jsp = new JScrollPane(northPanel);
		jsp.setVisible(true);
		// jsp.setEnabled(true);
		// jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		content.setLayout(new BorderLayout());
		content.add(box, BorderLayout.NORTH);
		content.add(jsp, BorderLayout.CENTER);
		patternWindow = new PatternWindow(this, patternFactory);
		editButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				showPatternWindow();
			}
		});
		runButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				analyzedClicked();
			}
		});
		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				save();
			}
		});
	}
	
	protected void analyzedClicked() {
		if (patternWindow.isVisible()) {
			JOptionPane.showMessageDialog(null,
					"Close PatternWindow first, before analyzing", "Warning",
					JOptionPane.WARNING_MESSAGE);
			patternWindow.requestFocus();
			return;
		}
		analyze();
	}

	protected void showPatternWindow() {
		patternWindow.setPatternSettings(patternSettings);
		patternWindow.setVisible(true);
	}

	private String getDateShort() {
		Date today = new Date();
		DateFormat formatter = DateFormat.getDateInstance(
				DateFormat.SHORT,
				new Locale(System.getProperty("user.language"), System
						.getProperty("user.country")));
		return formatter.format(today);
	}

	/**
	 * the function invoked when Analyze is pressed
	 */
	
	public void getPatternSettingsFromPatternWindow() {
		List<PatternSetting> oldSettings = Helpers
				.copyPatternSettings(patternSettings);
		patternSettings = patternWindow.getPatternSettings();
		for (PatternSetting p : patternSettings) {
			// when there is a result, check whether this
			// setting had the same values before
			// because otherwise, the result is unclear
			if (p.getResult() != null) {
				for (PatternSetting po : oldSettings) {
					if (p.getName().equals(po.getName())) {
						if (p.equals(po) == false) {
							p.setResult(null);
						}
					}
				}
			}
		}
		update();
	}


	
	public void update() {
		propertyPanel.removeAll();
		for (PatternSetting p : patternSettings) {
			//System.out.println(p);
			// check if the setting was changed

			JPanel newPanel = new JPanel();
			newPanel.setLayout(new BoxLayout(newPanel, BoxLayout.Y_AXIS));
			List<JLabel> labels = getLabelListForPatternSetting(p);
			for (JLabel label : labels) {
				newPanel.add(Helpers.jPanelLeft(label));
			}
			JPanel resultPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			resultPanel.add(new JLabel("Result: "));
			// check whether a result exists
			PatternResult result = p.getResult();
			if (result != null) {
				try {
					if (result.isFulfilled()) {
						resultPanel.add(new JLabel(IconFactory
								.getIcon("result_valid")));
					} else {
						resultPanel.add(new JLabel(IconFactory
								.getIcon("result_false")));
					}
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
				newPanel.add(resultPanel);
				DecimalFormat decimalFormat = new DecimalFormat("#.###");
				newPanel.add(Helpers.jPanelLeft(new JLabel("\tProbability: "
						+ decimalFormat.format(result.getProbability()))));
				addCounterExampleButton(result, newPanel);
				
			} else {
				try {
					resultPanel.add(new JLabel(IconFactory
							.getIcon("result_unknown")));
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
				newPanel.add(resultPanel);
			}
			newPanel.setBorder(BorderFactory.createTitledBorder(p.getName()));
			JPanel northPanel = new JPanel(new BorderLayout());
			northPanel.add(newPanel, BorderLayout.NORTH);
			propertyPanel.add(northPanel);
		}

		content.repaint();
		content.updateUI();
	}

	

	public void setAnalyseName(String text) {
		analysisName.setText(text);
	}

	public JPanel getContent() {
		return content;
	}

	public void setContent(JPanel panel) {
		this.content = panel;
	}

	public String getNetName() {
		return fileName;
	}

	public void setNetName(String netName) {
		this.fileName = netName.split("[.]")[0];
	}

	public boolean load(File f) {
		MessageDialog.getInstance().addMessage(
				"Loading Analysis Settings from " + f);
		patternSettings = AnalysisStore.loadFromFile(f);
		// System.out.println(newList);
		patternWindow.setPatternSettings(patternSettings);
		analysisName.setText("Analysis: "
				+ AnalysisStore.getDisplayNameforFilename(f.getName()));
		update();
		return true;
	}

	@Override
	public boolean save() {
		AnalysisStore.store(patternWindow.getPatternSettings(), fileName);
		// update the tree
		SwatTreeView.getInstance().updateAnalysis();
		SwatTreeView.getInstance().expandAll();
		SwatTreeView.getInstance().updateUI();
		return true;
	}
	/**
	 * create a list of JLabels that will later be added to an
	 * AnalyzePanel. Some parameters might be to long so they will
	 * be breaken off. a JLabel does not accept a newline character
	 * @param ps
	 * @param transitionsReverse
	 * @return
	 */
	public List<JLabel> getLabelListForPatternSetting(PatternSetting ps) {
		ArrayList<JLabel> labels=new ArrayList<JLabel>();
		for(Parameter para: ps.getParameters()) {
			String paraString=para.getName()+": ";
			int count=0;
			boolean labelsLeft=false;
			// display the values

			for(int i=0; i < para.getValue().size(); i++) {
				ParamValue val=para.getValue().get(i);
				labelsLeft=true;
				if(count > 0) {
					labels.add(new JLabel(paraString));
					// move it to the right
					paraString="    ";
					count=0;
					labelsLeft=false;
				}
				if(val.getOperandType()==OperandType.STATEPREDICATE) {
					
					String conjunctions[]=val.getOperandName().split(" & ");
					int conjunction_count=0;
					for(int j=0; j < conjunctions.length; j++) {
						conjunction_count++;
						labelsLeft=true;
						paraString+=conjunctions[j];
						if(j < conjunctions.length -1) {
							paraString+=" & ";
						}
						if(conjunction_count==2) {
							labels.add(new JLabel(paraString));
							// move it to the right
							paraString="    ";
							conjunction_count=0;
							labelsLeft=false;
						}
					}
					
				} else {
					paraString+=getCorrectValue(val);
					count++;
				}
				if(i < para.getValue().size() -1) {
					paraString+=", ";
				}
				// maximum 2 values in a row
				if(count==2) {
					labels.add(new JLabel(paraString));
					// move it to the right
					paraString="    ";
					count=0;
					labelsLeft=false;
				}
			}
			if(labelsLeft) {
				labels.add(new JLabel(paraString));
			}
		}
		return labels;
	}

}
