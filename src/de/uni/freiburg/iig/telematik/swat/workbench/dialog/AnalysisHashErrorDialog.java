package de.uni.freiburg.iig.telematik.swat.workbench.dialog;

import javax.swing.JOptionPane;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponentType;
import de.uni.freiburg.iig.telematik.swat.workbench.Workbench;

public class AnalysisHashErrorDialog {

    protected Object[] answers = {"Yes", "No"};

    protected String message;

    protected String title = "Update Analysis?";

    public AnalysisHashErrorDialog(SwatComponentType type) {

        switch (type) {
            case ARISTAFLOW_LOG:
            case MXML_LOG:
            case XES_LOG:
                message = "The log model has changed since the last analysis. Update results?";
                break;
            case PETRI_NET:
                message = "The petri net has changed since the last analysis. Update results?";
            default:
                throw new ParameterException("Dialog not possible for " + type);
        }
    }

    public boolean showUpdateDialog() throws Exception {
        int n = JOptionPane.showOptionDialog(Workbench.getInstance(), message, title, JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, answers, answers[1]);

        if (n == JOptionPane.YES_OPTION) {
            return true;
        }
        return false;

    }

    public static void main(String args[]) throws Exception {
        AnalysisHashErrorDialog test = new AnalysisHashErrorDialog(SwatComponentType.XES_LOG);
        System.out.println(test.showUpdateDialog());
    }

}
