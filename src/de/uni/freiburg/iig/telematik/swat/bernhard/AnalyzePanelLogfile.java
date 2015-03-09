package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import de.invation.code.toval.graphic.dialog.FileNameDialog;
import de.invation.code.toval.parser.ParserException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.jawl.parser.ParsingMode;
import de.uni.freiburg.iig.telematik.jawl.parser.xes.XESLogParser;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.AbstractACModel;
import de.uni.freiburg.iig.telematik.swat.logs.LogFileViewer;
import de.uni.freiburg.iig.telematik.swat.logs.LogModel;
import de.uni.freiburg.iig.telematik.swat.lukas.modelchecker.adapter.sciff.SCIFFResult;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.TimeContext;
import de.uni.freiburg.iig.telematik.swat.plugin.sciff.LogParserAdapter;
import de.uni.freiburg.iig.telematik.swat.workbench.Analysis;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.MessageDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.exception.SwatComponentException;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.ViewComponent;
/**
 * this is the specific subclass of AnalyzePanel which implements
 * the functionality used to analyze logfiles
 * @author bernhard
 *
 */
public class AnalyzePanelLogfile extends AnalyzePanel {

	private File mLogFile;

	/**
	 * Create an AnalyzePanel for analyzing Logfiles
	 * @param component the LogFileViewer
	 * @param log the filename of the logfile
	 */
	public AnalyzePanelLogfile(ViewComponent component, String log) {
		super(component, log);
		objectInformationReader=new LogFileInformation( (LogFileViewer) component);
		objectChanged();
		update();
		// TODO Auto-generated constructor stub
	}

	//	@Override
	//	protected void analyze() {
	//		System.out.println(123);
	//		LogModel log = SwatComponents.getInstance().getLogModel(analysisSourceName);
	//		File file = log.getFileReference();
	//		AristaFlowParser parser = null;
	//		try {
	//			parser = new AristaFlowParser(file);
	//		} catch (FileNotFoundException e1) {
	//			// TODO Auto-generated catch block
	//			e1.printStackTrace();
	//		}
	//		SCIFFChecker checker = new SCIFFChecker();
	//
	//		for (PatternSetting setting : patternSettings) {
	//			setting.reset();
	//			List<CompliancePattern> cPs = patternFactory
	//					.createPattern(setting.getName(), (ArrayList<GuiParameter>) setting.getParameters());
	//			for (CompliancePattern pattern : cPs) {
	//				CheckerReport report = null;
	//				try {
	//					report = checker.analyse(parser, pattern.getSCIFFRule(), TimeGranularity.MILLISECONDS);
	//				} catch (JDOMException e) {
	//					// TODO Auto-generated catch block
	//					e.printStackTrace();
	//				} catch (IOException e) {
	//					// TODO Auto-generated catch block
	//					e.printStackTrace();
	//				}
	//				CompositeRule rule = pattern.getSCIFFRule();
	//				SciffPresenter sciff = new SciffPresenter(report, rule, "", file);
	//				sciff.show();
	//			}
	//		}	
	//	}

	@Override
	protected void analyze() {

		//toolBar.reset();
		XESLogParser parser = new XESLogParser();
		try {
			parser.parse(mLogFile, ParsingMode.COMPLETE);
			LogParserAdapter adapter = new LogParserAdapter(parser);
			SCIFFAdapter sciffExecuter = new SCIFFAdapter(adapter);
			// build list of patterns
			HashMap<PatternSetting, ArrayList<CompliancePattern>> resultMap = new HashMap<PatternSetting, ArrayList<CompliancePattern>>();

			MessageDialog.getInstance().addMessage("Giving " + patternSettings.size() + " Patterns to SCIFF");
			ArrayList<CompliancePattern> compliancePatterns = new ArrayList<CompliancePattern>();

			for (PatternSetting setting : patternSettings) {
				setting.reset();
				ArrayList<CompliancePattern> cPs = patternFactory.createPattern(setting.getName(),
						(ArrayList<GuiParameter>) setting.getParameters());
				compliancePatterns.addAll(cPs);
				// store the setting and the pattern in dictionary
				resultMap.put(setting, cPs);
			}

			// analyze and set the results
			ArrayList<SCIFFResult> sciffResult = sciffExecuter.analyze(compliancePatterns);

			for (int i = 0; i < patternSettings.size(); i++) {
				PatternSetting setting = patternSettings.get(i);
				setting.setResult(sciffResult.get(i));
			}
			update();

		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	protected String adjustValue(GuiParamValue val) {
		return val.getOperandName();
	}

	/**
	 * open a save dialog and save the analysis for this log file
	 */
	public void save() {
		System.out.println("Saving log");
		Object dropDownObject = getDropDownMenu().getSelectedItem();
		String nameString;
		if (getDropDownMenu().getSelectedItem() == null || dropDownObject instanceof String) {
			//currently no item selected in dropdown list or "new analysis" is selected. Ask for name
			nameString = FileNameDialog.showDialog(Workbench.getInstance(), "Please enter name for analysis", "Analysis Name", true);
		} else {
			//use currently selected analysis as name
			nameString = getDropDownMenu().getSelectedItem().toString();
		}
		if (nameString == null || nameString.equals(""))
			return;

		Analysis analysis = new Analysis(nameString, patternWizard.getPatternSettings());
		try {
			SwatComponents.getInstance().addAnalysisForLog(analysis, analysisSourceName, true);
			//updateDropDownList(nameString);
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

	protected JComboBox getDropDownMenu() {
		if (this.dropDownMenu == null) {
			this.dropDownMenu = new JComboBox();
			dropDownMenu.addItem(NEW_ANALYSIS);
			try {
				for (Analysis analysis : SwatComponents.getInstance().getAnalyses(analysisSourceName)) {
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

	@Override
	public void petriNetAdded(AbstractGraphicalPN net) {
		// TODO Auto-generated method stub

	}

	@Override
	public void petriNetRemoved(AbstractGraphicalPN net) {
		// TODO Auto-generated method stub

	}

	@Override
	public void petriNetRenamed(AbstractGraphicalPN net) {
		// TODO Auto-generated method stub

	}

	@Override
	public void acModelAdded(AbstractACModel acModel) {
		// TODO Auto-generated method stub

	}

	@Override
	public void acModelRemoved(AbstractACModel acModel) {
		// TODO Auto-generated method stub

	}

	@Override
	public void analysisContextAdded(String netID, AnalysisContext context) {
		// TODO Auto-generated method stub

	}

	@Override
	public void analysisContextRemoved(String netID, AnalysisContext context) {
		// TODO Auto-generated method stub

	}

	@Override
	public void analysisRemoved(String netID, Analysis analysis) {
		// TODO Auto-generated method stub

	}

	@Override
	public void timeContextAdded(String netID, TimeContext context) {
		// TODO Auto-generated method stub

	}

	@Override
	public void timeContextRemoved(String netID, TimeContext context) {
		// TODO Auto-generated method stub

	}

	@Override
	public void logAdded(LogModel log) {
		// TODO Auto-generated method stub

	}

	@Override
	public void logRemoved(LogModel log) {
		// TODO Auto-generated method stub

	}

	@Override
	public void componentsChanged() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void addCounterExampleButton(PRISMPatternResult result, JPanel newPanel) {
		// TODO Auto-generated method stub

	}
	  

}
