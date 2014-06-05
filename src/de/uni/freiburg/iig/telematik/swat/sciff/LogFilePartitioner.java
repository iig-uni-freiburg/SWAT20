package de.uni.freiburg.iig.telematik.swat.sciff;

import java.util.LinkedList;
import java.util.List;

import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogReader;
import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogSummary;
import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogTrace;

public class LogFilePartitioner implements ISciffLogReader {
	private Integer[] logMapping;
	private ISciffLogReader originalReader;

	public LogFilePartitioner(ISciffLogReader reader, List<Integer> keep) {
		if (reader instanceof LogFilePartitioner) {
			LogFilePartitioner partReader = (LogFilePartitioner) reader;
			originalReader = partReader.getOriginalReader();
			logMapping = new Integer[keep.size()];

			//Store keep as Array for faster access
			Integer[] keepAsArray = new Integer[keep.size()];
			keepAsArray=keep.toArray(keepAsArray);

			//Store old mapping in Array
			Integer[] oldMapping = partReader.getMapping();

			for (int i = 0; i < logMapping.length; i++) {
				logMapping[i] = oldMapping[keepAsArray[i]];
			}

			//free arrays
			keepAsArray = null;
			oldMapping = null;
		}
		else {
			logMapping = new Integer[keep.size()];
			logMapping = keep.toArray(logMapping);
			originalReader = reader;
		}
	}

	@Override
	public List<ISciffLogTrace> getInstances() {
		LinkedList<ISciffLogTrace> filteredList = new LinkedList<ISciffLogTrace>();

		//Buffer original Reader list as Array for faster access
		ISciffLogTrace traces[] = {};
		traces = originalReader.getInstances().toArray(traces);

		for (Integer i : logMapping) {
			filteredList.add(traces[i]);
		}
		traces = null;
		return filteredList;
	}

	@Override
	public ISciffLogTrace getInstance(int index) {
		return originalReader.getInstance(logMapping[index]);
	}

	@Override
	public int traceCount() {
		return logMapping.length;
	}

	@Override
	public ISciffLogSummary getSummary() {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer[] getMapping() {
		return logMapping;
	}

	public ISciffLogReader getOriginalReader() {
		return originalReader;
	}

}
