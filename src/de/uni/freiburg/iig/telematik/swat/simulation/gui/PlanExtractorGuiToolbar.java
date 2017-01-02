package de.uni.freiburg.iig.telematik.swat.simulation.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JToolBar;

import de.uni.freiburg.iig.telematik.swat.simulation.FireSequenceFileWriter;
import de.uni.freiburg.iig.telematik.swat.simulation.SimulationResultsWriter;
import de.uni.freiburg.iig.telematik.swat.simulation.WorkflowExecutionPlan;

public class PlanExtractorGuiToolbar extends JToolBar {
	
	private PlanExtractorResult jliplanExtractorResult;

	public PlanExtractorGuiToolbar(PlanExtractorResult planExtractorResult) {
		this.jliplanExtractorResult=planExtractorResult;
		JButton save = new JButton("save");
		save.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				WorkflowExecutionPlan plan = planExtractorResult.getSelectedValue();
				try {
					FireSequenceFileWriter.writeToDisk(plan.getSeq());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}
		});
		add(save);
		
	}

}
