package de.uni.freiburg.iig.telematik.swat.editor.graph;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxCellState;

import de.invation.code.toval.graphic.misc.CircularPointGroup;
import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalIFNet;
import de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.TokenGraphics;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.abstr.AbstractCWNFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractRegularIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AccessMode;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.Labeling;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.SecurityLevel;
import de.uni.freiburg.iig.telematik.swat.editor.IFNetEditor;
import de.uni.freiburg.iig.telematik.swat.editor.actions.history.RedoAction;
import de.uni.freiburg.iig.telematik.swat.editor.graph.change.AnalysisContextChange;
import de.uni.freiburg.iig.telematik.swat.editor.graph.change.TransitionLabelingChange;
import de.uni.freiburg.iig.telematik.swat.editor.menu.AbstractCPNTokenConfigurer;
import de.uni.freiburg.iig.telematik.swat.editor.menu.EditorProperties;
import de.uni.freiburg.iig.telematik.swat.editor.properties.IFNetProperties;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.swat.lukas.Transition;
import de.uni.freiburg.iig.telematik.swat.workbench.properties.SwatProperties;

/**
 * @author julius
 * 
 */
public class IFNetGraph extends PNGraph {


	private HashMap<String, AbstractCPNTokenConfigurer> tokenConfigurerWindows = new HashMap<String, AbstractCPNTokenConfigurer>();
	private AbstractCPNTokenConfigurer lastTC;
	private AnalysisContext currentAnalysisContext;
	public AnalysisContext getCurrentAnalysisContext() {
		return currentAnalysisContext;
	}

	private boolean containsAnalysisContext = false;
	private AnalysisContext currentAC;
	public IFNetGraph(GraphicalIFNet GraphicalIFNet, IFNetProperties IFNetProperties) throws ParameterException {
		super(GraphicalIFNet, IFNetProperties);
	}

	@Override
	public GraphicalIFNet getNetContainer() {
		return (GraphicalIFNet) super.getNetContainer();
	}

	@Override
	protected IFNetProperties getPNProperties() {
		return (IFNetProperties) super.getPNProperties();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String getArcConstraint(AbstractFlowRelation relation) throws ParameterException {
		Map<String, Color> colors = getNetContainer().getPetriNetGraphics().getColors();
		colors.put("black", Color.BLACK);
		String arcString = "";
		for (Entry<String, Color> color : colors.entrySet()) {
			int colorNumber = ((IFNetFlowRelation) relation).getConstraint(color.getKey());
			if (colorNumber > 0)
				arcString += color.getKey() + ": " + String.valueOf(((IFNetFlowRelation) relation).getConstraint(color.getKey())) + "\n";
		}
		return arcString;
	}

	@Override
	public void updatePlaceState(String name, Multiset<String> state) throws ParameterException {
		IFNetMarking initialMarking = getNetContainer().getPetriNet().getInitialMarking();
		initialMarking.set(name, state);
		getNetContainer().getPetriNet().setInitialMarking(initialMarking);
	}

	@Override
	public
	/**
	 * @param cell
	 * @param circularPointGroup
	 * @return
	 */ Multiset<String> getPlaceStateForCell(String id, CircularPointGroup circularPointGroup) {
		IFNetPlace place = (IFNetPlace) getNetContainer().getPetriNet().getPlace(id);
		return place.getState( );
	}



	@Override
	protected String getPlaceToolTip(PNGraphCell cell) {
		IFNetPlace ptPlace = getNetContainer().getPetriNet().getPlace(cell.getId());
		return "Cap:" + ((ptPlace.getCapacity() == -1) ? "\u221e" : ptPlace.getCapacity());
	}

	@Override
	protected String getTransitionToolTip(PNGraphCell cell) {
		return "";
	}

	@Override
	protected String getArcToolTip(PNGraphCell cell) {
		return "";
	}

	@Override
	protected void setArcLabel(PNGraph pnGraph, String id, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AbstractMarking inOrDecrementPlaceState(PNGraphCell cell, int wheelRotation) throws ParameterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getTokenColorForName(String name) {
		Color color = getNetContainer().getPetriNetGraphics().getColors().get(name);
		return color;
	}

	@Override
	public void updateTokenColor(String name, Color value) {
		IFNet IFNet = getNetContainer().getPetriNet();
		Map<String, Color> colors = getNetContainer().getPetriNetGraphics().getColors();
		if(value != null)
		colors.put(name, value);
		else{
		colors.remove(name);	
		}
		getNetContainer().getPetriNetGraphics().setColors(colors);
		} 
	
	@Override
	public void updateConstraint(String name, Multiset value) {
		IFNetFlowRelation flowrelation = (IFNetFlowRelation) getNetContainer().getPetriNet().getFlowRelation(name);
		if(value != null)
			flowrelation.setConstraint(value);	
		else{
				flowrelation.setConstraint(new Multiset<String>());
		}
//		flowrelation.setConstraint(value);		
		PNGraphCell cell = arcReferences.get(name);
//		cell.setValue(getArcConstraint(flowrelation));
		refresh();
		
	}

	public void newTokenConfigurer(PNGraphCell cell, IFNetGraphComponent IFNetGraphComponent) {
		Window window = SwingUtilities.getWindowAncestor(IFNetGraphComponent);
double x;
double y;
double height = 0;
double deltaY = 0;
int spacing;
switch(cell.getType()){
case ARC:
	IFNetFlowRelation flowRelation = getNetContainer().getPetriNet().getFlowRelation(cell.getId());
	if (!tokenConfigurerWindows.containsKey(cell.getId())) {
		AbstractCPNTokenConfigurer tc = new AbstractCPNTokenConfigurer(window, flowRelation, this);
		spacing = (int) (window.getBounds().getY() + 120);
		if (lastTC != null) {
			height = lastTC.getBounds().getHeight();
			deltaY = lastTC.getBounds().getY();
			spacing = (int) (height + deltaY);
		}

		x = window.getBounds().getX();
		y = spacing;
		tc.setLocation((int) x, (int) y);
		tc.setVisible(true);
		tc.pack();
		tokenConfigurerWindows.put(cell.getId(), tc);
		lastTC = tc;
	} else {
		tokenConfigurerWindows.get(cell.getId()).setVisible(false);
		tokenConfigurerWindows.get(cell.getId()).setVisible(true);
	}
	break;
		case PLACE:
			IFNetPlace place = getNetContainer().getPetriNet().getPlace(cell.getId());
			if (!tokenConfigurerWindows.containsKey(cell.getId())) {
				AbstractCPNTokenConfigurer tc = new AbstractCPNTokenConfigurer(window, place, this);
				spacing = (int) (window.getBounds().getY() + 120);
				if (lastTC != null) {
					height = lastTC.getBounds().getHeight();
					deltaY = lastTC.getBounds().getY();
					spacing = (int) (height + deltaY);
				}

				x = window.getBounds().getX();
				y = spacing;
				tc.setLocation((int) x, (int) y);
				tc.setVisible(true);
				tc.pack();
				tokenConfigurerWindows.put(cell.getId(), tc);
				lastTC = tc;
			} else {
				tokenConfigurerWindows.get(cell.getId()).setVisible(false);
				tokenConfigurerWindows.get(cell.getId()).setVisible(true);
			}

	break;
case TRANSITION:
	AbstractIFNetTransition<IFNetFlowRelation> transition = getNetContainer().getPetriNet().getTransition(cell.getId());
	if (!tokenConfigurerWindows.containsKey(cell.getId())) {
		AbstractCPNTokenConfigurer tc = new AbstractCPNTokenConfigurer(window, transition, this);
		spacing = (int) (window.getBounds().getY() + 120);
		if (lastTC != null) {
			height = lastTC.getBounds().getHeight();
			deltaY = lastTC.getBounds().getY();
			spacing = (int) (height + deltaY);
		}

		x = window.getBounds().getX();
		y = spacing;
		tc.setLocation((int) x, (int) y);
		tc.setVisible(true);
		tc.pack();
		tokenConfigurerWindows.put(cell.getId(), tc);
		lastTC = tc;
	} else {
		tokenConfigurerWindows.get(cell.getId()).setVisible(false);
		tokenConfigurerWindows.get(cell.getId()).setVisible(true);
	}
	break;
default:
	break;

}

		
	}

	@Override
	public int getCapacityforPlace(String name, String color) {
		return getNetContainer().getPetriNet().getPlace(name).getColorCapacity(color);
	}

	@Override
	public void updatePlaceCapacity(String name, String color, int newCapacity) {
		IFNetPlace place = getNetContainer().getPetriNet().getPlace(name);
		if(newCapacity <= 0)
			place.removeColorCapacity(color);
		else
		place.setColorCapacity(color, newCapacity);			
	}

	@Override
	public void updateViews() {
		for(AbstractCPNTokenConfigurer w:tokenConfigurerWindows.values())
			w.updateView();
	}

	@Override
	public void updateTokenConfigurer(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Multiset<String> getConstraintforArc(String name) {
		return getNetContainer().getPetriNet().getFlowRelation(name).getConstraint();
		}


	public Set getAccessModeforTransition(String name, String color) {
		AbstractIFNetTransition<IFNetFlowRelation> transition = getNetContainer().getPetriNet().getTransition(name);
		if(transition instanceof AbstractRegularIFNetTransition)
		return	(Set) ((AbstractRegularIFNetTransition) transition).getAccessModes().get(color);
		else
			return new HashSet<AccessMode>();
	}

	public void updateAccessModeTransition(String name, String color, Set newAM) {
		AbstractIFNetTransition<IFNetFlowRelation> transition = getNetContainer().getPetriNet().getTransition(name);
		if(transition instanceof AbstractRegularIFNetTransition){
//			if(!newAM.isEmpty())
		((AbstractRegularIFNetTransition) transition).setAccessMode(color, newAM);
//			else
//				((AbstractRegularIFNetTransition) transition).removeAccessModes(color);
			 }
		
		
	}

	public void setCurrentAnalysisContext(AnalysisContext ac) {
		setContainsAnalysisContext(true);
		getNetContainer().getPetriNet().setAnalysisContext(ac);
		currentAnalysisContext = ac;
		refresh();
		
	}

	@Override
	protected void drawAdditionalTransitionGrahpics(mxGraphics2DCanvas canvas, mxCellState state) {
		PNGraphCell cell = (PNGraphCell) state.getCell();
		AbstractRegularIFNetTransition transistion = (AbstractRegularIFNetTransition) getNetContainer().getPetriNet().getTransition(cell.getId());

		Graphics g = canvas.getGraphics();
		Graphics2D g2 = (Graphics2D) g;
		int posX = (int) state.getX();
		int posY = (int) state.getY();
		int j = 0;
		int k = 0;
		int spacingY = 5;
		int spacingX = 3;
		
		for (String c : getNetContainer().getPetriNet().getTokenColors()) {
			Set<AccessMode> am = (Set<AccessMode>) transistion.getAccessModes(c);
			
			//Build String to get right PNG File
			if (!am.isEmpty()) {
				String imageString = "";
				if (am.contains(AccessMode.READ))
					imageString = "r";
				if (am.contains(AccessMode.WRITE))
					imageString = imageString + "w";
				if (am.contains(AccessMode.CREATE))
					imageString = imageString + "c";
				if (am.contains(AccessMode.DELETE))
					imageString = imageString + "d";
				Color color = getNetContainer().getPetriNetGraphics().getColors().get(c);
				ImageIcon imageIcon = null;

				try {
					imageIcon = IconFactory.getIcon(imageString);

				} catch (ParameterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (PropertyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				BufferedImage image = colorPNG(color, imageIcon);

				
				//Position and Draw PNG
				int a = (int) state.getX() + spacingX;
				int b = (int) state.getY() + spacingY;
				if (state.getWidth() >= spacingX + image.getWidth() * (1 + j) + (k * spacingX))
					a = a + (j * (image.getWidth() + spacingX));
				else {
					j = 0;
					k++;
				}

				if (state.getHeight() >= spacingY + image.getHeight() * (1 + k) + (k * spacingY)) {
					b = (int) b + (k * (image.getHeight() + spacingY));
					g2.drawImage(image, a, b, null);
					j++;
				} else {
					g2.setFont(new Font("TimesRoman", Font.PLAIN, 8));
					g2.drawString("...more", (int) state.getX() + 2, (int) (state.getY() + state.getHeight()) - 2);
				}
			}
		}

		if (isContainsAnalysisContext()) {

			if (currentAnalysisContext != null) {
				SecurityLevel l = currentAnalysisContext.getLabeling().getActivityClassification(cell.getId());
				int fontSize = 10;
				g.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));
				g.drawString(l.toString(), posX, posY);

				ImageIcon img = null;
				try {
					img = IconFactory.getIcon("user");

				} catch (ParameterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (PropertyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// ImageObserver observer;
				int x = (int) state.getCenterX() + (int) (state.getWidth() / 6);
				int y = (int) state.getCenterY();
				g.drawImage(img.getImage(), x, y, null);
				String subjectDescriptor = currentAnalysisContext.getSubjectDescriptor(cell.getId());
				String subjectString = "no subject descriptor";
				if (subjectDescriptor != null)
					subjectString = subjectDescriptor.toString();

				g.drawString(subjectString, x + img.getIconWidth(), y + img.getIconWidth());

			}

		}
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

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

	public boolean isContainsAnalysisContext() {
		return containsAnalysisContext;
	}

	public void setContainsAnalysisContext(boolean containsAnalysisContext) {
		this.containsAnalysisContext = containsAnalysisContext;
	}

	
	public SecurityLevel getCurrentTransitionLabeling(String name) {
		return getCurrentAnalysisContext().getLabeling().getActivityClassification(name);
	}

	
	public void updateTransitionLabeling(String name, SecurityLevel level) {
		getCurrentAnalysisContext().getLabeling().setActivityClassification(name, level);
	}
	
	public String getCurrentSubjectDescriptorForTransition(String name) {
		return getCurrentAnalysisContext().getSubjectDescriptor(name);
	}

	
	public void updateSubjectDescriptorForTransition(String activity, String subject) {
		if(subject !=null)
		getCurrentAnalysisContext().setSubjectDescriptor(activity, subject);
		else{
			AnalysisContext newAC = new AnalysisContext(getCurrentAnalysisContext().getLabeling());
			for(String a:getCurrentAnalysisContext().getActivities()){
				if(!a.contentEquals(activity) && getCurrentAnalysisContext().getSubjectDescriptor(a) != null)
				newAC.setSubjectDescriptor(activity, getCurrentAnalysisContext().getSubjectDescriptor(a));
			}
			setCurrentAnalysisContext(newAC);
			
			
		}
	}

	public void updateAnalysisContext(AnalysisContext ac) {
		setCurrentAnalysisContext(ac);
		
	}

	public SecurityLevel getSecurityLabelForTokenlabel(String label) {
		return getCurrentAnalysisContext().getLabeling().getAttributeClassification(label);
	}

	public void updateSecurityLabelForTokenlabel(String label, SecurityLevel level) {
//		if(!getCurrentAnalysisContext().getAttributes().contains(label)){
//			AnalysisContext ac = getCurrentAnalysisContext();
//
//			Set<String> attributes = getCurrentAnalysisContext().getAttributes();
//			
//			 HashMap<String, SecurityLevel> newAttributeMapping = new HashMap<String, SecurityLevel>();
//			for(String a:attributes){
//				newAttributeMapping.put(a, getCurrentAnalysisContext().getLabeling().getAttributeClassification(a));
//			}
//			newAttributeMapping.put(label, SecurityLevel.LOW);
//			getCurrentAnalysisContext().getLabeling().addAttributes(attributes);
//			Labeling labeling = new Labeling(ac.getLabeling().getActivities(), newAttributeMapping.keySet(), ac.getLabeling().getSubjects());
//			for(String a:newAttributeMapping.keySet()){
//				labeling.setAttributeClassification(a, newAttributeMapping.get(a));}
//			ac.setLabeling(labeling);
////			labeling.setAttributeClassification(tokenLabel, SecurityLevel.LOW);
//		}
		getCurrentAnalysisContext().getLabeling().setAttributeClassification(label, level);
		
	}

	public SecurityLevel getSecurityLevelForSubject(String subjectDescriptor) {
		return getCurrentAnalysisContext().getLabeling().getSubjectClearance(subjectDescriptor);
	}

	public void updateSecurityLevelForSubject(String subjectDescriptor, SecurityLevel level) {
		getCurrentAnalysisContext().getLabeling().setSubjectClearance(subjectDescriptor, level);		
	}

	@Override
	protected void drawAdditionalArcGrahpics(mxGraphics2DCanvas canvas, mxCellState state) {
		PNGraphCell cell = (PNGraphCell) state.getCell();
		String cellId = cell.getId();
		String hexColor = (String) state.getStyle().get(mxConstants.STYLE_STROKECOLOR);
		Color lineColor = Color.BLACK;;
		if(hexColor != null)
		lineColor  = Utils.parseColor(hexColor);
		AbstractCPNFlowRelation flowRelation = getNetContainer().getPetriNet().getFlowRelation(cellId);
		int pointDiameter = (int) (EditorProperties.getInstance().getDefaultTokenSize() * getView().getScale());
		 int posX = (int) state.getCenterX();
			int posY = (int) state.getCenterY();
		if (flowRelation.hasConstraints()) {
			Graphics g = canvas.getGraphics();
			int j = 0;
			int size = flowRelation.getConstraint().support().size();
			int startY = posY - (size/2*10)- (pointDiameter/2);
			int spacingY = pointDiameter+2;
			int spacingX = pointDiameter+2;
			for (String c : getNetContainer().getPetriNet().getTokenColors()) {
				int constraint = flowRelation.getConstraint(c);
				if(constraint<5){
				for (int i = 0; i < constraint; i++) {
					if (getNetContainer().getPetriNetGraphics().getColors().get(c) != null)
						g.setColor(getNetContainer().getPetriNetGraphics().getColors().get(c));
					g.fillOval(posX + (i * spacingX), startY + (j * spacingY), pointDiameter, pointDiameter);
					g.setColor(lineColor);
					g.drawOval(posX + (i * spacingX), startY + (j * spacingY), pointDiameter, pointDiameter);
				}
				}
				else {
					g.setColor(getNetContainer().getPetriNetGraphics().getColors().get(c));
					g.setFont(new Font("TimesRoman", Font.BOLD, 10)); 
					g.fillOval(posX , startY + (j * spacingY), pointDiameter, pointDiameter);
					g.setColor(lineColor);
					g.drawOval(posX, startY + (j * spacingY), pointDiameter, pointDiameter);
					g.setColor(Color.BLACK);
					 g.drawString(" : " +constraint +"\n", posX + pointDiameter , (startY+8) + (j * (spacingY)) );
				}
				if(constraint>0)
				j++;
			}
	        Graphics2D g2 = (Graphics2D) g;
	        g2.setRenderingHint (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		 
		 }
		
	}





}
