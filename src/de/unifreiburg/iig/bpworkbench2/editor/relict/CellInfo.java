package de.unifreiburg.iig.bpworkbench2.editor.relict;

import java.io.Serializable;

import de.unifreiburg.iig.bpworkbench2.editor.soul.MXConstants;

public class CellInfo implements Serializable {

    //serialization is NECESSARY
    private static final long serialVersionUID = 193485709283274L;
    private String type = null, name = null;
    private int marks = 0;
    private double probability = 1.0, variance = 0.0, lambda = 0;
    private boolean immediate = false, blocked = false;

    public CellInfo() {
        this(null);
    }

    public CellInfo(String name) {
        this.type = MXConstants.CONTAINER;
        this.name = name;
    }

    public CellInfo(int marks) {
        this.type = MXConstants.PLACE;
        this.marks = marks;
    }

    public CellInfo(double variance, double lambda) {
        this.type = MXConstants.TRANSITION;
        this.variance = variance;
        this.lambda = lambda;
        this.immediate = (variance == 0.0);
    }

    public CellInfo(double variance, double lambda, double probability) {
        this.type = MXConstants.TRANSITION;
        this.variance = variance;
        this.lambda = lambda;
        this.probability = (probability > 1.0 || probability < 0) ? 0.0 : probability;
        this.immediate = (variance == 0.0);
    }

    public int getMarks() {
        return marks;
    }

    public double getVariance() {
        return variance;
    }

    public String getType() {
        return type;
    }

    public double getProbability() {
        return probability;
    }

    public double getLambda() {
        return lambda;
    }

    public String getName() {
        return name;
    }

    public boolean isImmediate() {
        return immediate;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setImmediate(boolean immediate) {
        this.immediate = immediate;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public void setMarks(int marks) {
        this.marks = (marks > 0) ? marks : 0;
    }

    public void setVariance(double variance) {
        this.variance = (variance > 0) ? variance : 0;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setProbability(double probability) {
        this.probability = (probability > 1.0 || probability < 0) ? 0.0 : probability;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isTransition() {
        return (type.equals(MXConstants.TRANSITION)) ? true : false;
    }

    public boolean isContainer() {
        return (type.equals(MXConstants.CONTAINER)) ? true : false;
    }

    public boolean isPlace() {
        return (type.equals(MXConstants.PLACE)) ? true : false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.type.equals(MXConstants.PLACE)) {
            for (int i = 0; i < marks && marks <= 5; i++) {
                sb.append("•");
                if ((i == 0 && marks == 3) || (i == 1 && marks > 3)) {
                    sb.append("\n");
                }
            }
            if (marks > 5) {
                sb.append(marks);
            }
        }
        if (this.type.equals(MXConstants.TRANSITION) && !this.immediate) {
            sb.append("p:");
            sb.append(probability);
        }
        if (this.type.equals(MXConstants.CONTAINER)) {
            sb.append(name);
        }
        return sb.toString();
    }

    public void setLambda(double lambda) {
        this.lambda = lambda;
    }
}
