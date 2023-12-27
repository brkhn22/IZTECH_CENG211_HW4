package characters;

import opponents.Opponent;
import weapons.Weapon;

public class Hunter<W extends Weapon> extends Human<W>{
	// Hunter job that extends Human<W>
	private int specialTurnCounter;
	
	public Hunter(String name) {
		super(name, CharacterJob.HUNTER);
		this.specialTurnCounter = 0;
	}
	@Override
	public int specialAction(HumanAction humanAction, double attackModifier, Opponent opponent)
			throws SpecialAlreadyUsedException, InsufficientStaminaException {
		// method that returns hunter special action.
		int totalDamage = 0;
		if(!isSpecial()) 
			initSpecialAction();
		
		// get damage of the action.
		totalDamage = (int)Math.round(getDamageOfAction(humanAction));
		
		// if it is used for the first time
		if(specialTurnCounter == 0)
			totalDamage = (int)Math.round(attackModifier*totalDamage);
		else if(specialTurnCounter > 1) // if it is used for the more then second time.
			setSpecial(false);
		
		/*
		//	When spear thrown, dominated turns will not work consecutively.
		//	To make hunter attack even when spear would have thrown, then activate these lines
		if(isSkip() && isSpecial())
			setSkip(false);
		*/
		specialTurnCounter++;
		return opponent.getDamage(totalDamage);
	}

	
	@Override
	public void guard() {
		// call guard for getting reduced damage.
		super.guard();
		// implement the special action for guarding.
		if(isSpecial()) {
			if(specialTurnCounter > 1)
				setSpecial(false);
			specialTurnCounter++;
		}	
	}
	
	public int getSpecialTurnCounter() {
		return specialTurnCounter;
	}
}
