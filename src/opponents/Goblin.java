package opponents;

import characters.Human;

public class Goblin extends Opponent {
	// Goblin Opponent.
	private int specialCounter;
	
	public Goblin(int id) {
		super(id, OpponentType.GOBLIN);
		specialCounter = 0;
		
	}

	@Override
	public String toSpecial() {
		return "rushing attack";
	}

	@Override
	public int specialAttack(double attackModifier, Human human) {
		// special goblin attack that counts the attacks.
		int damage = (int)Math.round(getAttack()*attackModifier);
		specialCounter++;
		// if this is the second time to use this special.
		if(specialCounter > 1) {
			setSpecial(false);
			specialCounter = 0;
		}
		return human.getDamage(damage);
	}
	
	public int getSpecialCounter() {
		return specialCounter;
	}
	
}
