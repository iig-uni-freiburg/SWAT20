package de.uni.freiburg.iig.telematik.swat.simon;

import java.text.DecimalFormat;

import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.GammaDistribution;
import org.apache.commons.math3.distribution.LogNormalDistribution;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeBehaviour;
import de.uni.freiburg.iig.telematik.sewol.log.LogEntry;
import de.uni.freiburg.iig.telematik.swat.misc.timecontext.distributions.DistributionType;

public abstract class AbstractTimeBehaviour implements ITimeBehaviour {
	
	AbstractRealDistribution distribution;
	boolean available =true;
	DistributionType type = DistributionType.UNKNOWN;
	DecimalFormat format = new DecimalFormat("##.##");
	String[] parameterNames;
	private double[] parameterValues;


	@Override
	public boolean isAvailable() {
		// TODO Auto-generated method stub
		return available;
	}

	@Override
	public void setAvailability(boolean isAvailable) {
		this.available=isAvailable;

	}

	@Override
	public double getNeededTime() {
		return distribution.sample();
	}
	
	public DistributionType getType(){
		return type;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(type.toString());
		sb.append("(");

		try {
			for (double d : getParameterValues()) //insert later, after all log normals alre finished
				sb.append(format.format(d)+", ");
			sb.deleteCharAt(sb.length()-1);
			sb.deleteCharAt(sb.length()-1);
			
		} catch (Exception e) {
				sb.append(format.format(distribution.getNumericalMean()));
				sb.append(", " + format.format(distribution.getNumericalVariance()));
			} 
		sb.append(")");
		return sb.toString();
	}
	
	protected void setParameterNames(String... names){
		parameterNames=names;
	}
	
	protected void setParameterValues(double... values){
		parameterValues = values;
	}
	
	public double[] getParameterValues() {
		return parameterValues;
	}
	
	public int getNumberOfParameters(){
		return parameterNames.length;
	}
	
	public String getParameter(int index){
		return parameterNames[index];
	}
	
	public String[] getParameters(){
		return parameterNames;
	}
	
	public double getCummulativeValueAt(double x){
		return distribution.cumulativeProbability(x);
	}
	
	public void setParameterValues(double val1, double val2){
		double[] result={val1,val2};
		setParameterValues(result);
	}
	
	public void setParameterValues(double val1){
		double[] result={val1};
		setParameterValues(result);
	}

	}




