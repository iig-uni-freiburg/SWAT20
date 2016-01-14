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
import de.uni.freiburg.iig.telematik.swat.jascha.AwesomeResourceContext;
import de.uni.freiburg.iig.telematik.swat.jascha.ResourceStore;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class SetDefaultResourceStoreAction extends AbstractWorkbenchAction {

	public SetDefaultResourceStoreAction(String name) {
		super(name);
	}
	
	public SetDefaultResourceStoreAction(){
		super("set default Res-Store");
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
			for(ResourceStore store: SwatComponents.getInstance().getResourceStoreContainer().getComponents()){
				menu.add(new JMenuItem(new SetSpecificStore(store.getName())));
			}
		} catch (ProjectComponentException e) {
			Workbench.errorMessage("Could not load Resource store", e, false);
		}
		
		menu.setInvoker((Component) source);
		menu.setLocation((int) source.getLocationOnScreen().getX(), (int) source.getLocationOnScreen().getY());
		return menu;
	}
	
	class SetSpecificStore extends AbstractAction {
		
		private String name;
		
		public SetSpecificStore(String resourceStoreName) {
			super(resourceStoreName);
			this.name=resourceStoreName;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				//SwatProperties.getInstance().setActiveResouceContext(name);
				//SwatProperties.getInstance().store();
				updateNets();
			} 
			 catch (ProjectComponentException e1) {
				Workbench.errorMessage("Could not link Resource Store "+name, e1, true);
			} catch (IOException e1) {
				Workbench.errorMessage("I/O Error", e1, true);
			}
			
		}
		
		private void updateNets() throws ProjectComponentException, IOException{
			for (AbstractGraphicalPN net: SwatComponents.getInstance().getContainerPetriNets().getComponentsSorted()){
				if (net.getPetriNet() instanceof TimedNet){
					TimedNet castedNet = (TimedNet) net.getPetriNet();
					ResourceStore store = SwatComponents.getInstance().getResourceStoreContainer().getComponent(name);
					if(castedNet.getResourceContext()==null){
						IResourceContext defaultContext = SwatComponents.getInstance().getResourceContainer().getComponent(SwatProperties.getInstance().getActiveResourceContext());
						castedNet.setResourceContext(defaultContext);
					}
					((AwesomeResourceContext)((TimedNet)net.getPetriNet()).getResourceContext()).setResourceStore(store);
				}
			}
		}
		
	}
	
	

}
