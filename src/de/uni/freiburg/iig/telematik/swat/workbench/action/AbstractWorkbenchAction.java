package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.swat.misc.errorhandling.ErrorStorage;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeView;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;

public abstract class AbstractWorkbenchAction extends AbstractAction {

	private static final long serialVersionUID = -4935315995834697583L;

	private ImageIcon icon;
	
	public AbstractWorkbenchAction(String name) {
		super(name);
	}

	public AbstractWorkbenchAction(String name, Icon icon) {
		super(name, icon);
	}
	
	protected void setIcon(Icon icon) throws ParameterException {
		Validate.notNull(icon);
		this.icon = (ImageIcon) icon;
		putValue(SMALL_ICON, icon);
		putValue(LARGE_ICON_KEY, icon);
	}
	protected ImageIcon getIcon() {
		return icon;
	}

	protected void setTooltip(String toolTip) {
		Validate.notNull(toolTip);
		Validate.notEmpty(toolTip);
		putValue(SHORT_DESCRIPTION, toolTip);
	}

	protected String getTooltip() {
		return (String) getValue(SHORT_DESCRIPTION);
	}

	protected void setText(String buttonText) {
		Validate.notNull(buttonText);
		Validate.notEmpty(buttonText);
		putValue(NAME, buttonText);
	}

	protected String getText() {
		return (String) getValue(NAME);
	}

	protected void setAcceleratorKey(KeyStroke key) {
		putValue(ACCELERATOR_KEY, key);
	}

	protected Window getTreeViewParent() throws Exception {
                return SwingUtilities.getWindowAncestor(SwatTreeView.getInstance().getParent());
             
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			doFancyStuff(e);
		} catch(Exception ex){
			Workbench.errorMessage(this.getClass().getSimpleName(), ex, true);
			ErrorStorage.getInstance().addMessage("Error during AbstractWorkbenchAction " + this.getClass().getSimpleName(), ex);
		}
	}
	
	protected abstract void doFancyStuff(ActionEvent e) throws Exception;
	
	
	
}
