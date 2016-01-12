package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import de.invation.code.toval.misc.wd.ProjectComponentException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResourceContext;
import de.uni.freiburg.iig.telematik.swat.jascha.AwesomeResourceContext;
import de.uni.freiburg.iig.telematik.swat.jascha.fileHandling.ResourceContainer;
import de.uni.freiburg.iig.telematik.swat.jascha.gui.ResourceContextGUI;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatTabView;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.ViewComponent;

public class SelectResourceContextEditAction extends AbstractWorkbenchAction {
	
	JComboBox comboBox;
	
	public SelectResourceContextEditAction() {
		super("Edit Resource Contexts");
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		
		int result = JOptionPane.showConfirmDialog((Component) e.getSource(), getDialogPanel(), "Select Context...",
				JOptionPane.OK_CANCEL_OPTION);
		
		if (result == JOptionPane.OK_OPTION && comboBox.getSelectedItem()!=null) {
			IResourceContext selectedContext = SwatComponents.getInstance().getResourceContainer().getComponent((String) comboBox.getSelectedItem());
			ResourceContextGUI gui = new ResourceContextGUI(selectedContext);
			try{
			gui.setActivitiesHints(getActivitieHints());
			} catch (Exception e1){
				Workbench.errorMessage("Could not get activities", e1, false);
			}
			gui.setVisible(true);
		}

	}
	
	public JPanel getDialogPanel() {
		JPanel panel = new JPanel();
		if (getHints() != null)
			comboBox = new JComboBox<>(getHints());
		else
			comboBox = new JComboBox<>();
		//comboBox.setEditable(true);
		panel.add(comboBox);
		JButton bTnnew = new JButton("new...");
		bTnnew.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String newName=JOptionPane.showInputDialog(panel,"Enter new name for resource context");
				if(newName!=null && !newName.isEmpty()){
					IResourceContext context = new AwesomeResourceContext();
					context.setName(newName);
					try {
						SwatComponents.getInstance().getResourceContainer().addComponent(context,true,true);
						comboBox.insertItemAt(context.getName(), 0);
						comboBox.setSelectedItem(context.getName());
						comboBox.repaint();
					} catch (ProjectComponentException e1) {
						Workbench.errorMessage("Could not insert Resource Context "+newName, e1, true);
					}
				}
				
			}
		});
		panel.add(bTnnew);
		return panel;
	}
	
	/** return names of loaded resource contexts**/
	public String[] getHints(){
		LinkedList<String> list= new LinkedList<>();
		try {
			ResourceContainer resourceContexts = SwatComponents.getInstance().getResourceContainer();
			for(IResourceContext resContext:resourceContexts.getComponents()){
				list.add(resContext.getName());
			}
		} catch (ProjectComponentException e) {
			Workbench.errorMessage("Could not load resource contexts", e, true);
		}
		
		String result[] = new String[list.size()];
		for(int i = 0;i<list.size();i++){
			result[i]=list.get(i);
		}
		return result;
	}
	
	public List<String> getActivitieHints() throws Exception{
		LinkedList<AbstractTransition> hints = new LinkedList<>();
		for(int i = 0;i<SwatTabView.getInstance().getComponentCount();i++){
			if(SwatTabView.getInstance().getComponent(i) instanceof PNEditorComponent){
				PNEditorComponent comp = (PNEditorComponent) SwatTabView.getInstance().getComponent(i);
				hints.addAll(comp.getNetContainer().getPetriNet().getTransitions());
			}
		}
		
		LinkedList<String> result = new LinkedList<>();
		for(AbstractTransition t:hints){
			result.add(t.getLabel());
		}
		
		return result;
	}

}
