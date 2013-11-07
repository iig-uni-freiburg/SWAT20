package de.unifreiburg.iig.bpworkbench2.gui;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import de.unifreiburg.iig.bpworkbench2.model.files.UserFile;

public class FileRenamer {
	private UserFile uFile;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FileRenamer fr = new FileRenamer(new UserFile("/tmp/test"));
		fr.showDialog();

	}

	public FileRenamer(UserFile uFile) {
		this.uFile = uFile;
	}

	public void showDialog() {
		JFrame renameFrame = new JFrame();
		renameFrame.setSize(250, 50);
		renameFrame.setLayout(new BoxLayout(renameFrame.getContentPane(), BoxLayout.X_AXIS));
		JTextArea text = new JTextArea(uFile.toString());
		text.setPreferredSize(new Dimension(150, 50));
		renameFrame.add(text);
		JButton ok = new JButton("Ok");
		JButton cancel = new JButton("Cancel");
		renameFrame.add(ok);
		renameFrame.add(cancel);
		renameFrame.setVisible(true);
		renameFrame.pack();
		// TODO: Alternativ, Methode in File überschreiben. UserFile kann sich
		// selber um Darstellung des Dialogs kümmern und sich selbst umbenennen
	}

}
