/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni.freiburg.iig.telematik.swat.analysis.prism;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 * The class allows to run the model checker Prism from within java.
 * It is not possible to set the max. memory for Prism via this interface.
 * For information regadring out of memory exceptions of Prism and how
 * to increase available memory see:
 * http://www.prismmodelchecker.org/manual/ConfiguringPRISM/AllOnOnePage
 * @author Frank Böhr (boehr"at"iig.uni-freiburg.de)
 */
public class PrismRunner {

    
    //The command name for the Processbuilder i.e. the path to Prism
    //e.g. /home/boehr/prism-4.0.3-linux64/bin/prism    
    private String prismPath = "Prism path not set!";
    //The commandline arguments for the executed process
    private List<String> arguments = new LinkedList<String>();

    //The absolute path to the file containing the prism model
    //e.g. /home/boehr/Documents/Prism/SimplePN/Example.pm
    private String modelFilePath="Model file path not set!";

    //The absolute path to the file containing the properties to be checked by prism
    //e.g. /home/boehr/Documents/Prism/SimplePN/Example.ltl
    private String propertyFilePath="Property file path not set!";

    //The absolute path to the file in which the model checking results are stored
    //e.g. /home/boehr/Documents/Prism/SimplePN/result.txt
    private String resultFilePath="Result file path not set!";
    
    //The absolute path to the file in which the exported model (dot) gets stored
    //e.g. /home/boehr/Documents/Prism/SimplePN/modelGraph.dot
    private String exportFilePath="Export file path not set!";

    //The outputs of started processes is not shown if this variable is set to false.
    private boolean verbose = false;
    
    
    /**
     * This method adds all cli parameters in the right order to a list which are needed to perform model checking with prism. E.g.:
     * /home/boehr/prism-4.0.3-linux64/bin/prism -fixdl /home/boehr/Documents/Prism/SimplePN/Example.pm /home/boehr/Documents/Prism/SimplePN/Example.ltl -exportresults /home/boehr/Documents/Prism/SimplePN/result.txt
     */
    private void setModelCheckingArguments(){
                
        arguments = new LinkedList<String>();
        
        //add the path to prism
        arguments.add(prismPath);
        
        //automatically add self loops to deadlock states to enable analysis
        arguments.add("-fixdl");
        
        //add the path to the model which shoud be checked
        arguments.add(modelFilePath);
        
        //add the path to the properties which shoud be checked
        arguments.add(propertyFilePath);
        
        //Tell prism to export the results to a file
        arguments.add("-exportresults");
        
        //add the path to the file where the results get stored
        arguments.add(resultFilePath);
    }

    
    /**
     * This method adds all cli parameters in the right order to a list which are needed to export the model to dot. E.g.:
     * /home/boehr/prism-4.0.3-linux64/bin/prism -fixdl /home/boehr/Documents/Prism/SimplePN/Example.pm -exporttransdotstates /home/boehr/Desktop/test.dot
     */
    private void setModelExportArguments(){
        
        
        arguments = new LinkedList<String>();
        
        //add the path to prism
        arguments.add(prismPath);
        
        //automatically add self loops to deadlock states to enable analysis
        arguments.add("-fixdl");
        
        //add the path to the model which shoud be checked
        arguments.add(modelFilePath);
       
        //Tell prism to export the model to a file in dot format
        arguments.add("-exporttransdotstates");
        
        //add the path to the file where the exported model gets stored
        arguments.add(exportFilePath);
        
    }
    
    /**
     * This method adds all cli parameters in the right order to a list which are needed to convert a dot file to e.g. a jpeg.
     * Example: dot -Tjpeg ExportedModel.dot -o ExportedModel.jpeg
     * @param format to wich the model should be converted
     */
    private void setExportToPictureArguments(String format){
                 
        arguments = new LinkedList<String>();
        
        //the process which should be started is dot
        arguments.add("dot");
        
        //The intended output format is jpeg
        arguments.add("-T"+format);
        
        //The file which should be transformed to jpeg is a dot file
        arguments.add(exportFilePath);
       
        //The following file is output
        arguments.add("-o");
        
        
        //The full path of of the jpeg file
        String pathWithoutExtension = new StringTokenizer(exportFilePath, ".").nextToken();
        arguments.add(pathWithoutExtension+"." + format);
                        
    }
    
    
    
    /**
     * This method starts Prism with the previously set parameters for verification.
     * @return returns true if Prism is run successfully and false otherwise. The
     * model checking results are stored in the user specified file.
     */    
    public boolean verifyProperties(){
        
        //set commanline arguments for verification
        this.setModelCheckingArguments();
        
        //run prism
        return runProcess();    
    }
    
    
    /**
     * This method starts Prism with the previously set parameters for exporting the model.
     * @return returns true if Prism is run successfully and false otherwise.
     */    
    public boolean exportModel2Dot(){
        
        //set commanline arguments for exporting the model
        setModelExportArguments();
        
        //run prism
        return runProcess();
    }
    

    
    /**
     * This method starts Prism with the previously set parameters for exporting the model to dot and converts the dot
     * file to a picture additinally. Dot (included in graphviz) must be installed.
     * @param  format contains the format of the picture to which the model should be exported.
     *          Possible formats are e.g.:bmp, eps, jpg, pdf, svg ...
     *          See http://www.graphviz.org/doc/info/output.html for a list of all supprted formats.
     * @return returns true if Prism is run successfully and false otherwise.
     */    
    public boolean exportModel2Picture(String format){
        
        //Export to dot first
        boolean exportToDotOK = exportModel2Dot();
        
        //set the arguments to export to jpeg
        setExportToPictureArguments(format);       

        //run dot
        boolean dotConversionOK = runProcess();
        
        //run prism
        return (exportToDotOK & dotConversionOK);
    }
    
    
    
    /**
     * This method reads the results from the result file which gets generated from Prism.
     * @return List<PrismResult> contains the results in the same order as the result file.
     */
    public List<PrismResult> getResults(){
       
        //The result file
        File resultFile = new File(resultFilePath);
        List<PrismResult> resultList = new LinkedList<PrismResult>();
        
        if(resultFile.exists()){
            BufferedReader resultReader = null;
            try {
                resultReader = new BufferedReader(new FileReader(resultFile));
                String line;
                while ( (line = resultReader.readLine()) != null){
                 
                    //The resultfile contains tree lines per formula
                    //followed by an empty line e.g.:
                    //P=? [ G ((p2_Red>0&p2_Black>0&p3_Black<1)=>(t1=1|t4=1)) ]:
                    //Result
                    //0.6666666666666667
                    
                    //Create a result object
                    PrismResult pr = new PrismResult();
                    
                    if(!line.equals("Result")){
                    //line contains the current formula with a ":" at the end
                    //which gets removed
                    pr.setFormula(line.trim().substring(0, line.length()-1));
                    //red the line "Result"
                    line = resultReader.readLine();
                    //read the value
                    line = resultReader.readLine();
                    pr.setResult(Double.parseDouble(line));
                    //read the space between restults
                    line = resultReader.readLine();
                    
                    //All three lines are read now
                    //Add the result to the result list
                    resultList.add(pr);
                    
                    }else{
                    	pr.setFormula(line.trim());
                    	//read the value
                        line = resultReader.readLine();
                        pr.setResult(Double.parseDouble(line));
                        resultList.add(pr);
                    }
                                    
                }
            } catch (IOException ex) {
                Logger.getLogger(PrismRunner.class.getName()).log(Level.SEVERE, null, ex);
            }  finally {
                try {
                    resultReader.close();
                } catch (IOException ex) {
                    Logger.getLogger(PrismRunner.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        
        return resultList;
    }
    
    
    


     /**
     * This method starts a process with the previously set parameters (stored in variable arguments).
     * @return returns true if Prism is run successfully and false otherwise.
     */
    private boolean runProcess(){
        
        //create the process builder which executes a process e.g. prism or dot
    	//ProcessBuilder processBuilder = new ProcessBuilder(arguments);
        ProcessBuilder processBuilder = new ProcessBuilder(arguments);
        String workingDirectory = prismPath.substring(0, prismPath.length()-10);
        processBuilder.directory(new File(workingDirectory));

        //try to execute the process e.g. prism or dot
        try {
            //This line executes the command line argument
            Process process = processBuilder.start();
     
            //If there is any error or output to the commandline it must be read.
            //The programm will halt and wait forever otherwise
            BufferedReader ibr = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader ebr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String outLine = "";            
            String errorLine = "";                        
            while (((outLine = ibr.readLine()) != null) || ((errorLine = ebr.readLine()) != null)) {
                
                //Consume outputs to stdout
                if(outLine!=null){if(verbose){System.out.println(outLine);}}
                
                //Consume outputs to stderr
                if(errorLine!=null){if(verbose) {System.out.println(errorLine);}}
            }
            
            //Wait for the process to complete!
            process.waitFor();
            
            //clean up!
            //this is needed because of a bug in java.lang.processbuilder
            //see: http://kylecartmell.com/?p=9
            process.getErrorStream().close();
            process.getInputStream().close();
            process.getOutputStream().close();
            process.destroy();
            ibr.close();
            ebr.close();

        } catch (Exception ex) {
            System.out.println("Exeption in PrismRunner");
            Logger.getLogger(PrismRunner.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        return true;        
    }
    
    
    
    /**
     * This method returns the model which gets checked by prism.
     * @return The file containing the model if the file exists and null otherwise.
     */
    public File getModelFile() {
        File mFile = new File(modelFilePath);
        if(mFile.exists()){
            return mFile;
        }else{
            return null;
        }        
    }

    
    /**
     * This method sets the file containing the model to be checked if the file exists.
     * @param modelFile File containing the model for Pism.
     * @return true if the file exists false otherwise.
     */
    public boolean setModelFile(File modelFile) {        
        if(modelFile.exists()){
            this.modelFilePath = modelFile.getAbsolutePath();
            return true;
        }else{
            return false;
        }
    }

    
    
    /**
     * This method returns the file from which prism is started (exe or shell script).
     * @return The file if the file exists and null otherwise.
     */
    public File getPrismFile() {
        File prismFile = new File(prismPath);
        
        if(prismFile.exists()){
            return prismFile;
        }else{
            return null;
        }        
    }

     /**
     * This method sets file from which prism is started if the file exists.
     * @param prismFile containing the the Pism executable.
     * @return true if the file exists false otherwise.
     */
    public boolean setPrismFile(File prismFile) {
        
        if(prismFile.exists()){
            this.prismPath = prismFile.getAbsolutePath();
            return true;
        }else{
            return false;
        }
    }

     /**
     * This method returns the file containing the properties which gets checked by prism.
     * @return The file containing the properties if the file exists and null otherwise.
     */
    public File getPropertyFile() {
        File pFile = new File(propertyFilePath);
        
        if(pFile.exists()){
            return pFile;
        }else{
            return null;
        }        
    }

    
    /**
     * This method sets file containing the properties to be checked if the file exists.
     * @param propertyFile the file contaioning the properties.
     * @return ture if the file exists false otherwise.
     */
    public boolean setPropertyFile(File propertyFile) {
        
        if(propertyFile.exists()){
            this.propertyFilePath = propertyFile.getAbsolutePath();
            return true;
        }else{
            return false;
        }
    
    }

     /**
     * This method returns the file containing the results which got created by prism
     * @return The file containing the results if the file exists and null otherwise.
     */
    public File getResultFile() {
        File rFile = new File(resultFilePath);
        
        if(rFile.exists()){
            return rFile;
        }else{
            return null;
        }       
    }

    
    
    /**
     * This method sets the file where the results of running prism are stored.
     * The method tries to create the file if it does not exist.
     * @param resultFile The file where the results from running Prism should be stored.
     * @return True if setting the file was successfull, false otherwise.
     */
    public boolean setResultFile(File resultFile) {        
        if(resultFile.exists()){
            this.resultFilePath =resultFile.getAbsolutePath();
            return true;
        }else{
            try {
                if(resultFile.createNewFile()){
                    this.resultFilePath=resultFile.getAbsolutePath();
                    return true;
                }else{
                    return false;
                }
            } catch (IOException ex) {                
                Logger.getLogger(PrismRunner.class.getName()).log(Level.SEVERE, null, ex);                
                return false;
            }                        
        }
    }

   
     /**
     * This method sets the file where the exported prism model gets stored.
     * The method tries to create the file if it does not exist.
     * @param exportFile The file where the where the exported model gets stored.
     * @return True if setting the file was successfull, false otherwise.
     */
    public boolean setExportFile(File exportFile) {        
        if(exportFile.exists()){
            this.exportFilePath =exportFile.getAbsolutePath();
            return true;
        }else{
            try {
                if(exportFile.createNewFile()){
                    this.exportFilePath=exportFile.getAbsolutePath();
                    return true;
                }else{
                    return false;
                }
            } catch (IOException ex) {                
                Logger.getLogger(PrismRunner.class.getName()).log(Level.SEVERE, null, ex);                
                return false;
            }                        
        }
    }

    /**
     * The command line output of the Prism is consumed by prismRunner if prismRunner is not verbose. 
     * It is rediredcted to the commandline otherwise.
     * @return ture in verbose mode.
     */
    public boolean isVerbose() {
        return verbose;
    }

    /**
     * The command line output of the Prism is consumed by prismRunner if prismRunner is not set verbose. 
     * It is rediredcted to the commandline otherwise.
     * @param verbose true sets prismRunner to verbose mode.
     */
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }


    

    
    /**
     * An example how to use the PrismRunner.
     * @param args The arguments are not used
     */
    public static void main(String[] args){

        //Initialise the PrismRunner
        PrismRunner pr = new PrismRunner();
//        pr.setPrismFile(new File("/home/boehr/prism-4.0.3-linux64/bin/prism.bat"));
//        pr.setModelFile(new File("/home/boehr/Documents/Prism/SimplePN/Example.pm"));
//        pr.setPropertyFile(new File("/home/boehr/Documents/Prism/SimplePN/Example.ltl"));
//        pr.setResultFile(new File("/home/boehr/Documents/Prism/SimplePN/result.txt"));
//        pr.setExportFile(new File("/home/boehr/Documents/Prism/SimplePN/ExportedModel.dot"));
//        
        
        pr.setPrismFile(new File("C:\\Program Files\\prism-4.0.3\\bin\\prism.bat"));
        pr.setModelFile(new File("C:\\Users\\Andres\\Desktop\\Comp\\Example.pm"));
        pr.setPropertyFile(new File("C:\\Users\\Andres\\Desktop\\Comp\\Example.ltl"));
        pr.setResultFile(new File("C:\\Users\\Andres\\Desktop\\Comp\\result.txt"));
        pr.setExportFile(new File("C:\\Users\\Andres\\Desktop\\Comp\\ExportedModel.dot"));        
        //pr.setVerbose(true);
        
        
        //Verify the properties
        pr.verifyProperties();
        
        //Export the model to a graphical representation
        //pr.exportModel2Dot();
        //pr.exportModel2Picture("jpg");
        
        
        //Read the results
        List<PrismResult> resultList = pr.getResults();
        for(PrismResult r : resultList){
           System.out.println(r);            
        }
        
        
        
        
    }


    
    
 
}
