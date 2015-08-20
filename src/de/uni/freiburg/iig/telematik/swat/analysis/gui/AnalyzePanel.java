package de.uni.freiburg.iig.telematik.swat.analysis.gui;

import de.invation.code.toval.misc.wd.ProjectComponentException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.processmining.analysis.sciffchecker.logic.reasoning.CheckerReport;

import de.uni.freiburg.iig.telematik.swat.analysis.Analysis;
import de.uni.freiburg.iig.telematik.swat.analysis.AnalysisController;
import de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.ModelCheckerResult;
import de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.sciff.presenter.SciffResultPresenter;
import de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.sciff.presenter.SciffResultPresenter.resultType;
import de.uni.freiburg.iig.telematik.swat.analysis.prism.PrismFunctionValidator;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponentType;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.AnalysisHashErrorDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.exception.SwatComponentException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Panel on right side of SWAT. Shows analysis results **/
public class AnalyzePanel extends JPanel implements ItemListener {

	private static final long serialVersionUID = 1L;
	
	/*private JPanel mButtonPanel;
	
	private JButton mEditButton;
	private JButton mRunButton;
	private JButton mSaveButton;*/

	private AnalysisController mAnalysisController;
	
	private JComboBox dropDown;

	public AnalyzePanel(AnalysisController analysisController) throws Exception {	
		mAnalysisController = analysisController;
		updatePatternResults();
	
	}	
	
	private String getDateShort() {
		Date today = new Date();
		DateFormat formatter = DateFormat.getDateInstance(
				DateFormat.SHORT,
				new Locale(System.getProperty("user.language"), System
						.getProperty("user.country")));
		return formatter.format(today);
	}
	
	
	public void updatePatternResults() throws Exception {
		
	this.removeAll();
	JPanel content = new JPanel(new GridBagLayout());
	JLabel analysisName = new JLabel("Analysis from " + getDateShort());
	JButton editButton = new JButton("Edit");
	JButton runButton = new JButton("Run");
	JButton saveButton = new JButton("Save");
	JPanel propertyPanel = new JPanel();
	propertyPanel.setLayout(new BoxLayout(propertyPanel, BoxLayout.Y_AXIS));
	Box box = Box.createVerticalBox();

		box.add(getAnalysisDropDown(Workbench.getInstance().getNameOfCurrentComponent()));

	JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	buttonPanel.add(editButton);
	buttonPanel.add(runButton);
	buttonPanel.add(saveButton);
	box.add(jPanelLeft(analysisName));
	box.add(buttonPanel);

	box.add(jPanelLeft(new JLabel("Patterns to Check: ")));
	JPanel northPanel = new JPanel(new BorderLayout());
	northPanel.add(propertyPanel, BorderLayout.NORTH);
	JScrollPane jsp = new JScrollPane(northPanel);
	jsp.setVisible(true);
	content.setLayout(new BorderLayout());
	content.add(box, BorderLayout.NORTH);
	content.add(jsp, BorderLayout.CENTER);
	
		editButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				mAnalysisController.openPatternDialog();
			}
		});
		
		runButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					invokeModelChecking();
				} catch (Exception e) {
					Workbench.errorMessage("Analysis Exception while running model checker: " + e.getMessage(), e, true);
				}
			}
		});
		
		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
                            try {
                                saveRules();
                            } catch (Exception ex) {
                                Workbench.errorMessage("Cold not store analysis ", ex, true);
                                Logger.getLogger(AnalyzePanel.class.getName()).log(Level.SEVERE, null, ex);
                            }

			}
		});

		ArrayList<CompliancePattern> patterns = mAnalysisController.getPatterns();
		for (CompliancePattern pattern : patterns) {
			if (pattern.isInstantiated()) {
				propertyPanel.add(new PatternResultPanel(pattern));
			}
		}
		this.add(content);

		addModelCheckerResult();
	}
	


	private void invokeModelChecking() throws Exception {
		if (Workbench.getInstance().getTypeOfCurrentComponent().equals(SwatComponentType.PETRI_NET)) {
			if (!PrismFunctionValidator.checkPrism())
				Workbench.errorMessage("PRISM is not correctly set-up", new Exception("Could not load Prism"), true);
			return;
		}
		mAnalysisController.runModelChecker();
	}

	private Component getAnalysisDropDown(String netID) throws ProjectComponentException {
		if (dropDown == null) {
			Collection<Analysis> analyses = getAvailableAnalyses(netID);
			Collections.sort((List) analyses);
			dropDown = new JComboBox();
			dropDown.addItem("New Analysis...");
			for (Analysis a : analyses)
				dropDown.addItem(a);

			dropDown.addItemListener(this);
		}

		return dropDown;
	}
	
	private Collection<Analysis>getAvailableAnalyses(String netID) throws ProjectComponentException{
		LinkedList<Analysis> result = new LinkedList<>();
		SwatComponents sc=SwatComponents.getInstance();
		try{
		result.addAll(sc.getContainerPetriNets().getContainerAnalysis(netID).getComponents());
		} catch (ProjectComponentException e){}
		
		try{
			result.addAll(sc.getContainerAristaflowLogs().getContainerAnalysis(netID).getComponents());
		} catch (ProjectComponentException e){}
		
		try {
			result.addAll(sc.getContainerMXMLLogs().getContainerAnalysis(netID).getComponents());
		} catch (ProjectComponentException e){}
		
		try{
			result.addAll(sc.getContainerXESLogs().getContainerAnalysis(netID).getComponents());
		} catch (ProjectComponentException e){}
		
		return result;
	}

	private JPanel jPanelLeft(JComponent k) {
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p.add(k);
		return p;
	}

	@SuppressWarnings("rawtypes")
	private void saveRules() throws Exception {
		SwatComponents sc = SwatComponents.getInstance();
		String analysisTargetName = Workbench.getInstance().getNameOfCurrentComponent();
		try {
			String name = JOptionPane.showInputDialog(this, "Please name analysis", dropDown.getSelectedItem().toString());
			String oldName = getSelectedDropDownItemName();
			if (name.equalsIgnoreCase(oldName)) {
				dropDown.removeItemAt(dropDown.getSelectedIndex());
				sc.getContainerPetriNets().getContainerAnalysis(oldName).removeComponent(oldName, true);
			}

			Analysis save = new Analysis(name, mAnalysisController.getPatterns());
			save.setHashCode(Workbench.getInstance().getHashOfCurrentComponent());
			save.setLoadedFromDisk();
                        storeLogAnalysis(analysisTargetName, save);
			dropDown.addItem(save);
			dropDown.setSelectedItem(save);

		} catch (SwatComponentException e) {
			Workbench.errorMessage("Could process analysis", e, true);
		}
	}

	public void setPatterns(Analysis analysis) throws Exception{
		mAnalysisController.setPatterns(analysis.getPatternSetting());
		updatePatternResults();
		updateUI();
	}

	@Override
	/**Listen to dropdown change**/
	public void itemStateChanged(ItemEvent e) {
		if (dropDown.getSelectedItem() instanceof Analysis) {
                    try {
                        Analysis a = (Analysis) dropDown.getSelectedItem();
                        System.out.println("Setting pattern...");
                        setPatterns(a);
                        if (a.getHashCode() != Workbench.getInstance().getHashOfCurrentComponent()) {
                            boolean recompute = new AnalysisHashErrorDialog(Workbench.getInstance().getTypeOfCurrentComponent()).showUpdateDialog();
                            if (recompute) {
                                try {
                                    invokeModelChecking();
                                } catch (Exception e1) {
                                    Workbench.errorMessage("Could not invoke model checking", e1, true);
                                }
                            }
                            
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(AnalyzePanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
		}
	}
	
	private String getSelectedDropDownItemName() {
		String name;
		if (dropDown.getSelectedItem() instanceof Analysis)
			name = ((Analysis) dropDown.getSelectedItem()).toString();
		else
			name = "";
		return name;
	}
	
	private void addModelCheckerResult() {
		if (ModelCheckerResult.hasAResult() && ModelCheckerResult.getResult() instanceof CheckerReport) {
			final CheckerReport report = (CheckerReport) ModelCheckerResult.getResult();

			JButton wrong = new JButton("violating (" + report.wrongInstances().size() + ")");
			JButton correct = new JButton("correct (" + report.correctInstances().size() + ")");

			ActionListener present = new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					SciffResultPresenter presenter = new SciffResultPresenter(report);
					if (e.getActionCommand().contains("violating"))
						presenter.show(resultType.WRONG);
					else
						presenter.show(resultType.CORRECT);
				}
			};

			wrong.addActionListener(present);
			correct.addActionListener(present);

			this.add(wrong);
			this.add(correct);
			ModelCheckerResult.removeResult();
		}

	}

	/*
	public void updatePatternResults() {
		
		
		
		
		this.removeAll();
		Box box = Box.createVerticalBox();
		mButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		mEditButton = new JButton("Edit");
		mRunButton = new JButton("Run");
		mSaveButton = new JButton("Save");
		mButtonPanel.add(mEditButton);
		mButtonPanel.add(mRunButton);
		mButtonPanel.add(mSaveButton);
		box.add(mButtonPanel);
		
		mEditButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				mAnalysisController.openPatternDialog();
			}
		});
		
		mRunButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				mAnalysisController.runModelChecker();
				
			}
		});
		ArrayList<CompliancePattern> patterns = mAnalysisController.getPatterns();
		for (CompliancePattern pattern : patterns) {
			if (pattern.isInstantiated()) {
				box.add(new PatternResultPanel(pattern));
			}
		}
		this.add(box);
	}
	*/

    private void storeLogAnalysis(String logName, Analysis save) throws ProjectComponentException {
        //which kind of log
        SwatComponents comp = SwatComponents.getInstance();
        if(comp.getContainerAristaflowLogs().containsComponent(logName)){
            comp.getContainerAristaflowLogs().addAnalysis(save, logName, true);
        }
        else if (comp.getContainerMXMLLogs().containsComponent(logName)){
            comp.getContainerMXMLLogs().addAnalysis(save, logName, true);
        }
        else if (comp.getContainerXESLogs().containsComponent(logName)){
            comp.getContainerXESLogs().addAnalysis(save, logName, true);
        }
        
    }
}
