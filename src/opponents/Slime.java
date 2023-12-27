package opponents;

import characters.Human;

public class Slime extends Opponent {

	public Slime(int id) {
		super(id, OpponentType.SLIME);
	}
	
	protected void setPoints(int points) {
		if(points > 150)
			super.setPoints(150);
		else
			super.setPoints(points);
	}

	@Override
	public String toSpecial() {
		return "absorb";
	}

	@Override
	public int specialAttack(double attackModifier, Human human) {
		// Special slime attack.
		int damage = (int)Math.round(getAttack()*attackModifier);
		int dealedDamage = human.getDamage(damage);
		// increase the points with dealed damage to human.
		setPoints(getPoints()+dealedDamage);
		setSpecial(false);
		return dealedDamage;
	}

}
