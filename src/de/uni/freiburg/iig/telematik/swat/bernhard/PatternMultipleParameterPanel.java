package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.lukas.OperandType;
import de.uni.freiburg.iig.telematik.swat.lukas.ParamValue;
/**
 * this class represents a parameterpanel where several
 * dropdownparameter panels can be added
 * @author bernhard
 *
 */
public abstract class PatternMultipleParameterPanel extends PatternParameterPanel {

	protected List<PatternParameterPanel> panelList;
	protected JButton addButton;
	protected String[] values;
	protected String description;
	public PatternMultipleParameterPanel(String name, String description, String pvalues[]) {
		super(name);
		values=pvalues;
		this.description=description;
		panelList=new ArrayList<PatternParameterPanel>();
		try {
			addButton=new JButton(IconFactory.getIcon("maximize"));
			addButton.setToolTipText("Add another "+description+". All "+description+"s must hold.");
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
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addParameter();
			}
		});
		content=new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		addParameter();
		// TODO Auto-generated constructor stub
	}

	protected PatternParameterPanel addParameter() {
		// TODO Auto-generated method stub
		PatternParameterPanel p=new PatternStatePredicateParameter(name,values);
		panelList.add(p);
		updateContent();
		return p;
	}
	
	protected void updateContent() {
		content.removeAll();
		for(int i=0; i < panelList.size(); i++) {
			JPanel panel=new JPanel(new FlowLayout(FlowLayout.LEFT));
			JButton removeButton=new JButton("-");
			try {
				removeButton = new JButton(IconFactory.getIcon("minimize"));
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
			removeButton.setToolTipText("Remove this "+description);
			final int index=i;
			removeButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					removeParameter(panelList.get(index));
				}
			});
			panel.add(panelList.get(i).getContent());
			// only add remove button when there are at least 2 panels
			if(panelList.size() > 1) {
				panel.add(removeButton);
			}
			// the last gets the add Button
			if(i == panelList.size()-1) {
				panel.add(addButton);
			}
			content.add(panel);
			
		}
		content.repaint();
		content.updateUI();
	}

	protected void removeParameter(PatternParameterPanel patternParameterPanel) {
		// TODO Auto-generated method stub
		content.remove(patternParameterPanel.getContent());
		panelList.remove(patternParameterPanel);
		updateContent();
	}

	@Override
	public List<ParamValue> getValue() {
		// TODO Auto-generated method stub
		ArrayList<ParamValue> values=new ArrayList<ParamValue>();
		for(PatternParameterPanel panel: panelList) {
			values.addAll(panel.getValue());
		}
		return values;
	}

	@Override
	public void setValue(List<ParamValue> value) {
		// TODO Auto-generated method stub
		panelList.clear();
		content.removeAll();
		for(ParamValue val:value) {
			PatternParameterPanel p=addParameter();
			ArrayList<ParamValue> list=new ArrayList<ParamValue>();
			list.add(val);
			p.setValue(list);
		}
		updateContent();
	}

}
