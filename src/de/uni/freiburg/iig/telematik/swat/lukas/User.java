package de.uni.freiburg.iig.telematik.swat.lukas;

public class User extends Operand {

	private String mUserName;
	
	public User(String userName) {
		mUserName = userName;
	}

	@Override
	public String toString() {
		return mUserName;
	}

	@Override
	public String getName() {
		return mUserName;
	}

}
