package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.event.ActionEvent;

import javax.swing.SwingUtilities;

import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalCPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalIFNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.swat.workbench.PNNameDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatNewNetToolbar;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatNewNetToolbar.ToolbarNewNetButtonType;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.WorkbenchPopupToolBar;

public class NewNetAction extends AbstractWorkbenchAction {

	private static final long serialVersionUID = 5661121560829645417L;
	
	private ToolbarNewNetButtonType type;
	private WorkbenchPopupToolBar popupFontToolBar;

	private SwatNewNetToolbar swatNewNetToolbar;

	public NewNetAction(SwatNewNetToolbar.ToolbarNewNetButtonType type, SwatNewNetToolbar swatNewNetToolbar) {
		super("Create new net");
		this.type = type;
		this.swatNewNetToolbar = swatNewNetToolbar;
	}
	
	public NewNetAction(SwatNewNetToolbar.ToolbarNewNetButtonType type) {
		super("Create new net");
		this.type = type;
	}

	@Override
	public void doFancyStuff(ActionEvent e) throws Exception{
		String netName = requestNetName("Please choose a name for the new net:", "New Petri-Net");
		if (swatNewNetToolbar != null)
			swatNewNetToolbar.getToolBar().disposeAllWindows();
		if (netName != null) {
			// Test new file name
			switch (type) {
			case NEW_CPN:
				GraphicalCPN newCPN = new GraphicalCPN();
				newCPN.getPetriNet().setName(netName);
                                SwatComponents.getInstance().getContainerPetriNets().addComponent(newCPN, true);
				//SwatComponents.getInstance().addPetriNet(newCPN);
				break;
			case NEW_PT:
				GraphicalPTNet newPTNet = new GraphicalPTNet();
				newPTNet.getPetriNet().setName(netName);
				SwatComponents.getInstance().getContainerPetriNets().addComponent(newPTNet, true);
				break;
			case NEW_IF:
				GraphicalIFNet newIFNet = new GraphicalIFNet(new IFNet());
				newIFNet.getPetriNet().setName(netName);
				SwatComponents.getInstance().getContainerPetriNets().addComponent(newIFNet, true);
				break;

			default:
				break;
			}
		}
	}

	private String requestNetName(String message, String title) throws Exception {
		return new PNNameDialog(SwingUtilities.getWindowAncestor(Workbench.getInstance()), message, title, false).requestInput();

	}
}
