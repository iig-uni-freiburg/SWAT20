package de.uni.freiburg.iig.telematik.swat.editor.tree;

import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class PNTreePath extends TreePath{


	public PNTreePath(TreeNode[] path) {
		super(path);
	}

	@Override	
    public Object[] getPath() {
		System.out.println("HETPATHCALLED");
        Object[] path = new Object[getPathCount()];
        for (int i = 0; i < path.length; i++) {
            path[i] = getPathComponent(i);
            System.out.println(i+"#" + getPathComponent(i));
        }
        return path;
    }

}
