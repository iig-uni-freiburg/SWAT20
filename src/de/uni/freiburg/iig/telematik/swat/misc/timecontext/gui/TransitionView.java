package de.uni.freiburg.iig.telematik.swat.misc.timecontext.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.panelmatic.PanelBuilder;
import org.panelmatic.PanelBuilder.HeaderLevel;
import org.panelmatic.PanelMatic;

import de.uni.freiburg.iig.telematik.swat.misc.timecontext.StochasticTimeBehavior;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.TimeBehavior;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.TimeContext;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions.AbstractDistributionView;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions.ExponentialDistributionView;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions.IDistributionView;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.exception.SwatComponentException;

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
		AbstractDistributionView tview = new ExponentialDistributionView();
		tview.setParams(0.3, 1);
		timeContext.addTimeBehavior("test", tview);

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
		//setVisible(true);
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
		if (behavior instanceof StochasticTimeBehavior || behavior instanceof IDistributionView) {
			distributionType.setText(behavior.toString());
		}
		distributionType.revalidate();

	}

	private void initialize() {
		setLocationByPlatform(true);
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
		okBtn = getOKButton();

		cancelBtn = getCancelButton();
		setDistribution = new JButton("Set time behavior");
		setDistribution.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				DistributionChooser chooser = new DistributionChooser(timeContext.getTimeBehavior(transitionName));
				chooser.choose();
				if (!chooser.userAbort()) {
					timeContext.addTimeBehavior(transitionName, chooser.getDistribution());
					setDistributionTypeText();
				}
			}
		});

		getDistributionFromLog = new JButton("load from log");
	}

	private JButton getOKButton() {
		if (okBtn == null) {
			okBtn = new JButton("OK");
			okBtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					try {
						SwatComponents.getInstance().storeTimeContext(timeContext, timeContext.getCorrespondingNet());
					} catch (SwatComponentException e) {
						JOptionPane.showMessageDialog(null, "Could not save time context:\nReason: " + e.getMessage());
					} catch (NullPointerException e1) {
						JOptionPane.showMessageDialog(null, "Could not save time context:\nReason: " + e1.getMessage());
						e1.printStackTrace();
					}
					//					try {
					//						timeContext.storeTimeContext();
					//					} catch (FileNotFoundException e) {
					//						e.printStackTrace();
					//					} finally {
					//						dispose();
					//					}
					dispose();

				}
			});
		}
		return okBtn;
	}

	private JButton getCancelButton() {
		if (cancelBtn == null) {
			cancelBtn = new JButton("Cancel");
			cancelBtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return cancelBtn;
	}
}
