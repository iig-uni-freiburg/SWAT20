package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTPlace;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.lukas.Operand;
import de.uni.freiburg.iig.telematik.swat.lukas.OperandType;
import de.uni.freiburg.iig.telematik.swat.lukas.ParamValue;
import de.uni.freiburg.iig.telematik.swat.lukas.Parameter;
import de.uni.freiburg.iig.telematik.swat.lukas.PatternFactory;
/**
 * This class represents a Panel with all its Components Labels, Remove Button
 * A PatternSettingPanel can be added to the patternPanel of the PatternWindow
 * @author bernhard
 *
 */
public class PatternSettingPanel {
	private JPanel panel;
	private JPanel rightPanel;
	private JButton removeButton;
	private PatternSetting patternSetting;
	private List<PatternParameterPanel> parameterPanelList;
	private HashMap<String, String> transitionDic;
	private PatternWindow patternWindow;

	public PatternSettingPanel(PatternSetting ps, PatternWindow patternWindow, PatternFactory patternFactory) {
		init(ps.getName(), patternWindow, patternFactory);
		setPatternSetting(ps);
	}
	public PatternSettingPanel(String patternName, PatternWindow patternWindow, PatternFactory patternFactory) {
		init(patternName, patternWindow, patternFactory);
	}
	
	private void init(String patternName, PatternWindow patternWindow, PatternFactory patternFactory) {
		//pattern = PatternDatabase.getInstance()
		//	.getPattern(patternName);
	List<Parameter> parameters = patternFactory.getParametersOfPattern(patternName);
	this.patternWindow=patternWindow;
	transitionDic=patternWindow.getTransitionDic();
	patternSetting=new PatternSetting(patternName,parameters);
	panel = new JPanel(new GridLayout(patternSetting.getParameters().size() + 1, 2));
	// System.out.println(paraList);
	// System.out.println(paraList.size());
	parameterPanelList = new ArrayList<PatternParameterPanel>();

	JLabel label = new JLabel(patternName);
	Font font = label.getFont();
	// same font but bold
	Font boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize()+3);
	label.setFont(boldFont);
	panel.add(label);

	
	try {
		removeButton = new JButton(IconFactory.getIcon("minimize"));
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				removeFromPatternWindow();
			}
		});
		rightPanel=new JPanel(new FlowLayout(FlowLayout.LEFT));
		rightPanel.add(removeButton);
		panel.add(rightPanel);
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

	// add ParameterPanels for each Parameter
	//for (PatternParameter para : pattern.getParameters()) {
	
	for (Parameter pp : parameters) {
		PatternParameterPanel patternPara = null;
		panel.add(new JLabel("Choose "+pp.getName()));
		//System.out.println(para.type);
		//if (para.type.equals("data")) {
		Set<OperandType> operandSet=pp.getTypes();
		if(operandSet.contains(OperandType.TOKEN)) {
			List<String> dataList=patternWindow.getDataList();
			patternPara = new PatternDataParameter(pp.getName(), dataList.toArray(new String[dataList.size()]));
		} else if (operandSet.contains(OperandType.TRANSITION)) {
			patternPara = new PatternActivityParameter(pp.getName(), transitionDic.keySet().toArray(new String[transitionDic.keySet().size()]));
		}
		panel.add(patternPara.getjComponent());
		parameterPanelList.add(patternPara);
	}
	}

	

	protected void removeFromPatternWindow() {
		// TODO Auto-generated method stub
		patternWindow.removePatternPanel(this);
	}



	/**
	 * return a list of the entered values
	 * @return
	 */
	public String getValues() {
		String paraString = "";
		for (PatternParameterPanel para : parameterPanelList) {
			paraString += para.getValue() + ":";
		}
		return paraString;
	}
	public PatternSetting getPatternSetting() {
		return patternSetting;
	}
	public void setPatternSetting(PatternSetting ps) {
		patternSetting=ps;
		assert(ps.getParameters().size() == parameterPanelList.size());
		for(int i=0; i <ps.getParameters().size(); i++) {
			parameterPanelList.get(i).setValue(Helpers.getFirst(ps.getParameters().get(i).getValue()).getOperandName());
		}
	}

	public JPanel getJPanel() {
		return panel;
	}

	
	
	public void updatePatternValues() {
		// first store the values taken from the jcomponents
		
		for (PatternParameterPanel paraPanel : parameterPanelList) {
			for(Parameter patternPara: patternSetting.getParameters()) {
				if(paraPanel.getName().equals(patternPara.getName())) {
					// if its an activity, take the name and not the label
					if(paraPanel.getType()==OperandType.TRANSITION) {
						patternPara.setValue(new ParamValue(paraPanel.getValue(), OperandType.TRANSITION));
					} else if(paraPanel.getType()==OperandType.TOKEN) {
						patternPara.setValue(new ParamValue(paraPanel.getValue(), OperandType.TOKEN));
					}
				}
			}
			
		}
		// update the pattern representation
		patternSetting.updateParameterAppliedString();
		for(Parameter patternPara: patternSetting.getParameters()) {
			if(patternPara.getTypes().contains(OperandType.TRANSITION)) {
					// if its an activity, take the name and not the label
				patternPara.setValue(new ParamValue(transitionDic.get(Helpers.getFirst(patternPara.getValue()).getOperandName()), Helpers.getFirst(patternPara.getValue()).getOperandType()));
			}
		}
			
	}

	public String getPatternName() {
		// TODO Auto-generated method stub
		return patternSetting.getName();
	}
	
}
