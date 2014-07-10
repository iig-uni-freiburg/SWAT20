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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

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
	
	private List<String> getDataTypeList(PNEditor pneditor) {
		AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?, ?, ?> pn=pneditor.getNetContainer();
		AbstractPetriNet apn=pn.getPetriNet();
		if(apn.getNetType()==NetType.IFNet) {
			IFNet net=(IFNet) apn;
			Iterator it=net.getTransitions().iterator();
			//Set<String> colors=Arrays.;
			//colors.addAll(arg0)
			while(it.hasNext()) {
				AbstractCPNTransition t=(AbstractCPNTransition) it.next();
				t.getProcessedColors();
			}
		}
		return Arrays.asList("red", "green", "blue");
	}
	public AnalyzePanel(PNEditor pneditor) {
		this.pneditor=pneditor;
		transitionLabelDic=new HashMap<String, String>();
		updateTransitionLabelDic();
		panel=new JPanel(new GridLayout(PatternAnalyzeLogic.MAX_PATTERNS, 1, 10, 10));
		analyzeDescription=new JLabel("Analyze from "+getDate());
		editButton=new JButton("Edit");
		editButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				showPatternWindow();
			}
		});
		runButton=new JButton("Run");
		saveButton=new JButton("Save");
		
		patternWindow=new PatternWindow(this, transitionLabelDic, getDataTypeList(pneditor));
		update();
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

	public void update() {
		panel.removeAll();
		panel.add(Helpers.jPanelLeft(analyzeDescription));
		for(Pattern p: patternWindow.getPatterns()) {
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
	
}
