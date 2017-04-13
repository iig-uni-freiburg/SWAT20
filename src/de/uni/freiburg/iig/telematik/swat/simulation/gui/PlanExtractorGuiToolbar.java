package de.uni.freiburg.iig.telematik.swat.simulation.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JToolBar;

import de.uni.freiburg.iig.telematik.swat.jascha.OptimizationResult;
import de.uni.freiburg.iig.telematik.swat.simulation.SimulationResultsWriter;
import de.uni.freiburg.iig.telematik.swat.simulation.WorkflowExecutionPlan;

public class PlanExtractorGuiToolbar extends JToolBar {
	
	private PlanExtractorResult jliplanExtractorResult;

	public PlanExtractorGuiToolbar(PlanExtractorResult planExtractorResult) {
		this.jliplanExtractorResult=planExtractorResult;
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		JButton save = new JButton("save");
		save.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				OptimizationResult plan = planExtractorResult.getSelectedValue();
				File path = new File("/home/richard/Dokumente/diss/Ausarbeitung mit Vorlage/img/gantt/tmp/");
				try {
					SimulationResultsWriter.writeFireSequence(path,plan.getSeq());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}
		});
		add(save);
		
	}

}
