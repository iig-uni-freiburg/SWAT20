package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.lukas.Operand;
import de.uni.freiburg.iig.telematik.swat.lukas.OperandType;
import de.uni.freiburg.iig.telematik.swat.lukas.ParamValue;
import de.uni.freiburg.iig.telematik.swat.lukas.Parameter;
import de.uni.freiburg.iig.telematik.swat.lukas.PatternFactory;
import de.uni.freiburg.iig.telematik.swat.lukas.PatternResult;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponent;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeView;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.MessageDialog;
/**
 * This class represents the Pattern-Overview. It its an
 * abstract class. The functionality between the analysis of Petri nets
 * and logfiles differs in some ways. Therefore the abstract functions
 * must be implemented.
 * 
 * @author bernhard
 * 
 */
public abstract class AnalyzePanel {
	
	private JPanel content;
	private JPanel propertyPanel;
	private JLabel analysisName;
	private JButton editButton, runButton, saveButton;
	private String fileName;
	protected PatternWizard patternWizard;
	protected LogFileReader objectInformationReader;
	protected PatternFactory patternFactory;
	protected List<PatternSetting> patternSettings;
	private PNEditor mPNEditor;

	/**
	 * this method adjusts values to be displayed. E.g. display
	 * the label of a transition and not its name
	 * @param val the ParamValue to be adjusted
	 * @return a String with the adjusted value
	 */
	// 
	protected abstract String adjustValue(ParamValue val);
	/**
	 * add a Button to the newPanel Panel to visualize a Counterexample
	 * the functionality of such a button differs depending
	 * on the object beeing analyzed
	 * @param result the PatternResult containing the information like violating
	 * path or traces
	 * @param newPanel the panel to which the button should be added
	 */
	protected abstract void addCounterExampleButton(PatternResult result, JPanel newPanel);
	/**
	 * is called when the user presses the Run Button
	 * The funcionality differs depending on the object beeing
	 * analyzed. PRISMExecutor or SCIFFExecutor is used for analysis.
	 */
	protected abstract void analyze();
	
	/**
	 * should be invoked when the analyzed object was changed
	 */
	public void objectChanged() {
		objectInformationReader.update();
		// update the parameter boxes
		patternWizard.netChanged();
	}

	public LogFileReader getInformationReader() {
		return objectInformationReader;
	}
	/**
	 * Create an AnalyzePanel for a given SwatComponent and a given
	 * filename
	 * @param component the SwatComponent to which this AnalyzePanel should belong to
	 * @param file the filename of the file being analyzed
	 */
	public AnalyzePanel(SwatComponent component, String file) {
		fileName = file.split("[.]")[0];
		patternFactory = new PatternFactory(component);
		patternSettings = new ArrayList<PatternSetting>();
		mPNEditor = (PNEditor) component;
		initGui();

	}
	/**
	 * initialize the GUI
	 */
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
		patternWizard = new PatternWizard(this, patternFactory);
		editButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				showPatternWizard();
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
	/**
	 * invoked when the user presses the Run Button
	 */
	protected void analyzedClicked() {
		if (patternWizard.isVisible()) {
			JOptionPane.showMessageDialog(null,
					"Close Pattern Wizard first, before analyzing", "Warning",
					JOptionPane.WARNING_MESSAGE);
			patternWizard.requestFocus();
			return;
		}
		// run the abstract method which has to be implemented by subclasses
		analyze();
	}
/**
 * display the Pattern Wizard
 */
	protected void showPatternWizard() {
		patternWizard.setPatternSettings(patternSettings);
		Helpers.centerWindow(patternWizard);
		patternWizard.setVisible(true);
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
	 * load the Pattern Settings from the Pattern Wizard
	 * pay attention to settings that did not change values
	 * their results have to be kept, the others are set to null
	 */
	
	public void getPatternSettingsFromPatternWindow() {
		
		List<PatternSetting> oldSettings = Helpers
				.copyPatternSettings(patternSettings);
		patternSettings = patternWizard.getPatternSettings();
		for (PatternSetting p : patternSettings) {
			boolean patternInOld=false;
			// when there is a result, check whether this
			// setting had the same values before
			// because otherwise, the result is unclear
			if (p.getResult() != null) {
				for (PatternSetting po : oldSettings) {
					if (p.getName().equals(po.getName())) {
						if (p.equals(po) == true) {
							patternInOld=true;
						}
					}
				}
			}
			// if setting is not in the old settings than delete result
			if(patternInOld==false) {
				p.setResult(null);
			}
		}
		update();
		
	}
	
	protected void showDetailsButton(InformationFlowPatternSetting p, JPanel newPanel) {
		
		final ArrayList<PatternResult> resutls = p.getResults();
		final InformationFlowPatternSetting patternSetting = p;
		JButton detailsButton = new JButton("Details");
		detailsButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				 
		        showDetailsDialog(resutls, patternSetting);
			}

			private void showDetailsDialog(ArrayList<PatternResult> resutls, 
					InformationFlowPatternSetting patternSetting) {
				
				JDialog dialog = new JDialog();
				JPanel content = new JPanel();
				content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
				content.setBorder(BorderFactory.createTitledBorder(patternSetting.getName()));
				dialog.setTitle("Analysis Details");
				dialog.setContentPane(content);
				dialog.setModal(true);
				
				for (final PatternResult res : resutls) {
					
					if (res.equals(patternSetting.getResult())) {
						continue;
					}
					
					final JPanel panel = new JPanel();
					panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
					final ArrayList<Operand> operands = res.getPatternOperands();
					double prob = res.getProbability();
					String patternDesc = "Pattern:";
					for (int i=0; i<operands.size(); i++) {
						Operand op = operands.get(i);
						if (i == 0) {
							patternDesc += " " + op.getName();
						} else {
							patternDesc += ", " + op.getName();
						}
					}
					panel.add(new JLabel(patternDesc));
					panel.add(new JLabel("Prob: " + prob));
					panel.setBorder(BorderFactory.createEtchedBorder());
					panel.addMouseListener(new MouseAdapter() {
						
						GraphHighlighter mHighlighter = 
								new GraphHighlighter(mPNEditor, operands);
						
						@Override
				        public void mouseEntered(MouseEvent e) {
							panel.setBackground(Color.RED);
							mHighlighter.highlightOn();

				        }

						@Override
				        public void mouseExited(MouseEvent e) {
				        	panel.setBackground(UIManager.getColor ("Panel.background"));
				        	mHighlighter.highlightOff();
				        }
					});
					
					content.add(panel, BorderLayout.SOUTH);
					
				}
				
				dialog.pack();
				dialog.setVisible(true);
				
			}
		});
		
		newPanel.add(Helpers.jPanelLeft(detailsButton));
		
	}	

	// add pattern info to the analyze panel
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
				double prob = result.getProbability();
				newPanel.add(Helpers.jPanelLeft(new JLabel("\tProbability: "
						+ decimalFormat.format(prob))));
				addCounterExampleButton(result, newPanel);
				if (p instanceof InformationFlowPatternSetting && p.getResult().getProbability() != 0) {
					showDetailsButton((InformationFlowPatternSetting) p,
							newPanel);
				}
				
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

	
	/**
	 * changed the name of the current analysis
	 * @param text the new name to be displayed
	 */
	public void setAnalyseName(String text) {
		analysisName.setText("Analysis: "+text);
	}
	/**
	 * return the Content of the Panel
	 * @return a JPanel that can be added to another component
	 */
	public JPanel getContent() {
		return content;
	}

	public String getNetName() {
		return fileName;
	}

	public void setNetName(String netName) {
		this.fileName = netName.split("[.]")[0];
	}
	/**
	 * load the analysis of File f
	 */
	public boolean load(File f) {
		MessageDialog.getInstance().addMessage(
				"Loading Analysis Settings from " + f);
		patternSettings = AnalysisStorage.loadFromFile(f);
		// System.out.println(newList);
		patternWizard.setPatternSettings(patternSettings);
		analysisName.setText("Analysis: "
				+ AnalysisStorage.getDisplayNameforFilename(f.getName()));
		update();
		return true;
	}
	
	/**
	 * open a save dialog and save the analysis
	 */

	public void save() {
		String n =AnalysisStorage.store(patternWizard.getPatternSettings(), fileName);
		if(n != null)
			setAnalyseName(n);
		// update the tree
		SwatTreeView.getInstance().updateAnalysis();
		SwatTreeView.getInstance().expandAll();
		SwatTreeView.getInstance().updateUI();
	}
	/**
	 * create a list of JLabels that will later be added to an
	 * AnalyzePanel. Some parameters might be to long so they will
	 * be breaken off. a JLabel does not accept a newline character
	 * @param ps the PatternSetting object
	 * @return a list of JLabels that can be added to the AnalyzePanel
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
					paraString+=adjustValue(val);
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
