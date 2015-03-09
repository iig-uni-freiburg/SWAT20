package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import de.invation.code.toval.graphic.dialog.FileNameDialog;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.logs.LogModel;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTabView;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeNode;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.exception.SwatComponentException;

public class RenameAction extends AbstractWorkbenchAction {

	public RenameAction() {
		super("Rename");
		setTooltip("rename current PT-Net");
		setAcceleratorKey(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
		try {
			setIcon(IconFactory.getIcon("rename"));
		} catch (ParameterException e) {
		} catch (PropertyException e) {
		} catch (IOException e) {
		}
	}

	public RenameAction(String name, Icon icon) {
		super(name);
		setAcceleratorKey(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
		if (icon != null)
			setIcon(icon);
	}

	public RenameAction(String string) {
		super(string);
		setAcceleratorKey(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
		setTooltip("Rename");
		setAcceleratorKey(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
		try {
			setIcon(IconFactory.getIcon("rename"));
		} catch (ParameterException e) {
		} catch (PropertyException e) {
		} catch (IOException e) {
		}
	}

	private static final long serialVersionUID = 365967570541436082L;

	public void actionPerformed(ActionEvent e) {
		SwatTreeNode node = (SwatTreeNode) Workbench.getInstance().getTreeView().getSelectionPath().getLastPathComponent();
		String oldName, newName;
		Boolean open = SwatTabView.getInstance().containsComponent(node);
		
		switch(node.getObjectType()){
		case LABELING:
			break;
		case PETRI_NET:
			oldName = node.getDisplayName();
			newName = requestNetName("Enter new name for Petri net", "Rename Petri net");
			if (newName != null && !newName.equals(oldName)) {
				if (SwatTabView.getInstance().closeTabAndAskUser(oldName))
					try {
						SwatComponents.getInstance().renamePetriNet(oldName, newName);
					} catch (SwatComponentException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			}
			break;
		case PETRI_NET_ANALYSIS:
			break;
		case MXML_LOG:
		case XES_LOG:
		case ARISTAFLOW_LOG:
			oldName = node.getDisplayName();
			newName = requestNetName("Enter new name for Log", "Rename Log");
			LogModel model = SwatComponents.getInstance().getLogModel(oldName);
			try {
				SwatComponents.getInstance().renameLog(oldName, newName);
			} catch (SwatComponentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		default:
			break;
		}
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {

	}

	private String requestNetName(String message, String title) {
		return  FileNameDialog.showDialog(SwingUtilities.getWindowAncestor(Workbench.getInstance()), message, title, false);
	}
}
