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
	private Pattern pattern;
	private List<PatternParameterPanel> parameterList;
	private HashMap<String, String> transitionDic;
	private PatternWindow patternWindow;

	public PatternSettingPanel(String patternName, PatternWindow patternWindow, HashMap<String, String> transitionDic, List<String> dataList) {
		pattern = PatternDatabase.getInstance()
				.getPattern(patternName);
		this.transitionDic=transitionDic;
		this.patternWindow=patternWindow;
		panel = new JPanel(new GridLayout(pattern.getParameters().size() + 1, 2));
		// System.out.println(paraList);
		// System.out.println(paraList.size());
		parameterList = new ArrayList<PatternParameterPanel>();

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
					remove();
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
		for (PatternParameter para : pattern.getParameters()) {
			PatternParameterPanel patternPara = null;
			panel.add(new JLabel("Choose "+para.name));
			//System.out.println(para.type);
			if (para.type.equals("data")) {
				patternPara = new PatternDataParameter(para.name, dataList.toArray(new String[0]));
			} else if (para.type.equals("activity")) {
				patternPara = new PatternActivityParameter(para.name, transitionDic.keySet().toArray(new String[transitionDic.keySet().size()]));
			}
			panel.add(patternPara.getjComponent());
			parameterList.add(patternPara);
		}
	}

	

	protected void remove() {
		// TODO Auto-generated method stub
		patternWindow.removePatternPanel(this);
	}



	/**
	 * return a list of the entered values
	 * @return
	 */
	public String getValues() {
		String paraString = "";
		for (PatternParameterPanel para : parameterList) {
			paraString += para.getValue() + ":";
		}
		return paraString;
	}
	public Pattern getPattern() {
		return pattern;
	}

	public JPanel getJPanel() {
		return panel;
	}

	
	
	public void updatePatternValues() {
		// first store the values taken from the jcomponents
		
		for (PatternParameterPanel para : parameterList) {
			for(PatternParameter patternPara: pattern.getParameters()) {
				if(para.name.equals(patternPara.name)) {
					// if its an activity, take the name and not the label
					patternPara.value=para.getValue();
				}
			}
			
		}
		// update the pattern representation
		pattern.updateParameterAppliedString();
		for(PatternParameter patternPara: pattern.getParameters()) {
			if(patternPara.type=="activity") {
					// if its an activity, take the name and not the label
				patternPara.value=transitionDic.get(patternPara.value);
			}
		}
			
	}

	public String getPatternName() {
		// TODO Auto-generated method stub
		return pattern.getName();
	}
	
}
