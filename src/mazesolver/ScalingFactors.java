package mazesolver;

public class ScalingFactors {
	
	int ascendingFactor;
	int descendingFactor;
	
	/**
	 * ScalingFactors constructor. Simple class to hold
	 * the ascending and descending factors chosen by the user.
	 */
	public ScalingFactors(int ascending, int descending) {
		ascendingFactor = ascending;
		descendingFactor = descending;
	}
	
	public int getAscendingFactor() {
		return ascendingFactor;
	}
	
	public void setAscendingFactor(int factor) {
		ascendingFactor = factor;
	}
	
	public int getDescendingFactor() {
		return descendingFactor;
	}
	
	public void setDescendingFactor(int factor) {
		descendingFactor = factor;
	}
	
}