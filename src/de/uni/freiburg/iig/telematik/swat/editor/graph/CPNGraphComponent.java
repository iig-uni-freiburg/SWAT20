package de.uni.freiburg.iig.telematik.swat.editor.graph;

import java.awt.Color;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNPlace;
import de.uni.freiburg.iig.telematik.swat.editor.menu.TokenConfigurer;
import de.uni.freiburg.iig.telematik.swat.editor.menu.ToolBarDialog;

public class CPNGraphComponent extends PNGraphComponent {

	private static final long serialVersionUID = -1698182711658593407L;

	public CPNGraphComponent(CPNGraph cpnGraph) {
		super(cpnGraph);
	}

	@Override
	public CPNGraph getGraph() {
		return (CPNGraph) super.getGraph();
	}

	@Override
	protected boolean doubleClickOnPlace(PNGraphCell cell, MouseEvent e) {
		// TODO Table which lists constraints

		try {
			
//			JDialog dialog = 
//			Multiset<String> marking = getGraph().getNetContainer().getPetriNet().getMarking().get(cell.getId());
			getGraph().newTokenConfigurer(cell,this);
//			loadTokenConfigurer(cell, dialog);
		} catch (ParameterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return true;
	}
//
//	public void loadTokenConfigurer(PNGraphCell cell, JDialog dialog) {
//		Map<String, Color> colors = getGraph().getNetContainer().getPetriNetGraphics().getColors();
//		Multiset<String> marking = getGraph().getNetContainer().getPetriNet().getMarking().get(cell.getId());
//		CPNPlace place = getGraph().getNetContainer().getPetriNet().getPlace(cell.getId());
//		if (marking == null)
//			marking = new Multiset<String>();
//		TokenConfigurer tokenConfigurer = new TokenConfigurer(place, getGraph());
//		dialog.add(tokenConfigurer);
//		dialog.pack();
//		dialog.setVisible(true);		
//		tokenConfigurer.setDialog(dialog);
//	}

//	@Override
//	protected boolean doubleClickOnArc(PNGraphCell cell, MouseEvent e) {
//		

////			System.out.println(marking);
//			TokenConfigurer tC = new TokenConfigurer(cell.getId(),getGraph());
//			
//		Window window = SwingUtilities.getWindowAncestor(this);
//			JDialog dialog = new ToolBarDialog(window, "tokens",true);
//	
//			dialog.add(tC);
//
//			dialog.pack();
////			final Toolkit toolkit = Toolkit.getDefaultToolkit();
////			final java.awt.Dimension screenSize = toolkit.getScreenSize();
////			final int x = (screenSize.width - dialog.getWidth()) / 2;
////			final int y = (screenSize.height - dialog.getHeight()) / 2;
////			dialog.setLocation(x, y);
//			dialog.setVisible(true);
//			tC.setDialog(dialog);
//	
//		return false;
//	}

	@Override
	protected boolean doubleClickOnArc(PNGraphCell cell, MouseEvent e) {
		// TODO Table which lists constraints

		try {
			Window window = SwingUtilities.getWindowAncestor(getGraphComponent());
//			JDialog dialog = 
//			Multiset<String> marking = getGraph().getNetContainer().getPetriNet().getMarking().get(cell.getId());
//			getGraph().newTokenConfigurer(cell,window);
//			loadTokenConfigurer(cell, dialog);
		} catch (ParameterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return true;
	}
	@Override
	protected boolean mouseWheelOnPlace(PNGraphCell cell, MouseWheelEvent e) {
		// TODO decrementing or incrementing tokennumber of selected color with
		// mousewheel
		return false;
	}

}
