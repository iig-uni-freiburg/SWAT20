package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.util.HashMap;
import java.util.List;
/**
 * this interfaces defines all necessary methods to get Lists for every
 * import informations for a given petri net
 * @author bernhard
 *
 */
public interface PetriNetInformationReader extends InformationReader {
	public List<String> getDataTypesList();
	public List<String> getPlacesList();
	public String[] getPlacesArray();
	public String[] getDataTypesArray();
	public HashMap<String, String> getTransitionDictionary();
	public HashMap<String, String> getTransitionDictionaryReverse();
}
