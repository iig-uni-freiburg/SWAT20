package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import de.invation.code.toval.misc.wd.ProjectComponentException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResourceContext;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class SetDefaultResourceContextAction extends AbstractWorkbenchAction {

	public SetDefaultResourceContextAction(String name) {
		super(name);
	}
	
	public SetDefaultResourceContextAction(){
		super("set default Res-Context");
	}

	private static final long serialVersionUID = 8026496158703419449L;

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		JButton source = (JButton) e.getSource();
		JPopupMenu menu = getPopup(source);
		menu.setVisible(true);
	}
	
	private JPopupMenu getPopup(JButton source){
		JPopupMenu menu = new JPopupMenu();
		
		try {
			for(IResourceContext context: SwatComponents.getInstance().getResourceContainer().getComponents()){
				menu.add(new JMenuItem(new setSpecificContext(context.getName())));
			}
		} catch (ProjectComponentException e) {
			Workbench.errorMessage("Could not load Resource context", e, false);
		}
		
		menu.setInvoker((Component) source);
		menu.setLocation((int) source.getLocationOnScreen().getX(), (int) source.getLocationOnScreen().getY());
		return menu;
	}
	
	class setSpecificContext extends AbstractAction {
		
		private String name;
		
		public setSpecificContext(String contextName) {
			super(contextName);
			this.name=contextName;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				SwatProperties.getInstance().setActiveResouceContext(name);
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
					((TimedNet)net.getPetriNet()).setResourceContext(SwatComponents.getInstance().getResourceContainer().getComponent(name));
				}
			}
		}
		
	}
	
	

}
