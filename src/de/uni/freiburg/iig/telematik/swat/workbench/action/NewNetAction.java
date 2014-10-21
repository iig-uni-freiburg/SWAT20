package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.event.ActionEvent;

import javax.swing.SwingUtilities;

import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalCPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalIFNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.swat.workbench.PNNameDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatNewNetToolbar;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatNewNetToolbar.ToolbarNewNetButtonType;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.WorkbenchPopupToolBar;

public class NewNetAction extends AbstractWorkbenchAction {

	private static final long serialVersionUID = 5661121560829645417L;
	
	private ToolbarNewNetButtonType type;
	private WorkbenchPopupToolBar popupFontToolBar;

	public NewNetAction(SwatNewNetToolbar.ToolbarNewNetButtonType type, WorkbenchPopupToolBar popupFontToolBar) {
		this.type = type;
		this.popupFontToolBar = popupFontToolBar;
	}

	@Override
	public void doFancyStuff(ActionEvent e) throws Exception{
		String netName = requestFileName("Please choose a name for the new net:", "New Petri-Net");
		popupFontToolBar.disposeAllWindows();
		if (netName != null) {
			// Test new file name
			switch (type) {
			case NEW_CPN:
				GraphicalCPN newCPN = new GraphicalCPN();
				newCPN.getPetriNet().setName(netName);
				SwatComponents.getInstance().addPetriNet(newCPN);
				break;
			case NEW_PT:
				GraphicalPTNet newPTNet = new GraphicalPTNet();
				newPTNet.getPetriNet().setName(netName);
				SwatComponents.getInstance().addPetriNet(newPTNet);
				break;
			case NEW_IF:
				GraphicalIFNet newIFNet = new GraphicalIFNet();
				newIFNet.getPetriNet().setName(netName);
				SwatComponents.getInstance().addPetriNet(newIFNet);
				break;

			default:
				break;
			}
		}
	}

	private String requestFileName(String message, String title) {
		return new PNNameDialog(SwingUtilities.getWindowAncestor(Workbench.getInstance()), message, title, false).requestInput();

	}
}
