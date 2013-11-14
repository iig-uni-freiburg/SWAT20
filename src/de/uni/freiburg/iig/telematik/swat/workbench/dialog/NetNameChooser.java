package de.uni.freiburg.iig.telematik.swat.workbench.dialog;

import java.awt.Window;

import de.invation.code.toval.graphic.FileNameChooser;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;

public class NetNameChooser extends FileNameChooser {
	
	private static final String NET_NAME_ALREADY_IN_USE = "This net name is already in use";

	public NetNameChooser(Window parent, String title) {
		super(parent, "Choose name for new net:", title, false);
	}

	@Override
	protected boolean isValid(String input) {
		if(super.isValid(input)){
			if(SwatComponents.getInstance().containsNetWithFileName(input)){
				errorMessage = NET_NAME_ALREADY_IN_USE;
				return false;
			}
			return true;
		}
		return false;
	}

}
