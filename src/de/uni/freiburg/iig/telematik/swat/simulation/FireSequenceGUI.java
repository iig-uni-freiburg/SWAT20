package de.uni.freiburg.iig.telematik.swat.simulation;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.AbstractTimedTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.StatisticListener;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ExecutionState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.FireSequence;


public class FireSequenceGUI extends JFrame {
	
	private static final String newline="\r\n";
	
	Dimension d = new Dimension(600, 800);
	private HashMap<String, ArrayList<FireSequence>> fireSequences;
	
	public FireSequenceGUI() {
		this.fireSequences = StatisticListener.getInstance().getFireSequences();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(d);
		setPreferredSize(d);
		JScrollPane pane = new JScrollPane(getSequenceTextField());
		getContentPane().add(pane);
	}
	
	/**
	 * Use only the nets provide inside netsToConsider to generate results
	 * @param netsToConsider nets that should be used
	 */
	public FireSequenceGUI(Collection<String> netsToConsider){
		this.fireSequences = StatisticListener.getInstance().getFireSequences();
		HashMap<String,ArrayList<FireSequence>> filteredSequence = new HashMap<>();
		for(String net: netsToConsider) {
			filteredSequence.put(net, fireSequences.get(net));
		}
		fireSequences=filteredSequence;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(d);
		setPreferredSize(d);
		JScrollPane pane = new JScrollPane(getSequenceTextField());
		getContentPane().add(pane);
	}
	
	public FireSequenceGUI(StatisticListener statListener) {
		Map<String, List<Entry<Double, ExecutionState>>> workingTimes = statListener.getWorkingTimes();
	}
	
	public FireSequenceGUI(LinkedList<AbstractTimedTransition> sequence) {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(d);
		setPreferredSize(d);
		JScrollPane pane = new JScrollPane(getSequenceTextField());
		getContentPane().add(pane);
	}
	
	private JTextArea getSequenceTextField(){
		JTextArea field =new JTextArea();
		field.setEditable(false);
		field.setText(getSequenceString());
		return field;
	}
	
	private String getSequenceString(){
		StringBuilder b = new StringBuilder();
		
		for (Entry<String, ArrayList<FireSequence>> values:fireSequences.entrySet()){
			b.append(values.getKey());
			b.append(newline);
			b.append(getShortestSequences(values.getKey(), 3));
			b.append(newline);
			b.append(newline);
		}
		return b.toString();
	}
	
	private String getShortestSequences(String netName, int number){
		LinkedList<FireSequence> list = new LinkedList<>();
		list.addAll(fireSequences.get(netName));
		Collections.sort(list);
		LinkedList<FireSequence> result = new LinkedList<>();
		int i = 0;
		for(FireSequence sequence:list){
			result.add(sequence);
			i++;
			if(i>=number) break;
		}
		
		StringBuilder b = new StringBuilder(result.size()*30);
		
		for (FireSequence fs: result){
			b.append(fs.toString()+newline);
		}
		
		return b.toString();
		
	}

}
