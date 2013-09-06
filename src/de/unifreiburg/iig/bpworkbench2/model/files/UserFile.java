package de.unifreiburg.iig.bpworkbench2.model.files;

import gui.PNMLEditor;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.unifreiburg.iig.bpworkbench2.gui.SplitGui;
import de.unifreiburg.iig.bpworkbench2.logging.BPLog;

/**
 * Represents model of open files. Carries additional member "hasUnsavedChanges"
 * which is true if the file has unsaved changes. It is set to false otherwise
 * 
 * @author richard
 * 
 */
@SuppressWarnings("serial")
public class UserFile extends File {
	private boolean hasUnsavedChanges = false;
	// private JEditorPane editor;
	private PNMLEditor editor;
	private static Logger log = BPLog.getLogger(SplitGui.class.getName());

	public UserFile(String pathname) {
		super(pathname);
		hasUnsavedChanges = false;
		createEditor();
	}

	public UserFile(String pathname, boolean hasUnsavedChanges) {
		super(pathname);
		this.hasUnsavedChanges = hasUnsavedChanges;
		createEditor();
	}

	public UserFile(File file) {
		super(file.toString());
		createEditor();
	}

	public boolean hasUnsavedChanges() {
		return hasUnsavedChanges;
	}

	public void setUnsavedChanges() {
		hasUnsavedChanges = true;
		// OpenFileModel ofm = OpenFileModel.getInstance();
		// ofm.setUnsavedChange(this);
	}

	/**
	 * Return name of file without path. Append a "*" if this File has unsaved
	 * changes
	 */
	public String getName() {
		return hasUnsavedChanges ? "*" + super.getName() : super.getName();
	}

	public String toString() {
		return super.getName();
	}

	/**
	 * private void createEditor() { try { editor = new
	 * JEditorPane(this.toURI().toURL());
	 * 
	 * } catch (MalformedURLException e) { editor = new JEditorPane();
	 * log.log(Level.SEVERE, "Could not open file " + this.getPath() +
	 * this.toString()); } catch (IOException e) { editor = new JEditorPane();
	 * log.log(Level.SEVERE, "Could not open file " + this.getPath() +
	 * this.toString()); } }
	 **/

	private void createEditor() {
		try {
			editor = new PNMLEditor(this);
		} catch (Exception e) {
			log.log(Level.SEVERE, "Could not open editor: " + e.toString());
			editor = new PNMLEditor(null);
		}

	}

	public JPanel getEditor() {
		return editor;
	}

	/**
	 * Save file
	 */
	/**
	 * public void save() { FileWriter writer = null;
	 * 
	 * // Open FileWriter, try to write content of editor and finally close //
	 * FileWriter try { log.log(Level.INFO, "Writing file " + getName()); writer
	 * = new FileWriter(this); writer.write(editor.getDocument().getText(0,
	 * editor.getDocument().getLength())); writer.flush(); // setSaved(); //
	 * writer gets closed in the finally block }
	 * 
	 * catch (IOException e) { log.log(Level.SEVERE, "Writing to file " +
	 * getName() + " FAILED"); } catch (BadLocationException e) {
	 * log.log(Level.SEVERE, "Writing to file " + getName() + " FAILED"); }
	 * finally { try { writer.close(); } catch (IOException e) { //
	 * log.log(Level.SEVERE, "cannot close File writer for " // + getName()); }
	 * } }
	 **/

	public void setSaved() {
		hasUnsavedChanges = false;

	}

	// Not used until now. Make contructor with (uFile) and insert this handler
	// to SWAT2Controller instead
	class updateListener implements DocumentListener {

		@Override
		public void removeUpdate(DocumentEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void insertUpdate(DocumentEvent arg0) {
			setUnsavedChanges();
		}

		@Override
		public void changedUpdate(DocumentEvent arg0) {
			// TODO Auto-generated method stub

		}
	}

	public void save() {
		log.log(Level.SEVERE, "Saving of PNML currently unimplemented");
		System.out.println("Unimplented");

	}
}