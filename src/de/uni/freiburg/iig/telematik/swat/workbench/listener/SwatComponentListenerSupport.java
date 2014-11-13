package de.uni.freiburg.iig.telematik.swat.workbench.listener;

import de.invation.code.toval.event.AbstractListenerSupport;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.ACModel;
import de.uni.freiburg.iig.telematik.swat.logs.LogModel;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.TimeContext;
import de.uni.freiburg.iig.telematik.swat.workbench.Analysis;

public class SwatComponentListenerSupport extends AbstractListenerSupport<SwatComponentsListener> {

	private static final long serialVersionUID = -5976107306359206805L;

	public void notifyPetriNetAdded(AbstractGraphicalPN net){
		for(SwatComponentsListener listener: listeners){
			listener.petriNetAdded(net);
		}
		notifyComponentsChanged();
	}
	
	public void notifyPetriNetRemoved(AbstractGraphicalPN net){
		for(SwatComponentsListener listener: listeners){
			listener.petriNetRemoved(net);
		}
		notifyComponentsChanged();
	}
	
	public void notifyPetriNetRenamed(AbstractGraphicalPN net){
		for(SwatComponentsListener listener: listeners){
			listener.petriNetRenamed(net);
		}
		notifyComponentsChanged();
	}
	
	public void notifyLogAdded(LogModel log){
		for(SwatComponentsListener listener: listeners){
			listener.logAdded(log);
		}
		notifyComponentsChanged();
	}

	public void notifyLogRemoved(LogModel log){
		for(SwatComponentsListener listener: listeners){
			listener.logRemoved(log);
		}
		notifyComponentsChanged();
	}
	
	public void notifyACModelAdded(ACModel acModel){
		for(SwatComponentsListener listener: listeners){
			listener.acModelAdded(acModel);
		}
		notifyComponentsChanged();
	}
	
	public void notifyACModelRemoved(ACModel acModel){
		for(SwatComponentsListener listener: listeners){
			listener.acModelRemoved(acModel);
		}
		notifyComponentsChanged();
	}
	
	public void notifyAnalysisContextAdded(String netID, AnalysisContext context){
		for(SwatComponentsListener listener: listeners){
			listener.analysisContextAdded(netID, context);
		}
		notifyComponentsChanged();
	}
	
	public void notifyAnalysisContextRemoved(String netID, AnalysisContext context){
		for(SwatComponentsListener listener: listeners){
			listener.analysisContextRemoved(netID, context);
		}
		notifyComponentsChanged();
	}
	
	public void notifyAnalysisAdded(String netID, Analysis analysis){
		for(SwatComponentsListener listener: listeners){
			listener.analysisAdded(netID, analysis);
		}
		notifyComponentsChanged();
	}
	
	public void notifyAnalysisRemoved(String netID, Analysis analysis){
		for(SwatComponentsListener listener: listeners){
			listener.analysisRemoved(netID, analysis);
		}
		notifyComponentsChanged();
	}
	
	public void notifytimeContextAdded(String netID, TimeContext context){
		for(SwatComponentsListener listener: listeners){
			listener.timeContextAdded(netID, context);
		}
		notifyComponentsChanged();
	}
	
	public void notifyTimeContextRemoved(String netID, TimeContext context){
		for(SwatComponentsListener listener: listeners){
			listener.timeContextRemoved(netID, context);
		}
		notifyComponentsChanged();
	}
	
	public void notifyComponentsChanged(){
		for(SwatComponentsListener listener: listeners){
			listener.componentsChanged();
		}
	}

	public void notifyLogRenamed(LogModel oldModel, LogModel newModel) {
		for (SwatComponentsListener listener : listeners) {
			listener.logRemoved(oldModel);
			listener.componentsChanged();
		}

	}
}
