package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.PTGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.swat.bpmn2pn.BPMN2PNStartup;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.workbench.PNNameDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;

public class BpmnImportAction extends AbstractWorkbenchAction {

	private static final long serialVersionUID = -1045319694736116999L;

	public BpmnImportAction() {
		super("Import BPMN");
		try {
			setIcon(IconFactory.getIcon("import"));
		} catch (ParameterException | PropertyException | IOException e) {
		}
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		File f = getFile();
		if (f == null) return;
		PTNet net = BPMN2PNStartup.generateIFnet(f.getAbsolutePath());
		
		String name = requestNetName("Please enter name for net", "Net-Name");
		net.setName(name);
		GraphicalPTNet result = new GraphicalPTNet(net, new PTGraphics());
		SwatComponents.getInstance().getContainerPetriNets().addComponent(result,true);
		SwatComponents.getInstance().getContainerPetriNets().setAskForLayout(name);
		Workbench.consoleMessage("Net Imported "+name);
	}
	
	protected File getFile() throws Exception {
		File f = null;
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("bpmn", "bpmn");
		chooser.setFileFilter(filter);
		chooser.setAcceptAllFileFilterUsed(false);
		int returnVal = chooser.showOpenDialog(Workbench.getInstance());
		if(returnVal == JFileChooser.APPROVE_OPTION){
			f = chooser.getSelectedFile();
		}
		return f;
	}
	
	private String requestNetName(String message, String title) throws Exception {
		return new PNNameDialog(SwingUtilities.getWindowAncestor(Workbench.getInstance()), message, title, false).requestInput();
	}

}
