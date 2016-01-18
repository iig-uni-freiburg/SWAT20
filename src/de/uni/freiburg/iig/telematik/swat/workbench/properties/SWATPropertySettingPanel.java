package de.uni.freiburg.iig.telematik.swat.workbench.properties;

import java.io.IOException;

import javax.swing.JCheckBox;
import javax.swing.JLabel;

import de.invation.code.toval.graphic.util.SpringUtilities;
import de.invation.code.toval.properties.PropertyException;
import de.uni.freiburg.iig.telematik.wolfgang.editor.properties.WGPropertySettingPanel;

public class SWATPropertySettingPanel extends WGPropertySettingPanel {

	private static final long serialVersionUID = 2946945250675438051L;
	
	private JCheckBox checkPNValidation;

	public SWATPropertySettingPanel() throws PropertyException, IOException {
		super();
	}

	@Override
	protected void initialize() throws PropertyException, IOException {
		super.initialize();
		checkPNValidation = new JCheckBox();
		checkPNValidation.setSelected(SwatProperties.getInstance().getPNValidation());
	}
	
	@Override
	protected void addSettingComponents() {
		add(new JLabel("Petri Net Validation:", JLabel.RIGHT));
		add(checkPNValidation);
		super.addSettingComponents();
	}

	@Override
	protected void generateGrid() {
		SpringUtilities.makeCompactGrid(this, 25, 2, 5, 5, 5, 5);
	}
	
	public boolean getPNValidation(){
		return checkPNValidation.isSelected();
	}

}
