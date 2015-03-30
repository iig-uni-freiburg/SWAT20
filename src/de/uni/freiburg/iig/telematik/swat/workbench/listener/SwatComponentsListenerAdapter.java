package de.uni.freiburg.iig.telematik.swat.workbench.listener;

import de.invation.code.toval.misc.soabase.SOABase;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
import de.uni.freiburg.iig.telematik.sewol.accesscontrol.AbstractACModel;
import de.uni.freiburg.iig.telematik.swat.logs.LogModel;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.TimeContext;
import de.uni.freiburg.iig.telematik.swat.workbench.Analysis;

public class SwatComponentsListenerAdapter implements SwatComponentsListener {

	@Override
	public void petriNetAdded(AbstractGraphicalPN net) {}

	@Override
	public void petriNetRemoved(AbstractGraphicalPN net) {}
	
	@Override
	public void petriNetRenamed(AbstractGraphicalPN net) {}

	@Override
	public void acModelAdded(AbstractACModel acModel) {}

	@Override
	public void acModelRemoved(AbstractACModel acModel) {}

	@Override
	public void analysisContextAdded(String netID, AnalysisContext context) {}

	@Override
	public void analysisContextRemoved(String netID, AnalysisContext context) {}

	@Override
	public void timeContextAdded(String netID, TimeContext context) {}

	@Override
	public void timeContextRemoved(String netID, TimeContext context) {}

	@Override
	public void logAdded(LogModel log) {}

	@Override
	public void logRemoved(LogModel log) {}

	@Override
	public void componentsChanged() {}

	@Override
	public void analysisAdded(String netID, Analysis analysis) {}

	@Override
	public void analysisRemoved(String netID, Analysis analysis) {}

	@Override
	public void contextAdded(SOABase soaBase) {}

	@Override
	public void contextRemoved(SOABase soaBase) {}

}
