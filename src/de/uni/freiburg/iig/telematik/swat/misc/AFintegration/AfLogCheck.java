/*
 * Copyright (c) 2015, IIG Telematics, Uni Freiburg
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted (subject to the limitations in the disclaimer
 * below) provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * Neither the name of IIG Telematics, Uni Freiburg nor the names of its
 *   contributors may be used to endorse or promote products derived from this
 *   software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY
 * THIS LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BELIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.uni.freiburg.iig.telematik.swat.misc.AFintegration;

import de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.sciff.AristaFlowParser;
import de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.sciff.execution.SCIFF;
import de.uni.freiburg.iig.telematik.swat.logs.LogFileViewer;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.model_info_provider.XESLogInfoProvider;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.Exists;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.parameter.Parameter;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.parameter.ParameterTypeNames;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.xeslog.FourEyes;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.xeslog.XESLogExists;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.processmining.analysis.sciffchecker.logic.model.rule.CompositeRule;
import org.processmining.analysis.sciffchecker.logic.xml.XMLRuleSerializer;

/**
 *
 * @author richard
 */
public class AfLogCheck {
    AristaFlowParser parser;
    XESLogInfoProvider infoProvider;
    File file;
    
    public static void main (String args[]) throws Exception{
        AfLogCheck checker = new AfLogCheck(new File("/tmp/aflog.cvs"));
        System.out.println("Activity exists: "+checker.activityExists("Freigabe"));
        
    }
    
    public AfLogCheck(File file){
        try {
            parser = new AristaFlowParser(file);
            parser.parse(AristaFlowParser.whichTimestamp.BOTH);
           // infoProvider = new XESLogInfoProvider(file);
            this.file=file;
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AfLogCheck.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(AfLogCheck.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    /**returns true if both activities are done by different person**/
    public boolean checkFourEyes(String activity1, String activity2) throws Exception{
        FourEyes test = new FourEyes();
        test.acceptInfoProfider(infoProvider);
        ArrayList<Parameter> params = test.getParameters();
        params.get(0).getValue().setValue(activity1);
        params.get(1).getValue().setValue(activity2);
        test.setFormalization();
        test.instantiate();
        ArrayList<CompliancePattern> pattern=new ArrayList<>();
        pattern.add(test);
        SCIFF sciff = new SCIFF(file);
        sciff.run(pattern);
        if(test.getProbability()>=0.9999) return true;
        return false;
    }
    
    public boolean activityExists(String activity) throws Exception{
        XESLogExists test = new XESLogExists();
        //test.acceptInfoProfider(infoProvider);
        ArrayList<Parameter> params = test.getParameters();
        params.get(0).getValue().setValue(activity);
        params.get(0).getValue().setType(ParameterTypeNames.ACTIVITY);
        test.setFormalization();
        test.instantiate();
        ArrayList<CompliancePattern>pattern = new ArrayList<>();
        pattern.add(test);
        new SCIFF(file).run(pattern);
        if(test.getProbability()>=0.9999)return true;
        printPattern(((ArrayList<CompositeRule>) test.getFormalization()).get(0));
        return false;
        
    }
    
    private void printPattern(CompositeRule pattern){
        try {
			System.out.println("Rule: ");
			Element output = XMLRuleSerializer.serialize(pattern, "test");
			XMLOutputter outPutter = new XMLOutputter();
			outPutter.output(output, System.out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    }
    
    

