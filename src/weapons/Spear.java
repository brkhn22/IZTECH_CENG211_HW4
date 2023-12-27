package weapons;

public class Spear extends Weapon{
	// Spear Weapon
	
	public Spear() {
		super(WeaponType.SPEAR);
	}

	public WeaponReturn firstAttack(int attack) {
		int combinedDamage = attack + getAdditionalAttack();
		double totalDamage = combinedDamage * 1.1;
		return new WeaponReturn(totalDamage);
	}
	
	public WeaponReturn secondAttack(int attack) {
		int combinedDamage = attack + getAdditionalAttack();
		double totalDamage = combinedDamage * 2;
		return new WeaponReturn(totalDamage, true);
	}
	
	@Override
	public String[] getAttackNames() {
		return new String[]{"Stab", "Throw"};
	}
}