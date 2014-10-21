package de.uni.freiburg.iig.telematik.swat.workbench.listener;

import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.ACModel;
import de.uni.freiburg.iig.telematik.swat.logs.LogModel;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.TimeContext;
import de.uni.freiburg.iig.telematik.swat.workbench.Analysis;

public interface SwatComponentsListener {
	
	public void petriNetAdded(AbstractGraphicalPN net);
	
	public void petriNetRemoved(AbstractGraphicalPN net);
	
	public void petriNetRenamed(AbstractGraphicalPN net);

	public void acModelAdded(ACModel acModel);
	
	public void acModelRemoved(ACModel acModel);
	
	public void analysisContextAdded(String netID, AnalysisContext context);
	
	public void analysisContextRemoved(String netID, AnalysisContext context);
	
	public void analysisAdded(String netID, Analysis analysis);
	
	public void analysisRemoved(String netID, Analysis analysis);
	
	public void timeContextAdded(String netID, TimeContext context);
	
	public void timeContextRemoved(String netID, TimeContext context);
	
	public void logAdded(LogModel log);
	
	public void logRemoved(LogModel log);
	
	public void componentsChanged();
	
}
