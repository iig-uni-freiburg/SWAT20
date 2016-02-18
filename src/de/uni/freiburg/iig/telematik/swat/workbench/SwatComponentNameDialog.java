package de.uni.freiburg.iig.telematik.swat.workbench;

import java.awt.Window;

import de.invation.code.toval.misc.wd.ProjectComponentException;
import de.invation.code.toval.validate.ExceptionDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponentType;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;

public class SwatComponentNameDialog extends PNNameDialog {
	SwatComponentType type;

	public SwatComponentNameDialog(Window parent, String message, String title, boolean allowSpaces) {
		super(parent, message, title, allowSpaces);
		this.type=SwatComponentType.PETRI_NET;
	}
	
	public SwatComponentNameDialog(Window parent, String message, String title, boolean allowSpaces, SwatComponentType type){
		super(parent, message,title,allowSpaces);
		this.type=type;
	}
	
    @Override
    protected boolean isValid(String input) {
        if (!super.isValid(input)) {
            return false;
        }
        try {
            return !isContained(input);
        } catch (ProjectComponentException ex) {
            ExceptionDialog.showException(parent, "Exception", new Exception("Cannot access swat components", ex), true, false);
            return false;
        }
    }
    
    protected boolean isContained(String input) throws ProjectComponentException{
    	switch (type) {
		case PETRI_NET:
			return SwatComponents.getInstance().getContainerPetriNets().containsComponent(input);
		case MXML_LOG:
			return SwatComponents.getInstance().getContainerMXMLLogs().containsComponent(input);
		case XES_LOG:
			return SwatComponents.getInstance().getContainerXESLogs().containsComponent(input);
		case ARISTAFLOW_LOG:
			return SwatComponents.getInstance().getContainerAristaflowLogs().containsComponent(input);
                case LOG_VIEW:
                        return SwatComponents.getInstance().getContainerLogViews().containsComponent(input);
		default:
			throw new ProjectComponentException("can only access Nets, Logs, and Views");
		}
    }
}
