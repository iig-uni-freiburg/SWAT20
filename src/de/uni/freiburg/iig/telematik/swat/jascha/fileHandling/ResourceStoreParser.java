package de.uni.freiburg.iig.telematik.swat.jascha.fileHandling;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.thoughtworks.xstream.XStream;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResource;
import de.uni.freiburg.iig.telematik.swat.jascha.CompoundResource;
import de.uni.freiburg.iig.telematik.swat.jascha.ResourceStore;

public class ResourceStoreParser {
	
	static public ResourceStore parse(File file) throws IOException {
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
			ResourceStore store = (ResourceStore) new XStream().fromXML(content);
			linkCompountResources(store);
			return store;
		} catch (ClassCastException e) {
			throw new IOException("Could not convert to ResourceContext: ", e);
		}

	}
	
	static private void linkCompountResources(ResourceStore store){
		for(IResource res:store.getAllResources()){
			if(res instanceof CompoundResource){
				CompoundResource cRes=(CompoundResource) res;
				cRes.setStore(store);
			}
		}
	}

}
