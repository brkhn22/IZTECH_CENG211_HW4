package characters;

import opponents.Opponent;
import weapons.Weapon;

public interface Character<W> {
	
	// Character interface with weapon will be implemented by the class for users.
	public int punch(Opponent opponent) throws InsufficientStaminaException;
	@SuppressWarnings("hiding")
	public <W extends Weapon> int attackWithWeapon(W weapon, HumanAction humanAction, Opponent opponent)throws InsufficientStaminaException;
	public void guard();
	public void run() throws CharacterRunsException;
	public abstract int specialAction(HumanAction humanAction, double attackModifier, Opponent opponent) throws SpecialAlreadyUsedException, InsufficientStaminaException;
}
