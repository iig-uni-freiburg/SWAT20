package de.uni.freiburg.iig.telematik.swat.editor.actions.pn;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.JOptionPane;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPN;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;

public class CheckValidityAction extends AbstractPNEditorAction {

	private static final long serialVersionUID = 4315293729223367039L;

	public CheckValidityAction(PNEditor pnEditor) throws ParameterException, PropertyException, IOException {
		super(pnEditor, "CheckSoundness", IconFactory.getIcon("validcwn"));
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		if (editor != null) {
			AbstractPetriNet pn = editor.getNetContainer().getPetriNet();
			if (pn instanceof CPN) {
				CPN cpn = (CPN) pn;

			try{
					cpn.checkValidity();
					JOptionPane.showMessageDialog(editor.getGraphComponent(), "Awesome! You're Coloured Workflow Net is VALID.", "CWN is VALID - Awesome Job!", JOptionPane.INFORMATION_MESSAGE);

				} catch (PNValidationException e1) {
					JOptionPane.showMessageDialog(editor.getGraphComponent(), e1.getMessage(), "CWN Validation Failed", JOptionPane.ERROR_MESSAGE);
				} 
				
			}
		}		
	}
}
