package de.uni.freiburg.iig.telematik.swat.workbench.action;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.InconsistencyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.event.TokenEvent;
import de.uni.freiburg.iig.telematik.sepia.event.TokenListener;
import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.TimeMachine;
import de.uni.freiburg.iig.telematik.sepia.traversal.PNTraverser;
import de.uni.freiburg.iig.telematik.sepia.traversal.RandomPNTraverser;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

public class SimulateTimeAction extends AbstractWorkbenchAction {

	private static final long serialVersionUID = 1729386246000057281L;

	double[] results;
	private int numberOfRuns = 50000;
	private int numberOfBins = 100;

	private boolean drainPlaceReached = false;

	public SimulateTimeAction(int numberOfRuns) {
		super("");
		this.numberOfRuns = numberOfRuns;
		setTooltip("Simulate Timing");
		try {
			setIcon(IconFactory.getIcon("time"));
		} catch (ParameterException e) {
			setText("simulate Timing");
			e.printStackTrace();
		} catch (PropertyException e) {
			setText("simulate Timing");
			e.printStackTrace();
		} catch (IOException e) {
			setText("simulate Timing");
			e.printStackTrace();
		}
	}

	public SimulateTimeAction() {
		this(100000);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//		System.out.println("Drain-Place: " + getNet().getPetriNet().getDrainPlaces().toString());
		//		System.out.println("Source-Place: " + getNet().getPetriNet().getSourcePlaces().toString());
		results = new double[numberOfRuns];
		setupDrainPlaceListener(getNet());
		for (int i = 0; i < results.length; i++) {
			results[i] = getOneSimulationRun();
		}

		//		System.out.println("Needed times:");
		//		for (long l : results)
		//			System.out.print(" " + l);
		numberOfBins = (int) (Math.max(Math.sqrt(results.length) / 3, 5) + 1);
		generateDiagram(results, numberOfBins);
		System.out.println("Average time: " + getAverage(results));
	}

	public double[] getSimulationResults() {
		return results;
	}

	private AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> getNet() {
		return ((PNEditorComponent) Workbench.getInstance().getTabView().getSelectedComponent()).getNetContainer();
	}

	private TimeMachine<?, ?, ?, ?, ?> getTimeMachine(AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> net) {
		return new TimeMachine(net.getPetriNet(), SwatComponents.getInstance().getTimeContexts(net.getPetriNet().getName()).get(0));
	}

	private PNTraverser<AbstractTransition<?, Object>> getTraverser(AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> net) {
		return new RandomPNTraverser((AbstractPetriNet<?, ?, ?, ?, ?>) net.getPetriNet());
	}

	private AbstractTransition<?, Object> chooseTransition(PNTraverser<AbstractTransition<?, Object>> traverser,
			AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> net) throws InconsistencyException {
		return traverser.chooseNextTransition((List<AbstractTransition<?, Object>>) net.getPetriNet().getEnabledTransitions());
	}

	private double getOneSimulationRun() {
		double time = 0;
		AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> net = getNet();
		TimeMachine<?, ?, ?, ?, ?> timeMachine = getTimeMachine(net);
		PNTraverser<AbstractTransition<?, Object>> traverser = getTraverser(net);

		//do at least the first Run
		//do while enabled transitions available and timeMachine increases time
		//		boolean firstRun = true;
		//		while (firstRun || (timeMachine.incTime() && net.getPetriNet().hasEnabledTransitions())) {
		//			firstRun = false;
		//			AbstractTransition<?, Object> transition = chooseTransition(traverser, net);
		//			try {
		//				timeMachine.fire(transition.getName());
		//				time = timeMachine.getTime();
		//			} catch (PNException e1) {
		//				e1.printStackTrace();
		//			}
		//		}
		//		timeMachine.reset();
		//		return time;

		//		while (net.getPetriNet().hasEnabledTransitions() || fireAllEnabledTransitions(net, timeMachine, traverser)
		//				|| timeMachine.hasPendingActions()) {
		//				timeMachine.incTime();
		//		}

		//fireAllEnabledTransitions(net, timeMachine, traverser);
		while (fireAllEnabledTransitions(net, timeMachine, traverser) || timeMachine.hasPendingActions()
				|| net.getPetriNet().hasEnabledTransitions()) {
			if (drainPlaceReached) {
				System.out.println("Drain-Place reached");
				break;
			}
			timeMachine.incTime();
		}
		time = timeMachine.getTime();
		timeMachine.reset();
		drainPlaceReached = false;

		//		while (net.getPetriNet().hasEnabledTransitions() || timeMachine.incTime()) {
		//			while (fireAllEnabledTransitions(net, timeMachine, traverser))
		//				;
		//			timeMachine.incTime();
		//			//			if (!timeMachine.incTime())
		//			//				break;
		//		}

		//		time = timeMachine.getTime();
		//		timeMachine.reset();
		return time;
	}

	private boolean fireAllEnabledTransitions(AbstractGraphicalPN net, TimeMachine timeMachine,
			PNTraverser<AbstractTransition<?, Object>> traverser) {
		AbstractTransition<?, Object> transition = chooseTransition(traverser, net);
		boolean didFire = false;
		while (transition != null) {
			try {
				timeMachine.fire(transition.getName());
				//System.out.println(this.getClass().getSimpleName() + ": fired " + transition.getName());
				didFire = true;
			} catch (PNException e) {
			}
			transition = chooseTransition(traverser, net);
		}
		return didFire;
	}

	private <	P extends AbstractPlace<F, S>, 
				T extends AbstractTransition<F, S>, 
				F extends AbstractFlowRelation<P, T, S>, 
				M extends AbstractMarking<S>, 
				S extends Object,
				N extends AbstractPetriNet<P,T,F,M,S>,
				G extends AbstractPNGraphics<P,T,F,M,S>> void setupDrainPlaceListener(AbstractGraphicalPN<P,T,F,M,S,N,G> net) {
		for (P p : net.getPetriNet().getDrainPlaces()) {
			p.addTokenListener(new TokenListener<AbstractPlace<F,S>>() {

				@Override
				public void tokensAdded(TokenEvent<? extends AbstractPlace<F,S>> o) {
					//System.out.println("Reached " + o.getSource().getLabel());
					drainPlaceReached = true;
				}

				@Override
				public void tokensRemoved(TokenEvent<? extends AbstractPlace<F,S>> o) {
				}
			});

		}
	}

	private static void generateDiagram(double[] results2, int bins) {
		double[] buffer = new double[results2.length];
		for (int i = 0; i < results2.length; i++)
			buffer[i] = results2[i];
		// The histogram takes an array
		HistogramDataset histo = new HistogramDataset();
		histo.addSeries("Relative Occurence of Duration", buffer, bins);
		histo.setType(HistogramType.RELATIVE_FREQUENCY);
		//histo.setType(HistogramType.SCALE_AREA_TO_1);

		JFrame aFrame = new JFrame("Time analysis");
		//ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
		JFreeChart chart = ChartFactory.createHistogram("Distribution of simulated workflow duration",
				"Duration of Workflow execution in ms", "Relative occurence", histo, PlotOrientation.VERTICAL, true, true, false);

		// to save as JPG
		XYPlot plot = (XYPlot) chart.getPlot();
		XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();

		renderer.setDrawBarOutline(false);

		renderer.setSeriesOutlinePaint(0, Color.red);

		//plot.getRangeAxis().setRange(0, 0.2);
		//plot.getDomainAxis().setRange(0, 30);
		plot.getRangeAxis().setTickLabelFont(new Font("Arial", 0, 30));
		plot.getDomainAxis().setTickLabelFont(new Font("Arial", 0, 30));
		plot.getRangeAxis().setLabelFont(new Font("Arial", 1, 28));
		plot.getDomainAxis().setLabelFont(new Font("Arial", 1, 28));
		//plot.getLegendItems().get(0).set(new Font("Arial", 1, 26));
		//plot.getLegendItems().get(1).setLabelFont(new Font("Arial", 1, 24));
		LegendTitle legend = chart.getLegend();
		Font nwfont = new Font("Arial", 0, 26);
		legend.setItemFont(nwfont);
		//chart.setLegend(legend);

		ChartPanel panel = new ChartPanel(chart);
		panel.setPreferredSize(new java.awt.Dimension(900, 600));
		aFrame.setContentPane(panel);
		aFrame.setPreferredSize(new java.awt.Dimension(900, 600));
		aFrame.setSize(new Dimension(900, 600));
		aFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		aFrame.setVisible(true);
	}

	private double getAverage(double[] values) {
		double sum = 0;
		for (double l : values)
			sum += l;
		return sum / (double) values.length;
	}

	@Override
	protected void doFancyStuff(ActionEvent e) throws Exception {
		// TODO Auto-generated method stub

	}

}
