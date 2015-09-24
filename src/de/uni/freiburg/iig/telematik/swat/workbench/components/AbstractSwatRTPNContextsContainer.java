package de.uni.freiburg.iig.telematik.swat.workbench.components;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.thoughtworks.xstream.XStream;

import de.invation.code.toval.misc.NamedComponent;
import de.invation.code.toval.misc.wd.AbstractComponentContainer;

public abstract class AbstractSwatRTPNContextsContainer<X extends NamedComponent> extends AbstractComponentContainer<X> {

	public AbstractSwatRTPNContextsContainer(String basePath) {
		super(basePath);
		setUseSubdirectoriesForComponents(true);
	}

	@Override
	protected X loadComponentFromFile(String file) throws Exception {
		XStream stream = new XStream();
		String input = new String(Files.readAllBytes(Paths.get(file)), StandardCharsets.UTF_8);
		try {
			X o = (X) stream.fromXML(input);
			return o;
		} catch (ClassCastException e) {
			throw new Exception("Cannot cast to NamedComponent", e);
		}

	}

	@Override
	protected void serializeComponent(NamedComponent component, String basePath, String fileName) throws Exception {
		File out = new File(new File(basePath), fileName);
		XStream stream = new XStream();
		String serialized = stream.toXML(component);
		FileWriter fw = new FileWriter(out);
		fw.write(serialized);
		fw.close();
	}

}
