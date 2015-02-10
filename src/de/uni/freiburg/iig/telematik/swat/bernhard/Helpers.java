package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import de.uni.freiburg.iig.telematik.swat.lukas.patterns.factory.GuiParamValue;
import de.uni.freiburg.iig.telematik.swat.lukas.patterns.factory.GuiParameter;

/**
 * a class containing some help functions
 * @author bernhard
 *
 */
public class Helpers {

	/**
	 * returns the given JComponent in a JPanel with FlowLayout LEFT
	 * @param k
	 * @return
	 */
	public static JPanel jPanelLeft(JComponent k) {
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p.add(k);
		return p;
	}
/**
 * calculate the intersection of both lists
 * @param list1
 * @param list2
 * @return the intersection of the two lists
 */
	public static <T> List<T> Intersection(
			List<T> list1, List<T> list2) {
		List<T> list = new ArrayList<T>();

        for (T t : list1) {
            if(list2.contains(t)) {
                list.add(t);
            }
        }

        return list;
	}
	/**
	 * format a string so that after maximum n charaters there is a newline
	 * @param s the string to format
	 * @param n maximum number of characters in a row
	 * @return the formated string
	 */
	public static String formatString(String s, int n) {
		String result="";
		char arr[]=s.toCharArray();
		int count =0;
		for(int i=0; i < arr.length; i++) {
			if(count == n) {
				result+="\n";
				count=0;
			}
			if (arr[i] == '\n') {
				count=0;
			} else {
				count++;
			}
			result+=arr[i];
		}
		return result;
	}
	
	/**
	 * copy a list of pattern settings by value
	 * @param list
	 * @return
	 */
	public static List<PatternSetting> copyPatternSettings(List<PatternSetting> list) {
		ArrayList<PatternSetting> newList=new ArrayList<PatternSetting>();
		for(PatternSetting ps: list) {
			PatternSetting p2=ps.clone();
			newList.add(p2);
		}

		return newList;
	}
	/**
	 * copy a list of Parameters by value
	 * @param parameters the List of parameters to be copied
	 * @return
	 */
	
	public static List<GuiParameter> cloneParameterList(List<GuiParameter> parameters) {
		ArrayList<GuiParameter> newList=new ArrayList<GuiParameter>();
		for(GuiParameter p:parameters) {
			GuiParameter newP=new GuiParameter(p.getTypes(),p.getMultiplicity(),p.getName());
			newP.setValue((ArrayList<GuiParamValue>) copyParamValueList(p.getValue()));
			newList.add(newP);
		}
		return newList;
	}

	public static List<GuiParamValue> copyParamValueList(List<GuiParamValue> values) {
		ArrayList<GuiParamValue> newList=new ArrayList<GuiParamValue>();
		for(GuiParamValue pv:values) {
			newList.add(new GuiParamValue(pv.getOperandName(),pv.getOperandType()));
		}
		return newList;
	}
	/**
	 * center a window
	 * @param window the window to be centered
	 */
	public static void centerWindow(JFrame window) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = window.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		window.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
	}
}
