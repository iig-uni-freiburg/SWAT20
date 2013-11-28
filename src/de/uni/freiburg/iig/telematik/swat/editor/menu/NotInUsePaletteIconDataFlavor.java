package de.uni.freiburg.iig.telematik.swat.editor.menu;

import java.awt.datatransfer.DataFlavor;

public class NotInUsePaletteIconDataFlavor extends DataFlavor {
	public NotInUsePaletteIconDataFlavor(){
		super(NotInUsePaletteIcon.class, "PaletteIcon");
	}
}