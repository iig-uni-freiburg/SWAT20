package de.uni.freiburg.iig.telematik.swat.editor.tree;

import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;

import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperty;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PropertiesView.PropertiesField;

public class PNTreeNode extends DefaultMutableTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6517382720076787324L;

	private PropertiesField textfield;

	private PNTreeNodeType fieldType;
    
    public PNProperty getPropertyType() {
		return textfield.getPNProperty();
	}

	public PNTreeNode(String title, PNTreeNodeType type) {
        super(title);
        this.fieldType = type;
        
    }
    
    public PNTreeNode(String title, PNTreeNodeType type, PropertiesField field) {
    	super(title);
    	this.fieldType = type;
        this.textfield  = field;
    }
//    
//    public void addChild(TreeNode child) {
//        children.add(child);
//    }
//    
//    public void setParent(TreeNode parent) {
//        this.parent = parent;
//    }
//    
//    @Override
//    public Enumeration<TreeNode> children() {
//        return children.elements();
//    }
// 
//    @Override
//    public boolean getAllowsChildren() {
//        return true;
//    }
// 
//    @Override
//    public TreeNode getChildAt(int childIndex) {
//        return children.elementAt(childIndex);
//    }
// 
//    @Override
//    public int getChildCount() {
//        return children.size();
//    }
// 
//    @Override
//    public int getIndex(TreeNode node) {
//        return children.indexOf(node);
//    }
// 
//    @Override
//    public TreeNode getParent() {
//        return this.parent;
//    }
// 
//    @Override
//    public boolean isLeaf() {
//        return (children.size() == 0);
//    }
// 
//    public void setTitle(String title) {
//        this.title = title;
//    }
// 
//    public String getTitle() {
//        return title;
//    }
//    
//    public String toString() {
//        return title;
//    }
    
    public JTextField getTextfield() {
		return textfield;
	}
	
	public PNTreeNodeType getFieldType() {
		return fieldType;
	}

	public void setTextField(PropertiesField field) {
		this.textfield = field;
		
	}
   
}