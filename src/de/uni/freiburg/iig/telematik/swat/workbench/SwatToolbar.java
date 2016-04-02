package de.uni.freiburg.iig.telematik.swat.workbench;

import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import de.invation.code.toval.graphic.component.DisplayFrame;
import de.invation.code.toval.graphic.dialog.FileNameDialog;
import de.invation.code.toval.graphic.dialog.MessageDialog;
import de.invation.code.toval.graphic.dialog.StringDialog;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ExceptionDialog;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalCPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalIFNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalTimedNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.TimedNet;
import de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.lola.LolaPresenter;
import de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.lola.LolaTransformator;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatState.OperatingMode;
import de.uni.freiburg.iig.telematik.swat.workbench.action.AFSqlLogImportAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.DeleteAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.EditPropertiesAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.ImportAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.LolaAnalyzeAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.PTImportAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.PopUpToolBarAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.RenameAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.SaveActiveComponentAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.SaveAllAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.ManageWorkingDirectoryAction;
import de.uni.freiburg.iig.telematik.swat.workbench.exception.SwatComponentException;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatStateListener;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;
import de.uni.freiburg.iig.telematik.wolfgang.menu.WrapLayout;

/**
 * Model for Buttons. Holds buttons like "open", "save", ... With
 * {@link #getButtonPanel()} the buttons are available inside a {@link JPanel}.
 * Each button can be accesed through get(enum)
 *
 * @author richard
 *
 */
public class SwatToolbar extends JToolBar implements ActionListener, SwatStateListener {

    private static final long serialVersionUID = -4279345402764581310L;

    private static final Color TOOLBAR_BG_COLOR = new Color(185, 185, 185);
    private static final Color TOOLBAR_BORDER_COLOR = Color.black;

    private static final String ACTION_COMMAND_EDIT_MODE = "editMode";
    private static final String ACTION_COMMAND_ANALYSIS_MODE = "analysisMode";
    private static final int ICON_SPACING = 5;

    private JRadioButton rdbtnEdit = null;
    private JRadioButton rdbtnAnalysis = null;


    private SwatTabView tabView = null;
    private SwatTreeView treeView = null;

    private List<JComponent> standardItems = new LinkedList<JComponent>();

    public SwatToolbar(SwatTabView tabView, SwatTreeView treeView) {
        this.tabView = tabView;
        this.treeView = treeView;

        setBackground(TOOLBAR_BG_COLOR);
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, TOOLBAR_BORDER_COLOR));
        setFloatable(false);
        setRollover(true);

        setLayout(new WrapLayout(3));


        try {
			createButtons();
		} catch (ParameterException | PropertyException | IOException e1) {
			Workbench.errorMessage("Could not load Toolbar-Icons", e1, true);
		}
       
        addDefaultItems();

        try {
            SwatState.getInstance().addListener(this);
        } catch (ParameterException e) {
            // Cannot happen, since this is never null.
        	Workbench.errorMessage("Could not register SwatToolbar", e, true);
        }

        // try to get ICONSize
        try {
            //ICON_SIZE = SwatProperties.getInstance().getIconSize().getSize();
        } catch (Exception e) {
            // Cannot read property. Ignore and stay with default value (32)
        }

    }

    /**add default items to toolbar**/
    private void addDefaultItems() {
        for (JComponent button : standardItems) {
            add(button);
            button.setFocusable(false);
        }
        addSeparator();
    }

    /**
     * Put default buttons into linkedList {@link #standardItems} for later use
     *
     * @throws IOException
     * @throws PropertyException
     * @throws ParameterException
	 *
     */
    private void createButtons() throws ParameterException, PropertyException, IOException {
        standardItems.add(new JButton(new SaveActiveComponentAction()));
        standardItems.add(new SwatToolbarButton(ToolbarButtonType.SAVE_ALL));
        standardItems.add(new JButton(new DeleteAction()));
        standardItems.add(new JButton(new ManageWorkingDirectoryAction()));
        standardItems.add(getNewNetButton());
		//standardItems.add(new JButton(new ImportAction()));
        //standardItems.add(getImportButon());
        //standardItems.add(new SwatToolbarButton(ToolbarButtonType.AF_TEMPLATE));
        //		standardItems.add(new JButton(new AFtemplateImportAction()));
        //		standardItems.add(new JButton(new LogImportAction()));
        standardItems.add(new JButton(new ImportAction()));
        standardItems.add(new SwatToolbarButton(ToolbarButtonType.RENAME));
        standardItems.add(new SwatToolbarButton(ToolbarButtonType.EDIT_PROPERTY));
		//standardItems.add(getLolaButton());
        //standardItems.add(getAristaFlowButton());
        //standardItems.add(new JButton(new SimulateTimeAction()));
        //standardItems.add(new JButton(new SimulateInstanceAwareAction()));
        //standardItems.add(new SwatToolbarButton(ToolbarButtonType.PRISM));

        ButtonGroup group = new ButtonGroup();
        group.add(getAnalysisRadioButton());
        group.add(getEditRadioButton());
        getEditRadioButton().setSelected(true);
        //removeBorder();

        standardItems.add(getAnalysisRadioButton());
        standardItems.add(getEditRadioButton());
    }

    private void removeBorder() {
        Border emptyBorder = BorderFactory.createEmptyBorder();
        for (JComponent button : standardItems) {
            button.setBorder(emptyBorder);
        }
    }

    private JButton getLolaButton() throws ParameterException, PropertyException, IOException {
        JButton lola = new SwatToolbarButton(ToolbarButtonType.DETECTIVE);
        return lola;
    }

    private JButton getAristaFlowButton() throws ParameterException, PropertyException, IOException {
        JButton aristaFlow = new JButton(new AFSqlLogImportAction());
        return aristaFlow;
    }

    private JRadioButton getAnalysisRadioButton() {
        if (rdbtnAnalysis == null) {
            rdbtnAnalysis = new JRadioButton("Analyse");
            rdbtnAnalysis.setMnemonic(KeyEvent.VK_A);
            rdbtnAnalysis.setActionCommand(ACTION_COMMAND_ANALYSIS_MODE);
            rdbtnAnalysis.addActionListener(this);
        }
        return rdbtnAnalysis;
    }

    private JRadioButton getEditRadioButton() {
        if (rdbtnEdit == null) {
            rdbtnEdit = new JRadioButton("Edit");
            rdbtnEdit.setMnemonic(KeyEvent.VK_E);
            rdbtnEdit.setActionCommand(ACTION_COMMAND_EDIT_MODE);
            rdbtnEdit.addActionListener(this);
        }
        return rdbtnEdit;
    }

	//	private JButton getSwitchworkingDirectoryButton() throws ParameterException, PropertyException, IOException{
    //		if (openButton == null)
    //			openButton = new SwatToolbarButton(ToolbarButtonType.SWITCH_DIRECTORY);
    //		return openButton;
    //	}
    private JButton getNewNetButton() throws ParameterException, PropertyException, IOException {

        JButton newButton = new SwatToolbarButton(ToolbarButtonType.NEW);
        return newButton;
    }

    private String requestFileName(String message, String title) {
        return FileNameDialog.showDialog(SwingUtilities.getWindowAncestor(getParent()), message, title, false);
    }

    private File getAbsolutePathToWorkingDir(String name) throws PropertyException, ParameterException, IOException {
        File file = new File(SwatProperties.getInstance().getWorkingDirectory(), name);
        if (file.exists()) {
            throw new ParameterException("File already exists");
        }
        //TODO: Validate, test if SWATComponent already contains net with same name... etc?
        return file;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
        	
            if (e.getActionCommand().equals(ACTION_COMMAND_ANALYSIS_MODE)) {
					if(Workbench.getInstance().getCurrentComponentNeedsParsing() && Workbench.getInstance().getCurrentComponentsSourceSizeTooBig()){
                                                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), "Entering analysis Mode requires parsing first");	
                                                SwatState.getInstance().setOperatingMode(SwatToolbar.this, OperatingMode.EDIT_MODE);
                                                getEditRadioButton().setSelected(true);
					} else {
						SwatState.getInstance().setOperatingMode(SwatToolbar.this, OperatingMode.ANALYSIS_MODE);
                                        }
            } else if (e.getActionCommand().equals(ACTION_COMMAND_EDIT_MODE)) {
                SwatState.getInstance().setOperatingMode(SwatToolbar.this, OperatingMode.EDIT_MODE);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void operatingModeChanged() {
        switch (SwatState.getInstance().getOperatingMode()) {
            case ANALYSIS_MODE:
                getAnalysisRadioButton().setSelected(true);
                break;
            case EDIT_MODE:
                getEditRadioButton().setSelected(true);
                break;
        }
        repaint();
    }

	//	public void addOpenActionListener(ActionListener listener) throws ParameterException, PropertyException, IOException {
    //		getSwitchworkingDirectoryButton().addActionListener(listener);
    //	}
    /**
     * reset Toolbar, restore standard components *
     */
    public void clear() {
        this.removeAll();
        //createButtons();
        addDefaultItems();
    }

    private class SwatToolbarButton extends JButton {

        private static final long serialVersionUID = 9184814296174960480L;
        private static final String iconNameFormat = "../resources/icons/%s/%s-%s.png";

        public SwatToolbarButton(ToolbarButtonType type) throws ParameterException, PropertyException, IOException {
            super(IconFactory.getIcon(type.toString().toLowerCase()));
            setBorder(BorderFactory.createEmptyBorder(0, ICON_SPACING, 0, ICON_SPACING));
            setBorderPainted(false);
            setRollover(true);
            setFocusable(false);
			//setContentAreaFilled(false);
            //setVerticalAlignment(CENTER);
            tryToSetPressedButton(type);

            switch (type) {
                case IMPORT:
                    setToolTipText("Import PT-Net from filesystem");
                    addActionListener(new PTImportAction());
                    break;
                case NEW:
                    SwatNewNetToolbar newNetTB = new SwatNewNetToolbar(tabView, treeView);
                    PopUpToolBarAction newNetAction = new PopUpToolBarAction("new net", "new", newNetTB);
                    newNetAction.setButton(this);
                    addActionListener(newNetAction);
                    break;
                case OPEN:
                    break;
                case SAVE_ALL:
                    addActionListener(new SaveAllAction());
                    break;
                case NEW_CPN:
                    setToolTipText("Create new CPnet");
                    addActionListener(new createNewAction(type));
                    break;
                case NEW_PT:
                    setToolTipText("Create new PTnet");
                    addActionListener(new createNewAction(type));
                    break;
                case NEW_IF:
                    setToolTipText("Create new IFnet");
                    addActionListener(new createNewAction(type));
                    break;
                case RENAME:
                    setToolTipText("Rename current net");
                    addActionListener(new RenameAction());
                    break;
                case EDIT_PROPERTY:
                    setToolTipText("Edit SWAT Properties");
                    addActionListener(new EditPropertiesAction());
                    break;
                case DETECTIVE:
                    setToolTipText("Convert to Lola");
                    setHorizontalTextPosition(SwingConstants.CENTER);
                    setVerticalTextPosition(SwingConstants.BOTTOM);
                    setIconTextGap(0);
                    setText("LOLA");
                    //addActionListener(new LolaTransformAction());
                    addActionListener(new LolaAnalyzeAction());
                    break;
            }
        }

        private void tryToSetPressedButton(ToolbarButtonType type) {
                try {
					setPressedIcon(IconFactory.getIconPressed(type.toString().toLowerCase()));
				} catch (ParameterException | PropertyException | IOException e) {
					//Workbench.errorMessage("Could not load icon for pressed button", e, false);
				}
                //setOpaque(false);

        }

    }

    private enum ToolbarButtonType {

        NEW, SAVE, SAVE_ALL, OPEN, IMPORT, SWITCH_DIRECTORY, NEW_PT, NEW_CPN, NEW_IF, NEW_RTPN, RENAME, DETECTIVE, ARISTAFLOW, PRISM, DELETE, AF_TEMPLATE, TIME, EDIT_PROPERTY;
    }

    class createNewAction implements ActionListener {

        private ToolbarButtonType type;

        public createNewAction(SwatToolbar.ToolbarButtonType type) {
            this.type = type;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            String netName = requestFileName("Please choose a name for the new net:", "New Petri-Net");
            if (netName != null) {
				//IFNet newNet = new IFNet();
                //AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> newNet = null;
                try {
					// Test new file name
                    //File file = getAbsolutePathToWorkingDir(netName);
                    AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> net = null;
                    switch (type) {
                        case NEW_CPN:
                            net = new GraphicalCPN();
                            break;
                        case NEW_PT:
                            net = new GraphicalPTNet();
                            break;
                        case NEW_IF:
                            net = new GraphicalIFNet(new IFNet());
                            //newNet = new GraphicalIFNet();
                            break;
                        case NEW_RTPN:
                        	net = new GraphicalTimedNet(new TimedNet());
                        default:
                            break;
                    }

                    net.getPetriNet().setName(netName);
                    SwatComponents.getInstance().getContainerPetriNets().addComponent(net);

                } catch (Exception e2) {
                    ExceptionDialog.showException(SwingUtilities.getWindowAncestor(SwatToolbar.this), "Storage Exception", new Exception("Could not add Petri net"), true, false);
                }
            }
        }

        private String requestFileName(String message, String title) {
            return FileNameDialog.showDialog(SwingUtilities.getWindowAncestor(SwatToolbar.this.getParent()), message, title, false);
        }
    }

    class LolaTransformAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent arg0) {
            try {
                PNEditorComponent editor = (PNEditorComponent) tabView.getSelectedComponent();
                LolaTransformator lola = new LolaTransformator(editor);
                LolaPresenter presenter = new LolaPresenter(lola.getNetAsLolaFormat());
                presenter.show();
                System.out.println("Generating runtime...");
                System.out.println("Starting Lola...");
                Process p = Runtime.getRuntime().exec("lola-boundednet");
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                System.out.println("Analyzing net...");
                writer.write(lola.getNetAsLolaFormat());
                writer.flush();
                writer.close();
                String result;
                System.out.println("Getting results");
                StringBuilder b = new StringBuilder();
                while ((result = reader.readLine()) != null) {
                    b.append(result);
                    System.out.println(result);
                    b.append("\r\n");
                }
                //System.out.println("result from lola: " + b.toString());
                LolaPresenter outcome = new LolaPresenter(b.toString());
                outcome.show();

            } catch (Exception e) {
                System.out.println("Something went wrong");
                e.printStackTrace();

            }

        }

    }
    
//    public void setEditMode(boolean editMode){
//            getAnalysisRadioButton().setSelected(!editMode);
//            getEditRadioButton().setSelected(editMode);
//            repaint();
//            SwatState.getInstance().setOperatingMode(this, editMode?OperatingMode.EDIT_MODE:OperatingMode.ANALYSIS_MODE);
//    }


    public static void main(String[] args) throws Exception {
        JPanel panel = new JPanel();
        panel.add(new SwatToolbar(SwatTabView.getInstance(), SwatTreeView.getInstance()));
        new DisplayFrame(panel, true);
    }
}
