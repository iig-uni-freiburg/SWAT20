package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.awt.FlowLayout;
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
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.factory.ParamValue;
/**
 * This class represents a Parameterpanel, where several other Parameters
 * can be added
 * @author bernhard
 *
 */
public abstract class MultipleParameterPanel extends ParameterPanel {

	protected List<ParameterPanel> panelList;
	protected JButton addButton;
	protected String description;
	protected AnalysisComponentInfoProvider informationReader;
	protected int limit;
	/**
	 * Create a MultipleParameter that allows 2^32 values
	 * @param name the name of the parameter
	 * @param description description of the parameter e.g. State Predicate
	 * @param informationReader an object implementing the interface InformationReader
	 */
	public MultipleParameterPanel(String name, String description, AnalysisComponentInfoProvider informationReader) {
		super(name);
		limit=Integer.MAX_VALUE;
		init(description, informationReader);
	}
	/**
	 * Create a MultipleParameter for a limited amount of values
	 * @param name the name of the parameter
	 * @param description description of the parameter e.g. State Predicate
	 * @param informationReader an object implementing the interface InformationReader
	 * @param limit the maximum amount of values
	 */
	public MultipleParameterPanel(String name, String description, AnalysisComponentInfoProvider informationReader, int limit) {
		super(name);
		this.limit=limit;
		init(description, informationReader);
	}
	/**
	 * Initialize the UI
	 * @param description the description of the parameter 
	 * @param informationReader Object that implements InformationReader
	 */
	private void init(String description, AnalysisComponentInfoProvider informationReader) {
		this.description=description;
		this.informationReader=informationReader;
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
	/**
	 * add graphical components so that one more value can be selected
	 * @return the new ParameterPanel
	 */
	protected ParameterPanel addParameter() {
		// TODO Auto-generated method stub
		ParameterPanel p= getNewPanel();
		panelList.add(p);
		updateContent();
		return p;
	}
	/**
	 * This method creates a new ParameterPanel that will be added to the UI
	 * @return the new ParameterPanel
	 */
	protected abstract ParameterPanel getNewPanel();
	/**
	 * update the UI
	 */
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
	/**
	 * remove the components to choose a value for the parameter
	 * @param patternParameterPanel the ParameterPanel to remove
	 */
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
