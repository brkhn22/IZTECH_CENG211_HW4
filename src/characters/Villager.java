package characters;

import opponents.Opponent;
import weapons.Weapon;

public class Villager<W extends Weapon> extends Human<W> {

	public Villager(String name) {
		super(name, CharacterJob.VILLAGER);
	}
	
	@Override
	public int specialAction(HumanAction humanAction, double attackModifier, Opponent opponent)
			throws SpecialAlreadyUsedException, InsufficientStaminaException {
		// Villager has no special action.
		throw new UnsupportedOperationException();
	}
}
