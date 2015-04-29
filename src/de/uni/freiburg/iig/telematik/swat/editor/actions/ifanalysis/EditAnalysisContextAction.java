package de.uni.freiburg.iig.telematik.swat.editor.actions.ifanalysis;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.JOptionPane;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.editor.actions.IFNetContextAbstractAction;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

public class EditAnalysisContextAction extends IFNetContextAbstractAction {

	private static final long serialVersionUID = 4315293729223367039L;

	public EditAnalysisContextAction(PNEditorComponent pnEditor) throws ParameterException, PropertyException, IOException {
		super(pnEditor, "New Analysis Context", IconFactory.getIcon("plus_analysis_context"));
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		
		if (getPreceedingDependency() != null && getPreceedingDependency().getModelName() != null) {
			int i = JOptionPane.showConfirmDialog(getEditor().getGraphComponent(), "Is an Analysis Context selected?");
			if (i == 0)
				setSelectedModel("Gerd");
			else if (i == 1)
				setSelectedModel(null);
			else if (i == 2) {
				// do nothing
			}

		} else
			JOptionPane.showMessageDialog(getEditor().getGraphComponent(), "Please first select an AC Model");
		// TODO: Adapt to new ACStructure
		// IFNet ifNet = (IFNet) getEditor().getNetContainer().getPetriNet();
		// if (SwatComponents.getInstance().containsACModels()) {
		// AbstractACModel acModel = ((IFNetGraph)
		// editor.getGraphComponent().getGraph()).getSelectedACModel();
		//
		// if (acModel != null) {
		//
		// Set<String> ifSubjects = new HashSet<String>();
		// if (acModel instanceof ACLModel) {
		// ifSubjects = acModel.getContext().getSubjects();
		// }
		// if (acModel instanceof RBACModel) {
		// ifSubjects = ((RBACModel) acModel).getRoles();
		// }
		// ;
		// String name =
		// requestFileName("Pleache choose the name for new Analysis Context for "
		// + getEditor().getNetContainer().getPetriNet().getName() + " :",
		// "New Analysis Context");
		// if (name != null) {
		// AnalysisContext ac = new AnalysisContext(acModel.getContext());
		// ac.setName(name);
		// for (String a : ac.getActivities()) {
		// ac.setSubjectDescriptor(a, ifSubjects.iterator().next());
		// }
		//
		// // Labelings sichtbar machen
		// ((mxGraphModel)
		// editor.getGraphComponent().getGraph().getModel()).execute(new
		// AnalysisContextChange(editor, ac));
		//
		// try {
		// SwatComponents.getInstance().addAnalysisContext(ac,
		// editor.getNetContainer().getPetriNet().getName(), false);
		// } catch (SwatComponentException e1) {
		// JOptionPane.showMessageDialog(editor.getGraphComponent(),
		// "Analysis Context could not be Added",
		// "Analysis Context not added", JOptionPane.ERROR_MESSAGE);
		// e1.printStackTrace();
		// }
		// ((IFNetToolBar)editor.getEditorToolbar()).addAnalysisContextToComboBox(name);
		//
		// }
		// }
		// } else
		// JOptionPane.showMessageDialog(editor.getGraphComponent(),
		// "You first need to define your Access Controll Model and its Subjects to build an appropriate Analysis Context",
		// "Subjects missing", JOptionPane.ERROR_MESSAGE);
		//

	}

	@Override
	protected String getButtonSpecificToolTipForSelection() {
		return "Selected Analysis Context: " + "\"" + getModelName() + "\"";
	}

	@Override
	protected String getButtonSpecificToolTipForNonSelection() {
		return "No Analysis Context Selected";
	}

	@Override
	protected void setConsequencesForSelection() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setConsequencesForNonSelection() {
		getFollowingDependency().setSelectedModel(null);	
	}


}
