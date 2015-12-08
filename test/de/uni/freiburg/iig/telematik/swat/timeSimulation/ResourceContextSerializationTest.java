package de.uni.freiburg.iig.telematik.swat.timeSimulation;

import java.io.File;
import java.io.IOException;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResourceContext;
import de.uni.freiburg.iig.telematik.swat.jascha.fileHandling.ResourceContextParser;
import de.uni.freiburg.iig.telematik.swat.jascha.fileHandling.ResourceContextSerializer;

public class ResourceContextSerializationTest {
	
	public static void main(String args[]) throws IOException{
		IResourceContext context = ContextRepo.getResourceContext();
		System.out.println("Created Context:\r\n "+context.toString());
		File temp = File.createTempFile("resource-context", ".xml.tmp");
		System.out.println("File: "+temp.getAbsolutePath());
		System.out.println();
		ResourceContextSerializer.serialize(context, temp);
		IResourceContext loadedContext = ResourceContextParser.parse(temp);
		System.out.println("Loaded Context:\r\n "+loadedContext.toString());
		System.out.println();
		System.out.println("Equal: "+context.toString().equalsIgnoreCase(loadedContext.toString()));
		
	}

}
