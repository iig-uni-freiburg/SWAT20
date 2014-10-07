package de.uni.freiburg.iig.telematik.swat.workbench.listener;

import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.ACModel;

public interface SwatComponentsListener {
	
//	public void analysisAdded(SwatTreeNode node, Object AnalysisElement);
//
//	public void modelChanged();
//
//	public void nodeAdded(SwatTreeNode node);
//
//	public void elementRemoved(Object elem);
	
	public void petriNetAdded(String netID, AbstractGraphicalPN net);
	
	public void petriNetRemoved(String netID, AbstractGraphicalPN net);

	public void acModelAdded(String name, ACModel acModel);
	
	public void acModelRemoved(String name, ACModel acModel);
	
//TODO: Weitere Methoden für andere Komponenten
}
