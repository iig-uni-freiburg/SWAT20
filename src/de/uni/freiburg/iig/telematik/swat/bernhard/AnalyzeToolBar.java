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
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTFlowRelation;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
/**
 * This class represents a Toolbar used to play a counterexample
 * it consists of some buttons to play pause forward or rewind a counterexample
 * It starts a thread to play the CE with a pause of 3sec between each firing
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
	private JButton resetButton, stepButton, backButton, highlightButton, playPauseButton;
	private List<JButton> buttonList;
	private boolean highlightedPath = false;
	private Thread thread;

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
			backButton = new JButton(IconFactory.getIcon("first"));
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
					playCounterExample();
				}
			});
			/*stopButton = new JButton(IconFactory.getIcon("sleep"));
			stopButton.setToolTipText("Pause playing Counterexample");
			stopButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					stopCounterExample();
				}
			});*/
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

	protected void doStepBack() {
		// TODO Auto-generated method stub
		int position=counterExample.getCurrentPosition();
		resetCounterExample();
		for(int i=0; i < position-1; i++) {
			doStep();
		}
		if(counterExample.getCurrentPosition()==0) {
			backButton.setEnabled(false);
		}
	}

	protected void playPauseButtonRemoveListeners() {
		ActionListener listeners[]=playPauseButton.getActionListeners();
		if(listeners==null) {
			return;
		}
		for(ActionListener l:listeners) {
			playPauseButton.removeActionListener(l);
		}
	}
	protected void playPauseButtonSetActionPlay() {
		playPauseButtonRemoveListeners();
		playPauseButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				playCounterExample();
			}
		});
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
	}
	protected void playPauseButtonSetActionPause() {
		playPauseButtonRemoveListeners();
		playPauseButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				stopCounterExample();
			}
		});
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
	protected void stopCounterExample() {
		// TODO Auto-generated method stub
		thread.interrupt();
		playPauseButtonSetActionPlay();
		stepButton.setEnabled(true);
		backButton.setEnabled(true);
		resetButton.setEnabled(true);
	}

	protected void playCounterExample() {
		// TODO Auto-generated method stub

		stepButton.setEnabled(false);
		backButton.setEnabled(false);
		resetButton.setEnabled(false);
		playPauseButtonSetActionPause();
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
	}
/**
 * this method is invoked by the analyePanel
 * it loads the given path and resets and activates the toolbar
 * @param path the counterexample, List of Transitions
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
		if(!thread.isAlive())
			backButton.setEnabled(true);
		pnEditor.updateUI();
	}

	private void resetCounterExample() {
		counterExample.setCurrentPosition(0);
		stepButton.setEnabled(true);
		backButton.setEnabled(false);
		playPauseButton.setEnabled(true);
		playPauseButtonSetActionPlay();
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
		highlightedPath = false;
		resetHighlightning();
		counterExample = null;
	}

	public void highLightCounterExample() {
		Map<String, PNGraphCell> nodemap = pnEditor.getGraphComponent()
				.getGraph().nodeReferences;
		Map<String, PNGraphCell> arcmap = pnEditor.getGraphComponent()
				.getGraph().arcReferences;

		// System.out.println(nodemap);
		// get places to mark
		java.util.List<String> transitions = counterExample.getPath();
		;
		AbstractPetriNet pn = pnEditor.getNetContainer().getPetriNet();
		java.util.List<String> nodeList = new ArrayList<String>();
		java.util.List<String> arcList = new ArrayList<String>();
		nodeList.addAll(transitions);

		// we have to find the places and arcs in the path

		for (int i = 0; i < transitions.size() - 1; i++) {
			// first incoming places with no incoming edges
			java.util.List<PTFlowRelation> placesBeforeThis = pn
					.getTransition(transitions.get(i)).getIncomingRelations();
			for (PTFlowRelation relation : placesBeforeThis) {
				if (relation.getPlace().getIncomingRelations().size() == 0) {
					// add the place name
					String placeName = relation.getPlace().getName();
					nodeList.add(placeName);
					// add the arc
					arcList.add("arcPT_" + placeName + transitions.get(i));
				}
			}
			// then places between this transition and the next
			java.util.List<PTFlowRelation> placesAfterRel = pn.getTransition(
					transitions.get(i)).getOutgoingRelations();
			java.util.List<String> placesAfter = new ArrayList<String>();
			for (PTFlowRelation relation : placesAfterRel) {
				placesAfter.add(relation.getPlace().getName());
			}
			java.util.List<PTFlowRelation> placesBeforeRel = pn.getTransition(
					transitions.get(i + 1)).getIncomingRelations();
			java.util.List<String> placesBefore = new ArrayList<String>();
			for (PTFlowRelation relation : placesBeforeRel) {
				placesBefore.add(relation.getPlace().getName());
			}
			// add the intersection of both lists
			java.util.List<String> temp_list = Helpers.Intersection(
					placesAfter, placesBefore);
			for (String place : temp_list) {
				nodeList.add(place);
				// both edges
				arcList.add("arcTP_" + transitions.get(i) + place);
				arcList.add("arcPT_" + place + transitions.get(i + 1));
			}
		}
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
		// System.out.println(arcmap);

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
