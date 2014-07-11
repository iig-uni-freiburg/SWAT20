package de.uni.freiburg.iig.telematik.swat.lukas;

public class AtomicProposition extends Statepredicate {

	
	private String mRelation;
	private String mPlaceName;
	private int mNumber;

	public AtomicProposition(String placeName, Relation r, int number) {
		try {
			setRelationSymbol(r);
		} catch (UnsupportedRelation e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mPlaceName = placeName;
		mNumber = number;
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
        default: throw new UnsupportedRelation("This relation isn't supported!");
                 }
	}
	
	@Override
	public String toString() {

		return "(" + mPlaceName + mRelation + mNumber + ")";
		
	}
	
}
