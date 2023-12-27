package characters;

import opponents.Opponent;
import weapons.*;

public class Knight<W extends Weapon> extends Human<W> {

	public Knight(String name) {
		super(name, CharacterJob.KNIGHT);
	}

	@Override
	public int specialAction(HumanAction humanAction, double attackModifier, Opponent opponent)
			throws SpecialAlreadyUsedException, InsufficientStaminaException {
		int totalDamage = 0;
		// when special action used for the second time, this method should be called. 
		if(!isSpecial()) { 
			initSpecialAction();
		}
		totalDamage = (int)Math.round(getDamageOfAction(humanAction)*attackModifier);
		
		return opponent.getDamage(totalDamage);
	}
	
	public void activateSpecial(HumanAction humanAction) throws SpecialAlreadyUsedException {
		// activates the special action
		// when special action used for the first time, this method should be called. 
		if(humanAction == HumanAction.HUMAN_SPECIAL_ACTION)
			initSpecialAction();
	}
}
