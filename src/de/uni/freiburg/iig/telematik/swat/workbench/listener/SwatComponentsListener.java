package de.uni.freiburg.iig.telematik.swat.workbench.listener;

import de.uni.freiburg.iig.telematik.swat.workbench.SwatTreeView.SwatTreeNode;

public interface SwatComponentsListener {
	
	public void analysisAdded(SwatTreeNode node, Object AnalysisElement);

	public void modelChanged();

	public void nodeAdded(SwatTreeNode node);

	public void elementRemoved(Object elem);

}
