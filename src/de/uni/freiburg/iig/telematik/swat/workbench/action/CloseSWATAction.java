package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import de.uni.freiburg.iig.telematik.swat.workbench.SwatTabView;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;

public class CloseSWATAction extends AbstractAction {

	private static final long serialVersionUID = 7231652730616663333L;

	@Override
	public void actionPerformed(ActionEvent e) {

            try {
				int counter = 0;
	            try {
					for (int i = 0; i < SwatTabView.getInstance().getComponentCount() - 1; i++) {
						if (SwatTabView.getInstance().hasUnsavedChange(i)) counter++;
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				System.out.println(counter);
	            if (counter == 0) System.exit(0);
 				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to Save your Changes?", "Warning", dialogButton);
				if(dialogResult == JOptionPane.YES_OPTION) {
					new SaveAllAction().actionPerformed(new ActionEvent(this, 0, "save"));
				}
				System.exit(0);

            } catch (Exception ex) {
                Workbench.errorMessage("Error while saving Petri Nets...", ex, true);
            }
	}
}
