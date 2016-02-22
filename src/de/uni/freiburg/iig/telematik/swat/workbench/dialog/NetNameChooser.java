package de.uni.freiburg.iig.telematik.swat.workbench.dialog;

import java.awt.Window;

import de.invation.code.toval.graphic.dialog.FileNameDialog;
import de.invation.code.toval.misc.wd.ProjectComponentException;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;

public class NetNameChooser extends FileNameDialog {
	
	private static final String NET_NAME_ALREADY_IN_USE = "This net name is already in use";

	public NetNameChooser(Window parent, String title) {
		super(parent, "Choose name for new net:", title, false);
	}

	@Override
	protected boolean isValid(String input) {
		if(super.isValid(input)){
                    try {
                        if(SwatComponents.getInstance().getContainerPetriNets().containsComponent(input)){
                            errorMessage = NET_NAME_ALREADY_IN_USE;
                            return false;
                        }
                        return true;
                    } catch (ProjectComponentException ex) {
                        return true;
                    }
		}
		return false;
	}

}
