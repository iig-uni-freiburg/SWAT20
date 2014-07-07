package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
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
}