package opponents;

import characters.Human;

public class Wolf extends Opponent{
	
	public Wolf(int id) {
		super(id, OpponentType.WOLF);
	}
	
	private Wolf(int id, int points, int attack, int speed) {
		super(id, OpponentType.WOLF);
	}
	
	public Wolf callFriend(int nextId) {
		// 20% success chance for this special power.
		if(Math.random() <= 0.2) 
			return new Wolf(nextId, this.getPoints(), this.getAttack(), this.getSpeed());
		
		
		return null;
	}

	@Override
	public String toSpecial() {
		return "call friend";
	}

	@Override
	public int specialAttack(double attackModifier, Human human) {
		// wolf has no special attack.
		throw new UnsupportedOperationException();
	}
}
