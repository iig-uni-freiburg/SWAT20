package de.uni.freiburg.iig.telematik.swat.bernhard;

import java.util.HashMap;
import java.util.List;
/**
 * this interfaces defines all necessary methods to get Lists for every
 * import informations for a given petri net
 * @author bernhard
 *
 */
public interface PNReader extends LogFileReader {
	public String[] getPlacesArray();
	public String[] getDataTypesArray();
	public String[] getDataTypesWithBlackArray();
	public HashMap<String, String> getTransitionToLabelDictionary();
	public HashMap<String, String> getLabelToTransitionDictionary();
	public HashMap<String, String> getPlacesToLabelDictionary();
	public HashMap<String, String> getLabelToPlaceDictionary();
}