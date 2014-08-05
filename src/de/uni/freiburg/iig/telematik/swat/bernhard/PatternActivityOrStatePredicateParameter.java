package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import de.uni.freiburg.iig.telematik.swat.lukas.OperandType;
import de.uni.freiburg.iig.telematik.swat.lukas.ParamValue;

public class PatternActivityOrStatePredicateParameter extends
		PatternParameterPanel {

	private PetriNetInformationReader informationReader;
	private PatternDropDownParameter activityPanel;
	private PatternMultipleParameterPanel statePredicatePanel;
	boolean activityActive;
	private JComboBox chooseTypeBox;
	public PatternActivityOrStatePredicateParameter(String name, PetriNetInformationReader pr) {
		super(name);
		informationReader=pr;
		activityPanel=new PatternDropDownParameter(name, OperandType.TRANSITION, informationReader.getActivitiesArray());
		statePredicatePanel=new PatternStatePredicateParameter(name, informationReader);
		activityActive=true;
		String arr[]={"Activity","State Predicate"};
		chooseTypeBox=new JComboBox(arr);
		chooseTypeBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				// TODO Auto-generated method stub
				if(arg0.getItem().equals("Activity")) {
					setTypeActivity();
				} else {
					setTypeStatePredicate();
				}
				
			}
			
		});
		content=new JPanel(new FlowLayout(FlowLayout.LEFT));
		content.add(chooseTypeBox);
		content.add(activityPanel.getContent());
		// TODO Auto-generated constructor stub
	}

	protected void setTypeStatePredicate() {
		// TODO Auto-generated method stub
		content.remove(activityPanel.getContent());
		content.add(statePredicatePanel.getContent());
		
		activityActive=false;
		content.repaint();
		content.updateUI();
	}

	protected void setTypeActivity() {
		// TODO Auto-generated method stub
		content.remove(statePredicatePanel.getContent());
		content.add(activityPanel.getContent());
		activityActive=true;
		content.repaint();
		content.updateUI();
	}

	@Override
	public List<ParamValue> getValue() {
		// TODO Auto-generated method stub
		if(activityActive) {
			return activityPanel.getValue();
		} else {
			return statePredicatePanel.getValue();
		}
	}

	@Override
	public void setValue(List<ParamValue> value) {
		// TODO Auto-generated method stub
		ParamValue val=value.get(0);
		if(val.getOperandType() == OperandType.TRANSITION) {
			chooseTypeBox.setSelectedItem("Activity");
			setTypeActivity();
			activityPanel.setValue(value);
		} else if (val.getOperandType() == OperandType.STATEPREDICATE) {
			chooseTypeBox.setSelectedItem("State Predicate");
			setTypeStatePredicate();
			statePredicatePanel.setValue(value);
		}
	}

}
