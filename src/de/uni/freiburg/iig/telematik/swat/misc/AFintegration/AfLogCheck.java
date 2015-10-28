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

import de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.ModelCheckerResult;
import de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.sciff.execution.SCIFF;
import de.uni.freiburg.iig.telematik.swat.aristaFlow.AristaFlowParser;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.model_info_provider.XESLogInfoProvider;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.CompliancePattern;
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
import org.processmining.analysis.sciffchecker.logic.interfaces.ISciffLogReader;
import org.processmining.analysis.sciffchecker.logic.model.rule.CompositeRule;
import org.processmining.analysis.sciffchecker.logic.reasoning.CheckerReport;
import org.processmining.analysis.sciffchecker.logic.xml.XMLRuleSerializer;

/**
 *
 * @author richard
 */
public class AfLogCheck {

    AristaFlowParser parser;
    XESLogInfoProvider infoProvider;
    File file;

    private static void printhelp() {
        System.out.println("usage: AfLogCheck file activity1 [activity2]");
        System.out.println("Checks if activity1 is present and if activity1 and activity2 are done by different persons");
    }

    public static void main(String args[]) throws Exception {
        //String[] args2={"/tmp/aflog.csv","Freigabe","Sachliche Prüfung"};
        //args=args2;
        if (args.length == 0 || args.length == 1 || args.length > 3) {
            printhelp();
            System.exit(0);
        }
        try{

        AfLogCheck checker = new AfLogCheck(new File(args[0]));
        boolean exists = checker.activityExists(args[1]);
        if(exists)
        checker.fourEyes(args[1], args[2]);
        else
        	System.out.println("Could not check Four Eyes: "+args[1]+" does not exist");
        }
        catch(Exception e){
        	System.err.println("Error "+e.getMessage());
        }

    }

    public AfLogCheck(File file) throws Exception {

            parser = new AristaFlowParser(file);
            parser.parse(AristaFlowParser.whichTimestamp.BOTH);
            // infoProvider = new XESLogInfoProvider(file);
            this.file = file;


    }

    public boolean activityExists(String activity) throws Exception {
        System.out.println("Check activity '" + activity + "' exists:");
        XESLogExists test = new XESLogExists();
        ArrayList<Parameter> params = test.getParameters();
        params.get(0).getValue().setValue(activity);
        params.get(0).getValue().setType(ParameterTypeNames.ACTIVITY);
        invokeSciff(test);
        printDetails();
        
		// return report
		CheckerReport report = (CheckerReport) ModelCheckerResult.getResult();
		if (report.correctInstances() == null || report.correctInstances().isEmpty())
			return false;
		return true;

    }

    /**
     * returns true if both activities are done by different person*
     */
    public void fourEyes(String activity1, String activity2) throws Exception {
        CompliancePattern fourEyes = new FourEyes();
        System.out.println("Check Four Eyes '" + activity1 + "' and '" + activity2+"'");
        
        ArrayList<Parameter> params = fourEyes.getParameters();
        params.get(0).getValue().setValue(activity1);
        params.get(1).getValue().setValue(activity2);
        invokeSciff(fourEyes);
        printDetails();
    }

    private void printPattern(CompositeRule pattern) {
        try {
            System.out.println("Rule: ");
            Element output = XMLRuleSerializer.serialize(pattern, "test");
            XMLOutputter outPutter = new XMLOutputter();
            outPutter.output(output, System.out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void invokeSciff(CompliancePattern singlePattern) throws Exception {
        singlePattern.setFormalization();
        singlePattern.instantiate();
        ArrayList<CompliancePattern> test = new ArrayList<>();
        test.add(singlePattern);
        SCIFF checker = new SCIFF(file);
        checker.run(test);
    }

    private void printDetails() {
        if (ModelCheckerResult.hasAResult()) {
            CheckerReport report = (CheckerReport) ModelCheckerResult.getResult();
            ISciffLogReader log = report.getLog();
            System.out.println("Violating (" + report.wrongInstances().size() + ")");
            for (int violating : report.wrongInstances()) {
                System.out.println(log.getInstance(violating));
            }
            //System.out.println("");
            System.out.println("Correct (" + report.correctInstances().size() + ")");
            for (int correct : report.correctInstances()) {
                System.out.println(log.getInstance(correct));
            }
            System.out.println("");
        }
    }
}
