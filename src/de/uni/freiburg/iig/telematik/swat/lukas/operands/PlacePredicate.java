package de.uni.freiburg.iig.telematik.swat.lukas.operands;


public class PlacePredicate extends StateExpression {

	
	private String mRelation;
	private String mPlaceId;
	private String mPlaceName;
	private int mNumber;

	public PlacePredicate(String placeName, Relation r, int number) {
		try {
			setRelationSymbol(r);
		} catch (UnsupportedRelation e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mPlaceId = placeName;
		mNumber = number;
		mPlaceName = placeName;
	}

	private void setRelationSymbol(Relation r) throws UnsupportedRelation {
		switch (r) {
        case EQUALS:  mRelation = "=";
                 break;
        case SMALLER:  mRelation = "<";
                 break;
        case GREATER:  mRelation = ">";
                 break;
        case SMALLER_EQUAL:  mRelation = "<=";
                 break;
        case GREATER_EQUAL:  mRelation = ">=";
                 break;
        case NOT_EQUAL: mRelation = "!=";
        default: throw new UnsupportedRelation("This relation isn't supported!");
                 }
	}
	
	@Override
	public String toString() {

		return "(" + mPlaceId + mRelation + mNumber + ")";
		
	}

	@Override
	public String getNegation() {
		String relation = negate();
		return "(" + mPlaceId + relation + mNumber + ")";
	}

	private String negate() {
		
		String relation = "";
		
		if (mRelation.equals(">")) {
			relation = "<=";
		} else if (mRelation.equals("<")) {
			relation = ">=";
		} else if (mRelation.equals("=")) {
			relation = "!=";
		} else if (mRelation.equals("<=")) {
			relation = ">";
		} else if (mRelation.equals(">=")) {
			relation = "<";
		} else if (mRelation.equals("!=")) {
			relation = "=";
		}
		
		return relation;
	}

	@Override
	public String getName() {
		return mPlaceName;
	}
	
}
