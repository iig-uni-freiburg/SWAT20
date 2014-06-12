package de.uni.freiburg.iig.telematik.swat.prism.generator;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;

public class IFNetVerifier {

	//The IFNet which should be checked
	private IFNet IFNet;
	
    //The path to Prism
    //e.g. /home/boehr/prism-4.0.3-linux64/bin/prism    
    private String prismPath = null;

	//The absolute path to the file going to contain the prism IFNet model
    //e.g. /home/boehr/Documents/Prism/SimplePN/Example.pm
    private String modelFilePath = null;

    //The absolute path to the file going to containing the LTL formulas checked by prism
    //e.g. /home/boehr/Documents/Prism/SimplePN/Example.ltl
    private String propertyFilePath = null;

    //The absolute path to the file in which the model checking results from prism are stored
    //e.g. /home/boehr/Documents/Prism/SimplePN/result.txt
    private String resultFilePath = null;
    
    //The absolute path to the file in which the state space of the exported model (dot) gets stored
    //e.g. /home/boehr/Documents/Prism/SimplePN/modelGraph.dot
    private String exportFilePath = null ;
    
    
    
   
    
    
	public IFNetVerifier(IFNet IFNet) {
		this.IFNet = IFNet;
    }
    
  
    
    
    public List<PrismResult> verify(boolean exportStateSpace) throws ParameterException, IOException{
    	
		if (IFNet != null & prismPath != null & modelFilePath != null & propertyFilePath != null & resultFilePath != null) {
    		
    		
    		//1) Generate Prism model
			IFNetToPrismConverter converter = new IFNetToPrismConverter(IFNet);
			StringBuilder prismModel = converter.ConvertIFNetToPrism();
    		
    		// Create file containing the prism model   		 
    		BufferedWriter prismModelFileWriter = new BufferedWriter(new FileWriter(modelFilePath));
    		prismModelFileWriter.write(prismModel.toString());
    		prismModelFileWriter.flush();
    		prismModelFileWriter.close();
  		
    		    		
    		 //2) Generate Properties of Causal Places
			CausalPlaceLTLGenerator ltlGen = new CausalPlaceLTLGenerator(IFNet);
    		StringBuilder monoCausalLTLFormulas = ltlGen.generateMonoCausalPlaceLTL();
    		StringBuilder multiCausalLTLFormulas = ltlGen.generateMultiCausalPlaceLTL();
    		
    		
    		// Generate Properties of Reversed Causal Places
			ReversedCausalPlaceLTLGenerator ltlRevGen = new ReversedCausalPlaceLTLGenerator(IFNet);
    		StringBuilder monoReversedLTLFormulas = ltlRevGen.generateMonoReversedCausalPlaceLTL();
    		StringBuilder multiReversedLTLFormulas = ltlRevGen.generateMultiReversedCausalPlaceLTL();

    		
    		////3) Create file containing the ltl properties   		 
    		BufferedWriter ltlFileWriter = new BufferedWriter(new FileWriter(propertyFilePath));
    		ltlFileWriter.write(monoCausalLTLFormulas.toString());
    		ltlFileWriter.write(multiCausalLTLFormulas.toString());
    		ltlFileWriter.write(monoReversedLTLFormulas.toString());
    		ltlFileWriter.write(multiReversedLTLFormulas.toString());
    		ltlFileWriter.flush();
    		ltlFileWriter.close();
    		
    		
    	     //4) Run prism
    		 //Initialise the PrismRunner
            PrismRunner pr = new PrismRunner();           
            pr.setPrismFile(new File(prismPath));
            pr.setModelFile(new File(modelFilePath));
            pr.setPropertyFile(new File(propertyFilePath));
            pr.setResultFile(new File(resultFilePath));
            pr.setExportFile(new File(exportFilePath));
            pr.setVerbose(true);
            
            //Verify the properties
            pr.verifyProperties();
            
          //Export the model to a graphical representation if wanted
          //  if(exportStateSpace){            	
          //  	pr.exportModel2Dot();
          //  	pr.exportModel2Picture("svg");
          //  }
            
          //verification worked.
            //return the results
            List<PrismResult> resultList = pr.getResults();
    		return resultList;
    		
    		
    	}else{
    		//verification failed.
    		return null;
    	}
    	
    	    	
    }
  

   
	public String getPrismPath() {
		return prismPath;
	}

	public void setPrismPath(String prismPath) {
		this.prismPath = prismPath;
	}

	public String getModelFilePath() {
		
		return modelFilePath;
	}

	public void setModelFilePath(String modelFilePath) {
		this.modelFilePath = modelFilePath;
	}

	public String getPropertyFilePath() {
		return propertyFilePath;
	}

	public void setPropertyFilePath(String propertyFilePath) {
		this.propertyFilePath = propertyFilePath;
	}

	public String getResultFilePath() {
		return resultFilePath;
	}

	public void setResultFilePath(String resultFilePath) {
		this.resultFilePath = resultFilePath;
	}

	public String getExportFilePath() {
		return exportFilePath;
	}

	public void setExportFilePath(String exportFilePath) {
		this.exportFilePath = exportFilePath;
	}
	
	
}
