package characters;

import opponents.Opponent;
import weapons.Weapon;

public class Squire<W extends Weapon> extends Human<W> {

	public Squire(String name) {
		super(name, CharacterJob.SQUIRE);
	}

	@Override
	public int specialAction(HumanAction humanAction, double attackModifier, Opponent opponent)
			throws SpecialAlreadyUsedException, InsufficientStaminaException {
		initSpecialAction();
		
		int totalDamage = (int)Math.round(getDamageOfAction(humanAction)); // this method decreases stamina
		// set stamina to 10
		if(getStamina() < 10)
			setStamina(10);
		// set special false for there is no further special actions 
		setSpecial(false);
		return opponent.getDamage((int)Math.round(totalDamage*attackModifier));
	}
	
	@Override
	public String toSpecial() {
		String str = getJob().toString();
		return str+" Special (10 stamina)";
	}
}
