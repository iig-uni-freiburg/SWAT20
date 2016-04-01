package de.uni.freiburg.iig.telematik.swat.workbench;

import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponentType;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;
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
import java.util.HashSet;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicButtonUI;

import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogReader;

import de.invation.code.toval.misc.soabase.SOABase;
import de.invation.code.toval.misc.wd.ComponentListener;
import de.invation.code.toval.misc.wd.ProjectComponentException;
import de.invation.code.toval.validate.ExceptionDialog;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalCPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalIFNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalTimedNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.Labeling;
import de.uni.freiburg.iig.telematik.sewol.accesscontrol.AbstractACModel;
import de.uni.freiburg.iig.telematik.swat.analysis.Analysis;
import de.uni.freiburg.iig.telematik.swat.analysis.AnalysisController;
import de.uni.freiburg.iig.telematik.swat.editor.SwatIFNetEditorComponent;
import de.uni.freiburg.iig.telematik.swat.logs.LogFileViewer;
import de.uni.freiburg.iig.telematik.swat.logs.LogModel;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.TimeContext;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatState.OperatingMode;
import de.uni.freiburg.iig.telematik.swat.workbench.exception.SwatComponentException;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatTabViewListener;
import de.uni.freiburg.iig.telematik.wolfgang.editor.AbstractWolfgang;
import de.uni.freiburg.iig.telematik.wolfgang.editor.WolfgangCPN;
import de.uni.freiburg.iig.telematik.wolfgang.editor.WolfgangPT;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.CPNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.IFNetEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PTNetEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.RTPNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.ViewComponent;
import de.uni.freiburg.iig.telematik.wolfgang.event.PNEditorListener;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class SwatTabView extends JTabbedPane implements PNEditorListener, ComponentListener {

    private static SwatTabView tabView = null;

    //private Map<Object, Component> openedSwatComponents = new HashMap<Object, Component>();
    private Set<SwatTabViewListener> listeners = new HashSet<SwatTabViewListener>();

    private SwatTabView()  {
        addChangeListener(new SwatTabViewAdapter());
        try {
			SwatComponents.getInstance().addComponentListener(this);
		} catch (ProjectComponentException e) {
			Workbench.errorMessage("Could not add SwatTabView as component listener. Saving-hints(*) not available", e, true);
		}
    }

    public static SwatTabView getInstance() {
        if (tabView == null) {
            tabView = new SwatTabView();
        }
        return tabView;
    }

    public void componentSelected(SwatTreeNode node) {
        int index = getIndexOf(node.getDisplayName());
        if (index >= 0) {
            setSelectedIndex(getIndexOf(node.getDisplayName()));
        }

        //		for(Object openedComponent: openedSwatComponents.keySet()){
        //			if(openedComponent == node.getUserObject()){
        //				try {
        //					//due to close button: userObject might reside inside openendComponents but tab is not visible
        //					setSelectedComponent(openedSwatComponents.get(openedComponent));
        //				} catch (IllegalArgumentException e) {
        //					//Tab no longer visible. Remove from openedSwatComponents
        //					openedSwatComponents.remove(openedComponent);
        //				}
        //				return;
        //			}
        //		}
    }

    public int getIndexOf(String tabDisplayName) {
        for (int i = 0; i < getTabCount(); i++) {
            if (((ButtonTabComponent) getTabComponentAt(i)).getName().equalsIgnoreCase(tabDisplayName)) {
                return i;
            }
        }
        return -1;
    }

    public void addTabViewListener(SwatTabViewListener listener) {
        listeners.add(listener);
    }

    public boolean containsComponent(SwatTreeNode node) {
        if (getIndexOf(node.getDisplayName()) >= 0) {
            return true;
        }
        return false;
    }

    //	public boolean containsTab(String name) {
    //		for (int i = 0; i < getTabCount(); i++)
    //			if (((TabButton) getTabComponentAt(i)).getName.equals(name))
    //				return true;
    //	}
    public boolean hasUnsavedChange(int index) {
        return ((ButtonTabComponent) getTabComponentAt(index)).unsaved;
    }

    //	private JTextArea getTextArea(String text){
    //		JTextArea newArea = new JTextArea(text);
    //		newArea.setEditable(false);
    //		return newArea;
    //	}
    @SuppressWarnings("rawtypes")
    private synchronized void addPNEditor(AbstractGraphicalPN petriNet, String tabName) throws SwatComponentException {
    	boolean layout = false;
    			try {
					layout = SwatComponents.getInstance().getContainerPetriNets().needsLayout(petriNet.getPetriNet().getName());
				} catch (ProjectComponentException e) {
				}
    			
        if (petriNet instanceof GraphicalPTNet) {
            PTNetEditorComponent ptEditor = new PTNetEditorComponent((GraphicalPTNet) petriNet,layout);
            ptEditor.addEditorListener(this);
            addTab(tabName, ptEditor);
        } else if (petriNet instanceof GraphicalCPN) {
            CPNEditorComponent cpnEditor = new CPNEditorComponent((GraphicalCPN) petriNet,layout);
            cpnEditor.addEditorListener(this);
            addTab(tabName, cpnEditor);
        } else if (petriNet instanceof GraphicalIFNet) {
            IFNetEditorComponent ifEditor = new SwatIFNetEditorComponent((GraphicalIFNet) petriNet,layout);
            ifEditor.addEditorListener(this);
            addTab(tabName, ifEditor);
        }
            else if (petriNet instanceof GraphicalTimedNet ) {
            	RTPNEditorComponent ptEditor = new RTPNEditorComponent((GraphicalTimedNet) petriNet,layout);
            	ptEditor.addEditorListener(this);
            	addTab(tabName, ptEditor);
            }
        

//		Misses: , SwatComponents.getInstance().getPetriNetFile(
//				petriNet.getPetriNet().getName())?
        //openedSwatComponents.put(petriNet, getComponentAt(getComponentCount()-1));
        //openedSwatComponents.put(petriNet, getComponentAt(getTabCount() - 1));
        setSelectedIndex(getTabCount() - 1);
        
        try {
			SwatComponents.getInstance().getContainerPetriNets().removeLayoutNeed(petriNet.getPetriNet().getName());
		} catch (ProjectComponentException e) {
			
		}
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
     * @param node from SwatTreeView
     * @return The {@link SwatComponents} that the {@link PNEditor} generated
     * out of the node
     */
    @SuppressWarnings("rawtypes")
    public ViewComponent addNewTab(SwatTreeNode node) {
       // System.out.println("Adding " + node.getDisplayName());
        if (alreadyOpen(node.getDisplayName())) {
            return null;
        }
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
                    SwatTreeNode parent = (SwatTreeNode) node.getParent();
                    if (!alreadyOpen(parent.getDisplayName())) {
                        addPNEditor((AbstractGraphicalPN) parent.getUserObject(), parent.getDisplayName());
                    } else {
                        // set the editor as active
                        componentSelected(parent);
                    }
                    // switch operation mode and load analysis
                    SwatState.getInstance().setOperatingMode(this, OperatingMode.ANALYSIS_MODE);
                    //AnalyzePanelController.getInstance().loadSetting(parent.getFileReference().getName(), node.getFileReference());
                    if (!(node.getUserObject() instanceof Analysis)) {
                        AnalysisController.getInstance((ViewComponent) node.getUserObject()).getAnalyzePanel();
                    }
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
            return (ViewComponent) getComponentAt(getTabCount() - 1);
        } catch (ParameterException e) {
            Workbench.errorMessage("Cannot display component", e, false);
            //JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(getParent()), "Cannot display component in new tab.\nReason: "+e.getMessage(), "SWAT Exception", JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (Exception e) {
            Workbench.errorMessage("Cannot display component", e, false);
            return null;
        }
    }

    private boolean alreadyOpen(String name) {
        for (int i = 0; i < this.getTabCount(); i++) {
            if (getTitleAt(i).equals(name)) {
                return true;
            }
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
        //openedSwatComponents.put((LogModel) node.getUserObject(), viewer);
    }

    public void removeAll() {
        super.removeAll();
        //openedSwatComponents.clear();
    }

    public boolean closeTabAndAskUser(String netID) {
        for (int i = 0; i < getTabCount(); i++) {
            if (((ButtonTabComponent) getTabComponentAt(i)).getName().equalsIgnoreCase(netID)) {
                System.out.println("Asking close for " + ((ButtonTabComponent) getTabComponentAt(i)).getName());
                return closeTabAndAskUser(i);
            }
        }
        return true; //tab not open
    }

    /**
     * ask user if unsaved changes. Returns false if aborted. Saves tab if
     * needed
     *
     */
    public boolean closeTabAndAskUser(int index) {
        ButtonTabComponent tab = (ButtonTabComponent) getTabComponentAt(index);
        boolean unsaved = tab.unsaved;
        int userChoice = JOptionPane.CANCEL_OPTION;
        if (unsaved) { //test if this tab has unsaved changes
            userChoice = JOptionPane.showConfirmDialog(SwatTabView.this, "Save unsaved changes for " + tab.getName() + "?");
            switch (userChoice) {
                case JOptionPane.CANCEL_OPTION:
                    return false; //User wants to abort
                case JOptionPane.YES_OPTION:

                    try {
                        String netName = ((PNEditorComponent) getComponent(index)).getNetContainer().getPetriNet().getName();
                        SwatComponents.getInstance().getContainerPetriNets().storeComponent(netName);
                    } catch (Exception ex) {
                        ExceptionDialog.showException(SwingUtilities.getWindowAncestor(SwatTabView.this), "Storage Exception", new Exception("Cannot store Petri net", ex), true, false);
                    }
                    break;
                case JOptionPane.NO_OPTION:
                    System.out.println(this.getClass().getSimpleName() + ": Trying to cast tab at index " + index + "(" + getComponent(index)
                            + ") to PNEditor");
                    PNEditorComponent editor = (PNEditorComponent) getComponent(index);
                    while (editor.getUndoManager().canUndo()) {
                        editor.getUndoManager().undo();
                    }
            }

        }
        removeTabAt(index);
        //openedSwatComponents.remove(getComponent(index));
        return true; //user decided to either save or reject changes
    }

    private void remove(AbstractGraphicalPN ptnet) {
        try {
            removeTabAt(getIndexOf(ptnet.getPetriNet().getName()));
        } catch (IndexOutOfBoundsException e) {

        }
    }

    public void remove(SwatTreeNode node) {
        try {
            removeTabAt(getIndexOf(node.getDisplayName()));
        } catch (IndexOutOfBoundsException e) {

        }
        //		switch (node.getObjectType()) {
        //		case LABELING:
        //			// TODO:
        //			break;
        //		case PETRI_NET:
        //			remove((AbstractGraphicalPN) node.getUserObject());
        //			break;
        //		case MXML_LOG:
        //		case ARISTAFLOW_LOG:
        //			remove((LogModel) node.getUserObject());
        //			openedSwatComponents.remove(node.getUserObject());
        //			break;
        //
        //		}
    }

    private void remove(LogModel userObject) {
        try {
            removeTabAt(getIndexOf(userObject.getName()));
        } catch (IndexOutOfBoundsException e) {

        }
    }
//		Component component = openedSwatComponents.get(userObject);
//		for (int i = 0; i < getTabCount(); i++) {
//			System.out.println("Component: " + component + " TabComponent: " + getTabComponentAt(i));
//			if (getComponentAt(i) == (component)) {
//				removeTabAt(i);
//				openedSwatComponents.remove(component);
//			}
//		}

    /**
     * make Tab with close button *
     */
    @Override
    public void addTab(String title, Component component) {
        super.addTab(title, component);
        //make tab removable
        this.setTabComponentAt(this.getTabCount() - 1, new ButtonTabComponent(title, component));
    }

    public void unsetModifiedCurrent() {
        ((ButtonTabComponent) getTabComponentAt(getSelectedIndex())).unsetModified();
    }

    public void unsetModifiedAll() {
        for (int i = 0; i < getTabCount(); i++) {
            ((ButtonTabComponent) getTabComponentAt(i)).unsetModified();
        }
    }

    /**
     * notifies SwatTabViewListener if something interesting happened *
     */
    public class SwatTabViewAdapter implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent arg0) {
        	SwatTabView view =(SwatTabView) arg0.getSource();
        	
            if (view.getSelectedComponent() instanceof ViewComponent) {
                for (SwatTabViewListener listener : listeners) {
                    listener.activeTabChanged(view.getSelectedIndex(),
                            (ViewComponent) (view.getSelectedComponent()));       
                }
            }          
            
            if (view.getSelectedComponent() instanceof PNEditorComponent) {
	            AbstractWolfgang wolfgang = null;
	            PNEditorComponent pnEditor = (PNEditorComponent) view.getSelectedComponent();
	            try {
	                if (pnEditor instanceof PTNetEditorComponent)
						wolfgang = new WolfgangPT();				
					else if (pnEditor instanceof CPNEditorComponent)
	                	wolfgang = new WolfgangCPN();
					else if (pnEditor instanceof RTPNEditorComponent)
						wolfgang = new WolfgangPT();
					else
						wolfgang = new WolfgangPT();
	                /*
	                 * WolfgangIF() do not exist at the moment
	                 * 
	                else if (pnEditor instanceof IFNetEditorComponent)	
	                	wolfgang = new WolfgangIF();
	                */
	            } catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            wolfgang.setEditorComponent(pnEditor);
	            wolfgang.installShortcuts(pnEditor.getGraphComponent());            
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
        private JLabel label = new JLabel();
        protected Component component;
        public boolean unsaved = false;

        public ButtonTabComponent(final String title, Component component) {
            //unset default FlowLayout' gaps
            super(new FlowLayout(FlowLayout.LEFT, 0, 0));
            if (title == null) {
                throw new NullPointerException("TabbedPane is null");
            }
            this.component = component;
            this.title = title;
            setOpaque(false);
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
            unsaved = true;
            if (!label.getText().endsWith("*")) {
                label.setText(title + "*");
            }
            repaint();
        }

        public void unsetModified() {
            unsaved = false;
            if (label.getText().endsWith("*")) {
                label.setText(label.getText().substring(0, label.getText().length() - 1));
            }
        }

        public String getName() {
            return title;
        }

        public class TabButton extends JButton implements ActionListener {

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
                int userChoice = JOptionPane.CANCEL_OPTION;
                if (i != -1) {

                }
                if (!checkClose()) {
                    return; //user aborted
                }
                clearLogParserOfCurrentComponent();
                SwatTabView.this.remove(i); //remove current
                
            }
            
            private void clearLogParserOfCurrentComponent(){
            	try {
					String name = Workbench.getInstance().getNameOfCurrentComponent();
					SwatComponentType type = Workbench.getInstance().getTypeOfCurrentComponent();
					switch (type) {
					case MXML_LOG:
						SwatComponents.getInstance().getContainerMXMLLogs().getComponent(name).clearLogParser();
						break;
					case ARISTAFLOW_LOG:
						SwatComponents.getInstance().getContainerAristaflowLogs().getComponent(name).clearLogParser();
						break;
					case XES_LOG:
						SwatComponents.getInstance().getContainerXESLogs().getComponent(name).clearLogParser();
						break;
					default:
						break;
					}
				} catch (Exception e) {
					Workbench.errorMessage("Could not remove component", e, true);
				}
            	
            	
            }

            //we don't want to update UI for this button
            public void updateUI() {
            }

            /**
             * ask user if unsaved changes. Returns false if aborted *
             */
            private boolean checkClose() {
                int userChoice = JOptionPane.CANCEL_OPTION;
                if (unsaved) { //test if this tab has unsaved changes
                    userChoice = JOptionPane.showConfirmDialog(SwatTabView.this, "Save unsaved changes for "
                            + ((PNEditorComponent) component).getNetContainer().getPetriNet().getName() + "?");
                    switch (userChoice) {
                        case JOptionPane.CANCEL_OPTION:
                            System.out.println("Aborting");
                            return false; //User wants to abort
                        case JOptionPane.YES_OPTION:
                            try {
                                String netName = ((PNEditorComponent) component).getNetContainer().getPetriNet().getName();
                                SwatComponents.getInstance().getContainerPetriNets().storeComponent(netName);
                            } catch (Exception ex) {
                                ExceptionDialog.showException(SwingUtilities.getWindowAncestor(SwatTabView.this), "Storage Exception", new Exception("Cannot store Petri net", ex), true, false);
                            }
                            break;
                        case JOptionPane.NO_OPTION:
                            PNEditorComponent editor = (PNEditorComponent) component;
                            while (editor.getUndoManager().canUndo()) {
                                editor.getUndoManager().undo();
                            }
                    }

                }
                return true; //user decided to either save or reject changes
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
        } else {
            ((ButtonTabComponent) getTabComponentAt(getSelectedIndex())).unsetModified();
        }

    }

    @Override
    public void componentAdded(Object component) throws ProjectComponentException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void componentRemoved(Object component) throws ProjectComponentException {
        if (component instanceof LogModel) {
            LogModel log = (LogModel) component;
            System.out.println("Removing " + log.getName());
            int indexToRemove = getIndexOf(log.getName());
            if (indexToRemove >= 0) {
                removeTabAt(getIndexOf(log.getName()));
            }
        } else if (component instanceof AbstractGraphicalPN) {
            AbstractGraphicalPN net = (AbstractGraphicalPN) component;
            int remove = getIndexOf(net.getPetriNet().getName());
            if (remove >= 0) {
                remove(remove);
            }
        }
    }

    @Override
    public void componentRenamed(Object component, String oldName, String newName) throws ProjectComponentException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void componentsChanged() {
    }

    public void unsetModified(int i) {
        ((ButtonTabComponent) getTabComponentAt(i)).unsetModified();

    }

}
