package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.uni.freiburg.iig.telematik.swat.lukas.OperandType;
import de.uni.freiburg.iig.telematik.swat.lukas.ParamValue;
import de.uni.freiburg.iig.telematik.swat.lukas.Parameter;

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
 * @return
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
	public static ParamValue getFirst(ArrayList<ParamValue> arrayList) {
		return arrayList.iterator().next();
	}
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
	 * create a list of JLabels that will later be added to an
	 * AnalyzePanel. Some parameters might be to long so they will
	 * be breaken off. a JLabel does not accept a newline character
	 * @param ps
	 * @param transitionsReverse
	 * @return
	 */
	public static List<JLabel> getLabelListForPatternSetting(PatternSetting ps, HashMap<String,String> transitionsReverse) {
		ArrayList<JLabel> labels=new ArrayList<JLabel>();
		for(Parameter para: ps.getParameters()) {
			String paraString=para.getName()+": ";
			int count=0;
			boolean labelsLeft=false;
			// display the values

			for(int i=0; i < para.getValue().size(); i++) {
				ParamValue val=para.getValue().get(i);
				labelsLeft=true;
				if(count > 0) {
					labels.add(new JLabel(paraString));
					// move it to the right
					paraString="    ";
					count=0;
					labelsLeft=false;
				}
				if(val.getOperandType()==OperandType.STATEPREDICATE) {
					
					String conjunctions[]=val.getOperandName().split(" & ");
					int conjunction_count=0;
					for(int j=0; j < conjunctions.length; j++) {
						conjunction_count++;
						labelsLeft=true;
						paraString+=conjunctions[j];
						if(j < conjunctions.length -1) {
							paraString+=" & ";
						}
						if(conjunction_count==3) {
							labels.add(new JLabel(paraString));
							// move it to the right
							paraString="    ";
							conjunction_count=0;
							labelsLeft=false;
						}
					}
					
				} else {
					paraString+=transitionsReverse.get(val.getOperandName());
					count++;
				}
				if(i < para.getValue().size() -1) {
					paraString+=", ";
				}
				// maximum 2 values in a row
				if(count==2) {
					labels.add(new JLabel(paraString));
					// move it to the right
					paraString="    ";
					count=0;
					labelsLeft=false;
				}
			}
			if(labelsLeft) {
				labels.add(new JLabel(paraString));
			}
		}
		return labels;
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
	 * copy a list of parameters by value
	 * @param parameters
	 * @return
	 */
	
	public static List<Parameter> cloneParameterList(List<Parameter> parameters) {
		ArrayList<Parameter> newList=new ArrayList<Parameter>();
		for(Parameter p:parameters) {
			Parameter newP=new Parameter(p.getTypes(),p.getMultiplicity(),p.getName());
			newP.setValue((ArrayList<ParamValue>) copyParamValueList(p.getValue()));
			newList.add(newP);
		}
		return newList;
	}

	public static List<ParamValue> copyParamValueList(List<ParamValue> values) {
		ArrayList<ParamValue> newList=new ArrayList<ParamValue>();
		for(ParamValue pv:values) {
			newList.add(new ParamValue(pv.getOperandName(),pv.getOperandType()));
		}
		return newList;
	}
}
