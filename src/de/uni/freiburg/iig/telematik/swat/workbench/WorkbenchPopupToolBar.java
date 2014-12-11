package de.uni.freiburg.iig.telematik.swat.workbench;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import de.uni.freiburg.iig.telematik.wolfgang.menu.ToolBarDialog;

public class WorkbenchPopupToolBar {
	private JToolBar verticalToolBar;
	protected JToolBar horizontalToolBar;
	private Window[] windows;
	private DialogFocusListener focusListener;
	private JButton button;
	private boolean dialogOpen;

	private class DialogFocusListener implements WindowFocusListener {

		public void windowGainedFocus(WindowEvent e) {

		}

		public void windowLostFocus(WindowEvent e) {
			if (windows == null || windows.length == 0) {
				return;
			}
			if (!Arrays.asList(windows).contains(e.getOppositeWindow())) {
				System.out.println(e.getOppositeWindow());
				if (!(e.getOppositeWindow() instanceof JDialog) || (e.getOppositeWindow() instanceof ToolBarDialog))
					disposeAllWindows();
				if (!dialogOpen)
					button.setSelected(false);
			}
		}
	}

	public WorkbenchPopupToolBar() {

	}

	protected JToolBar createToolBar(int alignment) {
		JToolBar toolBar = new JToolBar(alignment) {
			@Override
			protected JButton createActionComponent(Action a) {
				final JButton button = super.createActionComponent(a);
				button.setFocusable(false);
				return button;
			}
		};
		toolBar.setFloatable(false);
		return toolBar;
	}

	/**
	 * Add an action to
	 * 
	 * @param action
	 *            the action to add
	 * @param alignment
	 *            the Alignment of the ToolBar either JToolBar.VERTICAL or
	 *            JToolBar.HORIZONTAL
	 * @return
	 */
	public JButton add(AbstractAction action, int alignment) {
		JButton button;
		switch (alignment) {
		case JToolBar.VERTICAL:
			if (verticalToolBar == null) {
				verticalToolBar = createToolBar(alignment);
			}
			button = verticalToolBar.add(action);
			break;
		case JToolBar.HORIZONTAL:
			if (horizontalToolBar == null) {
				horizontalToolBar = createToolBar(alignment);
			}
			button = horizontalToolBar.add(action);
			break;
		default:
			throw new IllegalArgumentException("Unknown alignment " + alignment);
		}
		return button;
	}

	/**
	 * Retreive the tool bar
	 * 
	 * @param alignment
	 * @return
	 */
	private JToolBar getToolBar(int alignment) {
		switch (alignment) {
		case JToolBar.VERTICAL:
			return verticalToolBar;
		case JToolBar.HORIZONTAL:
			return horizontalToolBar;
		default:
			throw new IllegalArgumentException("Unknown alignment " + alignment);
		}
	}

	/**
	 * Create the Window for the given alignment and
	 * 
	 * @param comp
	 *            father component
	 * @param alignment
	 *            algenment
	 * @param point
	 *            location point
	 * @return the created window
	 */
	protected Window createWindow(Component comp, int alignment, Point point) {
		JToolBar bar = getToolBar(alignment);
		if (bar == null) {
			return null;
		}
		final JDialog dialog = new JDialog(JOptionPane.getFrameForComponent(comp));
		dialog.addMouseMotionListener(new MouseMotionAdapter() {

			public void mouseMoved(MouseEvent e) {
				if (!dialog.hasFocus())
					dialog.requestFocusInWindow();
			}

		});
		dialog.setUndecorated(true);
		dialog.setLayout(new BorderLayout());
		dialog.add(bar);
		dialog.pack();

		Point loc = adujstPoint(point);
		SwingUtilities.convertPointToScreen(loc, comp);
		dialog.setLocation(loc);
		return dialog;
	}

	private Point adujstPoint(Point initPoint) {
		return new Point(initPoint.x, initPoint.y);
	}

	/**
	 * Creates the windows with the according toolbars and display them
	 * 
	 * @param comp
	 * @param x
	 * @param y
	 */
	public void show(Component comp, int x, int y) {

		disposeAllWindows();
		Window horizontalWindow = createWindow(comp, JToolBar.HORIZONTAL, new Point(x, y));
		Window verticalWindow = createWindow(comp, JToolBar.VERTICAL, new Point(x, y));
		if (horizontalWindow == null && verticalWindow == null) {
			return;
		}
		ArrayList<Window> windowList = new ArrayList<Window>();
		if (horizontalWindow != null) {
			windowList.add(horizontalWindow);
		}
		if (verticalWindow != null) {
			windowList.add(verticalWindow);
		}
		windows = windowList.toArray(new Window[windowList.size()]);
		windows[0].requestFocusInWindow();
		setFocusListener(new DialogFocusListener());
		for (int i = 0; i < windows.length; i++) {
			windows[i].addWindowFocusListener(getFocusListener());
			windows[i].setVisible(true);
		}
	}

	public DialogFocusListener getFocusListener() {
		return focusListener;
	}

	private void setFocusListener(DialogFocusListener focusListener) {
		this.focusListener = focusListener;
	}

	public void disposeAllWindows() {
		if (windows == null) {
			return;
		}
		for (int i = 0; i < windows.length; i++) {
			windows[i].dispose();
		}
	}

	public void add(JComponent action) {

		if (horizontalToolBar == null) {
			horizontalToolBar = createToolBar(JToolBar.HORIZONTAL);
		}
		horizontalToolBar.add(action);

	}

	public void setButton(JButton jButton, boolean dialogOpen) {
		this.button = jButton;
		this.dialogOpen = dialogOpen;

	}

}