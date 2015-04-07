package de.uni.freiburg.iig.telematik.swat.lukas.operands;

public class User extends PatternParameter {

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
