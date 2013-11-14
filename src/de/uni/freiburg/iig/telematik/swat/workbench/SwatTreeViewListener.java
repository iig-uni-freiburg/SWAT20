package de.uni.freiburg.iig.telematik.swat.workbench;

import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeView.SwatTreeNode;

public interface SwatTreeViewListener {
	
	public void componentSelected(SwatTreeNode node);
	
	public void componentActivated(SwatTreeNode node);

}
