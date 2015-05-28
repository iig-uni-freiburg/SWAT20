package de.uni.freiburg.iig.telematik.swat.editor.graph;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;

import javax.swing.ImageIcon;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.view.mxCellState;

import de.invation.code.toval.properties.PropertyException;
import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.graphic.GraphicalIFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.AnalysisContext;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.concepts.SecurityLevel;
import de.uni.freiburg.iig.telematik.swat.icons.IconFactory;
import de.uni.freiburg.iig.telematik.wolfgang.graph.IFNetGraph;
import de.uni.freiburg.iig.telematik.wolfgang.graph.PNGraphCell;
import de.uni.freiburg.iig.telematik.wolfgang.properties.IFNetProperties;

public class SwatIFNetGraph extends IFNetGraph {

	private AnalysisContext currentAnalysisContext;

	public SwatIFNetGraph(GraphicalIFNet GraphicalIFNet, IFNetProperties IFNetProperties) throws ParameterException {
		super(GraphicalIFNet, IFNetProperties);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void drawAdditionalContextToTransition(mxGraphics2DCanvas canvas, mxCellState state) throws PropertyException, IOException {
		PNGraphCell cell = (PNGraphCell) state.getCell();
		AbstractIFNetTransition transition = (AbstractIFNetTransition) getNetContainer().getPetriNet().getTransition(cell.getId());

		Graphics g = canvas.getGraphics();
		Graphics2D g2 = (Graphics2D) g;
		int posX = (int) state.getX();
		int posY = (int) state.getY();
		int j = 0;
		int k = 0;
		int spacingY = 5;
		int spacingX = 3;

		if (getCurrentAnalysisContext() != null) {

			if (currentAnalysisContext != null) {
				SecurityLevel l = currentAnalysisContext.getLabeling().getActivityClassification(cell.getId());
				int fontSize = 10;
				g.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));
				g.drawString(l.toString(), posX, posY);

				ImageIcon img = null;

				img = IconFactory.getIcon("user");

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
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		}

	}

	public void updateAnalysisContext(AnalysisContext ac) {
		if (ac != null)
			getNetContainer().getPetriNet().setAnalysisContext(ac);
		else
			getNetContainer().getPetriNet().removeAnalysisContext();
		setCurrentAnalysisContext(ac);
		refresh();

	}

	public AnalysisContext getCurrentAnalysisContext() {
		return currentAnalysisContext;
	}

	public void setCurrentAnalysisContext(AnalysisContext currentAnalysisContext) {
		this.currentAnalysisContext = currentAnalysisContext;
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
		if (subject != null)
			getCurrentAnalysisContext().setSubjectDescriptor(activity, subject);
		else {
			AnalysisContext newAC = new AnalysisContext(getCurrentAnalysisContext().getACModel(), false);
			newAC.setLabeling(getCurrentAnalysisContext().getLabeling());
			for (String a : getCurrentAnalysisContext().getACModel().getContext().getActivities()) {
				if (!a.contentEquals(activity) && getCurrentAnalysisContext().getSubjectDescriptor(a) != null)
					newAC.setSubjectDescriptor(activity, getCurrentAnalysisContext().getSubjectDescriptor(a));
			}
			updateAnalysisContext(newAC);
		}
	}

	public SecurityLevel getSecurityLabelForTokenlabel(String label) {
		return getCurrentAnalysisContext().getLabeling().getAttributeClassification(label);
	}

	public void updateSecurityLabelForTokenlabel(String label, SecurityLevel level) {
		// if(!getCurrentAnalysisContext().getAttributes().contains(label)){
		// AnalysisContext ac = getCurrentAnalysisContext();
		//
		// Set<String> attributes = getCurrentAnalysisContext().getAttributes();
		//
		// HashMap<String, SecurityLevel> newAttributeMapping = new
		// HashMap<String, SecurityLevel>();
		// for(String a:attributes){
		// newAttributeMapping.put(a,
		// getCurrentAnalysisContext().getLabeling().getAttributeClassification(a));
		// }
		// newAttributeMapping.put(label, SecurityLevel.LOW);
		// getCurrentAnalysisContext().getLabeling().addAttributes(attributes);
		// Labeling labeling = new Labeling(ac.getLabeling().getActivities(),
		// newAttributeMapping.keySet(), ac.getLabeling().getSubjects());
		// for(String a:newAttributeMapping.keySet()){
		// labeling.setAttributeClassification(a, newAttributeMapping.get(a));}
		// ac.setLabeling(labeling);
		// // labeling.setAttributeClassification(tokenLabel,
		// SecurityLevel.LOW);
		// }
		getCurrentAnalysisContext().getLabeling().setAttributeClassification(label, level);

	}

	public SecurityLevel getSecurityLevelForSubject(String subjectDescriptor) {
		return getCurrentAnalysisContext().getLabeling().getSubjectClearance(subjectDescriptor);
	}

	public void updateSecurityLevelForSubject(String subjectDescriptor, SecurityLevel level) {
		getCurrentAnalysisContext().getLabeling().setSubjectClearance(subjectDescriptor, level);
	}

}
