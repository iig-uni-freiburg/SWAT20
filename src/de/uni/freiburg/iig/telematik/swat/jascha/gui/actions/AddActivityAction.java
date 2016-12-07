package de.uni.freiburg.iig.telematik.swat.jascha.gui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResource;
import de.uni.freiburg.iig.telematik.swat.jascha.AwesomeResourceContext;

public class AddActivityAction extends AbstractAction {

	// private AwesomeResourceContext context;
	private List<String> hints;
	private JComboBox<String> comboBox;
	DefaultListModel<String> activities;

	public AddActivityAction(DefaultListModel<String> activities, List<String> activityHints) {
		super("add activity");
		this.activities = activities;
		this.hints = activityHints;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int result = JOptionPane.showConfirmDialog((Component) e.getSource(), getDialogPanel(), "Add activity...",
				JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION && comboBox.getSelectedItem()!=null) {
			if(!activities.contains(comboBox.getSelectedItem()))
				activities.addElement((String) comboBox.getSelectedItem());
			else
				JOptionPane.showMessageDialog((Component) e.getSource(), "activity already present");
		}

	}

	public JPanel getDialogPanel() {
		JPanel panel = new JPanel();
		if (getHints() != null)
			comboBox = new JComboBox<>(getHints());
		else
			comboBox = new JComboBox<>();
		comboBox.setEditable(true);
		panel.add(comboBox);
		return panel;
	}

	private String[] getHints() {
		try {
			String[] array = {};
			String[] result = hints.toArray(array);
			return result;
		} catch (NullPointerException e) {
			return null;
		}
	}

}