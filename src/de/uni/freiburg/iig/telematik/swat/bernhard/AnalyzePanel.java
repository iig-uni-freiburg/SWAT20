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
import de.uni.freiburg.iig.telematik.swat.lukas.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.lukas.IOUtils;
import de.uni.freiburg.iig.telematik.swat.lukas.OperandType;
import de.uni.freiburg.iig.telematik.swat.lukas.ParamValue;
import de.uni.freiburg.iig.telematik.swat.lukas.Parameter;
import de.uni.freiburg.iig.telematik.swat.lukas.PatternFactory;
import de.uni.freiburg.iig.telematik.swat.lukas.PatternResult;
import de.uni.freiburg.iig.telematik.swat.lukas.PrismExecutor;
import de.uni.freiburg.iig.telematik.swat.lukas.PrismResult;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponentType;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeView;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeView.SwatTreeNode;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.MessageDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

/**
 * this class represents a panel to be added on the right side
 * 
 * @author bernhard
 * 
 */
public class AnalyzePanel implements LoadSave {
	private JPanel content;
	private JPanel propertyPanel;
	private JLabel analysisTopLabelWithDate;
	private JButton editButton, runButton, saveButton;
	private String netName;
	private PatternWindow patternWindow;
	private PNEditor pnEditor;
	private PetriNetInformation netInfo;
	private PatternFactory patternFactory;
	private List<PatternSetting> patternSettings;
	private AnalyzeToolBar toolBar;

	public void netChanged() {
		netInfo.netChanged();
		// update the parameter boxes
		patternWindow.netChanged();
	}

	public PetriNetInformationReader getNetInformation() {
		return netInfo;
	}

	public AnalyzeToolBar getToolBar() {
		return toolBar;
	}

	public AnalyzePanel(PNEditor pneditor, String net) {
		this.pnEditor = pneditor;
		toolBar=new AnalyzeToolBar(pneditor);
		netName = net.split("[.]")[0];
		netInfo = new PetriNetInformation(pneditor);
		patternFactory = new PatternFactory(pneditor.getNetContainer()
				.getPetriNet());
		patternSettings = new ArrayList<PatternSetting>();
		initGui();
		netChanged();
		update();
	}
	
	private void initGui() {
		content=new JPanel(new GridBagLayout());
		analysisTopLabelWithDate = new JLabel("Analysis from "
				+ getDateShort());
		editButton = new JButton("Edit");
		runButton = new JButton("Run");
		saveButton = new JButton("Save");
		propertyPanel=new JPanel();
		propertyPanel.setLayout(new BoxLayout(propertyPanel,BoxLayout.Y_AXIS));
		Box box=Box.createVerticalBox();
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(editButton);
		buttonPanel.add(runButton);
		buttonPanel.add(saveButton);
		box.add(Helpers.jPanelLeft(analysisTopLabelWithDate));
		box.add(buttonPanel);

		box.add(Helpers.jPanelLeft(new JLabel("Patterns to Check: ")));
		JPanel northPanel=new JPanel(new BorderLayout());
		northPanel.add(propertyPanel, BorderLayout.NORTH);
		JScrollPane jsp=new JScrollPane(northPanel);
		jsp.setVisible(true);
		//jsp.setEnabled(true);
		//jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		content.setLayout(new BorderLayout());
		content.add(box,BorderLayout.NORTH);
		content.add(jsp,BorderLayout.CENTER);
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
				analyze();
			}
		});
		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				save();
			}
		});
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
	private void analyze() {
		if(patternWindow.isVisible()) {
			JOptionPane.showMessageDialog(null,
				    "Close PatternWindow first, before analyzing",
				    "Warning",
				    JOptionPane.WARNING_MESSAGE);
			return;
		}
		PrismExecutor prismExecuter = new PrismExecutor(pnEditor
				.getNetContainer().getPetriNet());
		// build list of patterns
		HashMap<PatternSetting, CompliancePattern> resultMap = new HashMap<PatternSetting, CompliancePattern>();

		List<PatternSetting> patternSettings = patternWindow
				.getPatternSettings();
		MessageDialog.getInstance().addMessage(
				"Giving " + patternSettings.size() + " Patterns to PRISM");
		ArrayList<CompliancePattern> compliancePatterns = new ArrayList<CompliancePattern>();
		for (PatternSetting setting : patternSettings) {
			CompliancePattern compliancePattern = patternFactory
					.createPattern(setting.getName(),
							(ArrayList<Parameter>) setting.getParameters());
			compliancePatterns.add(compliancePattern);
			resultMap.put(setting, compliancePattern);
		}
		System.out.println("Compliance Pattern: " + compliancePatterns.size());
		PrismResult prismResult = prismExecuter.analyze(compliancePatterns);
		for (PatternSetting setting : patternSettings) {
			PatternResult patternResult = prismResult
					.getPatternResult(resultMap.get(setting));
			setting.setResult(patternResult);
		}
		update();
	}

	public void update() {
		propertyPanel.removeAll();
		
		patternSettings = patternWindow.getPatternSettings();
		HashMap<String, String> transitionsReverse=netInfo.getTransitionDictionaryReverse();
		for (PatternSetting p : patternSettings) {
			System.out.println(p);
			PatternResult result = p.getResult();
			JPanel newPanel = new JPanel();
			newPanel.setLayout(new BoxLayout(newPanel, BoxLayout.Y_AXIS));
			for(Parameter para: p.getParameters()) {
				String paraString=para.getName()+": ";
				int count=0;
				boolean labelsLeft=false;
				for(int i=0; i < para.getValue().size(); i++) {
					ParamValue val=para.getValue().get(i);
					labelsLeft=true;
					count++;
					if(val.getOperandType()==OperandType.TRANSITION) {
						paraString+=transitionsReverse.get(val.getOperandName());
					} else {
						paraString+=val.getOperandName();
					}
					if(i < para.getValue().size() -1) {
						paraString+=", ";
					}
					// maximum 3 values in a row
					if(count==3) {
						newPanel.add(Helpers.jPanelLeft(new JLabel(paraString)));
						// move it to the right
						paraString="    ";
						count=0;
						labelsLeft=false;
					}
				}
				if(labelsLeft) {
					newPanel.add(Helpers.jPanelLeft(new JLabel(paraString)));
				}
			}
			// check whether a result exists
			if (result != null) {
				DecimalFormat decimalFormat = new DecimalFormat("#.###");
				newPanel.add(Helpers.jPanelLeft(new JLabel("\tProbability: "
						+ decimalFormat.format(result.getProbability()))));
				JButton counterButton = new JButton("Counterexample");
				final List<String> path=result.getViolatingPath();
				counterButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				showCounterExample(path);
			}
		});
				// counterButton.setPreferredSize(counterButton.getMaximumSize());
				if (result.isFulfilled() == false) {
					newPanel.add(Helpers.jPanelLeft(counterButton));
				}
			}
			newPanel.setBorder(BorderFactory.createTitledBorder(p.getName()));
			JPanel northPanel=new JPanel(new BorderLayout());
			northPanel.add(newPanel, BorderLayout.NORTH);
			propertyPanel.add(northPanel);
		}

		content.repaint();
		content.updateUI();
	}

	public void setAnalyseName(String text) {
		analysisTopLabelWithDate.setText(text);
	}

	public JPanel getContent() {
		return content;
	}

	public void setContent(JPanel panel) {
		this.content = panel;
	}

	public String getNetName() {
		return netName;
	}

	public void setNetName(String netName) {
		this.netName = netName.split("[.]")[0];
	}

	public boolean load(File f) {
		MessageDialog.getInstance().addMessage(
				"Loading Analysis Settings from " + f);
		patternSettings = AnalysisStore.loadFromFile(f);
		// System.out.println(newList);
		patternWindow.setPatternSettings(patternSettings);
		update();
		return true;
	}

	@Override
	public boolean save() {
		AnalysisStore.store(patternWindow.getPatternSettings(), netName);
		// update the tree
		SwatTreeView.getInstance().updateAnalysis();
		SwatTreeView.getInstance().expandAll();
		SwatTreeView.getInstance().updateUI();
		return true;
	}
	
	private void showCounterExample(List<String> path) {
		toolBar.setCounterExample(path);
	}

}
