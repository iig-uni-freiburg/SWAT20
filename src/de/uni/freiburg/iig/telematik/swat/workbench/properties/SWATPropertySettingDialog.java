package de.uni.freiburg.iig.telematik.swat.workbench.properties;

import java.awt.BorderLayout;
import java.awt.Window;
import java.io.IOException;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import de.invation.code.toval.graphic.util.SpringUtilities;
import de.invation.code.toval.properties.PropertyException;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.PropertySettingDialog;

public class SWATPropertySettingDialog extends PropertySettingDialog {

	private JCheckBox chekPNValidation;

	public SWATPropertySettingDialog(Window parent) throws PropertyException, IOException {
		super(parent);

	}

	@Override
	protected void addComponents() throws Exception {
		mainPanel().setLayout(new BorderLayout());
		mainPanel().add(new SWATPropertySettingPanel(), BorderLayout.CENTER);
		mainPanel().add(createNewWGPropertySettingPanel(), BorderLayout.SOUTH);

	}

	@Override
	protected void setTitle() {
		setTitle("Edit SWAT Properties");
	}

	@Override
	protected void okProcedure() {
		try {
			SwatProperties.getInstance().setPNValidation(chekPNValidation.isSelected());
			SwatProperties.getInstance().store();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(SWATPropertySettingDialog.this, "Cannot store all properties:\nReason: " + e.getMessage(), "Exception during property change", JOptionPane.ERROR_MESSAGE);
			return;
		}
		super.okProcedure();
	}

	private void initializeSWATProperties() throws PropertyException, IOException {
		chekPNValidation = new JCheckBox();
		chekPNValidation.setSelected(SwatProperties.getInstance().getPNValidation());

	}

	public class SWATPropertySettingPanel extends JPanel {

		private static final long serialVersionUID = 1066469038019795225L;

		public SWATPropertySettingPanel() throws PropertyException, IOException {
			super(new SpringLayout());
			initializeSWATProperties();
			add(new JLabel("Petri Net Validation:", JLabel.RIGHT));
			add(chekPNValidation);

			SpringUtilities.makeCompactGrid(this, 1, 2, 5, 5, 5, 5);
		}

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5045285017030584295L;

	public static void showDialog(Window parent) throws Exception {
		SWATPropertySettingDialog dialog = new SWATPropertySettingDialog(parent);
		dialog.setUpGUI();
	}

	public static void main(String[] args) throws Exception {
		SWATPropertySettingDialog.showDialog(null);
	}

}
