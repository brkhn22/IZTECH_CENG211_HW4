package abstractcharacter;

public abstract class AbstractCharacter{
	// Abstract Character that will be parent of the all characters in the game.
	
	private int points, attack, speed;
	
	public AbstractCharacter(int points, int attack, int speed) {
		this.points = points;
		this.attack = attack;
		this.speed = speed;
	}
	
	public int getPoints() {
		return points;
	}
	
	protected void setPoints(int points) {
		this.points = points;
	}

	public int getAttack() {
		return attack;
	}

	public int getSpeed() {
		return speed;
	}
	
	public boolean isAlive() {
		return this.points > 0;
	}
	
	public abstract String toFullCharacterInfo();
	public abstract String toName();
	public abstract String toSpecial();
}
