package de.uni.freiburg.iig.telematik.swat.misc.timecontext.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.apache.commons.math3.distribution.AbstractRealDistribution;

import de.uni.freiburg.iig.telematik.swat.misc.timecontext.gui.distributions.DistributionType;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.gui.distributions.DistributionViewFactory;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.gui.distributions.IDistributionView;

public class DistributionChooser extends JDialog implements ActionListener {
	JComboBox selector;
	JPanel panel = new JPanel(); //or could use cardLayout
	JButton ok_btn = new JButton("OK");
	JButton cancel_btn = new JButton("Cancel");
	boolean okay = false;

	public static void main(String[] args) {
		DistributionChooser chooser = new DistributionChooser();
		chooser.choose();
		if (!chooser.userAbort())
			System.out.println("Distribution is: " + chooser.getDistribution());
	}

	public AbstractRealDistribution getDistribution() {
		return ((IDistributionView) selector.getSelectedItem()).getDistribution();
	}

	public void choose() {
		//setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setVisible(true);
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
		setSize(300, 300);
		selector = new JComboBox(getDistributionViews());
		selector.addActionListener(this);
		this.add(selector);
		panel.add(((IDistributionView) selector.getSelectedItem()).getConfigView());
		this.add(panel);
		this.add(cancel_btn);
		this.add(ok_btn);
		//this.setVisible(true);
	}

	private IDistributionView[] getDistributionViews() {
		ArrayList<IDistributionView> result = new ArrayList<IDistributionView>();
		IDistributionView[] outArray = new IDistributionView[1];
		for (DistributionType type : DistributionType.values()) {
			IDistributionView current = DistributionViewFactory.getDistributionView(type);
			if (current != null)
				result.add(current);
		}
		return result.toArray(outArray);
	}

	/** JComboBox Listener: Repaint GUI on change **/
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
