package de.uni.freiburg.iig.telematik.swat.simon.gui.actions;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeBehaviour;
import de.uni.freiburg.iig.telematik.swat.misc.plots.SimulationHistogram;
import de.uni.freiburg.iig.telematik.swat.simon.MeasuredTimeBehaviour;

public class BehaviorDetailListener implements ListSelectionListener, MouseListener{

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(e.getValueIsAdjusting()) return;
		
		JList<ITimeBehaviour> list = getList(e);
		if(list == null) return;
		if(getMarkedBehaviour(list)==null) return;
		
		MeasuredTimeBehaviour behaviour = getMarkedBehaviour(list);
		new SimulationHistogram(getTiming(behaviour), 50, "Behaviour", "occurence").setVisible(true);
	}
	
	private JList<ITimeBehaviour> getList(EventObject e){
		if (e.getSource() instanceof JList<?>){
			return (JList<ITimeBehaviour>)e.getSource();
		}
		return null;
	}
	
	private MeasuredTimeBehaviour getMarkedBehaviour(JList<ITimeBehaviour> list) {
		ITimeBehaviour behaviour = list.getSelectedValue();
		if (behaviour instanceof MeasuredTimeBehaviour) 
			return (MeasuredTimeBehaviour) behaviour;
		return null;
	}
	
	private List<Double> getTiming(MeasuredTimeBehaviour behaviour){
		HashMap<Long, Double> map = behaviour.getMap();
		ArrayList<Double> result = new ArrayList<>();
		result.addAll(map.values());
		return result;
		//for (long l:map.keySet())
		//	result.add((double) l);
		//return result;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getClickCount()!=2) return;
		
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getClickCount()!=2) return;
		
		JList<ITimeBehaviour> list = getList(e);
		if(list == null) return;
		if(getMarkedBehaviour(list)==null) return;
		
		MeasuredTimeBehaviour behaviour = getMarkedBehaviour(list);
		new SimulationHistogram(getTiming(behaviour), 50, "Behaviour", "occurence").setVisible(true);
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
