package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JToolBar;

import com.mxgraph.swing.handler.mxCellMarker;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.exception.PNException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.CWNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTNet;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;

/**
 * This class represents a Toolbar used to play a counterexample it consists of
 * some buttons to play pause forward or rewind a counterexample It starts a
 * thread to play the CE with a pause of 3sec between each firing
 * 
 * @author bernhard
 * 
 */
public class AnalyzeToolBar extends JToolBar {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2518572389989655690L;

	/**
	 * 
	 */
	private Map<String, mxCellMarker> markerReference = new HashMap<String, mxCellMarker>();
	private PNEditor pnEditor;
	private CounterExampleVisualization counterExample;
	private JButton resetButton, stepButton, backButton, highlightButton,
			playPauseButton;
	private List<JButton> buttonList;
	private boolean highlightedPath = false;
	private Thread thread;
	private boolean threadRunning = false;

	public AnalyzeToolBar(final PNEditor pnEditor) throws ParameterException {
		super(JToolBar.HORIZONTAL);
		Validate.notNull(pnEditor);
		// setLayout(new WrapLayout(FlowLayout.LEFT));
		this.pnEditor = pnEditor;
		buttonList = new ArrayList<JButton>();
		try {
			resetButton = new JButton(IconFactory.getIcon("restart"));
			resetButton.setToolTipText("Move to the beginning");
			resetButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					resetCounterExample();
				}
			});
			stepButton = new JButton(IconFactory.getIcon("end"));
			stepButton.setToolTipText("Step forward");
			stepButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					doStep();
				}
			});
			backButton = new JButton(IconFactory.getIcon("back"));
			backButton.setToolTipText("Step back");
			backButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					doStepBack();
				}
			});
			highlightButton = new JButton(IconFactory.getIcon("solutions"));
			highlightButton.setToolTipText("Toggle Path Highlighting");
			highlightButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					toggleHightLight();
				}
			});
			playPauseButton = new JButton(IconFactory.getIcon("play"));
			playPauseButton.setToolTipText("Play/Pause Counterexample");
			playPauseButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					playPause();
				}
			});
			/*
			 * stopButton = new JButton(IconFactory.getIcon("sleep"));
			 * stopButton.setToolTipText("Pause playing Counterexample");
			 * stopButton.addActionListener(new ActionListener() {
			 * 
			 * @Override public void actionPerformed(ActionEvent arg0) {
			 * stopCounterExample(); } });
			 */
			buttonList.add(resetButton);
			buttonList.add(backButton);
			buttonList.add(playPauseButton);
			buttonList.add(stepButton);
			buttonList.add(highlightButton);
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		deActivate();
		for (JButton button : buttonList) {
			add(button);
		}
	}

	protected void playPause() {
		// TODO Auto-generated method stub
		if (threadRunning) {
			stopCounterExample();
		} else {
			playCounterExample();
		}
	}

	protected void doStepBack() {
		// TODO Auto-generated method stub
		int position = counterExample.getCurrentPosition();
		resetCounterExample();
		for (int i = 0; i < position - 1; i++) {
			doStep();
		}
		if (counterExample.getCurrentPosition() == 0) {
			backButton.setEnabled(false);
		}
	}

	protected void stopCounterExample() {
		// TODO Auto-generated method stub
		thread.interrupt();
		threadRunning = false;
		try {
			playPauseButton.setIcon(IconFactory.getIcon("play"));
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		stepButton.setEnabled(true);
		backButton.setEnabled(true);
		resetButton.setEnabled(true);
	}

	protected void playCounterExample() {
		// TODO Auto-generated method stub

		stepButton.setEnabled(false);
		backButton.setEnabled(false);
		resetButton.setEnabled(false);
		thread = new Thread() {
			@Override
			public void run() {
				while (counterExample.pathEnded() == false) {
					doStep();
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						return;
					}

				}
				resetButton.setEnabled(true);
				backButton.setEnabled(true);
				playPauseButton.setEnabled(false);
			}
		};
		thread.start();
		threadRunning = true;
		try {
			playPauseButton.setIcon(IconFactory.getIcon("sleep"));
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * this method is invoked by the analyePanel it loads the given path and
	 * resets and activates the toolbar
	 * 
	 * @param path
	 *            the counterexample, List of Transitions
	 */
	public void setCounterExample(List<String> path) {
		counterExample = new CounterExampleVisualization(path);
		resetCounterExample();
		activate();
	}

	public void deActivate() {
		for (JButton button : buttonList) {
			button.setEnabled(false);
		}
	}

	public void activate() {
		for (JButton button : buttonList) {
			button.setEnabled(true);
		}
	}

	private void doStep() {
		String transition = counterExample.getNextTransition();
		try {
			pnEditor.getNetContainer().getPetriNet().getTransition(transition)
					.fire();
		} catch (PNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (counterExample.pathEnded()) {
			stepButton.setEnabled(false);
			playPauseButton.setEnabled(false);
		}
		if (threadRunning == false)
			backButton.setEnabled(true);
		pnEditor.updateUI();
	}

	private void resetCounterExample() {
		counterExample.setCurrentPosition(0);
		stepButton.setEnabled(true);
		backButton.setEnabled(false);
		playPauseButton.setEnabled(true);
		try {
			playPauseButton.setIcon(IconFactory.getIcon("play"));
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		threadRunning = false;
		pnEditor.getGraphComponent().getGraph().getNetContainer()
				.getPetriNet().reset();
		pnEditor.updateUI();
	}

	public void toggleHightLight() {
		highlightedPath = !highlightedPath;
		if (highlightedPath) {
			highLightCounterExample();
		} else {
			resetHighlightning();
		}
	}

	public void reset() {
		deActivate();
		if (threadRunning) {
			thread.interrupt();
		}
		threadRunning = false;
		highlightedPath = false;
		resetHighlightning();
		counterExample = null;
	}
	
	public void highLightCounterExample() {
		Map<String, PNGraphCell> nodemap = pnEditor.getGraphComponent()
				.getGraph().nodeReferences;
		Map<String, PNGraphCell> arcmap = pnEditor.getGraphComponent()
				.getGraph().arcReferences;
		
		AbstractPetriNet pn =(AbstractPetriNet) pnEditor.getNetContainer().getPetriNet();
		PlacesArcsAlgorithm algo = null;
		List<String> nodeList=new ArrayList<String>();
		List<String> arcList=new ArrayList<String>();
		
		// determine the type of the ptnet
		// depending on the type the FlowRelation type must be chosen
		if(pn instanceof PTNet) {
			algo=new PlacesArcsAlgorithm<PTFlowRelation>();
		}
		else if(pn instanceof CWN) {
			algo=new PlacesArcsAlgorithm<CWNFlowRelation>();
		} else if(pn instanceof IFNet) {
			algo=new PlacesArcsAlgorithm<IFNetFlowRelation>();
		} else if(pn instanceof CPN) {
			algo=new PlacesArcsAlgorithm<CPNFlowRelation>();
		}
		
		algo.addPlacesArcs(pnEditor, counterExample.getPath(), nodeList,arcList);

		// mark all places or transitions
		for (String place : nodeList) {
			PNGraphCell cell = nodemap.get(place);
			final mxCellMarker marker = getCellMarker(cell);
			marker.highlight(pnEditor.getGraphComponent().getGraph().getView()
					.getState(cell), Color.RED);
		}

		for (String place : arcList) {
			PNGraphCell cell = arcmap.get(place);
			final mxCellMarker marker = getCellMarker(cell);
			marker.highlight(pnEditor.getGraphComponent().getGraph().getView()
					.getState(cell), Color.RED);
		}
	}

	public void resetHighlightning() {
		for (String cell : markerReference.keySet()) {
			markerReference.get(cell).reset();
		}
	}

	// store the markers so that they can be reset later
	private mxCellMarker getCellMarker(PNGraphCell cell) {
		if (!markerReference.containsKey(cell.getId()))
			markerReference.put(cell.getId(),
					new mxCellMarker(pnEditor.getGraphComponent()));
		return markerReference.get(cell.getId());
	}

}
