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
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetTransition;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;

/**
 * this class reads all necessary information for the GUI from a petri net given
 * by the PNEditor
 * 
 * @author bernhard
 * 
 */
public class PetriNetInformation implements PetriNetInformationReader {

	private PNEditor pneditor;
	// dictionary that maps the labels of the transitions
	// to the real name in the net
	private HashMap<String, String> transitionLabelDic;
	private HashMap<String, String> transitionLabelDicReverse;
	private List<String> dataTypeList;
	private List<String> dataTypeListWithBlack;
	private List<String> placesList;

	public PetriNetInformation(PNEditor pneditor) {
		super();
		this.pneditor = pneditor;
		transitionLabelDic = new HashMap<String, String>();
		transitionLabelDicReverse = new HashMap<String, String>();
		dataTypeList = new ArrayList<String>();
		dataTypeListWithBlack = new ArrayList<String>();
		dataTypeListWithBlack.add("black");
		placesList = new ArrayList<String>();
		netChanged();
	}

	public void netChanged() {
		updateTransitionLabelDic();
		updatePlacesList();
		if (pneditor.getNetContainer().getPetriNet().getNetType() == NetType.CWN
				|| pneditor.getNetContainer().getPetriNet().getNetType() == NetType.CPN
				|| pneditor.getNetContainer().getPetriNet().getNetType() == NetType.IFNet) {
			updateDataTypeList();
		}
	}

	private void updatePlacesList() {
		placesList.clear();
		for (AbstractPlace p : pneditor.getNetContainer().getPetriNet()
				.getPlaces()) {
			placesList.add(p.getName());
		}
		Collections.sort(placesList);
	}

	/**
	 * Helpfunction to get the List of all Labels of the current PN of editor
	 * 
	 * @param editor
	 * @return
	 */
	public void updateTransitionLabelDic() {
		transitionLabelDic.clear();
		for (AbstractTransition transition : pneditor.getNetContainer()
				.getPetriNet().getTransitions()) {
			transitionLabelDic.put(
					transition.getLabel() + " (" + transition.getName() + ")",
					transition.getName());
			transitionLabelDicReverse.put(transition.getName(),
					transition.getLabel() + " (" + transition.getName() + ")");
		}
	}

	/**
	 * update the list of colors
	 */
	public void updateDataTypeList() {
		AbstractGraphicalPN<?, ?, ?, ?, ?, ?, ?, ?, ?> pn = pneditor
				.getNetContainer();
		AbstractPetriNet apn = pn.getPetriNet();
		dataTypeList.clear();
		dataTypeListWithBlack.clear();
		Set<String> dataTypes = new HashSet<String>();


			Iterator it = apn.getTransitions().iterator();
			// Set<String> colors=Arrays.;
			// colors.addAll(arg0)
			while (it.hasNext()) {
				AbstractCPNTransition t = (AbstractCPNTransition) it.next();
				dataTypes.addAll(t.getConsumedColors());
				dataTypes.addAll(t.getProcessedColors());
				dataTypes.addAll(t.getProducedColors());
			}
			dataTypeListWithBlack.addAll(dataTypes);
			dataTypes.remove("black");
			dataTypeList.addAll(dataTypes);

		Collections.sort(dataTypeList);
		Collections.sort(dataTypeListWithBlack);
	}

	@Override
	public List<String> getDataTypesList() {
		// TODO Auto-generated method stub
		return dataTypeList;
	}

	@Override
	public List<String> getPlacesList() {
		// TODO Auto-generated method stub
		return placesList;
	}

	@Override
	public HashMap<String, String> getTransitionDictionary() {
		// TODO Auto-generated method stub
		return transitionLabelDic;
	}

	@Override
	public HashMap<String, String> getTransitionDictionaryReverse() {
		// TODO Auto-generated method stub
		return transitionLabelDicReverse;
	}

	@Override
	public List<String> getActivities() {
		// TODO Auto-generated method stub
		ArrayList<String> activities = new ArrayList<String>();
		activities.addAll(transitionLabelDic.keySet());
		Collections.sort(activities);
		return activities;
	}

	@Override
	public String[] getPlacesArray() {
		// TODO Auto-generated method stub
		List<String> places = placesList;
		return places.toArray(new String[places.size()]);
	}

	@Override
	public String[] getDataTypesArray() {
		// TODO Auto-generated method stub
		return dataTypeList.toArray(new String[dataTypeList.size()]);
	}

	@Override
	public String[] getActivitiesArray() {
		// TODO Auto-generated method stub
		List<String> activities = getActivities();
		return activities.toArray(new String[activities.size()]);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		netChanged();
	}

	@Override
	public String[] getRoleArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getDataTypesWithBlackArray() {
		// TODO Auto-generated method stub
		return dataTypeListWithBlack.toArray(new String[dataTypeListWithBlack
				.size()]);
	}

}
