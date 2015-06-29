package de.uni.freiburg.iig.telematik.swat.misc.timecontext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogReader;

import com.thoughtworks.xstream.XStream;
import de.invation.code.toval.misc.NamedComponent;

import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.PNTimeContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr.AbstractPTNet;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions.AbstractDistributionView;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions.DistributionViewFactory;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.logAdapter.ISCIFFAdapter;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.logAdapter.TimeAwareAdapter;

public class TimeContext implements PNTimeContext, NamedComponent {

	//TODO: timing for Places. How to get it from the log?

	Map<String, AbstractDistributionView> transitionTime = new HashMap<String, AbstractDistributionView>();
	File file;
	String name;
	String correspondingNet;
	double intendedFinishTime;
	
	public double getIntendedFinishTime() {
		return intendedFinishTime;
	}

	public void setIntendedFinishTime(double intendedFinishTime) {
		this.intendedFinishTime = intendedFinishTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static void main(String args[]) {
		//TimeMachine<?, ?, ?, ?, ?, ?, ?> timeMachine = new TimeMachine<AbstractPlace<F,S>, AbstractTransition<F,S>, AbstractFlowRelation<P,T,S>, AbstractMarking<S>, Object, AbstractMarkingGraphState<M,S>, AbstractMarkingGraphRelation<M,X,S>>(petriNet, timeContext)
	}

	public TimeContext(AbstractPTNet<?, ?, ?, ?> net, ISciffLogReader processLog) {
		TimeAwareAdapter adapter = new ISCIFFAdapter(processLog);
		for (Object transition : net.getTransitions()) {
			if (transition instanceof String) {
				//try to get timing for given transition
				List<Double> timing = adapter.getDurations((String) transition, 100);
				if (timing != null && !timing.isEmpty()) {
					transitionTime.put((String) transition, DistributionViewFactory.getDistributionView(timing));
				}
			}
		}
		//setName(net.getName());
	}

	public TimeContext() {

	}


	public TimeContext(File fileToSave) {
		this.file = fileToSave;
	}

	public void addTimeBehavior(String transitionName, AbstractDistributionView behavior) {
		//System.out.println("Adding time Behavior: " + behavior.toString());
		transitionTime.put(transitionName, behavior);
	}

	public double getSampleDuration(String transition) {
		if (transitionTime.containsKey(transition))
			return transitionTime.get(transition).getNeededTime();
		return 0;
	}

	public AbstractDistributionView getTimeBehavior(String transition) {
		if (transitionTime.containsKey(transition))
			return transitionTime.get(transition);
		return null;
	}

	public boolean containsTransition(String transition) {
		return transitionTime.containsKey(transition);
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
	public double getDelayPT(String placeName, String transitionName) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	synchronized public double getDelayTP(String transitionName, String placeName) {
		if (transitionTime.containsKey(transitionName))
			return transitionTime.get(transitionName).getNeededTime();
		else
			return 0;
	}

	public String getCorrespondingNet() {
		return correspondingNet;
	}

	public void setCorrespondingNet(String correspondingNet) {
		this.correspondingNet = correspondingNet;
	}

	protected void storeContext() throws FileNotFoundException {
		XStream xstream = new XStream();
		xstream.autodetectAnnotations(true);
		String serialString = xstream.toXML(this);
		PrintWriter writer = new PrintWriter(file);
		writer.write(serialString);
		writer.checkError();
		writer.close();
	}

	public String toString() {
		return getName();
	}

	public void removeBehavior(String name) {
		transitionTime.remove(name);
	}

}
