package de.uni.freiburg.iig.telematik.swat.patterns;

public class PatternException extends Exception {

	private static final long serialVersionUID = -4873355150012244412L;

	public PatternException() {
		super();
	}

	public PatternException(String message, Throwable cause) {
		super(message, cause);
	}

	public PatternException(String message) {
		super(message);
	}

	public PatternException(Throwable cause) {
		super(cause);
	}
	
}
