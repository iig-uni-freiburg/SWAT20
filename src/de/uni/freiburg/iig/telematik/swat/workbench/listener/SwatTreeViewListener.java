package de.uni.freiburg.iig.telematik.swat.workbench.listener;

import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeNode;


public interface SwatTreeViewListener {
	
	public void componentSelected(SwatTreeNode node);
	
	public void componentActivated(SwatTreeNode node);

}
