package de.uni.freiburg.iig.telematik.swat.jascha;

import java.util.LinkedList;
import java.util.List;

public class ResourceSet extends Resource {
	
	List<Resource> res = new LinkedList<>();
	String type ="resourceSet";
	
	public List<Resource> getRes() {
		return res;
	}

	public ResourceSet(String name, int amount) {
		this.name = name;
		for (int i = 0;i<amount;i++){
			res.add(new SimpleResource(name+"-"+i));
		}
	}

	@Override
	public boolean isAvailable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void use() {
		// TODO Auto-generated method stub

	}

	@Override
	public void unUse() {
		// TODO Auto-generated method stub

	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

}
