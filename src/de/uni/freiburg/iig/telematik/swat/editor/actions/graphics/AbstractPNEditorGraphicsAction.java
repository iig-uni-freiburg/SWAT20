package de.uni.freiburg.iig.telematik.swat.editor.actions.graphics;

import java.awt.event.ActionEvent;

import javax.swing.Icon;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.AbstractPNEditorAction;

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
