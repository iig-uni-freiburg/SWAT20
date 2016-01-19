package de.uni.freiburg.iig.telematik.swat.simon.gui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPopupMenu;

import de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions.DistributionType;

public class CreateTimeBehaviourAction extends AbstractAction {

	private static final long serialVersionUID = -1117202365016832417L;

	public CreateTimeBehaviourAction() {
		super("change");
	}

	public CreateTimeBehaviourAction(String name) {
		super(name);
	}

	public CreateTimeBehaviourAction(String name, Icon icon) {
		super(name, icon);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JPopupMenu menu = new JPopupMenu();
		for (DistributionType type:DistributionType.values()){
			if(!type.equals(DistributionType.UNKNOWN))
				menu.add(new SpecificDistributionAction(type));
		}
		
		JButton source = (JButton) e.getSource();
		menu.setInvoker((Component) e.getSource());
		menu.setLocation((int) source.getLocationOnScreen().getX(), (int) source.getLocationOnScreen().getY());
		menu.setVisible(true);
	}

}
