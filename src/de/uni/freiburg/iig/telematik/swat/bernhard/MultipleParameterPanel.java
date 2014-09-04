package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.lukas.OperandType;
import de.uni.freiburg.iig.telematik.swat.lukas.ParamValue;
/**
 * This class represents a parameterpanel where several other Parameters
 * can be added
 * @author bernhard
 *
 */
public abstract class MultipleParameterPanel extends ParameterPanel {

	protected List<ParameterPanel> panelList;
	protected JButton addButton;
	protected String description;
	protected LogFileReader informationReader;
	protected int limit;
	public MultipleParameterPanel(String name, String description, LogFileReader informationReader) {
		super(name);
		limit=Integer.MAX_VALUE;
		init(description, informationReader);
	}
	public MultipleParameterPanel(String name, String description, LogFileReader informationReader, int limit) {
		super(name);
		this.limit=limit;
		init(description, informationReader);
	}
	private void init(String description, LogFileReader informationReader2) {
		this.description=description;
		informationReader=informationReader2;
		panelList=new ArrayList<ParameterPanel>();
		try {
			addButton=new JButton(IconFactory.getIcon("maximize"));
			addButton.setToolTipText("Add another "+description);
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

	protected ParameterPanel addParameter() {
		// TODO Auto-generated method stub
		ParameterPanel p= getNewPanel();
		panelList.add(p);
		updateContent();
		return p;
	}
	/**
	 * this method implements the creation of a new parameterpanel
	 * @return
	 */
	protected abstract ParameterPanel getNewPanel();
	
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
			if(i == panelList.size()-1 && panelList.size() < limit) {
				panel.add(addButton);
			}
			content.add(panel);
			
		}
		JPanel p=(JPanel) content;
		//p.setBorder(BorderFactory.createTitledBorder(p.getName()));
		content.repaint();
		content.updateUI();
	}

	protected void removeParameter(ParameterPanel patternParameterPanel) {
		// TODO Auto-generated method stub
		content.remove(patternParameterPanel.getContent());
		panelList.remove(patternParameterPanel);
		updateContent();
	}

	@Override
	public List<ParamValue> getValue() {
		// TODO Auto-generated method stub
		ArrayList<ParamValue> values=new ArrayList<ParamValue>();
		for(ParameterPanel panel: panelList) {
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
			ParameterPanel p=addParameter();
			ArrayList<ParamValue> list=new ArrayList<ParamValue>();
			list.add(val);
			p.setValue(list);
		}
		updateContent();
	}

}
