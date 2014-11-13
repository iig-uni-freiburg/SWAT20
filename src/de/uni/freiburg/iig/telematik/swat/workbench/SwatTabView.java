package de.uni.freiburg.iig.telematik.swat.workbench;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicButtonUI;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalCPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalIFNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.ACModel;
import de.uni.freiburg.iig.telematik.swat.bernhard.AnalyzePanelController;
import de.uni.freiburg.iig.telematik.swat.editor.CPNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.IFNetEditor;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.PTNetEditor;
import de.uni.freiburg.iig.telematik.swat.editor.event.PNEditorListener;
import de.uni.freiburg.iig.telematik.swat.logs.LogFileViewer;
import de.uni.freiburg.iig.telematik.swat.logs.LogModel;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.TimeContext;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatState.OperatingMode;
import de.uni.freiburg.iig.telematik.swat.workbench.exception.SwatComponentException;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatComponentsListener;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatTabViewListener;

@SuppressWarnings("serial")
public class SwatTabView extends JTabbedPane implements PNEditorListener, SwatComponentsListener {
	
	private static SwatTabView tabView = new SwatTabView();
	
	private Map<Object, Component> openedSwatComponents = new HashMap<Object, Component>();

	private Set<SwatTabViewListener> listeners = new HashSet<SwatTabViewListener>();

	private SwatTabView() {
		addChangeListener(new SwatTabViewAdapter());
	}

	public static SwatTabView getInstance() {
		return tabView;
	}

	public void componentSelected(SwatTreeNode node) {
		for(Object openedComponent: openedSwatComponents.keySet()){
			if(openedComponent == node.getUserObject()){
				try {
					//due to close button: userObject might reside inside openendComponents but tab is not visible
					setSelectedComponent(openedSwatComponents.get(openedComponent));
				} catch (IllegalArgumentException e) {
					//Tab no longer visible. Remove from openedSwatComponents
					openedSwatComponents.remove(openedComponent);
				}
				return;
			}
		}
	}
	
	public void addTabViewListener(SwatTabViewListener listener) {
		listeners.add(listener);
	}

	public boolean containsComponent(SwatTreeNode node) {
		return openedSwatComponents.keySet().contains(node.getUserObject());
	}


	private JTextArea getTextArea(String text){
		JTextArea newArea = new JTextArea(text);
		newArea.setEditable(false);
		return newArea;
	}
	
	@SuppressWarnings("rawtypes")
	private void addPNEditor(AbstractGraphicalPN petriNet, String tabName) throws SwatComponentException {
		if(petriNet instanceof GraphicalPTNet){
			PTNetEditor ptEditor = new PTNetEditor((GraphicalPTNet) petriNet, SwatComponents.getInstance().getPetriNetFile(
					petriNet.getPetriNet().getName()));
			ptEditor.addEditorListener(this);
			addTab(tabName, ptEditor);
		} else if(petriNet instanceof GraphicalCPN){
			CPNEditor cpnEditor = new CPNEditor((GraphicalCPN) petriNet, SwatComponents.getInstance().getPetriNetFile(
					petriNet.getPetriNet().getName()));
			cpnEditor.addEditorListener(this);
			addTab(tabName, cpnEditor);
		} else if(petriNet instanceof GraphicalIFNet){
			IFNetEditor ifEditor = new IFNetEditor((GraphicalIFNet) petriNet, SwatComponents.getInstance().getPetriNetFile(
					petriNet.getPetriNet().getName()));
			ifEditor.addEditorListener(this);
			addTab(tabName, ifEditor);
		}
		//openedSwatComponents.put(petriNet, getComponentAt(getComponentCount()-1));
		openedSwatComponents.put(petriNet, getComponentAt(getTabCount() - 1));
		setSelectedIndex(getTabCount()-1);
	}
	
	// private void addSwatComponent(SwatComponent swatComponent, String
	// tabName){
	// addTab(tabName, swatComponent.getMainComponent());
	//
	// }

	/**
	 * Adds node to the tab view. Additionally, returns the swat component that
	 * the PNEditor generated
	 * 
	 * @param node
	 *            from SwatTreeView
	 * @return The {@link SwatComponents} that the {@link PNEditor} generated
	 *         out of the node
	 */
	@SuppressWarnings("rawtypes")
	public WorkbenchComponent addNewTab(SwatTreeNode node) {
		if (alreadyOpen(node.getDisplayName()))
			return null;
		try {
			switch (node.getObjectType()) {
			case LABELING:
				// TODO:
				break;
			case PETRI_NET:
				addPNEditor((AbstractGraphicalPN) node.getUserObject(), node.getDisplayName());
				break;
			case PETRI_NET_ANALYSIS:
				// show pn editor, parent is the node with the net 
				SwatTreeNode parent=(SwatTreeNode) node.getParent();
				if(!alreadyOpen(parent.getDisplayName())) {
					addPNEditor((AbstractGraphicalPN) parent.getUserObject(), parent.getDisplayName());
				} else {
					// set the editor as active
					componentSelected(parent);
				}
				// switch operation mode and load analysis
				SwatState.getInstance().setOperatingMode(this, OperatingMode.ANALYSIS_MODE);
				//AnalyzePanelController.getInstance().loadSetting(parent.getFileReference().getName(), node.getFileReference());
				AnalyzePanelController.getInstance().loadSetting(node.getDisplayName());
				break;
			case AC_MODEL:
				break;
			case ANALYSIS_CONTEXT:
				break;
			case TIME_CONTEXT:
				break;
			case ARISTAFLOW_LOG:
			case MXML_LOG:
			case XES_LOG:
				addLogFile(node);
				break;
			}
			return (WorkbenchComponent) getComponentAt(getTabCount() - 1);
		} catch (ParameterException e) {
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(getParent()), "Cannot display component in new tab.\nReason: "+e.getMessage(), "SWAT Exception", JOptionPane.ERROR_MESSAGE);
			return null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private boolean alreadyOpen(String name) {
		for (int i = 0; i < this.getTabCount(); i++) {
			if (getTitleAt(i).equals(name))
				return true;
		}
		return false;
	}

//	private void addXmlFile(SwatTreeNode node) throws IOException {
//		// node holds XMLFileView
//		// Change it all to only use SwatComponent?
//		XMLFileViewer viewer = new XMLFileViewer((XESLogModel) node.getUserObject());
//		addTab(node.getDisplayName(), viewer.getMainComponent());
//		setSelectedIndex(getTabCount() - 1);
//		//openedSwatComponents.put((XMLFileViewer) node.getUserObject(), getComponentAt(getComponentCount() - 1));
//		openedSwatComponents.put((XMLFileViewer) node.getUserObject(), ((XMLFileViewer) node.getUserObject()));
//	}

	//	private void addNewTab(SwatComponent swatComponent) {
	//		if (swatComponent instanceof PNEditor) {
	//			addTab(swatComponent.getName(), swatComponent.getMainComponent());
	//		}
	//		if (swatComponent instanceof LogFileViewer) {
	//			addTab(swatComponent.getName(), swatComponent.getMainComponent());
	//		}
	//		// addTab(swatComponent.getName(), swatComponent.getMainComponent());
	//		// openedSwatComponents.put(swatComponent.getName(),
	//		// getComponentAt(getComponentCount() - 1));
	//
	//		setSelectedIndex(getTabCount() - 1);
	//
	//	}

	private void addLogFile(SwatTreeNode node) throws Exception {
		// addTab(((LogFileViewer) node.getUserObject()).getName(),
		// ((LogFileViewer) node.getUserObject()).getMainComponent());
		LogFileViewer viewer = new LogFileViewer((LogModel) node.getUserObject());
		addTab(node.getDisplayName(), viewer.getMainComponent());
		setSelectedIndex(getTabCount() - 1);
		//openedSwatComponents.put((LogFileViewer) node.getUserObject(), getComponentAt(getComponentCount() - 1));
		openedSwatComponents.put((LogModel) node.getUserObject(), viewer);
	}
	
	public void removeAll() {
		super.removeAll();
		openedSwatComponents.clear();
	}
	
	private void remove(AbstractGraphicalPN ptnet) {
		Component component = openedSwatComponents.get(ptnet);
		for (int i = 0; i < getTabCount(); i++) {
			System.out.println("Component: " + component + " TabComponent: " + getTabComponentAt(i));
			if (getComponentAt(i) == (component)) {
				removeTabAt(i);
				openedSwatComponents.remove(component);
			}
		}

	}

	public void remove(SwatTreeNode node) {
		switch (node.getObjectType()) {
		case LABELING:
			// TODO:
			break;
		case PETRI_NET:
			remove((AbstractGraphicalPN) node.getUserObject());
			break;
		case MXML_LOG:
		case ARISTAFLOW_LOG:
			remove((LogModel) node.getUserObject());
			openedSwatComponents.remove(node.getUserObject());
			break;

		}
	}

	private void remove(LogModel userObject) {
		Component component = openedSwatComponents.get(userObject);
		for (int i = 0; i < getTabCount(); i++) {
			System.out.println("Component: " + component + " TabComponent: " + getTabComponentAt(i));
			if (getComponentAt(i) == (component)) {
				removeTabAt(i);
				openedSwatComponents.remove(component);
			}
		}

	}

	/** make Tab with close button **/
	@Override
	public void addTab(String title, Component component) {
		super.addTab(title, component);
		//make tab removable
		this.setTabComponentAt(this.getTabCount() - 1, new ButtonTabComponent(title));
	}
	
	public void unsetModifiedCurrent() {
		((ButtonTabComponent) getTabComponentAt(getSelectedIndex())).unsetModified();
	}

	public void unsetModifiedAll() {
		for (int i = 0; i < getTabCount(); i++) {
			((ButtonTabComponent) getTabComponentAt(i)).unsetModified();
		}
	}

	/** notifies SwatTabViewListener if something interesting happened **/
	public class SwatTabViewAdapter implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent arg0) {
			for (SwatTabViewListener listener : listeners) {
				listener.activeTabChanged(((SwatTabView) arg0.getSource()).getSelectedIndex(),
						(WorkbenchComponent) ((SwatTabView) arg0.getSource()).getSelectedComponent());
			}

		}

	}

	/*
	 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights
	 * reserved.
	 * 
	 * Redistribution and use in source and binary forms, with or without
	 * modification, are permitted provided that the following conditions are
	 * met:
	 * 
	 * - Redistributions of source code must retain the above copyright notice,
	 * this list of conditions and the following disclaimer.
	 * 
	 * - Redistributions in binary form must reproduce the above copyright
	 * notice, this list of conditions and the following disclaimer in the
	 * documentation and/or other materials provided with the distribution.
	 * 
	 * - Neither the name of Oracle or the names of its contributors may be used
	 * to endorse or promote products derived from this software without
	 * specific prior written permission.
	 * 
	 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
	 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
	 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
	 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
	 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
	 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
	 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
	 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
	 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
	 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
	 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
	 */

	/**
	 * Component to be used as tabComponent; Contains a JLabel to show the text
	 * and a JButton to close the tab it belongs to
	 */
	public class ButtonTabComponent extends JPanel {
		private final String title;
		private JLabel label;

		public ButtonTabComponent(final String title) {
			//unset default FlowLayout' gaps
			super(new FlowLayout(FlowLayout.LEFT, 0, 0));
			if (title == null) {
				throw new NullPointerException("TabbedPane is null");
			}
			this.title = title;
			setOpaque(false);

			//make JLabel read titles from JTabbedPane
			//			JLabel label = new JLabel() {
			//				public String getText() {
			//					return title;
			//				}
			//			};
			label = new JLabel();

			label.setText(title);

			add(label);
			//add more space between the label and the button
			label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
			//tab button
			JButton button = new TabButton();
			add(button);
			//add more space to the top of the component
			setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
		}

		public void setModified() {
			//title = name;
			if (!label.getText().endsWith("*"))
				label.setText(title + "*");
			System.out.println(label.getText());
			repaint();
		}

		public void unsetModified() {
			if (label.getText().endsWith("*"))
				label.setText(label.getText().substring(0, label.getText().length() - 1));
		}

		public String getName() {
			return title;
		}

		private class TabButton extends JButton implements ActionListener {
			public TabButton() {
				int size = 17;
				setPreferredSize(new Dimension(size, size));
				setToolTipText("close this tab");
				//Make the button looks the same for all Laf's
				setUI(new BasicButtonUI());
				//Make it transparent
				setContentAreaFilled(false);
				//No need to be focusable
				setFocusable(false);
				setBorder(BorderFactory.createEtchedBorder());
				setBorderPainted(false);
				//Making nice rollover effect
				//we use the same listener for all buttons
				addMouseListener(buttonMouseListener);
				setRolloverEnabled(true);
				//Close the proper tab by clicking the button
				addActionListener(this);
			}

			public void actionPerformed(ActionEvent e) {
				int i = indexOfTabComponent(ButtonTabComponent.this);
				if (i != -1) {
					//openedSwatComponents.remove(SwatTabView.this.getComponent(i));
					for (Map.Entry<Object, Component> entry : openedSwatComponents.entrySet()) {
						if (entry.getValue().equals(SwatTabView.this.getComponent(i))) {
							openedSwatComponents.remove(entry.getKey());
							break;
						}
					}

					SwatTabView.this.remove(i);
				}
			}

			//we don't want to update UI for this button
			public void updateUI() {
			}

			//paint the cross
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g.create();
				//shift the image for pressed buttons
				if (getModel().isPressed()) {
					g2.translate(1, 1);
				}
				g2.setStroke(new BasicStroke(2));
				g2.setColor(Color.BLACK);
				if (getModel().isRollover()) {
					g2.setColor(Color.MAGENTA);
				}
				int delta = 6;
				g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
				g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
				g2.dispose();
			}
		}

		private final MouseListener buttonMouseListener = new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				Component component = e.getComponent();
				if (component instanceof AbstractButton) {
					AbstractButton button = (AbstractButton) component;
					button.setBorderPainted(true);
				}
			}

			public void mouseExited(MouseEvent e) {
				Component component = e.getComponent();
				if (component instanceof AbstractButton) {
					AbstractButton button = (AbstractButton) component;
					button.setBorderPainted(false);
				}
			}
		};
	}

	@Override
	public void modificationStateChanged(boolean modified) {
		if (modified) {
			((ButtonTabComponent) getTabComponentAt(getSelectedIndex())).setModified();
			//getTabComponentAt(getSelectedIndex()).setName(getTabComponentAt(getSelectedIndex()).getName() + "*");
			getTabComponentAt(getSelectedIndex()).repaint();
			revalidate();
			repaint();
		}
		else
			((ButtonTabComponent) getTabComponentAt(getSelectedIndex())).unsetModified();
		
	}

	@Override
	public void petriNetAdded(AbstractGraphicalPN net) {
		// TODO Auto-generated method stub

	}

	@Override
	public void petriNetRemoved(AbstractGraphicalPN net) {
		// TODO Auto-generated method stub

	}

	@Override
	public void petriNetRenamed(AbstractGraphicalPN net) {
		// TODO Auto-generated method stub

	}

	@Override
	public void acModelAdded(ACModel acModel) {
		// TODO Auto-generated method stub

	}

	@Override
	public void acModelRemoved(ACModel acModel) {
		// TODO Auto-generated method stub

	}

	@Override
	public void analysisContextAdded(String netID, AnalysisContext context) {
		// TODO Auto-generated method stub

	}

	@Override
	public void analysisContextRemoved(String netID, AnalysisContext context) {
		// TODO Auto-generated method stub

	}

	@Override
	public void analysisAdded(String netID, Analysis analysis) {
		// TODO Auto-generated method stub

	}

	@Override
	public void analysisRemoved(String netID, Analysis analysis) {
		// TODO Auto-generated method stub

	}

	@Override
	public void timeContextAdded(String netID, TimeContext context) {
		// TODO Auto-generated method stub

	}

	@Override
	public void timeContextRemoved(String netID, TimeContext context) {
		// TODO Auto-generated method stub

	}

	@Override
	public void logAdded(LogModel log) {
		// TODO Auto-generated method stub

	}

	@Override
	public void logRemoved(LogModel log) {
		for (Object openedComponent : openedSwatComponents.keySet()) {
			if (openedComponent instanceof LogFileViewer) {
				((LogFileViewer) openedComponent).getName().equals(log.getName());
				remove(openedSwatComponents.get(openedComponent));
			}
		}
	}

	@Override
	public void componentsChanged() {
		// TODO Auto-generated method stub

	}

}
