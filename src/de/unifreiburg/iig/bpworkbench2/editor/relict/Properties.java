package de.unifreiburg.iig.bpworkbench2.editor.relict;

import java.io.*;

import de.unifreiburg.iig.bpworkbench2.editor.soul.MXConstants;

public class Properties implements Serializable {

    private static final long serialVersionUID = 123432432432435l;
    private static Properties instance = null;
    private String lastDir = System.getProperty("user.dir");
    private boolean treeSkipImmediates = true, treeShowDescriptions = true, divide = true;
    private double deltaT = 0.001;
    private double t0 = 0;
    private int tests = 50000;

    private Properties() {
        //init block
    }

    public static Properties getInstance() {
        if (instance == null) {
            try {
                FileInputStream fis = new FileInputStream(MXConstants.CONFIG_FILE);
                ObjectInputStream oin = new ObjectInputStream(fis);
                instance = (Properties) oin.readObject();
            } catch (Exception ex) {
                instance = new Properties();
            }
        }
        return instance;
    }

    public double getDeltaT() {
        return deltaT;
    }

    public double getT0() {
        return t0;
    }

    public int getTests() {
        return tests;
    }

    public String getLastDir() {
        return lastDir;
    }

    public boolean isTreeShowDescriptions() {
        return treeShowDescriptions;
    }

    public boolean isTreeSkipImmediates() {
        return treeSkipImmediates;
    }

    public void setTreeSkipImmediates(boolean treeSkipImmediates) {
        this.treeSkipImmediates = treeSkipImmediates;
    }

    public void setTreeShowDescriptions(boolean treeShowDescriptions) {
        this.treeShowDescriptions = treeShowDescriptions;
    }

    public void setTests(int tests) {
        this.tests = tests;
    }

    public void setLastDir(String lastDir) {
        this.lastDir = lastDir;
    }

    public void setDeltaT(double deltaT) {
        this.deltaT = deltaT;
    }

    public void setT0(double t0) {
        this.t0 = t0;
    }

    public void setDivide(boolean divide) {
        this.divide = divide;
    }

    public boolean isDivide() {
        return divide;
    }
}
