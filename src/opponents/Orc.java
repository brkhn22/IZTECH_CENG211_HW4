package opponents;

import characters.Human;

public class Orc extends Opponent {

	public Orc(int id) {
		super(id, OpponentType.ORC);
	}
	
	public void setAction() {
		// if not skip sets the action
		if(!isSkip())
			super.setAction();
		else   // if skip, skip will be set to false for next turn.
			setSkip(false);
		
		
	}
	
	@Override
	public String toSpecial() {
		return "heavy hit";
	}
	
	@Override
	public int specialAttack(double attackModifier, Human human) {
		// Special orc attack.
		int damage = (int)Math.round(getAttack()*attackModifier);
		// skips the next turn.
		setSkip(true);
		setSpecial(false);
		return human.getDamage(damage);
	}
}
