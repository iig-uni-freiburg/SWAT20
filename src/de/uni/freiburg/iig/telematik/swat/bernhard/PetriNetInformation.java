package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.util.ArrayList;
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
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.abstr.AbstractCPNTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.abstr.AbstractIFNetTransition;
import de.uni.freiburg.iig.telematik.swat.editor.PNEditor;

public class PetriNetInformation implements PetriNetInformationReader {

	private PNEditor pneditor;
	// dictionary that maps the labels of the transitions
	// to the real name in the net
	private HashMap<String, String> transitionLabelDic;
	private List<String> dataTypeList;
	private List<String> placesList;
	private List<String> subjectList;

	public PetriNetInformation(PNEditor pneditor) {
		super();
		this.pneditor = pneditor;
		transitionLabelDic = new HashMap<String, String>();
		dataTypeList = new ArrayList<String>();
		placesList = new ArrayList<String>();
		subjectList = new ArrayList<String>();
		netChanged();
	}

	public void netChanged() {
		updateTransitionLabelDic();
		updatePlacesList();
		if (pneditor.getNetContainer().getPetriNet().getNetType() == NetType.IFNet) {
			updateDataTypeList();
			updateSubjectList();
		}
	}

	private void updateSubjectList() {
		// TODO Auto-generated method stub
		
	}

	private void updatePlacesList() {
		placesList.clear();
		for (AbstractPlace p : pneditor.getNetContainer()
				.getPetriNet().getPlaces()) {
			placesList.add(p.getName());
		}
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
			transitionLabelDic
					.put(transition.getLabel()+" ("+transition.getName()+")", transition.getName());
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
		Set<String> dataTypes = new HashSet<String>();
		if (apn.getNetType() == NetType.IFNet) {
			IFNet net = (IFNet) apn;
			Iterator it = net.getTransitions().iterator();
			// Set<String> colors=Arrays.;
			// colors.addAll(arg0)
			while (it.hasNext()) {
				AbstractCPNTransition t = (AbstractCPNTransition) it.next();
				dataTypes.addAll(t.getConsumedColors());
				dataTypes.addAll(t.getProcessedColors());
				dataTypes.addAll(t.getProducedColors());
			}
			dataTypeList.addAll(dataTypes);
		}
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
	public List<String> getSubjectList() {
		// TODO Auto-generated method stub
		return subjectList;
	}
	

}
