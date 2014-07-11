package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;

/**
 * this class represents a panel to be added on the right side
 * @author bernhard
 *
 */
public class AnalyzePanel {

	private JPanel panel;
	private JLabel analyzeDescription;
	private JButton editButton, runButton, saveButton;
	private String netName;
	private PatternWindow patternWindow;
	private PNEditor pneditor;
	private HashMap<String, String> transitionLabelDic;
	private List<String> dataTypeList;
	
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
	public AnalyzePanel(PNEditor pneditor) {
		this.pneditor=pneditor;
		transitionLabelDic=new HashMap<String, String>();
		netChanged();
		panel=new JPanel(new GridLayout(PatternAnalyzeLogic.MAX_PATTERNS, 1, 10, 10));
		analyzeDescription=new JLabel("Analyze from "+getDate());
		editButton=new JButton("Edit");
		
		runButton=new JButton("Run");
		saveButton=new JButton("Save");
		
		patternWindow=new PatternWindow(this, pneditor);
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
		
	}
	public void update() {
		panel.removeAll();
		panel.add(Helpers.jPanelLeft(analyzeDescription));
		for(PatternSetting p: patternWindow.getPatterns()) {
			panel.add(Helpers.jPanelLeft(new JLabel(p.toString())));
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
		this.netName = netName;
	}
	public List<String> getDataList() {
		// TODO Auto-generated method stub
		return dataTypeList;
	}
	
}
