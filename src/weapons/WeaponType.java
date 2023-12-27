package weapons;

public enum WeaponType {
	// Weapon Type enum list with stamina information which operations consume.
	
	BOW(1, 3), SPEAR(2, 2), SWORD(2, 2); 
	
	private final int firstAttackStamina, secondAttackStamina;
	
	private WeaponType(int first, int second) {
		this.firstAttackStamina = first;
		this.secondAttackStamina = second;
	}
	
	public int getfirstStamina() {
		return firstAttackStamina;
	}
	public int getSecondStamina() {
		return secondAttackStamina;
	}
}
