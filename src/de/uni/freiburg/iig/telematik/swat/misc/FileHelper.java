package de.uni.freiburg.iig.telematik.swat.misc;

public class FileHelper {
    
    public static void main(String args[]){
        System.out.println(runningFromJar());
    }

//		//command = "/bin/sh -c echo hallo > /tmp/test.txt";
//		String readableCommand = "run command: " + " " + command[0] + " " + command[1] + " " + command[2];
//		//final JOptionPane optionPane = new JOptionPane("run " + readableCommand, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
//		//int i = JOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(SwatTreeView.getInstance()), "run " + readableCommand);
//		int i = JOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(SwatTreeView.getInstance()), readableCommand, "delete?",
//				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//		System.out.println("USER: " + i);
//		if (i == 0) {
//			//user choice YES
//			runCommand(command);
//			return true;
//		}
                
    public static boolean runningFromJar(){
        
   String className = FileHelper.class.getName().replace('.', '/');
   String classJar =  
     FileHelper.class.getResource("/" + className + ".class").toString();
   if (classJar.startsWith("jar:")) {
     return true;
   }
   return false;
    }
}
