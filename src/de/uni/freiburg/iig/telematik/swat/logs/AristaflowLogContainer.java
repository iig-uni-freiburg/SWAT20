/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.freiburg.iig.telematik.swat.logs;

import de.invation.code.toval.debug.SimpleDebugger;

/**
 *
 * @author stocker
 */
public class AristaflowLogContainer extends AbstractLogModelContainer {

    public AristaflowLogContainer(String basePath) {
        super(basePath);
    }

    public AristaflowLogContainer(String basePath, SimpleDebugger debugger) {
        super(basePath, debugger);
    }
    
    @Override
    protected SwatLogType getLogType() {
        return SwatLogType.Aristaflow;
    }
    
    
}
