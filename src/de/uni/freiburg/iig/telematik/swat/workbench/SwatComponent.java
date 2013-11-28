package de.uni.freiburg.iig.telematik.swat.workbench;

import javax.swing.JComponent;

public interface SwatComponent {
	
	public JComponent getMainComponent();
	
	public JComponent getPropertiesView();

	public String getName();

}
