/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.freiburg.iig.telematik.swat.analysis;

import com.thoughtworks.xstream.XStream;
import de.invation.code.toval.debug.SimpleDebugger;
import de.invation.code.toval.misc.wd.AbstractComponentContainer;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Container for pattern analysis report
 * @author stocker, richard
 */
public class AnalysisContainer extends AbstractComponentContainer<Analysis> {

    public static final String ANALYSIS_DESCRIPTOR = "Analysis";
    public static final String ANALYSIS_FILE_ENDING = "xml";

    public AnalysisContainer(String basePath) {
        super(basePath);
    }

    public AnalysisContainer(String basePath, SimpleDebugger debugger) {
        super(basePath, debugger);
    }

    @Override
    public String getComponentDescriptor() {
        return ANALYSIS_DESCRIPTOR;
    }

    @Override
    protected Analysis loadComponentFromFile(String file) throws Exception {
        XStream xstream = new XStream();
        Analysis analysis = null;
        try {
            analysis = (Analysis) xstream.fromXML(new FileInputStream(file));
            analysis.setFormalizationOnPatterns();
            analysis.setLoadedFromDisk();
        } catch (Exception e) {
            throw new Exception("Exception while parsing analysis from XML file "+file, e);
        }
        return analysis;
    }

    @Override
    protected void serializeComponent(Analysis component, String basePath, String fileName) throws Exception {
        XStream xstream = new XStream();
        xstream.autodetectAnnotations(true);//adhere to XMLomitFields annotations
        String serialString = xstream.toXML(component);
        PrintWriter writer = new PrintWriter(new File(basePath, fileName));
        //PrintWriter writer = new PrintWriter(String.format(AnalysisNameFormat, storagePath.getAbsolutePath(), analysis.getName()));
        writer.write(serialString);
        writer.checkError();
        writer.close();
    }

    @Override
    protected String getFileEndingForComponent(Analysis component) {
         return ANALYSIS_FILE_ENDING;
    }

    @Override
    public Set<String> getAcceptedFileEndings() {
        return new HashSet<>(Arrays.asList(ANALYSIS_FILE_ENDING));
    }
    
    

}
