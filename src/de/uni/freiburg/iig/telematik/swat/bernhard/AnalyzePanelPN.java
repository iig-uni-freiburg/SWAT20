package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.ACModel;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.logs.LogModel;
import de.uni.freiburg.iig.telematik.swat.lukas.modelchecker.adapter.PRISMPatternResult;
import de.uni.freiburg.iig.telematik.swat.lukas.modelchecker.adapter.PatternResult;
import de.uni.freiburg.iig.telematik.swat.lukas.modelchecker.adapter.prism.PrismAdapter;
import de.uni.freiburg.iig.telematik.swat.lukas.modelchecker.adapter.prism.PrismResult;
import de.uni.freiburg.iig.telematik.swat.lukas.operands.PatternParameter;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.DataflowAntiPattern;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.factory.GuiParamType;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.factory.GuiParamValue;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.factory.GuiParameter;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.TimeContext;
import de.uni.freiburg.iig.telematik.swat.workbench.Analysis;
import de.uni.freiburg.iig.telematik.swat.workbench.WorkbenchComponent;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.MessageDialog;
/**
 * This class implements the functionality for analyzing a PTNet.
 * @author bernhard
 *
 */
public class AnalyzePanelPN extends AnalyzePanel {

	private PNEditor pnEditor;
	protected AnalyzeToolBar toolBar;
	public AnalyzePanelPN(WorkbenchComponent component, String net) {
		super(component, net);
		pnEditor=(PNEditor) component;
		objectInformationReader=new PetriNetInformation(pnEditor);
		toolBar = new AnalyzeToolBar((PNEditor) component);
		objectChanged();
		update();
		// TODO Auto-generated constructor stub
	}
	/**
	 * return the AnalyzeToolBar
	 * @return the ToolBar
	 */
	public AnalyzeToolBar getToolBar() {
		return toolBar;
	}
	/**
	 * load the given Path in the AnalyzeToolBar
	 * @param path the Counterexample to be visualized
	 */
	private void showCounterExample(List<String> path) {
		toolBar.setCounterExample(path);
	}
	/**
	 * Start the analysis. Reset the toolbar, do some checks
	 * and then make the analysis logic start PRISM.
	 */
	protected void analyze() {
		
		toolBar.reset();
		if(!pnEditor.getNetContainer().getPetriNet().isCapacityBounded()) {
			JOptionPane.showMessageDialog(null,
					"Petri Net is not bounded!", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		PrismAdapter prismExecuter = new PrismAdapter(pnEditor
				.getNetContainer().getPetriNet());
		// build list of patterns
		HashMap<PatternSetting, ArrayList<CompliancePattern>> resultMap = 
				new HashMap<PatternSetting, ArrayList<CompliancePattern>>();

		MessageDialog.getInstance().addMessage(
				"Giving " + patternSettings.size() + " Patterns to PRISM");
		ArrayList<CompliancePattern> compliancePatterns = new ArrayList<CompliancePattern>();
		
		for (PatternSetting setting : patternSettings) {
			setting.reset();
			ArrayList<CompliancePattern> cPs = patternFactory
					.createPattern(setting.getName(),
							(ArrayList<GuiParameter>) setting.getParameters());
			compliancePatterns.addAll(cPs);
			// store the setting and the pattern in dictionary
			resultMap.put(setting, cPs);
		}
		
		// analyze and set the results
		PrismResult prismResult = prismExecuter.analyze(compliancePatterns);
		for (PatternSetting setting : patternSettings) {
			
			ArrayList<CompliancePattern> patterns = resultMap.get(setting);
			
			for (CompliancePattern pattern : patterns) {
				PRISMPatternResult patternResult = prismResult.getPatternResult(pattern);
				setting.setResult(patternResult);
			}
			
		}
		update();
		
	}
	
    protected void showDetailsButton(InformationFlowPatternSetting p, JPanel newPanel) {
		
		final ArrayList<PatternResult> resutls = p.getResults();
		final InformationFlowPatternSetting patternSetting = p;
		JButton detailsButton = new JButton("Details");
		detailsButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				 
		        showDetailsDialog(resutls, patternSetting);
			}

			private void showDetailsDialog(ArrayList<PatternResult> resutls, 
					InformationFlowPatternSetting patternSetting) {
				
				JDialog dialog = new JDialog();
				JPanel content = new JPanel();
				content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
				content.setBorder(BorderFactory.createTitledBorder(patternSetting.getName()));
				dialog.setTitle("Analysis Details");
				dialog.setContentPane(content);
				dialog.setModal(true);
				
				for (final PatternResult res : resutls) {
					
					if (res.equals(patternSetting.getResult())) {
						continue;
					}
					
					final JPanel panel = new JPanel();
					panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
					final ArrayList<PatternParameter> operands = ((PRISMPatternResult) res).getPatternOperands();
					double prob = ((PRISMPatternResult) res).getProbability();
					String patternDesc = "Pattern:";
					ArrayList<String> operandNames = new ArrayList<String>();
					for (PatternParameter op : operands) {
						operandNames.add(op.getName());
					}
					Collections.sort(operandNames);
					for (int i=0; i<operandNames.size(); i++) {
						String op = operandNames.get(i);
						if (i == 0) {
							patternDesc += " " + op;
						} else {
							patternDesc += ", " + op;
						}
					}
					panel.add(new JLabel(patternDesc));
					panel.add(new JLabel("Prob: " + prob));
					panel.setBorder(BorderFactory.createEtchedBorder());
					panel.addMouseListener(new MouseAdapter() {
						
						GraphHighlighter mHighlighter = 
								new GraphHighlighter(mPNEditor, operands);
						
						@Override
				        public void mouseEntered(MouseEvent e) {
							panel.setBackground(Color.RED);
							mHighlighter.highlightOn();

				        }

						@Override
				        public void mouseExited(MouseEvent e) {
				        	panel.setBackground(UIManager.getColor ("Panel.background"));
				        	mHighlighter.highlightOff();
				        }
					});
					
					content.add(panel, BorderLayout.SOUTH);
					
				}
				
				dialog.pack();
				dialog.setVisible(true);
				
			}
		});
		
		newPanel.add(Helpers.jPanelLeft(detailsButton));
		
	}	

	
	@Override
	protected void addCounterExampleButton(PRISMPatternResult result, JPanel newPanel) {
		// TODO Auto-generated method stub
		final List<String> path = result.getViolatingPath();
		
		if (path != null) {
			JButton counterButton = new JButton("Counterexample");
			counterButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					showCounterExample(path);
				}
			});
			// counterButton.setPreferredSize(counterButton.getMaximumSize());
			// if (result.isFulfilled() == false) {
			newPanel.add(Helpers.jPanelLeft(counterButton));
		}
	}
		
	@Override
	protected String adjustValue(GuiParamValue val) {
		// TODO Auto-generated method stub
		if(val.getOperandType() == GuiParamType.ACTIVITY) {
			HashMap<String, String> transitionDicReverse=((PetriNetInformation) this.objectInformationReader).getTransitionToLabelDictionary();
			return transitionDicReverse.get(val.getOperandName());
		}
		return val.getOperandName();
	}
	
    @Override
	public void update() {
		
		propertyPanel.removeAll();
		
		for (PatternSetting p : patternSettings) {
			//System.out.println(p);
			// check if the setting was changed

			JPanel newPanel = new JPanel();
			newPanel.setLayout(new BoxLayout(newPanel, BoxLayout.Y_AXIS));
			List<JLabel> labels = getLabelListForPatternSetting(p);
			for (JLabel label : labels) {
				newPanel.add(Helpers.jPanelLeft(label));
			}
			JPanel resultPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			resultPanel.add(new JLabel("Result: "));
			// check whether a result exists
			PRISMPatternResult result = (PRISMPatternResult) p.getResult();
			if (result != null) {
				try {
					String patterName = p.getName();
					HashMap<String, String> names = DataflowAntiPattern.getPatternDescription();
					boolean antiPattern = names.containsKey(patterName);
					if ((!antiPattern && result.isFulfilled()) || (antiPattern && !result.isFulfilled())) {
						resultPanel.add(new JLabel(IconFactory
								.getIcon("result_valid")));
					} else {
						resultPanel.add(new JLabel(IconFactory
								.getIcon("result_false")));
					} 
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
				newPanel.add(resultPanel);
				DecimalFormat decimalFormat = new DecimalFormat("#.###");
				double prob = result.getProbability();
				newPanel.add(Helpers.jPanelLeft(new JLabel("\tProbability: "
						+ decimalFormat.format(prob))));
				addCounterExampleButton(result, newPanel);
				if (p instanceof InformationFlowPatternSetting && 
						((PRISMPatternResult) p.getResult()).getProbability() != 0) {
					showDetailsButton((InformationFlowPatternSetting) p,
							newPanel);
				}
				
			} else {
				try {
					resultPanel.add(new JLabel(IconFactory
							.getIcon("result_unknown")));
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
				newPanel.add(resultPanel);
			}
			newPanel.setBorder(BorderFactory.createTitledBorder(p.getName()));
			JPanel northPanel = new JPanel(new BorderLayout());
			northPanel.add(newPanel, BorderLayout.NORTH);
			propertyPanel.add(northPanel);
		}
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
		// TODO Auto-generated method stub

	}

	@Override
	public void componentsChanged() {
		// TODO Auto-generated method stub

	}

}
