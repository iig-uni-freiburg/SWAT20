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

import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.RegularIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetTransition;
import de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.prism.PRISM;
import de.uni.freiburg.iig.telematik.swat.analysis.modelchecker.prism.PrismException;
import de.uni.freiburg.iig.telematik.swat.aristaFlow.AristaFlowToPnmlConverter;
import de.uni.freiburg.iig.telematik.swat.misc.FileHelper;
import de.uni.freiburg.iig.telematik.swat.misc.OperatingSystem;
import de.uni.freiburg.iig.telematik.swat.misc.OperatingSystem.OperatingSystems;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.model_info_provider.IFNetInfoProvider;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.CompliancePattern;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.ifnet.IFNetLeadsTo;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.parameter.Parameter;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.parameter.ParameterTypeNames;
import de.uni.freiburg.iig.telematik.swat.patterns.logic.patterns.ptnet.PTNetLeadsTo;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author richard
 */
public class AfTemplateCheck {

    AristaFlowToPnmlConverter parser;
    IFNet net;
    File prismPath;

    public static void main(String[] args) throws Exception {
        String[] args2 = {"/tmp/af.template", "/home/richard/bin/prism-4.2.beta1-linux64/", "Input Customer Data", "Sign Form"};
        args = args2;
        if (args.length == 0 || args.length < 1 || args.length > 4) {
            printhelp();
            System.exit(0);
        }
        try {
            AfTemplateCheck checker = new AfTemplateCheck(new File(args[0]), new File(args[1]));
            checker.printContainsActivityCheck(args[2]);
            checker.print4EyesCheck(args[2], args[3]);
            checker.printLeadsTo(args[2], args[3]);
        } catch (Exception e) {
            System.err.println("Error while parsing arguments or checking properties");
            e.printStackTrace();
        }
        //AristaFlowToPnmlConverter test = new AristaFlowToPnmlConverter(new File(args[0]));
        //test.printAllEntries();

    }

    private static void printhelp() {
        System.out.println("usage: AfTemplateCheck template-file prism-executable activity1 [activity2]");
        System.out.println("Checks if activity1 is present and if activity1 and activity2 are done by different persons");
        System.out.println("Checks if activity1 always leads to activity2");
    }

    public AfTemplateCheck(File template, File prismPath) throws Exception {
        try {
            parser = new AristaFlowToPnmlConverter(template);
            //parser.parseWithoutIfnet();
            net = parser.parse();
            this.prismPath = prismPath;
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            throw new Exception("Could not parse AristaFlow template");
        }
    }

    public boolean containsActivity(String ActivityName) {
        return parser.containsActivity(ActivityName);
    }

    public String getOriginator(String ActivityName) {
        return parser.getOriginator(ActivityName);
    }

    public boolean bySamePerson(String activityName1, String activityName2) {
        return parser.bySamePerson(activityName1, activityName2);
    }

    public void printContainsActivityCheck(String activityName) {
        System.out.print("Checking activity " + activityName + " present: ");
        String result = "no";
        if (containsActivity(activityName)) {
            result = "yes";
        }
        System.out.println(result);
    }

    public void print4EyesCheck(String activityName1, String activityName2) {
        try {
            if (containsActivity(activityName1) && containsActivity(activityName2)) {
                System.out.print("Checking activity " + activityName1 + " and " + activityName2 + " by different persons:");
                String result = "yes";

                if (bySamePerson(activityName1, activityName2)) {
                    result = "no";
                }
                System.out.println(result);
            } else {
                System.out.println("Could not check 4 Eyes principle");
            }
        } catch (NullPointerException e) {
            System.out.println("Could not check 4 Eyes principle");
        }
    }

    public void printLeadsTo(String activiy1, String activiy2) {
        try {
            CompliancePattern rule = leadsTo(activiy1, activiy2);
            System.out.print("Checking " + activiy1 + " leads to " + activiy2 + ": ");
            System.out.println(rule.getProbability());
            if (rule.getCounterExample() != null && !rule.getCounterExample().isEmpty()) {
                System.out.println("Counterexample: ");
                for (String s : rule.getCounterExample()) {
                    System.out.print(s + " ");
                }
            }

        } catch (PrismException ex) {
            System.out.println("Could not check leads-to. Reason: Prism reported exception");
            ex.printStackTrace();
        } catch (URISyntaxException ex) {
            System.out.println("Could not check leads-to. Reason: Could not load prism");
            ex.printStackTrace();
        }

    }

    private CompliancePattern leadsTo(String activity1, String activity2) throws PrismException, URISyntaxException {
        IFNetLeadsTo rule = new IFNetLeadsTo();
        rule.acceptInfoProfider(new IFNetInfoProvider(net));
        ArrayList<Parameter> test = rule.getParameters();
        test.get(0).setValue(ParameterTypeNames.TRANSITION);
        test.get(1).setValue(ParameterTypeNames.TRANSITION);
        test.get(0).getValue().setType(ParameterTypeNames.TRANSITION);
        test.get(1).getValue().setType(ParameterTypeNames.TRANSITION);
        test.get(0).getValue().setValue(enrichLabelInfo(activity1));
        test.get(1).getValue().setValue(enrichLabelInfo(activity2));
        rule.instantiate();
        rule.setFormalization();
        PRISM prism = new PRISM(net, prismPath);
        ArrayList<CompliancePattern> rules = new ArrayList<>();
        rules.add(rule);
        prism.run(rules);
        return rule;
    }

    private String enrichLabelInfo(String activity) {
        Object[] bla = net.getTransitions(activity).toArray();
        //System.out.println("Checking " + activity);
        if (bla[0] instanceof RegularIFNetTransition) {
            return ((AbstractIFNetTransition) bla[0]).getName() + "(" + activity + ")";
        }
        return activity + "()";
    }

    public void printAllTransitions() {
        for (AbstractIFNetTransition<IFNetFlowRelation> transitions : net.getTransitions()) {
            System.out.println(transitions.getLabel() + "(" + transitions.getName() + ")");
        }
    }

}
