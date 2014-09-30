package de.uni.freiburg.iig.telematik.swat.misc.timecontext.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.GammaDistribution;
import org.apache.commons.math3.distribution.LogNormalDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.WeibullDistribution;
import org.panelmatic.PanelBuilder;
import org.panelmatic.PanelBuilder.HeaderLevel;
import org.panelmatic.PanelMatic;

import de.uni.freiburg.iig.telematik.swat.misc.timecontext.StochasticTimeBehavior;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.TimeBehavior;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.TimeContext;

public class TransitionView extends JDialog {

	private static final long serialVersionUID = -3530398523113777866L;
	private JButton okBtn;
	private JButton cancelBtn;
	private JButton setDistribution;
	private JButton getDistributionFromLog;
	private JLabel distributionType = new JLabel("unknown");
	private String transitionName;
	private TimeContext timeContext;

	public static void main(String args[]) {
		
		TimeContext timeContext = new TimeContext();
		timeContext.addTimeBehavior("test", new StochasticTimeBehavior(new NormalDistribution(3, 0.2)));

		TransitionView view = new TransitionView("test", timeContext);
		view.setVisible(true);
	}

	public TransitionView(String transitionName, TimeContext timeContext) {
		super();
		this.transitionName = transitionName;
		this.timeContext = timeContext;
		setUpComponents();
		initialize();
		pack();
		setVisible(true);
	}

	private void setDistributionTypeText() {
		if (timeContext == null) {
			distributionType.setText("unknown");
			distributionType.revalidate();
			return;
		}
		TimeBehavior behavior = timeContext.getTimeBehavior(transitionName);
		if (behavior == null || timeContext == null) {
			distributionType.setText("unknown");
			distributionType.revalidate();
			return;
		}
		if (behavior instanceof StochasticTimeBehavior) {
			//savely castable
			AbstractRealDistribution currentDistribution = ((StochasticTimeBehavior) behavior).getDistribution();
			if (currentDistribution instanceof NormalDistribution) {
				distributionType.setText("Normal distributed");
			}
			else if (currentDistribution instanceof LogNormalDistribution) {
				distributionType.setText("Log-Normal distributed");
			} else if (currentDistribution instanceof GammaDistribution) {
				distributionType.setText("Gamme distributed");
			} else if (currentDistribution instanceof WeibullDistribution) {
				distributionType.setText("Weibull distributed");
			}
			else {
				distributionType.setText("unknown");
			}
		}
		distributionType.revalidate();

	}

	private void initialize() {
		this.setSize(300, 400);
		this.setResizable(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		//this.setContentPane(getContent());
		//add(getContent());
		//pack();
		PanelBuilder panelmatic = PanelMatic.begin();

		panelmatic.addHeader(HeaderLevel.H1, "Timing for " + transitionName);
		
		panelmatic.add("Distribution type : ", distributionType);

		panelmatic.add(setDistribution, getDistributionFromLog);

		panelmatic.add(cancelBtn, okBtn);

		this.add(panelmatic.get());
	}

	private JComponent getContent() {
		JPanel panel = new JPanel(new BorderLayout());
		this.add(new JButton("one"), BorderLayout.NORTH);
		this.add(new JButton("two"), BorderLayout.WEST);
		this.add(new JButton("three"), BorderLayout.SOUTH);
		//panel.setVisible(true);
		return panel;
	}

	private void setUpComponents() {
		setDistributionTypeText();
		okBtn = new JButton("OK");
		cancelBtn = new JButton("Cancel");
		setDistribution = new JButton("Set time behavior");
		setDistribution.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				DistributionChooser chooser = new DistributionChooser();
				chooser.choose();
				if (!chooser.userAbort()) {
					timeContext.addTimeBehavior(transitionName, new StochasticTimeBehavior(chooser.getDistribution()));
					setDistributionTypeText();
				}
			}
		});
		getDistributionFromLog = new JButton("load from log");
	}
}
