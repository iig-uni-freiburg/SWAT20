package de.uni.freiburg.iig.telematik.swat.simon.fileHandling;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import com.thoughtworks.xstream.XStream;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeContext;

public class TimeContextSerializer {
	
	public static void serialize(File file, ITimeContext context) throws FileNotFoundException{
		XStream xstream = new XStream();
		String contextStream=xstream.toXML(context);
		PrintWriter out = new PrintWriter(file);
		out.println(contextStream);
		out.flush();
		out.close();
	}

}
