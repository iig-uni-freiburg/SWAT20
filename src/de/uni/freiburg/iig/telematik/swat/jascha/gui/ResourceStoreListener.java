package de.uni.freiburg.iig.telematik.swat.jascha.gui;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResource;

public interface ResourceStoreListener {
	
	public void resourceStoreElementAdded(IResource resource);
	
	public void informStoreElementRemoved(IResource resource);
	
	/**inform name of ResourceStore changed**/
	public void nameChanged(String newName);

}
