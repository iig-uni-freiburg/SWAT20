package de.uni.freiburg.iig.telematik.swat.logs;

import de.invation.code.toval.debug.SimpleDebugger;

/**
 *
 * @author stocker
 */
public class MxmlLogContainer extends AbstractLogModelContainer{

    public MxmlLogContainer(String basePath) {
        super(basePath);
    }

    public MxmlLogContainer(String basePath, SimpleDebugger debugger) {
        super(basePath, debugger);
    }
    
    @Override
    protected SwatLogType getLogType() {
        return SwatLogType.MXML;
    }

	@Override
	protected String getFileEndingForComponent(LogModel component) {
		return "mxml";
	}
}
