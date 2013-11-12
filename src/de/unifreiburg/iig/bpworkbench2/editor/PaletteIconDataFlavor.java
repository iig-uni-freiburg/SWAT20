package de.unifreiburg.iig.bpworkbench2.editor;

import java.awt.datatransfer.DataFlavor;

public class PaletteIconDataFlavor extends DataFlavor {
	public PaletteIconDataFlavor(){
		super(PaletteIcon.class, "PaletteIcon");
	}
}