package de.unifreiburg.iig.bpworkbench2.logging;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class BPLog {

	private static Logger log;
	private static boolean initialized = false;
	private static Level lvl = Level.FINEST; // LogLevel for logfile and logger

	private BPLog() {

	}

	public synchronized static Logger getLogger(String name) {
		if (!initialized) {
			log = Logger.getLogger(name);
			log.setLevel(lvl);
			addHandler(name);
			initialized = true;
		}

		return log;

	}

	private static void addHandler(String name) {
		try {
			Handler handler = new FileHandler("log.txt", true);
			handler.setFormatter(new SimpleFormatter());
			log.addHandler(handler);
			handler.setLevel(lvl);
			log.log(Level.INFO, "Logging for " + name + " registered");
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
