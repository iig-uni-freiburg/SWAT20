/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.freiburg.iig.telematik.swat.misc.timecontext;

import com.thoughtworks.xstream.XStream;
import de.invation.code.toval.debug.SimpleDebugger;
import de.invation.code.toval.misc.wd.AbstractComponentContainer;
import java.io.File;
import java.io.PrintWriter;

/**
 *
 * @author stocker
 */
public class TimeContextContainer extends AbstractComponentContainer<TimeContext> {

    public static final String TIME_CONTEXT_DESCRIPTOR = "Time Context";

    public TimeContextContainer(String basePath) {
        this(basePath, null);
    }

    public TimeContextContainer(String basePath, SimpleDebugger debugger) {
        super(basePath, debugger);
        setIgnoreIncompatibleFiles(true);
        setUseSubdirectoriesForComponents(false);
    }

    @Override
    public String getComponentDescriptor() {
        return TIME_CONTEXT_DESCRIPTOR;
    }

    @Override
    protected TimeContext loadComponentFromFile(String file) throws Exception {
        return TimeContext.parse(new File(file));
    }

    @Override
    protected void serializeComponent(TimeContext component, String basePath, String fileName) throws Exception {
        XStream xstream = new XStream();
        xstream.autodetectAnnotations(true);
        String serialString = xstream.toXML(component);
        PrintWriter writer = new PrintWriter(basePath.concat(fileName));
        writer.write(serialString);
        writer.checkError();
        writer.close();
    }

}
