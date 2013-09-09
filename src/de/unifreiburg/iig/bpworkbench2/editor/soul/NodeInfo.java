package de.unifreiburg.iig.bpworkbench2.editor.soul;

import java.io.Serializable;
import javax.swing.tree.DefaultMutableTreeNode;

//needs revision
public class NodeInfo implements Serializable {

    //serialization is NECESSARY
    private static final long serialVersionUID = -74784948383838L;
    private DefaultMutableTreeNode node;
    private String fullPath = "", name = "", description = "";
    private boolean showDescriptions = true, immediate = false,
            duplicate = false, covering = false, fullCovering = false,
            terminal = false;

    public NodeInfo(String name) {
        this.name = name;
        this.showDescriptions = Properties.getInstance().isTreeShowDescriptions();
        this.fullCovering = Utils.isFullCovering(name);
    }

    public NodeInfo(int[] array) {
        this.name = Utils.arrayToString(array);
        this.showDescriptions = Properties.getInstance().isTreeShowDescriptions();
        this.fullCovering = Utils.isFullCovering(name);
    }

    public DefaultMutableTreeNode getNode() {
        return node;
    }

    public void setNode(DefaultMutableTreeNode node) {
        this.node = node;
    }

    public void addToPath(String str) {
        fullPath = str + " " + fullPath;
    }

    private void validateDescription() {
        StringBuilder sb = new StringBuilder("");
        if (immediate) {
            sb.append(" immediate");
        }
        if (duplicate) {
            sb.append(" duplicate");
        }
        if (covering) {
            sb.append(" covering");
        }
        if (terminal) {
            sb.append(" terminal");
        }
        description = sb.toString();
    }

    public void setType(boolean immediate, boolean duplicate, boolean terminal) {
        this.immediate = immediate;
        this.duplicate = duplicate;
        this.terminal = terminal;
    }

    public int[] getMarking() {
        return Utils.stringToArray(name);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getFullPath() {
        return fullPath;
    }

    public boolean isDuplicate() {
        return duplicate;
    }

    public boolean isImmediate() {
        return immediate;
    }

    public boolean isCovering() {
        return covering;
    }

    public boolean isTerminal() {
        return terminal;
    }

    public boolean isFullCovering() {
        return fullCovering;
    }

    public void setImmediate(boolean immediate) {
        this.immediate = immediate;
    }

    public void setCovering(boolean covering) {
        this.covering = covering;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("");
        if (!fullPath.equals("") && showDescriptions) {
            sb.append("(");
            sb.append(fullPath.trim());
            sb.append(") ");
        }
        sb.append(" [");
        sb.append(name);
        sb.append("]");
        validateDescription();
        if (!description.equals("") && showDescriptions) {
            sb.append(": ");
            sb.append(description.trim());
        }
        return sb.toString();
    }
}
