package de.uni.freiburg.iig.telematik.swat.workbench.dialog;

import de.invation.code.toval.debug.SimpleDebugger;
import java.awt.Window;
import java.beans.PropertyChangeListener;
import de.invation.code.toval.misc.wd.AbstractWorkingDirectoryDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperty;
import javax.swing.border.Border;


public class WorkingDirectoryDialog extends AbstractWorkingDirectoryDialog<SwatProperty> implements PropertyChangeListener {

    private static final long serialVersionUID = 2306027725394345926L;

    public WorkingDirectoryDialog(Window owner) throws Exception {
        super(owner, SwatProperties.getInstance());
    }

    public WorkingDirectoryDialog(Window owner, SimpleDebugger debugger) throws Exception {
        super(owner, SwatProperties.getInstance(), debugger);
    }
    
    public static String showDialog(Window owner) throws Exception {
        return showDialog(owner, null);
    }
    
    public static String showDialog(Window owner, SimpleDebugger debugger) throws Exception {
        WorkingDirectoryDialog dialog = new WorkingDirectoryDialog(owner, debugger);
        dialog.setUpGUI();
        return dialog.getDialogObject();
    }

}
