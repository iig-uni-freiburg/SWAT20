package de.uni.freiburg.iig.telematik.swat;


import de.invation.code.toval.graphic.misc.AbstractWorkingDirectoryStartup;
import de.invation.code.toval.misc.wd.AbstractWorkingDirectoryProperties;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;
import de.uni.freiburg.iig.telematik.swat.workbench.dialog.WorkingDirectoryDialog;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

public class SwatStartup extends AbstractWorkingDirectoryStartup {

    public final static String VERSION_NUMBER = "0.0.2";
    public final static String VERSION_NAME = "v" + VERSION_NUMBER;
    private static final String TOOL_NAME = "SWAT";
    private static final String WORKING_DIRECTORY_DESCRIPTOR = "Working Directory";

    @Override
    protected String getToolName() {
        return TOOL_NAME;
    }

    @Override
    protected void initializeComponentContainer() throws Exception {
        SwatProperties.getInstance();
    }

    @Override
    protected void createMainClass() throws Exception {
        Workbench.getInstance();
    }

    @Override
    protected String getWorkingDirectoryDescriptor() {
        return WORKING_DIRECTORY_DESCRIPTOR;
    }

    @Override
    protected String launchWorkingDirectoryDialog() throws Exception {
        return WorkingDirectoryDialog.showDialog(null);
    }

    @Override
    protected AbstractWorkingDirectoryProperties getWorkingDirectoryProperties() throws Exception {
        return SwatProperties.getInstance();
    }

    public static void main(String[] args) {
        new SwatStartup();
    }
}
