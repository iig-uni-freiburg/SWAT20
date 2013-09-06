package de.unifreiburg.iig.bpworkbench2.gui;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;

import org.jdesktop.swingx.MultiSplitLayout;
import org.jdesktop.swingx.MultiSplitLayout.Divider;
import org.jdesktop.swingx.MultiSplitLayout.Leaf;
import org.jdesktop.swingx.MultiSplitLayout.Node;
import org.jdesktop.swingx.MultiSplitLayout.Split;
import org.jdesktop.swingx.MultiSplitPane;

import de.unifreiburg.iig.bpworkbench2.controller.Increaser;
import de.unifreiburg.iig.bpworkbench2.model.BPWorkbenchCloseModel;
import de.unifreiburg.iig.bpworkbench2.model.NumberModel;

/**
 * Starts Main Window for SWAT when instantiated. To be run as Singleton. TODO:
 * Follow the MVC-Pattern TODO: Multi Split Pane
 * (https://today.java.net/pub/a/today/2006/03/23/multi-split-pane.html)
 * 
 * @author richy
 * 
 */
@SuppressWarnings("serial")
public class BPGui extends JFrame implements Observer, Serializable {

	public JLabel newFile = new JLabel(UIManager.getIcon("FileView.fileIcon"));
	private static MultiSplitPane msp;

	public static void main(String[] args) {
		System.out.println("start");
		System.out.println("Create Model");
		BPWorkbenchCloseModel closeModel = new BPWorkbenchCloseModel();
		System.out.println("Create View");
		final BPGui gui = new BPGui();
		System.out.println("Register Observer to Model");
		closeModel.addObserver(gui);
		NumberModel.getInstance().addObserver(gui);
		// gui.newFile.addMouseListener(null);
		// closeModel.closeGui();
		// gui.add(getMultiSplitPane(),BorderLayout.CENTER);
		JFrame testFrame = new JFrame();
		testFrame.getContentPane().add(generateView());
		testFrame.setSize(500, 300);
		testFrame.setVisible(true);
		testFrame.addWindowListener(new WindowListener() {

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
				XMLEncoder e;
				try {
					ObjectOutputStream oos = new ObjectOutputStream(
							new FileOutputStream("gui.obj"));
					oos.writeObject(this);
					oos.close();
				} catch (FileNotFoundException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				try {
					e = new XMLEncoder(new BufferedOutputStream(
							new FileOutputStream("gui.xml")));
				} catch (FileNotFoundException e1) {
					e = null;
					e1.printStackTrace();
				}
				Node model = msp.getMultiSplitLayout().getModel();
				e.writeObject(model);
				e.close();
				System.exit(0);

			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	private static MultiSplitPane getMultiSplitPane() {
		List<Node> children = Arrays.asList(new Leaf("left"), new Divider(),
				new Leaf("right"));
		Split modelRoot = new Split();
		modelRoot.setChildren(children);

		MultiSplitPane msp = new MultiSplitPane();
		msp.getMultiSplitLayout().setModel(modelRoot);
		msp.add(new JButton("Linker Knopf"), "left");
		msp.add(new JButton("Rechter Knopf"), "right");

		return msp;
	}

	private static MultiSplitPane generateView() {
		// Create String that will be parsed and represents the GUI Model
		String layoutDef = "(COLUMN weight=0.1 topmost iconline (ROW weight=0.1 left weight=0.8 middle (COLUMN weight=0.1 right.top right.bottom))weight=0.1 bottomline)";
		// Parse the model
		Node modelRoot = MultiSplitLayout.parseModel(layoutDef);
		// Set the parsed model as Root for the SplitLayout
		msp = new MultiSplitPane();
		msp.getMultiSplitLayout().setModel(modelRoot);

		// insert some Buttons
		msp.add(new JButton("This is topmost"), "topmost");
		msp.add(new JButton("This is iconline"), "iconline");
		msp.add(new JButton("middle"), "middle");
		msp.add(new JButton("right on top"), "right.top");
		msp.add(new JButton("right on bottom"), "right.bottom");
		msp.add(new JButton("This is the bottom status line"), "bottomline");
		msp.add(new JButton("This ist left"), "left");
		return msp;
	}

	/**
	 * Constructor creates and opens Main Windows
	 */
	public BPGui() {
		getContentPane().setLayout(new BorderLayout());
		this.add(newFile);
		this.setSize(400, 600);
		JButton closeButton = new JButton("Schliessen");
		setVisible(true);
		closeButton.addActionListener(new Increaser());
		this.add(closeButton, BorderLayout.WEST);

		// closeButton.addActionListener(new Increaser());

	}

	public void close() {
		System.out.println("Bye");
		System.exit(0);
	}

	@Override
	public void update(Observable o, Object arg) {
		System.out.println("There was an update by " + o + " Message: " + arg);
		o.hasChanged();
		if (o instanceof BPWorkbenchCloseModel
				&& ((BPWorkbenchCloseModel) o).close) {
			close();
		}
		if (o instanceof NumberModel) {
			System.out.println("Bank Account Changed");
		}

	}

}
