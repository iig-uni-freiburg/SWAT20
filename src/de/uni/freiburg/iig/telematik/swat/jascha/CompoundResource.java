package de.uni.freiburg.iig.telematik.swat.jascha;

import java.util.ArrayList;
import java.util.List;

import com.sun.org.apache.xml.internal.security.keys.storage.StorageResolverException;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import de.uni.freiburg.iig.telematik.sepia.petrinet.timedNet.concepts.IResource;
import de.uni.freiburg.iig.telematik.swat.jascha.gui.actions.removeResourceAction;
import de.uni.freiburg.iig.telematik.swat.workbench.components.SwatComponents;

public class CompoundResource extends Resource {

	List<IResource> resources = new ArrayList<>();
	
	@XStreamOmitField
	private ResourceStore store;

	public CompoundResource(String name, List<IResource> elements, ResourceStore store){
		super(name);
		this.store=store;
		type = ResourceType.COMPOUND;
		for(IResource res:elements)
			resources.add(res);
	}
	
	public CompoundResource(String name){
		super(name);
		type = ResourceType.COMPOUND;
	}
	
	//Konstruktor, bei dem die Ressource gleich in ein ResourceStore eingetragen wird.
//	public CompoundResource(String name, ResourceStore resourceStore){
//		super(name);
//		resourceStore.addResource(this);
//		type = ResourceType.COMPOUND;
//	}

	@Override
	public boolean isAvailable() {
		
		if(isDisabled)
			return false;
		
		for (IResource r : resources) {
			if (!store.getResource(r).isAvailable())
				return false;
		}
		return true;
	}
	
	public List<IResource> getConsistingResources(){
		ArrayList<IResource> result = new ArrayList<>();
		for(IResource res:resources)
			result.add(store.getResource(res));
		return result;
	}

	public String getName() {
//		StringBuilder b = new StringBuilder();
//		for (IResource r : resources) {
//			b.append(r.getName());
//			b.append(", ");
//		}
//		String result = b.toString();
//		if(result!=null && result.length()>1)result = result.substring(0, result.length()-1); //remove ", ";
//		if(result==null || result.isEmpty())
//			return super.getName();
//		return super.getName()+": "+result;
		return super.getName();
	}
	
	
	public void addResource(IResource r){
		resources.add(r);
	}
	
	public void removeRessource(String res){
		IResource match=null;
		for (IResource r:resources)
			if (r.equals(res))
				match = r;
		if(match!=null)
			removeResource(match);
	}
	
	public void removeResource(String r){
		resources.remove(r);
	}
	
	public void removeResource(IResource r){
		resources.remove(r.getName());
	}

	@Override
	public void use() {
		//throw new RuntimeException("Not yet implemented");		
		for (IResource r: resources){
			store.getResource(r).use();
		}
	}

	@Override
	public void unUse() {
		//throw new RuntimeException("Not yet implemented");		
		for (IResource r: resources){
			store.getResource(r).unUse();
		}		
	}

	@Override
	public void reset() {
		for(IResource r:resources){
			if(r == null)
				System.out.println(getName()+": contains a null ressource!");
			store.getResource(r).reset();
		}
	}
	
	public String toString(){
		return "("+name+")";
	}
	

	/**Method used for linking the corresponding ResourceStore to this CompoundResource**/
	public void setStore(ResourceStore store) {
		this.store = store;
	}

}
