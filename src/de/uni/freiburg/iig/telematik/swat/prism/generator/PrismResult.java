/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.freiburg.iig.telematik.swat.prism.generator;




/**
 *
 * @author boehr
 */
public class PrismResult {
    
    //The formula which got checked
    private String formula = "no formula entered!";
    
    //The resulting probability
    private double result  = -1.0;

    
    
    
    public PrismResult() {
    }

    public PrismResult(String formula, double result) {
        this.formula = formula;
        this.result = result;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

    //This to string method recreates the format in the result file created by prism
    @Override
    public String toString() {
    	
    	if(!formula.equals("Result")){
    	
    		return formula + ":" + "\n" +
    				"Result" + "\n" +
    				result+ "\n";
    	}else{
    		return formula + ":" + "\n" +
    				result+ "\n";
    		
    	}
    	
    }
    
    
    
    
}
