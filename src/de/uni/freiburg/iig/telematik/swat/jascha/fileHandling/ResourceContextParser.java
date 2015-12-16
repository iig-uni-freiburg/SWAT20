package de.uni.freiburg.iig.telematik.swat.jascha.fileHandling;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.thoughtworks.xstream.XStream;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResourceContext;

public class ResourceContextParser {

	static public IResourceContext parse(File file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		StringBuilder sb = new StringBuilder();
		String line = br.readLine();

		while (line != null) {
			sb.append(line);
			line = br.readLine();
		}
		br.close();
		try {
			String content = sb.toString();
			IResourceContext context = (IResourceContext) new XStream().fromXML(content);
			return context;
		} catch (ClassCastException e) {
			throw new IOException("Could not convert to ResourceContext: ", e);
		}

	}

}
