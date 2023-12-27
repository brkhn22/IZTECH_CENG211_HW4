package weapons;

public class Bow extends Weapon {
	// Bow Weapon
	
	public Bow() {
		super(WeaponType.BOW);
	}

	public WeaponReturn firstAttack(int attack) {
		int combinedDamage = attack+getAdditionalAttack();
		double totalDamage = combinedDamage*0.8;
		return new WeaponReturn(totalDamage);
	}
	
	public WeaponReturn secondAttack(int attack) {
		int combinedDamage = attack+getAdditionalAttack();
		double totalDamage = combinedDamage*2.5;
		return new WeaponReturn(totalDamage);
	}
	
	@Override
	public String[] getAttackNames() {
		return new String[]{"Single Arrow Shoot", "Double Arrow Shoot"};
	}
	
}