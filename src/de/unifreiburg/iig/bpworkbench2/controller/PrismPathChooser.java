package de.unifreiburg.iig.bpworkbench2.controller;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

import de.unifreiburg.iig.bpworkbench2.helper.PrismRunner;
import de.unifreiburg.iig.bpworkbench2.helper.SwatProperties;

public class PrismPathChooser extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JLabel currentPath;
	JLabel okLabel = new JLabel("Test Path ");

	public static void main(String args[]) {
		PrismPathChooser ppc = new PrismPathChooser();
		ppc.setVisible(true);
	}

	public PrismPathChooser() {
		// Initialize Layout and size
		setSize(400, 150);
		setPreferredSize(new Dimension(400, 100));
		setLocation(300, 350);
		// setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
		addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				setVisible(false);

			}

			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		// create Labels containing current Path

		// setLayout(new GridLayout(2, 2));
		// setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(new JLabel("Current path:"));
		currentPath = getPathLabel();
		add(currentPath);
		add(okLabel);
		add(getAutoSelect());
		add(saveButton());
		add(getManualSearch());
		validatePath();
	}

	/** Button to let user select prism path **/
	private JButton getManualSearch() {
		JButton result = new JButton("Select Path...");
		result.setActionCommand("manual");
		result.addActionListener(this);
		return result;
	}

	/** Button to automatically search for prism **/
	private JButton getAutoSelect() {
		JButton auto = new JButton("Search for prism");
		auto.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				PrismRunner pRunner = new PrismRunner();
				currentPath.setText(pRunner.searchForPrism());
				validatePath();
			}
		});
		return auto;
	}

	/** Label containing the currently used prism path **/
	private JLabel getPathLabel() {
		// get current set path to prism
		String path = SwatProperties.getInstance().getProperty("PrismPath", System.getProperty("home.dir"));
		JLabel currentPath = new JLabel(path);
		return currentPath;
	}

	/** get Button to store PrismPath **/
	private JButton saveButton() {
		return new JButton("Save") {
			{
				addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						SwatProperties.getInstance().setProperty("PrismPath", currentPath.getText());
					}
				});
			}
		};
	}

	private JButton setPath() {
		JButton setPath = new JButton("select Path...");
		// TODO: hier weiter machen
		return null;
	}

	private void validatePath() {
		PrismRunner pr = new PrismRunner();
		if (pr.validatePrismPath(currentPath.getText())) {
			okLabel.setText("Path valid");
		} else {
			okLabel.setText("Path seems unvalid");
			// TODO: FileChooser machen
		}

	}

	public void setPath(File file) {
		currentPath.setText(file.toString());
		validatePath();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// Manually search a path
		if (arg0.getActionCommand().equals("manual")) {
			// Let user search for prism
			JFileChooser fc = new JFileChooser(SwatProperties.getInstance().getProperty("PrismSearchPath", System.getProperty("home.dir")));
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = fc.showOpenDialog(null);
			if (returnVal != JFileChooser.APPROVE_OPTION) {
				// file was not correctly chosen
				return;
			}

			// File was chosen correctly
			File file = fc.getSelectedFile();
			SwatProperties.getInstance().setProperty("PrismSearchPath", file.toString());
			setPath(file);
		} else {
			System.out.println("Starting Prism Path Chooser");
			setVisible(true);
		}
	}

}
