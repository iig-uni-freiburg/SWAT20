package com.mxgraph.shape;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Ellipse2D.Float;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.plaf.metal.MetalIconFactory.PaletteCloseIcon;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxCellState;

import de.invation.code.toval.graphic.CircularPointGroup;
import de.invation.code.toval.graphic.GraphicUtils;
import de.invation.code.toval.graphic.PColor;
import de.invation.code.toval.graphic.Position;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.PTGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.NodeGraphics;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.TokenGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPNNode;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTPlace;
import de.unifreiburg.iig.bpworkbench2.editor.soul.Constants;

public class PNPlaceShape extends mxBasicShape {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;

	protected static final Dimension defaultDimension = new Dimension(400, 400);
	protected Dimension dimension = defaultDimension;
	protected Point center;
	protected CircularPointGroup pointGroup;
	CircularPointGroup circularPointGroup;

	private int k;

	private AbstractGraphicalPN<?, ?, ?, ?, ?> n;

	private mxCell cell;

	public Shape createShape(mxGraphics2DCanvas canvas, mxCellState state) {
		Rectangle temp = state.getRectangle();
		cell = (mxCell) state.getCell();
System.out.println(state.getStyle());
		Float shape = new Ellipse2D.Float(temp.x, temp.y, temp.width,
				temp.height);

		return shape;
	}

	protected Graphics drawPoints(mxGraphics2DCanvas canvas, Rectangle temp) throws ParameterException {
		Graphics g = canvas.getGraphics();
		Iterator<PColor> iter = pointGroup.getColors().iterator();
		PColor actColor;
		Set<TokenGraphics> tgSet = new HashSet<TokenGraphics>();

		while (iter.hasNext()) {
			actColor = iter.next();
			g.setColor(new Color(actColor.getRGB()));
			for (Position p : pointGroup.getCoordinatesFor(actColor)) {
				GraphicUtils.fillCircle(g, (int) (center.getX() + p.getX()),
						(int) (center.getY() + p.getY()),
						pointGroup.getPointDiameter());
				
				//Add Point to Graphical Information
				HashMap<String, Set<TokenGraphics>> tgMap = (n.getPetriNetGraphics().getTokenGraphics() == null) ? new HashMap<String, Set<TokenGraphics>>():(HashMap<String, Set<TokenGraphics>>) n
							.getPetriNetGraphics().getTokenGraphics();
				de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Position tokenposition = new de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes.Position(
						(int) (center.getX() + p.getX()) - cell.getGeometry().getCenterX(),
						(int) (center.getY() + p.getY()) - cell.getGeometry().getCenterY());
				TokenGraphics tg = new TokenGraphics(tokenposition);
				tgSet.add(tg);
				tgMap.put(cell.getId(), tgSet);
			}
		}
		return g;
	}

	public void paintShape(mxGraphics2DCanvas canvas, mxCellState state) {
		Shape shape = createShape(canvas, state);

		if (shape != null) {
			// Paints the background
			if (configureGraphics(canvas, state, true)) {
				canvas.fillShape(shape, hasShadow(canvas, state));
			}

			// Paints the foreground
			if (configureGraphics(canvas, state, false)) {
				Rectangle temp = state.getRectangle();
				Object value = ((mxCell) state.getCell()).getValue();

				if ((value != null)
						&& (((mxCell) state.getCell()).getParent() != null)) {
					n = (AbstractGraphicalPN<?, ?, ?, ?, ?>) ((mxCell) state
							.getCell()).getParent().getValue();
					PTPlace place = (PTPlace) n.getPetriNet().getPlace(
							cell.getId());

					k = place.getState();

					Double dotNumber = 0.0;
					int c = 0;
					for (c = 1; dotNumber < (k - 1); c++) {

						dotNumber += new Double(((2 * c) * Math.PI)).intValue();

					}

					int diameter = Math.min(temp.height, temp.width);
					diameter *= 0.3;
					diameter = Math.max(diameter, 6);

					if (c > 1) {
						diameter = Math.min(temp.width, temp.height);
						diameter *= 0.8;
						diameter /= (((c - 1) * 2) + 1);
					}// denominator:all circles in one row =>
					// (80% of the available inner CircleSize) / (maximal amount
					// of dots) = size for one dot

					circularPointGroup = new CircularPointGroup(1, diameter);
					PTGraphics g;
					Map<String, Color> colors = null;
					if (n.getPetriNetGraphics() instanceof PTGraphics) {
						g = (PTGraphics) n.getPetriNetGraphics();

						// colors = g.getColors();//for CPN
						colors = new HashMap<String, Color>();
						colors.put("black", new Color(0, 0, 0));
						circularPointGroup.addPoints(PColor.black, k);
					}
					// for CPN
					// Set<String> keyset = ((AbstractCPNPlace<CPNFlowRelation>)
					// place).getState().support();
					//
					// for (String s : keyset){
					// try {
					//
					// Color color = colors.get(s);
					// int number = ((AbstractCPNPlace<CPNFlowRelation>)
					// place).getState().multiplicity(s);
					// PColor pco;
					// if(color !=null)
					// pco = new PColor(color.getRed(), color.getGreen(),
					// color.getBlue());
					// else {pco = PColor.black;}
					//
					// circularPointGroup.addPoints(pco, number);
					// } catch (ParameterException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }
					// }

					if (k > 0) {
						// this.dimension = new
						// Dimension(circularPointGroup.getRequiredDiameter(),
						// circularPointGroup.getRequiredDiameter());
						center = new Point(temp.x + temp.width / 2, temp.y
								+ temp.height / 2);
						this.pointGroup = circularPointGroup;
						try {
							drawPoints(canvas, temp);
						} catch (ParameterException e) {
							System.out.println("Tokenposition could not be assigned!");

							e.printStackTrace();
						}
						System.out.println("HELLO");
					}
				}

				canvas.getGraphics().draw(shape);

			}
		}
	}

}
