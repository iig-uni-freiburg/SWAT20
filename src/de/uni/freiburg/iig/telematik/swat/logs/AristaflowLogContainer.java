package de.uni.freiburg.iig.telematik.swat.logs;

import de.invation.code.toval.debug.SimpleDebugger;

/**
 *
 * @author stocker
 */
public class AristaflowLogContainer extends AbstractLogModelContainer {

    public AristaflowLogContainer(String basePath) {
        super(basePath);
        setUseSubdirectoriesForComponents(true);
    }

    public AristaflowLogContainer(String basePath, SimpleDebugger debugger) {
        super(basePath, debugger);
        setUseSubdirectoriesForComponents(true);
    }
    
    @Override
    protected SwatLogType getLogType() {
        return SwatLogType.Aristaflow;
    }

//	public void addComponent(File log) throws ProjectComponentException {
//		Validate.notNull(log);
//		Validate.fileName(log.getName());
//		try {
//			Files.copy(log.toPath(), new File(getBasePath(),log.getName()).toPath(),StandardCopyOption.REPLACE_EXISTING);
//			super.addComponent(new LogModel(new File(getBasePath(),log.getName()), SwatLogType.Aristaflow),true,true);
//		} catch (IOException e) {
//			throw new ProjectComponentException("Could not copy log", e);
//		}
//	}
	
	@Override
	protected String getFileEndingForComponent(LogModel component) {
		return "csv";
	}
}
