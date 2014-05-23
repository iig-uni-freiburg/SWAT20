package de.uni.freiburg.iig.telematik.swat.editor.menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxIGraphModel.mxAtomicGraphModelChange;

import de.invation.code.toval.graphic.util.SpringUtilities;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.CPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPNPlace;
import de.uni.freiburg.iig.telematik.swat.editor.graph.CPNGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.CapacityChange;
import de.uni.freiburg.iig.telematik.swat.editor.graph.ConstraintChange;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.TokenChange;
import de.uni.freiburg.iig.telematik.swat.editor.graph.UpdateTokenChanges;
import de.uni.freiburg.iig.telematik.swat.resources.icons.IconFactory;

public class ConstraintsConfigurer extends JDialog {
	private static final double TOKEN_ROW_WIDTH = 300;
	private static final double TOKEN_ROW_HEIGHT = 40;
//	private static JDialog dialog;
	JPanel tokenPanel = new JPanel();
	private JButton addButton;
	private CPNGraph graph;
	private CPNFlowRelation place;
	private JPanel topPanel;
//	private JButton boundButton;
//	private JButton infiniteButton;
	private Multiset<String> constraintsForFR;
	private Map<String, Color> colors;
	private String placeName;
	private JPanel panel;

	public ConstraintsConfigurer(Window window, CPNFlowRelation flowRelation, CPNGraph cpnGraph) {
		super(window, flowRelation.getName());
//		setLayout(manager);
//		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

//		Dimension dimension = new Dimension(300,100);
//		setPreferredSize(dimension );
//		setMinimumSize(dimension);
////		setMaximumSize(dim2);
//		setSize(dimension);
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		add(panel);
//		add(new JLabel("gerd"));
		placeName = flowRelation.getName();
		graph = cpnGraph;
		topPanel = new JPanel();
//		dialog = dialog2;
//		add(this);
		updateView();
		try {
			addButton = new JButton(IconFactory.getIcon("maximize"));
			addButton.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					super.mouseClicked(e);
					Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), addButton);
					JPopupMenu popup = new JPopupMenu();
					if(constraintsForFR == null)
						constraintsForFR = new Multiset<String>();
					for (Entry<String, Color> c : colors.entrySet()) {
						final String color = c.getKey();
						if (!constraintsForFR.contains(color)) {
							JMenuItem item = new JMenuItem(color);
							item.setName(color);
							item.addActionListener(new ActionListener() {

								@Override
								public void actionPerformed(ActionEvent arg0) {
									((mxGraphModel) graph.getModel()).beginUpdate();
							
									
									((mxGraphModel) graph.getModel()).execute(new ConstraintChange((PNGraph)graph,place.getName(),constraintsForFR));
									((mxGraphModel) graph.getModel()).endUpdate();
									if (constraintsForFR.contains(colors.keySet())) {
										addButton.setEnabled(false);
									}
									updateView();
									pack();
								}
							});
							popup.add(item);
						}
						

					}

					popup.show(addButton, addButton.getWidth() * 4 / 5, addButton.getHeight() * 4 / 5);
				}
			});
			
//			infiniteButton = new JButton(IconFactory.getIcon("infinite"));
//			boundButton = new JButton(IconFactory.getIcon("bounded"));
			Dimension dim2 = new Dimension();
			double width2 = 40;
			double height2 = 40;
			dim2.setSize(width2, height2);
//			infiniteButton.setPreferredSize(dim2);
//			infiniteButton.setMinimumSize(dim2);
//			infiniteButton.setMaximumSize(dim2);
//			infiniteButton.setSize(dim2);
//			infiniteButton.addActionListener(new ActionListener() {
//				
//				@Override
//				public void actionPerformed(ActionEvent arg0) {
//					Set<String> tokencolors = graph.getNetContainer().getPetriNet().getTokenColors();
//					int newCapacity = -1;
//					((mxGraphModel) graph.getModel()).beginUpdate();
////					((mxGraphModel) graph.getModel()).execute(new UpdateTokenChanges((PNGraph)graph,place.getName()));
//					for(String color:tokencolors)
//						((mxGraphModel) graph.getModel()).execute(new CapacityChange((PNGraph)graph,place.getName(),color,newCapacity ));
////					((mxGraphModel) graph.getModel()).execute(new UpdateTokenChanges((PNGraph)graph,place.getName()));
//					((mxGraphModel) graph.getModel()).endUpdate();	
//					updateView();
//				}
//				
//			});
//
//			boundButton.setPreferredSize(dim2);
//			boundButton.setMinimumSize(dim2);
//			boundButton.setMaximumSize(dim2);
//			boundButton.setSize(dim2);
//			boundButton.addActionListener(new ActionListener() {
//				
//				@Override
//				public void actionPerformed(ActionEvent arg0) {
//					placeMarking = graph.getNetContainer().getPetriNet().getInitialMarking().get(placeName);
//if(placeMarking == null) placeMarking = new Multiset<String>();
//					//					restrictedColors = place.getColorsWithCapacityRestriction();
//					Set<String> tokencolors = graph.getNetContainer().getPetriNet().getTokenColors();
////					int newCapacity = -1;
//					//capacity bounded wechsel window update
//					((mxGraphModel) graph.getModel()).beginUpdate();
////					((mxGraphModel) graph.getModel()).execute(new UpdateTokenChanges((PNGraph)graph,place.getName()));
//					for(String color:tokencolors){
//					int newCapacity = placeMarking.multiplicity(color);
////					if(newCapacity ==0) newCapacity =1;
//					
//						((mxGraphModel) graph.getModel()).execute(new CapacityChange((PNGraph)graph,place.getName(),color,newCapacity ));
//					}
////					((mxGraphModel) graph.getModel()).execute(new UpdateTokenChanges((PNGraph)graph,place.getName()));
//					((mxGraphModel) graph.getModel()).endUpdate();
////					graph.updateViews();
//					updateView();
////					((CPNGraph)graph).updateTokenConfigurer(placeName);
//					
//				}
//			});
		
		} catch (ParameterException e) {
		} catch (PropertyException e) {
		} catch (IOException e) {
		}	
		
		
		
		final JPanel lastRow = new JPanel();
		lastRow.setLayout(new BorderLayout());
		Dimension dim = new Dimension();
		double width = TOKEN_ROW_WIDTH;
		double height = TOKEN_ROW_HEIGHT;
		dim.setSize(width, height);
		lastRow.setPreferredSize(dim);
		lastRow.setMinimumSize(dim);
		lastRow.setMaximumSize(dim);
		lastRow.setSize(dim);

		lastRow.add(addButton, BorderLayout.LINE_START);
		JPanel capacityPanel = new JPanel();
	
//		capacityPanel.add(infiniteButton);
//		capacityPanel.add(boundButton);
		lastRow.setAlignmentX(Component.RIGHT_ALIGNMENT);
		lastRow.add(capacityPanel, BorderLayout.CENTER);
		lastRow.setAlignmentX(Component.LEFT_ALIGNMENT);
		topPanel.add(lastRow);
		panel.add(topPanel);

	}



	private void addRow(String tokenLabel) {
		final JPanel row = new JPanel();
		row.setLayout(new BorderLayout());
		Dimension dim = new Dimension();
		double width = TOKEN_ROW_WIDTH;
		double height = TOKEN_ROW_HEIGHT;
		dim.setSize(width, height);
		row.setPreferredSize(dim);
		row.setMinimumSize(dim);
		row.setMaximumSize(dim);
		row.setSize(dim);
		
		final String tokenName = tokenLabel;
		int size = constraintsForFR.multiplicity(tokenLabel);
		Color tokenColor = colors.get(tokenLabel);
		CirclePanel circle = new CirclePanel(tokenColor);
//		int cap = (place.getColorCapacity(tokenName) <0)?99:place.getColorCapacity(tokenName);
//		System.out.println(tokenLabel+" = "+size + "#" + cap);
		SpinnerModel model = new SpinnerNumberModel(size, -1, 99, 1);
		JSpinner spinner = new JSpinner(model);
		spinner.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				System.out.println("gerd");
				JSpinner spinner = (JSpinner) e.getSource();
				Integer currentValue = (Integer)spinner.getValue();
				Multiset<String> newMarking = (Multiset<String>) graph.getNetContainer().getPetriNet().getFlowRelation(placeName).getConstraint();
				newMarking.setMultiplicity(tokenName, currentValue);
				((mxGraphModel) graph.getModel()).execute(new ConstraintChange((PNGraph)graph,placeName,newMarking));
//				graph.updateTokenConfigurer(placeName);
			}
		});
		
		JPanel firstElement = new JPanel();
		firstElement.add(circle);
		firstElement.add(spinner);
		firstElement.add(new JLabel(tokenName));
		row.add(firstElement, BorderLayout.LINE_START);
		row.setAlignmentX(Component.LEFT_ALIGNMENT);
		JButton remove = null;
		try {
			remove = new JButton(IconFactory.getIcon("minimize"));
		} catch (ParameterException e) {
		} catch (PropertyException e) {
		} catch (IOException e) {
		}
		remove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Multiset<String> newMarking = (Multiset<String>) graph.getNetContainer().getPetriNet().getFlowRelation(placeName).getConstraint();

				((mxGraphModel) graph.getModel()).beginUpdate();
//				((mxGraphModel) graph.getModel()).execute(new UpdateTokenChanges((PNGraph)graph,place.getName()));
				if(newMarking != null){
				newMarking.setMultiplicity(tokenName, 0);
				((mxGraphModel) graph.getModel()).execute(new ConstraintChange((PNGraph)graph,place.getName(),newMarking));}
//				((mxGraphModel) graph.getModel()).execute(new CapacityChange((PNGraph)graph,place.getName(),tokenName,0));
//				((mxGraphModel) graph.getModel()).execute(new UpdateTokenChanges((PNGraph)graph,place.getName()));
				((mxGraphModel) graph.getModel()).endUpdate();
				addButton.setEnabled(true);
				pack();
				updateView();
			}
		});
//		if(place != null){
//			JPanel capElement = new JPanel();
//			capElement.add(new JLabel("cap:"));		
//			JSpinner capacitySpinner;
//			if (!place.hasConstraints()) {
//				String[] string = { "\u221e" };
//				SpinnerModel capacityModel = new SpinnerListModel(string);
//				capacitySpinner = new JSpinner(capacityModel);
//			} 
//			else {
//				int capacitiy = place.getColorCapacity(tokenName);
//				SpinnerModel capacityModel = new SpinnerNumberModel(capacitiy, placeMarking.multiplicity(tokenName), 99, 1);
//				capacitySpinner = new JSpinner(capacityModel);
//			}
		
//		capacitySpinner.addChangeListener(new ChangeListener() {
//			
//			@Override
//			public void stateChanged(ChangeEvent e) {
//			JSpinner capacitySpinner = (JSpinner)e.getSource();
//				Integer currentValue = (Integer)capacitySpinner.getValue();
//
////				((mxGraphModel) graph.getModel()).execute(new UpdateTokenChanges((PNGraph)graph,place.getName()));
//				((mxGraphModel) graph.getModel()).execute(new CapacityChange((PNGraph)graph,place.getName(),tokenName,currentValue ));
////				((mxGraphModel) graph.getModel()).execute(new UpdateTokenChanges((PNGraph)graph,place.getName()));
//updateView();
//			}
//		});
//		capElement.add(capacitySpinner);
//		row.add(capElement, BorderLayout.CENTER);
//		}
		row.add(remove, BorderLayout.LINE_END);
		tokenPanel.add(row);

	}
	
//	private void addLastRow(final Multiset<String> newPlaceMarking, final Map<String, Color> colors, String tokenLabel) {
//		final JPanel lastRow = new JPanel();
//		lastRow.setLayout(new BorderLayout());
//		Dimension dim = new Dimension();
//		double width = TOKEN_ROW_WIDTH;
//		double height = TOKEN_ROW_HEIGHT;
//		dim.setSize(width, height);
//		lastRow.setPreferredSize(dim);
//		lastRow.setMinimumSize(dim);
//		lastRow.setMaximumSize(dim);
//		lastRow.setSize(dim);
//	
//
//		
//		JPanel firstElement = new JPanel();
//		lastRow.add(firstElement, BorderLayout.LINE_START);
//		lastRow.setAlignmentX(Component.LEFT_ALIGNMENT);
//
//		
//		}


	

	protected void setNewMarking(Multiset<String> newPlaceMarking) {	
	}



//	public static void setDialog(JDialog dialog2) {
//		dialog = dialog2;
//
//	}
//
//	public static JDialog getDialog() {
//		return dialog;
//	}

	public void updateView() {
		CPNGraphics cpnGraphics = graph.getNetContainer().getPetriNetGraphics();
		colors = cpnGraphics.getColors();
		if(graph.getNetContainer().getPetriNet().getTokenColors().contains("black"))
		colors.put("black", Color.BLACK);

		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		tokenPanel = new JPanel();
		tokenPanel.setLayout(new BoxLayout(tokenPanel, BoxLayout.Y_AXIS));
//		System.out.println(placeName);
		constraintsForFR = graph.getNetContainer().getPetriNet().getFlowRelation(placeName).getConstraint();
		place = graph.getNetContainer().getPetriNet().getFlowRelation(placeName);
		colors = cpnGraphics.getColors();
		for (String color : colors.keySet()) {
//			if (constraintsForFR == null){
//				constraintsForFR = new Multiset<String>();
////				if(place.isBounded())
////					addRow(color);
//			}
//			SpringUtilities.makeCompactGrid(parent, rows, cols, initialX, initialY, xPad, yPad);
			if (constraintsForFR.contains(color)){
					addRow(color);
					System.out.println(color);}
		}
		panel.removeAll();
		panel.add(tokenPanel);
		panel.add(topPanel);


		pack();
//		System.out.println("+"+SwingUtilities.getWindowAncestor(this));
//		dialog = (JDialog) SwingUtilities.getWindowAncestor(this);
//		dialog.pack();
//		System.out.println(getDialog());
////		if (getDialog() != null) {
//			getDialog().pack();
////		}

	}

//	private void putIntoChangeCommand(mxAtomicGraphModelChange command) {
//		((mxGraphModel) graph.getModel()).beginUpdate();
////		((mxGraphModel) graph.getModel()).execute(new UpdateTokenChanges((PNGraph)graph,place.getName()));
//		((mxGraphModel) graph.getModel()).execute(command);
////		((mxGraphModel) graph.getModel()).execute(new UpdateTokenChanges((PNGraph)graph,place.getName()));
//		((mxGraphModel) graph.getModel()).endUpdate();
//	}


}
