package de.uni.freiburg.iig.telematik.swat.editor.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.canvas.mxICanvas;
import com.mxgraph.canvas.mxImageCanvas;
import com.mxgraph.shape.mxEllipseShape;
import com.mxgraph.shape.mxIShape;
import com.mxgraph.shape.mxStencilRegistry;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;

import de.invation.code.toval.graphic.CircularPointGroup;
import de.invation.code.toval.graphic.GraphicUtils;
import de.invation.code.toval.graphic.PColor;
import de.invation.code.toval.graphic.Position;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalPTNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.TokenGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTPlace;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PTProperties;
import de.uni.freiburg.iig.telematik.swat.editor.properties.PNProperties.PNComponent;

public class PTGraph extends PNGraph {

	public PTGraph(GraphicalPTNet netContainer, PTProperties properties) throws ParameterException {
		super(netContainer, properties);
	}

	@Override
	public GraphicalPTNet getNetContainer() {
		return (GraphicalPTNet) super.getNetContainer();
	}

	@Override
	protected PTProperties getPNProperties() {
		return (PTProperties) super.getPNProperties();
	}
	
	@SuppressWarnings("rawtypes") 
	@Override
	protected String getArcConstraint(AbstractFlowRelation relation) {
		return String.valueOf(((PTFlowRelation) relation).getWeight());
	}

	@Override
	public void updatePlaceState(PNGraphCell cell, Object state) throws ParameterException {
		Integer tokens =  new Integer((String) state);
		PTMarking initialMarking = getNetContainer().getPetriNet().getInitialMarking();
		initialMarking.set(cell.getId(), new Integer(tokens));
		getNetContainer().getPetriNet().setInitialMarking(initialMarking);
	}


	@Override
	protected void drawAdditionalPlaceGrahpics(mxGraphics2DCanvas canvas, mxCellState state) {
		Rectangle temp = state.getRectangle();
		PNGraphCell cell = (PNGraphCell) state.getCell();
		PTPlace place = (PTPlace) getNetContainer().getPetriNet().getPlace(
				cell.getId());
			Integer k = (Integer) place.getState();

			Double dotNumber = 0.0;
			int circles = 0;
			for (circles = 1; dotNumber < (k - 1); circles++) {
				dotNumber += new Double(((2 * circles) * Math.PI)).intValue();
			}

			int diameter = Math.min(temp.height, temp.width);
			diameter *= 0.3;
			diameter = Math.max(diameter, 6);

			if (circles > 1) {
				diameter = Math.min(temp.height, temp.width);
				diameter *= 0.8;
				diameter /= (((circles - 1) * 2) + 1);
			}// denominator:all circles in one row => (80% of the available inner CircleSize) / (maximal amount of dots) = size for one dot

			CircularPointGroup circularPointGroup = new CircularPointGroup(1, diameter);
			Map<String, Color> colors = null;
				colors = new HashMap<String, Color>();
				colors.put("black", new Color(0, 0, 0));
				circularPointGroup.addPoints(PColor.black, k);		
				Point center = new Point(temp.x + temp.width / 2, temp.y
						+ temp.height / 2);
				Object g = null;
				try {
				g = drawPoints(canvas, temp, circularPointGroup, center);
				} catch (ParameterException e) {
					System.out.println("Tokenposition could not be assigned!");

					e.printStackTrace();
				}
		
	}
	
	
	protected Graphics drawPoints(mxGraphics2DCanvas canvas, Rectangle temp, CircularPointGroup circularPointGroup, Point center) throws ParameterException {
		Graphics g = canvas.getGraphics();
		Iterator<PColor> iter = circularPointGroup.getColors().iterator();
		PColor actColor;
		Set<TokenGraphics> tgSet = new HashSet<TokenGraphics>();

		while (iter.hasNext()) {
			actColor = iter.next();
			g.setColor(new Color(actColor.getRGB()));
			for (Position p : circularPointGroup.getCoordinatesFor(actColor)) {
				GraphicUtils.fillCircle(g, (int) (center.getX() + p.getX()),(int) (center.getY() + p.getY()),circularPointGroup.getPointDiameter());
			}
		}
		return g;
	}

}