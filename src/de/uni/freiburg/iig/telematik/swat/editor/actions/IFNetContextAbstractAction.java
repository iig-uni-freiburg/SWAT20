package de.uni.freiburg.iig.telematik.swat.editor.actions;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.wolfgang.actions.AbstractPNEditorAction;
import de.uni.freiburg.iig.telematik.wolfgang.editor.component.PNEditorComponent;

public abstract class IFNetContextAbstractAction extends AbstractPNEditorAction {

	/**
	 * Defines the general behaviour of IFNet Toolbar Buttons for the Creation, Editing andv Selection of ACModel, AnalysisContexts and Labelings.
	 */
	private static final long serialVersionUID = 4759421821503852267L;
	private Color noModelSelectedColor = Color.GRAY;
	private Color modelSelectedColor = Color.BLACK;
	private JToggleButton button;
	private String modelname;
	private IFNetContextAbstractAction previousDependency;
	private IFNetContextAbstractAction followingDependency;

	public IFNetContextAbstractAction(PNEditorComponent editor, String name, Icon icon) throws ParameterException {
		super(editor, name, icon);
	}

	public IFNetContextAbstractAction(PNEditorComponent editor, String name) throws ParameterException {
		super(editor, name);
	}

	public IFNetContextAbstractAction(PNEditorComponent editor) throws ParameterException {
		super(editor);
	}

	public void setSelectedModel(String modelName) {
		setModelName(modelName);

		if (modelName != null) {
			setIconImage(colorPNG(modelSelectedColor, getIcon()));
			setConsequencesForSelection();
			getButton().setToolTipText(getButtonSpecificToolTipForSelection());
		} else {
			setIconImage(colorPNG(noModelSelectedColor, getIcon()));
			setConsequencesForNonSelection();

			getButton().setToolTipText(getButtonSpecificToolTipForNonSelection());
		}

	}

	public void setModelName(String modelname) {
		this.modelname = modelname;
	}

	public String getModelName() {
		return modelname;
	}

	protected abstract String getButtonSpecificToolTipForNonSelection();

	protected abstract String getButtonSpecificToolTipForSelection();

	protected abstract void setConsequencesForSelection();

	protected abstract void setConsequencesForNonSelection();

	public void setIconImage(Image image) {
		getIcon().setImage(image);
	}

	private BufferedImage colorPNG(Color color, ImageIcon imageIcon) {
		BufferedImage image = toBufferedImage(imageIcon.getImage());
		int width = image.getWidth();
		int height = image.getHeight();
		WritableRaster raster = image.getRaster();

		for (int xx = 0; xx < width; xx++) {
			for (int yy = 0; yy < height; yy++) {
				int[] pixels = raster.getPixel(xx, yy, (int[]) null);
				pixels[0] = color.getRed();
				pixels[1] = color.getGreen();
				pixels[2] = color.getBlue();
				raster.setPixel(xx, yy, pixels);
			}
		}
		return image;
	}

	public static BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}

		// Create a buffered image with transparency
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		// Draw the image on to the buffered image
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		// Return the buffered image
		return bimage;
	}

	public void setButton(JToggleButton button) {
		this.button = button;
	}

	public JToggleButton getButton() {
		return button;
	}

	public void setPreceedingDependency(IFNetContextAbstractAction previousAction) {
		this.previousDependency = previousAction;
	}

	public IFNetContextAbstractAction getPreceedingDependency() {
		return previousDependency;
	}

	public void setFollowingDependency(IFNetContextAbstractAction previousAction) {
		this.followingDependency = previousAction;
	}

	public IFNetContextAbstractAction getFollowingDependency() {
		return followingDependency;
	}

}
