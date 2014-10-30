package de.uni.freiburg.iig.telematik.swat.editor.actions.graphics;

import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;

public abstract class AbstractPNEditorGraphicsAction extends AbstractPNEditorAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8723535025613390455L;


	public AbstractPNEditorGraphicsAction(PNEditor editor, String name, Icon icon) throws ParameterException {
		super(editor, name, icon);
		// TODO Auto-generated constructor stub
	}

	public AbstractPNEditorGraphicsAction(PNEditor editor, String name) throws ParameterException {
		super(editor, name);
		// TODO Auto-generated constructor stub
	}

	public AbstractPNEditorGraphicsAction(PNEditor editor) throws ParameterException {
		super(editor);
		// TODO Auto-generated constructor stub
	}


	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		if(getGraph().isLabelSelected())
		performLabelAction();
		else {
		performNoLabelAction();	
		}
		doMoreFancyStuff(e);
		updateViewWithSelectedCell();
	}

	protected abstract void performLabelAction();
	protected abstract void performNoLabelAction();	
	protected abstract void doMoreFancyStuff(ActionEvent e) throws Exception;




}
