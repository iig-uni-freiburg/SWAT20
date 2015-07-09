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

import de.uni.freiburg.iig.telematik.swat.aristaFlow.AristaFlowToPnmlConverter;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author richard
 */
public class AfTemplateCheck {
    
    AristaFlowToPnmlConverter parser;
    
    public static void main(String[] args) throws Exception{
        String[] args2={"/tmp/af.template","Input Customer Data","Sachliche Prüfung"};
        args=args2;
        if(args.length==0||args.length < 1|| args.length>3){
            printhelp();
            System.exit(0);
        }
        AfTemplateCheck checker = new AfTemplateCheck(new File(args[0]));
        checker.printContainsActivityCheck(args[1]);
        checker.print4EyesCheck(args[1], args[2]);
    }
    
    private static void printhelp() {
        System.out.println("usage: AfTemplateCheck file activity1 [activity2]");
        System.out.println("Checks if activity1 is present and if activity1 and activity2 are done by different persons");
    }
    
    public AfTemplateCheck(File file) throws Exception {
        try {
            parser = new AristaFlowToPnmlConverter(file);
            parser.parseWithoutIfnet();
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            throw new Exception("Could not parse AristaFlow template");
        }
    }
    
    public boolean containsActivity (String ActivityName){
        return parser.containsActivity(ActivityName);
    }
    
    public String getOriginator(String ActivityName){
        return parser.getOriginator(ActivityName);
    }
    
    public boolean bySamePerson(String activityName1, String activityName2){
        return parser.bySamePerson(activityName1, activityName2);
    }
    
    public void printContainsActivityCheck(String activityName){
        System.out.print("Activity "+activityName+" present: ");
        String result ="no";
        if (containsActivity(activityName))
            result="yes";
        System.out.println(result);
    }
    
       public void print4EyesCheck(String activityName1, String activityName2){
           if(containsActivity(activityName1)&&containsActivity(activityName2)){
        System.out.print("Activity "+activityName1+" and "+activityName2+" by different persons:");
        String result ="yes";
        if (bySamePerson(activityName1, activityName2))
            result="no";
        System.out.println(result);}
           else
               System.out.println("Could not check 4 Eyes principle");
    }
    
    
}
