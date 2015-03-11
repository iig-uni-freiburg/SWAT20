package de.uni.freiburg.iig.telematik.swat.misc;


public class OperatingSystem {
	
	public static OperatingSystems getOperatingSystem() {
		String OS = System.getProperty("os.name").toLowerCase();
		if (OS.contains("win"))
			return OperatingSystems.win;
		if (OS.contains("mac"))
			return OperatingSystems.mac;
		if (OS.contains("nux") || OS.contains("sunos"))
			return OperatingSystems.linux;
		return null;
	}

	public enum OperatingSystems {
		win, linux, mac;
	}

}


