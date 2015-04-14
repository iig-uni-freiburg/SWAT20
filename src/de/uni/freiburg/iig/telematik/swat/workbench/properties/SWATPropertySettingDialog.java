package de.uni.freiburg.iig.telematik.swat.workbench.properties;

import java.awt.Window;
import java.io.IOException;

import javax.swing.JPanel;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.WGPropertySettingDialog;

public class SWATPropertySettingDialog extends WGPropertySettingDialog {

	private static final long serialVersionUID = -881220479300190444L;
	SWATPropertySettingPanel swatSettingsPanel;

	public SWATPropertySettingDialog(Window parent) throws PropertyException, IOException {
		super(parent);
		swatSettingsPanel = new SWATPropertySettingPanel();
	}

	@Override
	protected JPanel getPropertySettingPanel() {
		return swatSettingsPanel;
	}

	@Override
	protected void setTitle() {
		setTitle("Edit SWAT Properties");
	}

	@Override
	protected void transferProperties() throws ParameterException, PropertyException, IOException {
		SwatProperties.getInstance().setPNValidation(swatSettingsPanel.getPNValidation());
		super.transferProperties();
	}

	public static void showDialog(Window parent) throws Exception {
		SWATPropertySettingDialog dialog = new SWATPropertySettingDialog(parent);
		dialog.setUpGUI();
	}

	public static void main(String[] args) throws Exception {
		SWATPropertySettingDialog.showDialog(null);
	}

}
