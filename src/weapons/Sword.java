package weapons;

public class Sword extends Weapon{
	// Sword Weapon
		
	public Sword() {
		super(WeaponType.SWORD);
	}

	public WeaponReturn firstAttack(int attack) {
		int combinedDamage = attack+getAdditionalAttack();
		return new WeaponReturn(combinedDamage);
	}
	
	public WeaponReturn secondAttack(int attack) {
		int totalDamage;
		if(Math.random() <= 0.75) {
			int combinedDamage = attack+getAdditionalAttack();
			totalDamage = combinedDamage*2;
			
		}else
			totalDamage = 0;
		return new WeaponReturn(totalDamage);
	}
	
	@Override
	public String[] getAttackNames() {
		return new String[]{"Slash", "Stab"};
	}
}