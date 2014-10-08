package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTabView;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeView;

public abstract class AbstractWorkbenchAction extends AbstractAction {

	private static final long serialVersionUID = -4935315995834697583L;

	private ImageIcon icon;

	private SwatTreeView treeView;

	private SwatTabView tabView;
	
	public AbstractWorkbenchAction(SwatTreeView treeView, SwatTabView tabView) {
		super();
		this.treeView = treeView;
		this.tabView = tabView;
	}

	
	public AbstractWorkbenchAction(SwatTreeView treeView, String name) throws ParameterException {
		super(name);
		setTreeView(treeView);
	}
	public AbstractWorkbenchAction(SwatTreeView treeView, SwatTabView tabView, String name) throws ParameterException {
		super(name);
		setTreeView(treeView);
		setTabView(tabView);
	}
	
	public AbstractWorkbenchAction(SwatTreeView treeView, String name, Icon icon) throws ParameterException {
		super(name, icon);
		setTreeView(treeView);
		setIcon(icon);
	}
	
	public AbstractWorkbenchAction(SwatTreeView treeView, SwatTabView tabView, String name, Icon icon) throws ParameterException {
		super(name, icon);
		setTreeView(treeView);
		setTabView(tabView);
		setIcon(icon);
	}
	
	protected void setIcon(Icon icon) throws ParameterException {
		Validate.notNull(icon);
		this.icon = (ImageIcon) icon;
	}
	protected ImageIcon getIcon() {
		return icon;
	}

	protected SwatTreeView getTreeView() {
		return treeView;
	}
	
	public SwatTabView getTabView() {
		return tabView;
	}

	public void setTabView(SwatTabView tabView) {
		this.tabView = tabView;
	}

	public void setTreeView(SwatTreeView treeView) {
		Validate.notNull(treeView);
		this.treeView = treeView;
	}
	protected Window getTreeViewParent() {
		return SwingUtilities.getWindowAncestor(treeView.getParent());
	}
	protected Window getTabViewParent() {
		return SwingUtilities.getWindowAncestor(tabView.getParent());
	}
	
	
}