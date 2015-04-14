package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.lola.LolaPresenter;
import de.uni.freiburg.iig.telematik.swat.prism.generator.IFNetToPrismConverter;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTabView;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.ViewComponent;

class PrismAnalyzeAction extends AbstractAction {

	private SwatTabView tabView;

	public PrismAnalyzeAction(SwatTabView tabview) {
		this.tabView = tabview;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {


	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		//Test if current open file is a IFnet
		if (isPTNet()) {
			//convert
			try{
			IFNetToPrismConverter converter = new IFNetToPrismConverter(getNet());
			StringBuilder prismString = converter.ConvertIFNetToPrism();

			//present
			LolaPresenter presenter = new LolaPresenter(prismString.toString());
				presenter.show();
			} catch (ClassCastException e) {
				JOptionPane.showMessageDialog(tabView, "This is not an IFNet");
			}
		}

	}

	private boolean isPTNet() {
		ViewComponent node = (ViewComponent) tabView.getSelectedComponent();
		if (node.getMainComponent() instanceof PNEditorComponent)
			return true;
		return false;
	}

	private IFNet getNet() {
		return (IFNet) ((PNEditorComponent) tabView.getSelectedComponent()).getNetContainer().getPetriNet();
	}

}
