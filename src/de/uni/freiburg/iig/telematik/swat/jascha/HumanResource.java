package de.uni.freiburg.iig.telematik.swat.jascha;

public class HumanResource extends SimpleResource {

	public HumanResource(String name) {
		super(name);
		type = ResourceType.HUMAN;
	}

	public HumanResource(String name, boolean fromSet) {
		super(name, fromSet);
		type = ResourceType.HUMAN;
	}

	public HumanResource(String name, ResourceStore resourceStore) {
		super(name, resourceStore);
		type = ResourceType.HUMAN;
	}

}
