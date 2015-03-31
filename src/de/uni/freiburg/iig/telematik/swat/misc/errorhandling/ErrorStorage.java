package de.uni.freiburg.iig.telematik.swat.misc.errorhandling;

import java.awt.Desktop;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.LinkedList;

class ErrorStorage {
	static ErrorStorage myErrorStorage = new ErrorStorage();
	private ErrorStore store = new ErrorStore();

	public static void main(String args[]) throws IOException, URISyntaxException {
		ErrorStorage eStore = ErrorStorage.getInstance();
		eStore.addMessage("Test1", new Exception("blubb"));
		eStore.addMessage("Test2", new Exception("blubb"));
		//System.out.println(("mailto:zahoransky@iig.uni-freiburg.de&bsubject=BugreportSWAT&body=" + eStore.toString()).substring(66, 76));
		eStore.sendAsMail();
	}


	private ErrorStorage() {
		//which file to use?
	}

	public static ErrorStorage getInstance() {
		return myErrorStorage;
	}
	
	@Override
	public String toString() {
		return store.toString();
	}

	public void addMessage(String message, Exception e) {
		store.addError(message, e);
	}


	private void sendAsMail() throws IOException, URISyntaxException {
		//first make URI.Compatible
		//String errorStrings = store.toString();
		String errorStrings = URLEncoder.encode(store.toString(), "UTF-8").replace("+", "%20");
		Desktop.getDesktop().mail(new URI("mailto:zahoransky@iig.uni-freiburg.de?subject=Bugreport%20SWAT&body=" + errorStrings));
	}


	class ErrorElement {

		public ErrorElement(String message, Exception e) {
			this.message = message;
			this.e = e;
		}

		public String toString() {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));

			return message + " (" + e.getMessage() + ") \r\n" + e.getCause() + "\r\n" + sw.toString();
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public Exception getE() {
			return e;
		}

		public void setE(Exception e) {
			this.e = e;
		}

		private String message;
		private Exception e;
	}

	class ErrorStore {
		LinkedList<ErrorElement> errors = new LinkedList<ErrorElement>();

		public void addError(String message, Exception e) {
			errors.add(new ErrorElement(message, e));
			if (errors.size() > 100)
				errors.removeFirst();
		}

		public String toString(){
			StringBuilder b = new StringBuilder();
			for (ErrorElement elem : errors) {
				b.append(elem.toString());
				b.append("\r\n");
			}
			return b.toString();
		}
	}

}
