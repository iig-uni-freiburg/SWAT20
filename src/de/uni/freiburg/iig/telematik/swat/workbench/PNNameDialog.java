package de.uni.freiburg.iig.telematik.swat.workbench;

import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;
import java.awt.Window;

import de.invation.code.toval.graphic.dialog.ExceptionDialog;
import de.invation.code.toval.graphic.dialog.FileNameDialog;
import de.invation.code.toval.misc.wd.ProjectComponentException;

public class PNNameDialog extends FileNameDialog {

    public PNNameDialog(Window parent, String message, String title, boolean allowSpaces) {
        super(parent, message, title, allowSpaces);
    }

    @Override
    protected boolean isValid(String input) {
        if (!super.isValid(input)) {
            return false;
        }
        try {
            return !SwatComponents.getInstance().getContainerPetriNets().containsComponent(input);
        } catch (ProjectComponentException ex) {
            ExceptionDialog.showException(parent, "Exception", new Exception("Cannot access swat components", ex), true, false);
            return false;
        }
    }

}
