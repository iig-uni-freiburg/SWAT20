package de.uni.freiburg.iig.telematik.swat.logs;

import de.invation.code.toval.debug.SimpleDebugger;

/**
 *
 * @author stocker
 */
public class XesLogContainer extends AbstractLogModelContainer {

        public XesLogContainer(String basePath) {
                super(basePath);
        }

        public XesLogContainer(String basePath, SimpleDebugger debugger) {
                super(basePath, debugger);
        }

        @Override
        protected SwatLogType getLogType() {
                return SwatLogType.XES;
        }

        @Override
        protected String getFileEndingForComponent(LogModel component) {
                return "xes";
        }

}
