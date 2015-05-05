package de.uni.freiburg.iig.telematik.swat.analysis.modelchecker;

public class ModelCheckerResult {

	private static Object result = null;
	private static boolean hasResult = false;

	public static Object getResult() {
		return result;
	}

	public static void setResult(Object modelCheckerResult) {
		System.out.println();
		System.out.println("setting result " + modelCheckerResult);
		result = modelCheckerResult;
		hasResult = true;
	}

	public static void removeResult() {
		result = null;
		hasResult = false;
	}

	public static boolean hasAResult() {
		return hasResult;
	}

}
