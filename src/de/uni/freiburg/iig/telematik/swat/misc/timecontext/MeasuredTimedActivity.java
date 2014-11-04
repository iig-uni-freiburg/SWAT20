//package de.uni.freiburg.iig.telematik.swat.misc.timecontext;
//
//import java.util.Arrays;
//import java.util.Random;
//
//import org.apache.commons.math3.distribution.AbstractRealDistribution;
//
//public class MeasuredTimedActivity implements TimeBehavior {
//
//	double[] bins;
//	double[] frequency;
//	double[] cummulated;
//	//double step;
//
//	Random r;
//
//	public static void main(String[] args){
//		double[] occurence = { 1, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4 };
//		MeasuredTimedActivity activity = new MeasuredTimedActivity(occurence, 4);
//		for (int i = 0; i < 10; i++)
//			System.out.println(activity.getNeededTime() + ",");
//	}
//
//	/** give array of all occurences and create a histogram **/
//	public MeasuredTimedActivity(double[] occurences, int numberOfBins) {
//		Arrays.sort(occurences);
//		initializeBins(occurences, numberOfBins);
//		frequency = fillBins(occurences, bins);
//
//		//normalize
//		normalize(frequency);
//
//		//transform frequency to inverse
//		cummulated = cummulate(frequency);
//
//		r = new Random();
//	}
//
//	/** make cummulative histogram. last entry will be one **/
//	private double[] cummulate(double[] frequency) {
//		double[] cummulated = new double[frequency.length];
//		double sum = 0;
//		for (int i = 0; i < frequency.length; i++) {
//			sum += frequency[i];
//			cummulated[i] = sum;
//		}
//		return cummulated;
//	}
//
//	/** normalize: divide frequency so they sum up to 1 **/
//	private void normalize(double[] frequency) {
//		double sum = 0.0;
//		for (double d : frequency) {
//			sum += d;
//		}
//		for (int i = 0; i < frequency.length; i++) {
//			frequency[i] = frequency[i] / sum;
//		}
//	}
//
//	/**
//	 * create histogram out of occurences with given bins. occurences must be
//	 * sorted
//	 **/
//	private double[] fillBins(double[] occurences, double[] bins) {
//		double[] frequency = new double[bins.length];
//		int startFrom = 0;
//		//sweep through bins
//		for (int i = 0; i < bins.length; i++) {
//			int count = 0;
//			while (startFrom < occurences.length) { //sweep through measeaured occurences
//				if (occurences[startFrom] <= bins[i]) {
//					count++; //add occurence into current bin
//					startFrom++; //go on with next occurence
//				}
//				else
//					break; //bin full. Go to next bin
//			}
//			frequency[i] = count; //put into bin: number of matching occurences
//		}
//		return frequency;
//
//	}
//
//	@Override
//	/** use inverse method to draw a random sample out of occurences**/
//	public double getNeededTime() {
//		double random = r.nextDouble();
//		int index = Arrays.binarySearch(cummulated, random);
//		if (index < 0)
//			index = Math.abs(index) - 1;//not a direct hit. use next neighbor
//
//		return bins[index];
//	}
//
//	/**
//	 * create bin-limits for histogram with
//	 * 
//	 * @param occurences
//	 *            values that will be inserted within bins
//	 * @param numberOfBins
//	 *            how many bins wanted
//	 */
//	public void initializeBins(double[] occurences, int numberOfBins) {
//		bins = new double[numberOfBins];
//		double min = occurences[0]; //value for first bin
//		double max = occurences[occurences.length - 1]; //value for last bin
//		double step = (max - min) / (((double) numberOfBins - 1)); //width of each bin
//		bins[0] = min;
//		for (int i = 1; i < numberOfBins; i++) {
//			bins[i] = bins[i - 1] + step; //value for each bin
//		}
//	}
//
//	@Override
//	public AbstractRealDistribution getDistribution() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//}
