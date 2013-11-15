package de.uni.freiburg.iig.telematik.swat.editor.tree;

import java.awt.Component;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.tree.TreeNode;

public class PNTreeNode implements TreeNode {

    private String title;
    
    private Vector<TreeNode> children = new Vector<TreeNode>();
    private TreeNode parent = null;;

	private JTextField textfield =null;

	private PNTreeNodeType fieldType;
  
    
    public PNTreeNode(String title, PNTreeNodeType root) {
        this.title = title;
        this.fieldType = root;
    }
    
    public PNTreeNode(String title, PNTreeNodeType type, JTextField field) {
    	this(title, type);
        this.textfield  = field;
    }
    
    public void addChild(TreeNode child) {
        children.add(child);
    }
    
    public void setParent(TreeNode parent) {
        this.parent = parent;
    }
    
    @Override
    public Enumeration<TreeNode> children() {
        return children.elements();
    }
 
    @Override
    public boolean getAllowsChildren() {
        return true;
    }
 
    @Override
    public TreeNode getChildAt(int childIndex) {
        return children.elementAt(childIndex);
    }
 
    @Override
    public int getChildCount() {
        return children.size();
    }
 
    @Override
    public int getIndex(TreeNode node) {
        return children.indexOf(node);
    }
 
    @Override
    public TreeNode getParent() {
        return this.parent;
    }
 
    @Override
    public boolean isLeaf() {
        return (children.size() == 0);
    }
 
    public void setTitle(String title) {
        this.title = title;
    }
 
    public String getTitle() {
        return title;
    }
    
    public String toString() {
        return title;
    }
    
    public JTextField getTextfield() {
		return textfield;
	}

	public void setTextfield(JTextField textfield) {
		this.textfield = textfield;
	}
	
	public PNTreeNodeType getFieldType() {
		return fieldType;
	}

   
}