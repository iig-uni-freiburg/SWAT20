package de.unifreiburg.iig.bpworkbench2.gui;

import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.unifreiburg.iig.bpworkbench2.logging.BPLog;
import de.unifreiburg.iig.bpworkbench2.model.files.OpenFileModel;
import de.unifreiburg.iig.bpworkbench2.model.files.UserFile;

@SuppressWarnings("serial")
public class TabView extends JTabbedPane implements Observer {
	// private JTabbedPane tab;
	private Logger log;

	public JTabbedPane getTabView() {
		return null;
	}

	public TabView() {
		log = BPLog.getLogger(SplitGui.class.getName());
		// tab = new JTabbedPane();
		// this.setDoubleBuffered(true);
		// this.setPreferredSize(new Dimension(50, 50));
		this.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				// SplitGui.getGui().revalidate();
				// synchronize active tab with active index in the Model
				OpenFileModel ofm = OpenFileModel.getInstance();
				int index = ((JTabbedPane) arg0.getSource()).getSelectedIndex();
				ofm.setOpenFileIndex(index, false);
			}
		});
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		OpenFileModel ofm = OpenFileModel.getInstance();
		if (arg1 != null && arg1.equals("indexChange")) {
			// only update active tab
			this.setSelectedIndex(ofm.getOpenFileIndex());
		} else {
			// else reload complete tabs
			removeAll();
			FileModelToTab();
		}
	}

	protected JComponent makeTextPanel(UserFile uFile) {
		JPanel panel = new JPanel(false);
		// JLabel filler = new JLabel(uFile.getName());
		// JEditorPane editor = new JEditorPane();
		// JEditorPane editor = uFile.getEditor();
		JPanel editor = uFile.getEditor();
		// editor.setHorizontalAlignment(JLabel.CENTER);
		if (editor != null) {
			panel.setLayout(new GridLayout(1, 1));
			panel.add(editor);
			return panel;
		}
		return null;

		// add Document Listener to set current file having changes
		/*
		 * editor.getDocument().addDocumentListener(new DocumentListener() {
		 * 
		 * @Override public void removeUpdate(DocumentEvent arg0) { // TODO
		 * Auto-generated method stub
		 * 
		 * }
		 * 
		 * @Override public void insertUpdate(DocumentEvent arg0) { if
		 * (!OpenFileModel.getInstance().hasUnsavedChange()) {
		 * System.out.println("setName"); setTitleAt(getSelectedIndex(), "*" +
		 * getTitleAt(getSelectedIndex()));
		 * OpenFileModel.getInstance().setUnsavedChanges(true); }
		 * 
		 * }
		 * 
		 * @Override public void changedUpdate(DocumentEvent arg0) { // TODO
		 * Auto-generated method stub
		 * 
		 * } });
		 */

	}

	protected void FileModelToTab() {
		// first store current viewed file. Each time a tab is added, the
		// TabSelectionhandler would fire and set a new index.
		// Alternatively: Temporarily Disable TabListener
		OpenFileModel ofm = OpenFileModel.getInstance();
		int curViewedTab = ofm.getOpenFileIndex();

		// add tab for every uFile only if uFile carries a valid PNML editor
		for (UserFile uFile : ofm.getFiles()) {
			if (uFile.getEditor() != null) {
				this.addTab(uFile.getName(), makeTextPanel(uFile));
			}
		}

		try {
			this.setSelectedIndex(curViewedTab);
			log.log(Level.FINEST, "Set active tab to: " + curViewedTab);
		} catch (IndexOutOfBoundsException e) {

		}

	}

	// public JTabbedPane getTab() {
	// return this;
	// }

}
