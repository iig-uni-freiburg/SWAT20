package de.uni.freiburg.iig.telematik.swat.prism.searcher;

import java.io.IOException;

import de.invation.code.toval.validate.ParameterException;

public class PrismSearcherFactory {
	
	public static PrismSearcher getPrismSearcher() throws IOException, ParameterException{
		String OS = System.getProperty("os.name").toLowerCase();
		
		if (OS.contains("win"))
			return new WindowsSearcher();
		if (OS.contains("mac"))
			return new MacOSSearcher();
		if (OS.contains("nux") || OS.contains("sunos"))
			return new LinuxSearcher();
		
		return null;
	}

}
