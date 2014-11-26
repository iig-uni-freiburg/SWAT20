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
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;

public class PTImportAction extends AbstractWorkbenchAction {

	public PTImportAction() {
		this("Import Net");
	}

	public PTImportAction(String name) {
		super(name);
		try {
			setIcon(IconFactory.getIcon("import"));
		} catch (ParameterException e) {
			e.printStackTrace();
		} catch (PropertyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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
		if(net.getPetriNet().getName().isEmpty() || SwatComponents.getInstance().containsPetriNetWithID(net.getPetriNet().getName())){
			String name = requestNetName("Name for imported net?", "New name?");
			net.getPetriNet().setName(name);
		}
		SwatComponents.getInstance().addPetriNet(net);
	}
	
	private String requestNetName(String message, String title) {
		return new PNNameDialog(SwingUtilities.getWindowAncestor(Workbench.getInstance()), message, title, false).requestInput();
	}
}