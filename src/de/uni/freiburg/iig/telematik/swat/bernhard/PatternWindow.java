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
	private int patternId=0;
	private List<String> transactions;
	private List<String> data;
	private JButton plusButton;
	private JPanel patternPanel;
	private HashMap<Integer,PatternSettingPanel> patternPanelDic;
	private PNEditor pneditor = null;
	private PatternAnalyzeLogic analyzeLogic;
	// List<Pattern> activePatterns;
	public PatternWindow() throws ParameterException, PropertyException,
			IOException {

		super("Choose Patterns");
		analyzeLogic=new PatternAnalyzeLogic(this);
		transactions = Arrays.asList("A", "B", "C");
		data = Arrays.asList("red", "green", "blue");
		BorderLayout bl = new BorderLayout();
		Container c = getContentPane();
		this.setLayout(bl);

		JButton analyzeButton = new JButton("Analyze!");
		analyzeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				analyze();
			}
			});
		
		JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		south.add(analyzeButton);
		patternPanelDic=new HashMap<Integer,PatternSettingPanel>();
		/*
		 * JPanel test=new JPanel(new SpringLayout());
		 * //middle.add(patternDropDown); op1=new JLabel("Choose A:"); op2=new
		 * JLabel("Choose B:"); op3=new JLabel("Choose k:"); JComboBox
		 * transactionsA=new JComboBox(patternList); JComboBox transactionsB=new
		 * JComboBox(patternList); JTextField number=new JTextField(20);
		 * test.add(jpanelLeft(op1)); test.add(jpanelLeft(transactionsA));
		 * test.add(jpanelLeft(op2)); test.add(jpanelLeft(transactionsB));
		 * test.add(jpanelLeft(op3)); test.add(jpanelLeft(number));
		 */
		patternPanel = new JPanel(new GridLayout(maxPatterns, 1, 10, 10));
		JScrollPane jsp = new JScrollPane(patternPanel);
		final List<String> itemNames = PatternDatabase.getPatternList();

		plusButton = new JButton(IconFactory.getIcon("maximize"));
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
	
	

	

	protected void analyze() {
		AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?, ?, ?> pn=pneditor.getNetContainer();
		AbstractPetriNet apn=pn.getPetriNet();
		NetType netType = apn.getNetType();
		MessageDialog.getInstance().addMessage(netType.toString());
		// TODO Auto-generated method stub
		for(PatternSettingPanel pp:patternPanelDic.values()) {
			MessageDialog.getInstance().addMessage(pp.getPatternName());
			MessageDialog.getInstance().addMessage((pp.getValues()));
		}
		//highLightCounterExample(new CounterExample());
	}



	public void setVisible(PNEditor editor) {
		if(this.isVisible() == false) {
			setVisible(true);
			pneditor=editor;
		}
		//transactions = getTransactionList(editor);
	}

	

	public static void main(String args[]) throws ParameterException,
			PropertyException, IOException {
		try {
			UIManager
					.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
		new PatternWindow().setVisible(true);
	}
	
	private void addPanelforPatternClick(String name) throws ParameterException, PropertyException, IOException {
		if (patternPanelDic.size() < 10) {
			addPanelforPattern(name);
		} else {
				JOptionPane.showMessageDialog(this,
					    "Cannot check more than 10 patterns",
					    "Error",
					    JOptionPane.ERROR_MESSAGE);
		}
	}
	private void addPanelforPattern(String p) throws ParameterException, PropertyException, IOException {
		PatternSettingPanel pSPanel=new PatternSettingPanel(p, patternPanel,pneditor);
		pSPanel.addCounterExample(new CounterExample(), this);
		patternPanel.add(pSPanel.getJPanel());
		
		patternPanelDic.put(patternId, pSPanel);
		patternId++;
		patternPanel.updateUI();;
	}





	public void highLightCounterExample(CounterExample ce) {
		// TODO Auto-generated method stub
		analyzeLogic.highLightCounterExample(ce);
	}





	public PNEditor getPneditor() {
		return pneditor;
	}
}
