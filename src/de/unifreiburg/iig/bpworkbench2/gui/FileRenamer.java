package de.unifreiburg.iig.bpworkbench2.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import de.unifreiburg.iig.bpworkbench2.model.files.SwatComponent;

public class FileRenamer {
	private SwatComponent uFile;
	private JFrame renameFrame;
	JTextArea text;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FileRenamer fr = new FileRenamer(new SwatComponent("/tmp/test"));
		fr.showDialog();

	}

	public FileRenamer(SwatComponent uFile) {
		this.uFile = uFile;
	}

	public void showDialog() {
		renameFrame = new JFrame();
		renameFrame.setSize(250, 50);
		renameFrame.setLayout(new BoxLayout(renameFrame.getContentPane(), BoxLayout.X_AXIS));
		text = new JTextArea(uFile.getName().split("\\.")[0]);
		text.setPreferredSize(new Dimension(150, 50));
		renameFrame.add(text);
		JButton ok = getOKButton();
		JButton cancel = getCancelButton();
		renameFrame.add(ok);
		renameFrame.add(cancel);
		renameFrame.setVisible(true);
		renameFrame.pack();
		// TODO: Alternativ, Methode in File überschreiben. UserFile kann sich
		// selber um Darstellung des Dialogs kümmern und sich selbst umbenennen
	}

	private JButton getCancelButton() {
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				renameFrame.setVisible(false);

			}
		});
		return cancel;

	}

	private JButton getOKButton() {
		JButton ok = new JButton("Ok");
		ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Rename file accordingly
				String newName = new File(uFile.getFile(), text.getText()).toString();
				if (newName.contains(".pnml"))
					uFile.getFile().renameTo(new File(newName));
				else
					uFile.getFile().renameTo(new File(newName.concat(".pnml")));
			}
		});
		return ok;
	}
}
