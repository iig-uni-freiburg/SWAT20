package de.uni.freiburg.iig.telematik.swat.editor.actions.pn;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNSoundnessException;
import de.uni.freiburg.iig.telematik.sepia.exception.PNValidationException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNTransition;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.swat.editor.graph.change.ConstraintChange;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;

public class ChecKSoundnessAction extends AbstractPNEditorAction {

	private static final long serialVersionUID = 4315293729223367039L;

	public ChecKSoundnessAction(PNEditor pnEditor) throws ParameterException, PropertyException, IOException {
		super(pnEditor, "CheckSoundness", IconFactory.getIcon("soundcwn"));
	}

	public void actionPerformed(ActionEvent e) {
		if (editor != null) {
			AbstractPetriNet pn = editor.getNetContainer().getPetriNet();
			if (pn instanceof CPN) {
				CPN cpn = (CPN) pn;
				try {
					cpn.checkSoundness(true);
					JOptionPane.showMessageDialog(editor.getGraphComponent(), "Awesome! You have a SOUND Coloured Workflow Net.", "CWN is Sound - Awesome Job!", JOptionPane.INFORMATION_MESSAGE);

				} catch (PNValidationException e1) {
					JOptionPane.showMessageDialog(editor.getGraphComponent(), e1.getMessage(), "CWN Validation Failed", JOptionPane.ERROR_MESSAGE);
				} catch (PNSoundnessException e1) {
					JOptionPane.showMessageDialog(editor.getGraphComponent(), e1.getMessage(), "CWN Is Not Sound", JOptionPane.ERROR_MESSAGE);
				}
				
			}
		}
	}
}
