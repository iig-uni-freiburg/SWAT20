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
	private JButton counterExampleButton;
	private String patternName;
	private List<PatternParameter> parameterList;
	String[] transactions = { "A", "B", "C" };
	String[] data = { "red", "green", "blue" };

	/**
	 * create a panel for a given pattern
	 * @param pattern
	 * @param parent give the patternPanel to which this panel will be added
	 * @param pneditor the current pneditor
	 */
	public PatternSettingPanel(String pattern, final JPanel parent,
			PNEditor pneditor) {
		patternName = pattern;
		List<String> paraList = PatternDatabase
				.getParameterForPattern(pattern);
		panel = new JPanel(new GridLayout(paraList.size() + 1, 2));
		// System.out.println(paraList);
		// System.out.println(paraList.size());
		parameterList = new ArrayList<PatternParameter>();
		if (pneditor != null) {
			transactions = getTransactionLabelList(pneditor);
			//data= getDataTypeList(pneditor);
		}

		JLabel label = new JLabel(pattern);
		Font font = label.getFont();
		// same font but bold
		Font boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize()+3);
		label.setFont(boldFont);
		panel.add(label);

		
		try {
			removeButton = new JButton(IconFactory.getIcon("minimize"));
			removeButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					parent.remove(panel);
					parent.updateUI();
				}
			});
			rightPanel=new JPanel(new FlowLayout(FlowLayout.LEFT));
			rightPanel.add(removeButton);
			counterExampleButton=new JButton("Show CounterExample");
			rightPanel.add(counterExampleButton);
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

		for (String para : paraList) {
			PatternParameter patternPara = null;
			if (para.equals("Data")) {
				patternPara = new PatternDataParameter(data);
				panel.add(new JLabel("Choose Data:"));
				panel.add(patternPara.getjComponent());
			} else if (para.equals("Activity")) {
				panel.add(new JLabel("Activity:"));
				patternPara = new PatternActivityParameter("A", transactions);
				panel.add(patternPara.getjComponent());
			}
			parameterList.add(patternPara);
		}

		parent.add(panel);
		parent.updateUI();
		;
	}

	public void removeCounterExampleButtons() {
		
	}
	public void addCounterExample(final CounterExample ce, final PatternWindow w) {
		counterExampleButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					w.highLightCounterExample(ce);
				}
			});
	}
	private String[] getDataTypeList(PNEditor pneditor) {
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
		return null;
	}

	/**
	 * return a list of the entered values
	 * @return
	 */
	public String getValues() {
		String paraString = "";
		for (PatternParameter para : parameterList) {
			paraString += para.getValue() + ":";
		}
		return paraString;
	}

	public String getPatternName() {
		return patternName;
	}

	public void setPatternName(String patternName) {
		this.patternName = patternName;
	}

	public JPanel getJPanel() {
		return panel;
	}

	/**
	 * Helpfunction to get the List of all Labels of the current PN of editor
	 * @param editor
	 * @return
	 */
	private String[] getTransactionLabelList(PNEditor editor) {
		
		List<String> result = new ArrayList<String>();
		for(AbstractTransition transition : editor.getNetContainer().getPetriNet().getTransitions()){
			result.add(transition.getLabel());
		}
		Collections.sort(result);
		return result.toArray(new String[0]);
	}
	
}
