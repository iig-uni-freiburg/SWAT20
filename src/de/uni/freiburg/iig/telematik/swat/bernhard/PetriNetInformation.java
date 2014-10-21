package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.invation.code.toval.types.Multiset;
import de.uni.freiburg.iig.telematik.sepia.graphic.AbstractGraphicalPN;
import de.uni.freiburg.iig.telematik.sepia.mg.ifnet.IFNetMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.ifnet.IFNetMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPNNode;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.NetType;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.CPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPN;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetTransition;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.ACModel;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.acl.ACLModel;
import de.uni.freiburg.iig.telematik.seram.accesscontrol.rbac.RBACModel;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;
import de.uni.freiburg.iig.telematik.swat.workbench.SwatComponents;

/**
 * this class reads all necessary information from a petri net given
 * by the PNEditor
 * 
 * @author bernhard
 * 
 */
public class PetriNetInformation implements PNReader {

	private PNEditor pneditor;
	// dictionary that maps the labels of the transitions
	// to the real name in the net
	
	// TransitionName -> Label
	private HashMap<String, String> transitionToLabelDic;
	// Label->TransitionName
	private HashMap<String, String> labelToTransitionDic;
	// Place -> Label
	private HashMap<String, String> placesLabelDic;
	// Label->Place
	private HashMap<String, String> placesLabelDicReverse;
	private List<String> dataTypeList;
	private List<String> dataTypeListWithBlack;
	private List<String> subjectList;

	/**
	 * Create an PetriNetInformation object for a given PNEditor
	 * @param pneditor
	 */
	public PetriNetInformation(PNEditor pneditor) {
		super();
		this.pneditor = pneditor;
		transitionToLabelDic = new HashMap<String, String>();
		labelToTransitionDic = new HashMap<String, String>();
		placesLabelDic = new HashMap<String, String>();
		placesLabelDicReverse = new HashMap<String, String>();
		dataTypeList = new ArrayList<String>();
		dataTypeListWithBlack = new ArrayList<String>();
		dataTypeListWithBlack.add("black");
		subjectList=new ArrayList<String>();
		netChanged();
	}
	/**
	 * the net has been changed so update the lists
	 */
	public void netChanged() {
		updateTransitionLabelDic();
		updatePlacesList();
		if (pneditor.getNetContainer().getPetriNet().getNetType() == NetType.CPN
				|| pneditor.getNetContainer().getPetriNet().getNetType() == NetType.IFNet) {
			updateDataTypeList();
		}
		if(pneditor.getNetContainer().getPetriNet().getNetType() == NetType.IFNet) {
			updateSubjectList();
		}
	}
	/**
	 * update the list of subjects
	 */
	private void updateSubjectList() {
		// TODO Auto-generated method stub
		subjectList.clear();
		try {
		if (SwatComponents.getInstance().containsACModels()) {
			ACModel acModel = SwatComponents.getInstance().getSelectedACModel();
			if (acModel != null) {
				System.out.println(acModel.getClass());

				Set<String> ifSubjects = new HashSet<String>();
				if (acModel instanceof ACLModel) {
					ifSubjects = acModel.getSubjects();
				}
				if (acModel instanceof RBACModel) {
					ifSubjects = ((RBACModel) acModel).getRoles();
				}
				subjectList.addAll(ifSubjects);
			}
				
		}
		}catch(Exception e) {
			
		}
	}
	/**
	 * update the Label dictionary for places
	 */
	private void updatePlacesList() {
		this.placesLabelDic.clear();
		this.placesLabelDicReverse.clear();
		for (AbstractPlace p : pneditor.getNetContainer().getPetriNet()
				.getPlaces()) {

			this.placesLabelDic.put(p.getName(), p.getLabel());
			this.placesLabelDicReverse.put(p.getLabel(), p.getName());
		}

	}

	/**
	 * update the Label dictionary for transitions 
	 */
	private void updateTransitionLabelDic() {
		transitionToLabelDic.clear();
		labelToTransitionDic.clear();
		for (AbstractTransition transition : pneditor.getNetContainer()
				.getPetriNet().getTransitions()) {
			labelToTransitionDic.put(
					transition.getLabel() + " (" + transition.getName() + ")",
					transition.getName());
			transitionToLabelDic.put(transition.getName(),
					transition.getLabel() + " (" + transition.getName() + ")");
		}
	}

	/**
	 * update the list of colors
	 */
	public void updateDataTypeList() {
		AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?, ?, ?> pn = pneditor
				.getNetContainer();
		AbstractCPN apn = (AbstractCPN) pn.getPetriNet();
		dataTypeList.clear();
		dataTypeListWithBlack.clear();
		dataTypeListWithBlack.addAll(apn.getTokenColors());
		dataTypeList.addAll(apn.getTokenColors());
		dataTypeList.remove("black");

		Collections.sort(dataTypeList);
		Collections.sort(dataTypeListWithBlack);
	}

	@Override
	public HashMap<String, String> getTransitionToLabelDictionary() {
		// TODO Auto-generated method stub
		return transitionToLabelDic;
	}

	@Override
	public HashMap<String, String> getLabelToTransitionDictionary() {
		// TODO Auto-generated method stub
		return labelToTransitionDic;
	}

	@Override
	public String[] getPlacesArray() {
		// TODO Auto-generated method stub
		List<String> places = new ArrayList<String>(this.placesLabelDicReverse.keySet());
		Collections.sort(places);
		return places.toArray(new String[places.size()]);
	}

	@Override
	public String[] getDataTypesArray() {
		// TODO Auto-generated method stub
		return dataTypeList.toArray(new String[dataTypeList.size()]);
	}

	@Override
	public String[] getActivities() {
		List<String> activities = new ArrayList<String>(this.labelToTransitionDic.keySet());
		Collections.sort(activities);
		return activities .toArray(new String[activities.size()]);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		netChanged();
	}

	@Override
	public String[] getRoles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getDataTypesWithBlackArray() {
		// TODO Auto-generated method stub
		return dataTypeListWithBlack.toArray(new String[dataTypeListWithBlack
				.size()]);
	}

	@Override
	public HashMap<String, String> getPlacesToLabelDictionary() {
		// TODO Auto-generated method stub
		return this.placesLabelDic;
	}

	@Override
	public HashMap<String, String> getLabelToPlaceDictionary() {
		// TODO Auto-generated method stub
		return this.placesLabelDicReverse;
	}

	@Override
	public String[] getSubjects() {
		// TODO Auto-generated method stub
		return null;
	}

}
