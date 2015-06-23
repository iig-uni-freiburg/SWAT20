package de.uni.freiburg.iig.telematik.swat.plugin.sciff;

import java.util.ArrayList;
import java.util.List;

import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogReader;
import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogSummary;
import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogTrace;

import de.invation.code.toval.misc.ArrayUtils;

public class LogParserAdapterFancy implements ISciffLogReader{
	
	private LogParserAdapter origAdapter = null;
	private int[] mask = null;
	
	public LogParserAdapterFancy(LogParserAdapter origAdapter){
		this.origAdapter = origAdapter;
		mask = ArrayUtils.createAndInitializeArray(origAdapter.traceCount(), 0);
	}
	
	public LogParserAdapterFancy(LogParserAdapterFancy fancyAdapter, List<Integer> keep){
		this.origAdapter = fancyAdapter.getOrigAdapter();
		mask = new int[keep.size()];
		for(int i=0; i<keep.size(); i++){
			mask[i] = keep.get(i);
		}
	}
	
	public LogParserAdapter getOrigAdapter(){
		return origAdapter;
	}

	@Override
	public List<ISciffLogTrace> getInstances() {
		List<ISciffLogTrace> instances = new ArrayList<ISciffLogTrace>();
		for(int i=0; i<mask.length; i++){
			instances.add(origAdapter.getInstance(mask[i]));
		}
		return null;
	}

	@Override
	public ISciffLogTrace getInstance(int index) {
		return origAdapter.getInstance(mask[index]);
	}

	@Override
	public int traceCount() {
		return mask.length;
	}

	@Override
	public ISciffLogSummary getSummary() {
		return null;
	}
}
