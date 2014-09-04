package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import de.uni.freiburg.iig.telematik.swat.lukas.OperandType;
import de.uni.freiburg.iig.telematik.swat.lukas.ParamValue;
/**
 * This class represents a Parameter which can either be an activity or
 * a state predicate. The user can choose the type of the parameter
 * and then the value of the parameter. Therefore a TransitionParameter
 * and a StatePredicateParameter are combined.
 * @author bernhard
 *
 */
public class ActivityOrStatePredicateParameter extends
		ParameterPanel {

	private PetriNetInformationReader informationReader;
	private TransitionParameter activityPanel;
	private StatePredicateParameter statePredicatePanel;
	boolean activityActive;
	private JComboBox chooseTypeBox;
	/**
	 * Create a Parameter that can either be an Activity or a state
	 * predicate.
	 * @param name The name of the Parameter e.g. "Q" or "P"
	 * @param pr An instance of a class implementing an PNReader that
	 * can extract the necessary information from the PN.
	 */
	public ActivityOrStatePredicateParameter(String name, PetriNetInformationReader pr) {
		super(name);
		informationReader=pr;
		activityPanel=new TransitionParameter(name,pr);
		statePredicatePanel=new StatePredicateParameter(name, informationReader);
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
	/**
	 * Invoked when the user selects State Predicate for the type
	 */
	protected void setTypeStatePredicate() {
		// TODO Auto-generated method stub
		content.remove(activityPanel.getContent());
		content.add(statePredicatePanel.getContent());
		
		activityActive=false;
		content.repaint();
		content.updateUI();
	}
	/**
	 * Invoked when the user selectes Activity for the type
	 */
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
