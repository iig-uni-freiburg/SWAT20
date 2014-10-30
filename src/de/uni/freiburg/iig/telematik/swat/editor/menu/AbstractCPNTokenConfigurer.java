package de.uni.freiburg.iig.telematik.swat.editor.menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.omg.CosNaming.IstringHelper;

import com.mxgraph.model.mxGraphModel;

import de.invation.code.toval.graphic.util.SpringUtilities;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalCPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.AbstractCPNGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractRegularIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AccessMode;
import de.uni.freiburg.iig.telematik.swat.editor.graph.IFNetGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraph;
import de.uni.freiburg.iig.telematik.swat.editor.graph.change.AccessModeChange;
import de.uni.freiburg.iig.telematik.swat.editor.graph.change.CapacityChange;
import de.uni.freiburg.iig.telematik.swat.editor.graph.change.ConstraintChange;
import de.uni.freiburg.iig.telematik.swat.editor.graph.change.TokenChange;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;

public class AbstractCPNTokenConfigurer extends JDialog {
	private static final double TOKEN_ROW_WIDTH = 250;
	private static final double TOKEN_ROW_HEIGHT = 40;
	// private static JDialog dialog;
	// JPanel tokenPanel = new JPanel();
	private JButton addButton;
	private PNGraph graph;
	private AbstractCPNPlace place;
	// private JPanel topPanel;
	private JToggleButton boundButton;
	private JToggleButton infiniteButton;
	private Multiset<String> multisetPA;
	private Map<String, Color> colors;
	private String paName;
	private JPanel panel;
	private boolean isPlace;
	private boolean isTransition = false;
	private Map<String, Set<AccessMode>> accessMode;

	public AbstractCPNTokenConfigurer(Window window, AbstractCPNPlace place2, PNGraph cpnGraph) {
		super(window, place2.getName());
		isPlace = true;
		panel = new JPanel();
		panel.setLayout(new SpringLayout());
		add(panel);
		paName = place2.getName();
		graph = cpnGraph;
		updateView();

	}

	public AbstractCPNTokenConfigurer(Window window, AbstractCPNFlowRelation flowRelation, PNGraph cpnGraph) {
		super(window, flowRelation.getName());
		isPlace = false;
		panel = new JPanel();
		panel.setLayout(new SpringLayout());
		add(panel);
		paName = flowRelation.getName();
		graph = cpnGraph;
		updateView();
	}

	public AbstractCPNTokenConfigurer(Window window, AbstractIFNetTransition<IFNetFlowRelation> transition, IFNetGraph cpnGraph) {
		super(window, transition.getName());
		isPlace = false;
		isTransition = true;
		panel = new JPanel();
		panel.setLayout(new SpringLayout());
		add(panel);
		paName = transition.getName();
		graph = cpnGraph;

		// for

		updateView();
	}

	private void addRow(String tokenLabel) {

		Color tokenColor = colors.get(tokenLabel);
		CirclePanel circle = null;
		try {
			circle = new CirclePanel(tokenColor);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Circle-Panel could not be generated. \nReason: "+e.getMessage(), ""+e.getClass(), JOptionPane.ERROR);
		}
		panel.add(circle);

		final String tokenName = tokenLabel;
		if (!isTransition) {
			int size = multisetPA.multiplicity(tokenLabel);
			int cap = 99;
			if (isPlace && !(place.getColorCapacity(tokenName) < 0))
				cap = place.getColorCapacity(tokenName);
			SpinnerModel model = new SpinnerNumberModel(size, -1, cap, 1);
			JSpinner spinner = new JSpinner(model);
			spinner.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					JSpinner spinner = (JSpinner) e.getSource();
					Integer currentValue = (Integer) spinner.getValue();

					Multiset<String> newMarking = getMultiSet();
					if (newMarking == null)
						newMarking = new Multiset<String>();
					newMarking.setMultiplicity(tokenName, currentValue);
					if (isPlace)
						((mxGraphModel) graph.getModel()).execute(new TokenChange((PNGraph) graph, paName, newMarking));
					else
						((mxGraphModel) graph.getModel()).execute(new ConstraintChange((PNGraph) graph, paName, newMarking));
				}
			});
			panel.add(spinner);
		}
		if (isTransition) {

			JPanel amPanel = new JPanel();
			amPanel.add(createAccessModeCheckBox(tokenName, AccessMode.READ, "r"));
			amPanel.add(createAccessModeCheckBox(tokenName, AccessMode.WRITE, "w"));
			amPanel.add(createAccessModeCheckBox(tokenName, AccessMode.DELETE, "d"));
			amPanel.add(createAccessModeCheckBox(tokenName, AccessMode.CREATE, "c"));

			panel.add(amPanel);

		}

		panel.add(new JLabel(tokenName));
		// panel.add(firstElement);
		JButton remove = null;
		try {
			remove = new JButton(IconFactory.getIcon("minimize"));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Minimize-Button could not be added. \nReason: "+e.getMessage(), ""+e.getClass(), JOptionPane.ERROR);
		}
		remove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!isTransition) {
					Multiset<String> newMarking = getMultiSet();
					((mxGraphModel) graph.getModel()).beginUpdate();
					if (isPlace) {
						if (newMarking != null) {
							newMarking.setMultiplicity(tokenName, 0);
							((mxGraphModel) graph.getModel()).execute(new TokenChange((PNGraph) graph, paName, newMarking));
						}
						((mxGraphModel) graph.getModel()).execute(new CapacityChange((PNGraph) graph, paName, tokenName, 0));
					} else {
						newMarking.setMultiplicity(tokenName, 0);
						((mxGraphModel) graph.getModel()).execute(new ConstraintChange((PNGraph) graph, paName, newMarking));
					}

					((mxGraphModel) graph.getModel()).endUpdate();
				} else {
					((mxGraphModel) graph.getModel()).execute(new AccessModeChange(graph, paName, tokenName, new HashSet<AccessMode>()));
					Object transition = graph.getNetContainer().getPetriNet().getTransition(paName);
					if (transition instanceof AbstractRegularIFNetTransition)
						((AbstractRegularIFNetTransition) transition).removeAccessModes(tokenName);
				}

				addButton.setEnabled(true);
				pack();
				updateView();
			}
		});

		if (isPlace && place != null) {

			panel.add(Box.createGlue());
			JSpinner capacitySpinner;
			if (!place.isBounded()) {
				String[] string = { "\u221e" };
				SpinnerModel capacityModel = new SpinnerListModel(string);
				capacitySpinner = new JSpinner(capacityModel);
			} else {
				int capacitiy = place.getColorCapacity(tokenName);
				SpinnerModel capacityModel = new SpinnerNumberModel(capacitiy, multisetPA.multiplicity(tokenName), 99, 1);
				capacitySpinner = new JSpinner(capacityModel);
			}

			capacitySpinner.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					JSpinner capacitySpinner = (JSpinner) e.getSource();
					Integer currentValue = (Integer) capacitySpinner.getValue();

					((mxGraphModel) graph.getModel()).execute(new CapacityChange((PNGraph) graph, place.getName(), tokenName, currentValue));
					updateView();
				}
			});
			panel.add(capacitySpinner);
		} else {
			panel.add(Box.createGlue());
			panel.add(Box.createGlue());
		}
		panel.add(remove);

	}

	private JCheckBox createAccessModeCheckBox(final String tokenName, final AccessMode accessModi, String accessModeName) {
		JCheckBox read = new JCheckBox(accessModeName);
		read.setSelected(accessMode.get(tokenName).contains(accessModi));
		read.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JCheckBox cb = (JCheckBox) e.getSource();
				Set am = accessMode.get(tokenName);
				if (!(am.contains(AccessMode.CREATE) && accessModi.equals(AccessMode.DELETE)) && !(am.contains(AccessMode.DELETE) && accessModi.equals(AccessMode.CREATE))) {
					Set amChange = ((Set) ((HashSet) am).clone());
					if (cb.isSelected()) {
						amChange.add(accessModi);
					} else {
						amChange.remove(accessModi);
					}
					((mxGraphModel) graph.getModel()).execute(new AccessModeChange(graph, paName, tokenName, amChange));
				}
				else {
					cb.setSelected(false);
					JOptionPane.showMessageDialog(null,"First deselect Create/Delete to select this access mode" ,"Create/Delete exclude each other", JOptionPane.ERROR_MESSAGE);
				}
			}

		});
		read.setToolTipText(accessModi.toString());
		return read;
	}

	protected Multiset<String> getMultiSet() {
		if (isPlace)
			return (Multiset<String>) graph.getNetContainer().getPetriNet().getInitialMarking().get(paName);
		else
			return (Multiset<String>) graph.getNetContainer().getPetriNet().getFlowRelation(paName).getConstraint();
	}

	protected void setNewMarking(Multiset<String> newPlaceMarking) {
	}



	public void updateView() {
		panel.removeAll();

		try {
			addButton = new JButton(IconFactory.getIcon("maximize"));
			addButton.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					super.mouseClicked(e);
					JPopupMenu popup = new JPopupMenu();
					if (multisetPA == null)
						multisetPA = new Multiset<String>();
					for (Entry<String, Color> c : colors.entrySet()) {
						final String color = c.getKey();
						if (!isTransition && !multisetPA.contains(color)) {
							JMenuItem item = new JMenuItem(color);
							item.setName(color);

							item.addActionListener(new ActionListener() {

								@Override
								public void actionPerformed(ActionEvent arg0) {
									((mxGraphModel) graph.getModel()).beginUpdate();
									if (isPlace) {
										if (place.getColorCapacity(color) == 0)
											((mxGraphModel) graph.getModel()).execute(new CapacityChange((PNGraph) graph, paName, color, 1));
										multisetPA.setMultiplicity(color, 1);
										((mxGraphModel) graph.getModel()).execute(new TokenChange((PNGraph) graph, paName, multisetPA));
									} else {
										multisetPA.setMultiplicity(color, 1);
										((mxGraphModel) graph.getModel()).execute(new ConstraintChange((PNGraph) graph, paName, multisetPA));
									}
									((mxGraphModel) graph.getModel()).endUpdate();
									if (!isTransition && multisetPA.contains(colors.keySet())) {
										addButton.setEnabled(false);
									}
									updateView();
									pack();
								}
							});
							popup.add(item);
						}

						if (isTransition && !accessMode.keySet().contains(color) && !color.contains("black")) {
							JMenuItem item = new JMenuItem(color);
							item.setName(color);

							item.addActionListener(new ActionListener() {

								@Override
								public void actionPerformed(ActionEvent arg0) {
									((mxGraphModel) graph.getModel()).beginUpdate();
									if (isTransition) {

										// accessMode.put(color, new
										// HashSet<AccessMode>());
										// Set am = accessMode.get(color);
										// Set amChange = ((Set) ((HashSet)
										// am).clone());
										Set amChange = new HashSet<AccessMode>();
										// amChange.add(AccessMode.READ);

										((mxGraphModel) graph.getModel()).execute(new AccessModeChange(graph, paName, color, amChange));

										if (!isTransition && accessMode.keySet().contains(colors.keySet())) {
											addButton.setEnabled(false);
										}
									}
									((mxGraphModel) graph.getModel()).endUpdate();
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
			if (isPlace) {
				infiniteButton = new JToggleButton(IconFactory.getIcon("infinite"));
				infiniteButton.setBorder(BorderFactory.createEmptyBorder());
				boundButton = new JToggleButton(IconFactory.getIcon("bounded"));
				boundButton.setBorder(BorderFactory.createEmptyBorder());
				Dimension dim2 = new Dimension();
				double width2 = 35;
				double height2 = 35;
				dim2.setSize(width2, height2);
				infiniteButton.setPreferredSize(dim2);
				infiniteButton.setMinimumSize(dim2);
				infiniteButton.setMaximumSize(dim2);
				infiniteButton.setSize(dim2);
				infiniteButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {

						Set<String> tokencolors = getTokenColors();
						int newCapacity = -1;
						((mxGraphModel) graph.getModel()).beginUpdate();
						for (String color : tokencolors)
							((mxGraphModel) graph.getModel()).execute(new CapacityChange((PNGraph) graph, paName, color, newCapacity));
						((mxGraphModel) graph.getModel()).endUpdate();
						updateView();
					}

				});

				boundButton.setPreferredSize(dim2);
				boundButton.setMinimumSize(dim2);
				boundButton.setMaximumSize(dim2);
				boundButton.setSize(dim2);
				boundButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						multisetPA = getMultiSet();
						if (multisetPA == null)
							multisetPA = new Multiset<String>();
						Set<String> tokencolors = getTokenColors();
						((mxGraphModel) graph.getModel()).beginUpdate();
						for (String color : tokencolors) {
							int newCapacity = multisetPA.multiplicity(color);
							((mxGraphModel) graph.getModel()).execute(new CapacityChange((PNGraph) graph, paName, color, newCapacity));
						}
						((mxGraphModel) graph.getModel()).endUpdate();
						updateView();

					}
				});
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Buttons could not be added. \nReason: "+e.getMessage(), ""+e.getClass(), JOptionPane.ERROR);


		}

		panel.add(addButton);

		// JPanel capacityPanel = new JPanel();

		if (isPlace)
			panel.add(new JLabel("Tokens"));
		else if (isTransition)
			panel.add(new JLabel("Data Usage"));
		else
			panel.add(new JLabel("Constraints"));
		panel.add(Box.createGlue());
		panel.add(Box.createGlue());
		if (isPlace)
			panel.add(new JLabel("Capacity"));
		else
			panel.add(Box.createGlue());
		panel.add(Box.createGlue());
		int size = 0;

		AbstractCPNGraphics cpnGraphics = (AbstractCPNGraphics) getNetContainer().getPetriNetGraphics();
		colors = cpnGraphics.getColors();
		if (getTokenColors().contains("black"))
			colors.put("black", Color.BLACK);

		if (!isTransition)
			multisetPA = getMultiSet();
		else {

			Object transition = graph.getNetContainer().getPetriNet().getTransition(paName);
			if (transition instanceof AbstractRegularIFNetTransition)
				accessMode = ((AbstractRegularIFNetTransition) transition).getAccessModes();
		}
		place = (AbstractCPNPlace) getNetContainer().getPetriNet().getPlace(paName);
		colors = cpnGraphics.getColors();
		for (String color : colors.keySet()) {
			if (multisetPA == null && !isTransition) {
				multisetPA = new Multiset<String>();
			}
			if (isPlace && (place.getColorCapacity(color) > 0 || multisetPA.contains(color))) {
				addRow(color);
				size++;
			}
			if (!isPlace && !isTransition && multisetPA.contains(color)) {
				addRow(color);
				size++;
			}
			if (!isPlace && isTransition && accessMode.containsKey(color)) {

				addRow(color);
				size++;
			}

		}

		// final JPanel lastRow = new JPanel();
		// lastRow.setLayout(new BorderLayout());
		Dimension dim = new Dimension();
		double width = TOKEN_ROW_WIDTH;
		double height = TOKEN_ROW_HEIGHT;
		dim.setSize(width, height);
		panel.add(Box.createGlue());

		panel.add(Box.createGlue());
		// C capPanel = new JPanel();
		panel.add(Box.createGlue());
		panel.add(Box.createGlue());
		JPanel boundOrInfinite = new JPanel();
		if (isPlace) {
			boundOrInfinite.add(infiniteButton);
			boundOrInfinite.add(boundButton);
			if (place.isBounded()) {
				boundButton.setEnabled(false);
				infiniteButton.setEnabled(true);
			}
			if (!place.isBounded()) {
				boundButton.setEnabled(true);
				infiniteButton.setEnabled(false);
			}
		} else {
			panel.add(Box.createGlue());
			panel.add(Box.createGlue());
		}

		SpringUtilities.makeCompactGrid(boundOrInfinite, 1, 2, 1, 1, 1, 1);
		panel.add(boundOrInfinite);

		panel.add(Box.createGlue());

		SpringUtilities.makeCompactGrid(panel, size + 2, 6, 6, 6, 6, 6);
		pack();

	}

	private AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?, ?, ?> getNetContainer() {
		AbstractGraphicalCPN graphicalNet = (AbstractGraphicalCPN) graph.getNetContainer();
		return graphicalNet;
	}

	protected Set<String> getTokenColors() {
		AbstractCPN net = (AbstractCPN) graph.getNetContainer().getPetriNet();
		return net.getTokenColors();
	}

}
