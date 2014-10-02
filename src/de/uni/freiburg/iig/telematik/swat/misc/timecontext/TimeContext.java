package de.uni.freiburg.iig.telematik.swat.misc.timecontext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogReader;

import com.thoughtworks.xstream.XStream;

import de.uni.freiburg.iig.telematik.sepia.petrinet.PNTimeContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTNet;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.logAdapter.ISCIFFAdapter;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.logAdapter.TimeAwareAdapter;

public class TimeContext implements PNTimeContext {

	//TODO: timing for Places. How to get it from the log?

	Map<String, TimeBehavior> transitionTime = new HashMap<String, TimeBehavior>();
	File file;

	public static void main(String args[]) {
		//TimeMachine<?, ?, ?, ?, ?, ?, ?> timeMachine = new TimeMachine<AbstractPlace<F,S>, AbstractTransition<F,S>, AbstractFlowRelation<P,T,S>, AbstractMarking<S>, Object, AbstractMarkingGraphState<M,S>, AbstractMarkingGraphRelation<M,X,S>>(petriNet, timeContext)
	}

	public TimeContext(AbstractPTNet<?, ?, ?, ?, ?, ?> net, ISciffLogReader processLog) {
		TimeAwareAdapter adapter = new ISCIFFAdapter(processLog);
		for (Object transition : net.getTransitions()) {
			if (transition instanceof String) {
				//try to get timing for given transition
				List<Double> timing = adapter.getDurations((String) transition, 100);
				if (timing != null && !timing.isEmpty()) {
					transitionTime.put((String) transition, TimeBehaviorFactory.getTimeBehavior(timing));
				}
			}
		}
	}

	public TimeContext() {

	}


	public TimeContext(File fileToSave) {
		this.file = fileToSave;
	}

	public void addTimeBehavior(String transitionName, TimeBehavior behavior) {
		transitionTime.put(transitionName, behavior);
	}

	public double getSampleDuration(String transition) {
		if (transitionTime.containsKey(transition))
			return transitionTime.get(transition).getNeededTime();
		return 0;
	}

	public TimeBehavior getTimeBehavior(String transition) {
		if (transitionTime.containsKey(transition))
			return transitionTime.get(transition);
		return null;
	}

	public boolean containsTransition(String transition) {
		return transitionTime.containsKey(transition);
	}

	public void storeTimeContext(File file) throws FileNotFoundException {
		if (file == null)
			return;
		setFile(file);
		String serialString = new XStream().toXML(this);
		PrintWriter writer = new PrintWriter(file);
		writer.write(serialString);
		writer.checkError();
		writer.close();
	}

	public static TimeContext parse(File file) {
		TimeContext context = (TimeContext) new XStream().fromXML(file);
		context.setFile(file);
		return context;
	}

	protected File getFile() {
		return file;
	}

	protected void setFile(File file) {
		this.file = file;
	}

	@Override
	public long getDelayPT(String placeName, String transitionName) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getDelayTP(String transitionName, String placeName) {
		if (!transitionTime.containsKey(transitionName))
			System.err.println("TimeContext does not contain information for " + transitionName);
		//System.out.println("Delay for " + transitionName + ": " + (long) transitionTime.get(transitionName).getNeededTime());
		return (long) transitionTime.get(transitionName).getNeededTime();
	}

	public void storeTimeContext() throws FileNotFoundException {
		storeTimeContext(file);

	}

}
