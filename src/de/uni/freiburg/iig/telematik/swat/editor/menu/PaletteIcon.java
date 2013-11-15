package de.uni.freiburg.iig.telematik.swat.editor.menu;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.TransferHandler;

import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperties.PNComponent;

public class PaletteIcon extends JLabel implements Transferable, DragSourceListener, DragGestureListener{

	private static final long serialVersionUID = 4267632604793586610L;
	
	private DragSource dragSource = null;
	private PNComponent type = null;

	public PaletteIcon(PNComponent type, Icon image) {
		super(image);
		this.type = type;
        setTransferHandler(new TransferHandler(){
			private static final long serialVersionUID = -7526817217384383778L;

			@Override
			protected Transferable createTransferable(JComponent c) {
				return PaletteIcon.this;
			}
        	
        });
        
        dragSource = new DragSource();
        dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE, this);
	}
	
	public PNComponent getType(){
		return type;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[]{new PaletteIconDataFlavor()};
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return true;
	}

	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		return this;
	}

	@Override
	public void dragEnter(DragSourceDragEvent dsde) {}

	@Override
	public void dragOver(DragSourceDragEvent dsde) {}

	@Override
	public void dropActionChanged(DragSourceDragEvent dsde) {}

	@Override
	public void dragExit(DragSourceEvent dse) {}

	@Override
	public void dragDropEnd(DragSourceDropEvent dsde) {
		repaint();
	}

	@Override
	public void dragGestureRecognized(DragGestureEvent dge) {
		// TODO Auto-generated method stub
		dragSource.startDrag(dge, DragSource.DefaultMoveDrop, this, this);
	}
	
	

}
