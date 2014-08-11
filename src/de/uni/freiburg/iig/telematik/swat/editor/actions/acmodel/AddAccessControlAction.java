package de.uni.freiburg.iig.telematik.swat.editor.actions.acmodel;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.SwingUtilities;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.secsy.logic.generator.Context;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.util.PNUtils;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.ACModel;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.swat.editor.menu.acmodel.ACModelForSWATDialog;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;

public class AddAccessControlAction extends AbstractPNEditorAction {

	private static final long serialVersionUID = 4315293729223367039L;
	
	public AddAccessControlAction(PNEditor pnEditor) throws ParameterException, PropertyException, IOException {
		super(pnEditor, "New Organizational Context", IconFactory.getIcon("accesscontrol"));
	}

	public void actionPerformed(ActionEvent e) {
		if (editor != null) {
			IFNet ifNet = (IFNet) getEditor().getNetContainer().getPetriNet();
			
			
			
						String name = ifNet.getName() +"_Context";
						Set<String> transitions =	PNUtils.getNameSetFromTransitions(ifNet.getTransitions(), true);
						Context context = new Context(name , transitions);
						Set<String> initialSubjects =  new HashSet<String>();
						initialSubjects.add("initial Subject");
						context.setSubjects(initialSubjects);
//						ACModel aclModel = new ACLModel("newACLModel", transitions);
//						aclModel.setTransactions(transitions);
//						context.setACModel(aclModel);
//						cont
//					System.out.println(	context.getActivities());
						Window window = SwingUtilities.getWindowAncestor(getEditor().getGraphComponent());
						ACModel acModel = ACModelForSWATDialog.showDialog(window, context );
						if(acModel != null){
							context.setACModel(acModel);
//							acModelAssigned = true;
//							txtACModelName.setText(acModel.getName());
//							updateVisibility();
						}
					
//						JOptionPane.showMessageDialog(ContextDialog.this, e1.getMessage(), "Cannot set access control model.", JOptionPane.ERROR_MESSAGE);
					
				
		
			
		}
	}
}
