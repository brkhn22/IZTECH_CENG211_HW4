package weapons;

public abstract class Weapon {
	// Abstract Weapon class.
	
	private int additionalAttack;
	private WeaponType weaponType;
	public Weapon(WeaponType weaponType) {
		this.weaponType = weaponType;
		
		// gives values between (10, 20)
		this.additionalAttack = ((int)Math.round(Math.random()*10))+10;
		
	}
	
	public int getAdditionalAttack() {
		return additionalAttack;
	}
	
	public WeaponType getWeaponType() {
		return this.weaponType;
	}
	
	// These methods will be implemented in derived classes.
	public abstract WeaponReturn firstAttack(int attack);
	public abstract WeaponReturn secondAttack(int attack);
	public abstract String[] getAttackNames();
}
