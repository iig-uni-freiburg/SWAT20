package de.uni.freiburg.iig.telematik.swat.editor.graph;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class Main extends JFrame {
  private DefaultMutableTreeNode root;

public Main() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    root = new DefaultMutableTreeNode("abcde");
    DefaultMutableTreeNode node = new DefaultMutableTreeNode("1");
//    node.set
    node.add(new DefaultMutableTreeNode("12345"));
    node.add(new DefaultMutableTreeNode("testing"));
    root.add(node);
    root.add(new DefaultMutableTreeNode("1234567890"));

    TreeModel tm = new DefaultTreeModel(root);
    final JTree tree = new JTree(tm);
//    tree.getSelectionModel().addTreeSelectionListener(new Selector());
    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    getContentPane().add(tree, BorderLayout.CENTER);
    JButton button = new JButton();
    
   
   
    
    
    
    button.addMouseListener(new MouseAdapter(){

		@Override
		public void mouseClicked(MouseEvent arg0) {
//			System.out.println(tree.getSelectionModel());
//			System.out.println(tree.getSelectionPath());
			TreeSelectionModel treePath = tree.getSelectionModel();
			 TreePath path = find(root,"12345");
			 System.out.println(path);
			 tree.setSelectionPath(path);
//			path = super.find(root, "1");
//			tree.setSelectionPath(path);
//			tree.scrollPathToVisible(path);
		}
    	
    });
    
    getContentPane().add(button, BorderLayout.LINE_START);
    JButton button2 = new JButton();
    
   
   
    
    
    
//    button2.addMouseListener(new MouseAdapter(){
//
//		@Override
//		public void mouseClicked(MouseEvent arg0) {
//
//			TreeSelectionModel treePath = tree.getSelectionModel();
//			 TreePath path = find(root,"1234567890");
//			 System.out.println(path);
//			 tree.collapseRow(1);
//			 tree.setSelectionPath(path);
//			tree.scrollPathToVisible(path);
//		}
//    	
//    });
//    
//    getContentPane().add(button2, BorderLayout.SOUTH);
    
    pack();
    setVisible(true);
  }
  private TreePath find(DefaultMutableTreeNode root, String s) {
	    @SuppressWarnings("unchecked")
	    Enumeration<DefaultMutableTreeNode> e = root.depthFirstEnumeration();
	    while (e.hasMoreElements()) {
	        DefaultMutableTreeNode node = e.nextElement();
	        if (node.toString().equalsIgnoreCase(s)) {
	            return new TreePath(node.getPath());
	        }
	    }
	    return null;
	} 
//  private class Selector implements TreeSelectionListener {
//    public void valueChanged(TreeSelectionEvent event) {
////      Object obj = event.getNewLeadSelectionPath().getLastPathComponent();
////      System.out.println("" + obj.toString().length());
//    }
//  }

  public static void main(String[] args) {
    Main m = new Main();
  }
  
  
}