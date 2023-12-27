package weapons;

public class WeaponReturn {
	// Weapon Return class to return weapon special values.
	
	// combined attack must be double value,
	// for multiplications work properly.
	private double combinedAttack;
	private boolean isSkip;
	
	public WeaponReturn(double combinedAttack, boolean isSkip) {
		this.combinedAttack = combinedAttack;
		this.isSkip = isSkip;
	}
	
	public WeaponReturn(double combinedAttack) {
		this(combinedAttack, false);
	}
	
	public double getCombinedAttack() {
		return combinedAttack;
	}
	
	public boolean isSkip() {
		return isSkip;
	}
}
