package de.uni.freiburg.iig.telematik.swat.editor.graph;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.SwingUtilities;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.TimeContext;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.gui.TransitionView;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;

public class IFNetGraphComponent extends PNGraphComponent {

	private static final long serialVersionUID = -1698182711658593407L;

	public IFNetGraphComponent(IFNetGraph IFNetGraph) {
		super(IFNetGraph);
	}

	@Override
	public IFNetGraph getGraph() {
		return (IFNetGraph) super.getGraph();
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


	@Override
	protected boolean doubleClickOnArc(PNGraphCell cell, MouseEvent e) {
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
	
	@Override
	protected boolean doubleClickOnTransition(PNGraphCell cell, MouseEvent e) {
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
	
	@Override
	protected boolean mouseWheelOnPlace(PNGraphCell cell, MouseWheelEvent e) {
		// TODO decrementing or incrementing tokennumber of selected color with
		// mousewheel
		return false;
	}
	
	@Override
	protected boolean rightClickOnTransition(PNGraphCell cell, MouseEvent e) {
		Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), this);
		getTransitionPopupMenu().show(IFNetGraphComponent.this, pt.x, pt.y);
		return false;
	}

	@Override
	protected boolean rightClickOnArc(PNGraphCell cell, MouseEvent e) {
		// Show TimeContext, set Filepath if none available
		//return super.rightClickOnArc(cell, e);
		try {
			SwatComponents comp = SwatComponents.getInstance();
			AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?, ?, ?> cur_net = ((PNEditor) Workbench.getInstance().getTabView()
					.getSelectedComponent()).getNetContainer();

			System.out.println("FIXME: " + this.getClass().getName() + ":" + Thread.currentThread().getStackTrace()[2].getLineNumber());
			TimeContext context = comp.getTimeContexts(cur_net.getPetriNet().getName()).get(0);

//			if (context == null){
//				File contextPath = new File(comp.getFile(cur_net).getParentFile(), SwatProperties.getInstance().getTimeAnalysisFolderName());
//				File fileToSave=new File(contextPath,"time-context.xml");
//				fileToSave.getParentFile().mkdirs();
//				context = new TimeContext(fileToSave);
//			}
			TransitionView view = new TransitionView(cell.getId(), context);
			view.setVisible(true);
				
		} catch (ClassCastException e1) {
			e1.printStackTrace();
//		} catch (IOException e2) {
//			e2.printStackTrace();
		}
		return false;
	}

}
