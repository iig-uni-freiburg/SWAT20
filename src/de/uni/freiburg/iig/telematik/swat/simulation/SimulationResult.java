package de.uni.freiburg.iig.telematik.swat.simulation;

import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map.Entry;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import alice.util.jedit.InputHandler.document_end;
import de.invation.code.toval.misc.NamedComponent;
import de.invation.code.toval.misc.wd.ProjectComponentException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalTimedNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.StatisticListener;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.WorkflowTimeMachine;
import de.uni.freiburg.iig.telematik.swat.simon.AwesomeTimeContext;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;
import jdk.nashorn.internal.ir.ReturnNode;


public class SimulationResult extends JFrame {
	
	private static final long serialVersionUID = -4759318829955102188L;
	WorkflowTimeMachine wtm;
	private AwesomeTimeContext tc;
	private static final DecimalFormat format = new DecimalFormat("#.##");
	
	public SimulationResult(WorkflowTimeMachine wtm, AwesomeTimeContext tc) {
		this.wtm = wtm;
		this.tc = tc;
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(800, 600);
		setPreferredSize(new Dimension(600, 500));
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		//add(getButtons());
		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
		for (String name: wtm.getResult().keySet()){
			content.add(getResult(name));
		}
		JScrollPane scrollPane = new JScrollPane(content);
		getContentPane().add(scrollPane);
		new FireSequenceGUI(wtm.getResult().keySet()).setVisible(true);
	}
	
	private JPanel getResult(String netName){
		JPanel panel = new JPanel();
		Dimension d = new Dimension(120, 70);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		String deadline = getDeadlineString(netName);
		JLabel label = new JLabel("<html> "+getNameDetail(netName)+": <br> "+getSuccessString(netName)+" <br> Deadline: <br>"+deadline+" </html> ");
		panel.add(label);
		label.setSize(d);
		label.setPreferredSize(d);
		label.setMaximumSize(d);
		panel.add(Box.createHorizontalStrut(5));
		panel.add(getHistogramm(netName));
		panel.add(Box.createHorizontalStrut(2));
		panel.add(getCummulativeChart(netName));
		return panel;
	}
	
	private String getDeadlineString(String netName) {
		try{
			return format.format(tc.getDeadlineFor(netName));
		} catch (Exception e){
			return "";
		}
	}
	
	private String getNameDetail(String netName) {
		try { 
			NamedComponent net = SwatComponents.getInstance().getContainerPetriNets().getComponent(netName);
			if(net instanceof GraphicalTimedNet){
				if (((GraphicalTimedNet)net).getPetriNet().isRecurring())
					return netName+"(r)"; //if net is recurring in the process
			}
		} catch (ProjectComponentException e) {
			return netName;
		}
		return netName;
	}

	private double getDeadlineFor(String netName){
		double deadline = Double.NaN;
		if(tc.containsDeadlineFor(netName))
			deadline = tc.getDeadlineFor(netName);
		return deadline;
	}
	
	private double getSuccessRatio(String netName){
		double deadline = getDeadlineFor(netName);
		ArrayList<Double> result = wtm.getResult().get(netName);
		int size = result.size();
		int success = 0;
		for (double d:result)
			if (d<=deadline)
				success++;
		return (double)success/size;
	}
	
	private String getSuccessString(String netName){
		String s = "";
		double successRatio = getSuccessRatio(netName)*100;
		if(successRatio!=Double.NaN){
			s = format.format(successRatio);
		}
		return s+"%";
	}
	
	private ChartPanel getHistogramm(String netName){
		SimulationHistogram histo = new SimulationHistogram(wtm.getResult().get(netName), 100, "title", "legend");
		ChartPanel panel = histo.getChart();
		panel.setPreferredSize(new Dimension(150, 75));
		return panel;
	}
	
	private ChartPanel getCummulativeChart(String netName){
		CumulativeHistrogram histo = new CumulativeHistrogram(wtm.getResult().get(netName), 100, "title", "legend");
		ChartPanel panel = histo.getChart();
		panel.setPreferredSize(new Dimension(150, 75));
		return panel;
	}
	
	private void toPDF(File file) throws FileNotFoundException, DocumentException{
		int width= 300;
		int height = 600;
		Document doc = new Document();
		PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(file));
		doc.open();
		PdfContentByte cb = writer.getDirectContent();
		PdfTemplate pdfTP = cb.createTemplate(PageSize.A4.getWidth(),PageSize.A4.getHeight());
		cb.addTemplate(pdfTP, 0,0);
		Graphics2D g2d = pdfTP.createGraphics(PageSize.A4.getWidth(),PageSize.A4.getHeight());
		print(g2d);
		g2d.dispose();
		doc.close();
	}
	
	private JPanel getButtons(){
		JPanel panel = new JPanel();
		JButton screenshot = new JButton("export");
		screenshot.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				chooser.setFileSelectionMode(JFileChooser.SAVE_DIALOG);
				int result = chooser.showSaveDialog(null);
				if(result == JFileChooser.APPROVE_OPTION){
					File file = chooser.getSelectedFile();
					try {
						toPDF(file);
					} catch (FileNotFoundException | DocumentException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
				
			}
		});
		panel.add(screenshot);
		return panel;
	}

}
