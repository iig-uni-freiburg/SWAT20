package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.lukas.OperandType;
import de.uni.freiburg.iig.telematik.swat.lukas.ParamValue;

public class PatternMultipleParameterPanel extends PatternParameterPanel {

	private List<PatternParameterPanel> panelList;
	private JButton addButton;
	private String[] values;
	public PatternMultipleParameterPanel(String name, String transitions[]) {
		super(name);
		values=transitions;
		panelList=new ArrayList<PatternParameterPanel>();
		try {
			addButton=new JButton(IconFactory.getIcon("maximize"));
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
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addParameter();
			}
		});
		content=new JPanel(new GridLayout(
				PatternAnalyzeLogic.MAX_PATTERNS, 1, 10, 10));
		content.add(addButton);
		// TODO Auto-generated constructor stub
	}

	protected void addParameter() {
		// TODO Auto-generated method stub
		PatternParameterPanel p=new PatternActivityParameterPanel("1",values);
		panelList.add(p);
		content.add(p.getContent());
	}

	@Override
	public List<ParamValue> getValue() {
		// TODO Auto-generated method stub
		ArrayList<ParamValue> values=new ArrayList<ParamValue>();
		for(PatternParameterPanel panel: panelList) {
			values.addAll(panel.getValue());
		}
		return values;
	}

	@Override
	public void setValue(List<ParamValue> value) {
		// TODO Auto-generated method stub

	}

}
