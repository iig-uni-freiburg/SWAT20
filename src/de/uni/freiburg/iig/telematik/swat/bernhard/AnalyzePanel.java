package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;

import com.thoughtworks.xstream.XStream;

import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.lukas.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.lukas.IOUtils;
import de.uni.freiburg.iig.telematik.swat.lukas.Parameter;
import de.uni.freiburg.iig.telematik.swat.lukas.PatternFactory;
import de.uni.freiburg.iig.telematik.swat.lukas.PatternResult;
import de.uni.freiburg.iig.telematik.swat.lukas.PrismExecutor;
import de.uni.freiburg.iig.telematik.swat.lukas.PrismResult;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponentType;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeView;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeView.SwatTreeNode;

/**
 * this class represents a panel to be added on the right side
 * @author bernhard
 *
 */
public class AnalyzePanel implements LoadSave {

	private JPanel panel;
	private JLabel analyzeDescription;
	private JButton editButton, runButton, saveButton;
	private String netName;
	private PatternWindow patternWindow;
	private PNEditor pneditor;
	private HashMap<String, String> transitionLabelDic;
	private List<String> dataTypeList;
	private PatternFactory patternFactory;
	
	public void netChanged() {
		updateTransitionLabelDic();
		if(pneditor.getNetContainer().getPetriNet().getNetType() == NetType.IFNet) {
			updateDataTypeList();
		}
	}
	/**
	 * Helpfunction to get the List of all Labels of the current PN of editor
	 * @param editor
	 * @return
	 */
	public void updateTransitionLabelDic() {
		List<String> result = new ArrayList<String>();
		for(AbstractTransition transition : pneditor.getNetContainer().getPetriNet().getTransitions()){
			transitionLabelDic.put(transition.getLabel(), transition.getName());
		}
	}
	
	public void updateDataTypeList() {
		AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?, ?, ?> pn=pneditor.getNetContainer();
		AbstractPetriNet apn=pn.getPetriNet();
		dataTypeList =new ArrayList<String>();
		Set<String> dataTypes=new HashSet<String>();
		if(apn.getNetType()==NetType.IFNet) {
			IFNet net=(IFNet) apn;
			Iterator it=net.getTransitions().iterator();
			//Set<String> colors=Arrays.;
			//colors.addAll(arg0)
			while(it.hasNext()) {
				AbstractCPNTransition t=(AbstractCPNTransition) it.next();
				dataTypes.addAll(t.getProcessedColors());
			}
			dataTypeList.addAll(dataTypes);
		}
		
		System.out.println(dataTypeList);
	}
	public AnalyzePanel(PNEditor pneditor, String net) {
		this.pneditor=pneditor;
		netName=net.split("[.]")[0];
		patternFactory=new PatternFactory(pneditor.getNetContainer().getPetriNet());
		transitionLabelDic=new HashMap<String, String>();
		netChanged();
		panel=new JPanel(new GridLayout(PatternAnalyzeLogic.MAX_PATTERNS, 1, 10, 10));
		analyzeDescription=new JLabel("Analysis from "+getDate());
		editButton=new JButton("Edit");
		
		runButton=new JButton("Run");
		saveButton=new JButton("Save");
		
		patternWindow=new PatternWindow(this, pneditor, patternFactory);
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
		update();
	}
	
	public HashMap<String, String> getTransitionLabelDic() {
		return transitionLabelDic;
	}
	private void addButtons() {
		panel.add(Helpers.jPanelLeft(editButton));
		panel.add(Helpers.jPanelLeft(runButton));
		panel.add(Helpers.jPanelLeft(saveButton));
	}
	
	private String getDate() {
		Date today = new Date();
		DateFormat formatter=DateFormat.getDateInstance(DateFormat.SHORT, new Locale(System.getProperty("user.language"),System.getProperty("user.country")));
		return formatter.format(today);
	}
	
	protected void showPatternWindow() {
		// TODO Auto-generated method stub
		patternWindow.setVisible(true);
	}

	/**
	 * the function invoked when Analyze is pressed
	 */
	private void analyze() {
		PrismExecutor prismExecuter = new PrismExecutor(pneditor.getNetContainer().getPetriNet());
		// build list of patterns
		List<PatternSetting> patternSettings=patternWindow.getPatternSettings();
		ArrayList<CompliancePattern> compliancePatterns=new ArrayList<CompliancePattern>();
		for(PatternSetting setting : patternSettings) {
			compliancePatterns.add(patternFactory.createPattern(setting.getName(), (ArrayList<Parameter>) setting.getParameters()));
		}
		PrismResult prismResult=prismExecuter.anaylaze(compliancePatterns);
		for(PatternSetting setting : patternSettings) {
			PatternResult patternResult =prismResult.getPatternResult(setting.getName());
			setting.setResult(patternResult);
		}
		update();
	}
	public void update() {
		panel.removeAll();
		panel.add(Helpers.jPanelLeft(analyzeDescription));
		for(PatternSetting p: patternWindow.getPatternSettings()) {
			// check whether a result exists
			JPanel newPanel=new JPanel();
			PatternResult result=p.getResult();
			if(result != null) {
				int rows=2;
				if(result.isFulfilled() == false) {
					rows=3;
				}
				newPanel=new JPanel(new GridLayout(rows,1,1,1));
				newPanel.add(new JLabel(p.toString()));
				newPanel.add(new JLabel("\tProb:"+result.getProbability()));
				if(result.isFulfilled() == false) {
					JButton counterButton=new JButton("Counterexample");
					newPanel.add(counterButton);
				}
			} else {
				newPanel=new JPanel(new FlowLayout(FlowLayout.LEFT));
				newPanel.add(new JLabel(p.toString()));
			}
			
			panel.add(newPanel);
		}
		addButtons();
		panel.repaint();
		panel.updateUI();
	}
	
	public void setAnalyseName(String text) {
		analyzeDescription.setText(text);
	}

	public JPanel getPanel() {
		return panel;
	}

	public void setPanel(JPanel panel) {
		this.panel = panel;
	}

	public String getNetName() {
		return netName;
	}

	public void setNetName(String netName) {
		this.netName=netName.split("[.]")[0];
	}
	public List<String> getDataList() {
		// TODO Auto-generated method stub
		return dataTypeList;
	}
	
	public boolean load(List<PatternSetting> patternSettings) {
		return true;
	}
	@Override
	public boolean save() {
		XStream xstream=new XStream();
		String xml=xstream.toXML(patternWindow.getPatternSettings());
		IOUtils.writeToFile(".", "analyse1.xml", xml);
		SwatTreeView sw=SwatTreeView.getInstance();
		DefaultMutableTreeNode tm= (DefaultMutableTreeNode) sw.getModel().getChild(sw.getModel().getRoot(), 0);
		for(int i = 0; i < tm.getChildCount(); i++) {
			SwatTreeNode child=(SwatTreeNode) tm.getChildAt(i);
			System.out.println(child.getDisplayName());
			System.out.println(netName);
			if (child.getDisplayName().equals(netName)) {
				System.out.println("hallo");
				child.setAllowsChildren(true);
				child.insert(sw.new SwatTreeNode("analysis1", SwatComponentType.PETRI_NET_ANALYSIS, new File("analyse1.xml")), 0);
			}
		}
		sw.expandAll();
		sw.repaint();
		sw.updateUI();
		return false;
	}

	
}
