package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import de.invation.code.toval.graphic.dialog.FileNameDialog;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.swat.aristaFlow.AristaFlowToPnmlConverter;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeView;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.exception.SwatComponentException;

public class AFtemplateImportAction extends AbstractWorkbenchAction {

	public AFtemplateImportAction() {
		this("Import AF-template");
	}


	public AFtemplateImportAction(String name) {
		super(name);
		setTooltip(name);
		try {
			setIcon(IconFactory.getIcon("import"));
		} catch (ParameterException e) {
		} catch (PropertyException e) {
		} catch (IOException e) {
		}

	}

	private static final long serialVersionUID = -33523248788649572L;

	private String requestNetName(String message, String title) {
		String name = new FileNameDialog(SwingUtilities.getWindowAncestor(SwatTreeView.getInstance().getParent()), message, title, false)
				.requestInput();
		return name;
		//			if (name.endsWith(".pnml"))
		//				return name;
		//			else
		//				return name + ".pnml";
	}


	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(Workbench.getInstance());
		String errorMessage = "";

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			Workbench.consoleMessage("Opening: " + file.getName());
			AristaFlowToPnmlConverter converter = new AristaFlowToPnmlConverter(file);
			try {
				converter.parse();
				AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?, ?, ?> net = converter.getGraphicalPN();
				net.getPetriNet().setName(requestNetName("Please enter a name for the imported net", "Please enter a new name"));
				SwatComponents.getInstance().addPetriNet(net);
				Workbench.consoleMessage("Imported " + net.getPetriNet().getName());

			} catch (ParserConfigurationException e1) {
				errorMessage = "Could not parse AristaFlow log";
			} catch (SAXException e2) {
				errorMessage = "AristaFlow log has wrong format";
			} catch (SwatComponentException e3) {
				errorMessage = "Could not parse AristaFlow log";
			} catch (IOException e4) {
				errorMessage = "Could not access AristaFlow log";
			} finally {
				if (errorMessage != "") {
					Workbench.errorMessage(errorMessage);
					JOptionPane.showMessageDialog(Workbench.getInstance(), errorMessage);
				}
			}

		}
	}

}
