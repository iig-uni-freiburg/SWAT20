package de.uni.freiburg.iig.telematik.swat.editor.actions;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JColorChooser;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;

/**
*
*/
@SuppressWarnings("serial")
public class ColorAction extends AbstractAction
{
	/**
	 * 
	 */
	protected String name, key;

	/**
	 * 
	 * @param key
	 */
	public ColorAction(String name, String key)
	{
		this.name = name;
		this.key = key;
	}

	/**
	 * 
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() instanceof mxGraphComponent)
		{
			mxGraphComponent graphComponent = (mxGraphComponent) e
					.getSource();
			mxGraph graph = graphComponent.getGraph();

			if (!graph.isSelectionEmpty())
			{
				Color newColor = JColorChooser.showDialog(graphComponent,
						name, null);

				if (newColor != null)
				{
					graph.setCellStyles(key, mxUtils.hexString(newColor));
				}
			}
		}
	}
}