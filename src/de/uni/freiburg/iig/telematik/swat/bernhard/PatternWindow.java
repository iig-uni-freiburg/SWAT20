package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.BoxLayout;
import com.itextpdf.text.log.SysoCounter;
import com.mxgraph.swing.handler.mxCellMarker;

import de.invation.code.toval.graphic.util.SpringUtilities;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.editor.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.lukas.PatternFactory;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.MessageDialog;

/**
 * This class represents the pattern wizard with a button to add a pattern and
 * the possibility to choose the parameters
 * 
 * @author bernhard
 * 
 */
public class PatternWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7132881901662491907L;
	public final static int maxPatterns = 10;
	private JButton plusButton, okButton, removeAllButton;
	private JPanel patternPanel;
	private List<PatternSettingPanel> patternPanelList;
	private AnalyzePanel analyzePanel;
	private PatternFactory patternFactory;

	/**
	 * init the GUI
	 */
	private void initGui() {
		BorderLayout bl = new BorderLayout();
		Container c = getContentPane();
		this.setLayout(bl);

		okButton = new JButton(" OK ");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				clickedOK();
			}
		});
		removeAllButton=new JButton("Remove All");
		removeAllButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				removeAllPatterns();
			}
		});
		JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		south.add(okButton);
		patternPanelList = new ArrayList<PatternSettingPanel>();
		patternPanel = new JPanel();
		patternPanel.setLayout(new BoxLayout(patternPanel, BoxLayout.Y_AXIS));
		
		//patternPanel=new JPanel();
		//patternPanel.setLayout(new BoxLayout(patternPanel, BoxLayout.Y_AXIS ));
		JPanel northPanel=new JPanel(new BorderLayout());
		northPanel.add(patternPanel, BorderLayout.NORTH);
		JScrollPane jsp = new JScrollPane(northPanel);
		jsp.setVisible(true);
		// get applicable Patterns from Factory
		final List<String> itemNames = patternFactory.getApplicablePatterns();
		plusButton = new JButton("Add Pattern");
		// create the dropdown menu
		plusButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				JPopupMenu popup = new JPopupMenu();

				for (String itemName : itemNames) {

					JMenuItem item = new JMenuItem(itemName);
					//String description="Beschreibung";
					String description=patternFactory.getDescOfPattern(itemName);
					item.setToolTipText(description);
					final String name = itemName;
					item.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent arg0) {
							// System.out.println("Create New Row");
							try {
								addPanelforPatternClick(name);
							} catch (ParameterException e) {
								// TODO Auto-generated catch block
						
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
					popup.add(item);
				}
				popup.show(plusButton, plusButton.getWidth() * 4 / 5,
						plusButton.getHeight() * 4 / 5);
			}
		});
		JPanel buttonPanel=new JPanel(new FlowLayout(FlowLayout.LEFT));
		buttonPanel.add(plusButton);
		buttonPanel.add(removeAllButton);
		c.add(buttonPanel, BorderLayout.NORTH);
		c.add(jsp, BorderLayout.CENTER);
		c.add(south, BorderLayout.SOUTH);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(900, 600);
	}

	protected void removeAllPatterns() {
		// TODO Auto-generated method stub
		patternPanel.removeAll();
		patternPanelList.clear();
		patternPanel.repaint();
		patternPanel.updateUI();
	}

	/**
	 * make the window invisible and update the analyze panel
	 */
	protected void clickedOK() {
		// TODO Auto-generated method stub
		setVisible(false);
		analyzePanel.getPatternSettingsFromPatternWindow();
	}

	public PatternWindow(AnalyzePanel p, PatternFactory pf)
			throws HeadlessException {
		super();
		analyzePanel = p;
		patternFactory = pf;
		initGui();
	}

	/**
	 * try to add a new pattern
	 * @param name Name of the pattern
	 * @throws ParameterException
	 * @throws PropertyException
	 * @throws IOException
	 */
	private void addPanelforPatternClick(String name) {
		if (patternPanelList.size() < 10) {
			addPanelforPattern(name);
		} else {
			JOptionPane.showMessageDialog(this,
					"Cannot check more than 10 patterns", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	

	/**
	 * invoked when a net is changed
	 * just update the lists of transitions and data types
	 * then, same as save and load ;)
	 */
	public void netChanged() {
		List<PatternSetting> psList=getPatternSettings();
		setPatternSettings(psList);
	}
	
	public InformationReader getNetInformations() {
		return analyzePanel.getInformationReader();
	}

	/**
	 * add a Pattern Panel for pattern p and add it to the internal list
	 * 
	 * @param p
	 *            Name of the Pattern to add
	 */
	private void addPanelforPattern(String p) {
		PatternSettingPanel pSPanel = new PatternSettingPanel(p, this,
				patternFactory);
		// pSPanel.addCounterExample(new CounterExample(), this);
		patternPanel.add(pSPanel.getJPanel());
		patternPanelList.add(pSPanel);
		patternPanel.updateUI();
	}

	/**
	 * add a new patternsetting panel with the given settings
	 * 
	 * @param ps
	 */
	private void addPanelforPattern(PatternSetting ps) {
		PatternSettingPanel pSPanel = new PatternSettingPanel(ps, this,
				patternFactory);
		patternPanel.add(pSPanel.getJPanel());
		patternPanelList.add(pSPanel);
		patternPanel.updateUI();
	}

	public List<PatternSetting> getPatternSettings() {
		List<PatternSetting> patternList = new ArrayList<PatternSetting>();
		for (PatternSettingPanel panel : patternPanelList) {
			panel.updatePatternSettingValues();
			patternList.add(panel.getPatternSetting());
			
		}
		return patternList;
	}
/**
 * used to load a saved List of PatternSettings
 * @param newList the list to load
 */
	public void setPatternSettings(List<PatternSetting> newList) {
		patternPanel.removeAll();
		patternPanelList.clear();

		for (PatternSetting ps : newList) {
			addPanelforPattern(ps);
		}

	}

	/**
	 * remove a pattern panel from the window
	 * @param panel
	 */
	public void removePatternPanel(PatternSettingPanel panel) {
		patternPanelList.remove(panel);
		patternPanel.remove(panel.getJPanel());
		patternPanel.repaint();
		patternPanel.updateUI();
		
	}
}
