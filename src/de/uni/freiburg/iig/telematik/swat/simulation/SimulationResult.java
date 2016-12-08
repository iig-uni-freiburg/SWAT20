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
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
import com.sun.nio.file.SensitivityWatchEventModifier;

import alice.util.jedit.InputHandler.document_end;
import de.invation.code.toval.misc.NamedComponent;
import de.invation.code.toval.misc.wd.ProjectComponentException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalTimedNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.abstr.StatisticListener;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.WorkflowTimeMachine;
import de.uni.freiburg.iig.telematik.swat.misc.PrintToPdf;
import de.uni.freiburg.iig.telematik.swat.simon.AwesomeTimeContext;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;
import jdk.nashorn.internal.ir.ReturnNode;


public class SimulationResult extends JFrame {
	
	private static final long serialVersionUID = -4759318829955102188L;
	WorkflowTimeMachine wtm;
	private AwesomeTimeContext tc;
	private static final DecimalFormat format = new DecimalFormat("#.##");
	private JPanel content;
	private ArchitectureResults ar;
	
	
	public SimulationResult(WorkflowTimeMachine wtm, AwesomeTimeContext tc, boolean storeResults) {
		this.wtm = wtm;
		this.tc = tc;
		this.ar = new ArchitectureResults(wtm,tc);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(800, 600);
		setPreferredSize(new Dimension(600, 500));
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setTitle("Simulation Result");
		//add(getButtons());
		content = new JPanel();
		content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
		content.add(overallResult());
		for (String name: wtm.getResult().keySet()){
			content.add(getResult(name));
			//writeToFile(new File("/home/richard/Dokumente/diss/Ausarbeitung mit Vorlage/img/simulation"+name+".txt"), name);
		}
		JScrollPane scrollPane = new JScrollPane(content);
		getContentPane().add(scrollPane);
		
		if(storeResults)
			writeResults();
		
		new FireSequenceGUI(wtm.getResult().keySet()).setVisible(true);
		
	}
	
	private JPanel overallResult(){
		JPanel results = new JPanel();
		results.setLayout(new BoxLayout(results, BoxLayout.LINE_AXIS));
		results.add(new JLabel("Overall Performance: "));
		results.add(new JLabel(format.format(100*ar.getArchitecturePerformance())));
		results.add(new JLabel(" Overall Costs: "));
		results.add(new JLabel(format.format(ar.totalGeneratedCosts())));
		return results;
	}
	
	public void setVisible(boolean visible){
		super.setVisible(visible);
		//PrintToPdf.paintToPDF(content, new File("/tmp/test123.pdf"));
		
	}
	
	private JPanel getResult(String netName){
		JPanel panel = new JPanel();
		Dimension d = new Dimension(120, 90);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		String deadline = getDeadlineString(netName);
		String cost = getCostString(netName);
		JLabel label = new JLabel("<html> "+getNameDetail(netName)+": <br> "+getSuccessString(netName)+" <br> Deadline: <br>"+deadline+"<br> Cost: "+cost+" </html> ");
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
	
	private String getCostString(String netName) {
		double cost = ar.getCost(netName);
		return format.format(cost);
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
	
	private String getSuccessString(String netName){
		String s = "";
		double successRatio = ar.getSuccessRatio(netName)*100;
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
	
	private void writeToFile(File file, String netName){
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				ArrayList<Double> results = wtm.getResult().get(netName);
				try {
					FileWriter fw = new FileWriter(file);
					for(double d: results){
						fw.write(Double.toString(d));
						fw.write(System.getProperty("line.separator"));
					}
					fw.flush();
					fw.close();
				} catch (IOException e) {
					Workbench.errorMessage("Could not write result file "+file.getAbsolutePath(), e, false);
				}
				
			}
		});
		thread.run();

	}
	
	private void writeResults(){
		boolean show=false;
		try {
			Workbench workbench = Workbench.getInstance();
			if (JOptionPane.showConfirmDialog(null, "write Files?")==JOptionPane.YES_OPTION) 
				show =true;
			
		} catch (Exception e1) {
		}
		
		if(!show) return;
		
		Thread timeResultThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					SimulationResultsWriter.writeIntoIndividualFiles(wtm, new File("/home/richard/Dokumente/diss/Ausarbeitung mit Vorlage/img/simulation/"));
				} catch (IOException e) {
					Workbench.errorMessage("Could not write result file "+SimulationResultsWriter.getFile().getAbsolutePath(), e, false);
				}
				
			}
		});
		
		Thread costResultThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				writeCostsResults();
				
			}
		});
		costResultThread.start();
		timeResultThread.start();
	}
	
	private void writeCostsResults() {
		HashMap<String, LinkedList<Double>> costs = generateCostMap();
		for(String netName:costs.keySet()){
			File path = new File("/home/richard/Dokumente/diss/Ausarbeitung mit Vorlage/img/costs/");
			File file = getFileNameFor(path, netName);
			try {
				FileWriter fw = new FileWriter(file);
				for(Double cost:costs.get(netName)){
					fw.write(Double.toString(cost));
					fw.write(System.getProperty("line.separator"));
				}
				fw.flush();
				fw.close();
			} catch (IOException e) {
				// Do nothing
			}
		}
	}
	
	private HashMap<String, LinkedList<Double>> generateCostMap() {
		HashMap<String, LinkedList<Double>> costResult = new HashMap<>();
		for(String netName:wtm.getResult().keySet()){
			LinkedList<Double> resultList = new LinkedList<>();
			double deadline = ar.getDeadlineFor(netName);
			TimedNet net = wtm.getNets().get(netName);
			for(double result: wtm.getResult().get(netName)){
				double cost=0;
				if(result>deadline){ //net has not met deadline
					cost += deadline * net.getCostPerTimeUnit(); //cost to deadline
					cost += (result-deadline)*net.getCostPerTimeUnitAfterDeadline(); //costs after deadline
				} else { //net is within deadline
					cost += result*net.getCostPerTimeUnit();
				}
				resultList.add(cost);
			}
			costResult.put(netName, resultList);
		}
		return costResult;
	}

	private File getFileNameFor(File path, String currentNet){
		StringBuilder b = new StringBuilder();
		b.append(path.getAbsolutePath()+"/");
		for(String netNames:wtm.getResult().keySet()) //get names of remaining open nets
			b.append(netNames+"-");
		b.append("("+currentNet.toUpperCase()+").csv"); //point out simulated net
		System.out.println("FilePath: "+b.toString());
		return new File(b.toString());
	}
	


}
