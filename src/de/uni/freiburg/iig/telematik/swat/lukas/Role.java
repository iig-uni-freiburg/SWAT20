package de.uni.freiburg.iig.telematik.swat.lukas;

public class Role extends Operand {
	
	private String mRolename;

	public Role(String roleName) {
		mRolename = roleName;
	}
	
	public String toString() {
		return mRolename;
	}
	

}
