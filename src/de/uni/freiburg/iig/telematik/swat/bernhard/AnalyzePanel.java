package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import de.invation.code.toval.graphic.dialog.FileNameDialog;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.logs.LogFileViewer;
import de.uni.freiburg.iig.telematik.swat.lukas.modelchecker.adapter.PatternResult;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.factory.PatternFactory;
import de.uni.freiburg.iig.telematik.swat.workbench.Analysis;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.MessageDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.exception.SwatComponentException;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatComponentsListener;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.ViewComponent;
/**
 * This class represents the Pattern-Overview. It its an
 * abstract class. The functionality between the analysis of Petri nets
 * and logfiles differs in some ways. Therefore the abstract functions
 * must be implemented.
 * 
 * @author bernhard
 * 
 */
public abstract class AnalyzePanel implements SwatComponentsListener {
	
	protected JPanel content;
	protected JPanel propertyPanel;
	protected JLabel analysisName;
	private JButton editButton, runButton, saveButton;
	private String fileName;
	protected PatternWizard patternWizard;
	protected AnalysisComponentInfoProvider objectInformationReader;
	protected PatternFactory patternFactory;
	protected List<PatternSetting> patternSettings;
	protected PNEditorComponent mPNEditor;
	protected JComboBox dropDownMenu;
	/** Name of net/log this analysis belongs to **/
	protected String analysisSourceName;

	protected final String NEW_ANALYSIS = "New ...";

	/**
	 * this method adjusts values to be displayed. E.g. display
	 * the label of a transition and not its name
	 * @param val the ParamValue to be adjusted
	 * @return a String with the adjusted value
	 */
	// 
	protected abstract String adjustValue(GuiParamValue val);
	/**
	 * add a Button to the newPanel Panel to visualize a Counterexample
	 * the functionality of such a button differs depending
	 * on the object beeing analyzed
	 * @param result the PatternResult containing the information like violating
	 * path or traces
	 * @param newPanel the panel to which the button should be added
	 */
	protected abstract void addCounterExampleButton(PRISMPatternResult result, JPanel newPanel);
	/**
	 * is called when the user presses the Run Button
	 * The funcionality differs depending on the object beeing
	 * analyzed. PRISMExecutor or SCIFFExecutor is used for analysis.
	 */
	protected abstract void analyze();
	
	@Override
	/**add as SwatComponentListener**/
	public void analysisAdded(String netID, Analysis analysis) {
		if (netID.equalsIgnoreCase(analysisSourceName)) //react only on own analysis
			updateDropDownList(analysis.getName());
	}

	/**
	 * should be invoked when the analyzed object was changed
	 */
	public void objectChanged() {
		objectInformationReader.update();
		// update the parameter boxes
		patternWizard.netChanged();
	}

	public AnalysisComponentInfoProvider getInformationReader() {
		return objectInformationReader;
	}
	/**
	 * Create an AnalyzePanel for a given SwatComponent and a given
	 * filename
	 * @param component the SwatComponent to which this AnalyzePanel should belong to
	 * @param file the filename of the file being analyzed
	 */
	public AnalyzePanel(ViewComponent component, String file) {
		System.out.println("Creating Analyze Panel...");
		SwatComponents.getInstance().addSwatComponentListener(this);
		fileName = file.split("[.]")[0];
		//fileName = ((LogModel) component).getFileReference().getAbsolutePath();
		patternFactory = new PatternFactory(component);
		patternSettings = new ArrayList<PatternSetting>(); //TODO: with SwatComponent
		if (component instanceof PNEditorComponent) {
			mPNEditor = (PNEditorComponent) component;
			analysisSourceName = ((PNEditorComponent) component).getNetContainer().getPetriNet().getName();
		}
 else if (component instanceof LogFileViewer) {
			analysisSourceName = ((LogFileViewer) component).getName();
		}
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
		box.add(getDropDownMenu());
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

	protected JComboBox getDropDownMenu() {
		if(this.dropDownMenu==null){
			this.dropDownMenu=new JComboBox();
			dropDownMenu.addItem(NEW_ANALYSIS);
			try {
			for(Analysis analysis: SwatComponents.getInstance().getAnalyses(mPNEditor.getNetContainer().getPetriNet().getName())){
				this.dropDownMenu.addItem(analysis);
			}
			} catch (NullPointerException e) {
				//Nothing to add... continue
			}

			dropDownMenu.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED)
						reactOnDropDownChange();
				}
			});

		}

		return this.dropDownMenu;
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
					final ArrayList<PatternParameter> operands = res.getPatternOperands();
					double prob = res.getProbability();
					String patternDesc = "Pattern:";
					ArrayList<String> operandNames = new ArrayList<String>();
					for (PatternParameter op : operands) {
						operandNames.add(op.getName());
					}
					Collections.sort(operandNames);
					for (int i=0; i<operandNames.size(); i++) {
						String op = operandNames.get(i);
						if (i == 0) {
							patternDesc += " " + op;
						} else {
							patternDesc += ", " + op;
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
			PRISMPatternResult result = (PRISMPatternResult) p.getResult();
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
	
	/** update GUI according to selected item in DropDownMenu **/
	protected void reactOnDropDownChange() {
		if (getDropDownMenu().getSelectedItem() instanceof String) {
			analysisName.setText("new analysis...");
			patternSettings = new LinkedList<PatternSetting>();
			patternWizard.setPatternSettings(patternSettings);
			update();
		}

		try {
			Analysis selectedAnalysis = (Analysis) getDropDownMenu().getSelectedItem();
			patternSettings = selectedAnalysis.getPatternSetting();
			patternWizard.setPatternSettings(patternSettings);
			analysisName.setText(selectedAnalysis.getName());
			update();
		} catch (ClassCastException e) {
			//nothing to do here...
		}
	}

	/**
	 * open a save dialog and save the analysis
	 */
	public void save() {
		Object dropDownObject = getDropDownMenu().getSelectedItem();
		String nameString;
		if (getDropDownMenu().getSelectedItem() == null || dropDownObject instanceof String) {
			//currently no item selected in dropdown list or "new analysis" is selected. Ask for name
			nameString = new FileNameDialog(Workbench.getInstance(), "Please enter name for analysis", "Analysis Name", true)
					.requestInput();
		}
		else {
			//use currently selected analysis as name
			nameString = getDropDownMenu().getSelectedItem().toString();
		}
		if (nameString == null || nameString.equals(""))
				return;

		Analysis analysis = new Analysis(nameString, patternWizard.getPatternSettings());
		try {
			SwatComponents.getInstance().addAnalysis(analysis, analysisSourceName, true);
			updateDropDownList(nameString);
		} catch (SwatComponentException e) {
			JOptionPane.showMessageDialog(Workbench.getInstance(), "Could not store Analysis: " + e.getMessage());
			e.printStackTrace();
		}
		//		String n = AnalysisStorage.store(patternWizard.getPatternSettings(), fileName);
		//		if(n != null)
		//			setAnalyseName(n);
		//		// update the tree
		//		SwatTreeView.getInstance().componentsChanged();
		//		SwatTreeView.getInstance().expandAll();
		//		SwatTreeView.getInstance().updateUI();
		analysisName.setText(analysis.getName());
	}

	private void updateDropDownList(String nameToSelect) {
		getDropDownMenu().removeAllItems();
		dropDownMenu.addItem(NEW_ANALYSIS); //Standard item
		int i = 0;
		int indexOfThisAnalysis = -1;
		for (Analysis analysis : SwatComponents.getInstance().getAnalyses(analysisSourceName)) {
			dropDownMenu.addItem(analysis);
			if (analysis.getName().equalsIgnoreCase(nameToSelect))
				indexOfThisAnalysis = i;
			i++;
		}
		if (indexOfThisAnalysis != -1) //select this analysis
			getDropDownMenu().setSelectedIndex(indexOfThisAnalysis);
		else
			getDropDownMenu().setSelectedIndex(getDropDownMenu().getItemCount() - 1);
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
		for(GuiParameter para: ps.getParameters()) {
			String paraString=para.getName()+": ";
			int count=0;
			boolean labelsLeft=false;
			// display the values

			for(int i=0; i < para.getValue().size(); i++) {
				GuiParamValue val=para.getValue().get(i);
				labelsLeft=true;
				if(count > 0) {
					labels.add(new JLabel(paraString));
					// move it to the right
					paraString="    ";
					count=0;
					labelsLeft=false;
				}
				if(val.getOperandType()==GuiParamType.STATEPREDICATE) {
					
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

	public boolean load(String name) {
		Analysis analysis = SwatComponents.getInstance().getAnalysisByName(name);
		patternSettings = analysis.getPatternSetting();
		patternWizard.setPatternSettings(patternSettings);
		analysisName.setText("Analysis: " + name);
		update();
		return true;
	}

}
