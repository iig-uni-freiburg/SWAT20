package de.uni.freiburg.iig.telematik.swat.misc.errorhandling;

import java.awt.Desktop;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.LinkedList;

public class ErrorStorage {
	static ErrorStorage myErrorStorage = new ErrorStorage();
	private ErrorStore store = new ErrorStore();

	public static void main(String args[]) throws IOException, URISyntaxException {
		ErrorStorage eStore = ErrorStorage.getInstance();
		//eStore.addMessage("Test1", new Exception("blubb"));
		//eStore.addMessage("Test2", new Exception("blubb"));
		//eStore.addMessage("Test3", new Exception("blubb"));
		eStore.addMessage(null, new Exception("String is empty"));
		eStore.addMessage("Exception is empty", null);
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


	public boolean sendAsMail() {
		//first make URI.Compatible
		String errorStrings;
		try {
			errorStrings = URLEncoder.encode(store.toString(), "UTF-8").replace("+", "%20");
			Desktop.getDesktop().mail(new URI("mailto:zahoransky@iig.uni-freiburg.de?subject=Bugreport%20SWAT&body=" + errorStrings));
			return true;
		} catch (UnsupportedEncodingException e) {
		} catch (IOException e) {
		} catch (URISyntaxException e) {
		}
		return false;

	}


	class ErrorElement {

		public ErrorElement(String message, Exception e) {
			this.message = "null";
			if (message != null && !message.isEmpty())
				this.message = message;

			this.e = e;

			this.date = getCalendarString();
		}

		public String toString() {
			String exceptionStackTrace = "null", exceptionMessage = "null", exceptionCause = "null";
			if (e != null) {
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				exceptionStackTrace = sw.toString();

				try {
					exceptionMessage = e.getMessage();
					exceptionCause = e.getCause().toString();
				} catch (Exception e) {
				}
			}

			return date + ": " + message + " (" + exceptionMessage + ") \r\n" + exceptionCause + "\r\n" + exceptionStackTrace;
		}

		private String getCalendarString() {
			Calendar cal = Calendar.getInstance();
			return cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH) + "/"
					+ cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
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
		private String date;
	}

	class ErrorStore {
		LinkedList<ErrorElement> errors = new LinkedList<ErrorElement>();

		public void addError(String message, Exception e) {
			errors.add(new ErrorElement(message, e));
			if (errors.size() > 50)
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
