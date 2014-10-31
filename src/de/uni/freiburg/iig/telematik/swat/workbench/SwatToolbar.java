package de.uni.freiburg.iig.telematik.swat.workbench;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import de.invation.code.toval.graphic.component.DisplayFrame;
import de.invation.code.toval.graphic.dialog.FileNameDialog;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalCPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalIFNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.menu.WrapLayout;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.logs.LogModel;
import de.uni.freiburg.iig.telematik.swat.lola.LolaPresenter;
import de.uni.freiburg.iig.telematik.swat.lola.LolaTransformator;
import de.uni.freiburg.iig.telematik.swat.sciff.AristaFlowSQLConnector;
import de.uni.freiburg.iig.telematik.swat.sciff.DatabaseChooser;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatState.OperatingMode;
import de.uni.freiburg.iig.telematik.swat.workbench.action.AFtemplateImportAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.ImportAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.LolaAnalyzeAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.PopUpToolBarAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.RenameAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.SaveActiveComponentAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.SaveAllAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.SwitchWorkingDirectoryAction;
import de.uni.freiburg.iig.telematik.swat.workbench.action.TimeActionListener;
import de.uni.freiburg.iig.telematik.swat.workbench.exception.SwatComponentException;
import de.uni.freiburg.iig.telematik.swat.workbench.listener.SwatStateListener;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

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
	
	private static final String ACTION_COMMAND_EDIT_MODE = "editMode";
	private static final String ACTION_COMMAND_ANALYSIS_MODE = "analysisMode";
	private static int ICON_SIZE = 32;
	private static final int ICON_SPACING = 5;
	
	private JRadioButton rdbtnEdit = null;
	private JRadioButton rdbtnAnalysis = null;

	private JButton openButton = null;

	private SwatTabView tabView = null;
	private SwatTreeView treeView = null;

	private List<JButton> standardItems = new LinkedList<JButton>();

	public SwatToolbar(SwatTabView tabView, SwatTreeView treeView) {
		this.tabView = tabView;
		this.treeView = treeView;

		setFloatable(false);
		setRollover(true);

		//HACK!: To show PNEditor-Icons (they seem to be bigger) 
		//setPreferredSize(new Dimension(200, ICON_SIZE + 30));
		
		setLayout(new WrapLayout(3));

		try {
			createButtons();
		} catch (ParameterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (PropertyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		addStandardButtons();

		try {
			SwatState.getInstance().addListener(this);
		} catch (ParameterException e) {
			// Cannot happen, since this is never null.
		}

		// try to get ICONSize
		try {
			ICON_SIZE = SwatProperties.getInstance().getIconSize().getSize();
		} catch (Exception e) {
			// Cannot read property. Ignore and stay with default value (32)
		}

	}

	private void addStandardButtons() {
		for (JButton button : standardItems) {
			add(button);
			button.setFocusable(false);
		}
		addSeparator();
	}

	/**
	 * Putstart Buttons into linkedList {@link #standardItems} for later use
	 * 
	 * @throws IOException
	 * @throws PropertyException
	 * @throws ParameterException
	 **/
	private void createButtons() throws ParameterException, PropertyException, IOException {
		standardItems.add(new JButton(new SaveActiveComponentAction()));
		standardItems.add(new SwatToolbarButton(ToolbarButtonType.SAVE_ALL));
		standardItems.add(new SwatToolbarButton(ToolbarButtonType.DELETE));
		standardItems.add(getSwitchworkingDirectoryButton());
		standardItems.add(getNewNetButton());
		//standardItems.add(new JButton(new ImportAction()));
		standardItems.add(getImportButon());
		//standardItems.add(new SwatToolbarButton(ToolbarButtonType.AF_TEMPLATE));
		standardItems.add(new JButton(new AFtemplateImportAction()));
		standardItems.add(new SwatToolbarButton(ToolbarButtonType.RENAME));
		//standardItems.add(getLolaButton());
		standardItems.add(getAristaFlowButton());
		standardItems.add(new SwatToolbarButton(ToolbarButtonType.TIME));
		//standardItems.add(new SwatToolbarButton(ToolbarButtonType.PRISM));
		
//		ButtonGroup group = new ButtonGroup();
//		group.add(getAnalysisRadioButton());
//		group.add(getEditRadioButton());
//		getEditRadioButton().setSelected(true);
		removeBorder();
	}
	
	private void removeBorder() {
		Border emptyBorder = BorderFactory.createEmptyBorder();
		for (JButton button : standardItems) {
			button.setBorder(emptyBorder);
		}
	}

	private JButton getLolaButton() throws ParameterException, PropertyException, IOException {
		JButton lola = new SwatToolbarButton(ToolbarButtonType.DETECTIVE);
		return lola;
	}

	private JButton getImportButon() throws ParameterException, PropertyException, IOException {
		//JButton newButton = new SwatToolbarButton(ToolbarButtonType.IMPORT);
		JButton importButton = new JButton(new ImportAction());
		return importButton;
	}

	private JButton getAristaFlowButton() throws ParameterException, PropertyException, IOException {
		JButton aristaFlow = new SwatToolbarButton(ToolbarButtonType.ARISTAFLOW);
		return aristaFlow;
	}

//	private JRadioButton getAnalysisRadioButton(){
//		if(rdbtnAnalysis == null){
//			rdbtnAnalysis = new JRadioButton("Analyse");
//			rdbtnAnalysis.setMnemonic(KeyEvent.VK_A);
//			rdbtnAnalysis.setActionCommand(ACTION_COMMAND_ANALYSIS_MODE);
//			rdbtnAnalysis.addActionListener(this);
//		}
//		return rdbtnAnalysis;
//	}
	
//	private JRadioButton getEditRadioButton(){
//		if(rdbtnEdit == null){
//			rdbtnEdit = new JRadioButton("Edit");
//			rdbtnEdit.setMnemonic(KeyEvent.VK_E);
//			rdbtnEdit.setActionCommand(ACTION_COMMAND_EDIT_MODE);
//			rdbtnEdit.addActionListener(this);
//		}
//		return rdbtnEdit;
//	}
	
	private JButton getSwitchworkingDirectoryButton() throws ParameterException, PropertyException, IOException{
		if (openButton == null)
			openButton = new SwatToolbarButton(ToolbarButtonType.SWITCH_DIRECTORY);
		// newButton.addActionListener(new
		// OpenWorkingDirectoryAction(SwingUtilities.getWindowAncestor(this)));
		return openButton;
	}
	
	
	private JButton getNewNetButton() throws ParameterException, PropertyException, IOException{
		
		JButton newButton = new SwatToolbarButton(ToolbarButtonType.NEW);		
		//newButton.addActionListener(new createNewAction(ToolbarButtonType.NEW_PT));

		return newButton;
	}
	
	private String requestFileName(String message, String title){
		return new FileNameDialog(SwingUtilities.getWindowAncestor(getParent()), message, title, false).requestInput();
	}
	
	private File getAbsolutePathToWorkingDir(String name) throws PropertyException, ParameterException, IOException {
		File file = new File(SwatProperties.getInstance().getWorkingDirectory(), name);
		if (file.exists())
			throw new ParameterException("File already exists");
		//TODO: Validate, test if SWATComponent already contains net with same name... etc?
		return file;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getActionCommand().equals(ACTION_COMMAND_ANALYSIS_MODE)) {
				SwatState.getInstance().setOperatingMode(SwatToolbar.this, OperatingMode.ANALYSIS_MODE);
			} else if (e.getActionCommand().equals(ACTION_COMMAND_EDIT_MODE)) {
				SwatState.getInstance().setOperatingMode(SwatToolbar.this, OperatingMode.EDIT_MODE);
			}
		} catch (ParameterException ex) {
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		JPanel panel = new JPanel();
		panel.add(new SwatToolbar(SwatTabView.getInstance(), SwatTreeView.getInstance()));
		new DisplayFrame(panel, true);
	}

	@Override
	public void operatingModeChanged() {
		switch(SwatState.getInstance().getOperatingMode()){
		case ANALYSIS_MODE:
//			getAnalysisRadioButton().setSelected(true);
			break;
		case EDIT_MODE:
//			getEditRadioButton().setSelected(true);
			break;
		}
		repaint();
	}

	public void addOpenActionListener(ActionListener listener) throws ParameterException, PropertyException, IOException {
		getSwitchworkingDirectoryButton().addActionListener(listener);
	}

	/** reset Toolbar, restore standard components **/
	public void clear() {
		this.removeAll();
		//createButtons();
		addStandardButtons();
	}


	
	//	public class ImportAction implements ActionListener {
	//		@Override
	//		public void actionPerformed(ActionEvent arg0) {
	//			AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> net = PNParserDialog.showPetriNetDialog(SwingUtilities
	//					.getWindowAncestor(SwatToolbar.this));
	//			if (net == null)
	//				return;
	//			String fileName = requestFileName("Name for imported net?", "New name?");
	//			try {
	//				File file = getAbsolutePathToWorkingDir(fileName);
	//				SwatComponents.getInstance().putIntoSwatComponent(net, file);
	//				treeView.removeAndUpdateSwatComponents();
	//			} catch (PropertyException e) {
	//				// TODO Auto-generated catch block
	//				e.printStackTrace();
	//			} catch (ParameterException e) {
	//				// TODO Auto-generated catch block
	//				e.printStackTrace();
	//			} catch (IOException e) {
	//				// TODO Auto-generated catch block
	//				e.printStackTrace();
	//			}
	//
	//		}
	//	}

	private class SwatToolbarButton extends JButton{

		private static final long serialVersionUID = 9184814296174960480L;
		private static final String iconNameFormat = "../resources/icons/%s/%s-%s.png";
		
		public SwatToolbarButton(ToolbarButtonType type) throws ParameterException, PropertyException, IOException{
			super(IconFactory.getIcon(type.toString().toLowerCase()));
			setBorder(BorderFactory.createEmptyBorder(0, ICON_SPACING, 0, ICON_SPACING));
			setBorderPainted(false);
			setRollover(true);
			setFocusable(false);
			//setContentAreaFilled(false);
			//setVerticalAlignment(CENTER);
			tryToSetPressedButton(type);

			switch(type){
			case IMPORT:
				setToolTipText("Import PT-Net from filesystem");
				addActionListener(new ImportAction());
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
			case SWITCH_DIRECTORY:
				addActionListener(new SwitchWorkingDirectoryAction(treeView, tabView));
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
			case DETECTIVE:
				setToolTipText("Convert to Lola");
				setHorizontalTextPosition(SwingConstants.CENTER);
				setVerticalTextPosition(SwingConstants.BOTTOM);
				setIconTextGap(0);
				setText("LOLA");
				//addActionListener(new LolaTransformAction());
				addActionListener(new LolaAnalyzeAction());
				break;
			case ARISTAFLOW:
				setToolTipText("Analyze active AristaFlow instance");
				addActionListener(new AristaFlowAction());
				break;
			//			case PRISM:
			//				setToolTipText("Analyze with PRISM");
			//				addActionListener(new PrismAnalyzeAction(tabView));
			//				break;
			case DELETE:
				setToolTipText("Remove from Workbench");
				addActionListener(new DeleteAction());
				//addKeyListener(new DeleteAction());
				setMnemonic(KeyEvent.VK_DELETE);
				//set
				break;
			case TIME:
				setToolTipText("Simulate Timing");
				//addActionListener(new TimeActionListener());
				addActionListener(new TimeActionListener());
			}
		}
		
		private void tryToSetPressedButton(ToolbarButtonType type) {
			try {
				setPressedIcon(IconFactory.getIconPressed(type.toString().toLowerCase()));
				//setOpaque(false);
			} catch (ParameterException e) {
			} catch (PropertyException e) {
			} catch (IOException e) {
			}

		}

	}
	
	private enum ToolbarButtonType {
		NEW, SAVE, SAVE_ALL, OPEN, IMPORT, SWITCH_DIRECTORY, NEW_PT, NEW_CPN, NEW_IF, RENAME, DETECTIVE, ARISTAFLOW, PRISM, DELETE, AF_TEMPLATE, TIME;
	}
	
	class AristaFlowAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				AristaFlowSQLConnector connector = DatabaseChooser.DatabaseChooser();
				//LogFileViewer viewer = con.dumpIntoWorkbench();
				LogModel model = connector.getModel();
				model = SwatComponents.getInstance().storeLogModelTo(model, model.getName());
				SwatComponents.getInstance().addLogModel(model);
				//SwatComponents.getInstance().reload();
				//connector.parse();
				//SciffAnalyzeAction sciffAction = new SciffAnalyzeAction(connector.getTempFile());
				//sciffAction.actionPerformed(e);
			} catch (SQLException e1) {
				e1.printStackTrace();
			} catch (ClassNotFoundException e2) {
				e2.printStackTrace();
			} catch (IOException e3) {
				e3.printStackTrace();
			} catch (Exception e1) {
				e1.printStackTrace();
			}


		}

	}

	class DeleteAction implements ActionListener, KeyListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			File file = null;
				SwatTreeNode selectedNode = (SwatTreeNode) treeView.getSelectionPath().getLastPathComponent();
			try {
				switch (selectedNode.getObjectType()) {
				case PETRI_NET:
					PNEditor editor = (PNEditor) selectedNode.getUserObject();
					SwatComponents.getInstance().removePetriNet(editor.getNetContainer().getPetriNet().getName(), true);
					file=SwatComponents.getInstance().getPetriNetFile(((AbstractGraphicalPN) selectedNode.getUserObject()).getPetriNet().getName());
					break;
				case PETRI_NET_ANALYSIS:
					break;
				default:
					//file=((SwatComponent)selectedNode.getUserObject()).getFile();
					//file = SwatComponents.getInstance().getFile((XESLogModel) selectedNode.getUserObject());
					break;
				}
			} catch (SwatComponentException e) {
				Workbench.getInstance().errorMessage("Could not delete " + selectedNode.getDisplayName() + ". \nReason: " + e.getMessage());

			}

//				boolean deleted = FileHelper.removeLinkOnly(file);
//				if (deleted) {
//					try {
//						//tabView.remove(tabView.getSelectedIndex());
//						tabView.remove(selectedNode);
////						for (int i = 0; i < tabView.getTabCount(); i++) {
////							if (tabView.getTabComponentAt(i) == selectedNode.getUserObject()) {
////								tabView.remove(i);
////							}
//						//						}
//					} catch (java.lang.IndexOutOfBoundsException e) {
//						//Tab wasn't open
//					}
//					SwatComponents.getInstance().remove(file);
//				}
//			} catch (ArrayIndexOutOfBoundsException e) {
//
//			}


		}

		@Override
		public void keyPressed(KeyEvent arg0) {
			int code=arg0.getKeyCode();
			if (code == KeyEvent.VK_DELETE) {
				actionPerformed(null);
			}

		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub

		}

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
					AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?, ?, ?> net = null;
					switch (type) {
					case NEW_CPN:
						net = new GraphicalCPN();
						break;
					case NEW_PT:
						net = new GraphicalPTNet();
						break;
					case NEW_IF:
						net = new GraphicalIFNet();
						//newNet = new GraphicalIFNet();
						break;
					default:
						break;
					}

					net.getPetriNet().setName(netName);
					SwatComponents.getInstance().addPetriNet(net);

				} catch (ParameterException e1) {
					JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(SwatToolbar.this), e1.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				} catch (SwatComponentException e2) {
					JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(SwatToolbar.this),
							"Could not add net.\nReason: " + e2.getMessage());
			}
		}
	}
	
	private String requestFileName(String message, String title){
			return new FileNameDialog(SwingUtilities.getWindowAncestor(SwatToolbar.this.getParent()), message, title, false)
					.requestInput();
	}

		private File getAbsolutePathToWorkingDir(String name) throws PropertyException, ParameterException, IOException {
			File file = new File(SwatProperties.getInstance().getWorkingDirectory(), name + ".pnml");
			if (file.exists())
				throw new ParameterException("File already exists");
			//TODO: Validate, test if SWATComponent already contains net with same name... etc?
			return file;
	}
}

	class LolaTransformAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
			PNEditor editor = (PNEditor) tabView.getSelectedComponent();
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
				//				System.out.println("result from lola: " + b.toString());
								LolaPresenter outcome = new LolaPresenter(b.toString());
								outcome.show();

			} catch (Exception e) {
				System.out.println("Something went wrong");
				e.printStackTrace();

			}

		}

	}

}
