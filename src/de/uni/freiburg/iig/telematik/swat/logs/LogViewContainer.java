package de.uni.freiburg.iig.telematik.swat.logs;

import de.invation.code.toval.debug.SimpleDebugger;
import de.invation.code.toval.misc.wd.AbstractComponentContainer;
import de.invation.code.toval.misc.wd.ComponentListener;
import de.invation.code.toval.misc.wd.ProjectComponentException;
import de.uni.freiburg.iig.telematik.sewol.log.LogView;
import de.uni.freiburg.iig.telematik.sewol.log.LogViewSerialization;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Adrian Lange
 */
public class LogViewContainer extends AbstractComponentContainer<LogView> implements ComponentListener<LogView> {

        public final static String LOG_VIEW_EXTENSION = "view";

        public LogViewContainer(String basePath) {
                this(basePath, null);
        }

        public LogViewContainer(String basePath, SimpleDebugger debugger) {
                super(basePath, debugger);
                setIgnoreIncompatibleFiles(true);
                setUseSubdirectoriesForComponents(true);
                addComponentListener(this);
        }

        @Override
        public String getComponentDescriptor() {
                return "Log View";
        }

        @Override
        protected File getComponentFile(File pathFile, String componentName) throws ProjectComponentException {
                File view;
                try {
                        view = new File(pathFile.getCanonicalPath(), componentName + "." + LOG_VIEW_EXTENSION);
                        return view;
                } catch (IOException e) {
                        throw new ProjectComponentException("Could not find log view file for " + componentName + ": " + e.getMessage());
                }
        }

        @Override
        public void loadComponents() throws ProjectComponentException {
                super.loadComponents();
                debugMessage("Load log view");
        }

        @Override
        protected LogView loadComponentFromFile(String file) throws Exception {
                return LogViewSerialization.parse(file);
        }

        @Override
        protected void serializeComponent(LogView component, String basePath, String fileName) throws Exception {
                LogViewSerialization.write(component, new File(basePath, fileName).toString());
        }

        @Override
        public Set<String> getAcceptedFileEndings() {
                return new HashSet<>(Arrays.asList(LOG_VIEW_EXTENSION));
        }

        @Override
        public void renameComponent(String oldName, String newName) throws ProjectComponentException {
                renameComponent(oldName, newName, DEFAULT_NOTIFY_LISTENERS);
        }

        @Override
        public void renameComponent(String oldName, String newName, boolean notifyListeners) throws ProjectComponentException {
                super.renameComponent(oldName, newName, notifyListeners);
                getComponent(newName).setFileReference(getComponentFile(newName));
        }

        @Override
        public void componentAdded(LogView component) throws ProjectComponentException {}

        @Override
        public void componentRemoved(LogView component) throws ProjectComponentException {}

        @Override
        public void componentRenamed(LogView component, String oldName, String newName) throws ProjectComponentException {}

        @Override
        public void componentsChanged() throws ProjectComponentException {}

        @Override
        public void storeComponent(String componentName) throws ProjectComponentException {
                super.storeComponent(componentName);
        }

        @Override
        public void storeComponents() throws ProjectComponentException {
                super.storeComponents();
        }

        @Override
        protected String getFileEndingForComponent(LogView component) {
                return LOG_VIEW_EXTENSION;
        }
}
