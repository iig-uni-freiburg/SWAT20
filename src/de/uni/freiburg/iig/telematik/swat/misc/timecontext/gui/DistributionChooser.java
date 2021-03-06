package de.uni.freiburg.iig.telematik.swat.misc.timecontext.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

import de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions.AbstractDistributionView;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions.DistributionType;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions.DistributionViewFactory;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions.IDistributionView;

public class DistributionChooser extends JDialog implements ActionListener {

	private static final long serialVersionUID = 4283113245125626969L;

	JComboBox selector;
	JPanel panel = new JPanel(); //or could use cardLayout
	JButton ok_btn = new JButton("OK");
	JButton cancel_btn = new JButton("Cancel");
	boolean okay = false;

	public static void main(String[] args) {
		AbstractDistributionView view = DistributionViewFactory.getDistributionView(DistributionType.UNIFORM);
		System.out.println("Setting view params");
		view.setParams(1, 2);
		System.out.println("view BEFORE: " + view);
		DistributionChooser chooser = new DistributionChooser(view);
		chooser.choose();
		if (!chooser.userAbort()) {
			System.out.println("USER choosen distribution is: " + chooser.getDistribution());
		}
		System.out.println("view AFTER: " + view);
	}

	public AbstractDistributionView getDistribution() {
		System.out.println("New Distribution: " + selector.getSelectedItem());
		return ((AbstractDistributionView) selector.getSelectedItem());
	}

	public void choose() {
		//setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		super.setVisible(true);
	}

	public void setVisible(boolean b) {
		if (b)
			choose();
	}

	public boolean userAbort() {
		return !okay;
	}

	public DistributionChooser(){
		ok_btn.setActionCommand("OK");
		ok_btn.addActionListener(this);
		cancel_btn.setActionCommand("Cancel");
		cancel_btn.addActionListener(this);
		this.setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(300, 150);
		selector = new JComboBox(getDistributionViews());
		selector.addActionListener(this);
		this.add(selector);
		panel.add(((AbstractDistributionView) selector.getSelectedItem()).getConfigView());
		this.add(panel);
		JPanel buttons = new JPanel();
		buttons.setLayout((new BoxLayout(buttons, BoxLayout.X_AXIS)));
		buttons.add(cancel_btn);
		buttons.add(ok_btn);
		//panel.add(buttons);
		this.add(buttons);

		//this.setVisible(true);
	}

	public DistributionChooser(AbstractDistributionView timeBehavior) {
		this();
		if (timeBehavior != null) {
			AbstractDistributionView currentView = (AbstractDistributionView) selector.getItemAt(getIndexOfTimeBehavior(timeBehavior));
			currentView.setParams(timeBehavior.getParams());
			selector.setSelectedIndex(getIndexOfTimeBehavior(timeBehavior));
		}
	}

	private int getIndexOfTimeBehavior(AbstractDistributionView timeBehavior) {
		if (timeBehavior == null)
			return 0;

		for (int i = 0; i < selector.getItemCount(); i++) {
			AbstractDistributionView selectorItem = (AbstractDistributionView) selector.getItemAt(i);
			if (selectorItem.getType().equals(timeBehavior.getType()))
				return i;
		}

		return 0;
	}

	private AbstractDistributionView[] getDistributionViews() {
		ArrayList<AbstractDistributionView> result = new ArrayList<AbstractDistributionView>();
		AbstractDistributionView[] outArray = new AbstractDistributionView[1];
		for (DistributionType type : DistributionType.values()) {
			AbstractDistributionView current = DistributionViewFactory.getDistributionView(type);
			if (current != null)
				result.add(current);
		}
		return result.toArray(outArray);
	}

	/** JComboBox & Button Listener: Repaint GUI on change **/
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() instanceof JComboBox) {
			JComboBox selectedChoice = (JComboBox) arg0.getSource();
			IDistributionView view = (IDistributionView) selectedChoice.getSelectedItem();
			panel.removeAll();
			panel.add(view.getConfigView());
			panel.repaint();
			panel.validate();
			this.validate();
		}

		else if (arg0.getActionCommand().equals("OK")) {
			okay = true;
			dispose();
		} else if (arg0.getActionCommand().equals("Cancel")) {
			okay = false;
			dispose();
		}
	}

}
