package de.unifreiburg.iig.bpworkbench2.helper;

public class JVMUsage {
	private static Runtime runtime = Runtime.getRuntime();
	public static int getMbUsed() {
		//test JVM memory usage before initiliazing in order to roughly estimate History-Size
        int mb = 1024*1024;
        
        //Getting the runtime reference from system
        
        return (int) ((runtime.totalMemory() - runtime.freeMemory()) / mb);
	}
	public static long getByteUsed(){
		return  (runtime.totalMemory() - runtime.freeMemory());
	}
}
