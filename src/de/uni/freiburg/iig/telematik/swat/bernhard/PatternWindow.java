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
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.MessageDialog;

public class PatternWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7132881901662491907L;
	public final static int maxPatterns = 10;
	private HashMap<String, String> transitionDic;
	private List<String> dataList;
	private JButton plusButton, okButton;
	private JPanel patternPanel;
	private List<PatternSettingPanel> patternPanelList;
	private PNEditor pneditor = null;
	private AnalyzePanel analyzePanel;
	// List<Pattern> activePatterns;
	private void initGui() {
		BorderLayout bl = new BorderLayout();
		Container c = getContentPane();
		this.setLayout(bl);

		okButton = new JButton(" OK ");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				updatePanel();
			}
			});
		
		JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		south.add(okButton);
		patternPanelList=new ArrayList<PatternSettingPanel>();
		patternPanel = new JPanel(new GridLayout(PatternAnalyzeLogic.MAX_PATTERNS, 1, 10, 10));
		JScrollPane jsp = new JScrollPane(patternPanel);
		final List<String> itemNames = PatternDatabase.getInstance().getPatternList();

		try {
			plusButton = new JButton(IconFactory.getIcon("maximize"));
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
		plusButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				JPopupMenu popup = new JPopupMenu();

				for (String itemName : itemNames) {

					JMenuItem item = new JMenuItem(itemName);
					final String name=itemName;
					item.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent arg0) {
							//System.out.println("Create New Row");
							try {
								addPanelforPatternClick(name);
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
					});
					popup.add(item);
				}
				popup.show(plusButton, plusButton.getWidth() * 4 / 5,
						plusButton.getHeight() * 4 / 5);
			}
		});
		//this.addPanelforPattern("Q Precedes P");
		c.add(Helpers.jPanelLeft(plusButton), BorderLayout.NORTH);
		// SpringUtilities.makeGrid(test,3,2,0,0,0,0);
		// middle.add(test);
		c.add(jsp, BorderLayout.CENTER);
		c.add(south, BorderLayout.SOUTH);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(800, 600);
	}
	
	protected void updatePanel() {
		// TODO Auto-generated method stub
		setVisible(false);
		analyzePanel.update();
	}

	public PatternWindow(AnalyzePanel p, HashMap<String, String> transactionDic, List<String> data)
			throws HeadlessException {
		super();
		this.transitionDic = transactionDic;
		this.dataList = data;
		analyzePanel=p;
		initGui();
	}
	protected void analyze() {
		AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?, ?, ?> pn=pneditor.getNetContainer();
		AbstractPetriNet apn=pn.getPetriNet();
		NetType netType = apn.getNetType();
		MessageDialog.getInstance().addMessage(netType.toString());
		// TODO Auto-generated method stub
		for(PatternSettingPanel pp:patternPanelList) {
			MessageDialog.getInstance().addMessage(pp.getPatternName());
			MessageDialog.getInstance().addMessage((pp.getValues()));
		}
		//highLightCounterExample(new CounterExample());
	}
	
	private void addPanelforPatternClick(String name) throws ParameterException, PropertyException, IOException {
		if (patternPanelList.size() < 10) {
			addPanelforPattern(name);
		} else {
				JOptionPane.showMessageDialog(this,
					    "Cannot check more than 10 patterns",
					    "Error",
					    JOptionPane.ERROR_MESSAGE);
		}
	}
	private void addPanelforPattern(String p) throws ParameterException, PropertyException, IOException {
		PatternSettingPanel pSPanel=new PatternSettingPanel(p, this, transitionDic, dataList);
		//pSPanel.addCounterExample(new CounterExample(), this);
		patternPanel.add(pSPanel.getJPanel());
		
		patternPanelList.add(pSPanel);
		patternPanel.updateUI();;
	}

	public PNEditor getPneditor() {
		return pneditor;
	}
	
	public List<Pattern> getPatterns() {
		List<Pattern> patternList=new ArrayList<Pattern>();
		for(PatternSettingPanel panel : patternPanelList) {
			panel.updatePatternValues();
			patternList.add(panel.getPattern());
		}
		return patternList;
	}
	
	public void removePatternPanel(PatternSettingPanel panel) {
		patternPanel.remove(panel.getJPanel());
		patternPanel.repaint();
		patternPanel.updateUI();
		patternPanelList.remove(panel);
	}
}
