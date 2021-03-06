package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.SwingUtilities;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.parser.graphic.PNParserDialog;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.workbench.PNNameDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;

public class PTImportAction extends AbstractWorkbenchAction {

	public PTImportAction() {
		this("Import Net");
	}

	public PTImportAction(String name) {
		super(name);
		try {
			setIcon(IconFactory.getIcon("import"));
		} catch (ParameterException | PropertyException | IOException e) {
			throw new RuntimeException(e);
		}
		setTooltip("Import PT-Net");
	}

	private static final long serialVersionUID = -8945460474139815880L;

	@SuppressWarnings("rawtypes")
	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		AbstractGraphicalPN net = PNParserDialog.showPetriNetDialog(SwingUtilities.getWindowAncestor(Workbench.getInstance()));
		if (net == null)
			return;
		if(net.getPetriNet().getName().isEmpty() || SwatComponents.getInstance().getContainerPetriNets().containsAnalysis((net.getPetriNet().getName()))){
			String name = requestNetName("Name for imported net?", "New name?");
			net.getPetriNet().setName(name);
		}
		SwatComponents.getInstance().getContainerPetriNets().addComponent(net, true);
                Workbench.consoleMessage("Imported " + net.getPetriNet().getName());
	}
	
	private String requestNetName(String message, String title) throws Exception {
		return new PNNameDialog(SwingUtilities.getWindowAncestor(Workbench.getInstance()), message, title, false).requestInput();
	}
}