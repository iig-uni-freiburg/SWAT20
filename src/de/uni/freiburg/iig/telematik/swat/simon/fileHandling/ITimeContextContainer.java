package de.uni.freiburg.iig.telematik.swat.simon.fileHandling;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import de.invation.code.toval.debug.SimpleDebugger;
import de.invation.code.toval.misc.wd.AbstractComponentContainer;
import de.invation.code.toval.misc.wd.ProjectComponentException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResourceContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.ITimeContext;

public class ITimeContextContainer extends AbstractComponentContainer<ITimeContext> {
	
	public static final String DEFAULT_FILE_ENDING = "tc";

	public ITimeContextContainer(String basePath, SimpleDebugger debugger) {
		super(basePath, debugger);
		setIgnoreIncompatibleFiles(true);
		setUseSubdirectoriesForComponents(true);
		setUseSubdirectoriesForComponents(false);
	}

	public ITimeContextContainer(String basePath) {
		this(basePath, null);
	}

	@Override
	public String getComponentDescriptor() {
		return "Time Contexts";
	}

	@Override
	protected ITimeContext loadComponentFromFile(String file) throws Exception {
		return TimeContextParser.parse(new File(file));
	}

	@Override
	protected void serializeComponent(ITimeContext component, String basePath, String fileName) throws Exception {
		File file = new File(basePath, fileName);
		TimeContextSerializer.serialize(file, component);

	}

	public Set<String> getAcceptedFileEndings() {
		return new HashSet<>(Arrays.asList("tc"));
	}

	protected File getComponentFile(File pathFile, String componentName) throws ProjectComponentException {
		return new File(pathFile, componentName + ".tc");
	}

	protected String getFileEndingForComponent(ITimeContext component) {
		return "tc";
	}

}
