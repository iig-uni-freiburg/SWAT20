package de.unifreiburg.iig.bpworkbench2.model;

import java.io.File;
import java.util.Observable;

import org.apache.commons.collections.buffer.CircularFifoBuffer;

public class GuiModel extends Observable {
	private static GuiModel myGuiModel = new GuiModel();
	private FileModel openFiles;

	public static GuiModel getInstance() {
		return myGuiModel;
	}

	private GuiModel() {

	}

	/**
	 * For a list of open files and to state if file has unsaved changes
	 * 
	 * @author richard
	 * 
	 */
	public class FileModel {
		File viewedFile;
		boolean unsavedChanges;
	}

	static class History {
		// GuiModel[] history = new GuiModel[10];
		static final CircularFifoBuffer test = new CircularFifoBuffer(10);

		// myGuiModel = (GuiModel) test.pop();
		static void addToHistory() {
			System.out.println("Test");
			test.add(myGuiModel);
		}

	}

}
