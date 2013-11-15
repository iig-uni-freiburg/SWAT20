package de.uni.freiburg.iig.telematik.swat.editor.menu;

import java.awt.datatransfer.DataFlavor;

public class PaletteIconDataFlavor extends DataFlavor {
	public PaletteIconDataFlavor(){
		super(PaletteIcon.class, "PaletteIcon");
	}
}