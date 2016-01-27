package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.Component;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import de.invation.code.toval.misc.wd.ProjectComponentException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResourceContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeContext;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.action.SetDefaultResourceContextAction.SetSpecificContext;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class SetDefaultTimeContextAction extends AbstractWorkbenchAction {
	
	private static final long serialVersionUID = 1L;

	public SetDefaultTimeContextAction(String name) {
		super(name);
	}
	
	public SetDefaultTimeContextAction() {
		super("set def timecontext");
	}



	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		JPopupMenu menu = getPopup(e.getSource());
		menu.setVisible(true);
	}
	
	JPopupMenu getPopup(Object object){
		JPopupMenu menu = new JPopupMenu();
		
		try {
			for(ITimeContext context: SwatComponents.getInstance().getTimeContextContainer().getComponents()){
				menu.add(new JMenuItem(new SetSpecificTimeContext(context.getName())));
			}
		} catch (ProjectComponentException e) {
			Workbench.errorMessage("Could not load time context", e, false);
		}
		
		menu.setInvoker((Component) object);
		menu.setLocation((int) MouseInfo.getPointerInfo().getLocation().getX(), (int) MouseInfo.getPointerInfo().getLocation().getY());
		return menu;

	}
	
	class SetSpecificTimeContext extends AbstractAction {
		
		private String name;
		
		public SetSpecificTimeContext(String contextName) {
			super(contextName);
			this.name=contextName;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				SwatProperties.getInstance().setActiveTimeContext(name);
				SwatProperties.getInstance().store();
				updateNets();
			} catch (IOException e1) {
				Workbench.errorMessage("Could not set Resource Context "+name, e1, true);
			} catch (ProjectComponentException e1) {
				Workbench.errorMessage("Could not link Resource Context "+name, e1, true);
			}
			
		}
		
		private void updateNets() throws ProjectComponentException{
			for (AbstractGraphicalPN net: SwatComponents.getInstance().getContainerPetriNets().getComponentsSorted()){
				if (net.getPetriNet() instanceof TimedNet){
					((TimedNet)net.getPetriNet()).setTimeContext(SwatComponents.getInstance().getTimeContextContainer().getComponent(name));
				}
			}
			SwatComponents.getInstance().getContainerPetriNets().storeComponents();
		}
		
	}

}
