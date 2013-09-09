package de.unifreiburg.iig.bpworkbench2.editor.soul;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

//import math.generators.LCGenerator;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;
//import math.simulation.Event;

public class Utils {

//    private static LCGenerator generator = new LCGenerator(0.5, 211321, 21765, 41);
    public static final Random random = new Random();
    public static final DecimalFormat numberFormat = new DecimalFormat();
//    public static final Comparator comparator = new Comparator();

    static {
        numberFormat.setMaximumFractionDigits(5);
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        numberFormat.setDecimalFormatSymbols(dfs);
    }

//    private static class Comparator implements java.util.Comparator<Event> {
//
//        public int compare(Event o1, Event o2) {
//            Double t1 = o1.getTime();
//            Double t2 = o2.getTime();
//            return t1.compareTo(t2);
//        }
//    };

//    public static double generate(double variance, double lambda) {
//        Flow flow;
//        if (variance == 1.0 / 3) {
//            flow = new EvenFlow(lambda, generator);
//        } else if (variance < 1.0) {
//            flow = new ErlangFlow(lambda, generator, variance);
//        } else if (variance == 1.0) {
//            flow = new ExpFlow(lambda, generator);
//        } else {
//            flow = new HyperExpFlow(lambda, generator, variance);
//        }
//        return flow.next();
//    }

    public static boolean isTransitionAllowed(int[] diRow, int[] marking) {
        boolean result = true;
        for (int i = 0; i < diRow.length; i++) {
            if (diRow[i] > 0) {
                result &= diRow[i] <= marking[i];
            }
        }
        return result;
    }

    public static boolean isVerticeDuplicate(ArrayList<NodeInfo> list, NodeInfo info) {
        for (NodeInfo listInfo : list) {
            if (listInfo.getName().equals(info.getName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isVerticeImmediate(DataHolder dataHolder, int[] marking) {
        for (int i = 0; i < dataHolder.getImmediates().length; i++) {
            if (dataHolder.getImmediates()[i]) {
                int[] row = dataHolder.getMatrixDi()[i];
                if (Utils.isTransitionAllowed(row, marking)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isVerticeTerminal(DataHolder dataHolder, NodeInfo info) {
        for (int i = 0; i < dataHolder.getMatrixDi().length; i++) {
            if (Utils.isTransitionAllowed(dataHolder.getMatrixDi()[i], info.getMarking())) {
                return false;
            }
        }
        return true;
    }

    public static int[] nextMarking(int[] diRow, int[] dqRow, int[] m) {
        int[] result = new int[m.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = m[i] - diRow[i] + dqRow[i];
        }
        return result;
    }

    public static int[] nextMarking(DataHolder dataHolder, int i) {
        int[] m = dataHolder.getMarking();
        int[] diRow = dataHolder.getMatrixDi()[i];
        int[] dqRow = dataHolder.getMatrixDq()[i];
        return nextMarking(diRow, dqRow, m);
    }

    public static String arrayToString(int[] array) {
        StringBuilder str = new StringBuilder();
        for (int i : array) {
            if (i >= Integer.MAX_VALUE >> 2) {
                str.append("ω");
            } else {
                str.append(i);
            }
            str.append(" ");
        }
        return str.toString().trim();
    }

    public static int[] stringToArray(String string) {
        StringTokenizer st = new StringTokenizer(string);
        int[] array = new int[st.countTokens()];
        for (int i = 0; i < array.length; i++) {
            String str = st.nextToken();
            if (str.equals("ω")) {
                array[i] = Integer.MAX_VALUE >> 1; //rather huge and overflow is impossible
            } else {
                array[i] = Integer.parseInt(str);
            }
        }
        return array;
    }

    public static boolean isFullCovering(String name) {
        StringTokenizer st = new StringTokenizer(name);
        do {
            if (!st.nextToken().equals("ω")) {
                return false;
            }
        } while (st.hasMoreTokens());
        return true;
    }

    public static NodeInfo handleCoverage(ArrayList<NodeInfo> list, NodeInfo info) {
        for (NodeInfo infoEx : list) {
            boolean needChange = true;
            boolean duplicateFlag = true;
            boolean[] positions = new boolean[info.getMarking().length];
            int[] existing = infoEx.getMarking();
            int[] current = info.getMarking();
            for (int i = 0; i < existing.length; i++) {
                positions[i] = current[i] > existing[i];
                needChange = (current[i] < existing[i]) ? false : needChange;
                duplicateFlag &= current[i] == existing[i];
            }
            if (needChange && !duplicateFlag) {
                for (int i = 0; i < positions.length; i++) {
                    current[i] = (positions[i]) ? Integer.MAX_VALUE / 2 : current[i];
                }
                info = new NodeInfo(current);
                info.setCovering(true);
                return info;
            }
        }
        return info;
    }

    public static boolean isMarkingDuplicate(ArrayList<String> existing, String marking) {
        boolean result = false;
        for (String markingEx : existing) {
            if (markingEx.equals(marking)) {
                result = true;
            }
        }
        return result;
    }

    public static int getMarkingIndex(ArrayList<String> existing, String marking) {
        for (int i = 0; i < existing.size(); i++) {
            if (marking.equals(existing.get(i))) {
                return i;
            }
        }
        return -1;
    }

//    public static Event getEventForTime(ArrayList<Event> list, double time) {
//        for (Event event : list) {
//            double curTime = event.getTime();
//            if (time >= curTime) {
//                return event;
//            }
//        }
//        return null; // never happens
//    }
//
//    public static boolean sameParents(int[] parent1, int[] parent2) {
//        for (int i = 0; i < parent1.length; i++) {
//            if (parent1[i] != parent2[i]) {
//                return false;
//            }
//        }
//        return true;
//    }

    public static double round(double number) {
        return Math.round(number * 1000000) / 1000000.0;
    }
}
