package de.unifreiburg.iig.bpworkbench2.model.files;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.PNMLParser;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.unifreiburg.iig.bpworkbench2.editor.PTNetEditor;
import de.unifreiburg.iig.bpworkbench2.gui.SplitGui;
import de.unifreiburg.iig.bpworkbench2.logging.BPLog;

/**
 * Represents model of open files. Carries additional member "hasUnsavedChanges"
 * which is true if the file has unsaved changes. It is set to false otherwise
 * 
 * @author richard
 * 
 */
public class SwatComponent {
	private boolean hasUnsavedChanges = false;
	// private JEditorPane editor;
	private JPanel editor;
	private static Logger log = BPLog.getLogger(SplitGui.class.getName());
	private boolean parseable = false;
	private boolean show = false;
	private File file;

	public SwatComponent(String pathname) {
		file = new File(pathname);
		hasUnsavedChanges = false;
		createEditor();
	}

	/** Set file to be shown **/
	public void show() {
		show = true;
		createEditor();
	}

	public SwatComponent(String pathname, boolean hasUnsavedChanges) {
		file = new File(pathname);
		this.hasUnsavedChanges = hasUnsavedChanges;
		createEditor();
	}

	public SwatComponent(File file) {
		this.file = file;
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
		return hasUnsavedChanges ? "*" + file.getName() : file.getName();
	}

	/** returns name of the file **/
	public String toString() {
		return file.getName();
	}

	/*
	 * private void createEditor() { try { editor = new
	 * JEditorPane(this.toURI().toURL());
	 * 
	 * } catch (MalformedURLException e) { editor = new JEditorPane();
	 * log.log(Level.SEVERE, "Could not open file " + this.getPath() +
	 * this.toString()); } catch (IOException e) { editor = new JEditorPane();
	 * log.log(Level.SEVERE, "Could not open file " + this.getPath() +
	 * this.toString()); } }
	 */

	private void createEditor() {
		if (!show) {
			// however, still check if it is parseable
			parseable = true;
			return;
		}

		try {

			/*
			 * PetriNet
			 */
			AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?> netContainer = new PNMLParser().parse(file, true, false);
			AbstractPetriNet<?, ?, ?, ?, ?> petriNet = netContainer.getPetriNet();

			// distinguish between different net-types to choose corresponding
			// editor
			if (netContainer instanceof GraphicalPTNet) {
				editor = new PTNetEditor((GraphicalPTNet) netContainer, file);
			}

			// TODO
			// if (petriNet instanceof CPN) {
			// editor = new CPNEditor(netContainer);
			// }
			//
			// if (petriNet instanceof CWN) {
			// editor = new CWNEditor(netContainer);
			//
			// }
			//
			// if (petriNet instanceof IFNet) {
			// editor = new IFNetEditor(netContainer);
			// }

			// editor = new PNMLEditor(this);
			// create file open Action and fire it onto the editor
			// OpenAction oa = new OpenAction();
			// oa.actionPerformed(new ActionEvent(editor,
			// ActionEvent.ACTION_PERFORMED, "open"));
			parseable = true;
		} catch (Exception e) {
			log.log(Level.SEVERE,
					"Could not open editor for " + file.getAbsolutePath() + ": in " + getClass().toString() + " " + e.toString());
			// editor = new PNMLEditor(null);
		}

	}

	public boolean isParseable() {
		return parseable;
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
		// log.log(Level.SEVERE, "Saving of PNML currently unimplemented");
		// System.out.println("Unimplented");
		// SaveAction sa = new SaveAction(editor);
		// sa.actionPerformed(new ActionEvent(editor,
		// ActionEvent.ACTION_PERFORMED, "save"));

	}

	public File getFile() {
		return file;
	}
}